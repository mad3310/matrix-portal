var currentPage = 1; //第几页 
var recordsPerPage = 15; //每页显示条数
var queryBuildStatusrefresh;//刷新handler
	
$(function(){
	page_init();
	/*动态加载界面下拉列表值*/
	var sltArray = [1,2,3,5,7,8,9,10,13,14];
	addSltOpt(sltArray,$("#containerStatus"));
	
	$(document).on('click', 'th input:checkbox' , function(){
		var that = this;
		$(this).closest('table').find('tr > td:first-child input:checkbox')
		.each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});
	var mclusterId;
	var status;
	//modal显示创建进度
	/*$(document).on('click', "[name='buildStatusBoxLink']" , function(){
		mclusterId = $(this).closest('tr').find('td:first input').val();
		
		if($(this).html().indexOf("运行中")>=0){
			$('#buildStatusHeader').html("创建成功");
			status = "1";
		}else if($(this).html().indexOf("创建中")>=0){
			$('#buildStatusHeader').html("<i class=\"ace-icon fa fa-spinner fa-spin green bigger-125\"></i>创建中...");
			status = "2";
		}else if($(this).html().indexOf("创建失败")>=0){
			//$('#buildStatusHeader').html("<font color=\"red\">创建失败</font>");
			$('#buildStatusHeader').html("创建失败");
			status = "3";
		}
		queryBuildStatus(mclusterId,"new");
	});*/
	
	$('#create-mcluster-status-modal').on('shown.bs.modal', function(){
		if(status == "2") {
			queryBuildStatusrefresh = setInterval(function() {  
				queryBuildStatus(mclusterId,"update");
			},5000);
		}
	}).on('hidden.bs.modal', function (e) {
		queryBuildStatusrefresh = window.clearInterval(queryBuildStatusrefresh);
		location.reload();
	});
	
	$("#mclusterSearch").click(function(){
		var iw=document.body.clientWidth;
		if(iw>767){//md&&lg
		}else{
			$('.queryOption').addClass('collapsed').find('.widget-body').attr('style', 'dispaly:none;');
			$('.queryOption').find('.widget-header').find('i').attr('class', 'ace-icon fa fa-chevron-down');
			var qryStr='';
			var qryStr1=$('#containerName').val();var qryStr2=$("#Physicalcluster").find('option:selected').text();var qryStr3=$("#containeruser").find('option:selected').text();var qryStr4;
			if($('#containerStatus').val()){
				qryStr4=translateStatus($('#containerStatus').val());
			}
			if(qryStr1){
				qryStr+='<span class="label label-success arrowed">'+qryStr1+'<span class="queryBadge" data-rely-id="containerName"><i class="ace-icon fa fa-times-circle"></i></span></span>&nbsp;'
			}
			if(qryStr2){
				qryStr+='<span class="label label-warning arrowed">'+qryStr2+'<span class="queryBadge" data-rely-id="Physicalcluster"><i class="ace-icon fa fa-times-circle"></i></span></span>&nbsp;'
			}
			if(qryStr3){
				qryStr+='<span class="label label-purple arrowed">'+qryStr3+'<span class="queryBadge" data-rely-id="containeruser"><i class="ace-icon fa fa-times-circle"></i></span></span>&nbsp;'
			}
			if(qryStr4){
				qryStr+='<span class="label label-yellow arrowed">'+qryStr4+'<span class="queryBadge" data-rely-id="containerStatus"><i class="ace-icon fa fa-times-circle"></i></span></span>&nbsp;'
			}
			if(qryStr){
				$('.queryOption').find('.widget-title').html(qryStr);
				$('.queryBadge').click(function(event) {
					var id=$(this).attr('data-rely-id');
					$('#'+id).val('');
					$(this).parent().remove();
					queryByPage();
					if($('.queryBadge').length<=0){
						$('.queryOption').find('.widget-title').html('Container集群查询条件');
					}
					return;
				});
			}else{
				$('.queryOption').find('.widget-title').html('Container集群查询条件');
			}

		}
		queryByPage();
	});
	$("#mclusterClearSearch").click(function(){
		var clearList = ["containerName","Physicalcluster","containeruser","containerStatus"];
		clearSearch(clearList);
	});
	
	enterKeydown($(".page-header > .input-group input"),queryByPage);
});

function queryByPage() {
	var clusterName = $("#containerName").val()?$("#containerName").val():'';
	var hclusterName = $("#Physicalcluster").find('option:selected').attr('data-hclsName')?$("#Physicalcluster").find('option:selected').attr('data-hclsName'):'';
	var userName = $("#containeruser").find('option:selected').text()?$("#containeruser").find('option:selected').text():'';
	var status = $("#containerStatus").val()?$("#containerStatus").val():'';
	var queryCondition = {
			'currentPage':currentPage,
			'recordsPerPage':recordsPerPage,
			'clusterName':clusterName,
			'hclusterName':hclusterName,
			'userName':userName,
			// 'createTime':createTime,
			'status':status
	}
	$("#tby tr").remove();
	getLoading();
	$.ajax({
		cache:false,
		type : "get",
		// url : "/slb/cluster",
		url : queryUrlBuilder("/slb/cluster",queryCondition),
		dataType : "json", /*这句可用可不用，没有影响*/
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var array = data.data.data;
			var tby = $("#tby");
			var totalPages = data.data.totalPages;
			
			for (var i = 0, len = array.length; i < len; i++) {
				var td1 = $("<td class=\"center\">"
								+"<label class=\"position-relative\">"
								+"<input name=\"mcluster_id\" value= \""+array[i].id+"\" type=\"checkbox\" class=\"ace\"/>"
								+"<span class=\"lbl\"></span>"
								+"</label>"
							+"</td>");
				var td2 = $("<td>"
						+  "<a class=\"link\" href=\"/detail/slb/cluster/"+array[i].id+"\">"+array[i].clusterName+"</a>"
						+ "</td>");
				if(array[i].hcluster){
					var td3 = $("<td class='hidden-480'>"
							+ "<a class=\"link\" href=\"/detail/hcluster/" + array[i].hclusterId+"\">"+array[i].hcluster.hclusterNameAlias+"</a>"
							+ "</td>");//
				} else {
					var td3 = $("<td class='hidden-480'> </td>");
				} 
				var type = "";
				if(array[i].type) {
					type="后台创建";
				} else {
					type = "系统创建";
				}
				var td4 = $("<td class='hidden-480'>"
						+ type
						+ "</td>");
				
				var userName='system';
				if(array[i].createUserModel) {
					userName = array[i].createUserModel.userName;
				}
				var td5 = $("<td>"
						+ userName
						+ "</td>");
				var td6 = $("<td class='hidden-480'>"
						+ date('Y-m-d H:i:s',array[i].createTime)
						+ "</td>");
				if(array[i].status == 2){
					var td7 = $("<td>"
							+"<a name=\"buildStatusBoxLink\" data-toggle=\"modal\" data-target=\"#create-mcluster-status-modal\" style=\"cursor:pointer; text-decoration:none;\">"
							+"<i class=\"ace-icon fa fa-spinner fa-spin green bigger-125\"/>"
							+"创建中...</a>"
							+ "</td>");
				}else if(array[i].status == 3){
					var td7 = $("<td>"
							+"<a name=\"buildStatusBoxLink\" data-toggle=\"modal\" data-target=\"#create-mcluster-status-modal\" style=\"cursor:pointer; text-decoration:none;\">"
							+translateStatus(array[i].status)
							+"</a>"
							+ "</td>");
				}else{
					var td7 = $("<td>"
							+translateStatus(array[i].status)
							+ "</td>");
					
				}
				
				if(array[i].status == 3){
					var td8 = $("<td class='hidden'>"
							+"<div class=\"hidden-sm hidden-xs  action-buttons\">"
							+"<a class=\"green\" href=\"#\" onclick=\"startMcluster(this)\" onfocus=\"this.blur();\" title=\"启动\" data-toggle=\"tooltip\" data-placement=\"right\">"
							+"<i class=\"ace-icon fa fa-play-circle-o bigger-130\"></i>"
							+"</a>"
							+"<a class=\"blue\" href=\"#\" onclick=\"stopMcluster(this)\" onfocus=\"this.blur();\" title=\"停止\" data-toggle=\"tooltip\" data-placement=\"right\">"
								+"<i class=\"ace-icon fa fa-power-off bigger-120\"></i>"
							+"</a>"
							+"<a class=\"red\" href=\"#\" onclick=\"deleteMcluster(this);\" onfocus=\"this.blur();\"  title=\"删除\" data-toggle=\"tooltip\" data-placement=\"right\">"
							+"<i class=\"ace-icon fa fa-trash-o bigger-120\"></i>"
							+"</a>"
							+"</div>"
							+'<div class="hidden-md hidden-lg">'
							+'<div class="inline pos-rel">'
							+'<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto">'
								+'<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>'
							+'</button>'
							+'<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">'
								+'<li>'
									+'<a class=\"green\" href=\"#\" onclick=\"startMcluster(this)\" onfocus=\"this.blur();\" title=\"启动\" data-toggle=\"tooltip\" data-placement=\"right\">'
										+'<span class="blue">'
											+'<i class="ace-icon fa fa-play-circle-o bigger-120"></i>'
										+'</span>'
									+'</a>'
								+'</li>'
								+'<li>'
									+'<a class=\"blue\" href=\"#\" onclick=\"stopMcluster(this)\" onfocus=\"this.blur();\" title=\"停止\" data-toggle=\"tooltip\" data-placement=\"right\">'
										+'<span class="green">'
											+'<i class="ace-icon fa fa-power-off bigger-120"></i>'
										+'</span>'
									+'</a></li><li>'
									+'<a  class=\"red\" href=\"#\" onclick=\"deleteMcluster(this);\" onfocus=\"this.blur();\"  title=\"删除\" data-toggle=\"tooltip\" data-placement=\"right\">'
										+'<span class="red"><i class="ace-icon fa fa-trash-o bigger-120"></i></span>'
									+'</a></li></ul></div></div>'
							+ "</td>"
					);
				}else{
					var td8 = $("<td class='hidden'>"
							+"<div class=\"hidden-sm hidden-xs  action-buttons\">"
							+"<a class=\"green\" href=\"#\" onclick=\"startMcluster(this)\" onfocus=\"this.blur();\" title=\"启动\" data-toggle=\"tooltip\" data-placement=\"right\">"
							+"<i class=\"ace-icon fa fa-play-circle-o bigger-130\"></i>"
							+"</a>"
							+"<a class=\"blue\" href=\"#\" onclick=\"stopMcluster(this)\" onfocus=\"this.blur();\" title=\"停止\" data-toggle=\"tooltip\" data-placement=\"right\">"
							+"<i class=\"ace-icon fa fa-power-off bigger-120\"></i>"
							+"</a>"
							/*+"<a class=\"red\" href=\"#\" onclick=\"deleteMcluster(this);\" onfocus=\"this.blur();\"  title=\"删除\" data-toggle=\"tooltip\" data-placement=\"right\">"
							+"<i class=\"ace-icon fa fa-trash-o bigger-120\"></i>"
							+"</a>"*/
							+"</div>"
							+'<div class="hidden-md hidden-lg">'
							+'<div class="inline pos-rel">'
							+'<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto">'
								+'<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>'
							+'</button>'
							+'<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">'
								+'<li>'
									+'<a class=\"green\" href=\"#\" onclick=\"startMcluster(this)\" onfocus=\"this.blur();\" title=\"启动\" data-toggle=\"tooltip\" data-placement=\"right\">'
										+'<span class="blue">'
											+'<i class="ace-icon fa fa-play-circle-o bigger-120"></i>'
										+'</span>'
									+'</a>'
								+'</li>'
								+'<li>'
									+'<a class=\"blue\" href=\"#\" onclick=\"stopMcluster(this)\" onfocus=\"this.blur();\" title=\"停止\" data-toggle=\"tooltip\" data-placement=\"right\">'
										+'<span class="green">'
											+'<i class="ace-icon fa fa-power-off bigger-120"></i>'
										+'</span>'
									+'</a></li>'
									+'</ul></div></div>'
							+ "</td>"
					);
				}
				
					
				if(array[i].status == 3||array[i].status == 4||array[i].status == 14){
					var tr = $("<tr class=\"default-danger\"></tr>");
				}else if(array[i].status == 5||array[i].status == 13){
					var tr = $("<tr class=\"warning\"></tr>");
				}else{
					var tr = $("<tr></tr>");
				}
				
				tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7).append(td8);
				tr.appendTo(tby);
			}//循环json中的数据 
			
			/*初始化tooltip*/
			$('[data-toggle = "tooltip"]').tooltip();
			
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
		queryByPage();
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
			queryByPage();
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
			queryByPage();
		}
	});

	// 末页
	$("#lastPage").bind("click", function() {
		currentPage = $("#totalPage_input").val();
		queryByPage();
	});
}
function queryHcluster(){
	var options=$('#Physicalcluster');
	getLoading();
	$.ajax({
		cache:false,
		url:'/hcluster/byType/slb',
		type:'get',
		dataType:'json',
		success:function(data){
			removeLoading();
			var array = data.data;
			for(var i = 0, len = array.length; i < len; i++){
				
				var option = $("<option value=\""+array[i].id+"\" data-hclsName='"+array[i].hclusterName+"'>"
								+array[i].hclusterNameAlias
								+"</option>");
				options.append(option)
			}
			initChosen();
		}
	});
}
function page_init(){
	queryHcluster();
	queryUser();
	queryByPage();
	pageControl();
	$('[name = "popoverHelp"]').popover();
}
