/**
 * Created by yaokuo on 2014/12/12.
 * dblist page
 */
define(function(require){
	var pFresh,iFresh;
    var common = require('../../common');
    var cn = new common();
    
    cn.Tooltip();
    cn.initNavbarMenu([{
			name : "开放存储服务 OSS",
			herf : "/list/oss"
		}]);

    /*加载数据*/
    var dataHandler = require('./dataHandler');
    var dbListHandler = new dataHandler();
    /*
     * 初始化数据
     */
	asyncData();
	
	$("#search").click(function() {
		cn.currentPage = 1;
		asyncData();
	});
	$("#refresh").click(function() {		
		asyncData();
	});
	$("#name").keydown(function(e){
		if(e.keyCode==13){
			e.preventDefault();
			cn.currentPage = 1;
			asyncData();
		}
	});
	$("#tby").click(function(e){
		e = e? e:window.event;
		var target = e.target || e.srcElement;
		var ossId = $(target).attr("oss-id");
		if(ossId != undefined && ossId != null){
			cn.DelFolderModal('删除Object','您确定要删除该文件夹 及 文件夹下的全部内容吗?');
			$('#delfolder').unbind('click').click(function(event) {
				$('#DelFolderModal').modal('hide').remove();
				var url = "/oss/"+ossId;
				cn.DeleteData(url,asyncDataHander);
			});
		}
	})
	
	/*初始化按钮*/
	$(".btn-region-display").click(function(){
		$(".btn-region-display").removeClass("btn-success").addClass("btn-default");
		$(this).removeClass("btn-default").addClass("btn-success");
		$("#name").val("");
		asyncData();
	})
	
	/*
	 * 可封装公共方法 begin
	 */
	//初始化分页组件
	$('#paginator').bootstrapPaginator({
		size:"small",
    	alignment:'right',
		bootstrapMajorVersion:3,
		numberOfPages: 1,
		onPageClicked: function(e,originalEvent,type,page){
			cn.currentPage = page;
        	asyncData(page);
        }
	});
	//初始化checkbox
	$(document).on('click', 'th input:checkbox' , function(){
		var that = this;
		$(this).closest('table').find('tr > td:first-child input:checkbox')
		.each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});
	$(document).on('click', 'tfoot input:checkbox' , function(){
		var that = this;
		$(this).closest('table').find('tr > td:first-child input:checkbox,th input:checkbox ')
		.each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});
	/*
	 * 可封装公共方法 end
	 */
	
	// setInterval(asyncData,5000);

	//加载列表数据
	function asyncData(page) {
		var name = $("#name").val(),location = $("#location").val();
		if(!page) page = cn.currentPage;
		var url = "/oss?currentPage=" + page +"&&recordsPerPage=" + cn.recordsPerPage + "&&name=" + name + "&&location=" + location;
		// cn.GetData(url,dbListHandler.DbListHandler);
		cn.GetData(url,refreshData);
	}
	function refreshData(data) {
		dbListHandler.DbListHandler(data);
		var interval=$('#waitTime').val();
		if(iFresh){
			clearInterval(iFresh);
		}
		iFresh=setInterval(asyncData,interval)
	}
	function asyncDataHander(){
		asyncData();
	}
});
