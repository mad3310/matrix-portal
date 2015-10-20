package com.letv.portal.service.app.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.dao.app.IMessageDao;
import com.letv.portal.model.app.MessageModel;
import com.letv.portal.service.app.IMessageService;
import com.letv.portal.service.impl.BaseServiceImpl;

@Service("messageService")
public class MessageServiceImpl extends BaseServiceImpl<MessageModel> implements IMessageService{
	
	private final static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
	
	@Resource
	private IMessageDao messageDao;
	
	public MessageServiceImpl() {
		super(MessageModel.class);
	}

	@Override
	public IBaseDao<MessageModel> getDao() {
		return this.messageDao;
	}
}
