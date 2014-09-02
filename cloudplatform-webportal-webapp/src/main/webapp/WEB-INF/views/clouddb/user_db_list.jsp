<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>用户数据库管理</title>
</head>
<body>
<div class="row">
	<div class="col-md-3">
		<h3 class="text-left">DB管理</h3>
	</div>
	<div  class="col-md-6">
		<div id="pageMessage"></div>
	</div>
	<div class="col-md-3">
		<form class="navbar-form navbar-right" role="search">
			<div class="form-group">
				<input id="clusterId" type="hidden" value="0e7e5fba-274f-11e4-a3d9-b82a72b53876" />
				<input id="dbName" type="text" value=""
					class="form-control" />
			</div>
			<button type="button" class="btn btn-default" id="searchButton">搜索</button>
		</form>
	</div>
	<hr
		style="FILTER: alpha(opacity = 0, finishopacity = 100, style = 1)"
		width="100%" color=#987cb9 SIZE=3></hr>
</div>

<div class="row clearfix">
	<div class="col-md-3 column">
		<h2>提示：</h2>
		<p>如果发现数据库性能不够告警，请联系运维管理员提升数据库性能。</p>
<!-- 		<p>
			<a class="btn" href="#">查看详细使用教程 »</a>
		</p> -->
	</div>
	<div class="col-md-9 column">
		<button id="db_apply" type="button" class="btn btn-success"
			data-toggle="modal">申请DB</button>
		
		<table id="db_list"
			class="table table-striped table-hover table-responsive">
			<thead>
				<tr>
					<th>DB名称</th>
					<th>创建时间</th>
					<th>当前状态</th>
				</tr>
			</thead>
			<tbody id="tby">							
			</tbody>
		</table>
	</div>

</div>
<script type="text/javascript">
var currentPage = 1; //第几页 
var recordsPerPage = 10; //每页显示条数
var currentSelectedLineDbName = 1;
	
	 $(function(){
		//初始化列表 
		queryByPage(currentPage, recordsPerPage);
		pageControl();
		$("#pageMessage").hide();
	});	
	function queryByPage(currentPage,recordsPerPage) {
		$("#tby tr").remove();
		$.ajax({ 
			type : "post",
			url : "${ctx}/db/list/data?currentPage="
					+ currentPage
					+ "&recordsPerPage="
					+ recordsPerPage
					+ "&flag=self"
					+ "&dbName="
					+ $("#dbName").val(),
			dataType : "json", /*这句可用可不用，没有影响*/
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				var array = data.data.data;
				var tby = $("#tby");
				var totalPages = data.data.totalPages;
				
				function translateStatus(status){
					if(status == 0){
						return "审核中";
					}else if(status  == 1 ||status  == 2){
						return "审核通过";
					}else if(status  == -1){
						return "审核未通过";
					}
				}
				
				for (var i = 0, len = array.length; i < len; i++) {
					var td1 = $("<td>"
							+ "<a href=\"${ctx}/db/dbApplyInfo?dbId="+array[i].id+"\">"+array[i].dbName+"</a>"
							+ "</td>");
					/* var td2 = $("<td>"
							+ array[i].cluster.mclusterName
							+ "</td>"); */
					var td2 = $("<td>"
							+ array[i].createTime
							+ "</td>");
					var td3 = $("<td>"
							+ translateStatus(array[i].status)
							+ "</td>");
					if(array[i].status == -1){
						var tr = $("<tr class=\"danger\"></tr>");
					}else{
						var tr = $("<tr></tr>");
					}
					
					tr.append(td1).append(td2).append(td3);
					tr.appendTo(tby);
				}
				if (totalPages <= 1) {
					$("#pageControlBar").hide();
				} else {
					$("#pageControlBar").show();
					$("#totalPage_input").val(totalPages);
					$("#currentPage").html(currentPage);
					$("#totalRows").html(data.data.totalRecords);
					$("#totalPage").html(totalPages);
					//循环json中的数据 
				}
			},
			error : function(XMLHttpRequest,textStatus, errorThrown) {
				$('#pageMessage').html("<p class=\"bg-warning\" style=\"color:red;font-size:16px;\"><strong>警告!</strong>"+errorThrown+"</p>").show().fadeOut(3000);
			}
		});
    }
	
	function pageControl() {
		// 首页
		$("#firstPage").bind("click", function() {
			currentPage = 1;
			queryByPage(currentPage,recordsPerPage);
		});

		// 上一页
		$("#prevPage").click(function() {
			if (currentPage == 1) {
				$('#pageMessage').html(pageMessage("warning","已经到达首页")).show().fadeOut(3000);
				
			} else {
				currentPage--;
				queryByPage(currentPage,recordsPerPage);
			}
		});

		// 下一页
		$("#nextPage").click(function() {
			if (currentPage == $("#totalPage_input").val()) {
				$('#pageMessage').html(pageMessage("warning","已经到达末页")).show().fadeOut(3000);
			} else {
				currentPage++;
				queryByPage(currentPage,recordsPerPage);
			}
		});

		// 末页
		$("#lastPage").bind("click", function() {
			currentPage = $("#totalPage_input").val();
			queryByPage(currentPage,recordsPerPage);
		});
		//搜索
		$("#searchButton").click(function() {
			queryByPage(currentPage,recordsPerPage);
		});
		//申请数据库button跳转
		$("#db_apply").click(function() {
			location.href = "${ctx}/db/toForm";
		});
	}
</script>
</body>
</html>
