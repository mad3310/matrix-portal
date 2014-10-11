<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default">
	<script type="text/javascript">
		try {
			ace.settings.check('navbar', 'fixed')
		} catch (e) {
		}
	</script>
	<div class="navbar-container" id="navbar-container">
		<!-- #section:basics/sidebar.mobile.toggle -->
		<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
			<span class="sr-only">Toggle sidebar</span> 
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<!-- /section:basics/sidebar.mobile.toggle -->
		<div class="navbar-header pull-left">
			<!-- #section:basics/navbar.layout.brand -->
			<a href="#" class="navbar-brand"> 
					<img src="${ctx}/static/image/site.logo.png"  alt="paas云管理平台" style="max-width:150px; margin-top: -5px;"/>
					<b><small>PAAS portal</small></b>
					<tt><small style="font-size:25%;">beta</small></tt>
			</a>
			<!-- /section:basics/navbar.layout.brand -->
			<!-- #section:basics/navbar.toggle -->
		</div>
		<!-- #section:basics/navbar.dropdown -->
		<div class="navbar-buttons navbar-header pull-right" role="navigation">
			<ul class="nav ace-nav">
				<!-- #section:basics/navbar.user_menu -->
				<li class="light-blue">
					<a data-toggle="dropdown" href="#" class="dropdown-toggle"> 
						<img class="nav-user-photo" src="${ctx}/static/ace/avatars/user.jpg" alt="Jason's Photo" /> 
						<span class="user-info"> 
							<small>欢迎会员,</small> 
							${sessionScope.loginName}
						</span>
						<i class="ace-icon fa fa-caret-down"></i>
					</a>
					<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
						<!-- <li>
							<a href="#">
								<i class="ace-icon fa fa-cog"></i>
								设置
							</a>
						</li>
						<li>
							<a href="profile.html">
							<i class="ace-icon fa fa-user"></i> 
								属性
							</a>
						</li>
						<li class="divider"></li> -->
						<li>
							<a href="${ctx}/account/logout"> 
								<i class="ace-icon fa fa-power-off"></i>
								退出登录
							</a>
						</li>
					</ul>
				</li>
				<!-- /section:basics/navbar.user_menu -->
			</ul>
		</div>
		<!-- /section:basics/navbar.dropdown -->
	</div>
	<!-- /.navbar-container -->
</div>
<!-- /section:basics/navbar.layout -->
