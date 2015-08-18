/**
 * Created by yaokuo on 2014/12/14.
 */
define(function(require,exports,module){
    var $ = require('jquery');
    var common = require('../../common');
    var cn = new common();
    var innerServerIds = [];
    
    var DataHandler = function(){
    };

    module.exports = DataHandler;

    DataHandler.prototype = {
        ServerHandler : function(data){
            $(".data-tr").remove();
            var $tby = $('#tby');
            var array = data.data.data;
            /*内外网服务器不能同时添加*/
            if(array != null && array.length != 0){
            	if(array[0].type == "CUSTOM"){
            		$("#add-custom-server").removeClass("hidden");
            	}else{
            		/*$("#inner-server-list-tab").removeClass("hidden");
            		$("#add-inner-server").removeClass("hidden");*/
            	}
            }else{
            	$("#inner-server-list-tab").removeClass("hidden");
            	$("#add-inner-server").removeClass("hidden");
            	$("#add-custom-server").removeClass("hidden");
            }
            
            for(var i= 0, len= array.length;i<len;i++){
                var td1 = $("<td>"
                + array[i].serverName
                + "</td>");
                var td2=$("<td class='hidden-xs'>-</td>");
                if(array[i].gceContainers != undefined && array[i].gceContainers != null){
                	td2 = $("<td class=\"padding-left-32 hidden-xs\">"
                            + "<span>"+getAccpetAddr(array[i].gceContainers)+"</span>"
                            +"</td>");
                }else{
                	td2 = $("<td  class='hidden-xs'>"
                 	+"<span class=\"text-success\">"+array[i].serverIp+"</span>"
                	+"<span class=\"text-success\">:</span>"
                	+"<span class=\"text-success\">"+array[i].port+"</span><br>"
               		+ "</td>");
                }
                var td10="<td  class='hidden-xs'>-</td>"
                if(array[i].slbConfig != undefined && array[i].slbConfig != null){
                    td10="<td  class='hidden-xs'>"
                    +"<span class=\"text-success\">"+array[i].slbConfig.agentType+"</span>"
                    +"<span class=\"text-success\">:</span>"
                    +"<span class=\"text-success\">"+array[i].slbConfig.frontPort+"</span><br>"
                    +"</td>";
                }
                var status ="-";
                if(array[i].status==5) status = "未生效";
                if(array[i].status==6) status = "生效";
                var td4 = $("<td>"
                + "<span class=\"text-success\">"+status+"</span>"
                + "</td>");
                var td5 = $("<td  class='hidden-xs'>"
                + "<span>经典网络</span>"
                + "</td>");
                var td6 = $("<td  class='hidden-xs'>"
                + "<span>包年</span>"
                + "</td>");
                var td7 = $("<td  class='hidden-xs'>"
                + "<span class=\"text-success\">正常</span>"
                + "</td>");
                var td8 = $("<td  class='hidden-xs'>"
                + "<span>100</span>"
                + "</td>");
                var td9 = $("<td class=\"text-right hidden-xs\">" 
                +"<a href=\"javascript:void(0)\"><span class=\"backend-server-delete text-explode\" slb-config-id=\""+array[i].id+"\">移除</span></a>" 
                +"</td>"
                +"<td class='text-right hidden-sm hidden-md hidden-lg'><a href=\"javascript:void(0)\"><span class=\"backend-server-delete text-explode text-danger glyphicon glyphicon-trash\" slb-config-id=\""+array[i].id+"\"></span></a></td>"
                );
                var tr = $("<tr class='data-tr'></tr>");

                tr.append(td1).append(td2).append(td10).append(td4).append(td5).append(td6).append(td7).append(td8).append(td9);
                tr.appendTo($tby);
                if(array[i].gceId != null && innerServerIds.every(function(gceId){
                	return array[i].gceId != gceId;
                })){
                	innerServerIds.push(array[i].gceId);
                }
            }
              /*初始化移除按钮*/
            $(".backend-server-delete").click(function () {    
            	var id =$(this).attr("slb-config-id");
            	var name = $(this).closest("tr").find("td:first").html();
                var title = "确认";
                var text = "您确定要删除("+name+")服务器";
                cn.DialogBoxInit(title,text,backendServerDel,id);
            })
        },
         InnerServerHandler : function(data){
            $(".inner-server-data-tr").remove();
            var tby = $('#server-tby');
            var array = data.data.data;
            for(var i= 0, len= array.length;i<len;i++){
            	if(innerServerIds.every(function(gceId){
            		return array[i].id != gceId;$("<tr class='data-tr'></tr>")
            	})){
            		 var td1 = $("<td class=\"name\">"
	                + array[i].gceName
	                + "</td>");
	                var td3 = $("<td class=\"padding-left-32 hidden-xs\">"
	                		 + "<span>"+getAccpetAddr(array[i].gceServerProxy?array[i].gceServerProxy.gceContainers:array[i].gceContainers)+"</span>"
                            +"</td>");
	                var td4 = $("<td class='hidden-xs'>"
	                + "<span>经典网路</span>"
	                + "</td>");
	                var td5 = $("<td class='hidden-xs'>"
	                + "<span>包年</span>"
	                + "</td>");
	                var td6 = $("<td>"
	                + "<span>"+cn.TranslateStatus(array[i].status)+"</span>"
	                + "</td>");
	                if(array[i].status == 6){
		                var td7 = $("<td class=\"text-right hidden-xs\">" 
		                +"<a href=\"javascript:void(0)\"><span class=\"backend-server-add text-explode\" gce-id=\""+array[i].id+"\">添加</span></a>" 
		                +"</td><td class='text-right hidden-sm hidden-md hidden-lg'>"
                        +"<a href=\"javascript:void(0)\"><span class=\"backend-server-add text-explode glyphicon glyphicon-plus\" gce-id=\""+array[i].id+"\"></span></a></td>" 
                        );
	                }else{
	                	 var td7 = $("<td class=\"text-right hidden-xs\">" 
		                +"<span class=\"text-explode\" inner-server-id=\""+array[i].id+"\">添加</span>" 
		                +"</td><td class='text-right hidden-sm hidden-md hidden-lg'>"
                        +"<span class=\"text-explode glyphicon glyphicon-plus\" inner-server-id=\""+array[i].id+"\"></span></td>" 
                        );
	                }
	                var tr = $("<tr class='inner-server-data-tr'></tr>");
	                tr.append(td1).append(td3).append(td4).append(td5).append(td6).append(td7);
					tr.appendTo(tby);
            	}else{
            		continue;
            	}
            }
             
            $(".backend-server-add").click(function () {    
            	var data={
            		serverName:$(this).closest("tr").find(".name").html(),
            		slbId: $("#slbId").val(),
            		configId : $("[name = frontPort]").val(),
            		gceId:$(this).attr("gce-id"),
            		type:"WEB"
            	}
                var title = "确认";
                var text = "您确定要添加("+data.serverName+")服务器";
                cn.DialogBoxInit(title,text,innerServerAdd,data);
            })
        },
        ConfigHandler : function(data){
            var select = $("[name = frontPort]")
            var array = data.data.data;
            for(var i= 0,len=array.length;i<len;i++) {
                var option = $("<option value=\""+array[i].id+"\">"+addPort(array[i])+"</option>");
                option.appendTo(select);
            }
        }
    }
    function addPort(data){
        if(data ==null || data.length == 0){
            return "-";
        }
        return "<span>"+data.agentType+"</span>"
		        +"<span>:</span>"
		        +"<span>"+data.frontPort+"</span><br>";
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
    function backendServerDel(id){
            var url = "/slbBackend/"+id;
            cn.DeleteData(url, function () {
                /*刷新本身ifame*/
               cn.RefreshIfame();
            });
    }
    function innerServerAdd(data){
        var url = "/slbBackend";
        cn.PostData(url, data, function () {
            /*刷新本身ifame*/
            cn.RefreshIfame();
            });
    }
});