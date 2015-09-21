package com.letv.portal.service.openstack.impl;

import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.session.SessionServiceImpl;
import com.letv.common.util.ConfigUtil;
import com.letv.common.util.SpringContextUtil;
import com.letv.portal.service.cloudvm.ICloudvmRegionService;
import com.letv.portal.service.cloudvm.ICloudvmVmCountService;
import com.letv.portal.service.openstack.OpenStackService;
import com.letv.portal.service.openstack.OpenStackSession;
import com.letv.portal.service.openstack.exception.OpenStackException;
import com.letv.portal.service.openstack.internal.UserExists;
import com.letv.portal.service.openstack.internal.UserRegister;
import com.letv.portal.service.openstack.password.PasswordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

/**
 * Created by zhouxianguang on 2015/6/8.
 */
@Service("openStackService")
public class OpenStackServiceImpl implements OpenStackService {

	@Value("${openstack.keystone.host}")
	private String keystoneHost;

	@Value("${openstack.keystone.version}")
	private String keystoneVersion;

	@Value("${openstack.keystone.public.port}")
	private String publicPort;

	@Value("${openstack.keystone.admin.port}")
	private String adminPort;

	@Value("${openstack.keystone.protocol}")
	private String protocol;

	@Value("${openstack.keystone.user.register.token}")
	private String userRegisterToken;

	@Value("${openstack.neutron.network.global.public.id}")
	private String globalPublicNetworkId;

	@Value("${openstack.neutron.network.global.shared.id}")
	private String globalSharedNetworkId;

	@Value("${openstack.neutron.network.user.private.name}")
	private String userPrivateNetworkName;

	@Value("${openstack.neutron.network.user.private.subnet.name}")
	private String userPrivateNetworkSubnetName;

	@Value("${openstack.neutron.network.user.private.subnet.cidr}")
	private String userPrivateNetworkSubnetCidr;

	@Value("${openstack.neutron.router.user.private.name}")
	private String userPrivateRouterName;

	private String publicEndpoint;

	private String adminEndpoint;

	private OpenStackConf openStackConf;

	@Autowired
	private PasswordService passwordService;

	@Autowired
	private ICloudvmRegionService cloudvmRegionService;

	@Autowired
	private ITemplateMessageSender defaultEmailSender;

	@Autowired
	private SessionServiceImpl sessionService;
	
	@Autowired
	private ICloudvmVmCountService cloudvmVmCountService;
	
	private OpenStackServiceGroup openStackServiceGroup;
	
	private static OpenStackServiceImpl INSTANCE;

	public OpenStackServiceImpl() {
		INSTANCE=this;
	}
	
	@PostConstruct
	public void open() {
		ConfigUtil.class.getName();
		publicEndpoint = MessageFormat.format("{0}://{1}:{2}/v{3}/", protocol,
				keystoneHost, publicPort, keystoneVersion);
		adminEndpoint = MessageFormat.format("{0}://{1}:{2}/v{3}/", protocol,
				keystoneHost, adminPort, keystoneVersion);
		openStackConf = new OpenStackConf();
		openStackConf.setGlobalPublicNetworkId(globalPublicNetworkId);
		openStackConf.setGlobalSharedNetworkId(globalSharedNetworkId);
		openStackConf.setPublicEndpoint(publicEndpoint);
		openStackConf.setUserPrivateNetworkName(userPrivateNetworkName);
		openStackConf
				.setUserPrivateNetworkSubnetCidr(userPrivateNetworkSubnetCidr);
		openStackConf
				.setUserPrivateNetworkSubnetName(userPrivateNetworkSubnetName);
		openStackConf.setUserPrivateRouterName(userPrivateRouterName);
		openStackConf.setAdminEndpoint(adminEndpoint);
		openStackConf.setUserRegisterToken(userRegisterToken);

		openStackServiceGroup = new OpenStackServiceGroup();
		openStackServiceGroup.setCloudvmRegionService(cloudvmRegionService);
		openStackServiceGroup.setDefaultEmailSender(defaultEmailSender);
		openStackServiceGroup.setPasswordService(passwordService);
		openStackServiceGroup.setSessionService(sessionService);
		openStackServiceGroup.setCloudvmVmCountService(cloudvmVmCountService);
	}

	@Override
	public OpenStackSession createSession(String userId, String email,
			String userName) throws OpenStackException {
		OpenStackUser openStackUser = new OpenStackUser();
		openStackUser.setUserId(userId);
		openStackUser.setEmail(email);
		openStackUser.setUserName(userName);
//		openStackUser.setFirstLogin(false);
		openStackUser.setInternalUser(false);

		return new OpenStackSessionImpl(
				openStackConf, openStackUser);
	}

	public static OpenStackServiceGroup getOpenStackServiceGroup() {
		return INSTANCE.openStackServiceGroup;
	}
}
