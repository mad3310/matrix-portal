package com.letv.portal.service.openstack.resource.manager.impl;

import org.jclouds.openstack.nova.v2_0.domain.Server;

public interface ServerChecker {
	boolean check(Server server) throws Exception;
}
