<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-compatible" content="IE=edge,chrome=1" />
<meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1, user-scalable=no"/>
<!-- bootstrap css -->
<link type="text/css" rel="stylesheet"
	href="${ctx}/static/css/bootstrap.min.css" />
<!-- fontawesome css -->
<link type="text/css" rel="stylesheet"
	href="${ctx}/static/css/font-awesome.min.css" />
<!-- ui-css -->
<link type="text/css" rel="stylesheet"
	href="${ctx}/static/css/ui-css/common.css" />
<title>Le云控制台首页</title>
</head>
<body>
<%@ include file="../../layouts/header.jsp"%>
<style>
	.bk-form-row-name{padding-left:0;}
</style>
<div class="container-fluid">
	<div class="row main-header hidden-xs">
		<div class="col-xs-12 col-sm-6 col-md-6">
			<div class="pull-left">
				<h4><span>关系型数据库RDS</span></h4>
			</div>
		</div>
	</div>
	<div class="row" style="margin:0;">
		<ul class="nav nav-tabs RDSCreateUl hidden-xs" role="tablist" id="RDSCreateTab">
			<li role="presentation" class="active"><a href="#year" role="tab" data-toggle="tab" style="">包年包月</a></li>
			<!--<li role="presentation"><a href="#dosage" role="tab" data-toggle="tab">按量付费</a></li>-->
		</ul>
		<div class="tab-content mt20">
			<div role="tabpanel" class="tab-pane fade active in" id="year">
				<div class="col-xs-12 col-sm-9 col-md-9">
					<dl class="bk-group">
						<dt class="bk-group-title">基本配置</dt>
						<dd class="bk-group-detail">
							<div class="bk-group-control"></div>
							<div>
								<form id="monthPurchaseForm">
									<div class="form-group bk-form-row col-sm-12">
										<label class="bk-form-row-name col-sm-2">数据库名称：</label>
										<div class="col-sm-4 row">
											<input id="dbName" class="form-control" name="dbName" type="text">
										</div>
									</div>
								</form>
								<div class="bk-form-row col-xs-12 col-sm-12">
									<label class="bk-form-row-name col-xs-12 col-sm-2">地域：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li">
											<div class="bk-buttontab">
												<button class=" bk-button bk-button-primary bk-button-current">
													<div>
														<span>北京</span>
													</div>
												</button>
												<button class="bk-button bk-button-primary disabled hidden-xs">
													<div>
														<span>成都</span>
													</div>
												</button>
												<button class="bk-button bk-button-primary disabled hidden-xs">
													<div>
														<span>青岛</span>
													</div>
												</button>
												<button class="bk-button bk-button-primary disabled hidden-xs">
													<div>
														<span>香港</span>
													</div>
												</button>
												<button class="bk-button bk-button-primary disabled hidden-xs">
													<div>
														<span>深圳</span>
													</div>
												</button>
											</div>
											<span class="bk-lnk hidden-xs" style="cursor: text;">暂时只有北京可用,其他地域陆续开通</span>
										</div>
										<div class="bk-form-row-txt hidden-xs">不同地域之间的产品内网不互通；订购后不支持更换地域，请谨慎选择</div>
									</div>
								</div>
								<div class="bk-form-row col-xs-12 col-sm-12">
									<label class="bk-form-row-name col-xs-12 col-sm-2">可用区：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li clearfix">
											<div class="pull-left">
												<span class="sleBG"> <span class="sleHid">
														<div class="divselect">
															<span></span>
															<ul>
															</ul>
															<input name="hclusterId" type="hidden" value="" />
														</div>
												</span>
												</span> <span class="bk-select-arrow"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="bk-form-row col-xs-12 col-sm-12">
									<label class="bk-form-row-name col-xs-12 col-sm-2">数据库类型：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li clearfix">
											<div class="pull-left">
												<span class="sleBG"> <span class="sleHid">
														<div class="divselect">
															<span>MYSQL</span>
															<ul style="display: none;">
																<li class="bk-select-option"><a href="javascript:;" selectid="1">MYSQL</a></li>
															</ul>
															<input name="mysql" type="hidden" value="" />
														</div>
												</span>
												</span> <span class="bk-select-arrow"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="bk-form-row col-xs-12 col-sm-12 hidden-xs">
									<label class="bk-form-row-name col-xs-12 col-sm-2">版本：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li">
											<div class="bk-buttontab">
												<button class="bk-button bk-button-primary bk-button-current">
													<div><span>5.5</span></div>
												</button>
												<button class=" bk-button bk-button-primary disabled">
													<div><span>5.6</span></div>
												</button>

											</div>
											<span class="bk-lnk" style="cursor: text;">暂时只提供5.5版本</span>
										</div>
									</div>
								</div>
								<div class="bk-form-row col-xs-12 col-sm-12">
									<label class="bk-form-row-name col-xs-12 col-sm-2">存储引擎：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li">
											<div class="bk-buttontab">
												<input name="engineType" type="text" class="hide" value="0">
												<button class=" bk-button bk-button-primary bk-button-current" value="0">
													<div>
														<span>InnDB</span>
													</div>
												</button>
											</div>
										</div>
									</div>
								</div>
								<div class="bk-form-row col-xs-12 col-sm-12 hidden-xs">
									<label class="bk-form-row-name col-xs-12 col-sm-2">链接类型：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li">
											<div class="bk-buttontab">
												<input name="linkType" type="text" class="hide" value="0">
												<button class=" bk-button bk-button-primary bk-button-current" value="0">
													<div>
														<span>长链接</span>
													</div>
												</button>
												<button class="bk-button bk-button-primary" value="1">
													<div>
														<span>短链接</span>
													</div>
												</button>
											</div>
										</div>
									</div>
								</div>
								<div class="bk-form-row col-xs-12 col-sm-12 hidden-xs">
									<label class="bk-form-row-name col-xs-12 col-sm-2">默认管理账户：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li">
											<div class="bk-buttontab">
												<input name="isCreateAdmin" type="text" class="hide" value="0">
												<button class=" bk-button bk-button-primary " value="1">
													<div>
														<span>创建</span>
													</div>
												</button>
												<button class="bk-button bk-button-primary bk-button-current" value="0">
													<div>
														<span>不创建</span>
													</div>
												</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</dd>
					</dl>
					<dl class="bk-group">
						<dt class="bk-group-title">存储</dt>
						<dd class="bk-group-detail">
							<div class="bk-group-control"></div>
							<div class="disabled">
								<div class="bk-form-row col-xs-12 col-sm-12">
									<label class="bk-form-row-name  col-xs-12 col-sm-2">存储空间：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li clearfix">
											<div class="pull-left" style="height: 36px;">
												<span class="sleBG"> <span class="sleHid">
													<div class="divselect">
														<span>10G</span>
														<ul style="display: none;">
															<li class="bk-select-option"><a href="javascript:;" selectid="10737418240">10G</a></li>
														</ul>
														<input name="storageSize" type="hidden" value="10737418240" />
													</div>
												</span>
												</span> <span class="bk-select-arrow"></span>
											</div>
										</div>
										<div class="bk-form-row-txt notice-block">存储空间暂时不限制</div>
									</div>
								</div>
								<div class="bk-form-row col-xs-12 col-sm-12">
									<label class="bk-form-row-name col-xs-12 col-sm-2">内存：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li clearfix">
											<div class="pull-left" style="height: 36px;">
												<span class="sleBG"> <span class="sleHid">
													<div class="divselect">
														<span>4G</span>
														<ul style="display: none;">
															<li class="bk-select-option"><a href="javascript:;" selectid="4294967296">4G</a></li>
														</ul>
														<input name="memorySize" type="hidden" value="4294967296" />
													</div>
											</span>
												</span> <span class="bk-select-arrow"></span>
											</div>
										</div>
										<div class="bk-form-row-txt">最大连接数:60 IOPS:150</div>
									</div>
								</div>
							</div>
						</dd>
					</dl>
					<dl class="bk-group">
						<dt class="bk-group-title">购买量</dt>
						<dd class="bk-group-detail">
							<div class="bk-group-control"></div>
							<div class="disabled">
								<div class="bk-form-row col-xs-12 col-sm-12">
									<label class="bk-form-row-name col-xs-12 col-sm-2">购买时长：</label>
									<div class="col-xs-12 col-sm-10 row">
										<div class="bk-form-row-li clearfix">
											<div class="pull-left" style="height: 36px;">
												<span class="sleBG">
												<span class="sleHid">
													<div class="divselect">
														<span>一年</span>
														<ul style="display: none;">
															<li class="bk-select-option"><a href="javascript:;" selectid="1">一年</a></li>
														</ul>
														<input type="hidden" value="" />
													</div>
												</span>
												</span>
												<span class="bk-select-arrow"></span>
											</div>
										</div>
										<div class="bk-form-row-txt">此功能暂时不开放</div>
									</div>
								</div>
							</div>
							<div class="bk-form-row">
								<label class="bk-form-row-name">数量：</label>
								<div class="bk-form-row-cell">
									<div class="bk-form-row-li">
										<span class="bk-number"> <input type="text" class="bk-number-input" value="3"> <span class="bk-number-unit">台</span> <span class="bk-number-control"> <span class="bk-number-up bk-number-disabled hide"> <i class="bk-number-arrow"></i>
											</span> <span class="bk-number-up"> <i class="bk-number-arrow"></i>
											</span> <span class="bk-number-down bk-number-disabled"> <i class="bk-number-arrow"></i>
											</span> <span class="bk-number-down hide"> <i class="bk-number-arrow"></i>
											</span>
										</span>
										</span>
									</div>
								</div>
							</div>
						</dd>
					</dl>
					<div class="hidden-sm hidden-md hidden-lg step-next" style="margin-bottom:15px;padding-left:45px;">
						<button class="btn-info bk-button"><span style="letter-spacing:5px;">下一步</span> <i class="fa fa-angle-right"></i> </button>
					</div>
				</div>
				<div class="col-xs-12 col-sm-3 col-md-3 config-buy"> <!-- background:#f60; -->
					<div class="hidden-sm hidden-md hidden-lg btn-info step-forward" style="padding:10px 5px; margin-bottom:10px;color:#fff;font-size:16px;"><a class="" style="padding:0 5px;color:#fff;letter-spacing:3px;"><i class="fa fa-angle-left"></i> 上一步</a></div>
					<div class="bk-orders-menu  bk-mb4 hidden-xs">
						<span class="bk-orders-menu-name">购买清单</span> <span class="bk-orders-menu-quantity bk-pale">3台</span>
					</div>
					<div class="bk-scope bk-items bk-mb4">
						<div class="bk-items-title">当前配置</div>
						<div>
							<div class="bk-items-list">
								<ul>
									<li><span class="bk-items-item-name">地域：</span> <span class="bk-items-item-value">北京</span></li>
									<li><span class="bk-items-item-name">配置：</span> <span class="bk-items-item-value">10GB存储空间、2G内存、MySQL 5.5</span></li>
									<li><span class="bk-items-item-name">购买量：</span> <span class="bk-items-item-value">1年x 3台</span></li>
								</ul>
							</div>
						</div>
						<div class="bk-items-price" style="padding-top: 0">
							<div class="bk-items-price-title bk-pale">配置费用：</div>
							<div class="bk-items-price-settle">
								<div>
									<span class="bk-cny">¥</span> <span class="bk-items-price-money">0.00</span> <span class="bk-items-price-unit"></span>
								</div>
							</div>
						</div>
						<div class="bk-items-price">
							<div class="bk-items-price-title bk-pale">公网流量费用：</div>
							<div class="bk-items-price-settle">
								<span class="bk-cny">¥</span> <span class="bk-items-price-money">0.00</span> <span class="bk-items-price-unit">/GB</span>
							</div>
						</div>
						<div class="bk-items-price-control">
							<button form="monthPurchaseForm" type="submit" id="monthPurchaseBotton" class="bk-button">
								<div>
									<span class="ng-scope">立即购买</span>
								</div>
							</button>
							<button class=" hide bk-button bk-button-default">
								<div>
									<span class="ng-scope">加入清单</span>
								</div>
							</button>
							<div class="bk-form-row-txt notice-block">您购买的数据库创建大约需要2分钟,请耐心等待...</div>
						</div>
						<div class="bk-pb4"></div>
					</div>
					<!-- <div class="bk-scope bk-items">
						<div class="bk-items-title">服务专员信息</div>
					</div> -->
					<div class="bk-scope bk-items hidden-xs">
						<div class="bk-items-title">服务专员信息</div>
						<div class="bk-items-list">
							<ul>
								<li><span class="bk-items-item-name">姓名：</span> <span class="bk-items-item-value">周秉政</span></li>
								<li><span class="bk-items-item-name">邮箱：</span> <span class="bk-items-item-value">zhoubingzheng@letv.com</span></li>
								<li><span class="bk-items-item-name">电话：</span> <span class="bk-items-item-value">15901422865</span></li>
								<li><span class="bk-items-item-name">Q  Q：</span> <span class="bk-items-item-value">473877850</span></li>
							</ul>
						</div>
					</div>
					<div class="bk-scope bk-items hidden-sm hidden-md hidden-lg">
						<div class="bk-items-title connect-help"><a class="btn btn-default"><i class="fa fa-comments"></i> 联系服务专员</a></div>
						<div class="bk-items-list hide">
							<ul>
								<li><span class="bk-items-item-name">姓名：</span> <span class="bk-items-item-value">周秉政</span></li>
								<li><span class="bk-items-item-name">邮箱：</span> <span class="bk-items-item-value">zhoubingzheng@letv.com</span></li>
								<li><span class="bk-items-item-name">电话：</span> <span class="bk-items-item-value">15901422865</span></li>
								<li><span class="bk-items-item-name">Q  Q：</span> <span class="bk-items-item-value">473877850</span></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
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
seajs.use("${ctx}/static/page-js/clouddb/dbCreate/main");
</script>

<script type="text/javascript">
scale = function (btn, bar,layer,value,stepLen) {
	this.btn = document.getElementById(btn);
	this.bar = document.getElementById(bar);
	this.layer = document.getElementById(layer);
	this.value = document.getElementById(value);
	/* this.title = document.getElementById(title); */
	/*this.step = this.bar.getElementsByTagName("DIV")[0];*/
	this.stepLen = stepLen
	//this.init();
};
scale.prototype = {
	init: function () {
		var f = this, g = document, b = window, m = Math;
		f.btn.onmousedown = function (e) {
			var x = (e || b.event).clientX;
			var l = this.offsetLeft;
			var max = f.bar.offsetWidth - this.offsetWidth;
			g.onmousemove = function (e) {
				var thisX = (e || b.event).clientX;
				var to = m.min(max, m.max(-2, l + (thisX - x)));
				f.btn.style.left = to +'px';				
				f.ondrag(m.round(m.max(0, to / max) * 100), to);				
				b.getSelection ? b.getSelection().removeAllRanges() : g.selection.empty();
			};
			g.onmouseup = new Function('this.onmousemove=null');
		};
	},
	ondrag: function (pos, x) {
		/* 此处设置步长 */
		if ( x % this.stepLen != 0){
			x = x + this.stepLen - (x % this.stepLen);
		};
		this.step.style.width = Math.max(0, x) + 'px'; 
		this.layer.style.width = Math.max(0, x) + 'px';
		this.btn.style.left = Math.max(0, x) + 'px';
		this.value.value = x + ' ';
	}
}
var stepLen0=5;
var stepLen1=40;
var stepLen2=5;
new scale('btn0', 'bar0','layer0','value0',stepLen0);
new scale('btn1', 'bar1','layer1','value1',stepLen1);
new scale('btn2', 'bar2','layer2','value2',stepLen2);
</script>
</html>
