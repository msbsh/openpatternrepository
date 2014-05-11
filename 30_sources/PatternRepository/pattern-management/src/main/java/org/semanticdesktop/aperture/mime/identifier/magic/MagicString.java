/*
 * Copyright (c) 2006 - 2008 Aduna.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.mime.identifier.magic;

public class MagicString {

    private char[] magicChars;
    
    private boolean caseSensitive;
    
    public MagicString(char[] magicChars, boolean caseSensitive) {
    	if (magicChars == null) {
    		throw new IllegalArgumentException("magicChars should not be null");
    	}
    	
        this.magicChars = magicChars;
        this.caseSensitive = caseSensitive;
        
        if (!caseSensitive) {
            for (int i = 0; i < magicChars.length; i++) {
                magicChars[i] = Character.toLowerCase(magicChars[i]);
            }
        }
    }
    
    public char[] getMagicChars() {
        return magicChars;
    }
    
    public int getMinimumLength() {
        return magicChars.length;
    }
    
    public boolean matches(char[] chars, int skippedLeadingChars) {
    	// check whether the specified array is long enough to check for the char sequence
        if (chars.length < magicChars.length + skippedLeadingChars) {
            return false;
        }
        
        // check the magic chars
        for (int i = 0; i < magicChars.length; i++) {
            char magicChar = magicChars[i];
            char testedChar = chars[i + skippedLeadingChars];
            
            if (!caseSensitive) {
                testedChar = Character.toLowerCase(testedChar);
            }
            
            if (magicChar != testedChar) {
                return false;
            }
        }

        // apparently all magic chars are present
        return true;
    }
}
