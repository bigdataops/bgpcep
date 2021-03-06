/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.protocol.bmp.impl.app;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.net.InetSocketAddress;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.controller.md.sal.dom.api.DOMDataBroker;
import org.opendaylight.controller.md.sal.dom.api.DOMDataWriteTransaction;
import org.opendaylight.protocol.bgp.rib.spi.RIBExtensionConsumerContext;
import org.opendaylight.protocol.bmp.api.BmpDispatcher;
import org.opendaylight.protocol.bmp.impl.spi.BmpMonitoringStation;
import org.opendaylight.tcpmd5.api.KeyMapping;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bmp.monitor.rev150512.BmpMonitor;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bmp.monitor.rev150512.MonitorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bmp.monitor.rev150512.bmp.monitor.Monitor;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bmp.monitor.rev150512.routers.Router;
import org.opendaylight.yangtools.binding.data.codec.api.BindingCodecTree;
import org.opendaylight.yangtools.binding.data.codec.api.BindingCodecTreeFactory;
import org.opendaylight.yangtools.sal.binding.generator.util.BindingRuntimeContext;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.NodeIdentifier;
import org.opendaylight.yangtools.yang.data.impl.schema.Builders;
import org.opendaylight.yangtools.yang.data.impl.schema.ImmutableNodes;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BmpMonitoringStationImpl implements BmpMonitoringStation {

    private static final Logger LOG = LoggerFactory.getLogger(BmpMonitoringStationImpl.class);

    private static final QName MONITOR_ID_QNAME = QName.cachedReference(QName.create(Monitor.QNAME, "monitor-id"));

    private final DOMDataBroker domDataBroker;
    private final YangInstanceIdentifier yangMonitorId;
    private final RouterSessionManager sessionManager;
    private final Channel channel;
    private final MonitorId monitorId;

    private BmpMonitoringStationImpl(final DOMDataBroker domDataBroker, final YangInstanceIdentifier yangMonitorId,
            final Channel channel, final RouterSessionManager sessionManager, final MonitorId monitorId) {
        this.domDataBroker = Preconditions.checkNotNull(domDataBroker);
        this.yangMonitorId = Preconditions.checkNotNull(yangMonitorId);
        this.channel = Preconditions.checkNotNull(channel);
        this.sessionManager = Preconditions.checkNotNull(sessionManager);
        this.monitorId = monitorId;
        createEmptyMonitor();
        LOG.info("BMP Monitoring station {} started", this.monitorId.getValue());
    }

    public static BmpMonitoringStation createBmpMonitorInstance(final RIBExtensionConsumerContext ribExtensions, final BmpDispatcher dispatcher,
            final DOMDataBroker domDataBroker, final MonitorId monitorId, final InetSocketAddress address, final Optional<KeyMapping> keys,
            final BindingCodecTreeFactory codecFactory, final SchemaContext schemaContext) throws InterruptedException {
        Preconditions.checkNotNull(ribExtensions);
        Preconditions.checkNotNull(dispatcher);
        Preconditions.checkNotNull(domDataBroker);
        Preconditions.checkNotNull(monitorId);
        Preconditions.checkNotNull(address);
        final YangInstanceIdentifier yangMonitorId = YangInstanceIdentifier.builder()
                .node(BmpMonitor.QNAME)
                .node(Monitor.QNAME)
                .nodeWithKey(Monitor.QNAME, MONITOR_ID_QNAME, monitorId.getValue())
                .build();

        final BindingRuntimeContext runtimeContext = BindingRuntimeContext.create(ribExtensions.getClassLoadingStrategy(),
                schemaContext);
        final BindingCodecTree tree  = codecFactory.create(runtimeContext);
        final RouterSessionManager sessionManager = new RouterSessionManager(yangMonitorId, domDataBroker, ribExtensions, tree);
        final ChannelFuture channelFuture = dispatcher.createServer(address, sessionManager, keys);
        return new BmpMonitoringStationImpl(domDataBroker, yangMonitorId, channelFuture.sync().channel(), sessionManager, monitorId);
    }

    private void createEmptyMonitor() {
        final DOMDataWriteTransaction wTx = this.domDataBroker.newWriteOnlyTransaction();
        wTx.put(LogicalDatastoreType.OPERATIONAL,
                YangInstanceIdentifier.of(BmpMonitor.QNAME),
                Builders.containerBuilder()
                .withNodeIdentifier(
                        new NodeIdentifier(BmpMonitor.QNAME))
                        .addChild(ImmutableNodes.mapNodeBuilder(Monitor.QNAME)
                                .addChild(ImmutableNodes.mapEntryBuilder(Monitor.QNAME, MONITOR_ID_QNAME, this.monitorId.getValue())
                                        .addChild(ImmutableNodes.leafNode(MONITOR_ID_QNAME, monitorId.getValue()))
                                        .addChild(ImmutableNodes.mapNodeBuilder(Router.QNAME).build())
                                        .build()).build())
                        .build());
        try {
            wTx.submit().checkedGet();
        } catch (final TransactionCommitFailedException e) {
            LOG.error("Failed to initiate BMP Monitor {}.", this.monitorId.getValue(), e);
        }
    }

    @Override
    public void close() throws Exception {
        this.channel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                BmpMonitoringStationImpl.this.sessionManager.close();
            }
        }).await();
        final DOMDataWriteTransaction wTx = this.domDataBroker.newWriteOnlyTransaction();
        wTx.delete(LogicalDatastoreType.OPERATIONAL, this.yangMonitorId);
        wTx.submit().checkedGet();
        LOG.info("BMP monitoring station {} closed.", this.monitorId.getValue());
    }

}
