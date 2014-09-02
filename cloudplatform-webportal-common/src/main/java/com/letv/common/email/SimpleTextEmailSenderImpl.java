/*
 *  Copyright (c) 2011 乐视网（letv.com）. All rights reserved
 * 
 *  LETV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package com.letv.common.email;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Construct a text email and sent to the target email address.
 * 
 * The implementation need the inject the following properties:
 * <pre>
 * mail.host=mail.letv.com
 * mail.port=25
 * mail.from=pizhigang@letv.com
 * mail.password=abc
 * mail.auth=true
 * </pre>
 * 
 * @author <a href="mailto:pizhigang@letv.com">pizhigang</a>
 */
public class SimpleTextEmailSenderImpl implements SimpleTextEmailSender {

    private static final Logger log = LoggerFactory.getLogger(SimpleTextEmailSenderImpl.class);
    
    private JavaMailSender mailSender;
    private String mailFrom;

    @Required
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Required
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    /**
     * Send an email to the given user.
     * @TODO: please make this asynchronouse.
     * 
     * @param subject
     * @param username
     * @param content
     * @param email 
     */
    @Override
    public void send(String subject, String username, String content, String email) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username cannot be null");
        }
        if (StringUtils.isEmpty(email)) {
            throw new IllegalArgumentException("email cannot be null");
        }
        
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setFrom(this.mailFrom);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        try {
            log.info("sending email to {} ...", email);
            this.mailSender.send(mailMessage);
            log.info("successful sent email to {}" , email);
        } catch (MailException ex) {
            log.warn("An exception occured when trying to send email", ex);
            // ignore it and let it go
        }
    }
}
