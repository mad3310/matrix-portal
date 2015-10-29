/**
 * Created by yaokuo on 2014/12/14.
 */
define(function(require,exports,module){
    var $ = require('jquery');
    var common = require('../../common');
    var cn = new common();
    
    var DataHandler = function(){
    };

    module.exports = DataHandler;

    DataHandler.prototype = {
    	/*
    	 * 相关资源量赋值
    	 */
        resCountHandler : function(data){
            if(cn.Displayable(data.data.status)){
                $("#gceName").html(data.data.gceName);
                $("#gceStatus").html("("+cn.TranslateStatus(data.data.status)+")");
                var logId =  data.data.logId;
                if(logId) {
                		/*cn.PostData( "/gce/log/url", {logId:logId}, function (data) {
                			if(data.data) {
                				$("#logUrl").attr("src",data.data);
                			} else {
                				$("#logUrlInst").html("镜像中未配置");
                			}
                		});*/
                } else {
                	$("#logUrlInst").html("镜像中未配置");
                }
            }else{
               // location.href = '/list/gce';
            }
        }
    }
});