/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.flowspec.handlers;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import org.opendaylight.protocol.bgp.flowspec.spi.handlers.FlowspecTypeParser;
import org.opendaylight.protocol.bgp.flowspec.spi.handlers.FlowspecTypeSerializer;
import org.opendaylight.protocol.util.Ipv6Util;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.flowspec.rev150807.flowspec.destination.flowspec.FlowspecType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.flowspec.rev150807.flowspec.destination.ipv6.flowspec.flowspec.type.SourceIpv6PrefixCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.flowspec.rev150807.flowspec.destination.ipv6.flowspec.flowspec.type.SourceIpv6PrefixCaseBuilder;

public final class FSSourceIpv6PrefixHandler implements FlowspecTypeParser, FlowspecTypeSerializer {
    static final int SOURCE_PREFIX_VALUE = 2;

    @Override
    public void serializeType(FlowspecType value, ByteBuf output) {
        Preconditions.checkArgument(value instanceof SourceIpv6PrefixCase, "SourceIpv6PrefixCase class is mandatory!");
        output.writeByte(SOURCE_PREFIX_VALUE);
        output.writeBytes(Ipv6Util.bytesForPrefixBegin(((SourceIpv6PrefixCase) value).getSourcePrefix()));
    }

    @Override
    public FlowspecType parseType(ByteBuf buffer) {
        Preconditions.checkArgument(((int) buffer.readUnsignedByte()) == SOURCE_PREFIX_VALUE, "Source prefix type does not match!");
        return new SourceIpv6PrefixCaseBuilder().setSourcePrefix(Ipv6Util.prefixForByteBuf(buffer)).build();
    }
}
