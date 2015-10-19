/**
 * Created by yaokuo on 2014/12/14.
 * dblist page 
 */
define(function(require,exports,module){
    var $ = require('jquery');
    /*
	 * 引入相关js，使用分页组件
	 */
	require('bootstrap');
	require('paginator')($);
	
    var common = require('../../common');
    var cn = new common();
   
    
    var DataHandler = function(){
    };

    module.exports = DataHandler;

    DataHandler.prototype = {
        GceListHandler : function(data){
        	$(".data-tr").remove();
            var $tby = $('#tby');
            var array = data.data.data;
            if(array.length == 0){
            	cn.emptyBlock($tby);
            	if($("#paginatorBlock").length > 0){
            		$("#paginatorBlock").hide();
            	}
            }else{
            	 if($("#noData").length > 0){
            		 $("#noData").remove();
            	 }
            	 if($("#paginatorBlock").length > 0){
            		 $("#paginatorBlock").show();
            	 }
                for(var i= 0, len= array.length;i<len;i++){
                    var td1 = $("<td width=\"10\">"
                            + "<input type=\"checkbox\" name=\"gcecluster_id\" value= \""+array[i].id+"\">"
                            + "</td>");
                    var gceName = "";
                    if(cn.Displayable(array[i].status)){
                        gceName = "<a href=\"/detail/gce/"+array[i].id+"\">" + array[i].gceName + "</a>"
                    }else{
                        gceName = "<span class=\"text-explode font-disabled\">"+array[i].gceName +"</span>"
                    }
                    var td2 = $("<td class=\"padding-left-32\">"
                            + gceName
                            +"</td>");
                    var td10 = $("<td width=\"250\">"
                    		+ (array[i].descn?array[i].descn:'')
                    		+"</td>");
                    var td6 = $("<td class='hidden-xs'>-</td>");
                   if(array[i].gceServerProxy != undefined && array[i].gceServerProxy!=null){
	                   	 if(cn.Displayable(array[i].status) && cn.Displayable(array[i].gceServerProxy.status)){
	                        gceProxyName = "<a href=\"/detail/gce/"+array[i].gceServerProxy.id+"\">" + array[i].gceServerProxy.gceName + "</a>"
	                    }else{
	                        gceProxyName = "<span class=\"text-explode font-disabled\">"+array[i].gceServerProxy.gceName +"</span>"
	                    }
                    	td6=$("<td  class='hidden-xs'>"+gceProxyName+"</td>");
                    }
                    var td3 = $("<td><span>"+cn.TranslateStatus(array[i].status)+"</span></td>");
                    var td4 = $("<td class='hidden-xs'><span>"+cn.gceTypeTranslation(array[i].type)+"</span></td>");
                    var td5 = $("<td class=\"padding-left-32 hidden-xs\">"
                            + "<span>"+getAccpetAddr(array[i].gceServerProxy?array[i].gceServerProxy.gceContainers:array[i].gceContainers)+"</span>"
                            +"</td>");
                    var td7="<td class='hidden-xs'></td>";
                    if(array[i].hcluster != undefined && array[i].hcluster != null){
                        var td7 = $("<td class='hidden-xs'>"
                        + "<span>"+array[i].hcluster.hclusterNameAlias+"</span></td>"
                        + "</td>");
                    }
                    
                    var td8 = $("<td class='hidden-xs'><span><span>包年  </span><span class=\"text-success\">"+cn.RemainAvailableTime(array[i].createTime)+"</span><span>天后到期</span></span></td>");
                    var td9 = $("<td></td>");
                    if(array[i].status == 6){
                    	td9 = $("<td class=\"text-right hidden-xs\"><a href=\"/detail/gce/"+array[i].id+"\">管理</a>"
                        		+ "<span class=\"text-explode font-disabled\">|</span>"
                    			+ "<a href=\"javascript:void(0)\"><span class=\"text-explode gce-stop\" >停止</span></a>"
                    			+ "<span class=\"text-explode font-disabled\">|</span>"
                    			+ "<a href=\"javascript:void(0)\"><span class=\"text-explode gce-restart\">重启</span></a>"
                    			+ "<span class=\"text-explode font-disabled\">|续费|升级|删除</span></td>" 
                                // +"<td class='text-right hidden-sm hidden-md hidden-lg'><a href=\"/detail/gce/"+array[i].id+"\"><span class='text-success'><i class='fa fa-cogs'></i></span></a>&nbsp;&nbsp;"
                                // +"<a href><span class=\"text-explode gce-stop text-warning\"><i class='fa fa-dot-circle-o'></i></span></a>&nbsp;&nbsp;"
                                // +"<a href><span class=\"text-explode gce-restart\"><i class='fa fa-repeat'></i></span></a>&nbsp;&nbsp;"
                                // +"<a><span class=\"text-explode font-disabled\"><i class='fa fa-shopping-cart'></i></span></a>&nbsp;&nbsp;"
                                // +"<a><span class='text-explode font-disabled'><i class='fa fa-upload'></i></span></a>&nbsp;&nbsp;<a><span class='text-explode font-disabled'><i class='fa fa-trash'></i></span></a></td>"

                                +"<td class='text-right hidden-sm hidden-md hidden-lg'><div class='hidden-sm hidden-md hidden-lg pull-right m-tgbtn dropdown'><button type='button' class='dropdown-toggle' data-toggle='dropdown'><i class='fa fa-navicon'></i></button>"                 
                                +"<ul class='dropdown-menu'><li><a href=\"/detail/gce/"+array[i].id+"\"><span class='text-success'><i class='fa fa-cogs'></i></span></a></li>"
                                +"<li><a href><span class=\"text-explode gce-stop text-warning\"><i class='fa fa-dot-circle-o'></i></span></a></li>"
                                +"<li><a href><span class=\"text-explode gce-restart text-primary\"><i class='fa fa-repeat'></i></span></a></li>"
                                +"<li><a><span class=\"text-explode font-disabled\"><i class='fa fa-shopping-cart'></i></span></a></li>"
                                +"<li><a><span class='text-explode font-disabled'><i class='fa fa-upload'></i></span></a></li><li><a><span class='text-explode font-disabled'><i class='fa fa-trash'></i></span></a></li></ul></div></td>" 
                    			/*+"<a href=\"javascript:void(0)\"><span class=\"text-explode gce-delete\">删除</span></a></td>"*/);
                    }else if(array[i].status == 9){
                    	td9 = $("<td class=\"text-right hidden-xs\"><span class=\"text-explode font-disabled\">管理</span>"
                    		+ "<span class=\"text-explode font-disabled\">|</span>"
                			+ "<a href=\"javascript:void(0)\"><span class=\"text-explode gce-start\">启动</span></a>"
                			+ "<span class=\"text-explode font-disabled\">|续费|升级|删除</span></td>"
                            // +"<td class='text-right hidden-sm hidden-md hidden-lg'><a href=\"/detail/gce/"+array[i].id+"\"><span class='text-explode font-disabled'><i class='fa fa-cogs'></i></span></a>&nbsp;&nbsp;"
                            // +"<a href><span class=\"text-explode gce-start text-success\"><i class='fa fa-play-circle'></i></span></a>&nbsp;&nbsp;"
                            // +"<a><span class=\"text-explode font-disabled\"><i class='fa fa-shopping-cart'></i></span></a>&nbsp;&nbsp;"
                            // +"<a><span class='text-explode font-disabled'><i class='fa fa-upload'></i></span></a>&nbsp;&nbsp;<a><span class='text-explode font-disabled'><i class='fa fa-trash'></i></span></a></td>"

                            // +"<td class='text-right hidden-sm hidden-md hidden-lg'><div class='hidden-sm hidden-md hidden-lg pull-right m-tgbtn dropdown'><button type='button' class='dropdown-toggle' data-toggle='dropdown'><i class='fa fa-navicon'></i></button>"                 
                            // +"<ul class='dropdown-menu'><li><a href=\"/detail/gce/"+array[i].id+"\"><span class='text-explode font-disabled'><i class='fa fa-cogs'></i></span></a></li>"
                            // +"<li><a href><span class=\"text-explode gce-start text-success\"><i class='fa fa-play-circle'></i></span></a></li>"
                            // +"<li><a><span class=\"text-explode font-disabled\"><i class='fa fa-shopping-cart'></i></span></a></li>"
                            // +"<li><a><span class='text-explode font-disabled'><i class='fa fa-upload'></i></span></a></li><li><a><span class='text-explode font-disabled'><i class='fa fa-trash'></i></span></a></li></ul></div></td>" 
                			/*+"<a href=\"javascript:void(0)\"><span class=\"text-explode gce-delete\">删除</span></a></td>"*/);
                    }else if(array[i].status == 5){
                    	td9 = $("<td class=\"text-right hidden-xs\"><span class=\"text-explode font-disabled\">管理</span>"
                    		+ "<span class=\"text-explode font-disabled\">|</span>"
                			+ "<a href=\"javascript:void(0)\"><span class=\"text-explode gce-restart\">重启</span></a>"
                			+ "<span class=\"text-explode font-disabled\">|续费|升级|删除</span></td>"
                            // +"<td class='text-right hidden-sm hidden-md hidden-lg'><a href=\"/detail/gce/"+array[i].id+"\"><span class='text-explode font-disabled'><i class='fa fa-cogs'></i></span></a>&nbsp;&nbsp;"
                            // +"<a href><span class=\"text-explode gce-restart text-success\"><i class='fa fa-repeat'></i></span></a>&nbsp;&nbsp;"
                            // +"<a><span class=\"text-explode font-disabled\"><i class='fa fa-shopping-cart'></i></span></a>&nbsp;&nbsp;"
                            // +"<a><span class='text-explode font-disabled'><i class='fa fa-upload'></i></span></a>&nbsp;&nbsp;<a><span class='text-explode font-disabled'><i class='fa fa-trash'></i></span></a></td>"

                            +"<td class='text-right hidden-sm hidden-md hidden-lg'><div class='hidden-sm hidden-md hidden-lg pull-right m-tgbtn dropdown'><button type='button' class='dropdown-toggle' data-toggle='dropdown'><i class='fa fa-navicon'></i></button>"                 
                            +"<ul class='dropdown-menu'><li><a href=\"/detail/gce/"+array[i].id+"\"><span class='text-explode font-disabled'><i class='fa fa-cogs'></i></span></a></li>"
                            +"<li><a href><span class=\"text-explode gce-restart text-success\"><i class='fa fa-repeat'></i></span></a></li>"
                            +"<li><a><span class=\"text-explode font-disabled\"><i class='fa fa-shopping-cart'></i></span></a></li>"
                            +"<li><a><span class='text-explode font-disabled'><i class='fa fa-upload'></i></span></a></li><li><a><span class='text-explode font-disabled'><i class='fa fa-trash'></i></span></a></li></ul></div></td>" 
                			/*+"<a href=\"javascript:void(0)\"><span class=\"text-explode gce-delete\">删除</span></a></td>"*/);
                    }else{
                    	td9 = $("<td class=\"text-right hidden-xs\"><span class=\"text-explode font-disabled\">管理|续费|升级|删除</span></td>"
                            // +"<td class='text-right hidden-sm hidden-md hidden-lg'><a href><span class='text-explode font-disabled'><i class='fa fa-cogs'></i></span></a>&nbsp;&nbsp;"
                            // +"<a><span class=\"text-explode font-disabled\"><i class='fa fa-shopping-cart'></i></span></a>&nbsp;&nbsp;"
                            // +"<a><span class='text-explode font-disabled'><i class='fa fa-upload'></i></span></a>&nbsp;&nbsp;<a><span class='text-explode font-disabled'><i class='fa fa-trash'></i></span></a></td>"

                             +"<td class='text-right hidden-sm hidden-md hidden-lg'><div class='hidden-sm hidden-md hidden-lg pull-right m-tgbtn dropdown'><button type='button' class='dropdown-toggle' data-toggle='dropdown'><i class='fa fa-navicon'></i></button>"                 
                             +"<ul class='dropdown-menu'><li><a href='https://www.baidu.com/'><span class='text-explode font-disabled'><i class='fa fa-cogs'></i></span></a></li>"
                             +"<li><a><span class=\"text-explode font-disabled\"><i class='fa fa-shopping-cart'></i></span></a></li>"
                             +"<li><a><span class='text-explode font-disabled'><i class='fa fa-upload'></i></span></a></li><li><a><span class='text-explode font-disabled'><i class='fa fa-trash'></i></span></a></li></ul></div></td>"
                    		/*+"<a href=\"javascript:void(0)\"><span class=\"text-explode gce-delete\">删除</span></a></td>"*/);
                    }
                     var tr = $("<tr class='data-tr'></tr>");
                    tr.append(td1).append(td2).append(td10).append(td6).append(td3).append(td4).append(td5).append(td7).append(td8).append(td9);
                    tr.appendTo($tby);
	            }
	             $(".gce-start").click(function(){
	            		var data = {id : $(this).closest("tr").find("[name=gcecluster_id]").val()};
	            		var url = "/gce/start";
	            		cn.PostData(url,data,function(){
	            			window.location.href = "/list/gce";
	            		});
	            	});
	            	$(".gce-restart").click(function(){
	            		var data = {id : $(this).closest("tr").find("[name=gcecluster_id]").val()};
	            		var url = "/gce/restart";
	            		cn.PostData(url,data,function(){
	            			window.location.href = "/list/gce";
	            		});
	            	});
	            	$(".gce-stop").click(function(){
	            		var data = {id : $(this).closest("tr").find("[name=gcecluster_id]").val()};
	            		var url = "/gce/stop";
	            		cn.PostData(url,data,function(){
	            			window.location.href = "/list/gce";
	            		});
	            	});
	            	 $(".gce-delete").click(function () {    
		            	var id =$(this).closest("tr").find("input").val();
		            	var name = $(this).closest("tr").find("td:eq(1)").html();
		                var title = "确认";
		                var text = "您确定要删除("+name+")此应用";
		                cn.DialogBoxInit(title,text,gceDelete,id);
		            })
	       
	            /*
	             * 设置分页数据
	             */
	            $("#totalRecords").html(data.data.totalRecords);
	            $("#recordsPerPage").html(data.data.recordsPerPage);
	            
	            if(data.data.totalPages < 1){
	        		data.data.totalPages = 1;
	        	};
	        	
	            $('#paginator').bootstrapPaginator({
	                currentPage: data.data.currentPage,
	                totalPages:data.data.totalPages
	            });
	        }
	    }
    }
    function getAccpetAddr(data){
        if(data == null || data.length == 0){
            return "-";
        }
        var ret="";
        for(var i= 0,len=data.length;i<len;i++){
        	var port = "8001";
        	if(data[i].type == "jetty")
        		port = "8080";
            ret = ret
            +"<span class=\"text-success\">"+data[i].ipAddr+"</span>"
            +"<span class=\"text-success\">:</span>"
            +"<span class=\"text-success\">"+port+"</span><br>"
        }
        return ret;
    }
      function gceDelete(id){
    	var url = "/gce/"+id;
            cn.DeleteData(url, function () {
                location.href="/list/gce";
            });
    }
});