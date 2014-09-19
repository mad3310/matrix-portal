package com.letv.portal.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SessionFilter implements Filter {
	private final static Logger logger = LoggerFactory.getLogger(SessionFilter.class);


    public void init(FilterConfig filterConfig) throws ServletException {
//        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
//        roleService = (RoleService) wac.getBean("roleService");

    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        AttributePrincipal principal = (AttributePrincipal) ((HttpServletRequest) request).getUserPrincipal();
        if (session.getAttribute("username") == null) {
            String username = principal.getName();
            
            username = username + "@letv.com";
            session.setAttribute("username", username);
            session.setAttribute("userId", username);
            session.setAttribute("role", "user");
        }
        if(principal != null) {
        	logger.debug("AssertionHolder.getAssertion().getPrincipal().getName()==>" +AssertionHolder.getAssertion().getPrincipal().getName());
        	logger.debug("principal===>" + principal.getName());
        	logger.debug("req.getRemoteUser===>" + req.getRemoteUser());
        }
        chain.doFilter(req, res);
    } 
    public void destroy() { 
    }
} 
