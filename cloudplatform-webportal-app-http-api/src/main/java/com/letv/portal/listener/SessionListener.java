package com.letv.portal.listener;

import com.letv.common.session.Session;
import com.letv.portal.service.openstack.OpenStackSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;

/**
 * Created by zhouxianguang on 2015/6/12.
 */
public class SessionListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        Session session = (Session) sessionEvent.getSession().getAttribute(Session.USER_SESSION_REQUEST_ATTRIBUTE);
        if (session != null) {
            OpenStackSession openStackSession = (OpenStackSession) session.getOpenStackSession();
            if (openStackSession != null && !openStackSession.isClosed()) {
                try {
                    openStackSession.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
