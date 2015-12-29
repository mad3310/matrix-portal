/**
 * Created by yaokuo on 2014/12/14.
 * accountManager page
 */
define(function(require,exports,module){
    var $ = require('jquery');
    var common = require('../../common');
    var cn = new common();
    var dbUsernames = new Array();

    var selectNameDist = 0;

    var DataHandler = function(){
    };

    module.exports = DataHandler;

    DataHandler.prototype = {
        usernamExist: function () {
            for(var i= 0,len=dbUsernames.length;i<len;i++){
                if($("[name='username']").val()==dbUsernames[i]){
                    return false;
                }
            }
            return true;
        },
        DbUserListHandler : function(data){
            $(".data-tr").remove();
            var $tby = $('#tby');
            var array = data.data;
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
                    dbUsernames.push(array[i].username);
                    var td1 = $("<td>"
                        + array[i].username
                        +"</td>");
                    var td2 = $("<td>" + cn.TranslateStatus(array[i].status) +"</td>");
                    var td3 = $("<td class=\"hide\">"+ array[i].readWriterRate + "</td>");
                    var td4 = $("<td class='hidden-xs'><span>"+array[i].maxConcurrency+"</span></td>");
                    var td5 = $("<td class='hidden-xs' style=\"word-break:break-all\"><span>"+cn.FilterNull(array[i].descn)
                        + "</span>"
                        + "<a class=\"mc-hide btn btn-default btn-xs glyphicon glyphicon-pencil\"></a>"
                        + "</td>");
                    var td6 = $("<td class=\"text-right\"> <div class='hidden-xs'>"
                        + "<a class=\"dbuser-list-ip-privilege\" href=\"javascript:void(0);\">ip访问权限</a><span class=\"text-explode\">"
                        + "|</span><a class=\"dbuser-list-reset-password\"  href=\"javascript:void(0);\">重置密码</a><span class=\"text-explode\">"
                        + "|</span><a class=\"dbuser-list-modify-privilege\"  href=\"javascript:void(0);\">修改权限</a><span class=\"text-explode\">"
                        + "|</span><a class=\"dbuser-list-delete\"  href=\"javascript:void(0);\">删除</a> </div>"
                        +"<div class='hidden-sm hidden-md hidden-lg'><a class=\"dbuser-list-ip-privilege\" href=\"javascript:void(0);\"><span class='glyphicon glyphicon-cog'></span></a>&nbsp;"
                        +"<a class=\"dbuser-list-reset-password\"  href=\"javascript:void(0);\"><span class='glyphicon glyphicon-repeat text-warning'></span></a>&nbsp;"
                        +"<a class=\"dbuser-list-modify-privilege\"  href=\"javascript:void(0);\"><span class='glyphicon glyphicon-edit text-success'></span></a>&nbsp;"
                        +"<a class=\"dbuser-list-delete\"  href=\"javascript:void(0);\"><span class='glyphicon glyphicon-trash text-danger'></span></a></div></td>");
                    var tr = $("<tr class='data-tr'></tr>");
                    tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6);
                    tr.appendTo($tby);
                }
            }
            /*获取行信息*/
            function getLineData(obj){
                var line = {
                    "username":$(obj).closest("tr").find("td:first").html(),
                    "readWriterRate":$(obj).closest("tr").find("td:eq(2)").html(),
                    "maxConcurrency":$(obj).closest("tr").find("td:eq(3) span").html(),
                    "descn":$(obj).closest("tr").find("td:eq(4) span").html()
                }
                return line;
            }

            var  dbUser = new DataHandler();
            /*初始化修改用户权限按钮*/
            $(".dbuser-list-modify-privilege").click(function(){
                $("#newAccountTab").addClass("mc-hide");
                $("#accountList").addClass("mc-hide");
                $("#modifyAccountTab").removeClass("mc-hide");

                var lineData = getLineData(this);
                cn.GetData("/dbIp/"+$("#dbId").val()+"/"+lineData.username,dbUser.ModifyDbUserIpHandler);

                $("#modifyFormDbUsername").html(lineData.username);
                $("#modifydbUserMaxConcurrency").val(lineData.maxConcurrency);
                $("#modifydbUserReadWriterRate").val(lineData.readWriterRate);
                $("#modifyFormDbDesc").html(lineData.descn);
            })

            /*初始化查看用户权限按钮*/
            $(".dbuser-list-ip-privilege").click(function () {
                var lineData = getLineData(this);
                cn.GetData("/dbIp/"+$("#dbId").val()+"/"+lineData.username,dbUser.GetDbUserPrivilege);
                $("#showDbuserIpPrivilegeTitle").html(lineData.username);
            })
            /*初始化删除按钮*/
            $(".dbuser-list-delete").click(function () {
                var lineData = getLineData(this);
                var title = "确认";
                var text = "您确定要删除("+lineData.username+")账户";
                var args = lineData.username;
                cn.DialogBoxInit(title,text,dbUser.DeleteDbUser,args);
            })
            /*初始化重置密码*/
            $(".dbuser-list-reset-password").click(function () {
                var lineData = getLineData(this);
                $("#reset-password-box-title").html("重置账户("+lineData.username+")密码");
                $("#reset-password-box").modal({
                    backdrop:false,
                    show:true
                });
                $("#reset-password-username").val(lineData.username);
            })
            /*初始化编辑描述*/
            $(".glyphicon-pencil").click(function(){
                cn.EditBoxInit(this);
                var lineData = getLineData(this);
                $("#modify-descn-username").val(lineData.username);
                $("#modifyDescn").val(lineData.descn);
            });
            /* 编辑描述对话框初始化完毕*/

            /*初始化tr hover方法*/
            $("#tby tr").hover(function(){
                $(this).find(".glyphicon-pencil").show();
            },function(){
                $(this).find(".glyphicon-pencil").hide();
            });
            /*tr hover方法初始化完毕*/
        },
        DbUserIpHandler: function(data){
            InitDoubleFrame(".multi-select",data.data);
            if(data.data.length == 0){
                var title = "注意";
                var text = "您当前IP名单为空,点击确认去维护IP名单.";
                cn.DialogBoxInit(title,text, function () {
                    cn.RefreshIfame("/detail/security/"+$("#dbId").val());
                });
            }
        },
        ModifyDbUserIpHandler: function(data){
            InitDoubleFrame(".modify-multi-select",data.data);
        },
        DeleteDbUser:function(data){
            var username = data;
            var url = "/dbUser/"+$("#dbId").val()+"/"+username;
            cn.DeleteData(url, function () {
                /*刷新本身ifame*/
                var $iframe = $("body",parent.document).find("iframe");
                $iframe.attr("src",$iframe.attr("src"));
            });
        },
        GetDbUserPrivilege: function(data){
            var $tby = $("#ip-privilege-tby");
            $tby.find("tr").remove();

            $('#showDbuserIpPrivilege').modal({
                backdrop:false,
                show:true
            });

            var array = data.data;
            for(var i= 0,len=array.length;i<len;i++){
                if(array[i].used == 1){
                    var td1 =  $("<td>"
                        + array[i].addr
                        + "</td>");
                    var td2 =  $("<td>"
                        + cn.TranslateDbUserType(array[i].type)
                        + "</td>");
                    var tr =$("<tr></tr>");
                    tr.append(td1).append(td2);
                    tr.appendTo($tby);
                }
            }
        },
        GetCreateDbUserData: function(){
            var dbId = $("#dbId").val();
            var username = $("[name = 'username']").val();
            var readWriterRate = "2:1";
            var maxConcurrency = $("[name = 'maxConcurrency']").val();
            var newPwd1 = $("[name = 'newPwd1']").val();
            var accountDesc = $("[name = 'descn']").val();

            var ips = "";
            var types = "";
            $(".multi-select .select-list-right").find("li").each(function () {
                var addr = $(this).find("p:first").html();
                var type = $(this).find(":checked").val();
                ips += addr + ",";
                types += type + ",";
            })

            var data = {
                "dbId" : dbId,
                "username":username,
                "readWriterRate":readWriterRate,
                "maxConcurrency":maxConcurrency,
                "password":newPwd1,
                "descn":accountDesc,
                "ips":ips,
                "types":types
            }
            return data;
        },
        GetModifyDbUserData: function(){
            var dbId = $("#dbId").val();
            var username =$("#modifyFormDbUsername").html();
            var readWriterRate = $("#modifydbUserReadWriterRate").val();
            var maxConcurrency = $("#modifydbUserMaxConcurrency").val();

            var ips = "";
            var types = "";
            $(".modify-multi-select .select-list-right").find("li").each(function () {
                var addr = $(this).find("p:first").html();
                var type = $(this).find(":checked").val();
                ips += addr + ",";
                types += type + ",";
            })

            var data = {
                "dbId" : dbId,
                "username":username,
                "maxConcurrency": maxConcurrency,
                "readWriterRate": readWriterRate,
                "ips":ips,
                "types":types
            }
            return data;
        }
    }

    /*双选框初始化 --begin*/
    function InitDoubleFrame(id,data){
        var $d = $(id);		                    //双选框div
        $d.find("li").remove();
        $sl = $d.find(".select-list-left");		//左选框ul
        $sr = $d.find(".select-list-right");	//右选框ul
        for(var i= 0,len=data.length;i<len;i++){
            if(data[i].used == 0){
                AddToLeftFrame($sl,data[i]);
            }else{
                AddToRightFrame($sr,data[i]);
            }
        };
        /*初始化选中操作*/
        SelectToggle($d);
        /*双击移动操作*/
        DoubleClickToggle($d);

        /*添加选中内容*/
        $d.find(".btn_db_add").unbind('click').click(function(){
            var selected= [];
            $sl.find(".active").each(function (i,val) {
                selected.push({addr:$(val).html()});
            });
            $sl.find(".active").remove();
            for(var i= 0,len=selected.length;i<len;i++){
                AddToRightFrame($sr,selected[i]);
            }
            SelectToggle($d);
            DoubleClickToggle($d);
        });
        $d.find(".btn_db_remove").unbind('click').click(function(){
            var selected= [];
            $sr.find(".active").each(function (i,val) {
                selected.push({addr:$(val).find("p:first").html()});
            });
            $sr.find(".active").remove();
            for(var i= 0,len=selected.length;i<len;i++){
                AddToLeftFrame($sl,selected[i]);
            }
            SelectToggle($d);
            DoubleClickToggle($d);
        });
        /*全选按钮初始化*/
        $d.find(".select-all-rw").unbind('click').click(function(){
            var $sr = $(this).closest(".mcluster-select");
            if($(this).html() == "全部设管理"){
                $(this).html("全部设读写");
                $sr.find(".mgr").prev().click();
            }else if ($(this).html() == "全部设读写"){
                $(this).html("全部设只读");
                $sr.find(".rw").prev().click();
            }else{
                $(this).html("全部设管理");
                $sr.find(".ro").prev().click();
            }
        });

    };

    function AddToLeftFrame($sl,data){
        var $li = $("<li class=\"select-item\">"+data.addr+"</li>");
        $li.appendTo($sl);
    };
    function AddToRightFrame($sr,data){
        var ipName = selectNameDist++;
        var $li = $("<li class=\"select-item\"> "
            + "<p class=\"pull-left\">"+data.addr+"</p>"
            + "<p class=\"pull-right\" style=\"margin-right:5px\">"
            + "<span>"
            + "<input type=\"radio\" name=\""+ipName+"\" value=\"1\" >"
            + "<label class=\"mgr\">管理</label>"
            + "</span>"
            + "<span>"
            + "<input type=\"radio\" name=\""+ipName+"\" value=\"2\">"
            + "<label class=\"ro\">只读</label>"
            + "</span>"
            + "<span>"
            + "<input type=\"radio\" name=\""+ipName+"\" value=\"3\">"
            + "<label class=\"rw\">读写</label>"
            + "</span>"
            + "</p>"
            + "</li>");
        if(data.type == 1){
            $li.find("input:first").attr("checked",true);
        }else if(data.type == 2){
            $li.find("input:eq(1)").attr("checked",true);
        }else{
            $li.find("input:eq(2)").attr("checked",true);
        }
        $li.appendTo($sr);
    };
    function SelectToggle($d){
        $d.find("li").each(function() {
            if ($(this).find("p").length == 0) {
                $(this).unbind("click");
                $(this).click(function () {
                    $(this).toggleClass("active");
                });
            } else {
                $(this).find("p:first").unbind("click");
                $(this).find("p:first").click(function () {
                    $(this).closest("li").toggleClass("active");
                });
            }
        })
    };
    function DoubleClickToggle($d){
        $d.find("li").unbind("dblclick");
        $sl = $d.find(".select-list-left");		    //左选框ul
        $sr = $d.find(".select-list-right");	    //右选框ul
        $sl.find("li").each(function(){
            $(this).dblclick(function(){
                var selected= [];
                selected.push({addr:$(this).html()});
                $(this).remove();
                AddToRightFrame($sr,selected[0]);
                SelectToggle($d);                //重新初始化单机事件
                DoubleClickToggle($d);           //重新初始化双击事件
            })
        });
        $sr.find("li").each(function(){
            $(this).dblclick(function(){
                var selected= [];
                selected.push({addr:$(this).find("p:first").html()});
                $(this).remove();
                AddToLeftFrame($sl,selected[0]);
                SelectToggle($d);               //重新初始化单机事件
                DoubleClickToggle($d);          //重新初始化双击事件
            })
        });
    }
    /*双选框初始化 --end*/
});

