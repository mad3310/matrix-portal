define(function(require){
    var common = require('../../common');
    var cn = new common();
    
    cn.Tooltip();
    cn.initNavbarMenu([{
				name : "负载均衡SLB",
				herf : "/list/slb"
			}]);

    /*加载数据*/
	/*加载数据*/
	var dataHandler = require('./dataHandler');
	var slbDataHandler = new dataHandler();
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
	$("#slbName").keydown(function(e){
		if(e.keyCode==13){
			e.preventDefault();
			cn.currentPage = 1;
			asyncData();
		}
	});
	
	/*初始化按钮*/
	$(".btn-region-display").click(function(){
		$(".btn-region-display").removeClass("btn-success").addClass("btn-default");
		$(this).removeClass("btn-default").addClass("btn-success");
		$("#dbName").val("");
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
		numberOfPages: 5,
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

	setInterval(asyncData,cn.dbListRefreshTime);
	//加载列表数据
	function asyncData(page) {
		var slbName = $("#slbName").val();
		if(!page) page = cn.currentPage;
		//var url = "/static/page-js/cloudslb/gceList/data.json";
		url = "/slb?currentPage=" + page +"&&recordsPerPage=" + cn.recordsPerPage + "&&slbName=" + slbName;
		cn.GetData(url,slbDataHandler.SlbListHandler);

	}
});
