<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row">
	<div class="col-md-12">
		<h3 class="text-left">DB详情</h3>
	</div>
	<hr
		style="FILTER: alpha(opacity = 0, finishopacity = 100, style = 1)"
		width="100%" color=#987cb9 SIZE=3>
	</hr>
</div>

<div class="row clearfix">
	<div class="col-md-3 column">
		<h2>注意：</h2>
		<p>如果修改数据库后，请联系我们更新相关数据。</p>
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
		location.href = "${ctx}/db/mgrList";
	});
}
</script>
