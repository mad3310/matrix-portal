<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row">
	<div class="col-xs-12">
		<form id="db_apply_form" class="form-horizontal" role="form" action="${ctx}/db/save">
			<div class="form-group">
				<label class="col-sm-2 control-label" for="project_name">上线项目名称</label>
				<div class="col-sm-4">
					<input class="form-control" name="applyName" id="applyName" type="text" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label" for="db_name">数据库名</label>
				<div class="col-sm-4">
					<input class="form-control" name="applyCode" id="applyCode" type="text" />
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label" for="business_description">业务描述</label>
				<div class="col-sm-8">
					<textarea name="descn" id="descn" class="form-control" rows="3"></textarea>
				</div>
			</div>
			<div class="form-group">
<!-- 						<fieldset disabled> -->
					<label class="col-sm-2 control-label" for="disk_engine">存储引擎</label>
					<div class="col-sm-4">
						<select class="form-control" name="engineType" id="engineType">
							<option>InnoDB</option>
						</select>
					</div>
<!-- 						</fieldset> -->
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label" for="database_access_ip_list">访问数据库IP列表</label>
				<div class="col-sm-6">
					<input class="form-control" name="dataLimitIpList" id="dataLimitIpList" type="text"
						placeholder="192.168.1.102-192.168.105;192.168.1.107" />
				</div>

			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label" for="database_mgr_ip_list">数据库管理IP</label>
				<div class="col-sm-6">
					<input class="form-control" name="mgrLimitIpList" id="mgrLimitIpList" type="text"
						placeholder="192.168.1.102-192.168.105;192.168.1.107" />
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

			<div class="form-group">
				<label class="col-sm-2 control-label" for="develop_language">开发语言</label>
				<div class="col-sm-4">
					<input class="form-control" name="developLanguage" id="developLanguage" type="text"
						placeholder="python,java" />
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label" for="connection_type">链接类型</label>
				<div class="col-sm-4">
					<select class="form-control" name="linkType" id="linkType">
						<option>长链接</option>
						<option>短链接</option>
					</select>
				</div>
			</div>


			<div class="form-group">
				<label class="col-sm-2 control-label" for="email_notification">邮件通知</label>
				<div class="col-sm-3">
					<div class="switch switch-small">
						<input name="isEmailNotice" id="isEmailNotice" type="checkbox" value="1" checked />
					</div>
				</div>
			</div>


			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">申请</button>
				</div>
			</div>
		</form>

	</div>
</div>
<script type="text/javascript">
$(document).ready(function() {
	$("#isEmailNotice").bootstrapSwitch();
	$("#db_apply_form").bootstrapValidator({
		  message: 'This value is not valid',
          feedbackIcons: {
              valid: 'glyphicon glyphicon-ok',
              invalid: 'glyphicon glyphicon-remove',
              validating: 'glyphicon glyphicon-refresh'
          },
          fields: {
        	  applyName: {
                  validMessage: 'The project_name looks great',
                  validators: {
                      notEmpty: {
                          message: '上线项目名称不能为空!'
                      },
			          stringLength: {
			              max: 40,
			              message: '项目名过长'
			          }
                  }
              },
              applyCode: {
                  validMessage: 'The project_name looks great',
                  validators: {
                      notEmpty: {
                          message: '上线项目名称不能为空!'
                      },
			          stringLength: {
			              max: 40,
			              message: '项目名过长'
			          }
                  }
              },
              descn: {
                  validMessage: 'The business_description looks great',
                  validators: {
                      notEmpty: {
                          message: '业务描述不能为空!'
                      },
                      stringLength: {
		              	  max: 100,
		              	  message: '项目描述名过长'
				  		}
             	 	}
              },
              
              fromDbIp:{
                  validMessage: 'The business_description looks great',
                  validators: {
                	  ip: {
                          message: 'IP地址无效'
                      }
                  }
              },
              fromDbPort:{
                  validMessage: 'The business_description looks great',
                  validators: {
                	  integer: {
                          message: '请输入端口号'
                      }
                  }
              },
              dataLimitIpList: {
                  validMessage: 'The business_description looks great',
                  validators: {
                      notEmpty: {
                          message: '数据库访问ip不能为空!'
                      },
		              regexp: {
		                  regexp: /^(((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3})(((\-(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}\;?){1})|(\;?)))+$/,
		                  message: '请按提示格式输入'
		              },
		              ipRangeValidator:{
		            	  
		              }
                  }
              },
              mgrLimitIpList: {
                  validMessage: 'The business_description looks great',
                  validators: {
                      notEmpty: {
                          message: '数据库管理ip不能为空!'
                      },
		              regexp: {
		                  regexp: /^(((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3})(((\-(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}\;?){1})|(\;?)))+$/,
		                  message: '请按提示格式输入'
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
              developLanguage: {
                  validMessage: 'The business_description looks great',
                  validators: {
                      notEmpty: {
                          message: '开发语言不能为空!'
                      },
			          stringLength: {
			              max: 40,
			              message: '内容过长'
			          }
                  }
              }
          }
      });
});
</script>

