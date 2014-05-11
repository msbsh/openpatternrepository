/*
 * Copyright (c) 2005 - 2008 Aduna and Deutsches Forschungszentrum fuer Kuenstliche Intelligenz DFKI GmbH.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.mime.identifier;

/**
 * A MimeTypeIdentifierFactory creates instances of a specific MimeTypeIdentifier implementation.
 * 
 * <P>
 * MimeTypeIdentifierFactories should be very light-weight to create. This allows them to be used for
 * service registration in service-oriented architectures.
 */
public interface MimeTypeIdentifierFactory {

    /**
     * Get a MimeTypeIdentifier.
     */
    public MimeTypeIdentifier get();
}
