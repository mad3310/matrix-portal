<%@ page language="java" pageEncoding="UTF-8"%>
<body>
<div class="row">
	<div class="col-md-3">
		<h3 class="text-left">运维管理</h3>
	</div>
	<div  class="col-md-5">
		<div id="pageMessage"></div>
	</div>
	<div class="col-md-4">
		<form class="navbar-form navbar-right" role="search">
			<div class="form-group">
				<input id="mclusterName" type="text" value=""
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
		<p>一个Mcluster上可以有多个DB。</p>
<!-- 		<p>
			<a class="btn" href="#">查看详细使用教程 »</a>
		</p> -->
	</div>
	<div class="col-md-9 column">
<!-- 		<button id="db_audit" type="button" class="btn btn-success" data-toggle="modal">审批DB</button> -->
		<table id="userdata"
			class="table table-striped table-hover table-responsive">
			<thead>
				<tr>
					<th>Mcluster名称</th>
					<th>所属用户</th>
					<th>创建时间</th>
					<th>当前状态</th>
				</tr>
			</thead>
			<tbody id="tby"></tbody>
		</table>
		<div id="pageControlBar">
			<input type="hidden" id="totalPage_input" />
			<ul class="pager">
				<li><a href="javascript:void(0);" id="firstPage">&laquo首页</a></li>
				<li><a href="javascript:void(0);" id="prevPage">上一页</a></li>
				<li><a href="javascript:void(0);" id="nextPage">下一页</a></li>
				<li><a href="javascript:void(0);" id="lastPage">末页&raquo</a></li>

				<li>共<lable id="totalPage"></lable>页
				</li>
				<li>第<lable id="currentPage"></lable>页
				</li>
				<li>共<lable id="totalRows"></lable>条记录
				</li>
			</ul>
		</div>
	</div>
</div>
<script type="text/javascript">
var currentPage = 1; //第几页 
var recordsPerPage = 10; //每页显示条数
	
	 $(function(){
		$("#signin").show();//显示header中登录框
		$("#mclusterMgr").addClass("active");//高亮显示页面名
		//初始化列表 
		queryByPage(currentPage, recordsPerPage);
		pageControl();
		$("#pageMessage").hide();
		page_init();
	});	
	function queryByPage(currentPage,recordsPerPage) {
		$("#tby tr").remove();
		$.ajax({
			type : "post",
			url : "${ctx}/mcluster/list/mgrData?currentPage="
					+ currentPage
					+ "&recordsPerPage="
					+ recordsPerPage
					+ "&mclusterName="
					+ $("#mclusterName").val(),
			dataType : "json", /*这句可用可不用，没有影响*/
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				var array = data.data.data;
				var tby = $("#tby");
				var totalPages = data.data.totalPages;
				
				function translateStatus(status){
					var statuStr;
					if(status == 0){
						return "启动";
					}else if(status  == 1){
						return "关闭";
					}else{
						return "异常";
					}
				}
				
				for (var i = 0, len = array.length; i < len; i++) {
					var td1 = $("<td>"
							+  "<a href=\"${ctx}/mcluster/mgrMclusterInfo?clusterId="+array[i].id+"\">"+array[i].mclusterName+"</a>"
							+ "</td>");
/* 					var td1 = $("<td>"
							+  "<a href=\"${ctx}/db/mgrList?clusterId="+array[i].id+"\">"+array[i].mclusterName+"</a>"
							+ "</td>"); */
					var td2 = $("<td>"
							+ array[i].mclusterName
							+ "</td>");
					var td3 = $("<td>"
							+ array[i].createTime
							+ "</td>");
					var td4 = $("<td>"
							+ translateStatus(array[i].status)
							+ "</td>");
					if(array[i].status == 3){
						var tr = $("<tr class=\"danger\"></tr>");
					}else{
						var tr = $("<tr></tr>");
					}
					
					tr.append(td1).append(td2).append(td3).append(td4);
					tr.appendTo(tby);
				}//循环json中的数据 
				
				if (totalPages <= 1) {
					$("#pageControlBar").hide();
				} else {
					$("#pageControlBar").show();
					$("#totalPage_input").val(totalPages);
					$("#currentPage").html(currentPage);
					$("#totalRows").html(data.data.totalRecords);
					$("#totalPage").html(totalPages);
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
				$('#pageMessage').html("<p class=\"bg-warning\" style=\"color:red;font-size:16px;\"><strong>警告!</strong>已经到达最后一页</p>").show().fadeOut(3000);
				
			} else {
				currentPage--;
				queryByPage(currentPage,recordsPerPage);
			}
		});

		// 下一页
		$("#nextPage").click(function() {
			if (currentPage == $("#totalPage_input").val()) {
				$('#pageMessage').html("<p class=\"bg-warning\" style=\"color:red;font-size:16px;\"><strong>警告!</strong>已经到达最后一页</p>").show().fadeOut(3000);
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
		$("#searchButton").click(function() {
			queryByPage(currentPage,recordsPerPage);
		});
	}
	
	function page_init(){
		$("#db_audit").click(function() {
			location.href = "${ctx}/db/mgrAudit/list";
		});
		
	}
</script>

</body>
</html>
