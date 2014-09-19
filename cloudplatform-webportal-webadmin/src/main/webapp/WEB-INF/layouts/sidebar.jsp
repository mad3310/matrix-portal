<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- #section:basics/sidebar -->
<div id="sidebar" class="sidebar responsive">
	<script type="text/javascript">
		try {
			ace.settings.check('sidebar', 'fixed')
		} catch (e) {
		}
	</script>
	<!-- /.sidebar-shortcuts -->

	<ul id="sidebar-list" class="nav nav-list">
		<li id="sidebar-mcluster-mgr" class="">
			<a href="#" class="dropdown-toggle">
				<i class="menu-icon fa fa-cubes"></i> 
				<span class="menu-text"> 集群管理 </span> 
				<b class="arrow fa fa-angle-down"></b>
			</a>
			<b class="arrow"></b>
			<ul class="submenu">
				<li id="sidebar-mcluster-list" class="">
					<a href="${ctx}/mcluster/list"> 
						<i class="menu-icon fa fa-caret-right"></i>
						集群列表
					</a>
					<b class="arrow"></b>
				</li>
			</ul>
		</li>
		<li id="sidebar-db-mgr" class="">
			<a href="#" class="dropdown-toggle">
				<i class="menu-icon fa fa-database"></i> 
				<span class="menu-text"> 数据库管理 </span> 
				<b class="arrow fa fa-angle-down"></b>
			</a>
			<b class="arrow"></b>
			<ul class="submenu">
				<li id="sidebar-db-list" class="">
					<a href="${ctx}/db/list"> 
						<i class="menu-icon fa fa-caret-right"></i>
						数据库列表
					</a>
					<b class="arrow"></b>
				</li>
				<li id="sidebar-db-list" class="">
					<a href="${ctx}/db/user/list"> 
						<i class="menu-icon fa fa-caret-right"></i>
						数据库用户列表
					</a>
					<b class="arrow"></b>
				</li>
			</ul>
		</li>
		
	</ul>
	<!-- /.nav-list -->

	<!-- #section:basics/sidebar.layout.minimize -->
	<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
		<i class="ace-icon fa fa-angle-double-left"
			data-icon1="ace-icon fa fa-angle-double-left"
			data-icon2="ace-icon fa fa-angle-double-right"></i>
	</div>
	
	<!-- /section:basics/sidebar.layout.minimize -->
	<script type="text/javascript">
		try {
			ace.settings.check('sidebar', 'collapsed')
		} catch (e) {
		}
	</script>
</div>