<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="page-content-area">
	<div id="page-header-id" class="page-header">
		<h1> 
			<a href="${ctx}/db/list">数据库列表</a>
			<small> 
				<i class="ace-icon fa fa-angle-double-right"></i> 
				${db.dbName}
			</small>
		</h1>
	</div>
	<!-- /.page-header -->
	<div class="row">
		<div class="widget-box transparent ui-sortable-handle">
			<div class="widget-header">
				<div class="widget-toolbar no-border pull-left">
					<ul id="db-detail-tabs" class="nav nav-tabs" id="myTab2">
						<li class="active">
							<a data-toggle="tab" href="#db-detail-user-mgr">用户管理</a>
						</li>
						<li class="">
							<a data-toggle="tab" href="#db-detail-info">数据库信息</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="widget-body">
				<div class="widget-main padding-12 no-padding-left no-padding-right">
					<div class="tab-content padding-4">
						<div id="db-detail-info" class="tab-pane">
							<div id="db-detail-table" class="col-xs-6">
								<table class="table table-bordered" id="db_detail_table">
									<tr>
										<td width="50%">数据库名</td>
										<td width="50%">${db.dbName}</td>
									</tr>
									<tr>
										<td>所属用户</td>
										<td>${db.createUser}</td>
									</tr>
									<tr>
										<td>创建时间</td>
										<td>${db.createTime}</td>
									</tr>
									<c:forEach items="${containers}" var="container">
										<tr>
											<td>${container.clusterNodeName}</td>
											<td>${container.ipAddr}</td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
						<div id="db-detail-user-mgr" class="tab-pane  active">
							<!-- <div class="col-xs-10">
								<div class=" pull-right">
									<button type="button" class="btn btn-xs btn-success bigger" data-toggle="modal" data-target="#create-dbuser-form">
										<i class="ace-icont fa fa-plus"></i>创建用户
									</button>
									<button type="button" class="btn btn-xs btn-danger bigger disabled">
										<i class="ace-icont fa fa-trash-o"></i>删除用户
									</button>
								</div>
							</div> -->
							<div class="col-xs-10"><!--style="margin-top:8px"  -->
								<table class="table table-bordered" id="db_detail_table" >
								<thead>
									<tr style="background-color: #307ECC;color:#FFFFFF;">
										<th class="center">
											<label class="position-relative">
												<input type="checkbox" class="ace" />
												<span class="lbl"></span>
											</label>
										</th>
										<th>
											用户名
										</th>
										<th>用户权限</th>
										<th>ip地址</th>
										<th>频次限制</th>
										<th>
											当前状态
										</th>
									</tr>
								</thead>
									<tbody id="tby">
									<c:forEach items="${dbUsers}" var="dbUser">
										<tr>
											<td class="center">
												<label class="position-relative">
												<input type="checkbox" class="ace"/>
												<span class="lbl"></span>
												</label>
											</td>
											<td>${dbUser.username}</td>
											<td>
												<c:if test="${dbUser.type eq 'wr'}">读写</c:if>
												<c:if test="${dbUser.type eq 'manager'}">管理员</c:if>
											</td>
											<td>${dbUser.acceptIp}</td>
											<td>${dbUser.maxConcurrency}</td>
											<td>
												<c:if test="${dbUser.status eq 0}">未审核</c:if>
												<c:if test="${dbUser.status eq 1}">正常</c:if>
												<c:if test="${dbUser.status eq 3}">创建失败</c:if>
												<c:if test="${dbUser.status eq 4}">未通过</c:if>
											</td>
										</tr>
									</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="create-dbuser-form" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<form id="db_apply_form" name="db_apply_form" class="form-horizontal" role="form" action="" method="post">
					<div class="col-xs-12">
						<h4 class="lighter">
							<i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
							<a href="#modal-wizard" data-toggle="modal" class="blue"> 创建数据库用户 </a>
						</h4>
						<div class="widget-box">
							<div class="widget-body">
								<div class="widget-main">
									<div class="form-group">
										<label class="col-sm-2 control-label" for="userName">用户名</label>
										<div class="col-sm-8">
											<input class="form-control" name="userName" id="userName" type="text" />
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label" for="db_name">密码</label>
										<div class="col-sm-8">
											<input class="form-control" name="password" id="password" type="text" />
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label" for="connection_type">用户类型</label>
										<div class="col-sm-4">
											<select class="form-control" name="userPrivilege" id="userPrivilege">
												<option>管理员</option>
												<option>读写</option>
											</select>
										</div>
									</div>
									 <div class="form-group">
								        <label class="col-sm-2 control-label">IP地址</label>
								        <div class="col-sm-4">
								            <input type="text" class="form-control" name="Ip[]" />
								        </div>
								        <div class="col-sm-2">
								            <button type="button" class="btn btn-success addButton btn-sm">
								                <i class="fa fa-plus"></i>
								            </button>
								        </div>
								    </div>
								    <div class="form-group hide" id="optionTemplate">
								        <div class="col-sm-offset-2 col-sm-4">
								            <input type="text" class="form-control" name="Ip[]" />
								        </div>
								        <div class="col-sm-4">
								            <button type="button" class="btn btn-danger btn-sm removeButton">
								                <i class="fa fa-minus"></i>
								            </button>
								        </div>
								    </div>
									<div class="form-group">
										<label class="col-sm-2 control-label" for="read_write_ratio">读写比例</label>
										<div class="col-sm-2">
											<input class="form-control" name="readWriterRate" id="readWriterRate" type="text"
												placeholder="1:1" />
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label" for="maximum_concurrency">最大并发量</label>
										<div class="col-sm-2">
											<input class="form-control" name="maxConcurrency" id="maxConcurrency" type="text"
												placeholder="100"/>
										</div>
										<label class="control-label" for="maximum_concurrency">/s</label>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">关闭</button>
						<button type="submit" class="btn btn-sm btn-primary" onclick="">创建</button>
					</div>
				</form>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- /.page-content-area -->
<link rel="stylesheet" href="${ctx}/static/styles/bootstrap/bootstrapValidator.min.css" />
<script src="${ctx}/static/scripts/bootstrap/bootstrapValidator.min.js"></script>
<script type="text/javascript">
$(function(){
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
function pageinit(){
	checkboxControl();
}
</script>
