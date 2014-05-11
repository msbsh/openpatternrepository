/*
 * Copyright (c) 2005 - 2008 Aduna.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * ResourceUtil is a utility class for retrieving resources (images, property-files, etc) from the
 * classpath.
 */
public class ResourceUtil {
	
    public static URL getURL(String resourceName, Class clazz) {
        URL result = null;

        result = ResourceUtil.class.getResource(resourceName);
        if (result == null) {
            result = clazz.getClassLoader().getResource(resourceName);
            if (result == null) {
                result = ClassLoader.getSystemResource(resourceName);
            }
        }

        return result;
    }

    public static InputStream getInputStream(String resourceName, Class clazz) {
        InputStream result = null;

        result = ResourceUtil.class.getResourceAsStream(resourceName);
        if (result == null) {
            result = clazz.getClassLoader().getResourceAsStream(resourceName);
            if (result == null) {
                result = ClassLoader.getSystemResourceAsStream(resourceName);
            }
        }

        return result;
    }

    public static String getString(String resourceName, Class clazz) throws IOException {
        String result = null;

        InputStream in = ResourceUtil.getInputStream(resourceName,clazz);

        if (in != null) {
            try {
                result = IOUtil.readString(in);
            }
            finally {
                in.close();
            }
        }

        return result;
    }
}
