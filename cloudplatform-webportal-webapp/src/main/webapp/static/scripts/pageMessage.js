/**
 * 
 */

function pageMessage(type,context) {
	var htmlContext;
	if(type == "warning"){
		htmlContext = "<p class=\"bg-warning\" style=\"color:red;font-size:16px;\"><strong>警告!</strong>"+context+"</p>"
	}else if(type == "success"){
		htmlContext = "<p class=\"bg-success\" style=\"color:red;font-size:16px;\"><strong>成功!</strong>"+context+"</p>"
	}else if(type == "danger"){
		htmlContext = "<p class=\"bg-danger\" style=\"color:red;font-size:16px;\"><strong>失败!</strong>"+context+"</p>"
	}else{
		htmlContext = "<p class=\"bg-info\" style=\"color:red;font-size:16px;\"><strong>信息!</strong>"+context+"</p>"
	}
	return htmlContext;
}
