<%@ page language="java" pageEncoding="UTF-8"%>
<body>
<div class="row">
	<div class="col-xs-12">
	<div class="table-header">集群列表</div>
		<div>
			<table id="mcluster_list" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th class="center">
							<label class="position-relative">
								<input type="checkbox" class="ace" />
								<span class="lbl"></span>
							</label>
						</th>
						<th>集群名称</th>
						<th>集群所属用户</th>
						<th>
							<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
							创建时间 
						</th>
						<th class="hidden-480">当前状态</th>
						<th></th>
					</tr>
				</thead>
				<tbody id="tby">
				</tbody>
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
</div>
<script src="${ctx}/static/ace/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/static/ace/js/jquery.dataTables.bootstrap.js"></script>
<script type="text/javascript">
var currentPage = 1; //第几页 
var recordsPerPage = 10; //每页显示条数
	
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
function queryByPage(currentPage,recordsPerPage) {
	$("#tby tr").remove();
	$.ajax({
		type : "post",
		url : "${ctx}/mcluster/list/mgrData?currentPage="
				+ currentPage
				+ "&recordsPerPage="
				+ recordsPerPage
				+ "&mclusterName="
				+ $("#nav-search-input").val(),
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
				var td1 = $("<td class=\"center\">"
								+"<label class=\"position-relative\">"
								+"<input type=\"checkbox\" class=\"ace\"/>"
								+"<span class=\"lbl\"></span>"
								+"</label>"
							+"</td>");
				var td2 = $("<td>"
						+  "<a href=\"${ctx}/mcluster/mgrMclusterInfo?clusterId="+array[i].id+"\">"+array[i].mclusterName+"</a>"
						+ "</td>");
				var td3 = $("<td>"
						+ array[i].mclusterName
						+ "</td>");
				var td4 = $("<td>"
						+ array[i].createTime
						+ "</td>");
				var td5 = $("<td>"
						+ translateStatus(array[i].status)
						+ "</td>");
					
				var td6 = $("<td>"
							+"<div class=\"hidden-sm hidden-xs action-buttons\">"
								+"<a class=\"blue\" href=\"#\">"
									+"<i class=\"ace-icon fa fa-play-circle-o bigger-130\"></i>"
								+"</a>"
								+"<a class=\"green\" href=\"#\">"
									+"<i class=\"ace-icon fa  fa-power-off bigger-130\"></i>"
								+"</a>"
								+"<a class=\"red\" href=\"#\">"
									+"<i class=\"ace-icon fa fa-trash-o bigger-130\"></i>"
								+"</a>"
							+"</div>"
							+"<div class=\"hidden-md hidden-lg\">"
								+"<div class=\"inline position-relative\">"
									+"<button class=\"btn btn-minier btn-yellow dropdown-toggle\" data-toggle=\"dropdown\" data-position=\"auto\">"
										+"<i class=\"ace-icon fa fa-caret-down icon-only bigger-120\"></i>"
									+"</button>"
									+"<ul class=\"dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close\">"
										+"<li>"
											+"<a href=\"#\" class=\"tooltip-info\" data-rel=\"tooltip\" title=\"View\">"
												+"<span class=\"blue\">"
													+"<i class=\"ace-icon fa fa-search-plus bigger-120\"></i>"
												+"</span>"
											+"</a>"
										+"</li>"
										+"<li>"
											+"<a href=\"#\" class=\"tooltip-success\" data-rel=\"tooltip\" title=\"Edit\">"
												+"<span class=\"green\">"
													+"<i class=\"ace-icon fa fa-pencil-square-o bigger-120\"></i>"
												+"</span>"
											+"</a>"
										+"</li>"
										+"<li>"
											+"<a href=\"#\" class=\"tooltip-error\" data-rel=\"tooltip\" title=\"Delete\">"
												+"<span class=\"red\">"
													+"<i class=\"ace-icon fa fa-trash-o bigger-120\"></i>"
												+"</span>"
											+"</a>"
										+"</li>"
									+"</ul>"
								+"</div>"
							+"</div>"
						+"</td>"
					);	
					
				if(array[i].status == 3){
					var tr = $("<tr class=\"danger\"></tr>");
				}else{
					var tr = $("<tr></tr>");
				}
				
				tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6);
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
		} else {
			currentPage--;
			queryByPage(currentPage,recordsPerPage);
		}
	});

	// 下一页
	$("#nextPage").click(function() {
		if (currentPage == $("#totalPage_input").val()) {
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

function pageAction() {
	$("#searchButton").click(function() {
		queryByPage(currentPage,recordsPerPage);
	});
}
function page_init(){
	queryByPage(currentPage, recordsPerPage);
	pageAction();
	pageControl();
}
</script>
