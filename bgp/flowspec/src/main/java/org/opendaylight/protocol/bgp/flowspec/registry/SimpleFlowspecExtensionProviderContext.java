/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.flowspec;

import org.opendaylight.protocol.bgp.flowspec.SimpleFlowspecTypeRegistry;

public class SimpleFlowspecExtensionProviderContext {

    private final SimpleFlowspecTypeRegistry flowspecIpv4TypeRegistry = new SimpleFlowspecTypeRegistry();

    private final SimpleFlowspecTypeRegistry flowspecIpv6TypeRegistry = new SimpleFlowspecTypeRegistry();

    public AutoClosealbe registerFlowspecIpv4TypeParser(final int type, final FlowspecTypeParser parser) {
    	this.flowspecIpv4TypeRegistry.registerFlowspecTypeParser(type, parser);
    }

    public AutoClosealbe registerFlowspecIpv4TypeSerializer(final FlowspecType typeClass, final FlowspecTypeSerializer serializer) {
    	this.flowspecIpv4TypeRegistry.registerFlowspecTypeSerializer(typeClass, serializer);
    }

    public AutoClosealbe registerFlowspecIpv6TypeParser(final int type, final FlowspecTypeParser parser) {
    	this.flowspecIpv6TypeRegistry.registerFlowspecTypeParser(type, parser);
    }

    public AutoClosealbe registerFlowspecIpv46ypeSerializer(final FlowspecType typeClass, final FlowspecTypeSerializer serializer) {
    	this.flowspecIpv6TypeRegistry.registerFlowspecTypeSerializer(typeClass, serializer);
    }

    public SimpleFlowspecTypeRegistry getFlowspecIpv4TypeRegistry() {
    	return this.flowspecIpv4TypeRegistry;
    }

    public SimpleFlowspecTypeRegistry getFlowspecIpv6TypeRegistry() {
    	return this.flowspecIpv6TypeRegistry;
    }    
}
