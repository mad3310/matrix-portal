<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/common/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>数据库详情</title>

</head>
<body>
<div class="row">
	<div class="col-md-12">
		<h3 class="text-left">DB详情</h3>
	</div>
	<hr
		style="FILTER: alpha(opacity = 0, finishopacity = 100, style = 1)"
		width="100%" color=#987cb9 SIZE=3></hr>
</div>

<div class="row clearfix">
	<div class="col-md-3 column">
		<h2>提示：</h2>
		<p>请按照数据库信息表中提供的信息配置使用数据库，如有问题请联系运维管理员。</p>
<!-- 		<p>
			<a class="btn" href="#">查看详细使用教程 »</a>
		</p> -->
	</div>
	<div class="col-md-9 column">
		<div class="col-sm-10">
		<button id="goBack" name="goBack" type="button" class="btn btn-default">返回</button>
		<table class="table table-bordered" id="db_detail_table" name="db_detail_table">
		<caption>数据库信息</caption>
			<tr>
				<td width="30%">数据库名</td>
				<td width="70%">${db.dbName}</td>
			</tr>
			<c:forEach items="${dbUsers}" var="dbUser">
				<tr>
					<td>用户名</td>
					<td>${dbUser.username}</td>
				</tr>
				<tr>
					<td>密码</td>
					<td>${dbUser.password}</td>
				</tr>
			</c:forEach>
			<c:forEach items="${containers}" var="container">
			<tr>
				<td>${container.type}</td>
				<td>${container.ipAddr}</td>
			</tr>
			</c:forEach>
		</table>
		<table class="table table-bordered" id="db_apply_table" name="db_apply_table">
		<caption>申请信息</caption>
		<tr>
			<td width="30%">项目名称</td>
			<td width="70%">${dbApplyStandard.applyName}</td>
		</tr>
		<tr>
			<td>业务描述</td>
			<td>${dbApplyStandard.descn}</td>
		</tr>
		<tr>
			<td>链接类型</td>
			<td>${dbApplyStandard.linkType}</td>
		</tr>
		<tr>
			<td>最大访问量</td>
			<td>${dbApplyStandard.maxConcurrency}</td>
		</tr>
		<tr>
			<td>读写比例</td>
			<td>${dbApplyStandard.readWriterRate}</td>
		</tr>
		<tr>
			<td>开发语言</td>
			<td>${dbApplyStandard.developLanguage}</td>
		</tr>
		<tr>
			<td>ip访问列表</td>
			<td>${dbApplyStandard.dataLimitIpList}</td>
		</tr>
		<tr>
			<td>管理员ip访问列表</td>
			<td>${dbApplyStandard.mgrLimitIpList}</td>
		</tr>
		<tr>
			<td>数据库引擎</td>
			<td>${dbApplyStandard.engineType}</td>
		</tr>
		<tr>
			<td>邮件通知</td>
			<td>
				<c:if test="${dbApplyStandard.isEmailNotice == '1'}">开启</c:if>
				<c:if test="${dbApplyStandard.isEmailNotice != '1'}">关闭</c:if>
			</td>
		</tr>
		<tr>
			<td>申请时间</td>
			<td>${dbApplyStandard.createTime}</td>
		</tr>
		</table>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	initPage();
});

function translateStatus(status){
	if(status = 1)
	{
		return "是";
	}else{
		return "否";
	}
}

function initPage(){
	$("#goBack").click(function() {
		location.href = "${ctx}/db/list";
	});
}
</script>
</body>

</html>
