<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh">

<head>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-compatible" content="IE=edge,chrome=1"/>
	<meta name="viewpoint" content="width=device-width,initial-scale=1, maximum-scale=1, user-scalable=no"/>
	<!-- bootstrap css -->
	<link type="text/css" rel="stylesheet" href="${ctx}/static/css/bootstrap.min.css"/>
	<!-- ui-css -->
	<link type="text/css" rel="stylesheet" href="${ctx}/static/css/ui-css/common.css"/>
	<!-- bootstrapValidator-->
	<link type="text/css" rel="stylesheet" href="${ctx}/static/css/bootstrapValidator.css"/>

	<title>RDS账户管理</title>
</head>

<body>
<!-- 全局参数 start -->
<input class="hidden" value="${dbId}" name="dbId" id="dbId" type="text" />
<!-- 全局参数 end -->
<!-- 账号管理主界面div -->
<div id="accountList" class="m-pr10" role="tablist" aria-multiselectable="true">
	<div class="se-heading" id="headingOne">
		<div class="pull-left">
			<h5>账号管理</h5>
			<a href="/helpCenter/helpCenter.jsp?container=help-createUser" target="_blank" class="hidden-xs"><span class="glyphicon glyphicon-paperclip "></span> 如何创建用户</a>
		</div>
		<div class="pull-right hidden-xs">
			<button id="refresh" class="btn btn-default">
				<span class="glyphicon glyphicon-refresh"></span>
				刷新
			</button>
			<button class="btn btn-primary toCreateAccount">创建帐号</button>
		</div>
		<div class="pull-right hidden-sm hidden-md hidden-lg">
			<button class="btn btn-xs btn-primary toCreateAccount"><span class="glyphicon glyphicon-plus"></span> 创建</button>
		</div>
	</div>
	<div class="table-responsive">
		<table class="table table-hover table-se">
			<thead>
			<tr>
				<th width="15%">帐号</th>
				<th width="15%">状态</th>
				<th width="15%" class="hide">读写比例
					<a class="hide btn btn-default btn-xs glyphicon glyphicon-pencil" href="javascript:void(0)"></a>
				</th>
				<th width="10%" class="hidden-xs">最大并发量
					<a class="hide btn btn-default btn-xs glyphicon glyphicon-pencil" href="javascript:void(0)"></a>
				</th>
				<th width="20%" class="hidden-xs">备注说明</th>
				<th class="text-right" width="25%">
					<span style="padding-left:8px">操作</span>
				</th>
			</tr>
			</thead>
			<tbody id="tby">
			</tbody>
		</table>
	</div>
</div>
<!-- 点击“创建账号”后加载的div 去掉mc-hide既可以显示此div-->
<div id="newAccountTab" class="mc-hide m-pr10" role="tablist" aria-multiselectable="true">
	<!-- heading部分 -->
	<div class="se-heading">
		<div class="pull-left">
			<h5>创建新账号</h5>
			<a class="toAccountList">返回帐号管理</a>
		</div>
	</div>
	<!-- 内容部分，由一个form承载 -->
	<div class="">
		<form id="db_user_create_form" role="form" class="form-horizontal" name="account_modify_form">
			<!-- 数据库账号模块 -->
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label">
					<span class="text-danger">*</span>
					数据库帐号：
				</label>
				<div class="">
					<div class="col-xs-12 col-sm-4">
						<input name="username" class="form-control input-radius-2" type="text"></div>
					<div class="col-xs-12 col-sm-offset-2 col-sm-10 notice-block">
						<p class="text-correct">由小写字母，数字、下划线组成、字母开头，字母或数字结尾，最长16个字符</p>
					</div>
				</div>
			</div>
			<!-- 数据库账号模块end -->
			<!-- 授权数据库模块 -->
			<div class="form-group multi-select">
				<label class="col-xs-12 col-sm-2 control-label">
					<span class="text-danger">*</span>
					授权IP：
				</label>
				<div class="inline-block mcluster-select col-xs-10 col-sm-2">
					<div class="select-head clearfix">
						<p class="inline-block">未授权IP</p>
						<p class="inline-block pull-right">
							<a id="manager-ip-list">管理IP名单</a>
						</p>
					</div>
					<div class="select">
						<ul class="select-list select-list-left">
						</ul>
						<!-- 没有数据记录时显示暂无数据 -->
						<div class="select-msg mc-hide">
							<span class="text-muted">暂无数据</span>
						</div>
					</div>
				</div>
				<div class="inline-block col-xs-2 col-sm-1 m-authorize">
					<div style="margin-bottom:5px">
						<a class="btn_db_add">授权&nbsp;&gt;</a>
					</div>
					<div>
						<a class="btn_db_remove">&lt;&nbsp;移除</a>
					</div>
				</div>
				<div class="inline-block mcluster-select col-xs-12 col-sm-5 col-md-4">
					<div class="select-head clearfix">
						<p class="inline-block">已授权IP</p>
						<p class="inline-block pull-right">
							<span style="padding-right: 5px;color:#bbb">权限</span>
							<a class="select-all-rw">全部设读写</a>
						</p>
					</div>
					<div class="select">
						<div class="select-wrap">
							<ul class="select-list select-list-right">
							</ul>
							<!--无数据时显示暂无数据 -->
							<div class="select-msg mc-hide">
								<span class="text-muted">暂无数据</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 授权数据库模块end -->
			<!-- 密码输入模块 -->
			<div class="hide form-group">
				<label class="col-sm-2 control-label">
					<span class="text-danger">*</span>
					读写比例：
				</label>
				<div class="col-sm-8 row">
					<!-- 密码输入框 -->
					<div class="col-sm-4">
						<input name="readWriterRate" class="form-control input-radius-2"/>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label">
					<span class="text-danger">*</span>
					最大并发量：
				</label>
				<div class="">
					<!-- 密码输入框 -->
					<div class="col-xs-12 col-sm-4">
						<input name="maxConcurrency" class="form-control input-radius-2"/>
					</div>
					<div class="col-sm-1 hidden-xs" style="padding-top : 8px">
						<a data-container="body" data-toggle="popover" data-placement="right" data-html="true"
						   data-content="
								<table border='1' style='width:100%;font-size:12px;color:gray;border:1px solid gray;'>
									<tr>
									<th>并发量</th>
									<td>maxConcurrency</td>
								</tr>
								<tr>
									<th>MAX_QUERIES_PER_HOUR</th>
									<td>maxConcurrency*7200</td>
								</tr>
								<tr>
									<th>MAX_UPDATES_PER_HOUR</th>
									<td>maxConcurrency*3600</td>
								</tr>
								<tr>
									<th>MAX_CONNECTIONS_PER_HOUR</th>
									<td>maxConcurrency*7200</td>
								</tr>
								<tr>
									<th>MAX_USER_CONNECTIONS</th>
									<td>maxConcurrency</td>
								</tr>
								</table>
								">
							<span>查看计算公式</span>
						</a>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label">
					<span class="text-danger">*</span>
					密码：
				</label>
				<div class="">
					<!-- 密码输入框 -->
					<div class="col-xs-12 col-sm-4">
						<input name="newPwd1" class="form-control input-radius-2" type="text" onfocus="this.type='password'" autocomplete="off"/>
					</div>
					<!-- 密码规则提示 -->
					<div class="col-xs-12 notice-block col-sm-10 col-sm-offset-2">
						<p class="">由字母、数字或特殊字符如：@#$%^&*!~_- 组成，长度6~32位</p>
					</div>
				</div>
			</div>
			<!-- 密码输入模块end -->
			<!-- 确认密码模块 -->
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label">
					<span class="text-danger">*</span>
					确认密码：
				</label>
				<div class="">
					<!-- 确认密码输入框 -->
					<div class="col-xs-12 col-sm-4">
						<input name="newPwd2" class="form-control input-radius-2" type="text" onfocus="this.type='password'" autocomplete="off"/>
					</div>
				</div>
			</div>
			<!-- 密码确认模块end -->
			<!-- 备注说明模块 -->
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label">备注说明：</label>
				<!-- 备注输入框 -->
				<div class="">
					<div class="col-xs-12 col-sm-4">
						<textarea name="descn" class="form-control input-radius-2" style="width:100%;height:90px"></textarea>
					</div>
					<!-- 备注输入超过长度限制提示 -->
					<div class="col-sm-8 help-info mc-hide">
						<small class="text-danger" >
							<span class="glyphicon glyphicon-remove-sign"></span>
							备注说明最多256个字符
						</small>
					</div>
					<!-- 备注信息规则静态提示 -->
					<div class="col-sm-10 col-sm-offset-2 col-xs-12 notice-block">
						<p class="text-correct">请输入备注说明，输入长度不超过100个字符!</p>
					</div>
				</div>
			</div>
			<!-- 备注说明模块end -->
			<!-- 按钮模块 -->
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label"></label>
				<div class="col-xs-12 col-sm-4">
					<button type="submit" id="submitCreateUserForm" class="btn btn-primary">提交</button>
					<button type="button" class="btn btn-default toAccountList">返回</button>
				</div>
			</div>
			<!-- 按钮模块end -->
		</form>
	</div>
</div>
<div id="modifyAccountTab" class="mc-hide m-pr10" role="tablist" aria-multiselectable="true">
	<!-- heading部分 -->
	<div class="se-heading">
		<div class="pull-left">
			<h5>编辑账号</h5>
			<a class="toAccountList">返回帐号管理</a>
		</div>
	</div>
	<!-- 内容部分，由一个form承载 -->
	<div style="width:auto;height:auto;">
		<form id="db_user_modify_form" role="form" class="form-horizontal" name="account_modify_form">
			<input class="hidden" id="modifydbUserReadWriterRate" type="text" />

			<!-- 数据库账号模块 -->
			<div class="form-group">
				<label class="col-xs-6 col-sm-2 control-label">
					数据库帐号：
				</label>
				<label id="modifyFormDbUsername" class="col-xs-6 col-sm-4 control-label"></label>
				<div class="">
					<div class="col-xs-6 col-sm-4">
						<label id="modifyFormDbUsername" style="padding-top:7px;"></label>
					</div>
				</div>
			</div>
			<!-- 数据库账号模块end -->
			<!-- 授权数据库模块 -->
			<div class="form-group modify-multi-select">
				<label class="col-xs-12 col-sm-2 control-label">
					<span class="text-danger">*</span>
					授权IP：
				</label>
				<div class="inline-block mcluster-select col-xs-10 col-sm-2">
					<div class="select-head clearfix">
						<p class="pull-left">未授权IP</p>
					</div>
					<div class="select">
						<ul class="select-list select-list-left">
						</ul>
						<!-- 没有数据记录时显示暂无数据 -->
						<div class="select-msg mc-hide">
							<span class="text-muted">暂无数据</span>
						</div>
					</div>
				</div>
				<div class="inline-block col-xs-2 col-sm-1 m-authorize">
					<div style="margin-bottom:5px">
						<a class="btn_db_add">授权&nbsp;&gt;</a>
					</div>
					<div>
						<a class="btn_db_remove">&lt;&nbsp;移除</a>
					</div>
				</div>
				<div class="inline-block mcluster-select col-xs-12 col-sm-5 col-md-4">
					<div class="select-head clearfix">
						<p class="pull-left">已授权IP</p>
						<p class="pull-right">
							<span style="padding-right: 5px;color:#bbb">权限</span>
							<a class="select-all-rw">全部设读写</a>
						</p>
					</div>
					<div class="select">
						<div class="select-wrap">
							<ul class="select-list select-list-right">
							</ul>
							<!--无数据时显示暂无数据 -->
							<div class="select-msg mc-hide">
								<span class="text-muted">暂无数据</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label"> <span class="text-danger">*</span> 最大并发量：
				</label>
				<div class="">
					<!-- 密码输入框 -->
					<div class="col-xs-12 col-sm-4">
						<input id="modifydbUserMaxConcurrency" name="modifydbUserMaxConcurrency" class="form-control input-radius-2" />
					</div>
					<div class="col-sm-1 hidden-xs" style="padding-top : 8px">
						<a data-container="body" data-toggle="popover" data-placement="right" data-html="true"
						   data-content="
								<table border='1' style='width:100%;font-size:12px;color:gray;border:1px solid gray;'>
									<tr>
									<th>并发量</th>
									<td>maxConcurrency</td>
								</tr>
								<tr>
									<th>MAX_QUERIES_PER_HOUR</th>
									<td>maxConcurrency*7200</td>
								</tr>
								<tr>
									<th>MAX_UPDATES_PER_HOUR</th>
									<td>maxConcurrency*3600</td>
								</tr>
								<tr>
									<th>MAX_CONNECTIONS_PER_HOUR</th>
									<td>maxConcurrency*7200</td>
								</tr>
								<tr>
									<th>MAX_USER_CONNECTIONS</th>
									<td>maxConcurrency</td>
								</tr>
								</table>
								">
							<span>查看计算公式</span>
						</a>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label"> <span class="text-danger">*</span> 密码：
				</label>
				<div class="">
					<!-- 密码输入框 -->
					<div class="col-xs-12 col-sm-4">
						<input name="modifyFormNewPwd1" id="modifyFormNewPwd1" class="form-control input-radius-2" type="text" onfocus="this.type='password'" autocomplete="off"/>
					</div>
					<!-- 密码规则提示 -->
					<div class="col-xs-12 notice-block col-sm-10 col-sm-offset-2">
						<p class="">由字母、数字、中划线或下划线组成，长度6~32位</p>
					</div>
				</div>
			</div>
			<!-- 密码输入模块end -->
			<!-- 确认密码模块 -->
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label"> <span class="text-danger">*</span> 确认密码：
				</label>
				<div class="">
					<!-- 确认密码输入框 -->
					<div class="col-xs-12 col-sm-4">
						<input name="modifyFormNewPwd2" id="modifyFormNewPwd2" class="form-control input-radius-2" type="text" onfocus="this.type='password'" autocomplete="off" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label">备注说明：</label>
				<div class="">
					<div class="col-xs-12 col-sm-4">
						<label id="modifyFormDbDesc" style="padding-top:7px"></label>
					</div>
				</div>
			</div>
			<!-- 按钮模块 -->
			<div class="form-group">
				<label class="col-xs-12 col-sm-2 control-label"></label>
				<div class="col-xs-12 col-sm-4">
					<button type="submit" id="submitModifyUserForm" class="btn btn-primary">提交</button>
					<button type="button" class="btn btn-default toAccountList">返回</button>
				</div>
			</div>
			<!-- 按钮模块end -->
		</form>
	</div>
</div>
<div id="showDbuserIpPrivilege" class="modal fade" style="position:absolute;">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h5 id="showDbuserIpPrivilegeTitle" class="modal-title"></h5>
			</div>
			<div class="modal-body">
				<div class="table-responsive">
					<table id="dbuser-list-ip-privilege-table" class="table table-bordered table-striped">
						<thead>
						<tr>
							<th>IP
							</td>
							<th>权限
							</td>
						</tr>
						</thead>
						<tbody id="ip-privilege-tby">
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>

<!--确认对话框-->
<div id="dialog-box" class="modal">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<!--	<button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
                        </button>-->
				<h5 id="dialog-box-title" class="modal-title"></h5>
			</div>
			<div class="modal-body clearfix">
				<div class="col-xs-1 col-sm-1 col-md-1 warning-sign">
					<span class="glyphicon glyphicon-exclamation-sign"></span>
				</div>
				<div id="dialog-box-text" class="col-xs-11 col-sm-10 "></div>
			</div>
			<div class="modal-footer">
				<button id="dialogBoxSubmit" type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
				<button id="dialogBoxCancel" type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
</div>
<!--重置密码-->
<div id="reset-password-box" class="modal">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h5 id="reset-password-box-title" class="modal-title"></h5>
			</div>
			<form id="reset-password-form" role="form" class="form-horizontal">
				<input id="reset-password-username" class="hidden" name="usename" type="text"/>
				<div class="modal-body">
					<div id="reset-password-box-text">
						<div class="form-group">
							<label class="col-sm-4 control-label">密码： </label>
							<div class="col-sm-8 row">
								<div class="col-sm-12">
									<input name="reset-password" class="form-control input-radius-2" type="text" onfocus="this.type='password'" autocomplete="off"/>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">确认密码： </label>
							<div class="col-sm-8 row">
								<div class="col-sm-12">
									<input name="reset-password-repeat" class="form-control input-radius-2" type="text" onfocus="this.type='password'" autocomplete="off"/>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button id="resetPasswordBoxSubmit" type="submit" class="btn btn-primary">确定</button>
					<button id="resetPasswordBoxCancel" type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				</div>
			</form>
		</div>
	</div>

</div>

<!--内容编辑框-->
<div id="edit-spin" class=""></div>
<div class="edit-text-box hide">
	<div class="col-sm-12 col-md-12">
		<h5>请输入描述:</h5>
	</div>
	<form id="modifyDescnForm" role="form" class="form-horizontal">
		<input id="modify-descn-username" class="hidden" name="usename" type="text"/>
		<div class="form-group col-sm-12 col-md-12">
			<input name="modifyDescn" id="modifyDescn" type="text" class="form-control input-radius-2">
			<div class="col-sm-12 notice-block" style="padding-left:0;">
				<p style="color:#999">输入范围：输入长度不超过100个字符</p>
			</div>
		</div>
		<div class="form-group col-sm-12 col-md -12">
			<button id="editBoxCancel" type="button" class="btn btn-default pull-right margin-left-5">取消</button>
			<button id="editBoxSubmit" type="submit" class="btn btn-primary pull-right margin-left-5">提交</button>
		</div>
	</form>
</div>
</body>
<!-- js -->
<script type="text/javascript" src="${ctx}/static/modules/seajs/2.3.0/sea.js"></script>
<script type="text/javascript">

	// Set configuration
	seajs.config({
		base: "${ctx}/static/modules/",
		alias: {
			"jquery": "jquery/2.0.3/jquery.min.js",
			"bootstrap": "bootstrap/bootstrap/3.3.0/bootstrap.js",
			"bootstrapValidator": "bootstrap/bootstrapValidator/0.5.3/bootstrapValidator.js"
		}
	});
	seajs.use("${ctx}/static/page-js/clouddb/accountManager/main");
</script>

</html>
