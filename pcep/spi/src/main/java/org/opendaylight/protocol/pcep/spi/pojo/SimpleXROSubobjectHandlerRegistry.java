/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.pcep.spi.pojo;

import org.opendaylight.protocol.concepts.HandlerRegistry;
import org.opendaylight.protocol.pcep.spi.XROSubobjectHandlerRegistry;
import org.opendaylight.protocol.pcep.spi.XROSubobjectParser;
import org.opendaylight.protocol.pcep.spi.XROSubobjectSerializer;
import org.opendaylight.protocol.util.Values;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.basic.explicit.route.subobjects.SubobjectType;
import org.opendaylight.yangtools.yang.binding.DataContainer;

import com.google.common.base.Preconditions;

public final class SimpleXROSubobjectHandlerRegistry implements XROSubobjectHandlerRegistry {
	private final HandlerRegistry<DataContainer, XROSubobjectParser, XROSubobjectSerializer> handlers = new HandlerRegistry<>();

	public AutoCloseable registerSubobjectParser(final int subobjectType, final XROSubobjectParser parser) {
		Preconditions.checkArgument(subobjectType >= 0 && subobjectType <= Values.UNSIGNED_SHORT_MAX_VALUE);
		return this.handlers.registerParser(subobjectType, parser);
	}

	public AutoCloseable registerSubobjectSerializer(final Class<? extends SubobjectType> subobjectClass,
			final XROSubobjectSerializer serializer) {
		return this.handlers.registerSerializer(subobjectClass, serializer);
	}

	@Override
	public XROSubobjectParser getSubobjectParser(final int subobjectType) {
		Preconditions.checkArgument(subobjectType >= 0 && subobjectType <= Values.UNSIGNED_SHORT_MAX_VALUE);
		return this.handlers.getParser(subobjectType);
	}

	@Override
	public XROSubobjectSerializer getSubobjectSerializer(final SubobjectType subobject) {
		return this.handlers.getSerializer(subobject.getImplementedInterface());
	}
}