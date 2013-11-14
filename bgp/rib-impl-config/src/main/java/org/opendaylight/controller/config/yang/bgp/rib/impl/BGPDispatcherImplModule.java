/**
 * Generated file

 * Generated from: yang module name: bgp-rib-impl  yang module local name: bgp-dispatcher-impl
 * Generated by: org.opendaylight.controller.config.yangjmxgenerator.plugin.JMXGenerator
 * Generated at: Wed Nov 06 13:02:32 CET 2013
 *
 * Do not modify this file unless it is present under src/main directory
 */
package org.opendaylight.controller.config.yang.bgp.rib.impl;

import org.opendaylight.protocol.bgp.parser.BGPMessageFactory;
import org.opendaylight.protocol.bgp.rib.impl.BGPDispatcherImpl;

/**
*
*/
public final class BGPDispatcherImplModule
		extends
		org.opendaylight.controller.config.yang.bgp.rib.impl.AbstractBGPDispatcherImplModule {

	public BGPDispatcherImplModule(
			org.opendaylight.controller.config.api.ModuleIdentifier name,
			org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
		super(name, dependencyResolver);
	}

	public BGPDispatcherImplModule(
			org.opendaylight.controller.config.api.ModuleIdentifier name,
			org.opendaylight.controller.config.api.DependencyResolver dependencyResolver,
			BGPDispatcherImplModule oldModule,
			java.lang.AutoCloseable oldInstance) {
		super(name, dependencyResolver, oldModule, oldInstance);
	}

	@Override
	public void validate() {
		super.validate();
		// Add custom validation for module attributes here.
	}

	@Override
	public java.lang.AutoCloseable createInstance() {
		final BGPMessageFactory messageFactoryDependency = getMessageFactoryDependency();
		return new BGPDispatcherImpl(messageFactoryDependency,
				getBossGroupDependency(), getWorkerGroupDependency());
	}
}