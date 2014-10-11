<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html> 
<head>
<title>Letv CloudPlateform WebPortal<sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="description" content="overview &amp; stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

<!-- bootstrap & fontawesome -->
<link rel="stylesheet" href="${ctx}/static/ace/css/bootstrap.min.css" />
<link rel="stylesheet" href="${ctx}/static/ace/css/font-awesome.min.css" />

<!-- page specific plugin styles -->

<!-- text fonts -->
<link rel="stylesheet" href="${ctx}/static/ace/css/ace-fonts.css" />

<!-- ace styles -->
<link rel="stylesheet" href="${ctx}/static/ace/css/ace.min.css" id="main-ace-style" />

<!--[if lte IE 9]>
	<link rel="stylesheet" href="${ctx}/static/ace/css/ace-part2.min.css" />
<![endif]-->
<link rel="stylesheet" href="${ctx}/static/ace/css/ace-skins.min.css" />
<link rel="stylesheet" href="${ctx}/static/ace/css/ace-rtl.min.css" />

<!--[if lte IE 9]>
	<link rel="stylesheet" href="${ctx}/static/ace/css/ace-ie.min.css" />
<![endif]-->

<!-- inline styles related to this page -->

<!-- ace settings handler -->
<script src="${ctx}/static/ace/js/jquery.min.js"></script>
<script src="${ctx}/static/ace/js/ace-extra.min.js"></script>
<script src="${ctx}/static/ace/js/bootstrap.min.js"></script>

<!-- warning box -->
<link rel="stylesheet" href="${ctx}/static/ace/css/jquery.gritter.css" />
<script src="${ctx}/static/ace/js/jquery.gritter.min.js"></script>
<script type="text/javascript">
function error(errorThrown,time) {
	if(time == "null"){
		time == 1000;
	}
	
	var errorText = null;
	if(errorThrown.status == 500){
		errorText = "服务器出现异常,请稍后重试.";
	}else{
		errorText = "内部错误,请稍后重试.";
	}
	
	$.gritter.add({
		title: '错误',
		text: errorText,
		sticky: false,
		time: time,
		class_name: 'gritter-error'
	});
}
</script>

<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

<!--[if lte IE 8]>
	<script src="${ctx}/static/ace/js/html5shiv.min.js"></script>
	<script src="${ctx}/static/ace/js/respond.min.js"></script>
<![endif]-->

</head>

<body class="no-skin">
	<%@ include file="/WEB-INF/layouts/header.jsp"%>
	<div class="main-container" id="main-container">
		<script type="text/javascript">
			try {
				ace.settings.check('main-container', 'fixed')
			} catch (e) {
			}
		</script>
	<%@ include file="/WEB-INF/layouts/sidebar.jsp"%>
	
	<!--#内容显示 -->
	<div class="main-content">
	
		<div class="page-content">
<div class="page-content-area">
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->

		<!-- #section:pages/error -->
		<div class="error-container">
			<div class="well">
				<h1 class="grey lighter smaller">
					<span class="blue bigger-125">
						<i class="ace-icon fa fa-sitemap"></i>
						404
					</span>
				</h1>

				<hr>
				<h3 class="lighter smaller">无法载入页面!</h3>

				<div>
					<div class="space"></div>
					<h4 class="smaller">尝试如下动作:</h4>

					<ul class="list-unstyled spaced inline bigger-110 margin-15">
						<li>
							<i class="ace-icon fa fa-hand-o-right blue"></i>
							重新打开页面
						</li>
						<li>
							<i class="ace-icon fa fa-hand-o-right blue"></i>
							联系我们
						</li>
					</ul>
				</div>

				<hr>
				<div class="space"></div>
			</div>
		</div>

		<!-- /section:pages/error -->

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->
</div>
		</div>
	</div>
	<!-- /.内容显示  -->
	<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
	

	
<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
	window.jQuery || document.write("<script src='${ctx}/static/ace/js/jquery.min.js'>" + "<"+"/script>");
</script>
<!-- <![endif]-->

<!--[if IE]>
	<script type="text/javascript">  window.jQuery || document.write("<script src='${ctx}/static/ace/js/jquery1x.min.js'>"+"<"+"/script>");
	</script>
<![endif]-->
<script type="text/javascript">
	if ('ontouchstart' in document.documentElement)
		document.write("<script src='${ctx}/static/ace/js/jquery.mobile.custom.min.js'>" + "<"+"/script>");
</script>
<!-- 设置sidebar的高亮显示 -->
<script type="text/javascript">
		var path = window.location.pathname;
		if(path.indexOf("/db/list") >= 0 ||path.indexOf("/db/detail") >= 0){
			$('#sidebar-list ul li:first').addClass("active");
			$('#main-content-header li:first a').attr("href", "${ctx}/db/list").html("数据库管理");
			$('#main-content-header li:eq(1)').html("数据库列表");
		}
	</script>
<!-- ace scripts -->
<script src="${ctx}/static/ace/js/ace-elements.min.js"></script>
<script src="${ctx}/static/ace/js/ace.min.js"></script>
</body>
</html>
