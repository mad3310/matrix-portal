$(function(){
	//隐藏搜索框
	$('#nav-search').addClass("hidden");
	queryContainer();
})
function queryContainer(){
	$("#tby tr").remove();
	getLoading();
	$.ajax({ 
		cache:false,
		type : "get",
		url : "/container/"+$("#mclusterId").val(),
		dataType : "json", 
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var array = data.data;
			var array_len=array.length;
			var tby = $("#tby");
			if($('#mclusterTitle').html().indexOf(array[0].mcluster.mclusterName) < 0){
 				$("#mclusterTitle").prepend(array[0].mcluster.mclusterName);
			}
 			$("#headerContainerName").append(array[0].mcluster.mclusterName);
			for (var i = 0, len = array.length; i < len; i++) {
				var td0 = $("<input name=\"container_id\" value= \""+array[i].id+"\" type=\"hidden\"/>");
				var td1 = $("<td>"
					    + array[i].containerName
				        + "</td>");
				var	td2 = $("<td class='hidden-480'>"
						+ array[i].type
						+ "</td>");
				var	td3 = $("<td class='hidden-480'>"
						+ array[i].hostIp
						+ "</td>");
				var	td4 = $("<td>"
						+ array[i].ipAddr
						+ "</td>");
				if(array[i].mountDir != null){
					jsonStr = array[i].mountDir.substring(1,array[i].mountDir.length-1);
					jsonArr = jsonStr.split(",");
					var mountDir = "";
					for (var j = 0; j < jsonArr.length; j++){						
						mountDir += jsonArr[j]+"<br/>";					
					}
					var	td5 = $("<td  class='hidden-480'>"
							+ mountDir
							+ "</td>");
				}else{
					var	td5 = $("<td class='hidden-480'>"
							+ '-'
							+ "</td>");
				}
				if(array[i].zookeeperIp != null){
					var	td6 = $("<td class='hidden-480'>"
							+ array[i].zookeeperIp
							+ "</td>");
				}else{
					var	td6 = $("<td class='hidden-480'>"
							+ '-'
							+ "</td>");
				}
				var	td7 = $("<td>"
						+ stateTransform(array[i].status,"rdsMcluster")
						+ "</td>");

				if(array_len<=4||array[i].type=="mclustervip"){
					var td8 = $("<td>"
						+"<div class=\"hidden-sm hidden-xs action-buttons\">"
						+"<a class=\"green\" href=\"#\" onclick=\"startContainer(this)\" title=\"启动\" data-toggle=\"tooltip\" data-placement=\"right\">"
						+"<i class=\"ace-icon fa fa-play-circle-o bigger-130\"></i>"
						+"</a>"
						+"<a class=\"blue\" href=\"#\" onclick=\"stopContainer(this)\" title=\"停止\" data-toggle=\"tooltip\" data-placement=\"right\">"
							+"<i class=\"ace-icon fa fa-power-off bigger-120\"></i>"
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
									+'</a>'
								+'</li>'
								+'</ul></div></div>'
						+ "</td>"
					);
				}else{
					var td8 = $("<td>"
						+"<div class=\"hidden-sm hidden-xs action-buttons\">"
						+"<a class=\"green\" href=\"#\" onclick=\"startContainer(this)\" title=\"启动\" data-toggle=\"tooltip\" data-placement=\"right\">"
						+"<i class=\"ace-icon fa fa-play-circle-o bigger-130\"></i>"
						+"</a>"
						+"<a class=\"blue\" href=\"#\" onclick=\"stopContainer(this)\" title=\"停止\" data-toggle=\"tooltip\" data-placement=\"right\">"
							+"<i class=\"ace-icon fa fa-power-off bigger-120\"></i>"
						+"</a>"
						+"<a class=\"red\" href=\"#\" onclick=\"compressContainer(this)\" title=\"缩容\" data-toggle=\"tooltip\" data-placement=\"right\">"
							+"<i class=\"ace-icon fa fa-compress bigger-120\"></i>"
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
									+'</a>'
								+'</li>'
								+'<li>'
									+'<a class=\"red\" href=\"#\" onclick=\"compressContainer(this)\" onfocus=\"this.blur();\" title=\"缩容\" data-toggle=\"tooltip\" data-placement=\"right\">'
										+'<span class="red">'
											+'<i class="ace-icon fa fa-compress bigger-120"></i>'
										+'</span>'
									+'</a></li></ul></div></div>'
						+ "</td>"
					);
				}
				
				var tr = $("<tr></tr>");;				
				tr.append(td0).append(td1).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7).append(td8);
				tr.appendTo(tby);
			}
			
			/*初始化tooltip*/
			$('[data-toggle = "tooltip"]').tooltip();
		}
	});
}

function startContainer(obj){
	var tr = $(obj).parents("tr").html();
	if (tr.indexOf("已停止") < 0){
		warn("当前状态无法执行启动操作!",3000);
		return 0;
	}
	function startCmd(){
		var containerId =$(obj).parents("tr").find('[name="container_id"]').val();
		getLoading();
		$.ajax({
			cache:false,
			url:'/container/start',
			type:'post',
			data:{containerId : containerId},
			success:function(data){
				removeLoading();
				if(error(data)) return;
				queryContainer();
			}
		});
	}
	confirmframe("启动container","启动container大概需要几分钟时间!","请耐心等待...",startCmd);
}
function stopContainer(obj){
	var tr = $(obj).parents("tr").html();
	if (tr.indexOf("运行中") < 0 && tr.indexOf("异常") < 0){
		warn("当前状态无法执行关闭操作!",3000);
		return 0;
	}
	function stopCmd(){
		var containerId =$(obj).parents("tr").find('[name="container_id"]').val();
		getLoading();
		$.ajax({
			cache:false,
			url:'/container/stop',
			type:'post',
			data:{containerId : containerId},
			success:function(data){
				removeLoading();
				if(error(data)) return;
				queryContainer();
			}
		});
	}
	confirmframe("关闭container","关闭container将不能提供服务,再次启动需要十几分钟!","您确定要关闭?",stopCmd);
}
function compressContainer(obj){
	var tr = $(obj).parents("tr").html();
	if (tr.indexOf("删除中") >= 0){
		warn("正在删除container,请耐心等待...",3000);
		return 0;
	}
	function compressCmd(){
		var containerId =$(obj).parents("tr").find('[name="container_id"]').val();
		getLoading();
		$.ajax({
			cache:false,
			url:'/container/'+containerId,
			type:'delete',
			success:function(data){
				removeLoading();
				if(error(data)) return;
				queryContainer();
			}
		});
	}
	confirmframe("删除container","删除container,缩容container集群!","您确定要删除?",compressCmd);
}
