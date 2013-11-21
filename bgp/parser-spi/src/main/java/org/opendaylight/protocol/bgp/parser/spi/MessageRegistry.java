/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.parser.spi;

import org.opendaylight.protocol.bgp.parser.BGPDocumentedException;
import org.opendaylight.protocol.framework.DeserializerException;
import org.opendaylight.yangtools.yang.binding.Notification;

public interface MessageRegistry {
	public Notification parseMessage(final byte[] bytes) throws BGPDocumentedException, DeserializerException;
	public byte[] serializeMessage(Notification message);
}