var currentPage = 1; //第几页 
var recordsPerPage = 15; //每页显示条数
	
$(function(){
	//初始化 
	page_init();
	//chosen-select 组件兼容 2015-06-16
	if(!IsPC()){
		$('.chosen-select').removeClass('chosen-select');
	}
	$(document).on('click', 'th input:checkbox' , function(){
		var that = this;
		$(this).closest('table').find('tr > td:first-child input:checkbox')
		.each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});
});	
function queryByPage() {
	var queryCondition = {
			'currentPage':currentPage,
			'recordsPerPage':recordsPerPage
		}
	
	$("#tby tr").remove();
	getLoading();
	$.ajax({
		cache:false,
		type : "get",
		url : queryUrlBuilder("/gce/image",queryCondition),
		dataType : "json", /*这句可用可不用，没有影响*/
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var array = data.data.data;
			var tby = $("#tby");
			var totalPages = data.data.totalPages;
			
			for (var i = 0, len = array.length; i < len; i++) {
				var td1 = $("<td class=\"center\">"
							+"<label class=\"position-relative\">"
							+"<input name=\"gce_image_id\" value= \""+array[i].id+"\" type=\"checkbox\" class=\"ace\"/>"
							+"<span class=\"lbl\"></span>"
							+"</label>"
							+"</td>");
				var td2 = $("<td class=\"td-name\" >"
						+array[i].name
						+ "</td>");
				var td3 = $("<td class=\"td-type hidden-480\" >"
						+ array[i].type
						+ "</td>");
				var td4 = $("<td class=\"td-tag\" >"
						+ array[i].tag
						+ "</td> ");
				var td5 = $("<td class=\"td-url hidden-480\" >"
						+ array[i].url
						+ "</td>");
				var td6 = $("<td class=\"td-netType hidden-480\" >"
						+ array[i].netType
						+ "</td>");
				var td7 = $("<td class=\"td-status\" >"
						+ array[i].status
						+ "</td>");
				var td8= $("<td class=\"td-username hidden-480\" user-id=\""+array[i].owner+"\"> - </td>");
				if(array[i].createUserModel != undefined && array[i].createUserModel != null ){
					td8.html(array[i].createUserModel.userName);
				}
				var td9 = $("<td>"
						+"<div class=\"hidden-sm hidden-xs  action-buttons\">"
						+"<a class=\"green\"  title=\"修改\" style=\"cursor:pointer\" data-toggle=\"tooltip\" data-placement=\"right\">"
						+"<i class=\"ace-icon fa fa-pencil bigger-120\" data-toggle=\"modal\" data-target=\"#modify-image-modal\"></i>"
						+"</a>"
						+"<a class=\"red\" onclick=\"delGceImage(this)\" style=\"cursor:pointer\" onfocus=\"this.blur();\"  title=\"删除\" data-toggle=\"tooltip\" data-placement=\"right\">"
						+"<i class=\"ace-icon fa fa-trash-o bigger-120\"></i>"
						+"</a>"
						+"<a class=\"blue\" onclick=\"showGceImagePushModal(this);\" style=\"cursor:pointer\" onfocus=\"this.blur();\"  title=\"上传镜像\" data-toggle=\"tooltip\" data-placement=\"right\">"
						+"<i class=\"ace-icon fa fa-upload bigger-120\"></i>"
						+"</a>"
						+"</div>"
						+'<div class="hidden-md hidden-lg">'
							+'<div class="inline pos-rel">'
							+'<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto">'
								+'<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>'
							+'</button>'
							+'<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">'
								+'<li>'
									+'<a class=\"green\" title=\"修改\" data-toggle=\"tooltip\" data-placement=\"right\">'
										+'<span class="blue">'
											+'<i class="ace-icon fa fa-pencil bigger-120" data-toggle=\"modal\" data-target=\"#modify-image-modal\"></i>'
										+'</span>'
									+'</a>'
								+'</li>'
								+'<li>'
									+'<a class=\"red\" href=\"#\" onclick=\"delGceImage(this)\" onfocus=\"this.blur();\" title=\"删除\" data-toggle=\"tooltip\" data-placement=\"right\">'
										+'<span class="red">'
											+'<i class="ace-icon fa fa-trash-o bigger-120"></i>'
										+'</span>'
									+'</a></li>'
									+'<li>'
								+'<li>'
									+'<a class=\"blue\" href=\"#\" onclick=\"showGceImagePushModal(this);\" onfocus=\"this.blur();\" title=\"上传镜像\" data-toggle=\"tooltip\" data-placement=\"right\">'
										+'<span class="blue">'
											+'<i class="ace-icon fa fa-upload bigger-120"></i>'
										+'</span>'
									+'</a></li>'
							+'</ul></div></div>'
						+"</td>");
				var tr = $("<tr></tr>");
				
				tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7).append(td8).append(td9);
				tr.appendTo(tby);
			}//循环json中的数据 
			
			/*初始化tooltip*/
			$('[data-toggle = "tooltip"]').tooltip();
			initModify();
			
			if (totalPages <= 1) {
				$("#pageControlBar").hide();
			} else {
				$("#pageControlBar").show();
				$("#totalPage_input").val(totalPages);
				$("#currentPage").html(currentPage);
				$("#totalRows").html(data.data.totalRecords);
				$("#totalPage").html(totalPages);
			}
		},
		error : function(XMLHttpRequest,textStatus, errorThrown) {
			error(XMLHttpRequest);
			return false;
		}
	});
	}
   

function pageControl() {
	// 首页
	$("#firstPage").bind("click", function() {
		currentPage = 1;
		queryByPage();
	});

	// 上一页
	$("#prevPage").click(function() {
		if (currentPage == 1) {
			$.gritter.add({
				title: '警告',
				text: '已到达首页',
				sticky: false,
				time: '5',
				class_name: 'gritter-warning'
			});
	
			return false;
			
		} else {
			currentPage--;
			queryByPage();
		}
	});

	// 下一页
	$("#nextPage").click(function() {
		if (currentPage == $("#totalPage_input").val()) {
			$.gritter.add({
				title: '警告',
				text: '已到达末页',
				sticky: false,
				time: '5',
				class_name: 'gritter-warning'
			});
	
			return false;
			
		} else {
			currentPage++;
			queryByPage();
		}
	});

	// 末页
	$("#lastPage").bind("click", function() {
		currentPage = $("#totalPage_input").val();
		queryByPage();
	});
}

	function searchAction(){
		$('#nav-search-input').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	queryByPage();
	        }
	    });
	}
	
function formValidate() {
	$("#add-gce-image-form").bootstrapValidator({
	  message: '无效的输入',
         feedbackIcons: {
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
         }
     }).on('success.form.bv', function(e) {
    	 e.preventDefault();
    	addImage();
     });
}
function updateFormValidate() {
	$("#modify-gce-image-form").bootstrapValidator({
	  message: '无效的输入',
         feedbackIcons: {
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
         }
     }).on('success.form.bv', function(e) {
    	 e.preventDefault();
    	updateImage();
     });
}
function getUser(){
	var select = $("#owner");
	$.ajax({
    		cache:false,
    		type : "get",
    		url : "/gce/user",
    		success : function(data) {
    			var users = data.data;
    			for (var i=0,len = users.length;i < len; i++){
    				var option =$("<option value=\""+users[i].id+"\">"+users[i].userName+"</option>");
    				option.appendTo(select);
    			}
    			initChosen("","272px");
    		}
    	})
}
function getModifyUser(ownerId){
	var select = $("#modify-owner");
	$.ajax({
    		cache:false,
    		type : "get",
    		url : "/gce/user",
    		success : function(data) {
    			var users = data.data;
    			for (var i=0,len = users.length;i < len; i++){
    				if(users[i].id != ownerId){
    					var option =$("<option value=\""+users[i].id+"\">"+users[i].userName+"</option>");
    				}else{
    					var option =$("<option value=\""+users[i].id+"\" selected=\"selected\">"+users[i].userName+"</option>");
    				}
    				option.appendTo(select);
    			}
    			initChosen("","272px");
    		}
    	})
}

function initModify(){
	$(".fa-pencil").click(function(){
		var tr = $(this).closest("tr");
		var trData={
		 		id : tr.find("input").val(),
		 		name : tr.find(".td-name").html(),
		 		url : tr.find(".td-url").html(),
		 		netType : tr.find(".td-netType").html(),
		 		tag : tr.find(".td-tag").html(),
		 		status : tr.find(".td-status").html(),
		 		type : tr.find(".td-type").html(),
		 		descn : tr.find(".td-descn").html(),
		 		owner : tr.find(".td-username").attr("user-id")
		}
		
		getModifyUser(trData.owner);
		$('#modify-imageId').val(trData.id);
		$('#modify-name').val(trData.name);
    	$('#modify-type').val(trData.type);
    	$('#modify-descn').val(trData.descn);
    	$('#modify-tag').val(trData.tag);
    	$('#modify-url').val(trData.url);
    	$('#modify-netType').val(trData.netType);
    	$('#modify-status').val(trData.status);
	})
}
function addImage(){
$.ajax({
    		cache:false,
    		type : "post",
    		url : "/gce/image",
    		data: {
    			name:$('#name').val(),
    			type:$('#type').val(),
    			descn:$('#descn').val(),
    			tag:$('#tag').val(),
    			url:$('#url').val(),
    			status:$('#status').val(),
    			owner:$('#owner').val(),
    			netType:$('#netType').val()
    		},
    		success : function(data) {
    			location.href = "/list/gce/image";
    		}
    	})
}
function updateImage(){
	$.ajax({
    		cache:false,
    		type : "post",
    		url : "/gce/image/"+$('#modify-imageId').val(),
    		data: {
    			id:$('#modify-imageId').val(),
    			name:$('#modify-name').val(),
    			type:$('#modify-type').val(),
    			descn:$('#modify-descn').val(),
    			tag:$('#modify-tag').val(),
    			url:$('#modify-url').val(),
    			netType:$('#modify-netType').val(),
    			status:$('#modify-status').val(),
    			owner:$('#modify-owner').val()
    		},
    		success : function(data) {
    			location.href = "/list/gce/image";
    		}
    	})
}

function delGceImage(obj){
	var gceImageId = $(obj).closest("tr").find("input").val();
	function delCmd(){
		$.ajax({
			cache:false,
			type : "delete",
			url : "/gce/image/"+gceImageId,
			success : function(){
				location.href = "/list/gce/image";
			}
		})
	}
	confirmframe("删除镜像","删除"+$(obj).closest("tr").find("td:eq(1)").html()+"后可重新添加","您确定要删除?",delCmd);
}

function initGceImagePushModal(){
	$('#modalGceImagePush').modal({
		  keyboard: false,
		  show:false,
		  backdrop:'static'
		});
}

function initGceImagePushHclusterSelector(){
	var selectHclusterElement=$('#selectHcluster');
	selectHclusterElement.chosen();
	$.ajax({
		cache:false,
		type : 'get',
		url : '/hcluster',
	}).then(function(data){
		if(data && data.data && data.data.length){
			var optionHtml=data.data.map(function(option){
				return '<option value="'+option.id+'">'+option.hclusterName+'</option>';
			}).join('');
			selectHclusterElement.html(optionHtml);
			selectHclusterElement.trigger("chosen:updated");
		}
	});
}

function formGceImagePushValidate() {
	$("#formGceImagePush").bootstrapValidator({
	  message: '无效的输入',
         feedbackIcons: {
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
         }
     }).on('success.form.bv', function(e) {
    	 e.preventDefault();
    	 pushGceImage();
     });
}

function enableGceImagePushForm(){
	//$("#formGceImagePush").data('formValidation').resetForm();
	$('#formGceImagePush button[type=submit]').prop('disabled', false);
}


function showGceImagePushModal(obj){
	var gceImageId = $(obj).closest("tr").find("input").val();
	$('#hidInputGceImagePushId').val(gceImageId);
	$('#selectHcluster').val('').trigger('chosen:updated');
	$('#modalGceImagePush').modal('show');
}


function pushGceImage(){
	var gceImageId = $('#hidInputGceImagePushId').val();
	var hClusterIds = $('#selectHcluster').val();
	if(!hClusterIds || !hClusterIds.length){
		$.gritter.add({title: '警告',text: '请选择物理机集群',sticky: false,time: '2000',class_name: 'gritter-warning'});
		enableGceImagePushForm();
		return;
	}
	$.gritter.add({title: '提示',text: '镜像上传中，请稍后。',sticky: false,time: '2000',class_name: 'gritter-info'});
	$.ajax({
		cache:false,
		type : "post",
		url : "/gce/image/push/"+gceImageId,
		data:{hclusterIds: hClusterIds.join(',')},
		success : function(data){
			if(data.result===1){
				$.gritter.add({title: '提示',text: data.msgs[0],sticky: false,time: '2000',class_name: 'gritter-info'});
				setTimeout(function(){
					location.href = "/list/gce/image";
				}, 2000);
			}
			else{
				$.gritter.add({title: '错误',text: data.msgs[0] || '上传镜像出错',sticky: false,time: '2000',class_name: 'gritter-error'});
				enableGceImagePushForm();
			}
			
		}
	});
	
}

function page_init(){
	$('[name = "popoverHelp"]').popover();
	queryByPage();
	formValidate();
	updateFormValidate();
	pageControl();
	 getUser();
	 formGceImagePushValidate();
	 initGceImagePushModal();
	 initGceImagePushHclusterSelector();
}
