package com.letv.portal.service.openstack.resource.manager;

import java.util.Map;
import java.util.Set;

import com.letv.portal.service.openstack.exception.OpenStackException;
import com.letv.portal.service.openstack.resource.Region;

public interface ResourceManager {
	public Set<String> getRegions();
	public Map<String, Map<String, Map<Integer, Region>>> getGroupRegions() throws OpenStackException;
}
