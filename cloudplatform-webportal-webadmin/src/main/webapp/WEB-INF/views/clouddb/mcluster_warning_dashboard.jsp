<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${ctx}/static/styles/ui-css/style.css"/>
<div class="page-content-area">
	<div class="row">
		<div class="col-xs-12">
			<div class="row">
			
			<div class="col-sm-12 infobox-container">
				<div class="col-sm-4">
					<div class="col-md-12 col-xs-12" style="padding:0px;">
						<div class=" logo-label mt10">
							<div class="width-100  center padding-tb-5">
								<h4>
									<b>cluster状态</b>
									<a class="blue" href="#" onclick="updateMclusterChart('monitorCluster',1)" data-toggle="modal" data-target="#">
										<i class="ace-icon fa fa-refresh icon-on-right bigger-110"></i>
									</a>
								</h4>
							</div>
						</div>
					</div>
					<div id="statistics" class="col-md-12 col-xs-12 mt10">
						<div id="monitorCluster" style="height: 300px"></div>	
						<div class="hr hr8 hr-double"></div>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="col-md-12 col-xs-12">
						<div class=" logo-label mt10">
							<div class="width-100  center padding-tb-5">
								<h4>
									<b>Node状态</b>
									<a class="blue" href="#" onclick="updateMclusterChart('monitorNode',2)" data-toggle="modal" data-target="#">
										<i class="ace-icon fa fa-refresh icon-on-right bigger-110"></i>
									</a>
								</h4>
							</div>
						</div>
					</div>
					<div id="statistics" class="col-md-12 col-xs-12 mt10">
						<div id="monitorNode" style="height: 300px"></div>	
						<div class="hr hr8 hr-double"></div>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="col-md-12 col-xs-12" style="padding:0px;">
						<div class=" logo-label mt10">
							<div class="width-100  center padding-tb-5">
								<h4>
									<b>db状态</b>
									<a class="blue" href="#" onclick="updateMclusterChart('monitorDb',3)" data-toggle="modal" data-target="#">
										<i class="ace-icon fa fa-refresh icon-on-right bigger-110"></i>
									</a>
								</h4>
							</div>
						</div>
					</div>
					<div id="statistics" class="col-md-12 col-xs-12 mt10">
						<div id="monitorDb" style="height: 300px"></div>	
						<div class="hr hr8 hr-double"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="${ctx}/static/scripts/highcharts/highcharts.js"></script>
<script src="${ctx}/static/scripts/highcharts/themes/grid-light.js"></script>
<script src="${ctx}/static/scripts/pagejs/mcluster_warning_dashboard.js"></script>
