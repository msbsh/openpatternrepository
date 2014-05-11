/*
 * Copyright (c) 2005 - 2008 Aduna.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.mime.identifier.magic;

import java.util.ArrayList;

public class MimeTypeDescription {

	private String mimeType;

	private String parentType;

	private ArrayList extensions;

	private ArrayList magicStrings;

	private ArrayList magicNumbers;

	private boolean allowsLeadingWhiteSpace;

	private ArrayList requiringTypes;

	public MimeTypeDescription(String mimeType, String parentType, ArrayList extensions,
			ArrayList magicStrings, ArrayList magicNumbers, boolean allowsLeadingWhiteSpace) {
		this.mimeType = mimeType;
		this.parentType = parentType;
		this.extensions = extensions;
		this.magicStrings = magicStrings;
		this.magicNumbers = magicNumbers;
		this.allowsLeadingWhiteSpace = allowsLeadingWhiteSpace;
		this.requiringTypes = new ArrayList(0);
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getParentType() {
		return parentType;
	}

	public ArrayList getExtensions() {
		return extensions;
	}

	public ArrayList getMagicStrings() {
		return magicStrings;
	}

	public ArrayList getMagicNumbers() {
		return magicNumbers;
	}

	public boolean getAllowsLeadingWhiteSpace() {
		return allowsLeadingWhiteSpace;
	}

	/**
	 * Register a requiring MimeTypeDescription on this MimeTypeDescription. The specified MimeTypeDescription
	 * should have at least one Condition that has this MimeTypeDescription's mimeType as parent type.
	 */
	public void addRequiringType(MimeTypeDescription description) {
		requiringTypes.add(description);
	}

	public ArrayList getRequiringTypes() {
		return requiringTypes;
	}

	public boolean matches(char[] chars) {
		if (chars != null && magicStrings != null && !magicStrings.isEmpty()) {
			int skippedChars = 0;

			if (allowsLeadingWhiteSpace) {
				for (int i = 0; i < chars.length; i++) {
					char c = chars[i];
					if (Character.isWhitespace(c)) {
						skippedChars++;
					}
					else {
						break;
					}
				}
			}

			int nrStrings = magicStrings.size();
			for (int i = 0; i < nrStrings; i++) {
				MagicString magicString = (MagicString) magicStrings.get(i);
				if (magicString.matches(chars, skippedChars)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean matches(byte[] bytes) {
		if (bytes != null && magicNumbers != null && !magicNumbers.isEmpty()) {
			int skippedBytes = 0;

			// we might have to test character-based files using byte arrays instead than strings, e.g. when
			// encountering a UTF8 HTML file without a byte order mark: in this case only a byte array is
			// specified to this method, not a character array. Therefore we still have to see if we must skip
			// white space.
			if (allowsLeadingWhiteSpace) {
				// The range of chars in ASCII overlaps with Unicode in the range 0 - 127 so we can happily
				// cast bytes to chars here and add some additional tests for bytes from the UTF-8 and UTF-16
				// Byte Order Marks (see
				// http://msdn.microsoft.com/library/default.asp?url=/library/en-us/intl/unicode_42jv.asp).
				//
				// The only problem is with the null characters used by UTF-16: not only does this make
				// recognition of white space characters more difficult (null chars appearing before or after
				// the "real" character bytes), also the whole concept of testing for magic number *bytes*
				// after that won't work at all, regardless of whether it starts with whitespace or not.
				// However, this probably only affects text/html and text/xml documents in UTF-16 format that
				// have no typical format-specific file extension and that are not obtained from a web or
				// email server that tells you the correct MIME type as a fallback mechanism :)
				for (int i = 0; i < bytes.length; i++) {
					char c = (char) bytes[i];
					if (Character.isWhitespace(c) || c == '\u0000' || c == '\u00ff' || c == '\u00fe'
							|| c == '\u00ef' || c == '\u00bb' || c == '\u00bf') {
						skippedBytes++;
					}
					else {
						break;
					}
				}
			}

			int nrNumbers = magicNumbers.size();
			for (int i = 0; i < nrNumbers; i++) {
				MagicNumber magicNumber = (MagicNumber) magicNumbers.get(i);
				if (magicNumber.matches(bytes, skippedBytes)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns whether the set of file extensions of this MimeTypeDescription contains the specified file
	 * extension.
	 */
	public boolean containsExtension(String extension) {
		return extensions.contains(extension);
	}
}
