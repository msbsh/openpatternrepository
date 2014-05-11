/*
 * Copyright (c) 2005 - 2008 Aduna.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.mime.identifier.magic;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ontoware.rdf2go.model.node.URI;
import org.semanticdesktop.aperture.mime.identifier.MimeTypeIdentifier;
import org.semanticdesktop.aperture.util.ResourceUtil;
import org.semanticdesktop.aperture.util.UtfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Determines the MIME type of a binary resource based on magic number-based heuristics.
 */
public class MagicMimeTypeIdentifier implements MimeTypeIdentifier {

    private static final String MIME_TYPES_RESOURCE = "org/semanticdesktop/aperture/mime/identifier/magic/mimetypes.xml";

    private static final int PLAIN_TEXT_TEST_ARRAY_LENGTH = 100;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ArrayList mimeTypeDescriptions;

    private int minArrayLength;

    public MagicMimeTypeIdentifier() {
        this(MIME_TYPES_RESOURCE);
    }

    public MagicMimeTypeIdentifier(String definitionsResource) {
        readDescriptions(definitionsResource);
        setRequiringTypes();
        determineMinArrayLength();
    }

    private void readDescriptions(String definitionsResource) {
        // get the mimetypes.xml resource as an input stream
        InputStream stream = ResourceUtil.getInputStream(definitionsResource, MagicMimeTypeIdentifier.class);

        // setup a document builder
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setValidating(false);
        docBuilderFactory.setExpandEntityReferences(true);
        DocumentBuilder docBuilder;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            // this is an internal error rather than an extraction problem, hence the RuntimeException
            throw new RuntimeException("unable to instantiate DocumentBuilder", e);
        }

        // parse the document
        Document document;
        try {
            document = docBuilder.parse(stream);
        }
        catch (SAXException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // loop over all description elements
        Element documentRoot = document.getDocumentElement();
        NodeList descriptionElements = documentRoot.getElementsByTagName("description");

        int nrDescriptions = descriptionElements.getLength();
        mimeTypeDescriptions = new ArrayList(nrDescriptions);

        for (int i = 0; i < nrDescriptions; i++) {
            Element descriptionElement = (Element) descriptionElements.item(i);
            MimeTypeDescription description = createMimeTypeDescription(descriptionElement);
            if (description != null) {
                mimeTypeDescriptions.add(description);
            }
        }
    }

    private MimeTypeDescription createMimeTypeDescription(Element descriptionElement) {
        // initialize variables
        String mimeType = null;
        String parentType = null;
        ArrayList extensions = new ArrayList();
        ArrayList magicStrings = new ArrayList();
        ArrayList magicNumbers = new ArrayList();
        boolean allowsLeadingWhiteSpace = false;

        // extract info from the specified element
        NodeList childNodes = descriptionElement.getChildNodes();
        int nrNodes = childNodes.getLength();
        for (int i = 0; i < nrNodes; i++) {
            // fetch the next element node
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            String tagName = childNode.getNodeName();

            // handle mimeType element
            if ("mimeType".equals(tagName)) {
                Node valueNode = childNode.getFirstChild();
                if (valueNode != null) {
                    mimeType = valueNode.getNodeValue().trim();
                }
            }

            // handle parentType element
            if ("parentType".equals(tagName)) {
                Node valueNode = childNode.getFirstChild();
                if (valueNode != null) {
                    parentType = valueNode.getNodeValue().trim();
                }
            }

            // handle extensions element
            else if ("extensions".equals(tagName)) {
                Node valueNode = childNode.getFirstChild();
                if (valueNode != null) {
                    String extensionsString = valueNode.getNodeValue().trim();
                    StringTokenizer tokenizer = new StringTokenizer(extensionsString, ", ", false);
                    while (tokenizer.hasMoreTokens()) {
                        extensions.add(tokenizer.nextToken().toLowerCase());
                    }
                }
            }

            // handle allowsLeadingWhiteSpace element
            else if ("allowsLeadingWhiteSpace".equals(tagName)) {
                Node valueNode = childNode.getFirstChild();
                if (valueNode != null) {
                    String text = valueNode.getNodeValue().trim();
                    allowsLeadingWhiteSpace = Boolean.parseBoolean(text);
                }
            }

            // handle magicNumber elements
            else if ("magicNumber".equals(tagName)) {
                createMagicNumber((Element) childNode, mimeType, magicNumbers);
            }

            // handle magicString elements
            else if ("magicString".equals(tagName)) {
                createMagicString((Element) childNode, mimeType, magicStrings, magicNumbers);
            }
        }

        // create the resulting MimeTypeDescription
        if (mimeType == null) {
            return null;
        }
        else {
            return new MimeTypeDescription(mimeType, parentType, extensions, magicStrings, magicNumbers,
                    allowsLeadingWhiteSpace);
        }
    }

    private void createMagicNumber(Element element, String mimeType, ArrayList magicNumbers) {
        byte[] magicBytes = null;

        // extract info from the specified element
        Node firstChild = element.getFirstChild();
        if (firstChild == null) {
            logger.warn("missing magicNumber content in " + mimeType + " description");
            return;
        }

        String numberString = firstChild.getNodeValue();
        String encodingString = element.getAttribute("encoding").trim();
        String offsetString = element.getAttribute("offset").trim();

        // interpret the offset string
        int offset = 0;
        if (offsetString.length() > 0) {
            try {
                offset = Integer.parseInt(offsetString);
            }
            catch (NumberFormatException e) {
                logger.warn("illegal offset: " + offsetString + ", ignoring magic number");
                return;
            }
        }

        // interpret the number string
        if ("string".equals(encodingString)) {
            // get the string as bytes, using UTF-8 decoding
            try {
                magicBytes = numberString.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                // clearly an internal error, Java should always support UTF-8
                throw new RuntimeException(e);
            }
        }
        else if ("hex".equals(encodingString)) {
            // remove all leading and trailing white space, as well as all spaces inbetween
            numberString = numberString.trim();
            numberString = numberString.replaceAll(" ", "");

            // convert the hex encoding to a byte array
            numberString = numberString.toLowerCase();

            int nrChars = numberString.length();
            magicBytes = new byte[nrChars / 2];
            int cumulative = 0;

            for (int j = 0; j < nrChars; j++) {
                // fetch the next character
                char c = numberString.charAt(j);

                // convert this hexadecimal char to its int representation
                int decimalValue = 0;
                if (c >= '0' && c <= '9') {
                    decimalValue = c - '0';
                }
                else if (c >= 'a' && c <= 'f') {
                    decimalValue = c - 'a' + 10;
                }
                else {
                    logger.warn("illegal hexadecimal char: " + c);
                    return;
                }

                // take the value of two consecutive hexadecimal chars together to form a byte
                if (j % 2 == 0) {
                    cumulative = 16 * decimalValue;
                }
                else {
                    cumulative += decimalValue;
                    magicBytes[j / 2] = (byte) cumulative;
                }
            }
        }
        else {
            logger.warn("unknown or empty encoding: " + encodingString);
            return;
        }

        // create the resulting MagicNumber
        magicNumbers.add(new MagicNumber(magicBytes, offset));
    }

    private void createMagicString(Element element, String mimeType, ArrayList magicStrings,
            ArrayList magicNumbers) {
        // extract info from the specified element
        Node firstChild = element.getFirstChild();
        if (firstChild == null) {
            logger.warn("missing magicString content in " + mimeType + " description");
            return;
        }
        String magicString = firstChild.getNodeValue();

        boolean caseSensitive = Boolean.parseBoolean(element.getAttribute("caseSensitive"));

        // register the magic string
        magicStrings.add(new MagicString(magicString.toCharArray(), caseSensitive));

        // register as magic bytes, to be used as a fallback
        try {
            // convert to bytes
            byte[] bytes = magicString.getBytes("UTF-8");

            // make sure it does not start with a BOM
            byte[] bom = UtfUtil.findMatchingBOM(bytes);
            if (bom != null) {
                int remainingLength = bytes.length - bom.length;
                byte[] newBytes = new byte[remainingLength];
                System.arraycopy(bytes, bom.length, newBytes, 0, remainingLength);
                bytes = newBytes;
            }

            // register the magic number
            magicNumbers.add(new MagicNumber(bytes, 0));
        }
        catch (UnsupportedEncodingException e) {
            // no support for UTF-8 is clearly an error: bail out immediately
            throw new RuntimeException(e);
        }
    }

    private void setRequiringTypes() {
        // create a mapping from MIME type to MimeTypeDescription
        HashMap descriptionMap = new HashMap();

        int nrDescriptions = mimeTypeDescriptions.size();
        for (int i = 0; i < nrDescriptions; i++) {
            MimeTypeDescription description = (MimeTypeDescription) mimeTypeDescriptions.get(i);
            descriptionMap.put(description.getMimeType(), description);
        }

        // loop over all MimeTypeDescriptions
        Iterator iterator = mimeTypeDescriptions.iterator();
        while (iterator.hasNext()) {
            MimeTypeDescription description = (MimeTypeDescription) iterator.next();

            // see if it has a parent type
            String parentType = description.getParentType();
            if (parentType != null) {
                // remove it from the list
                iterator.remove();

                // look up the the description of its parent type
                MimeTypeDescription parentDescription = (MimeTypeDescription) descriptionMap.get(parentType);

                // register it as a requiring type of this parent, or discard when the parent does not exist
                if (parentDescription == null) {
                    logger
                            .warn("unable to retrieve parent type description for "
                                    + description.getMimeType());
                }
                else {
                    parentDescription.addRequiringType(description);
                }
            }
        }
    }

    private void determineMinArrayLength() {
        minArrayLength = PLAIN_TEXT_TEST_ARRAY_LENGTH;

        // loop over all MimeTypeDescriptions
        int nrDescriptions = mimeTypeDescriptions.size();
        for (int i = 0; i < nrDescriptions; i++) {
            MimeTypeDescription description = (MimeTypeDescription) mimeTypeDescriptions.get(i);

            // loop over all its MagicNumbers
            ArrayList numbers = description.getMagicNumbers();
            int nrNumbers = numbers.size();
            for (int j = 0; j < nrNumbers; j++) {
                MagicNumber number = (MagicNumber) numbers.get(j);
                minArrayLength = Math.max(minArrayLength, number.getMinimumLength());
            }

            // loop over all its MagicStrings
            ArrayList strings = description.getMagicStrings();
            int nrStrings = strings.size();
            for (int j = 0; j < nrStrings; j++) {
                MagicString string = (MagicString) strings.get(j);
                // assuming a UTF-16 file, we'd need two bytes per char + 2 for the BOM
                minArrayLength = Math.max(minArrayLength, string.getMinimumLength() * 2 + 2);
            }
        }
    }

    protected ArrayList getMimeTypeDescriptions() {
        return mimeTypeDescriptions;
    }

    public String identify(byte[] firstBytes, String fileName, URI uri) {
        // see if the file is some kind of UTF file
        char[] firstChars = null;
        byte[] realBom = null;

        if (firstBytes != null) {
            realBom = UtfUtil.findMatchingBOM(firstBytes);

            // for the sake of trying to create a String out of chars, we assume UTF-8 when no BOM is found.
            // Still, we need the actual BOM later on.
            byte[] tmpBom;
            int bomLength;

            if (realBom == null) {
                tmpBom = UtfUtil.UTF8_BOM;
                bomLength = 0;
            }
            else {
                tmpBom = realBom;
                bomLength = tmpBom.length;
            }

            // remove the Byte Order Mark from firstBytes: improves String.toCharArray output and also
            // improved magic number testing later on, when magic string testing fails. Make sure that
            // contentLength is an even number (each char consists of two bytes).
            int contentLength = firstBytes.length - bomLength;
            if ((contentLength & 1) == 1) {
                contentLength--;
            }
            byte[] contentBytes = new byte[contentLength];
            System.arraycopy(firstBytes, bomLength, contentBytes, 0, contentLength);

            // create a character representation of the bytes
            String charset = UtfUtil.getCharsetName(tmpBom);
            if (charset != null) {
                try {
                    String string = new String(contentBytes, charset);
                    firstChars = string.toCharArray();

                }
                catch (UnsupportedEncodingException e) {
                    // ignore, just continue
                }
            }
        }

        // determine a file name extension that we can use as a fallback if type detection based on
        // content cannot be performed or is incomplete (most notably the MS Office file types)
        String extension = fileName;

        if (extension == null && uri != null) {
            extension = uri.toString();
            if (!extension.contains("!/") && !extension.contains("?/")) {
                // don't remove the query on a subcrawled uri
            	// the ?/ occurs in uris produced by some crawlers
            	// even though it is against the convention
                extension = removeFragment('?', extension);
                extension = removeFragment('#', extension);
            }
        }

        if (extension != null) {
            int lastDotIndex = extension.lastIndexOf('.');

            if (lastDotIndex > 0 && lastDotIndex < extension.length() - 1) {
                extension = extension.substring(lastDotIndex + 1);
            }

            extension = extension.toLowerCase();
        }

        // now traverse the MimeTypeDescription tree to find a matching MIME type
        String mimeType = identify(firstChars, firstBytes, extension, mimeTypeDescriptions);

        // if we could not find a matching description but a UTF BOM was found, the least we know is that it's
        // a textual format
        if (mimeType == null && realBom != null) {
            return "text/plain";
        }
        // if we didn't find a BOM but the first bytes look like readable characters (no control characters),
        // we report it as plain text anyway. This may not work for UTF-8 files without a BOM that use
        // characters outside the ASCII set.
        else if (mimeType == null && isReadableASCII(firstBytes)) {
            return "text/plain";
        }
        else {
            return mimeType;
        }
    }

    private String removeFragment(char separatorChar, String input) {
        String result = input;

        if (input != null) {
            int index = input.indexOf(separatorChar);
            if (index >= 0 && index < input.length() - 1) {
                return input.substring(0, index);
            }
        }

        return result;
    }

    protected String identify(char[] firstChars, byte[] firstBytes, String extension, ArrayList descriptions) {
        if (firstChars != null) {
            // loop over the specified list of descriptions
            int nrDescriptions = descriptions.size();
            for (int i = 0; i < nrDescriptions; i++) {
                MimeTypeDescription description = (MimeTypeDescription) descriptions.get(i);

                // see if this description has a matching magic string
                if (description.matches(firstChars)) {
                    // we found at least one matching mime type.
                    // see if it is overrules by any of the requiring mime type descriptions
                    ArrayList requiringTypes = description.getRequiringTypes();
                    String overrulingResult = identify(firstChars, firstBytes, extension, requiringTypes);
                    return overrulingResult == null ? description.getMimeType() : overrulingResult;
                }
            }
        }

        // loop over the specified list of descriptions
        if (firstBytes != null) {
            int nrDescriptions = descriptions.size();
            for (int i = 0; i < nrDescriptions; i++) {
                MimeTypeDescription description = (MimeTypeDescription) descriptions.get(i);

                // see if this description has a matching magic number
                if (description.matches(firstBytes)) {
                    // we found at least one matching mime type.
                    // see if it is overrules by any of the requiring mime type descriptions
                    ArrayList requiringTypes = description.getRequiringTypes();
                    String overrulingResult = identify(firstChars, firstBytes, extension, requiringTypes);
                    return overrulingResult == null ? description.getMimeType() : overrulingResult;
                }
            }
        }

        // no match based on magic number could be found, now try on file extension
        if (extension != null) {
            int nrDescriptions = descriptions.size();
            for (int i = 0; i < nrDescriptions; i++) {
                MimeTypeDescription description = (MimeTypeDescription) descriptions.get(i);
                if (description.containsExtension(extension)) {
                    return description.getMimeType();
                }
            }
        }

        // we couldn't find any matching mime types
        return null;
    }

    public int getMinArrayLength() {
        return minArrayLength;
    }

    /**
     * Utility method for listing all known file extensions of the specified MIME type.
     * 
     * @return A List of file name extensions known to be used for the specified MIME type, or null when the
     *         MIME type was not recognized.
     */
    public List getExtensionsFor(String mimeType) {
        return getExtensionsFor(mimeType, mimeTypeDescriptions);
    }

    private List getExtensionsFor(String mimeType, ArrayList descriptions) {
        // loop over the specified description
        int nrExtensions = descriptions.size();
        for (int i = 0; i < nrExtensions; i++) {
            MimeTypeDescription description = (MimeTypeDescription) descriptions.get(i);

            // see if this description matches the required MIME type
            if (description.getMimeType().equals(mimeType)) {
                return description.getExtensions();
            }

            // it doesn't, repeat recursively on dependent descriptions
            List result = getExtensionsFor(mimeType, description.getRequiringTypes());
            if (result != null) {
                return result;
            }
        }

        // the MIME type was not found in this branch
        return null;
    }

    /**
     * Determines whether the specified bytes appear to be readable ASCII characters.
     */
    private boolean isReadableASCII(byte[] bytes) {
        if (bytes == null) {
            return false;
        }

        for (int i = 0; i < bytes.length; i++) {
            if (!isReadableASCII(bytes[i])) {
                return false;
            }
        }

        return true;
    }

    private boolean isReadableASCII(int b) {
        return b == 9 || b == 10 || b == 13 || (b >= 32 && b <= 126);
    }
}
