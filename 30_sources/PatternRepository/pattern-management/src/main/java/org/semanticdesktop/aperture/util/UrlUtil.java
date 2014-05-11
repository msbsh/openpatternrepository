/*
 * Copyright (c) 2005 - 2008 Aduna.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Offers utility methods related to URLs, network connections, etc.
 */
public class UrlUtil {

	/**
     * Normalizes a URL. The following steps are taken to normalize a URL:
     * 
     * <ul>
     * <li>The protocol is made lower-case.
     * <li>The host is made lower-case.
     * <li>A specified port is removed if it matches the default port.
     * <li>Any query parameters are sorted alphabetically.
     * <li>Any anchor information is removed.
     * </ul>
     */
	public static URL normalizeURL(URL url) {
		// retrieve the various parts of the URL
		String protocol = url.getProtocol();
		String host = url.getHost();
		int port = url.getPort();
		String path = url.getPath();
		String query = url.getQuery();

		// normalize the fields
		protocol = protocol.toLowerCase();
		host = host.toLowerCase();

		if (port == url.getDefaultPort()) {
			port = -1;
		}

		String file = normalizePath(path);

		if (query != null) {
			query = normalizeQuery(query);
			file += "?" + query;
		}

		// create the normalized URL
		try {
			return new URL(protocol, host, port, file);
		}
		catch (MalformedURLException e) {
			// something wrong with the algorithm?
			throw new RuntimeException(e);
		}
	}
    
	/**
     * Remove relative references and "mistakes" like double slashes from the path.
     * 
     * @param path The path to normalize.
     * @return The normalized path.
     */
	public static String normalizePath(String path) {
		String result = path;

		// replace all double slashes with a single slash
		result = StringUtil.replace("//", "/", result);
		
		// replace all references to the current directory with nothing
		result = StringUtil.replace("/./", "/", result);
		
		// replace all references to the parent directory with nothing
		result = result.replaceAll("/[^/]+/\\.\\./", "/");

		return result;
	}
	
	/**
     * Normalizes a query string by sorting the query parameters alpabetically.
     * 
     * @param query The query string to normalize.
     * @return The normalized query string.
     */
	public static String normalizeQuery(String query) {
		TreeSet sortedSet = new TreeSet();

		// extract key-value pairs from the query string
		StringTokenizer tokenizer = new StringTokenizer(query, "&");
		while (tokenizer.hasMoreTokens()) {
			sortedSet.add(tokenizer.nextToken());
		}

		// reconstruct query string
		StringBuilder result = new StringBuilder(query.length());
        
		Iterator iterator = sortedSet.iterator();
		while (iterator.hasNext()) {
			result.append(iterator.next());

			if (iterator.hasNext()) {
				result.append('&');
			}
		}

		return result.toString();
	}
}
