/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.pcep.impl.object;

import org.opendaylight.protocol.concepts.Ipv4Util;
import org.opendaylight.protocol.concepts.Ipv6Util;
import org.opendaylight.protocol.pcep.PCEPDeserializerException;
import org.opendaylight.protocol.pcep.PCEPDocumentedException;
import org.opendaylight.protocol.pcep.PCEPErrors;
import org.opendaylight.protocol.pcep.spi.AbstractObjectParser;
import org.opendaylight.protocol.pcep.spi.SubobjectHandlerRegistry;
import org.opendaylight.protocol.pcep.spi.TlvHandlerRegistry;
import org.opendaylight.protocol.util.ByteArray;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.EndpointsObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.Object;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.ObjectHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.Tlv;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.endpoints.object.AddressFamily;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.endpoints.object.address.family.Ipv4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.endpoints.object.address.family.Ipv4Builder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.endpoints.object.address.family.Ipv6;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.endpoints.object.address.family.Ipv6Builder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.pcinitiate.message.pcinitiate.message.requests.EndpointsBuilder;

/**
 * Parser for IPv4 {@link EndpointsObject}
 */
public class PCEPEndPointsObjectParser extends AbstractObjectParser<EndpointsBuilder> {

	public static final int CLASS = 4;

	public static final int TYPE = 1;

	/*
	 * fields lengths and offsets for IPv4 in bytes
	 */
	public static final int SRC4_F_LENGTH = 4;
	public static final int DEST4_F_LENGTH = 4;

	public static final int SRC4_F_OFFSET = 0;
	public static final int DEST4_F_OFFSET = SRC4_F_OFFSET + SRC4_F_LENGTH;

	public static final int CLASS_6 = 4;

	public static final int TYPE_6 = 2;

	public static final int SRC6_F_LENGTH = 16;
	public static final int DEST6_F_LENGTH = 16;

	public static final int SRC6_F_OFFSET = 0;
	public static final int DEST6_F_OFFSET = SRC6_F_OFFSET + SRC6_F_LENGTH;

	public PCEPEndPointsObjectParser(final SubobjectHandlerRegistry subobjReg, final TlvHandlerRegistry tlvReg) {
		super(subobjReg, tlvReg);
	}

	@Override
	public EndpointsObject parseObject(final ObjectHeader header, final byte[] bytes) throws PCEPDeserializerException,
	PCEPDocumentedException {
		if (bytes == null) {
			throw new IllegalArgumentException("Array of bytes is mandatory");
		}

		if (!header.isProcessingRule()) {
			throw new PCEPDocumentedException("Processed flag not set", PCEPErrors.P_FLAG_NOT_SET);
		}

		final EndpointsBuilder builder = new EndpointsBuilder();
		builder.setIgnore(header.isIgnore());
		builder.setProcessingRule(header.isProcessingRule());

		if (bytes.length == SRC4_F_LENGTH + DEST4_F_LENGTH) {
			final Ipv4Builder b = new Ipv4Builder();
			b.setSourceIpv4Address(Ipv4Util.addressForBytes(ByteArray.subByte(bytes, SRC4_F_OFFSET, SRC4_F_LENGTH)));
			b.setDestinationIpv4Address((Ipv4Util.addressForBytes(ByteArray.subByte(bytes, DEST4_F_OFFSET, DEST4_F_LENGTH))));
			builder.setAddressFamily(b.build());
		} else if (bytes.length == SRC6_F_LENGTH + DEST6_F_LENGTH) {
			final Ipv6Builder b = new Ipv6Builder();
			b.setSourceIpv6Address(Ipv6Util.addressForBytes(ByteArray.subByte(bytes, SRC6_F_OFFSET, SRC6_F_LENGTH)));
			b.setDestinationIpv6Address((Ipv6Util.addressForBytes(ByteArray.subByte(bytes, DEST6_F_OFFSET, DEST6_F_LENGTH))));
			builder.setAddressFamily(b.build());
		} else {
			throw new PCEPDeserializerException("Wrong length of array of bytes.");
		}
		return builder.build();
	}

	@Override
	public void addTlv(final EndpointsBuilder builder, final Tlv tlv) {
		// No tlvs defined
	}

	@Override
	public byte[] serializeObject(final Object object) {
		if (!(object instanceof EndpointsObject)) {
			throw new IllegalArgumentException("Wrong instance of PCEPObject. Passed " + object.getClass() + ". Needed EndpointsObject.");
		}

		final EndpointsObject ePObj = (EndpointsObject) object;

		final AddressFamily afi = ePObj.getAddressFamily();

		if (afi instanceof Ipv4) {
			final byte[] retBytes = new byte[SRC4_F_LENGTH + DEST4_F_LENGTH];
			ByteArray.copyWhole(Ipv4Util.bytesForAddress(((Ipv4) afi).getSourceIpv4Address()), retBytes, SRC4_F_OFFSET);
			ByteArray.copyWhole(Ipv4Util.bytesForAddress(((Ipv4) afi).getDestinationIpv4Address()), retBytes, DEST4_F_OFFSET);
			return retBytes;
		} else if (afi instanceof Ipv6) {
			final byte[] retBytes = new byte[SRC6_F_LENGTH + DEST6_F_LENGTH];
			ByteArray.copyWhole(Ipv6Util.bytesForAddress(((Ipv6) afi).getSourceIpv6Address()), retBytes, SRC6_F_OFFSET);
			ByteArray.copyWhole(Ipv6Util.bytesForAddress(((Ipv6) afi).getDestinationIpv6Address()), retBytes, DEST6_F_OFFSET);
			return retBytes;
		} else {
			throw new IllegalArgumentException("Wrong instance of NetworkAddress. Passed " + afi.getClass() + ". Needed IPv4");
		}
	}

	@Override
	public int getObjectType() {
		return TYPE;
	}

	@Override
	public int getObjectClass() {
		return CLASS;
	}

	public int get6ObjectType() {
		return TYPE_6;
	}

	public int get6ObjectClass() {
		return CLASS_6;
	}
}
