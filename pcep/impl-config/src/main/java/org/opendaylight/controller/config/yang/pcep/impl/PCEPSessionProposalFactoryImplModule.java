/**
 * Generated file

 * Generated from: yang module name: pcep-impl  yang module local name: pcep-session-proposal-factory-impl
 * Generated by: org.opendaylight.controller.config.yangjmxgenerator.plugin.JMXGenerator
 * Generated at: Wed Nov 06 13:16:39 CET 2013
 *
 * Do not modify this file unless it is present under src/main directory
 */
package org.opendaylight.controller.config.yang.pcep.impl;

import java.net.InetSocketAddress;

import org.opendaylight.controller.config.api.JmxAttributeValidationException;
import org.opendaylight.protocol.pcep.PCEPSessionProposalFactory;
import org.opendaylight.protocol.pcep.impl.PCEPSessionProposalFactoryImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.open.object.Open;

/**
*
*/
public final class PCEPSessionProposalFactoryImplModule
		extends
		org.opendaylight.controller.config.yang.pcep.impl.AbstractPCEPSessionProposalFactoryImplModule {

	public PCEPSessionProposalFactoryImplModule(
			org.opendaylight.controller.config.api.ModuleIdentifier name,
			org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
		super(name, dependencyResolver);
	}

	public PCEPSessionProposalFactoryImplModule(
			org.opendaylight.controller.config.api.ModuleIdentifier name,
			org.opendaylight.controller.config.api.DependencyResolver dependencyResolver,
			PCEPSessionProposalFactoryImplModule oldModule,
			java.lang.AutoCloseable oldInstance) {
		super(name, dependencyResolver, oldModule, oldInstance);
	}

	@Override
	public void validate() {
		super.validate();
		JmxAttributeValidationException
				.checkCondition(
						getDeadTimerValue() % getKeepAliveTimerValue() != 4,
						"Parameter 'dead timer value' should be 4 times greater than keepAlive timer value.",
						deadTimerValueJmxAttribute);
		if ((getActive() || getVersioned() || getTimeout() > 0)
				&& !getStateful())
			setStateful(true);
	}

	@Override
	public java.lang.AutoCloseable createInstance() {
		PCEPSessionProposalFactoryImpl inner = new PCEPSessionProposalFactoryImpl(
				getDeadTimerValue(), getKeepAliveTimerValue(), getStateful(),
				getActive(), getVersioned(), getInstantiated(), getTimeout());
		return new PCEPSessionProposalFactoryCloseable(inner);
	}

	private static final class PCEPSessionProposalFactoryCloseable implements
			PCEPSessionProposalFactory, AutoCloseable {

		private PCEPSessionProposalFactoryImpl inner;

		public PCEPSessionProposalFactoryCloseable(
				PCEPSessionProposalFactoryImpl inner) {
			this.inner = inner;
		}

		@Override
		public void close() throws Exception {
		}

		@Override
		public Open getSessionProposal(
				InetSocketAddress inetSocketAddress, int i) {
			return inner.getSessionProposal(inetSocketAddress, i);
		}
	}
}