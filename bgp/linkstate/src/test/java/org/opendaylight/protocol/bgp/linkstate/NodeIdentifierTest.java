/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.linkstate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import org.junit.Test;

import org.opendaylight.protocol.concepts.ASNumber;
import org.opendaylight.protocol.concepts.IPv4;
import org.opendaylight.protocol.concepts.IPv6;
import org.opendaylight.protocol.concepts.ISOSystemIdentifier;

public class NodeIdentifierTest {
	private final NodeIdentifierFactory f = new NodeIdentifierFactory(new ASNumber(25600, 0), null, null);

	@Test
	public void testHashCodeEquals() {
		final NodeIdentifier id1 = this.f.identifierForRouter(new ISISRouterIdentifier(new ISOSystemIdentifier(new byte[] { 0x22, 0x22,
				0x22, 0x22, 0x22, 0x22 })));
		final NodeIdentifier id2 = this.f.identifierForRouter(new ISISRouterIdentifier(new ISOSystemIdentifier(new byte[] { 0x22, 0x22,
				0x22, 0x22, 0x22, 0x22 })));
		final NodeIdentifier id3 = this.f.identifierForRouter(new IPv4RouterIdentifier(IPv4.FAMILY.addressForString("192.168.1.5")));
		final NodeIdentifier id4 = this.f.identifierForRouter(new IPv6RouterIdentifier(IPv6.FAMILY.addressForString("2001:db8:85a3::8a2e:370:7334")));

		assertEquals("HashCodes should be equal", id1.hashCode(), id2.hashCode());
		assertEquals("toString should be equal", id1.toString(), id2.toString());
		assertEquals(id1, id2);

		assertNotSame(id1, id3);
		assertNotSame(id3, id4);
	}

	@Test
	public void testIPv4RouterIdentifier() {
		try {
			new IPv4RouterIdentifier(null);
			fail("Nullable IPv4RouterIdentifier");
		} catch (final NullPointerException e) {
			assertEquals("Address may not be null", e.getMessage());
		}

		final IPv4RouterIdentifier ip1 = new IPv4RouterIdentifier(IPv4.FAMILY.addressForString("127.0.0.1"));
		final IPv4RouterIdentifier ip2 = new IPv4RouterIdentifier(IPv4.FAMILY.addressForString("127.0.0.1"));

		assertEquals(ip1, ip2);
	}

	@Test
	public void testIPv6RouterIdentifier() {
		try {
			new IPv6RouterIdentifier(null);
			fail("Nullable IPv6RouterIdentifier");
		} catch (final NullPointerException e) {
			assertEquals("Address may not be null", e.getMessage());
		}

		final IPv6RouterIdentifier ip1 = new IPv6RouterIdentifier(IPv6.FAMILY.addressForString("2001:db8:85a3::8a2e:370:7334"));
		final IPv6RouterIdentifier ip2 = new IPv6RouterIdentifier(IPv6.FAMILY.addressForString("2001:db8:85a3::8a2e:370:7334"));

		assertEquals(ip1, ip2);
	}

	@Test
	public void testISISRouterIdentifier() {
		final ISISRouterIdentifier is1 = new ISISRouterIdentifier(new ISOSystemIdentifier(new byte[] { 0x22, 0x22, 0x22, 0x22, 0x22, 0x22 }));
		final ISISRouterIdentifier is2 = new ISISRouterIdentifier(new ISOSystemIdentifier(new byte[] { 0x22, 0x22, 0x22, 0x22, 0x22, 0x22 }));

		assertEquals(is1, is2);
	}

	@Test
	public void testOSPFNodeIdentifier() {
		try {
			new OSPFRouterIdentifier(null);
			fail("Nullable Router Identifier!");
		} catch (final NullPointerException e) {
			assertEquals("Router identifier may not be null", e.getMessage());
		}

		try {
			new OSPFRouterIdentifier(new byte[] { 5 });
			fail("Wrong length of OSPF Router Identifier!");
		} catch (final IllegalArgumentException e) {
			assertEquals("Router identifier must be 4 bytes long", e.getMessage());
		}

		final OSPFRouterIdentifier os1 = new OSPFRouterIdentifier(new byte[] { 5, 5, 5, 5 });
		final OSPFRouterIdentifier os2 = new OSPFRouterIdentifier(new byte[] { 5, 5, 5, 5 });

		assertEquals(os1, os2);
	}
}