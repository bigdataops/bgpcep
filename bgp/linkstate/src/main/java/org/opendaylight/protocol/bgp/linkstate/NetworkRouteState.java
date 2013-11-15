/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.protocol.bgp.linkstate;

import org.opendaylight.protocol.bgp.concepts.NextHop;

import org.opendaylight.protocol.concepts.NetworkAddress;
import com.google.common.base.Preconditions;

/**
 * A single route existing within the network. A route is a way how to get from the local node to a set of network
 * addresses. The set of addresses is represented as a Prefix of a particular address type and is the unique identifier
 * for the route. The routing part is represented as the directly-connected neighbor, which should be used used as a
 * relay for traffic going to the set of addresses.
 * 
 * @param <T>
 */
public final class NetworkRouteState<T extends NetworkAddress<?>> extends NetworkObjectState {
	private static final long serialVersionUID = 1L;
	private final NextHop<T> nextHop;

	public NetworkRouteState(final NextHop<T> nextHop) {
		this(NetworkObjectState.EMPTY, nextHop);
	}

	public NetworkRouteState(final NetworkObjectState orig, final NextHop<T> nextHop) {
		super(orig);
		this.nextHop = Preconditions.checkNotNull(nextHop);
	}

	protected NetworkRouteState(final NetworkRouteState<T> orig) {
		super(orig);
		this.nextHop = orig.nextHop;
	}

	@Override
	protected NetworkRouteState<T> newInstance() {
		return new NetworkRouteState<T>(this);
	}

	/**
	 * Get the relay for the set of addresses covered by this route.
	 * 
	 * @return NextHop<T> next hop.
	 */
	public NextHop<T> getNextHop() {
		return this.nextHop;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.nextHop == null) ? 0 : this.nextHop.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NetworkRouteState<?> other = (NetworkRouteState<?>) obj;
		if (this.nextHop == null) {
			if (other.nextHop != null)
				return false;
		} else if (!this.nextHop.equals(other.nextHop))
			return false;
		return true;
	}
}