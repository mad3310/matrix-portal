<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="page-content-area">
	<div class="page-header">
		<h1> 
			数据库用户列表
			<!-- <small> 
				<i class="ace-icon fa fa-angle-double-right"></i> 
				overview &amp; stats
			</small> -->
		</h1>
	</div>
	<!-- /.page-header -->
	<div class="row">
		<div class="widget-box widget-color-blue ui-sortable-handle col-xs-12">
			<div class="widget-header">
				<h5 class="widget-title">数据库用户列表</h5>
				<div class="widget-toolbar no-border">
					<button id="create_db_user" class="btn btn-white btn-primary btn-xs" type="button" onclick="buildUser()">
						<i class="ace-icont fa fa-plus"></i>
						 创建用户
					</button>
				</div>
			</div>
			<div class="widget-body">
				<div class="widget-main no-padding">
					<table class="table table-bordered" id="db_detail_table" >
						<thead>
							<tr>
								<th class="center">
									<label class="position-relative">
										<input type="checkbox" id="titleCheckbox" class="ace" />
										<span class="lbl"></span>
									</label>
								</th>
								<th>用户名</th>
								<th>所属数据库</th>
								<th>用户权限</th>
								<th>ip地址</th>
								<th>频次限制</th>
								<th>
									当前状态
								</th>
							</tr>
						</thead>
						<tbody id="tby">
						</tbody>
					</table>
				</div>
			</div>
		</div>
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
<!-- /.page-content-area -->
<script type="text/javascript">
var currentPage = 1; //第几页 
var recordsPerPage = 10; //每页显示条数
var currentSelectedLineDbName = 1;
	
 $(function(){
	//初始化
	page_init();

	$(document).on('click', 'th input:checkbox' , function(){
		var that = this;
		$(this).closest('table').find('tr > td:first-child input:checkbox')
		.each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});
});	
 
 	function buildUser() {
 		var ids = $("[name='db_user_id']:checked");
 		var str="";
 		var flag = 0; //0:无数据 -1:错误
 		ids.each(function(){
 			if($(this).closest("tr").children().last().html() == "正常"){
 				$.gritter.add({
 					title: '警告',
 					text: '创建用户只能选择待审核数据!',
 					sticky: false,
 					time: '3000',
 					class_name: 'gritter-warning'
 				});
 				flag = -1;
 				return false;
 			}else{
 				str +=($(this).val())+",";
 	 			flag += 1;
 			}
 		});
 		
 		if(flag > 0) {
	 		$.ajax({ 
				type : "get",
				url : "${ctx}/db/user/build/"+ str,
				dataType : "json",
				success : function(data) {
					if(data.result == 1) {
						$("#titleCheckbox").attr("checked", false);
						queryByPage(currentPage,recordsPerPage);
					}
				}
	 		});
 		} else if (flag == 0){
 			$.gritter.add({
				title: '警告',
				text: '请选择数据！',
				sticky: false,
				time: '5',
				class_name: 'gritter-warning'
			});
	
			return false;
 		}else{
 			return false;
 		}
 	}
 
	function queryByPage(currentPage,recordsPerPage) {
		$("#tby tr").remove();
		var dbName = $("#nav-search-input").val()?$("#nav-search-input").val():'null';
		$.ajax({ 
			type : "get",
			url : "${ctx}/db/user/list/" + currentPage + "/" + recordsPerPage+"/" + dbName,
			dataType : "json", /*这句可用可不用，没有影响*/
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				var array = data.data.data;
				var tby = $("#tby");
				var totalPages = data.data.totalPages;
				
				function translateStatus(status){
					if(status == 0){
						return "待审核";
					}else if(status  == 1){
						return "正常";
					}else if(status  == 4){
						return "未通过";
					}else{
						return "创建失败";
					}
				}
				
				for (var i = 0, len = array.length; i < len; i++) {
					var td1 = $("<td class=\"center\">"
									+"<label class=\"position-relative\">"
									+"<input name=\"db_user_id\" value= \""+array[i].id+"\" type=\"checkbox\" class=\"ace\"/>"
									+"<span class=\"lbl\"></span>"
									+"</label>"
								+"</td>");
					var td2 = $("<td>"
							+ array[i].username
							+ "</td>");
					var td3 = $("<td>"
							+ "<a href=\"${ctx}/db/detail/" + array[i].dbId+"\">"+array[i].db.dbName+"</a>"
							+ "</td>");
					if(array[i].type == "wr")
					{
						var td4 = $("<td>读写</td>");
					}else{
						var td4 = $("<td>管理员</td>");
					}
					var td5 = $("<td>"
							+ array[i].acceptIp
							+ "</td>");
					var td6 = $("<td>"
							+ array[i].maxConcurrency
							+ "</td>");
					var td7 = $("<td>"
							+ translateStatus(array[i].status)
							+ "</td>"); 
						
					if(array[i].status == 0){
						var tr = $("<tr class=\"warning\"></tr>");
					}else if(array[i].status == 3||array[i].status == 4){
						var tr = $("<tr class=\"danger\"></tr>");
					}else{
						var tr = $("<tr></tr>");
					}
					tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7);
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
				}
			},
			error : function(XMLHttpRequest,textStatus, errorThrown) {
				error(XMLHttpRequest);
				return false;
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
				$.gritter.add({
					title: '警告',
					text: '已到达首页',
					sticky: false,
					time: '5',
					class_name: 'gritter-warning'
				});
		
				return false;
				
			} else {
				currentPage--;
				queryByPage(currentPage,recordsPerPage);
			}
		});

		// 下一页
		$("#nextPage").click(function() {
			if (currentPage == $("#totalPage_input").val()) {
				$.gritter.add({
					title: '警告',
					text: '已到达末页',
					sticky: false,
					time: '5',
					class_name: 'gritter-warning'
				});
		
				return false;
				
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
	}
	
	function searchAction(){
		$('#nav-search-input').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	queryByPage(currentPage, recordsPerPage);
	        }
	    });
	}
	
	function page_init(){
		searchAction();
		queryByPage(currentPage,recordsPerPage);
		pageControl();
	}
</script>
