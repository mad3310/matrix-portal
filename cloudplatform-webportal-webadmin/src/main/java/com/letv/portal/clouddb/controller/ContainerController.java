package com.letv.portal.clouddb.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IDbService;

@Controller
@RequestMapping("/container")
public class ContainerController {
	
	@Resource
	private IContainerService containerService;
	@Resource
	private IDbService dbService;
	
	private final static Logger logger = LoggerFactory.getLogger(ContainerController.class);
}
