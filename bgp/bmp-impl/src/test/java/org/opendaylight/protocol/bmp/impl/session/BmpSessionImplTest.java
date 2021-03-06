/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.protocol.bmp.impl.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opendaylight.protocol.bmp.api.BmpSession;
import org.opendaylight.protocol.bmp.impl.test.TestUtil;
import org.opendaylight.protocol.bmp.spi.parser.BmpDeserializationException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bmp.message.rev150512.InitiationMessage;

public class BmpSessionImplTest {

    private static final SocketAddress FAKE_ADDRESS = InetSocketAddress.createUnresolved("localhost", 12345);

    private BmpSession session;
    private EmbeddedChannel channel;
    private BmpTestSessionListener listener;
    @Mock
    private Channel mockedChannel;
    @Mock
    private ChannelHandlerContext mockedHandlerContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.doReturn(FAKE_ADDRESS).when(this.mockedChannel).localAddress();
        Mockito.doReturn(FAKE_ADDRESS).when(this.mockedChannel).remoteAddress();
        Mockito.doReturn(null).when(this.mockedChannel).close();
        Mockito.doReturn("").when(this.mockedChannel).toString();
        Mockito.doReturn(null).when(this.mockedHandlerContext).channel();
        Mockito.doReturn(null).when(this.mockedHandlerContext).fireChannelInactive();
        this.listener = new BmpTestSessionListener();
        this.session = new BmpSessionImpl(this.listener, this.mockedChannel);
        this.channel = new EmbeddedChannel(this.session);
        Assert.assertTrue(this.listener.isUp());
    }

    @Test
    public void testOnInitiate() {
        this.channel.writeInbound(createInitMsg());
        Assert.assertEquals(this.listener.getListMsg().size(), 1);
        this.channel.writeInbound(createInitMsg());
        Assert.assertEquals(this.listener.getListMsg().size(), 2);
    }

    @Test
    public void testOnTerminationAfterInitiated() {
        this.channel.writeInbound(createInitMsg());
        Assert.assertEquals(this.listener.getListMsg().size(), 1);
        this.channel.writeInbound(TestUtil.createTerminationMsg());
        Assert.assertEquals(this.listener.getListMsg().size(), 1);
        Mockito.verify(this.mockedChannel, Mockito.times(1)).close();
    }

    @Test
    public void testOnTermination() {
        this.channel.writeInbound(TestUtil.createTerminationMsg());
        Assert.assertEquals(this.listener.getListMsg().size(), 0);
        Mockito.verify(this.mockedChannel, Mockito.times(1)).close();
    }

    @Test
    public void testOnUnexpectedMessage() {
        this.channel.writeInbound(TestUtil.createPeerDownFSM());
        Assert.assertEquals(this.listener.getListMsg().size(), 0);
        Mockito.verify(this.mockedChannel, Mockito.times(1)).close();
    }

    @Test
    public void testExceptionCaught() {
        try {
            this.session.exceptionCaught(this.mockedHandlerContext, new BmpDeserializationException(""));
            Assert.assertEquals(this.listener.getListMsg().size(), 0);
            Mockito.verify(this.mockedChannel, Mockito.times(1)).close();
        } catch (final Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testChannelInactive() {
        try {
            this.session.channelInactive(this.mockedHandlerContext);
            Assert.assertEquals(this.listener.getListMsg().size(), 0);
            Assert.assertFalse(this.listener.isUp());
        } catch (final Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private static InitiationMessage createInitMsg() {
        return TestUtil.createInitMsg("", "", "");
    }

}
