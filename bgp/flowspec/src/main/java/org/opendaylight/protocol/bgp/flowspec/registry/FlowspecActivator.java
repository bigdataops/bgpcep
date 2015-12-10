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

public final class FlowspecActivator {
    protected List<AutoCloseable> startImpl(final SimpleFlowspecExtensionProviderContext context) {
        final List<AutoCloseable> regs = new ArrayList<>();
        registerFlowspecTypeHandlers(regs, context);
        return regs;
    }

    protected void registerFlowspecTypeHandlers(final List<AutoCloseable> regs, SimpleFlowspecExtensionProviderContext context) {
    }
}
