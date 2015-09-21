package com.letv.portal.service.openstack.impl;

import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.session.SessionServiceImpl;
import com.letv.portal.service.cloudvm.ICloudvmRegionService;
import com.letv.portal.service.cloudvm.ICloudvmVmCountService;
import com.letv.portal.service.openstack.password.PasswordService;

public class OpenStackServiceGroup {
	private ICloudvmRegionService cloudvmRegionService;
	private ITemplateMessageSender defaultEmailSender;
	private PasswordService passwordService;
	private SessionServiceImpl sessionService;
	private ICloudvmVmCountService cloudvmVmCountService;
	
	public ICloudvmRegionService getCloudvmRegionService() {
		return cloudvmRegionService;
	}

	public void setCloudvmRegionService(
			ICloudvmRegionService cloudvmRegionService) {
		this.cloudvmRegionService = cloudvmRegionService;
	}

	public ITemplateMessageSender getDefaultEmailSender() {
		return defaultEmailSender;
	}

	public void setDefaultEmailSender(ITemplateMessageSender defaultEmailSender) {
		this.defaultEmailSender = defaultEmailSender;
	}

	public PasswordService getPasswordService() {
		return passwordService;
	}

	public void setPasswordService(PasswordService passwordService) {
		this.passwordService = passwordService;
	}

	public SessionServiceImpl getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionServiceImpl sessionService) {
		this.sessionService = sessionService;
	}

	public ICloudvmVmCountService getCloudvmVmCountService() {
		return cloudvmVmCountService;
	}

	public void setCloudvmVmCountService(
			ICloudvmVmCountService cloudvmVmCountService) {
		this.cloudvmVmCountService = cloudvmVmCountService;
	}
}
