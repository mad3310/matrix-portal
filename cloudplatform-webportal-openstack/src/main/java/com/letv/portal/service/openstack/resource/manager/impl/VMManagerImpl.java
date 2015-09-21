package com.letv.portal.service.openstack.resource.manager.impl;

import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.CharUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jclouds.openstack.cinder.v1.domain.Volume;
import org.jclouds.openstack.cinder.v1.domain.VolumeAttachment;
import org.jclouds.openstack.neutron.v2.domain.Network;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Console;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.FloatingIP;
import org.jclouds.openstack.nova.v2_0.domain.Quota;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.Server.Status;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.extensions.ConsolesApi;
import org.jclouds.openstack.nova.v2_0.extensions.FloatingIPApi;
import org.jclouds.openstack.nova.v2_0.extensions.QuotaApi;
import org.jclouds.openstack.nova.v2_0.extensions.VolumeAttachmentApi;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;
import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.letv.common.email.bean.MailMessage;
import com.letv.common.paging.impl.Page;
import com.letv.common.util.PasswordRandom;
import com.letv.portal.model.cloudvm.CloudvmVmCount;
import com.letv.portal.service.cloudvm.ICloudvmVmCountService;
import com.letv.portal.service.openstack.exception.APINotAvailableException;
import com.letv.portal.service.openstack.exception.OpenStackException;
import com.letv.portal.service.openstack.exception.PollingInterruptedException;
import com.letv.portal.service.openstack.exception.RegionNotFoundException;
import com.letv.portal.service.openstack.exception.ResourceNotFoundException;
import com.letv.portal.service.openstack.exception.TaskNotFinishedException;
import com.letv.portal.service.openstack.exception.VMDeleteException;
import com.letv.portal.service.openstack.exception.VMStatusException;
import com.letv.portal.service.openstack.impl.OpenStackConf;
import com.letv.portal.service.openstack.impl.OpenStackServiceImpl;
import com.letv.portal.service.openstack.impl.OpenStackUser;
import com.letv.portal.service.openstack.resource.FlavorResource;
import com.letv.portal.service.openstack.resource.NetworkResource;
import com.letv.portal.service.openstack.resource.VMResource;
import com.letv.portal.service.openstack.resource.VolumeResource;
import com.letv.portal.service.openstack.resource.impl.FlavorResourceImpl;
import com.letv.portal.service.openstack.resource.impl.VMResourceImpl;
import com.letv.portal.service.openstack.resource.impl.VolumeResourceImpl;
import com.letv.portal.service.openstack.resource.manager.ImageManager;
import com.letv.portal.service.openstack.resource.manager.RegionAndVmId;
import com.letv.portal.service.openstack.resource.manager.VMCreateConf;
import com.letv.portal.service.openstack.resource.manager.VMManager;
import com.letv.portal.service.openstack.resource.manager.impl.task.AddVolumes;
import com.letv.portal.service.openstack.resource.manager.impl.task.BindFloatingIP;
import com.letv.portal.service.openstack.resource.manager.impl.task.WaitingVMCreated;

public class VMManagerImpl extends AbstractResourceManager<NovaApi> implements VMManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1012274540203347510L;

	@SuppressWarnings("unused")
	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(VMManager.class);

//	private NovaApi novaApi;

	private ImageManager imageManager;

	private NetworkManagerImpl networkManager;

	private VolumeManagerImpl volumeManager;

	// private IdentityManagerImpl identityManager;
	
	public VMManagerImpl() {
	}

	public VMManagerImpl(
			OpenStackConf openStackConf, OpenStackUser openStackUser) {
		super(openStackConf, openStackUser);

//		Iterable<Module> modules = ImmutableSet
//				.<Module> of(new SLF4JLoggingModule());
//
//		novaApi = ContextBuilder
//				.newBuilder("openstack-nova")
//				.endpoint(openStackConf.getPublicEndpoint())
//				.credentials(
//						openStackUser.getUserId() + ":"
//								+ openStackUser.getUserId(),
//						openStackUser.getPassword()).modules(modules)
//				.buildApi(NovaApi.class);
	}

	@Override
	public void close() throws IOException {
//		novaApi.close();
	}

	@Override
	public Set<String> getRegions() throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi,Set<String>>() {
			
			@Override
			public Set<String> run(NovaApi api) throws Exception {
				return api.getConfiguredRegions();
			}
		});
	}

	@Override
	public List<VMResource> list(final String region) throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi,List<VMResource>>() {
			
			@Override
			public List<VMResource> run(NovaApi novaApi) throws Exception {
				checkRegion(region);

				String regionDisplayName = getRegionDisplayName(region);

				ServerApi serverApi = novaApi.getServerApi(region);
				List<Server> resources = serverApi.listInDetail().concat().toList();
				List<VMResource> vmResources = new ArrayList<VMResource>(
						resources.size());
				for (Server resource : resources) {
					vmResources.add(new VMResourceImpl(region, regionDisplayName,
							resource, VMManagerImpl.this, imageManager, openStackUser));
				}
				
				return vmResources;
			}
		});
	}

	@Override
	public Page listAll(String name, Integer currentPage, Integer recordsPerPage)
			throws OpenStackException {
		Set<String> regions = getRegions();
		return listByRegions(regions, name, currentPage, recordsPerPage);
	}

	@Override
	public Page listByRegionGroup(String regionGroup, String name,
			Integer currentPage, Integer recordsPerPage)
			throws RegionNotFoundException, ResourceNotFoundException,
			APINotAvailableException, OpenStackException {
		Set<String> groupRegions = getGroupRegions(regionGroup);
		return listByRegions(groupRegions, name, currentPage, recordsPerPage);
	}

	/**
	 * 
	 * @param regions
	 * @param name
	 * @param currentPage
	 *            从1开始
	 * @param recordsPerPage
	 * @return
	 * @throws RegionNotFoundException
	 * @throws ResourceNotFoundException
	 * @throws APINotAvailableException
	 * @throws OpenStackException
	 */
	private Page listByRegions(final Set<String> regions,final String name,
			final Integer currentPagePara, final Integer recordsPerPage)
			throws RegionNotFoundException, ResourceNotFoundException,
			APINotAvailableException, OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi,Page>() {
			
			@Override
			public Page run(NovaApi novaApi) throws Exception {
				Integer currentPage;
				if (currentPagePara != null) {
					currentPage = currentPagePara - 1;
				}else{
					currentPage = null;
				}

				Map<String, String> transMap = getRegionCodeToDisplayNameMap();
				List<VMResource> vmResources = new LinkedList<VMResource>();
				int serverCount = 0;
				boolean needCollect = true;
				for (String region : regions) {
					ServerApi serverApi = novaApi.getServerApi(region);
					if (needCollect) {
						String regionDisplayName = transMap.get(region);
						List<Server> resources = serverApi.listInDetail().concat()
								.toList();
						for (Server resource : resources) {
							if (name == null
									|| (resource.getName() != null && resource
											.getName().contains(name))) {
								if (currentPage == null || recordsPerPage == null) {
									vmResources.add(new VMResourceImpl(region,
											regionDisplayName, resource, VMManagerImpl.this,
											imageManager, openStackUser));
								} else {
									if (needCollect) {
										if (serverCount >= (currentPage + 1)
												* recordsPerPage) {
											needCollect = false;
										} else if (serverCount >= currentPage
												* recordsPerPage) {
											vmResources.add(new VMResourceImpl(region,
													regionDisplayName, resource, VMManagerImpl.this,
													imageManager, openStackUser));
										}
									}
								}
								serverCount++;
							}
						}
					} else {
						for (org.jclouds.openstack.v2_0.domain.Resource resource : serverApi
								.list().concat().toList()) {
							if (name == null
									|| (resource.getName() != null && resource
											.getName().contains(name))) {
								serverCount++;
							}
						}
					}
				}

				Page page = new Page();
				page.setData(vmResources);
				page.setTotalRecords(serverCount);
				if (recordsPerPage != null) {
					page.setRecordsPerPage(recordsPerPage);
				} else {
					page.setRecordsPerPage(10);
				}
				if (currentPage != null) {
					page.setCurrentPage(currentPage + 1);
				} else {
					page.setCurrentPage(1);
				}

				return page;
			}
		});
	}

	@Override
	public VMResource get(final String region, final String id) throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi, VMResource>() {

			@Override
			public VMResource run(NovaApi novaApi) throws Exception {
				checkRegion(region);

				ServerApi serverApi = novaApi.getServerApi(region);
				Server server = serverApi.get(id);
				if (server != null) {
					String regionDisplayName = getRegionDisplayName(region);
					VMResourceImpl vmResourceImpl = new VMResourceImpl(region,
							regionDisplayName, server, VMManagerImpl.this, imageManager,
							openStackUser);
					vmResourceImpl.setVolumes(volumeManager.getOfVM(region,
							regionDisplayName, id));
					return vmResourceImpl;
				} else {
					throw new ResourceNotFoundException("VM", "虚拟机", id);
				}
			}
			
		});
	}

	@Override
	public VMResource create(final String region, final VMCreateConf conf)
			throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi, VMResource>() {

			@Override
			public VMResource run(NovaApi novaApi) throws Exception {
		
		checkUserEmail();

		checkRegion(region);
		final String regionDisplayName = getRegionDisplayName(region);

		FloatingIP floatingIP = null;
		List<Volume> volumes = null;

		try {
			List<Integer> volumeSizes = null;
			if (conf.getVolumeSizesJson() != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					volumeSizes = objectMapper.readValue(
							conf.getVolumeSizesJson(),
							new TypeReference<List<Integer>>() {
							});
				} catch (JsonParseException e) {
					throw new OpenStackException("请求数据格式错误", e);
				} catch (JsonMappingException e) {
					throw new OpenStackException("请求数据格式错误", e);
				} catch (IOException e) {
					throw new OpenStackException("后台服务错误", e);
				}
			}

			CreateServerOptions createServerOptions = new CreateServerOptions();

			Set<String> networks = new LinkedHashSet<String>();
			{
//				NetworkManagerImpl networkManagerImpl = (NetworkManagerImpl) networkManager;
//				NeutronApi neutronApi = networkManagerImpl.getNeutronApi();
//				NetworkApi networkApi = neutronApi.getNetworkApi(region);
//				for (Network network : networkApi.list().concat().toList()) {
//					if (openStackConf.getUserPrivateNetworkName().equals(
//							network.getName())) {
//						networks.add(network.getId());
//						break;
//					}
//				}
				Network userPrivateNetwork=networkManager.getOrCreateUserPrivateNetwork(region);
				if(userPrivateNetwork!=null) {
					networks.add(userPrivateNetwork.getId());
				}

				List<NetworkResource> networkResources = conf.getNetworkResources();
				if (networkResources != null) {
					for (int i = 0; i < networkResources.size(); i++) {
						networks.add(networkResources.get(i).getId());
					}
				}
				
				if (openStackUser.getInternalUser()&&!openStackConf.getGlobalSharedNetworkId().isEmpty()) {
					networks.add(openStackConf.getGlobalSharedNetworkId());
				}
			}
			createServerOptions.networks(networks);

			if (conf.getAdminPass() == null || conf.getAdminPass().isEmpty()) {
				conf.setAdminPass(PasswordRandom.genStr(10));
			} else {
				for (char ch : conf.getAdminPass().toCharArray()) {
					if (!CharUtils.isAsciiAlphanumeric(ch)) {
						throw new OpenStackException(
								"User password contains illegal characters.",
								"用户密码包含不合法的字符");
					}
				}
			}
			createServerOptions.adminPass(conf.getAdminPass());
			// createServerOptions.userData(MessageFormat.format(
			// "#!/bin/sh\npasswd root<<EOF\n{0}\n{0}\nEOF\n",
			// conf.getAdminPass()).getBytes(Charsets.UTF_8));

			// test code begin(ssh login)
			{
				// Optional<SecurityGroupApi> securityGroupApi = novaApi
				// .getSecurityGroupApi(region);
				// if (securityGroupApi.isPresent()) {
				// SecurityGroup defaultSecurityGroup = null;
				// List<SecurityGroup> securityGroups = securityGroupApi.get()
				// .list().toList();
				// for (SecurityGroup securityGroup : securityGroups) {
				// if ("default".equals(securityGroup.getName())) {
				// defaultSecurityGroup = securityGroup;
				// break;
				// }
				// }
				// if (defaultSecurityGroup != null) {
				// } else {
				// throw new
				// OpenStackException("Default security group is not found.");
				// }
				// } else {
				// throw new APINotAvailableException(SecurityGroupApi.class);
				// }

				createServerOptions.securityGroupNames("default");
			}
			// test code end

			final ServerApi serverApi = novaApi.getServerApi(region);

			{
				FlavorApi flavorApi = novaApi.getFlavorApi(region);
				FlavorResource flavorResource = conf.getFlavorResource();
				int serverCount = 1, serverTotalVcpus = flavorResource
						.getVcpus(), serverTotalRam = flavorResource.getRam();
				List<Server> servers = serverApi.listInDetail().concat()
						.toList();
				Map<String, Flavor> idToFlavor = new HashMap<String, Flavor>();
				for (Server server : servers) {
					serverCount++;
					String flavorId = server.getFlavor().getId();
					Flavor flavor = idToFlavor.get(flavorId);
					if (flavor == null) {
						flavor = flavorApi.get(flavorId);
						if (flavor == null) {

						}
						idToFlavor.put(flavorId, flavor);
					}
					serverTotalVcpus += flavor.getVcpus();
					serverTotalRam += flavor.getRam();
				}

				Optional<QuotaApi> quotaApiOptional = novaApi
						.getQuotaApi(region);
				if (!quotaApiOptional.isPresent()) {
					throw new APINotAvailableException(QuotaApi.class);
				}

				Quota quota = quotaApiOptional.get().getByTenant(
						openStackUser.getTenantId());
				if (quota == null) {
					throw new OpenStackException("VM quota is not available.",
							"虚拟机配额不可用。");
				}

				if (serverCount > quota.getInstances()) {
					throw new OpenStackException(
							"VM count exceeding the quota.", "虚拟机数量超过配额。");
				}
				if (serverTotalVcpus > quota.getCores()) {
					throw new OpenStackException(
							"Vcpu count exceeding the quota.", "虚拟CPU数量超过配额。");
				}
				if (serverTotalRam > quota.getRam()) {
					throw new OpenStackException(
							"Ram amounts exceeding the quota.", "内存总量超过配额。");
				}
			}

			if (conf.getBindFloatingIP()) {
				floatingIP = allocFloatingIP(region);
			}

			if (volumeSizes != null && !volumeSizes.isEmpty()) {
				volumes = volumeManager.create(region, volumeSizes);
				// Set<BlockDeviceMapping> blockDeviceMappings = new
				// HashSet<BlockDeviceMapping>();
				// char deviceNameSuffix = 'e';
				// for (Volume volume : volumes) {
				// blockDeviceMappings.add(BlockDeviceMapping.builder()
				// .uuid(volume.getId()).deviceName("/dev/vdc")
				// // .deviceName("/dev/vd" + deviceNameSuffix)
				// .sourceType("blank").build());
				// deviceNameSuffix++;
				// }
				// createServerOptions.blockDeviceMappings(blockDeviceMappings);
			}

			final ServerCreated serverCreated = serverApi.create(
					conf.getName(), conf.getImageResource().getId(), conf
							.getFlavorResource().getId(), createServerOptions);
			incVmCount();
			Server server = serverApi.get(serverCreated.getId());
			VMResourceImpl vmResourceImpl = new VMResourceImpl(region,
					regionDisplayName, server, VMManagerImpl.this, imageManager,
					openStackUser);

			emailVmCreated(vmResourceImpl, conf);

			List<Runnable> afterTasks = new LinkedList<Runnable>();
			if (floatingIP != null) {
				afterTasks.add(new BindFloatingIP(VMManagerImpl.this, imageManager, region,
						server, floatingIP));
			}
			if (volumes != null) {
				afterTasks.add(new AddVolumes(VMManagerImpl.this, volumeManager, region,
						server, volumes));
			}
			if (!afterTasks.isEmpty()) {
				new Thread(new WaitingVMCreated(VMManagerImpl.this, region,
						serverCreated.getId(), afterTasks)).start();
			}

			// test code begin(ssh login)
			// {
			// Optional<FloatingIPApi> floatingIPApiOptional = novaApi
			// .getFloatingIPApi(region);
			// if (!floatingIPApiOptional.isPresent()) {
			// throw new APINotAvailableException(FloatingIPApi.class);
			// }
			// FloatingIPApi floatingIPApi = floatingIPApiOptional.get();
			// // FloatingIP floatingIP = floatingIPApi.create();
			// for(Entry<String,Address> entry:server.getAddresses().entries()){
			// logger.info(MessageFormat.format("server address: {0} => {1}",
			// entry.getKey(),entry.getValue().getAddr()));
			// }
			// // floatingIPApi.addToServer(floatingIP.getIp(), server.getId());
			// }
			// test code end

			return vmResourceImpl;
		} catch (Exception ex) {
			if (floatingIP != null) {
				deleteFloatingIP(region, floatingIP);
			}
			if (volumes != null) {
				volumeManager.delete(region, volumes);
			}
			if (ex instanceof OpenStackException) {
				throw (OpenStackException) ex;
			} else {
				if(ex.getMessage()!=null && ex.getMessage().contains("Flavor's disk is too small for requested image.")){
					throw new OpenStackException("硬件配置过低，不满足镜像的要求。",ex);
				}
				throw new OpenStackException("后台服务错误", ex);
			}
		}
}
			
		});
	}

	/**
	 * send email to user after vm creating
	 */
	public void emailVmCreated(VMResource vm, VMCreateConf conf)
			throws OpenStackException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", openStackUser.getUserName());
		params.put("region", getRegionDisplayName(vm.getRegion()));
		params.put("vmId", vm.getId());
		params.put("vmName", vm.getName());
		params.put("adminUserName", "root");
		params.put("password", conf.getAdminPass());
		params.put("createTime", format.format(new Date(vm.getCreated())));

		MailMessage mailMessage = new MailMessage("乐视云平台web-portal系统",
				openStackUser.getEmail(), "乐视云平台web-portal系统通知",
				"cloudvm/createVm.ftl", params);
		mailMessage.setHtml(true);
		OpenStackServiceImpl.getOpenStackServiceGroup().getDefaultEmailSender().sendMessage(mailMessage);
	}

	/**
	 * send email to user after vm binding floating IP
	 */
	public void emailBindIP(VMResource vm, String floatingIP)
			throws OpenStackException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", openStackUser.getUserName());
		params.put("region", getRegionDisplayName(vm.getRegion()));
		params.put("vmId", vm.getId());
		params.put("vmName", vm.getName());
		params.put("ip", floatingIP);
		params.put("port", 22);
		params.put("bindTime", format.format(new Date()));

		MailMessage mailMessage = new MailMessage("乐视云平台web-portal系统",
				openStackUser.getEmail(), "乐视云平台web-portal系统通知",
				"cloudvm/bindFloatingIp.ftl", params);
		mailMessage.setHtml(true);
		OpenStackServiceImpl.getOpenStackServiceGroup().getDefaultEmailSender().sendMessage(mailMessage);
	}

	@Override
	public void unpublish(final String region, final VMResource vm)
			throws RegionNotFoundException, APINotAvailableException,
			TaskNotFinishedException, VMStatusException, OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {
				checkRegion(region);

				if (vm.getTaskState() != null) {
					throw new TaskNotFinishedException();
				}

				Optional<FloatingIPApi> floatingIPApiOptional = novaApi
						.getFloatingIPApi(region);
				if (!floatingIPApiOptional.isPresent()) {
					throw new APINotAvailableException(FloatingIPApi.class);
				}
				FloatingIPApi floatingIPApi = floatingIPApiOptional.get();

				for (FloatingIP floatingIP : floatingIPApi.list().toList()) {
					if (vm.getId().equals(floatingIP.getInstanceId())) {
						floatingIPApi.removeFromServer(floatingIP.getIp(), vm.getId());
						floatingIPApi.delete(floatingIP.getId());
					}
				}
				
				
				return null;
			}
		
		});
		
	}

	public FloatingIP allocFloatingIP(final String region) throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi, FloatingIP>() {

			@Override
			public FloatingIP run(NovaApi novaApi) throws Exception {
				networkManager.getOrCreateUserPrivateRouter(region);

				Optional<FloatingIPApi> floatingIPApiOptional = novaApi
						.getFloatingIPApi(region);
				if (!floatingIPApiOptional.isPresent()) {
					throw new APINotAvailableException(FloatingIPApi.class);
				}
				FloatingIPApi floatingIPApi = floatingIPApiOptional.get();
				List<FloatingIP> floatingIps = floatingIPApi.list().toList();

				Optional<QuotaApi> quotaApiOptional = novaApi.getQuotaApi(region);
				if (!quotaApiOptional.isPresent()) {
					throw new APINotAvailableException(QuotaApi.class);
				}

				Quota quota = quotaApiOptional.get().getByTenant(
						openStackUser.getTenantId());
				if (quota == null) {
					throw new OpenStackException("VM quota is not available.",
							"虚拟机配额不可用。");
				}
				if (floatingIps.size() + 1 > quota.getFloatingIps()) {
					throw new OpenStackException(
							"Floating IP count exceeding the quota.", "公网IP数量超过配额。");
				}

				FloatingIP floatingIP = floatingIPApi.allocateFromPool(openStackConf
						.getGlobalPublicNetworkId());
				return floatingIP;
			}
		});
	}

	private void deleteFloatingIP(final String region, final FloatingIP floatingIP)
			throws APINotAvailableException,OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {
				Optional<FloatingIPApi> floatingIPApiOptional = novaApi
						.getFloatingIPApi(region);
				if (!floatingIPApiOptional.isPresent()) {
					throw new APINotAvailableException(FloatingIPApi.class);
				}
				FloatingIPApi floatingIPApi = floatingIPApiOptional.get();

				floatingIPApi.delete(floatingIP.getId());
				return null;
			}
		});
	}

	public void bindFloatingIP(final String region,final FloatingIP ipPara,final String vmId)
			throws OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {
				Optional<FloatingIPApi> floatingIPApiOptional = novaApi
						.getFloatingIPApi(region);
				if (!floatingIPApiOptional.isPresent()) {
					throw new APINotAvailableException(FloatingIPApi.class);
				}
				FloatingIPApi floatingIPApi = floatingIPApiOptional.get();

				FloatingIP ip = floatingIPApi.get(ipPara.getId());
				if (ip.getInstanceId() != null) {
					throw new OpenStackException(
							"Public IP has been binding, cannot be bound to the virtual machine.",
							"公网IP已经被绑定，不能绑定到多台虚拟机。");
				}

				List<FloatingIP> floatingIps = floatingIPApi.list().toList();

				for (FloatingIP floatingIP : floatingIps) {
					if (vmId.equals(floatingIP.getInstanceId())) {
						throw new OpenStackException(
								"Virtual machine has been binding public IP, cannot repeat binding.",
								"虚拟机已经绑定公网IP，不能重复绑定。");
					}
				}

//				org.jclouds.openstack.neutron.v2.features.PortApi neutronFloatingIPApiOptional = networkManager
//						.getNeutronApi().getPortApi(region);
				
				floatingIPApi.addToServer(ip.getIp(), vmId);
				return null;
			}
		});
	}

	@Override
	public void publish(String region, VMResource vm) throws OpenStackException {
		checkRegion(region);

		// Server server = ((VMResourceImpl) (vm)).server;

		if (vm.getTaskState() != null) {
			throw new TaskNotFinishedException();
		}
		// Status currentServerStatus = server.getStatus();
		// if (currentServerStatus != Server.Status.ACTIVE) {
		// throw new VMStatusException("The status of vm is not active.",
		// "虚拟机的状态不是活跃的，不能绑定公网IP。");
		// }

		// Collection<Address> addresses = server.getAddresses().get(
		// openStackConf.getUserPrivateNetworkName());
		// // for (Entry<String, Address> entry : ((VMResourceImpl) (vm)).server
		// // .getAddresses().entries()) {
		// //
		// System.out.println(MessageFormat.format("server address: {0} => {1}",
		// // entry.getKey(), entry.getValue().getAddr()));
		// // }
		// if (addresses.isEmpty()) {
		// throw new OpenStackException(
		// "Virtual machine is not assigned IP address of user private network.");
		// }
		// Address address = addresses.iterator().next();
		// String ip = address.getAddr();

		FloatingIP floatingIP = allocFloatingIP(region);
		bindFloatingIP(region, floatingIP, vm.getId());
		emailBindIP(vm, floatingIP.getIp());

		// NetworkManagerImpl networkManagerImpl = (NetworkManagerImpl)
		// networkManager;
		// NeutronApi neutronApi = networkManagerImpl.getNeutronApi();
		// neutronApi.getPortApi(region).create(CreatePort.createBuilder(""))
		// neutronApi
		// .getFloatingIPApi(region)
		// .get()
		// .create(CreateFloatingIP.createBuilder(
		// openStackConf.getGlobalPublicNetworkId()).portId("sss").build());
	}

	/**
	 * call before deleting vm
	 * 
	 * @param region
	 * @param vmId
	 * @throws OpenStackException 
	 */
	private void removeAndDeleteFloatingIPOfVM(final String region,final VMResource vm)
			throws OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {
				final String vmId = vm.getId();
				Optional<FloatingIPApi> floatingIPApiOptional = novaApi
						.getFloatingIPApi(region);
				if (!floatingIPApiOptional.isPresent()) {
					throw new APINotAvailableException(FloatingIPApi.class);
				}
				FloatingIPApi floatingIPApi = floatingIPApiOptional.get();
				for (FloatingIP floatingIP : floatingIPApi.list().toList()) {
					if (vmId.equals(floatingIP.getInstanceId())) {
						floatingIPApi.removeFromServer(floatingIP.getIp(), vmId);
						floatingIPApi.delete(floatingIP.getId());
					}
				}
				
				return null;
			}
			
		});
	}

	@Override
	public List<FlavorResource> listFlavorResources(final String region)
			throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi, List<FlavorResource>>() {

			@Override
			public List<FlavorResource> run(NovaApi novaApi) throws Exception {
				checkRegion(region);

				FlavorApi flavorApi = novaApi.getFlavorApi(region);
				List<Flavor> resources = flavorApi.listInDetail().concat().toList();
				List<FlavorResource> flavorResources = new ArrayList<FlavorResource>(
						resources.size());
				for (Flavor resource : resources) {
					flavorResources.add(new FlavorResourceImpl(region, resource));
				}
				return flavorResources;
			}
			
		});
	}

	@Override
	public FlavorResource getFlavorResource(final String region, final String id)
			throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi, FlavorResource>() {

			@Override
			public FlavorResource run(NovaApi novaApi) throws Exception {
				checkRegion(region);

				FlavorApi flavorApi = novaApi.getFlavorApi(region);
				Flavor flavor = flavorApi.get(id);
				if (flavor != null) {
					return new FlavorResourceImpl(region, flavor);
				} else {
					throw new ResourceNotFoundException("Flavor", "规格", id);
				}
			}
			
		});
	}

	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	public void setNetworkManager(NetworkManagerImpl networkManager) {
		this.networkManager = networkManager;
	}

	private void waitingVM(String vmId, ServerApi serverApi,
			ServerChecker checker) throws OpenStackException {
		try {
			Server server = null;
			while (true) {
				server = serverApi.get(vmId);
				if (checker.check(server)) {
					break;
				}
				Thread.sleep(1000);
			}
		} catch (OpenStackException e) {
			throw e;
		} catch (InterruptedException e) {
			throw new PollingInterruptedException(e);
		} catch (Exception e) {
			throw new OpenStackException("后台错误", e);
		}
	}

	private void waitingVMs(final List<RegionAndVmId> vmIds,final ServerChecker checker)
			throws OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {
				try {
					List<RegionAndVmId> unFinishedVMIds = new LinkedList<RegionAndVmId>();
					unFinishedVMIds.addAll(vmIds);
					while (!unFinishedVMIds.isEmpty()) {
						for (RegionAndVmId vmId : unFinishedVMIds
								.toArray(new RegionAndVmId[0])) {
							Server server = novaApi.getServerApi(vmId.getRegion()).get(
									vmId.getVmId());
							if (checker.check(server)) {
								unFinishedVMIds.remove(vmId);
							}
						}
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					throw new PollingInterruptedException(e);
				}
				return null;
			}
		});
	}

	@Override
	public void delete(final String region, final VMResource vm)
			throws OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {

				checkRegion(region);

				if (vm.getTaskState() != null) {
					throw new TaskNotFinishedException();
				}

				removeAndDeleteFloatingIPOfVM(region, vm);
				ServerApi serverApi = novaApi.getServerApi(region);
				boolean isSuccess = serverApi.delete(vm.getId());
				if (!isSuccess) {
					throw new VMDeleteException(vm.getId());
				}
				
				return null;
			}
		});
		
	}

	private boolean isDeleteFinished(Server server) throws OpenStackException {
		if (server == null) {
			decVmCount();
			return true;
		}
		return server.getStatus() == Status.ERROR;
	}

	@Override
	public void deleteSync(final String region,final  VMResource vm)
			throws OpenStackException {
		delete(region, vm);

		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {

				ServerApi serverApi = novaApi.getServerApi(region);
				waitingVM(vm.getId(), serverApi, new ServerChecker() {

					@Override
					public boolean check(Server server) throws Exception {
						return isDeleteFinished(server);
					}
				});
				return null;
			}
		});
	}

	@Override
	public void start(final String region,final VMResource vm)
			throws OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {

				checkRegion(region);

				if (vm.getTaskState() != null) {
					throw new TaskNotFinishedException();
				}
				if (((VMResourceImpl) vm).server.getStatus() != Server.Status.SHUTOFF) {
					throw new VMStatusException(
							"The status of vm is not shut off.",
							"虚拟机的状态不是关闭的，不能启动。");
				}

				ServerApi serverApi = novaApi.getServerApi(region);
				serverApi.start(vm.getId());
				return null;
			}
		});
	}

	private boolean isStartFinished(Server server) {
		return server == null || Server.Status.ACTIVE == server.getStatus()
				|| server.getStatus() == Status.ERROR;
	}

	@Override
	public void startSync(final String region,final VMResource vm)
			throws OpenStackException {
		start(region, vm);

		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {

				ServerApi serverApi = novaApi.getServerApi(region);
				waitingVM(vm.getId(), serverApi, new ServerChecker() {

					@Override
					public boolean check(Server server) {
						return isStartFinished(server);
					}
				});
				return null;
			}
		});
	}

	@Override
	public void stop(final String region,final VMResource vm)
			throws OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {

				checkRegion(region);

				if (vm.getTaskState() != null) {
					throw new TaskNotFinishedException();
				}
				Status currentServerStatus = ((VMResourceImpl) vm).server
						.getStatus();
				if (currentServerStatus != Server.Status.ACTIVE
						&& currentServerStatus != Server.Status.ERROR) {
					throw new VMStatusException(
							"The status of vm is not stoppable.",
							"虚拟机的状态不是可关闭的，不能关闭。");
				}

				ServerApi serverApi = novaApi.getServerApi(region);
				serverApi.stop(vm.getId());

				return null;
			}
		});
	}

	private boolean isStopFinished(Server server) {
		return server == null || Server.Status.SHUTOFF == server.getStatus()
				|| server.getStatus() == Status.ERROR;
	}

	@Override
	public void stopSync(final String region,final VMResource vm)
			throws OpenStackException {
		stop(region, vm);
		
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {
				ServerApi serverApi = novaApi.getServerApi(region);
				waitingVM(vm.getId(), serverApi, new ServerChecker() {

					@Override
					public boolean check(Server server) {
						return isStopFinished(server);
					}
				});
				
				return null;
			}
		});
	}

	@Override
	public int totalNumber() throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi, Integer>() {

			@Override
			public Integer run(NovaApi novaApi) throws Exception {
				int total = 0;
				Set<String> regions = getRegions();
				for (String region : regions) {
					ServerApi serverApi = novaApi.getServerApi(region);
					total += serverApi.list().concat().toList().size();
				}
				return total;
			}
		});
	}

	public List<FloatingIP> listFloatingIPs(final String region)
			throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi, List<FloatingIP>>() {

			@Override
			public List<FloatingIP> run(NovaApi novaApi) throws Exception {
				checkRegion(region);

				Optional<FloatingIPApi> floatingIPApiOptional = novaApi
						.getFloatingIPApi(region);
				if (!floatingIPApiOptional.isPresent()) {
					throw new APINotAvailableException(FloatingIPApi.class);
				}
				FloatingIPApi floatingIPApi = floatingIPApiOptional.get();
				return floatingIPApi.list().toList();
			}
		});
	}

	@Override
	public Map<Integer, Map<Integer, Map<Integer, FlavorResource>>> groupFlavorResources(
			final String region) throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi, Map<Integer, Map<Integer, Map<Integer, FlavorResource>>>>() {

			@Override
			public Map<Integer, Map<Integer, Map<Integer, FlavorResource>>> run(
					NovaApi novaApi) throws Exception {

				checkRegion(region);

				FlavorApi flavorApi = novaApi.getFlavorApi(region);
				List<Flavor> resources = flavorApi.listInDetail().concat()
						.toList();

				Map<Integer, Map<Integer, Map<Integer, FlavorResource>>> flavorResources = new HashMap<Integer, Map<Integer, Map<Integer, FlavorResource>>>();
				for (Flavor resource : resources) {
					FlavorResource flavorResource = new FlavorResourceImpl(
							region, resource);

					Map<Integer, Map<Integer, FlavorResource>> vcpusFlavorResources = flavorResources
							.get(flavorResource.getVcpus());
					if (vcpusFlavorResources == null) {
						vcpusFlavorResources = new HashMap<Integer, Map<Integer, FlavorResource>>();
						flavorResources.put(flavorResource.getVcpus(),
								vcpusFlavorResources);
					}

					Map<Integer, FlavorResource> vcpusRamFlavorResources = vcpusFlavorResources
							.get(flavorResource.getRam());
					if (vcpusRamFlavorResources == null) {
						vcpusRamFlavorResources = new HashMap<Integer, FlavorResource>();
						vcpusFlavorResources.put(flavorResource.getRam(),
								vcpusRamFlavorResources);
					}

					if (vcpusRamFlavorResources.get(flavorResource.getDisk()) != null) {
						throw new OpenStackException(
								"There are repeated flavors.", "存在重复的规格");
					} else {
						vcpusRamFlavorResources.put(flavorResource.getDisk(),
								flavorResource);
					}
				}

				return flavorResources;
			}
		});
	}

	public void setVolumeManager(VolumeManagerImpl volumeManager) {
		this.volumeManager = volumeManager;
	}

	@Override
	public void batchDeleteSync(String vmIdListJson) throws OpenStackException {
		List<RegionAndVmId> regionAndVmIds = RegionAndVmId
				.listFromJson(vmIdListJson);

		Set<String> regions = getRegions();

		for (RegionAndVmId regionAndVmId : regionAndVmIds) {
			String region = regionAndVmId.getRegion();
			if (!regions.contains(region)) {
				throw new RegionNotFoundException(region);
			}
			delete(region, get(region, regionAndVmId.getVmId()));
		}

		waitingVMs(regionAndVmIds, new ServerChecker() {
			@Override
			public boolean check(Server server) throws Exception {
				return isDeleteFinished(server);
			}
		});
	}

	@Override
	public void batchStartSync(String vmIdListJson) throws OpenStackException {
		List<RegionAndVmId> regionAndVmIds = RegionAndVmId
				.listFromJson(vmIdListJson);

		Set<String> regions = getRegions();

		for (RegionAndVmId regionAndVmId : regionAndVmIds) {
			String region = regionAndVmId.getRegion();
			if (!regions.contains(region)) {
				throw new RegionNotFoundException(region);
			}
			start(region, get(region, regionAndVmId.getVmId()));
		}

		waitingVMs(regionAndVmIds, new ServerChecker() {
			@Override
			public boolean check(Server server) {
				return isStartFinished(server);
			}
		});
	}

	@Override
	public void batchStopSync(String vmIdListJson) throws OpenStackException {
		List<RegionAndVmId> regionAndVmIds = RegionAndVmId
				.listFromJson(vmIdListJson);

		Set<String> regions = getRegions();

		for (RegionAndVmId regionAndVmId : regionAndVmIds) {
			String region = regionAndVmId.getRegion();
			if (!regions.contains(region)) {
				throw new RegionNotFoundException(region);
			}
			stop(region, get(region, regionAndVmId.getVmId()));
		}

		waitingVMs(regionAndVmIds, new ServerChecker() {
			@Override
			public boolean check(Server server) {
				return isStopFinished(server);
			}
		});
	}

	@Override
	public void attachVolume(final VMResource vmResource,
		 final	VolumeResource volumeResource) throws OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {


				if (vmResource.getTaskState() != null) {
					throw new TaskNotFinishedException();
				}

				if (!vmResource.getRegion().equals(volumeResource.getRegion())) {
					throw new OpenStackException(
							"Under the different regions of the vm and volume can not attach.",
							"不同地域下的虚拟机和云硬盘不能附加");
				}

				Server.Status vmStatus = ((VMResourceImpl) vmResource).server
						.getStatus();
				if (vmStatus != Server.Status.ACTIVE) {
					throw new OpenStackException(
							"The current status of the virtual machine can not attach volume.",
							"虚拟机当前的状态不能附加云硬盘。");
				}

				Volume.Status volumeStatus = ((VolumeResourceImpl) volumeResource).volume
						.getStatus();
				if (volumeStatus != Volume.Status.AVAILABLE) {
					throw new OpenStackException(
							"The status of the volume is not available.",
							"云硬盘的状态不是可用的。");
				}

				Optional<VolumeAttachmentApi> volumeAttachmentApiOptional = novaApi
						.getVolumeAttachmentApi(vmResource.getRegion());
				if (!volumeAttachmentApiOptional.isPresent()) {
					throw new APINotAvailableException(VolumeAttachmentApi.class);
				}
				VolumeAttachmentApi volumeAttachmentApi = volumeAttachmentApiOptional
						.get();

				volumeAttachmentApi.attachVolumeToServerAsDevice(
						volumeResource.getId(), vmResource.getId(), "");

				volumeManager.waitingVolume(volumeResource.getRegion(),  volumeResource.getId(),
						new VolumeChecker() {

							@Override
							public boolean check(Volume volume) {
								return volume == null
										|| volume.getStatus() == Volume.Status.IN_USE
										|| volume.getStatus() == Volume.Status.ERROR
										|| volume.getStatus() == Volume.Status.ERROR_DELETING;
							}
						});
				
				return null;
			}
		});
		
	}

	@Override
	public void detachVolume(final VMResource vmResource,
			final VolumeResource volumeResource) throws OpenStackException {
		runWithApi(new ApiRunnable<NovaApi, Void>() {

			@Override
			public Void run(NovaApi novaApi) throws Exception {
		
		if (vmResource.getTaskState() != null) {
			throw new TaskNotFinishedException();
		}

		if (!vmResource.getRegion().equals(volumeResource.getRegion())) {
			throw new OpenStackException(
					"Under the different regions of the vm and volume can not detach.",
					"不同地域下的虚拟机和云硬盘不能分离");
		}

		Server.Status vmStatus = ((VMResourceImpl) vmResource).server
				.getStatus();
		if (vmStatus != Server.Status.ACTIVE) {
			throw new OpenStackException(
					"The current status of the virtual machine can not attach volume.",
					"虚拟机当前的状态不能分离云硬盘。");
		}

		Volume.Status volumeStatus = ((VolumeResourceImpl) volumeResource).volume
				.getStatus();
		if (volumeStatus != Volume.Status.IN_USE) {
			throw new OpenStackException(
					"The status of the volume is not in use.", "云硬盘没有被使用。");
		}

		boolean isAttachedToServer = false;
		for (VolumeAttachment volumeAttachment : ((VolumeResourceImpl) volumeResource).volume
				.getAttachments()) {
			if (volumeAttachment.getServerId().equals(vmResource.getId())) {
				isAttachedToServer = true;
				if ("/".equals(volumeAttachment.getDevice())) {
					throw new OpenStackException(
							"Attached on the the volume can't be detached in the root directory.",
							"挂载在根路径上的云硬盘不能被分离。");
				}
			}
		}
		if (!isAttachedToServer) {
			throw new OpenStackException(
					"The volume is not attached to this vm.", "云硬盘没有被这台虚拟机使用。");
		}

		Optional<VolumeAttachmentApi> volumeAttachmentApiOptional = novaApi
				.getVolumeAttachmentApi(vmResource.getRegion());
		if (!volumeAttachmentApiOptional.isPresent()) {
			throw new APINotAvailableException(VolumeAttachmentApi.class);
		}
		VolumeAttachmentApi volumeAttachmentApi = volumeAttachmentApiOptional
				.get();

		boolean success = volumeAttachmentApi.detachVolumeFromServer(
				volumeResource.getId(), vmResource.getId());
		if (!success) {
			throw new OpenStackException(MessageFormat.format(
					"Volume \"{0}\" detach failed.", volumeResource.getId()),
					MessageFormat.format("云硬盘“{0}”分离失败。",
							volumeResource.getId()));
		}

		volumeManager.waitingVolume(volumeResource.getRegion(),volumeResource.getId(), 
				new VolumeChecker() {

					@Override
					public boolean check(Volume volume) {
						return volume == null
								|| volume.getStatus() == Volume.Status.AVAILABLE
								|| volume.getStatus() == Volume.Status.ERROR
								|| volume.getStatus() == Volume.Status.ERROR_DELETING;
					}
				});
		return null;
			}
		});
	}

	@Override
	public String openConsole(final VMResource vmResource)
			throws OpenStackException {
		return runWithApi(new ApiRunnable<NovaApi, String>() {

			@Override
			public String run(NovaApi novaApi) throws Exception {
				Optional<ConsolesApi> consoleApiOptional = novaApi
						.getConsolesApi(vmResource.getRegion());
				if (!consoleApiOptional.isPresent()) {
					throw new APINotAvailableException(ConsolesApi.class);
				}
				URI uri = consoleApiOptional.get()
						.getConsole(vmResource.getId(), Console.Type.NOVNC).getUrl();
				return uri.getScheme() + "://" + "localhost:8081" + uri.getPath() + "?"
						+ uri.getQuery();// TODO change localhost:8081
			}
			
		});
	}

	@Override
	protected String getProviderOrApi() {
		return "openstack-nova";
	}

	@Override
	protected Class<NovaApi> getApiClass() {
		return NovaApi.class;
	}

	// public void setIdentityManager(IdentityManagerImpl identityManager) {
	// this.identityManager = identityManager;
	// }

	public void incVmCount() throws OpenStackException {
		ICloudvmVmCountService cloudvmVmCountService = OpenStackServiceImpl
				.getOpenStackServiceGroup().getCloudvmVmCountService();
		CloudvmVmCount cloudvmVmCount = cloudvmVmCountService
				.getVmCountOfCurrentUser();
		if (cloudvmVmCount == null) {
			cloudvmVmCountService.createVmCountOfCurrentUser(1);
		} else {
			cloudvmVmCountService.updateVmCountOfCurrentUser(cloudvmVmCount
					.getVmCount() + 1);
		}
	}

	private void decVmCount() throws OpenStackException {
		ICloudvmVmCountService cloudvmVmCountService = OpenStackServiceImpl
				.getOpenStackServiceGroup().getCloudvmVmCountService();
		CloudvmVmCount cloudvmVmCount = cloudvmVmCountService
				.getVmCountOfCurrentUser();
		if (cloudvmVmCount == null) {
			throw new OpenStackException(
					"Vm count of user is not synchronized.", "用户数据错误");
		} else {
			cloudvmVmCountService.updateVmCountOfCurrentUser(cloudvmVmCount
					.getVmCount() - 1);
		}
	}
}
