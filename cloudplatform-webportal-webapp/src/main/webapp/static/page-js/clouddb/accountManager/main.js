/**
 * Created by yaokuo on 2014/12/12.
 * accountManager page
 */
define(function(require){
    var common = require('../../common');
    var cn = new common();
    var $ = require("jquery");
    require("bootstrapValidator")($);
    var dataHandler = require('./dataHandler');
    var dbUser = new dataHandler();

    /*初始化工具提示*/
    cn.Tooltip();
    cn.Popover();
    /*modal提示框居中*/
    cn.center();

   

    /*页面按钮初始化 --start*/
    $(".toCreateAccount").click(function () {           //切换到创建账户
        $("#accountList").addClass("mc-hide");
        $("#modifyAccountTab").addClass("mc-hide");
        $("#newAccountTab").removeClass("mc-hide");
        asyncDbUserIpData();
    })
    $(".toAccountList").click(function () {             //切换到账户列表
        $("#newAccountTab").addClass("mc-hide");
        $("#modifyAccountTab").addClass("mc-hide");
        $("#accountList").removeClass("mc-hide");
    })

    $("#refresh").click(function() {
        asyncData();
    });
    $("#manager-ip-list").click(function () {
        cn.RefreshIfame("/detail/security/"+$("#dbId").val());
    })
    /*页面按钮初始化 --end*/

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
                    },callback:{
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
                        max:cn.maxConcurrency,
                        message:'最大并发量1-'+cn.maxConcurrency
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
                    },               
                    callback: {
                        callback: function(value, validator, $field) {
                            if (/[<>=]/.test(value)) {
                                return {
                                    valid: false,
                                    message: '不能包含左右尖括号以及等号'
                                }
                            }
                            else{
                                return {
                                    valid: true
                                }
                            }

                        }
                    }
                }
            }
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();
        var createUserData = dbUser.GetCreateDbUserData();
        if(createUserData.ips != '' && createUserData.type != ''){
            var url = "/dbUser";
            var text=$("#submitCreateUserForm").text();
            $("#submitCreateUserForm").addClass('disabled').text("提交中...");
           cn.PostData(url, createUserData, function () {
                /*刷新本身ifame*/
                var $iframe = $("body", parent.document).find("iframe");
                $iframe.attr("src", $iframe.attr("src"));
            },'#submitCreateUserForm',text);
        }else{
            var title = "警告";
            var text = "您创建的数据库账户没有添加IP名单,请添加后创建!";
            cn.DialogBoxInit(title,text,function(){
                $("#submitCreateUserForm").removeAttr("disabled");
            });
        }
    }).on('keyup', '[name="newPwd1"]', function () {
        if($("[name = 'newPwd2']").val() != ''){
            $('#db_user_create_form').bootstrapValidator('revalidateField', 'newPwd2');
        }
    })

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
                        max:cn.maxConcurrency,
                        message:'最大并发量1-'+cn.maxConcurrency
                    }
                }
            },
            modifyFormNewPwd1: {
                validators: {
                    notEmpty: {
                        message:'密码不能为空'
                    }
            		/*,regexp: {
                        regexp: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z@#$%^&*!~_-]{6,32}$/,
                        message: "由字母、数字或特殊字符如：@#$%^&*!~ 组成,要求6-32位，必须要包含数字，大小写字母"
                    }*/
            		/*,regexp: {
                        regexp: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z_-]{6,32}$/,
                        message: "由字母、数字、中划线或下划线组成,要求6-32位，必须要包含数字，大小写字母"
                    }*/
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
            }
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();

        var modifyUserData = dbUser.GetModifyDbUserData();
        if(modifyUserData.ips != '' && modifyUserData.type != ''){
            var url = "/dbUser/authority/"+$("#modifyFormDbUsername").html();
            var text=$("#submitModifyUserForm").text();
            $("#submitModifyUserForm").addClass("disabled").text("提交中...");
            cn.PostData(url,modifyUserData,function(){
                /*刷新本身ifame*/
                var $iframe = $("body",parent.document).find("iframe");
                $iframe.attr("src",$iframe.attr("src"));
            },'#submitModifyUserForm',text);
        }else{
            var title = "警告";
            var text = "您修改的数据库账户没有添加IP名单,请添加后修改!";
            cn.DialogBoxInit(title,text,function(){
                $("#submitModifyUserForm").removeAttr("disabled");
            });
        }
    }).on('keyup', '[name="modifyFormNewPwd1"]', function () {
        if($("[name = 'modifyFormNewPwd2']").val() != ''){
            $('#db_user_modify_form').bootstrapValidator('revalidateField', 'modifyFormNewPwd2');
        }
    });
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
                        // regexp: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z_-]{6,32}$/,
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
        var data = {
            "username":$("#reset-password-username").val(),
            "password":$("[name = 'reset-password']").val(),
            "dbId":$("#dbId").val()
        }
        var url = "/dbUser/security/"+$("#reset-password-username").val();
        cn.PostData(url,data, function () {
            /*刷新本身ifame*/
            var $iframe = $("body",parent.document).find("iframe");
            $iframe.attr("src",$iframe.attr("src"));
        },'#resetPasswordBoxSubmit');
    }).on('keyup', '[name="reset-password"]', function () {
        if($("[name = 'reset-password-repeat']").val() != ''){
            $('#reset-password-form').bootstrapValidator('revalidateField', 'reset-password-repeat');
        }
    });
    /*修改描述*/
    $("#modifyDescnForm").bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields:{
        	'modifyDescn': {
                validators: {
                    stringLength: {
                        max: 100,
                        message: '输入长度不超过100个字符'
                    }
                }
            }
        }	
    }).on('success.form.bv', function(e) {
    	e.preventDefault();
    	var username = $("#modify-descn-username").val();
    	var url = "/dbUser/descn/" + username;
    	var data = {
    			username: username,
    			descn : $("#modifyDescn").val(),
    			dbId : $("#dbId").val()
    	}
        cn.PostData(url, data, function () {
        	 /*刷新iframe*/
             var $iframe = $("body", parent.document).find("iframe");
             $iframe.attr("src", $iframe.attr("src"));
         });
    })
    
    /*加载数据*/
    asyncData();

    function asyncData() {
        var dbUserListUrl = "/dbUser/"+$("#dbId").val();
    	cn.GetData(dbUserListUrl,dbUser.DbUserListHandler);
    }
    function asyncDbUserIpData(){
        cn.GetData("/dbIp/"+$("#dbId").val()+"/null",dbUser.DbUserIpHandler);   //创建用户加载IP
    }
})
