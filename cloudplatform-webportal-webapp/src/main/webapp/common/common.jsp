<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //防止代理服务器缓
%>

<link href="${pageContext.request.contextPath}/static/styles/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/static/styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">

<script src="${pageContext.request.contextPath}/scripts/bootstrap/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery/jquery.min.js"></script>
