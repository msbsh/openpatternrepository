/*
 * Copyright (c) 2005 - 2008 Aduna and Deutsches Forschungszentrum fuer Kuenstliche Intelligenz DFKI GmbH.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.mime.identifier;

import org.ontoware.rdf2go.model.node.URI;

/**
 * Identifies the MIME type of a binary resource. Implementations typically use heuristics such as magic
 * numbers, file name extension, etc. to perform the identification.
 */
public interface MimeTypeIdentifier {

    /**
     * Identifies the MIME type of a binary resource based on the specified characteristics. Any of the
     * parameters may be null.
     * 
     * @param firstBytes The first bytes of the resource's binary contents. The getMinArrayLength method
     *            will return how big this array should minimally be for this MimeTypeIdentifier to do
     *            its job.
     * @param fileName The resource's file name.
     * @param uri the URI used to typically identify the resource.
     * @return the detected MIME type of the resource, or null when the MIME type could not be
     *         determined.
     * 
     * @see #getMinArrayLength()
     */
    public String identify(byte[] firstBytes, String fileName, URI uri);

    /**
     * Returns the minimum length needed by the identify method for its array parameter.
     * 
     * @see #identify(byte[], String, URI)
     */
    public int getMinArrayLength();
}
