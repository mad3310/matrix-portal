<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
</script>

<div id="sidebar" class="sidebar responsive" data-sidebar="true" data-sidebar-scroll="true" data-sidebar-hover="true">
  <ul id="sidebar-list" class="nav nav-list">
    <li id="sidebar-dashboard" >
      <a href="${ctx}/dashboard">
	<i class="menu-icon fa fa-tachometer"></i> 
	<span class="menu-text">Dashboard</span> 
      </a>
    </li>
    <li id="sidebar-common-mgr">
      <a href="#" class="dropdown-toggle">
	<i class="menu-icon fa fa-cogs"></i><span class="menu-text">通用管理</span><b class="arrow fa fa-angle-down"></b>
      </a>
      <b class='arrow'></b>
      <ul class="submenu">
	<li><a href="${ctx}/list/hcluster"><i class="menu-icon fa fa-caret-right"></i> 物理机集群列表</a><b class="arrow"></b></li>
	<li><a href="${ctx}/list/timingTask"><i class="menu-icon fa fa-caret-right"></i> 定时任务管理</a><b class="arrow"></b></li>
	<li><a href="${ctx}/list/baseImages"><i class="menu-icon fa fa-caret-right"></i> 基础镜像管理</a><b class="arrow"></b></li>
	<li><a href="${ctx}/list/dictMgr"><i class="menu-icon fa fa-caret-right"></i> 字典管理</a><b class="arrow"></b></li>
	<li><a href="${ctx}/list/zk"><i class="menu-icon fa fa-cog"></i> zookeeper管理</a><b class="arrow"></b></li>
	<li id="sidebar-task-mgr">
	  <a href="#" class="dropdown-toggle">
	    <i class="menu-icon fa fa-cogs"></i><span class="menu-text">任务管理</span><b class="arrow fa fa-angle-down"></b>
	  </a>
	  <b class='arrow'></b>
	  <ul class="submenu">
	    <li><a href="${ctx}/list/job/unit"><i class="menu-icon fa fa-caret-right"></i> 任务单元列表</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/job/stream"><i class="menu-icon fa fa-caret-right"></i> 任务流列表</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/job/monitor"><i class="menu-icon fa fa-caret-right"></i> 任务监控</a><b class="arrow"></b></li>
	  </ul>
	</li>
      </ul>
    </li>
    <li id="sidebar-rds-mgr">
      <a href="#" class="dropdown-toggle">
	<i class="menu-icon fa fa-database"></i> <span class="menu-text"> RDS 管理 </span> <b class="arrow fa fa-angle-down"></b>
      </a>
      <b class="arrow"></b>
      <ul class="submenu">
	<li id="sidebar-cluster-mgr">
	  <a class="dropdown-toggle" href="#"><i class="menu-icon fa fa-sitemap"></i><span class="menu-text">集群管理 </span><b class="arrow fa fa-angle-down"></b></a>
	  <b class="arrow"></b>
	  <ul class="submenu">
	    <li><a href="${ctx}/list/mcluster"><i class="menu-icon fa fa-caret-right"></i>Container集群列表</a><b class="arrow"></b>
	    </li>
	    <li><a href="${ctx}/list/container"><i class="menu-icon fa fa-caret-right"></i>Container列表</a><b class="arrow"></b></li>
	  </ul>
	</li>
	<li id="sidebar-db-mgr">
	  <a class="dropdown-toggle" href="#"><i class="menu-icon fa fa-database"></i><span class="menu-text">数据库管理 </span><b class="arrow fa fa-angle-down"></b></a>
	  <b class="arrow"></b>
	  <ul class="submenu">
	    <li><a href="${ctx}/list/db"> <i class="menu-icon fa fa-caret-right"></i>数据库列表</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/dbUser"> <i class="menu-icon fa fa-caret-right"></i>数据库用户列表</a><b class="arrow"></b></li>
	  </ul>
	</li>
	<li><a href="${ctx}/list/backup"><i class="menu-icon fa fa-clipboard"></i>备份与恢复</a><b class="arrow"></b></li>
	<li id="sidebar-monitor-mgr">
	  <a class="dropdown-toggle" href="#"><i></i><span class="menu-text">监控管理 </span><b class="arrow fa fa-angle-down"></b></a>
	  <b class="arrow"></b>
	  <ul class="submenu">
	    <li><a href="${ctx}/view/mcluster/monitor"><i class="menu-icon fa fa-caret-right"></i>container集群监控图</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/rds/node/health"><i class="menu-icon fa fa-caret-right"></i>rds健康监控</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/rds/node/resource"><i class="menu-icon fa fa-caret-right"></i>rds资源监控</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/rds/node/keyBuffer"><i class="menu-icon fa fa-caret-right"></i>rds键缓存监控</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/rds/node/innodb"><i class="menu-icon fa fa-caret-right"></i>rds InnoDB监控</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/rds/node/galera"><i class="menu-icon fa fa-caret-right"></i>rds galera监控</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/rds/node/dbSpace"><i class="menu-icon fa fa-caret-right"></i>rds表空间分析</a><b class="arrow"></b></li>
	  </ul>
	</li>
	<li id="sidebar-forewarning-monitor-mgr">
	  <a class="dropdown-toggle" href="#"><i class="menu-icon fa fa-list"></i><span class="menu-text">预警管理 </span><b class="arrow fa fa-angle-down"></b></a>
	  <b class="arrow"></b>
	  <ul class="submenu">
	    <li><a href="${ctx}/list/mcluster/monitor/1"><i class="menu-icon fa fa-caret-right"></i>cluster监控列表</a>
	      <b class="arrow"></b></li>
	    <li><a href="${ctx}/list/mcluster/monitor/2"><i class="menu-icon fa fa-caret-right"></i>node监控列表</a>
	      <b class="arrow"></b></li>
	    <li><a href="${ctx}/list/mcluster/monitor/3"><i class="menu-icon fa fa-caret-right"></i>db监控列表</a>
	      <b class="arrow"></b></li>
	  </ul>
	</li>
      </ul>
    </li>
    <li id='sidebar-gce-mgr'>
      <a href="#" class="dropdown-toggle">
	<i class="menu-icon fa fa-inbox"></i><span class="menu-text"> GCE 管理</span><b class="arrow fa fa-angle-down"></b>
      </a>
      <!-- <a href="#" class="">
	   <i class="menu-icon fa fa-inbox"></i><span class="menu-text"> GCE 管理 (敬请期待...)</span>
      </a> -->
      <b class="arrow"></b>
      <ul class="submenu">
	<li id="sidebar-gce-cluster-mgr">
	  <a href="#" class="dropdown-toggle">
	    <i class="menu-icon fa fa-sitemap"></i><span class="menu-text">集群管理</span><b class="arrow fa fa-angle-down"></b>
	  </a>
	  <b class="arrow"></b>
	  <ul class="submenu">
	    <li><a href="${ctx}/list/gce/cluster"><i class="menu-icon fa fa-caret-right"></i>Container集群列表</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/gce/container"><i class="menu-icon fa fa-caret-right"></i>Container列表</a><b class="arrow"></b></li>
	  </ul>
	</li>
	<li><a href="${ctx}/list/gce/server"><i class="menu-icon fa fa-unlink"></i> GCE列表</a><b class="arrow"></b></li>
	<li><a href="${ctx}/list/gce/image"><i class="menu-icon fa fa-download"></i> 镜像管理</a><b class="arrow"></b></li>
      </ul>
    </li>
    <li id="sidebar-slb-mgr">
      <a href="#" class="dropdown-toggle">
	<i class="menu-icon fa fa-share-alt-square"></i> <span class="menu-text">SLB 管理</span><b class="arrow fa fa-angle-down"></b>
      </a>
      <b class="arrow"></b>
      <ul class="submenu">
	<li id="sidebar-slb-cluster-mgr">
	  <a href="#" class="dropdown-toggle">
	    <i class="menu-icon fa fa-sitemap"></i><span class="menu-text">集群管理</span><b class="arrow fa fa-angle-down"></b>
	  </a>
	  <b class="arrow"></b>
	  <ul class="submenu">
	    <li><a href="${ctx}/list/slb/cluster"><i class="menu-icon fa fa-caret-right"></i>Container集群列表</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/slb/container"><i class="menu-icon fa fa-caret-right"></i>Container列表</a><b class="arrow"></b></li>
	  </ul>
	</li>
	<li id="sidebar-slb-server-mgr"><a href="${ctx}/list/slb"><i class="menu-icon fa fa-caret-right"></i> SLB列表</a><b class="arrow"></b></li>
      </ul>
    </li>
    <li id="sidebar-ocs-mgr">
      <a href="#" class="dropdown-toggle">
	<i class="menu-icon fa fa-cloud-download"></i> <span class="menu-text">OCS 管理</span><b class="arrow fa fa-angle-down"></b>
      </a>
      <!-- <a href="#" class="">
	   <i class="menu-icon fa fa-cloud-download"></i> <span class="menu-text">OCS 管理(敬请期待...)</span>
      </a> -->
      <b class="arrow"></b>
      <ul class="submenu">
	<li id="sidebar-ocs-cluster-mgr">
	  <a href="#" class="dropdown-toggle">
	    <i class="menu-icon fa fa-sitemap"></i> <span class="menu-text">集群管理</span><b class="arrow fa fa-angle-down"></b>
	  </a>
	  <b class="arrow"></b>
	  <ul class="submenu">
	    <li><a href="${ctx}/list/ocs/cluster"><i class="menu-icon fa fa-caret-right"></i>Container集群列表</a><b class="arrow"></b></li>
	    <li><a href="${ctx}/list/ocs/container"><i class="menu-icon fa fa-caret-right"></i>Container列表</a><b class="arrow"></b></li>
	  </ul>
	</li>
	<li><a href="${ctx}/list/bucket"><i class="menu-icon fa fa-cube"></i> Bucket列表</a><b class="arrow"></b></li>
      </ul>
    </li>
    <li id="sidebar-oss-mgr">
      <a href="#" class="dropdown-toggle">
	<i class="menu-icon fa fa-cubes"></i> <span class="menu-text">OSS 管理</span><b class="arrow fa fa-angle-down"></b>
      </a>
      <!-- <a href="#" class="">
	   <i class="menu-icon fa fa-cubes"></i> <span class="menu-text">OSS 管理(敬请期待...)</span>
      </a> -->
      <b class="arrow"></b>
      <ul class="submenu">
	<li><a href="${ctx}/list/oss"><i class="menu-icon fa fa-caret-right"></i> OSS服务列表</a><b class="arrow"></b></li>
      </ul>
    </li>
    <li id="sidebar-gfs-mgr" >
      <a href="#" class="dropdown-toggle">
	<i class="menu-icon fa fa-tasks"></i> <span class="menu-text">GFS管理</span> <b class="arrow fa fa-angle-down"></b>
      </a>
      <b class="arrow"></b>
      <ul class="submenu">
	<li id="sidebar-task-unit-view" >
	  <a href="${ctx}/list/gfs/peer"> <i class="menu-icon fa fa-caret-right"></i>节点列表</a><b class="arrow"></b>
	</li>
	<li id="sidebar-task-stream-view" >
	  <a href="${ctx}/list/gfs/volume"> <i class="menu-icon fa fa-caret-right"></i>卷列表</a><b class="arrow"></b>
	</li>
      </ul>
    </li>
  </ul>

  <!-- #section:basics/sidebar.layout.minimize -->
  <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
    <i class="ace-icon fa fa-angle-double-left"
       data-icon1="ace-icon fa fa-angle-double-left"
       data-icon2="ace-icon fa fa-angle-double-right"></i>
  </div>
  
  <!-- /section:basics/sidebar.layout.minimize -->
  <script type="text/javascript">
    /* 	try {
    ace.settings.check('sidebar', 'collapsed')
    } catch (e) {
    } */
    if(!IsPC()){
    //判断是否支持触摸事件
    ace.settings.navbar_fixed(true);
    ace.settings.sidebar_fixed(true);
    function isTouchDevice() {
    try {
    document.createEvent("TouchEvent");
    bindEvent(); //绑定事件
    } catch (e) {
    }
    }
    //全局变量，触摸开始位置
    var startX = 0, startY = 0;
    //touchstart事件
    function touchSatrtFunc(evt) {
    try {
    //evt.preventDefault(); //阻止触摸时浏览器的缩放、滚动条滚动等
    var touch = evt.touches[0]; //获取第一个触点
    var x = Number(touch.pageX); //页面触点X坐标
    var y = Number(touch.pageY); //页面触点Y坐标
    //记录触点初始位置
    startX = x;
    startY = y;
    } catch (e) {
    }
    }
    //touchmove事件，这个事件无法获取坐标
    function touchMoveFunc(evt) {
    try {
    //evt.preventDefault(); //阻止触摸时浏览器的缩放、滚动条滚动等
    var touch = evt.touches[0]; //获取第一个触点
    var x = Number(touch.pageX); //页面触点X坐标
    var y = Number(touch.pageY); //页面触点Y坐标
    //判断滑动方向
    if (x - startX > 60) {
    $("#sidebar").addClass("display ");
    $("#menu-toggler").addClass("display");
    }else if(x - startX < -60){
			  $("#sidebar").removeClass("display");
			  $("#menu-toggler").removeClass("display");
			  }
			  } catch (e) {
			  }
			  }
			  //touchend事件
			  function touchEndFunc(evt) {
			  try {
			  } catch (e) {
			  }
			  }
			  //绑定事件
			  function bindEvent() {
			  document.addEventListener('touchstart', touchSatrtFunc, true);
			  document.addEventListener('touchmove', touchMoveFunc, true);
			  document.addEventListener('touchend', touchEndFunc, true);
			  }
			  isTouchDevice() ;//touchDevice添加touch事件
			  } 
			  </script>
</div>
