<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="${ctx}/static/scripts/jquery.bootstrap-duallistbox.min.js"></script>
<div class="page-content-area">
	<div id="page-header-id" class="page-header">
		<h1> 
			<a href="${ctx}/db/list">数据库列表</a>
			<small> 
				<i class="ace-icon fa fa-angle-double-right"></i> 
				${dbApplyStandard.applyCode}
			</small>
		</h1>
	</div>
	<!-- /.page-header -->
	<input type="text" class="form-control hide" id="clusterId" name="clusterId"/>
	<div class="row clearfix">
		<div class="col-md-5 column">
			<div class="col-sm-12">
			<table class="table table-bordered" id="db_detail_table">	
			<caption>用户申请单</caption>
			<tr>
				<td>数据库名</td>
				<td>${dbApplyStandard.applyCode}</td>
			</tr>
			<tr>
				<td>所属用户</td>
				<td>${dbApplyStandard.createUser}</td>
			</tr>
			<tr>
				<td>链接类型</td>
				<td>${dbApplyStandard.linkType}</td>
			</tr>
			<tr>
				<td>数据库引擎</td>
				<td>${dbApplyStandard.engineType}</td>
			</tr>
			<%-- <tr>
				<td>邮件通知</td>
				<td>
					<c:if test="${dbApplyStandard.isEmailNotice == '1'}">开启</c:if>
					<c:if test="${dbApplyStandard.isEmailNotice != '1'}">关闭</c:if>
				</td>
			</tr> --%>
			<tr>
				<td>申请时间</td>
				<td>${dbApplyStandard.createTime}</td>
			</tr>
			</table>
			</div>
		</div>
		<div class="col-md-7 column">
			<ul id="myTab" class="nav nav-tabs">
			   <li class="active">
			      <a href="#create_on_cluster" data-toggle="tab">在已有集群创建</a>
			   </li>
			   <li>
			   	  <a href="#create_on_new_cluster" data-toggle="tab">在新集群创建</a>
			   </li>
			   <li>
			   	  <a href="#disagree" data-toggle="tab">不同意</a>
			   </li>
			</ul>					
			<div id="myTabContent" class="tab-content" >
				<div class="tab-pane fade in active" id="create_on_cluster" style="margin-top: 45px;margin-bottom: 45px;">
				<form id="create_on_old_cluster_form" class="form-horizontal" role="form" action="${ctx}/db/audit/save" method="post">
					<input type="text" class="form-control hide" value="${dbApplyStandard.applyCode}" id="applyCode" name="applyCode"/>
					<input type="text" class="form-control hide" id="dbId" name="dbId" value="${dbApplyStandard.belongDb}"/>
					<input type="text" class="form-control hide" value="2" id="auditType" name="auditType"/>
					<input type="text" class="form-control hide" value="${dbApplyStandard.id}" id="dbApplyStandardId" name="dbApplyStandardId"/>
						<div class="form-group">
							<label for="text" class="col-sm-2 control-label">选择集群</label>
							<div class="col-sm-8">
								<select id="mclusterOption" name="mclusterId" class="form-control">
									<option value=""></option>
									<c:forEach var="mcluster" items="${mclusters}">
										<option value="${mcluster.id}">${mcluster.mclusterName}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-sm-2">
								<button id="create_on_old_mcluster_botton" type="button" onclick="createDbOnOldMcluster()" class="btn btn-sm btn-primary disabled">创建</button>
							</div>
						</div>
					</form>
			   </div>
				<div class="tab-pane fade" id="create_on_new_cluster" style="margin-top: 45px;margin-bottom: 45px;">
					<form class="form-horizontal" id="create_on_new_cluster_form" method="post" action="${ctx}/db/audit/save">
					<input type="text" class="form-control hide" value="${dbApplyStandard.applyCode} id="applyCode" name="applyCode"/>
					<input type="text" class="form-control hide" id="dbId" name="dbId" value="${dbApplyStandard.belongDb}"/>
					<input type="text" class="form-control hide" value="${dbApplyStandard.id}" id="dbApplyStandardId" name="dbApplyStandardId"/>
					<input type="text" class="form-control hide" value="2" id="auditType" name="auditType"/>
						<div class="form-group">
							<label for="text" class="control-label col-sm-2">集群名称</label>
							<div class="col-sm-8">
								<input class="form-control" name="mclusterName" id="mclusterName" type="text" value="${dbApplyStandard.applyCode}"/>
							</div>
							<div class="col-sm-2">
								<button id="create-mcluster-botton" type="button" onclick="createDbOnNewMcluster()" class="btn btn-sm btn-primary">创建</button>
							</div>
						</div>
					</form>
				</div>
				<div class="tab-pane fade" id="disagree">
				<div>
				      <form class="form-horizontal" id="refuse_create_mcluster" role="form" method="post" action="${ctx}/db/audit/save">
						  <input type="text" class="form-control hide" value="4" id="auditType" name="auditType"/>
							<input type="text" class="form-control hide" value="${dbApplyStandard.applyCode} id="applyCode" name="applyCode"/>
							<input type="text" class="form-control hide" id="dbId" name="dbId" value="${dbApplyStandard.belongDb}"/>
							<input type="text" class="form-control hide" value="${dbApplyStandard.id}" id="dbApplyStandardId" name="dbApplyStandardId"/>
						  <div class="form-group">
						  	<div class="col-sm-12">
						      <textarea  id="auditInfo" name="auditInfo" class="form-control" rows="3" placeholder="请输入未通过原因"></textarea>
						  	</div>
						  	<div class="col-sm-12">
						  		<button type="button" class="btn btn-sm btn-primary pull-right" onclick="refuseCreateMcluster()" style="margin-top:10px">确定</button>
						  		</div>
						  </div>
					  </form>
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
	
	hostDualListBox();
	formValidate();
	$("#pageMessage").hide();
});
function createDbOnOldMcluster(){
	$.ajax({
		type : "post",
		url : "${ctx}/db/audit/save",
		data :$('#create_on_old_cluster_form').serialize()
	});
	window.location = "${ctx}/db/list";
}
function createDbOnNewMcluster(){
	var isClick = true;
	$('#create_on_new_cluster_form').bootstrapValidator('revalidateField', 'mclusterName')
	    .on('success.field.bv', function(e, data) {
			if(isClick){
				isClick = false;
				$.ajax({
					type : "post",
					url : "${ctx}/db/audit/save",
					data :$('#create_on_new_cluster_form').serialize()
				});
				window.location = "${ctx}/db/list";
			}
	    });
}
function refuseCreateMcluster(){
	$.ajax({
		type : "post",
		url : "${ctx}/db/audit/save",
		data :$('#refuse_create_mcluster').serialize()
	});
	window.location = "${ctx}/db/list";
}
function request(paras)
{ 
    var url = location.href; 
    var paraString = url.substring(url.indexOf("?")+1,url.length).split("&"); 
    var paraObj = {} 
    for (i=0; j=paraString[i]; i++){ 
    paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length); 
    } 
    var returnValue = paraObj[paras.toLowerCase()]; 
    if(typeof(returnValue)=="undefined"){ 
    return ""; 
    }else{ 
    return returnValue; 
    } 
}
//创建db表单验证
function formValidate() {
	$("#create_on_new_cluster_form").bootstrapValidator({
	  message: '无效的输入',
         feedbackIcons: {
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
       	  mclusterName: {
                 validMessage: '请按提示输入',
                 validators: {
                     notEmpty: {
                         message: '集群名称不能为空!'
                     },
			          stringLength: {
			              max: 40,
			              message: '集群名过长'
			          },regexp: {
		                  regexp: /^([a-zA-Z_]+[a-zA-Z_0-9]*)$/,
  		                  message: "请输入字母数字或'_',集群名不能以数字开头."
                 	  },remote: {
	                        message: '集群名已存在!',
	                        url: "${ctx}/mcluster/validate"
	                    }
	             }
         	}	
         }
     }).on('error.field.bv', function(e, data) {
    	 $("#validate").val("");
    	 $('#create-mcluster-botton').addClass("disabled");
     }).on('success.field.bv', function(e, data) {
    	 $("#validate").val("true");
    	 $('#create-mcluster-botton').removeClass("disabled");
     });
	
	$("#create_on_old_cluster_form").bootstrapValidator({
	  message: '无效的输入',
         fields: {
        	 mclusterId: {
                 validMessage: '请按提示输入',
                 validators: {
                     notEmpty: {
                         message: '选择集群不能为空!'
                     }
	             }
         	}	
         }
     }).on('error.field.bv', function(e, data) {
    	 $('#create_on_old_mcluster_botton').addClass("disabled");
     }).on('success.field.bv', function(e, data) {
    	 $('#create_on_old_mcluster_botton').removeClass("disabled");
     });
}
function hostDualListBox()
{	
	 var demo1 = $('select[name="hostIds"]').bootstrapDualListbox();
     $("#demoform").submit(function() {
    	 if(4 != $("#duallistbox").val().length){
			$('#pageMessage').html(pageMessage("warning","请选择4台物理机")).show().fadeOut(3000);
    		return false;
    	 }else{
    		 return true;
    	 }
     });		
}
</script>
