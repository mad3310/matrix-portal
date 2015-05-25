 $(function(){
	//初始化
	page_init();
	/*查询功能*/
	
	$("#monitorNodeSearch").click(function(){
		var iw=document.body.clientWidth;
		if(iw>767){//md&&lg
		}else{
			$('.queryOption').addClass('collapsed').find('.widget-body').attr('style', 'dispaly:none;');
			$('.queryOption').find('.widget-header').find('i').attr('class', 'ace-icon fa fa-chevron-down');
			var qryStr='';
			var qryStr1=$('#monitorContainer').val();var qryStr2=$('#VipAddress').val();var qryStr3=$('#monitorPhyM').val();
			if(qryStr1){
				qryStr+='<span class="label label-success arrowed">'+qryStr1+'<span class="queryBadge" data-rely-id="monitorContainer"><i class="ace-icon fa fa-times-circle"></i></span></span>&nbsp;'
			}
			if(qryStr2){
				qryStr+='<span class="label label-warning arrowed">'+qryStr2+'<span class="queryBadge" data-rely-id="VipAddress"><i class="ace-icon fa fa-times-circle"></i></span></span>&nbsp;'
			}
			if(qryStr3){
				qryStr+='<span class="label label-purple arrowed">'+qryStr3+'<span class="queryBadge" data-rely-id="monitorPhyM"><i class="ace-icon fa fa-times-circle"></i></span></span>&nbsp;'
			}
			if(qryStr){
				$('.queryOption').find('.widget-title').html(qryStr);
				$('.queryBadge').click(function(event) {
					var id=$(this).attr('data-rely-id');
					$('#'+id).val('');
					$(this).parent().remove();
					queryMclusterMonitor();
					if($('.queryBadge').length<=0){
						$('.queryOption').find('.widget-title').html('container集群监控列表');
					}
					return;
				});
			}else{
				$('.queryOption').find('.widget-title').html('container集群监控列表');
			}

		}
		queryMclusterMonitor();
	});
	$("#monitorNodeClear").click(function(){
		var clearList = ["monitorContainer","monitorPhyM","VipAddress"]
		clearSearch(clearList);
	})
	enterKeydown($(".page-header > .input-group input"),queryMclusterMonitor);
});	
 
function queryMclusterMonitor(){
	var mclusterName = $("#monitorContainer").val()?$("#monitorContainer").val():'';
	var hclusterName = $("#monitorPhyM").val()?$("#monitorPhyM").val():'';
	var vip = $("#VipAddress").val()?$("#VipAddress").val():'';
	var queryCondition = {
			'mclusterName':mclusterName,
			'hclusterName':hclusterName,
			'ipAddr':vip.replace(/\%/g,"%25")
		}	
	$("#tby tr").remove();
	getLoading();
	$.ajax({ 
		cache:false,
		type : "get",
		//url : "/monitor/mcluster/list",
		url : queryUrlBuilder("/monitor/mcluster/list/",queryCondition),
		dataType : "json", /*这句可用可不用，没有影响*/
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var array = data.data;
			var tby = $("#tby");
			
			for (var i = 0, len = array.length; i < len; i++) {
				if(array[i] == null) continue;
				var mclusterName = '';
        		if(array[i].mcluster != undefined && array[i].mcluster != null) {
        			mclusterName = array[i].mcluster.mclusterName;
        		}
				var td1 = $("<td>"
						+ "<a class=\"link\" href=\"/detail/mcluster/" + array[i].mclusterId+"\">"+ mclusterName +"</a>"
						+ "</td>");
				var td2 = $("<td name=\"vip\">"
						+ array[i].ipAddr
						+ "</td>");

				if(array[i].hcluster.hclusterNameAlias){
					var td3 = $("<td  class='hidden-480'>"
							+ "<a class=\"link\"  href=\"/detail/hcluster/" + array[i].host.hclusterId+"\">"+array[i].hcluster.hclusterNameAlias+"</a>"
							+ "</td>");
				}else{
					var td3 = $("<td class='hidden-480'>"
 							+ "-"
							+ "</td>");
				}
				var td4 = $("<td name=\"mclusterStatus\">"
							+"<a><i class=\"ace-icon fa fa-spinner fa-spin  bigger-120\"/>数据抓取中...</a>"
							+ "</td>");
				var td5 = $("<td>"
						//+ "<a href=\"/monitor/"+array[i].ipAddr+"/node/status\" target=\"_blank\">查看详情</a>"
						+ "<a class=\"link\" href=\"/detail/mcluster/monitor/list/" + array[i].ipAddr + "/2\">查看详情</a>"
					+ "</td>");
				if(array[i].status == 0 ||array[i].status == 5||array[i].status == 13){
					var tr = $("<tr class=\"warning\"></tr>");
				}else if(array[i].status == 3 ||array[i].status == 4||array[i].status == 14){
					var tr = $("<tr class=\"default-danger\"></tr>");
				}else{
					var tr = $("<tr></tr>");
				}
				
				tr.append(td1).append(td2).append(td3).append(td4).append(td5);
				tr.appendTo(tby);
			}//循环json中的数据 
			/*增加标记行，控制各种状态的归类显示*/
			var tr1 = $("<tr class=\"default-danger normalTag\"></tr>");
			var tr2 = $("<tr class=\"default-danger lightDangerTag\"></tr>");
			var tr3 = $("<tr class=\"default-danger seriousTag\"></tr>");
			var tr4 = $("<tr class=\"default-danger disableClusterTag\"></tr>");
			var tr5 = $("<tr class=\"default-danger timeoutClusterTag\"></tr>");
			tby.prepend(tr1).prepend(tr2).prepend(tr3).prepend(tr4).prepend(tr5);
			updateMclusterStatus();//查询集群状态
		}
	});
}
function getMclusterStatus(ip,obj) {
	getLoading();
	$.ajax({ 
		cache:false,
		type : "get",
		url : "/monitor/"+ip+"/node/status",
		dataType : "json", 
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var result = data.data.result;
			if(result == "0"){
				$(obj).removeClass();
				$(obj).find('[name="mclusterStatus"]').html("<a>正常</a>");
				//$(obj).parent().find(".normalTag").after($(obj));
			}else if(result == "1"){
				$(obj).removeClass();
				$(obj).find('[name="mclusterStatus"]').html("<a>单节点故障</a>");
				$(obj).addClass("default-danger lightDanger");
				$(obj).parent().find(".lightDangerTag").after($(obj));	
			}else if(result == "2"){
				$(obj).removeClass();
				$(obj).find('[name="mclusterStatus"]').html("<a>危险</a>");
				$(obj).addClass("default-danger serious");
				$(obj).parent().find(".seriousTag").after($(obj));
			}else if(result == "3"){
				$(obj).removeClass();
				$(obj).find('[name="mclusterStatus"]').html("<a>集群不可用</a>");
				$(obj).addClass("default-danger disableCluster");
				$(obj).parent().find(".disableClusterTag").after($(obj));	
			}else if(result == "4"){
				$(obj).removeClass();
				$(obj).find('[name="mclusterStatus"]').html("<a>获取数据超时</a>");
				$(obj).addClass("default-danger timeoutCluster");
				$(obj).parent().find(".timeoutClusterTag").after($(obj));
			}
		}	
	});
}
function updateMclusterStatus(){
	$("#tby tr").each(function(){
		var ip = $(this).find('[name="vip"]').html();
		if(ip != null){
			var status = getMclusterStatus(ip,$(this));
		}
	});
}
function page_init(){
	$('#nav-search').addClass("hidden");
	queryMclusterMonitor();
	setInterval(function() {
		updateMclusterStatus();
		},60000);
}
