package com.letv.common.email.impl;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

public class SendMailThread implements Runnable{
    private final static Logger logger = LoggerFactory.getLogger(SendMailThread.class);
    
	private MimeMessage[] mimes;
	
	private JavaMailSender sender;
	
	public SendMailThread(JavaMailSender sender,MimeMessage[] mimes)
	{
		this.sender = sender;
		this.mimes = mimes;
	}
	
	@Override
	public void run() {
	    try {
	        sender.send(mimes);
	    } catch (Exception e) {
	        retry(0);
	    }
	}
	
	private void retry(int  i) {
	    if (i < 3) {
	        try {
	            sender.send(mimes);
	        } catch (Exception e) {
	            retry(i+1);
	        }
	    } else {
	        for (MimeMessage mm : mimes) {
                StringBuilder sb = new StringBuilder();
                try {
					sb.append("from: " + mm.getFrom().toString() + ", ");
					sb.append("to: " + mm.getSender().toString() + ", ");
	                sb.append("reply: " + mm.getReplyTo().toString() + ",");
					sb.append("" + mm.getContent().toString() + "");
				} catch (MessagingException e) {
	                logger.error("邮件信息取值异常" + e.getMessage());
				}catch (IOException e) {
	                logger.error("邮件信息取值异常" + e.getMessage());
				}
                
                logger.error("邮件发送异常" + sb.toString());
	        }
	    }
	}
}
