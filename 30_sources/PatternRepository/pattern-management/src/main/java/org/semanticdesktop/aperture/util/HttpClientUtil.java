/*
 * Copyright (c) 2005 - 2008 Aduna.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

/**
 * Utility methods for clients dealing with HTTP communication.
 */
public class HttpClientUtil {

    /**
     * Encodes a string according to RFC 3986 : Uniform Resource locators (URL). According to this spec,
     * any characters outside the range 0x20 - 0x7E must be escaped because they are not printable
     * characters. Within the range a number of characters are deemed unsafe or are marked as reserved.
     * In short: According to the spec only the alphanumerics and the special characters from -._~,
     * can be left unencoded. 
     * 
     * @param s The String to encode.
     * @param buffer The buffer to store the encoded String in.
     */
    public static void formUrlEncode(String s, StringBuilder buffer) {
        formUrlEncode(s, buffer, null);
    }

    /**
     * Encodes a string according to RFC 3986 : Uniform Resource locators (URL). According to this spec,
     * any characters outside the range 0x20 - 0x7E must be escaped because they are not printable
     * characters. Within the range a number of characters are deemed unsafe or are marked as reserved.
     * In short: According to the spec only the alphanumerics and the special characters from -._~,
     * can be left unencoded. 
     * 
     * @param s The String to encode.
     * @return An encoded version of the specified String.
     */
    public static String formUrlEncode(String s) {
        StringBuilder result = new StringBuilder(s.length() + 10);
        formUrlEncode(s, result,null);
        return result.toString();
    }
    
    /**
     * Does the same as HttpClientUtil.formUrlEncode (i.e. RFC 1738) except for some characters that are to be
     * left as they are.
     * 
     * @param string the string to be encoded
     * @param charsToLeave a string containing characters that will not be escaped. An example value is "/"
     *            useful for slashes, that are to be left alone in imap folder uris according to RFC 2192
     * @return the encoded folder name
     */
    public static String formUrlEncode(String string, String charsToLeave) {
        StringBuilder result = new StringBuilder(string.length() + 10);
        formUrlEncode(string, result,charsToLeave);
        return result.toString();
    }
    
    /**
     * Decodes an url-encoded string. This method basically substitutes all '+' signs with a space and all
     * '%'-escape sequences with proper character values - according to the UTF16 encoding. 
     * 
     * @param string the string to be decoded
     * @return the decoded version
     */
    public static String formUrlDecode(String string) {
        int length = string.length();
        StringBuilder buffer = new StringBuilder(length + 10);

        int i = 0;
        while (i < length) {
            char c = string.charAt(i);
            if (c == '+') {
                buffer.append(' ');
                i++;
            }
            else if (c == '%') {
                int start = i;
                String character = "";
                do {
                    i += 3;
                } while (i < length && string.charAt(i) == '%'); // remeber to check if i < length
                try {
                    character = URLDecoder.decode(string.substring(start,i),"UTF-8");
                } catch (Exception e) {
                    // gulp!
                }
                buffer.append(character);
            } else {
                buffer.append((char)c);
                i++;
            }
        }
        return buffer.toString();
    }

    /**
     * Sets a request property on the supplied connection indicating that a server can respond with
     * gzip-encoded data if it wants to.
     */
    public static void setAcceptGZIPEncoding(URLConnection conn) {
        conn.setRequestProperty("Accept-Encoding", "gzip");
    }

    /**
     * Gets the InputStream for reading the response from a server. This method handles any
     * encoding-related decoding of the data, e.g. gzip.
     */
    public static InputStream getInputStream(URLConnection conn) throws IOException {
        InputStream responseStream = conn.getInputStream();

        if ("gzip".equalsIgnoreCase(conn.getContentEncoding())) {
            responseStream = new GZIPInputStream(responseStream);
        }

        return responseStream;
    }
    
    private static void formUrlEncode(String s, StringBuilder buffer, String charsToLeave) {
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            
            if (c == ' ' && charsToLeave.indexOf(' ') < 0) {
                // replace all spaces with a '+'
                buffer.append('+');
            }
            else {
                int cInt = (int) c;
                
                if (cInt >= 48 && cInt <= 57 || // numbers
                    cInt >= 65 && cInt <= 90 || // uppercase
                    cInt >= 97 && cInt <= 122 || // lowercase
                    cInt == 45 || cInt == 95 || cInt == 46 || cInt == 126 || //hyphen, underscode, dot, tilde
                    (charsToLeave != null && charsToLeave.indexOf(c) != -1)) {
                    /*
                     * The RFC 3986 calls these the UNRESERVED CHARACTERS - section 2.3
                     */
                    buffer.append(c);
                }
                else if (cInt == '*') {
                    /*
                     * for some bizarre reason the URL encoder does not percent-encode a '*' character, even
                     * though according to the RFC3986 it is not on the UNRESERVED CHARACTERS list. That's why
                     * we treat this case separately.
                     */
                    buffer.append("%2a");
                }
                else {
                    /*
                     * all characters that are not on unreserved list, and are not on the charsToLeave list
                     * must be percent encoded
                     */
                    String stringVal = String.valueOf((char)c);
                    String hexVal = null;
                    try {
                        hexVal = URLEncoder.encode(stringVal, "UTF-8");
                    }
                    catch (UnsupportedEncodingException e) {
                        // this will not happen
                    }

                    buffer.append(hexVal);
                }
            }
        }
    }
}
