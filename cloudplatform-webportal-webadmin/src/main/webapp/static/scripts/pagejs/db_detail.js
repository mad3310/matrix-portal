var selectNameDist = 0;	
var maxConcurrency = 2000;
var dbUsernames = new Array();
var dbUser={
    DbUserIpHandler: function(data){
        // InitDoubleFrame(".multi-select",data.data);
        if(data.data.length == 0){
            var text = "您当前IP名单为空,点击确认去维护IP名单.";
            warn(text);
            return;
        }else{
        	InitDoubleFrame(".multi-select",data.data);
        	$("#accountList").addClass("hide");
	        $("#modifyAccountTab").addClass("hide");
	        $("#newAccountTab").removeClass("hide");
        }
    },
    usernamExist: function () {
        for(var i= 0,len=dbUsernames.length;i<len;i++){
            if($("[name='username']").val()==dbUsernames[i]){
                return false;
            }
        }
        return true;
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
        //var password = $("#modifyFormNewPwd1").val();
        var descn = $("#modifyFormDbDesc").html();

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
            //"password":password,
            "ips":ips,
            "types":types,
            "descn":descn
        }
        return data;
    },
    TranslateDbUserType: function(type){
        if(type == 1){
            return "管理";
        }else if(type == 2){
            return "只读"
        }else{
            return "读写"
        }
    }
}
$(function(){
	//隐藏搜索框
	$('#nav-search').addClass("hidden");
	
	pageinit();
	var MAX_OPTIONS = 10;
    $('#db_apply_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	userName: {
                validators: {
                    notEmpty: {
                        message: 'The question required and cannot be empty'
                    },
			          stringLength: {
		              max: 40,
		              message: '用户名太长了!'
		          }
                }
            },
            password: {
             validators: {
             notEmpty: {
             message: 'The question required and cannot be empty'
             },
             stringLength: {
             max: 20,
             message: '密码太长了!'
             }
             }
             },
            readWriterRate: {
                validMessage: 'The business_description looks great',
                validators: {
                    notEmpty: {
                        message: '读写比例不能为空!'
                    },
                    regexp: {
		                  regexp: /^((\d|\d\d|\d\d\d)(\:(\d|\d\d|\d\d\d))){1}$/,
		                  message: '请按提示输入'
		              }
                }
            },
            maxConcurrency: {
                validMessage: 'The business_description looks great',
                validators: {
                    notEmpty: {
                        message: '最大并发量不能为空!'
                    },integer: {
                        message: '请输入数字'
                    }
                }
            },
            'Ip[]': {
                validators: {
                    notEmpty: {
                        message: '地址不能为空'
                    },
		              regexp: {
	                  regexp: /^(\d|1\d\d|2[0-4]\d|25[0-5])((\.(\d|1\d\d|2[0-4]\d|25[0-5]))|(\.\%)){3}$/,
	                  message: '请按提示格式输入'
	              }
                }
            }
        }
    }).on('click', '.addButton', function() {
        var $template = $('#optionTemplate'),
            $clone    = $template
                            .clone()
                            .removeClass('hide')
                            .removeAttr('id')
                            .insertBefore($template),
            $option   = $clone.find('[name="Ip[]"]');
        $('#db_apply_form').bootstrapValidator('addField', $option);
    }).on('click', '.removeButton', function() {
        var $row    = $(this).parents('.form-group'),
            $option = $row.find('[name="Ip[]"]');
        $row.remove();
        $('#db_apply_form').bootstrapValidator('removeField', $option);
    }).on('added.field.bv', function(e, data) {
        if (data.field === 'Ip[]') {
            if ($('#db_apply_form').find(':visible[name="Ip[]"]').length >= MAX_OPTIONS) {
                $('#db_apply_form').find('.addButton').attr('disabled', 'disabled');
            }
        }
    }).on('removed.field.bv', function(e, data) {
       if (data.field === 'Ip[]') {
            if ($('#db_apply_form').find(':visible[name="Ip[]"]').length < MAX_OPTIONS) {
                $('#db_apply_form').find('.addButton').removeAttr('disabled');
            }
        }
    });
    // 创建新用户按钮
    $(".toCreateAccount").unbind('click').click(function () {           //切换到创建账户
        var dbId=$('#dbId').val();
        $.get("/dbIp/"+dbId+"/null",function(data) {
        	dbUser.DbUserIpHandler(data)
        });
    });
    $(".toAccountList").unbind('click').click(function () {             //切换到账户列表
        $("#newAccountTab").addClass("hide");
        $("#modifyAccountTab").addClass("hide");
        $("#accountList").removeClass("hide");
        // var hash='';
        // if(!location.hash){
        //     hash=location.href+"#db-detail-user-mgr";
        // }
        // location.href=hash;
        // window.location.reload();
    });
    // 新用户创建及验证
    $('#db_user_create_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                validators: {
                    notEmpty: {
                        message: '用户名不能为空!'
                    },
                    stringLength: {
                        max: 16,
                        message: '用户名过长!'
                    }, regexp: {
                        regexp: /^[a-zA-Z_]+[a-zA-Z_0-9]*$/,
                        message: "请输入字母数字或'_',用户名不能以数字开头."
                    }
                    ,callback:{
                        message:"用户名已存在.",
                        callback: dbUser.usernamExist
                    }
                }
            },
            maxConcurrency: {
                validMessage: '请按提示输入',
                validators: {
                    notEmpty: {
                        message: '最大并发量不能为空!'
                    },integer: {
                        message: '请输入数字'
                    },between:{
                        min:1,
                        max:maxConcurrency,
                        message:'最大并发量1-'+maxConcurrency
                    }
                }
            },
            newPwd1: {
                validators: {
                    notEmpty: {
                        message:'密码不能为空'
                    },different: {
                        field: 'username',
                        message: '密码不能与账户名相同'
                    },stringLength: {
                        min:6,
                        max: 32,
                        message: '密码长度为6-32之间'
                    },regexp: {
                        regexp: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z@#$%^&*!~_-]{6,32}$/,
                        message: "由字母、数字或特殊字符如：@#$%^&*!~ 组成,要求6-32位，必须要包含数字，大小写字母"
                    }
                }
            },
            newPwd2: {
                validators: {
                    notEmpty: {
                        message:'密码不能为空'
                    },identical: {
                        field: 'newPwd1',
                        message: '两次输入密码不同'
                    }
                }
            },
            descn: {
                validators: {
                    stringLength: {
                        max: 100,
                        message: '输入长度不超过100个字符!'
                    }
                }
            }
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();
        var createUserData = dbUser.GetCreateDbUserData();
        if(createUserData.ips != '' && createUserData.type != ''){
            var url = "/dbUser";
            $("#submitCreateUserForm").addClass('disabled').text("提交中...");
			$.post(url,createUserData, function(data) {
				if(error(data)){
					$("#submitCreateUserForm").removeClass('disabled').removeAttr('disabled').text("提交");
					return;
				}else{
					var hash='';
                    if(!location.hash){
                        hash=location.href+"#db-detail-user-mgr";
                    }
                    location.href=hash;
                    window.location.reload();
				}
			});
        }else{
            var text = "您创建的数据库账户没有添加IP名单,请添加后创建!";
           	warn(text);
           	$("#submitCreateUserForm").removeAttr("disabled");
        }
    }).on('keyup', '[name="newPwd1"]', function () {
        if($("[name = 'newPwd2']").val() != ''){
            $('#db_user_create_form').bootstrapValidator('revalidateField', 'newPwd2');
        }
    });
    //用户权限修改验证
    $('#db_user_modify_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            modifydbUserMaxConcurrency: {
                validMessage: '请按提示输入',
                validators: {
                    notEmpty: {
                        message: '最大并发量不能为空!'
                    },integer: {
                        message: '请输入数字'
                    },between:{
                        min:1,
                        max:maxConcurrency,
                        message:'最大并发量1-'+maxConcurrency
                    }
                }
            },
           /* modifyFormNewPwd1: {
                validators: {
                    notEmpty: {
                        message:'密码不能为空'
                    },regexp: {
                        regexp: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z@#$%^&*!~_-]{6,32}$/,
                        message: "由字母、数字或特殊字符如：@#$%^&*!~ 组成,要求6-32位，必须要包含数字，大小写字母"
                    }
            		/!*,regexp: {
                        regexp: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z_-]{6,32}$/,
                        message: "由字母、数字、中划线或下划线组成,要求6-32位，必须要包含数字，大小写字母"
                    }*!/
                }
            },
            modifyFormNewPwd2: {
                validators: {
                    notEmpty: {
                        message:'密码不能为空'
                    },
                    identical: {
                        field: 'modifyFormNewPwd1',
                        message: '两次输入密码不同'
                    }
                }
            }*/
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();
        var modifyUserData = dbUser.GetModifyDbUserData();
        if(modifyUserData.ips != '' && modifyUserData.type != ''){
            var url = "/dbUser/authority/"+$("#modifyFormDbUsername").html();
            $("#submitModifyUserForm").addClass("disabled").text("提交中...");
    		$.post(url,modifyUserData,function(data) {
                $("#submitModifyUserForm").removeClass("disabled").text("提交");
    			if(error(data)){return;
    			}else{
                    var hash='';
                    if(!location.hash){
                        hash=location.href+"#db-detail-user-mgr";
                    }
                    location.href=hash;
                    window.location.reload();
    			}
    		});
        }else{
            var text = "您修改的数据库账户没有添加IP名单,请添加后修改!";
            warn(text);
            $("#submitModifyUserForm").removeAttr("disabled");
        }
    }).on('keyup', '[name="modifyFormNewPwd1"]', function () {
        if($("[name = 'modifyFormNewPwd2']").val() != ''){
            $('#db_user_modify_form').bootstrapValidator('revalidateField', 'modifyFormNewPwd2');
        }
    });
});

function checkboxControl(){
	$('th input:checkbox').click(function(){
		var that = this;
		$(this).closest('table').find('tr > td:first-child input:checkbox')
		.each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});
}
function queryDbUser(){
	getLoading();
	$.ajax({ 
		cache:false,
		type : "get",
		url : "/dbUser/"+$("#dbId").val(),
		dataType : "json", /*这句可用可不用，没有影响*/
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var array = data.data;
			var tby = $("#tby");
			$(".data-tr").remove();
			for (var i = 0, len = array.length; i < len; i++) {
				dbUsernames.push(array[i].username);
				var td1 = $("<td class=\"center\">"
						    + "<label class=\"position-relative\">"
						    + "<input type=\"checkbox\" class=\"ace\"/>"
						    + "<span class=\"lbl\"></span>"
						    + "</label>"
					        + "</td>");
				var	td2 = $("<td>"
							+ array[i].username
							+ "</td>");
				// var td3;
				// if(array[i].type == 1){
				// 	var td3 = $("<td>"
				// 			    + "管理员"
				// 			    + "</td>");
				// }else if(array[i].type == 2){
				// 	var td3 = $("<td>"
				// 		    + "只读用户"
				// 		    + "</td>");
				// }else{
				// 	var td3 = $("<td>"
				// 			    + "读写用户"
				// 			    + "</td>");
				// }
				// var td4 = $("<td>"
				// 			+array[i].acceptIp
				// 			+ "</td>");
				var td5 = $("<td class='hidden-xs'>"
							+array[i].maxConcurrency
							+ "</td>");
				var td6 = $("<td>"
							+translateStatus(array[i].status)
							+ "</td>");
				var td7; 
				if(array[i].descn){
					td7=$("<td class='hidden-xs'><span>"
						+array[i].descn
						+"</span></td>");
				}else{td7=$("<td class='hidden-xs'><span>-"
						+"</span></td>")}
				var td8=$("<td class='hidden-xs'>" 
						+ "<a class=\"dbuser-list-ip-privilege text-primary\" href='javascript:void(0)' data-db-id="+array[i].dbId+">ip访问权限</a><span class=\"text-explode\">"
						+ "|</span><a class=\"dbuser-list-reset-password text-success\"  href=\"javascript:void(0);\" data-db-id="+array[i].dbId+">重置密码</a><span class=\"text-explode\">"
	                    + "|</span><a class=\"dbuser-list-modify-privilege text-warning\" href=\"javascript:void(0);\" data-db-id="+array[i].dbId+">修改权限</a><span class=\"text-explode\">"
	                    + "|</span><a class=\"dbuser-list-delete text-danger\"  href=\"javascript:void(0);\" data-db-id="+array[i].dbId+">删除</a></div>"
						+"</td>"
						+"<td class='hidden-lg hidden-md hidden-sm'>"
						+ "<a class=\"dbuser-list-ip-privilege text-primary\" href=\"javascript:void(0);\" data-db-id="+array[i].dbId+"><span class='text-primary'><i class='fa fa-cogs'></i></span></a><span class=\"text-explode\">"
						+ "|</span><a class=\"dbuser-list-reset-password text-success\"  href=\"javascript:void(0);\" data-db-id="+array[i].dbId+"><i class='fa fa-key'></i></a><span class=\"text-explode\">"
	                    + "|</span><a class=\"dbuser-list-modify-privilege text-warning\"  href=\"javascript:void(0);\" data-db-id="+array[i].dbId+"><i class='fa fa-edit'></i></a><span class=\"text-explode\">"
	                    + "|</span><a class=\"dbuser-list-delete text-danger\"  href=\"javascript:void(0);\" data-db-id="+array[i].dbId+"><i class='fa fa-trash-o'></i></a> </div>"
						+"</td>");	
				if(array[i].status == 0 ||array[i].status == 5||array[i].status == 13){
					var tr = $("<tr class=\"warning data-tr\"></tr>");
				}else if(array[i].status == 3 ||array[i].status == 4||array[i].status == 14){
					var tr = $("<tr class=\"danger data-tr\"></tr>");
				}else{
					var tr = $("<tr class='data-tr'></tr>");
				}
				
				// tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7);
				tr.append(td1).append(td2).append(td5).append(td6).append(td7).append(td8);
				tr.appendTo(tby);
			}//循环json中的数据

			/*获取行信息*/
            function getLineData(obj){
                var line = {
                    "username":$(obj).closest("tr").find("td:eq(1)").html(),
                    // "readWriterRate":$(obj).closest("tr").find("td:eq(2)").html(),
                    "maxConcurrency":$(obj).closest("tr").find("td:eq(2)").html(),
                    "descn":$(obj).closest("tr").find("td:eq(5) span").html()
                }
                return line;
            } 
			var dbUserFuc={
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
			                + dbUser.TranslateDbUserType(array[i].type)
			                + "</td>");
			                var tr =$("<tr></tr>");
			                tr.append(td1).append(td2);
			                tr.appendTo($tby);
			            }
			        }
			    },
			    DeleteDbUser:function(name,id){
			        var username = name;
			        var url= "/dbUser/"+id+"/"+username;
			        $.ajax({
			        	url:url,
			        	type: 'delete',
			        	dataType: 'json',
			        	success:function(data){
			        		if(error(data)){return;}else{success('删除操作执行成功！')}
			        		queryDbUser();
			        	}
			        });
			    },
			     ModifyDbUserIpHandler: function(data){
			        InitDoubleFrame(".modify-multi-select",data.data);
			    }
			}
			/*初始化查看用户权限按钮*/
			$(".dbuser-list-ip-privilege").unbind('click').click(function () {
			    var lineData = getLineData(this);
			    var dbId=$(this).attr('data-db-id');
			    $.ajax({
			    	url: "/dbIp/"+dbId+"/"+lineData.username,
			    	type: 'get',
			    	success:function(data){
			    		if(error(data)){warn(data.msgs);return;}
			    		dbUserFuc.GetDbUserPrivilege(data);
			    	}
			    });
			    $("#showDbuserIpPrivilegeTitle").html(lineData.username);
			});
			/*初始化重置密码*/
            $(".dbuser-list-reset-password").unbind('click').click(function () {
                var lineData = getLineData(this);
                $("#reset-password-box-title").html("重置账户("+lineData.username+")密码");
                $("#reset-password-box").modal({
                    backdrop:false,
                    show:true
                });
                $("#reset-password-username").val(lineData.username);
                var dbId=$(this).attr('data-db-id');
                $("#reset-password-username").attr('data-db-id', dbId);
            });
            /*初始化删除按钮*/
            $(".dbuser-list-delete").unbind('click').click(function(event) {
            	event.preventDefault();    
                var lineData = getLineData(this);
                var title = "确认";
                var text = "您确定要删除("+lineData.username+")账户";
                var name = lineData.username;
                var dbId=$(this).attr('data-db-id');
                DialogBoxInit(title,text,dbUserFuc.DeleteDbUser,name,dbId);
            });
            // /*初始化修改用户权限按钮*/
            $(".dbuser-list-modify-privilege").unbind('click').click(function(){
                $("#newAccountTab").addClass("hide");
                $("#accountList").addClass("hide");
                $("#modifyAccountTab").removeClass("hide");
                var dbId=$(this).attr('data-db-id');
                $('#dbId').val(dbId);
                var lineData = getLineData(this);
                $.get("/dbIp/"+dbId+"/"+lineData.username, function(data) {
                	dbUserFuc.ModifyDbUserIpHandler(data)
                });
                $("#modifyFormDbUsername").html(lineData.username);
                $("#modifydbUserMaxConcurrency").val(lineData.maxConcurrency);
                $("#modifydbUserReadWriterRate").val(lineData.readWriterRate);
                $("#modifyFormDbDesc").html(lineData.descn);
            });
		},
		error : function(XMLHttpRequest,textStatus, errorThrown) {
			$.gritter.add({
				title: '警告',
				text: errorThrown,
				sticky: false,
				time: '5',
				class_name: 'gritter-warning'
			});
	
			return false;
		}
	});
}
function queryDbInfo(){
	getLoading();
	$.ajax({ 
		cache:false,
		type : "get",
		url : "/db/"+$("#dbId").val(),
		dataType : "json", 
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var dbInfo = data.data;
			$("#headerDbName").append(dbInfo.dbName);
			$("#db_detail_table_dbname").text(dbInfo.dbName);
			$("#db_detail_table_belongUser").text(dbInfo.user.userName);
			$("#db_detail_table_createtime").text(date('Y-m-d H:i:s',dbInfo.createTime));
			for(var i=0,len = dbInfo.containers.length; i < len; i++)
			{
				var td1 = $("<th>"
						+ dbInfo.containers[i].containerName
						+"</th>");
				var td2 =$("<td>"
						+ dbInfo.containers[i].ipAddr
						+"</td>");
				var tr = $("<tr></tr>");
				tr.append(td1).append(td2);
				$("#db_detail_table").append(tr);
			}
		},
		error : function(XMLHttpRequest,textStatus, errorThrown) {
			$.gritter.add({
				title: '警告',
				text: errorThrown,
				sticky: false,
				time: '5',
				class_name: 'gritter-warning'
			});
			return false;
		}
	});
}

function pageinit(){
	checkboxControl();
	queryDbUser();
	queryDbInfo();
	formValidate();
    var hash=window.location.hash;
    if(hash){
        $('#db-detail-tabs a[href="'+hash+'"]').tab('show')
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

}
function AddToLeftFrame($sl,data){
    var $li = $("<li class=\"select-item\">"+data.addr+"</li>");
    $li.appendTo($sl);
}
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
}
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
}
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
//修改密码表单验证
function formValidate() {
	$('#reset-password-form').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'reset-password': {
                validators: {
                    notEmpty: {
                        message:'密码不能为空'
                    },regexp: {
                        regexp: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z@#$%^&*!~_-]{6,32}$/,
                        message: "由字母、数字或特殊字符如：@#$%^&*!~ 组成,要求6-32位，必须要包含数字，大小写字母"
                    }
                }
            },
            'reset-password-repeat': {
                validators: {
                    notEmpty: {
                        message:'密码不能为空'
                    },
                    identical: {
                        field: 'reset-password',
                        message: '两次输入密码不同'
                    }
                }
            }
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();
        var postdata = {
            "username":$("#reset-password-username").val(),
            "password":$("[name = 'reset-password']").val(),
            "dbId":$("#reset-password-username").attr('data-db-id')
        }
        var url = "/dbUser/security/"+$("#reset-password-username").val();
    	$.post(url,postdata, function(data) {
    		if(error(data)){warn(data.msgs);return;
    		}else{
    			success('密码重置成功！');$('#reset-password-form')[0].reset();$('#reset-password-box').modal('hide');
    			queryDbUser();
    		}
    		
    	});
    }).on('keyup', '[name="reset-password"]', function () {
        if($("[name = 'reset-password-repeat']").val() != ''){
            $('#reset-password-form').bootstrapValidator('revalidateField', 'reset-password-repeat');
            $("#reset-password-box").modal({show:false});
        }
    });
}

