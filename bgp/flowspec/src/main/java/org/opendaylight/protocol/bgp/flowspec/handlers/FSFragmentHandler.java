/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.flowspec;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;
import org.opendaylight.protocol.bgp.flowspec.BitmaskOperandParser;
import org.opendaylight.protocol.bgp.flowspec.spi.handlers.FlowspecTypeParser;
import org.opendaylight.protocol.bgp.flowspec.spi.handlers.FlowspecTypeSerializer;
import org.opendaylight.protocol.util.ByteArray;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.flowspec.rev150807.BitmaskOperand;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.flowspec.rev150807.flowspec.destination.flowspec.FlowspecType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.flowspec.rev150807.flowspec.destination.flowspec.flowspec.type.FragmentCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.flowspec.rev150807.flowspec.destination.flowspec.flowspec.type.FragmentCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.flowspec.rev150807.flowspec.destination.flowspec.flowspec.type.fragment._case.Fragments;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.flowspec.rev150807.flowspec.destination.flowspec.flowspec.type.fragment._case.FragmentsBuilder;

public abstract class FSFragmentHandler implements FlowspecTypeParser, FlowspecTypeSerializer {
    protected static final int FRAGMENT_VALUE = 12;

    protected abstract Fragment parseFragment(final byte fragment);
    protected abstract byte serializeFragment(final Fragment fragment);

    @Override
    public void serializeType(FlowspecType fsType, ByteBuf output) {
        Preconditions.checkArgument(fsType instanceof FragmentCase, "FragmentCase class is mandatory!");
        output.writeByte(FRAGMENT_VALUE);
        serializeFragments(((FragmentCase) fsType).getFragment(), output);
    }

    @Override
    public FlowspecType parseType(ByteBuf buffer) {
        Preconditions.checkArgument(((int) buffer.readUnsignedByte()) == FRAGMENT_VALUE, "Destination prefix type does not match!");
        return new FragmentCaseBuilder().setFragment(parseFragment(buffer)).build();
    }

    protected final void serializeFragments(final List<Fragments> fragments, final ByteBuf nlriByteBuf) {
        for (final Fragments fragment : fragments) {
            BitmaskOperandParser.INSTANCE.serialize(fragment.getOp(), 1, nlriByteBuf);
            nlriByteBuf.writeByte(serializeFragment(fragment.getValue()));
        }
    }

    protected final List<Fragments> parseFragments(final ByteBuf nlri) {
        final List<Fragments> fragments = new ArrayList<>();
        boolean end = false;
        // we can do this as all fields will be rewritten in the cycle
        final FragmentsBuilder builder = new FragmentsBuilder();
        while (!end) {
            final byte b = nlri.readByte();
            final BitmaskOperand op = BitmaskOperandParser.INSTANCE.parse(b);
            builder.setOp(op);
            builder.setValue(parseFragment(nlri.readByte()));
            end = op.isEndOfList();
            fragments.add(builder.build());
        }
        return fragments;
    }
}