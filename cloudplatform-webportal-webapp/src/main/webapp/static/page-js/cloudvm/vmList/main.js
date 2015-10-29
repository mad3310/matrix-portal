/**
 * Created by yaokuo on 2014/12/12.
 * dblist page
 */
define(function(require){
	var pFresh,iFresh;
    var common = require('../../common');
    var cn = new common();
    
    cn.Tooltip();
     /* 初始化navbar-header-menu */
	cn.initNavbarMenu([{name : "云主机",herf : "/list/vm",isActive:true},
	                   {name : "磁盘",herf : "/list/vm/disk"}]);
    
	/*禁用退格键退回网页*/
	window.onload=cn.DisableBackspaceEnter();
	
    /*加载数据*/
    var dataHandler = require('./dataHandler');
    var vmListHandler = new dataHandler();
    var currentPage=1;
    var recordsPerPage=3;	
    var vmNameOfSearch='';
	
	/*
	 * 可封装公共方法 begin
	 */
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
    /*按钮组件封装 --begin*/
	$(document).on('click', '.region-city-list button' , function(e){
		e.preventDefault();
        if(!$(this).hasClass("btn-success")){
            $(this).parent().find("button").removeClass("btn-success");
            $(this).addClass("btn-success");
            if($(this).parent().find("input:hidden").length > 0 ){
                var val = $(this).val();
                var hiddenInput=$(this).parent().find("input:hidden");
                if(val!==hiddenInput.val()){
                    hiddenInput.val(val);
                    hiddenInput.trigger('change');
                }	
            }
        }
	});
	/*按钮组件封装 --end*/
    $('#search').on('click',function(e){
    	var vmName=$('#vmName').val();
    	vmNameOfSearch=vmName;
    	asyncData();
    });
	$("#vmName").keydown(function(e){
		if(e.keyCode==13){
			e.preventDefault();
			currentPage = 1;
			asyncData();
		}
	});
	//初始化分页组件
	$('#paginator').bootstrapPaginator({
		size:"small",
    	alignment:'right',
		bootstrapMajorVersion:3,
		numberOfPages: 3,
		onPageClicked: function(e,originalEvent,type,page){
			currentPage = page;
        	asyncData();
        }
	});

	
	$("#refresh").click(function() {		
		asyncData();
	});	
	
	/*
	 * 可封装公共方法 end
	 */
    $(".region-city-list input").change(function (){
    	var flavorRam= $(this).val();
    	asyncData();
    });
    $('.batch-operate').on('click',function(e){
    	var requestData=getRequestData();
    	if(!requestData.length) return;
    	
    	var title = "确认";
        var text = '';
        var operationUrl='';
        var operationCallback=null;
        var doOperation=function(){
            cn.DialogBoxInit(title,text,function(){
                if($('body').find('.sidebar').length==0){   //layout界面不添加loading
                    $('body').append("<div class=\"spin\"></div>");
                    $('body').append("<div class=\"far-spin\"></div>");
                 }
                console.log(requestData);
        		cn.PostData(operationUrl,{vms:JSON.stringify(requestData)},function(data){
                    $('body').find('.spin').remove();
                    $('body').find('.far-spin').remove();
        	    	operationCallback(data);
        		});                	
            });
		};
		
		var operationType=$(e.currentTarget).attr('id');
		switch(operationType){
    		case 'batch_start':
    			text='您确定要开始这些虚拟机吗？';
    			operationUrl='/ecs/vm-batch-start';
    			operationCallback=function(data){
    				if(data.result==1){
        				asyncData();	
    				}
    				else{
    					cn.alertoolDanger(data.msgs[0]);
    				}
        		};
        		doOperation();
        		break;
    		case 'batch_stop':
    			text='您确定要停止这些虚拟机吗？';
    			operationUrl='/ecs/vm-batch-stop';
    			operationCallback=function(data){
    				cn.alertoolSuccess("虚拟机已停止");
    				asyncData();
        		};
        		doOperation();
        		break;
        	default:
        		break;
		}
    	
    });
    
    /*
     * 初始化数据
     */
    initComponents();
	
	//加载列表数据
	function asyncData() {
		var currentRegion=vmListHandler.getSelectedRegion();
		var vmName=$('#vmName').val();
		var baseUrl = '/ecs/region';	
		var queryParams="?currentPage=" + currentPage +"&&recordsPerPage=" + recordsPerPage+ "&&name=" + vmName;
		var url = currentRegion!='All' ? baseUrl+'/'+currentRegion+queryParams:baseUrl+queryParams;
		cn.GetData(url,refreshCtl);
	}
	function refreshCtl(data) {
		vmListHandler.VmListHandler(data);
        $('.vm-operation').each(function(index,element){
        	$(element).on('click',function(e){
        		var vmId= $(e.currentTarget.closest('tr')).find('input:checkbox[name=vm_id]').val();
        		var fieldRegion= $(e.currentTarget.closest('tr')).find('.field-region').val();
        		var operationType=$(e.currentTarget).attr('class').split(' ')[1];
        		vmListHandler.operateVm(vmId,fieldRegion,operationType,asyncData);
        	});
        });
		if ($(".vm-building").length == 0){
			if(pFresh){
				clearInterval(pFresh);
			}
			if(iFresh){
				clearInterval(iFresh);
			}
			iFresh = setInterval(asyncData,cn.dbListRefreshTime);
		}else{
			asyncProgressData();
			if(iFresh){
				clearInterval(iFresh);
			}
			if(pFresh){
				clearInterval(pFresh);
			}
			pFresh = setInterval(asyncProgressData,10000);
		}
		$('th input:checkbox').prop('checked', false);
		$('tfoot input:checkbox').prop('checked', false);
		
	}	
	 /*进度条数据刷新*/
	function asyncProgressData(){
		$("input[name = progress_vm_id]").each(function(){
			var vmId = $(this).val();
			var fieldRegion=$($(this).closest('tr')).find('.field-region').val();
			function progress_func(data){
				vmListHandler.progress(vmId,data,asyncData);
			}
			var url = '/ecs/region/'+fieldRegion+'/vm/' + vmId;
			cn.GetLocalData(url,progress_func);
		});
	}
    function initComponents(){
    	initRegionSelector();
    }
    function initRegionSelector(){
    	var url='/ecs/regions/group';
		return cn.GetData(url,vmListHandler.initRegionSelectorHandler);
    }
    
    function getRequestData(){
    	var vms=[];
    	$('input:checkbox:checked[name=vm_id]').each(function(index,element){
    		var region=$(element).closest('tr').find('.field-region').val();
    		vms.push({vmId:$(element).val(),region:region});
    	});
    	return vms;
    }

});
