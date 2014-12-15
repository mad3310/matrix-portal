<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="page-content-area">
	<div id="page-header-id" class="page-header">
		<h1> 
			<a href="${ctx}/list/db">数据库列表</a>
			<small id="headerDbName"> 
				<i class="ace-icon fa fa-angle-double-right"></i> 
			</small>
		</h1>
	</div>
	<!-- /.page-header -->
	<input class="hidden" value="${dbId}" name="dbId" id="dbId" type="text" />
	<div class="row">
		<div class="widget-box transparent ui-sortable-handle">
			<div class="widget-header">
				<div class="widget-toolbar no-border pull-left">
					<ul id="db-detail-tabs" class="nav nav-tabs" id="myTab2">
						<li class="active">
							<a data-toggle="tab" href="#db-detail-info">数据库信息</a>
						</li>
						<li class="">
							<a data-toggle="tab" href="#db-detail-user-mgr">用户管理</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="widget-body">
				<div class="widget-main padding-12 no-padding-left no-padding-right">
					<div class="tab-content padding-4">
						<div id="db-detail-info" class="tab-pane  active">
							<div id="db-detail-table" class="col-xs-6">
								<table class="table table-bordered" id="db_detail_table">
									<tr>
										<td width="50%">数据库名</td>
										<td width="50%" id="db_detail_table_dbname"></td>
									</tr>
									<tr>
										<td>所属用户</td>
										<td id="db_detail_table_belongUser"></td>
									</tr>
									<tr>
										<td>创建时间</td>
										<td id="db_detail_table_createtime"></td>
									</tr>
								</table>
							</div>
						</div>
						<div id="db-detail-user-mgr" class="tab-pane">
							<div class="widget-box widget-color-blue ui-sortable-handle col-xs-10">
								<div class="widget-header">
									<div class="widget-toolbar no-border pull-right">
										<button type="button" class="btn btn-white btn-primary btn-xs hide" data-toggle="modal" data-target="#create-dbuser-form">
											<i class="ace-icont fa fa-plus"></i>创建用户
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
													<input type="checkbox" class="ace" />
													<span class="lbl"></span>
												</label>
											</th>
											<th>用户名</th>
											<th>用户权限</th>
											<th>ip地址</th>
											<th>读写比例</th>
											<th>当前状态</th>
										</tr>
									</thead>
										<tbody id="tby">
										</tbody>
									</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<link rel="stylesheet" href="${ctx}/static/styles/bootstrap/bootstrapValidator.min.css" />
<script src="${ctx}/static/scripts/bootstrap/bootstrapValidator.min.js"></script>

<script type="text/javascript">
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
	$.ajax({ 
		type : "get",
		url : "${ctx}/dbUser/"+$("#dbId").val(),
		dataType : "json", /*这句可用可不用，没有影响*/
		success : function(data) {
			if(error(data)) return;
			var array = data.data;
			var tby = $("#tby");
			
			for (var i = 0, len = array.length; i < len; i++) {
				var td1 = $("<td class=\"center\">"
						    + "<label class=\"position-relative\">"
						    + "<input type=\"checkbox\" class=\"ace\"/>"
						    + "<span class=\"lbl\"></span>"
						    + "</label>"
					        + "</td>");
				var	td2 = $("<td>"
							+ array[i].username
							+ "</td>");
				var td3;
				if(array[i].type == 1){
					var td3 = $("<td>"
							    + "管理员"
							    + "</td>");
				}else if(array[i].type == 2){
					var td3 = $("<td>"
						    + "只读用户"
						    + "</td>");
				}else{
					var td3 = $("<td>"
							    + "读写用户"
							    + "</td>");
				}
				var td4 = $("<td>"
							+array[i].acceptIp
							+ "</td>");
				var td5 = $("<td>"
							+array[i].maxConcurrency
							+ "</td>");
				var td6 = $("<td>"
							+translateStatus(array[i].status)
							+ "</td>");
					
				if(array[i].status == 0 ||array[i].status == 5||array[i].status == 13){
					var tr = $("<tr class=\"warning\"></tr>");
				}else if(array[i].status == 3 ||array[i].status == 4||array[i].status == 14){
					var tr = $("<tr class=\"danger\"></tr>");
				}else{
					var tr = $("<tr></tr>");
				}
				
				tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6);
				tr.appendTo(tby);
			}//循环json中的数据 
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
	$.ajax({ 
		type : "get",
		url : "${ctx}/db/"+$("#dbId").val(),
		dataType : "json", 
		success : function(data) {
			if(error(data)) return;
			var dbInfo = data.data;
			$("#headerDbName").append(dbInfo.dbName);
			$("#db_detail_table_dbname").text(dbInfo.dbName);
			$("#db_detail_table_belongUser").text(dbInfo.user.userName);
			$("#db_detail_table_createtime").text(date('Y-m-d H:i:s',dbInfo.createTime));
			for(var i=0,len = dbInfo.containers.length; i < len; i++)
			{
				var td1 = $("<td>"
						+ dbInfo.containers[i].containerName
						+"</td>");
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
}
</script>
