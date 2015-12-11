/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.flowspec;

import java.util.ArrayList;
import java.util.List;
import org.opendaylight.protocol.bgp.flowspec.SimpleFlowspecExtensionProviderContext;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSDesinationPortHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSDscpHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIcmpCodeHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIcmpTypeHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIpProtocolHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIpv4DestinationPrefixHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIpv4FragmentHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIpv4SourcePrefixHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIpv6DestinationPrefixHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIpv6SourcePrefixHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIpv6NextHeaderHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIpv6FragmentHandler
import org.opendaylight.protocol.bgp.flowspec.handlers.FSIpv6FlowLabelHander;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSPacketLengthHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSPortHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSSourcePortHandler;
import org.opendaylight.protocol.bgp.flowspec.handlers.FSTcpFlagsHandler;


public final class FlowspecActivator {

    public static List<AutoCloseable> startImpl(final SimpleFlowspecExtensionProviderContext context) {

        final List<AutoCloseable> regs = new ArrayList<>();
        registerIpv4FlowspecTypeHandlers(regs, context);
        return regs;
    }

    protected void registerIpv4FlowspecTypeHandlers(final List<AutoCloseable> regs, SimpleFlowspecExtensionProviderContext context) {

    	final FSIpv4DestinationPrefixHandler destinationPrefixHandler = new FSIpv4DestinationPrefixHandler();
    	regs.add(context.registerFlowspecIpv4TypeParser(FSIpv4DestinationPrefixHandler.DESTINATION_PREFIX_VALUE, destinationPrefixHandler));
    	regs.add(context.registerFlowspecIpv4TypeSerializer(DestinationPrefixCase.class, destinationPrefixHandler));

        final FSIpv4SourcePrefixHandler sourcePrefixHandler = new FSIpv4SourcePrefixHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSIpv4SourcePrefixHandler.SOURCE_PREFIX_VALUE, sourcePrefixHandler);
        regs.add(context.registerFlowspecIpv4TypeSerializer(SourcePrefixCase.class, sourcePrefixHandler));

        final FSIpProtocolHandler ipProtocolHandler = new FSIpProtocolHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSIpProtocolHandler.IP_PROTOCOL_VALUE, ipProtocolHandler));
        regs.add(context.registerFlowspecIpv4TypeSerializer(ProtocolIpCase.class, ipProtocolHandler));

        final FSPortHandler portHandler = new FSPortHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSPortHandler.PORT_VALUE, portHandler));
        regs.add(context.registerFlowspecIpv4TypeSerializer(PortCase.class, portHandler)));

        final FSDesinationPortHandler destinationPortHandler = new FSDesinationPortHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSDesinationPortHandler.DESTINATION_PORT_VALUE, destinationPortHandler));
        regs.add(context.registerFlowspecIpv4TypeSerializer(DestinationPortCase.class, destinationPortHandler));

        final FSSourcePortHandler sourcePortHandler = new FSSourcePortHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSSourcePortHandler.SOURCE_PORT_VALUE, sourcePortHandler));
        regs.add(conexts.registerFlowspecIpv4TypeSerializer(SourcePortCase.class, sourcePortHandler));

        final FSIcmpTypeHandler icmpTypeHandler = new FSIcmpTypeHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSIcmpTypeHandler.ICMP_TYPE_VALUE, icmpTypeHandler));
        regs.add(context.registerFlowspecIpv4TypeSerializer(IcmpTypeCase.class, icmpTypeHandler));

        final FSIcmpCodeHandler icmpCodeHandler = new FSIcmpCodeHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSIcmpCodeHandler.ICMP_CODE_VALUE, icmpCodeHandler));
        regs.add(context.registerFlowspecIpv4TypeSerializer(IcmpCodeCase.class, icmpCodeHandler));

        final FSTcpFlagsHandler tcpFlagsHandler = new FSTcpFlagsHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSTcpFlagsHandler.TCP_FLAGS_VALUE, tcpFlagsHandler));
        regs.add(context.registerFlowspecIpv4TypeSerializer(TcpFlagsCase.class, tcpFlagsHandler));

        final FSPacketLengthHandler packetlengthHandler = new FSPacketLengthHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSPacketLengthHandler.PACKET_LENGTH_VALUE, packetlengthHandler));
        regs.add(context.registerFlowspecIpv4TypeSerializer(PacketLengthCase.class, packetlengthHandler));

        final FSDscpHandler dscpHandler = new FSDscpHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSDscpHandler.DSCP_VALUE, dscpHandler));
        regs.add(context.registerFlowspecIpv4TypeSerializer(DscpCase.class, dscpHandler));

        final FSIpv4FragmentHandler fragmentHandler = new FSIpv4FragmentHandler();
        regs.add(context.registerFlowspecIpv4TypeParser(FSIpv4FragmentHandler.FRAGMENT_VALUE, fragmentHandler));
        regs.add(context.registerFlowspecIpv4TypeSerializer(FragmentCase.class, fragmentHandler));
    }

    protected void registerIpv6FlowspecTypeHandlers(final List<AutoCloseable> regs, SimpleFlowspecExtensionProviderContext context) {

    	final FSIpv6DestinationPrefixHandler destinationPrefixHandler = new FSIpv6DestinationPrefixHandler();
    	regs.add(context.registerFlowspecIpv6TypeParser(FSIpv6DestinationPrefixHandler.DESTINATION_PREFIX_VALUE, destinationPrefixHandler));
    	regs.add(context.registerFlowspecIpv6TypeSerializer(DestinationIpv6PrefixCase.class, destinationPrefixHandler));

        final FSIpv6SourcePrefixHandler sourcePrefixHandler = new FSIpv6SourcePrefixHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSIpv6SourcePrefixHandler.SOURCE_PREFIX_VALUE, sourcePrefixHandler);
        regs.add(context.registerFlowspecIpv6TypeSerializer(SourcePrefixCase.class, sourcePrefixHandler));

        final FSIpv6NextHeaderHandler ipProtocolHandler = new FSIpv6NextHeaderHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSIpv6NextHeaderHandler.IP_PROTOCOL_VALUE, ipProtocolHandler));
        regs.add(context.registerFlowspecIpv6TypeSerializer(NextHeaderCase.class, ipProtocolHandler));

        final FSPortHandler portHandler = new FSPortHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSPortHandler.PORT_VALUE, portHandler));
        regs.add(context.registerFlowspecIpv6TypeSerializer(PortCase.class, portHandler)));

        final FSDesinationPortHandler destinationPortHandler = new FSDesinationPortHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSDesinationPortHandler.DESTINATION_PORT_VALUE, destinationPortHandler));
        regs.add(context.registerFlowspecIpv6TypeSerializer(DestinationPortCase.class, destinationPortHandler));

        final FSSourcePortHandler sourcePortHandler = new FSSourcePortHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSSourcePortHandler.SOURCE_PORT_VALUE, sourcePortHandler));
        regs.add(conexts.registerFlowspecIpv6TypeSerializer(SourcePortCase.class, sourcePortHandler));

        final FSIcmpTypeHandler icmpTypeHandler = new FSIcmpTypeHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSIcmpTypeHandler.ICMP_TYPE_VALUE, icmpTypeHandler));
        regs.add(context.registerFlowspecIpv6TypeSerializer(IcmpTypeCase.class, icmpTypeHandler));

        final FSIcmpCodeHandler icmpCodeHandler = new FSIcmpCodeHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSIcmpCodeHandler.ICMP_CODE_VALUE, icmpCodeHandler));
        regs.add(context.registerFlowspecIpv6TypeSerializer(IcmpCodeCase.class, icmpCodeHandler));

        final FSTcpFlagsHandler tcpFlagsHandler = new FSTcpFlagsHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSTcpFlagsHandler.TCP_FLAGS_VALUE, tcpFlagsHandler));
        regs.add(context.registerFlowspecIpv6TypeSerializer(TcpFlagsCase.class, tcpFlagsHandler));

        final FSPacketLengthHandler packetlengthHandler = new FSPacketLengthHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSPacketLengthHandler.PACKET_LENGTH_VALUE, packetlengthHandler));
        regs.add(context.registerFlowspecIpv6TypeSerializer(PacketLengthCase.class, packetlengthHandler));

        final FSDscpHandler dscpHandler = new FSDscpHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSDscpHandler.DSCP_VALUE, dscpHandler));
        regs.add(context.registerFlowspecIpv6TypeSerializer(DscpCase.class, dscpHandler));

        final FSIpv6FragmentHandler fragmentHandler = new FSIpv6FragmentHandler();
        regs.add(context.registerFlowspecIpv6TypeParser(FSIpv6FragmentHandler.FRAGMENT_VALUE, fragmentHandler));
        regs.add(context.registerFlowspecIpv6TypeSerializer(FragmentCase.class, fragmentHandler));

        final FSIpv6FlowLabelHander flowlabelHander = new FSIpv6FlowLabelHander();
        regs.add(context.registerFlowspecIpv4TypeParser(FSIpv6FlowLabelHander.FLOW_LABEL_VALUE, flowlabelHander));
        regs.add(conext.registerFlowspecIpv6TypeSerializer(FlowLabelCase, flowlabelHander));
    }
}
