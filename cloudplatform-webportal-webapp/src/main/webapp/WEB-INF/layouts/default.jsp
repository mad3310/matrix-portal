<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" />


<!DOCTYPE html>
<html> 
<head>
<title>QuickStart示例:<sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

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

<!-- 警告框 -->
<script src="${ctx}/static/scripts/pageMessage.js"></script>

<sitemesh:head/>
</head>

<body>
	<div class="container">
		<div id="wrap" style="min-height:800px;">
			<%@ include file="/WEB-INF/layouts/header.jsp"%>
			<div id="content">
				<sitemesh:body/>
			</div>
		</div>
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
</body>
</html>
