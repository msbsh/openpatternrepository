/*
 * Copyright (c) 2005 - 2008 Aduna.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Provides utility methods for String handling.
 */
public class StringUtil {

    /**
     * Substitute String "old" by String "new" in String "text" everywhere.
     * 
     * @param olds The String to be substituted.
     * @param news The String containing the new content.
     * @param text The String in which the substitution is done.
     * @return The result String containing the substitutions; if no substitutions were made, the
     *         specified 'text' instance is returned.
     */
    public static String replace(String olds, String news, String text) {
        if (olds == null || olds.length() == 0) {
            // nothing to substitute.
            return text;
        }
        if (text == null) {
            return null;
        }

        // search for any occurences of 'olds'.
        int oldsIndex = text.indexOf(olds);
        if (oldsIndex == -1) {
            // Nothing to substitute.
            return text;
        }

        // we're going to do some substitutions.
        StringBuilder buffer = new StringBuilder(text.length());
        int prevIndex = 0;

        while (oldsIndex >= 0) {
            // first, add the text between the previous and the current occurence
            buffer.append(text.substring(prevIndex, oldsIndex));

            // then add the substition pattern
            buffer.append(news);

            // remember the index for the next loop
            prevIndex = oldsIndex + olds.length();

            // search for the next occurence
            oldsIndex = text.indexOf(olds, prevIndex);
        }

        // add the part after the last occurence
        buffer.append(text.substring(prevIndex));

        return buffer.toString();
    }
    
    /**
     * Computes the SHA1 hash for the given string.
     * <p>
     * The code has been 'borrowed' from the mimedir-parser available from
     * http://ilrt.org/discovery/2003/02/cal/mimedir-parser/
     * 
     * @param string The string for which we'd like to get the SHA1 hash.
     * @return The generated SHA1 hash
     */
    public static String sha1Hash(String string) {
        try {
            return sha1Hash(string.getBytes());
        }
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Computes the SHA1 hash for the given byte array.
     * @param bytes the byte array
     * @return SHA1 hash for the given byte array
     */
    public static String sha1Hash(byte [] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(bytes);
            byte[] digest = md.digest();
            BigInteger integer = new BigInteger(1, digest);
            return integer.toString(16);
        }
        catch (Exception e) {
            return null;
        }
    }
}
