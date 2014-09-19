<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //防止代理服务器缓
%>

<link href="${ctx}/static/styles/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<%-- <link href="${ctx}/static/styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet"> --%>
<link href="${ctx}/static/styles/style.css" rel="stylesheet"></link>

<script src="${ctx}/static/scripts/jquery/jquery.min.js"></script>
<script src="${ctx}/static/scripts/bootstrap/bootstrap.min.js"></script>

<!-- bootstrap validator -->
<script type="text/javascript" src="${ctx}/static/scripts/bootstrap/bootstrapValidator.js"></script>
<link href="${ctx}/static/styles/bootstrap/css/bootstrapValidator.min.css" rel="stylesheet"/>

<!-- bootstrap switch -->
<link href="${ctx}/static/styles/bootstrap/css/bootstrap-switch.min.css" rel="stylesheet">
<script src="${ctx}/static/scripts/bootstrap/bootstrap-switch.min.js"></script>

<!-- bootstrap-duallistbox 左右选框 -->
<link href="${ctx}/static/styles/bootstrap/css/bootstrap-duallistbox.min.css" rel="stylesheet">
<script src="${ctx}/static/scripts/bootstrap/bootstrap-duallistbox.js"></script>
