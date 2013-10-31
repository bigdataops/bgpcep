/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.pcep.impl.subobject;

import org.opendaylight.protocol.pcep.PCEPDeserializerException;
import org.opendaylight.protocol.pcep.impl.Util.BiParsersMap;
import org.opendaylight.protocol.pcep.subobject.RROProtectionSubobject;
import org.opendaylight.protocol.pcep.subobject.RROProtectionType1Subobject;
import org.opendaylight.protocol.pcep.subobject.RROProtectionType2Subobject;
import org.opendaylight.protocol.util.ByteArray;

public class RROProtectionSubobjectParser {

    public static final int RES_F_LENGTH = 1;

    public static final int C_TYPE_F_LENGTH = 1;

    public static final int RES_F_OFFSET = 0;

    public static final int C_TYPE_F_OFFSET = RES_F_OFFSET + RES_F_LENGTH;

    public static final int HEADER_LENGTH = C_TYPE_F_OFFSET + C_TYPE_F_LENGTH;

    public static final int U_FLAG_OFFSET = 0;

    private static class MapOfParsers extends BiParsersMap<Class<? extends RROProtectionSubobject>, Integer, RROProtectionParser> {
	private final static MapOfParsers instance = new MapOfParsers();

	private MapOfParsers() {
	    this.fillInMap();
	}

	private void fillInMap() {
	    this.put(RROProtectionType1Subobject.class, 1, new RROProtectionType1SubobjectParser());
	    this.put(RROProtectionType2Subobject.class, 2, new RROProtectionType2SubobjectParser());
	}

	public static MapOfParsers getInstance() {
	    return instance;
	}
    }

    public static RROProtectionSubobject parse(byte[] soContentsBytes) throws PCEPDeserializerException {
	if (soContentsBytes == null || soContentsBytes.length == 0)
	    throw new IllegalArgumentException("Array of bytes is mandatory. Can't be null or empty.");
	if (soContentsBytes.length < HEADER_LENGTH)
	    throw new PCEPDeserializerException("Wrong length of array of bytes. Passed: " + soContentsBytes.length + "; Expected: >" + HEADER_LENGTH + ".");

	final int c_type = soContentsBytes[C_TYPE_F_OFFSET] & 0xFF;

	final RROProtectionParser parser = MapOfParsers.getInstance().getValueFromKeyValue(c_type);

	if (parser == null) {
	    throw new PCEPDeserializerException("Unknown C-TYPE for ero protection subobject. Passed: " + c_type);
	}

	return parser.parse(ByteArray.cutBytes(soContentsBytes, HEADER_LENGTH));
    }

    public static byte[] put(RROProtectionSubobject objToSerialize) {
	final Integer c_type = MapOfParsers.getInstance().getKeyValueFromKey(objToSerialize.getClass());
	final RROProtectionParser parser = MapOfParsers.getInstance().getValueFromKeyValue(c_type);

	if (c_type == null || parser == null)
	    throw new IllegalArgumentException("Unknown EROProtectionSubobject instance. Passed " + objToSerialize.getClass());

	final byte[] protBytes = parser.put(objToSerialize);

	final byte[] retBytes = new byte[protBytes.length + HEADER_LENGTH];

	System.arraycopy(protBytes, 0, retBytes, HEADER_LENGTH, protBytes.length);

	retBytes[C_TYPE_F_OFFSET] = (byte) c_type.intValue();

	return retBytes;
    }
}