<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/common/common.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Bootstrap 101 Template</title>
</head> 
<body>
			<div class="row clearfix" style="margin-top:200px;">
				<div class="col-md-offset-3 col-md-8 column ">
					<form class="form-horizontal" role="form" action="/account/login">
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputEmail">用户名</label>
							<div class="col-sm-4">
								<input class="form-control" id="loginName" name="loginName" type="text" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputEmail">密码</label>
							<div class="col-sm-4">
								<input class="form-control" id="password" name="password" type="password" />
							</div>
						</div>
						<div>${error}</div>
						<div class="form-group">
						<div class="col-sm-offset-2 col-sm-4">
							<button id="db_create_button" type="submit"
								class="btn btn-primary pull-right">登录</button>
						</div>
					</div>
					</form>
				</div>
			</div>
</body>

</html>
