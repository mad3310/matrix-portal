<%@ page language="java" pageEncoding="UTF-8"%>
<div class="page-content-area">
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->

		<!-- #section:pages/error -->
		<div class="error-container">
			<div class="well">
				<h1 class="grey lighter smaller">
					<span class="blue bigger-125">
						<i class="ace-icon fa fa-sitemap"></i>
						404
					</span>
					页面错误
				</h1>

				<hr>
				<h3 class="lighter smaller">无法载入页面!</h3>

				<div>
					<div class="space"></div>
					<h4 class="smaller">尝试如下动作:</h4>

					<ul class="list-unstyled spaced inline bigger-110 margin-15">
						<li>
							<i class="ace-icon fa fa-hand-o-right blue"></i>
							重新打开页面
						</li>
						<li>
							<i class="ace-icon fa fa-hand-o-right blue"></i>
							联系我们
						</li>
					</ul>
				</div>

				<hr>
				<div class="space"></div>

				<div class="center">
					<a href="javascript:history.back()" class="btn btn-grey">
						<i class="ace-icon fa fa-arrow-left"></i>
						返回
					</a>

					<a href="${ctx}/mcluster/list" class="btn btn-primary">
						<i class="ace-icon fa fa-tachometer"></i>
						首页
					</a>
				</div>
			</div>
		</div>

		<!-- /section:pages/error -->

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->
</div>