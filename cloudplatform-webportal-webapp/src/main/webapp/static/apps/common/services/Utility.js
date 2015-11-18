/**
 * Created by jiangfei on 2015/8/19.
 */
define(['./common.service'],function (serviceModule) {
  serviceModule.factory('Utility', ['$timeout',
    function ($timeout) {
      var service = {};
      service.getRzSliderHack=function(scope){
         return function(){
           $timeout(function() {
             scope.$broadcast('reCalcViewDimensions');
           });
         };
      };

      service.delaySliderModel = function () {
        var delayQueue = [],
          timeoutPromise = null;
        return function (value, onTimeout) {
          if (delayQueue.length == 0) {
            timeoutPromise = $timeout(function () {
              onTimeout(delayQueue[delayQueue.length - 1]);
              delayQueue.splice(0, delayQueue.length);
            }, 1500);
          }
          delayQueue.push(value);
        };

      };
      service.setOperationBtns=function($scope,objList,productInfo,operationArry,Config){
          var type=productInfo.type;
          var state=productInfo.state;
          var otheraffect=productInfo.other;
          var operaArraytemp=productInfo.operations;
          var operationArraycopy=[];
          var othertemp=1;
          for(var i in objList){
            operationArry[i]=[];
            if(objList[i].checked){
              var objtemp=objList[i];
              for(var j in operaArraytemp){
                if(otheraffect.length>0){//其他影响因素
                  for(var k in otheraffect){
                    if(objtemp[otheraffect[k]].length>0){
                      operationArry[i][j]=Config.statusOperations[type][objtemp[state]][operaArraytemp[j]]*Config.statusOperations[type][otheraffect[k]][operaArraytemp[j]];
                    }else{
                      operationArry[i][j]=Config.statusOperations[type][objtemp[state]][operaArraytemp[j]]*Config.statusOperations[type][otheraffect[k]+'null'][operaArraytemp[j]];
                    }
                  }
                }else{//无其他因素影响
                  operationArry[i][j]=Config.statusOperations[type][objtemp[state]][operaArraytemp[j]];
                }
                // operationArry[i][j]=Config.statusOperations[type][objList[i][state]][operaArraytemp[j]];
                operationArraycopy[j]=1;
              }
            }else{
              operationArry[i]=[1,1,1,1,1,1,1,1,1,1,1,1]
            }   
          }
          for(var i in operationArry){//多记录状态叠加
            for(var j in operationArry[i]){
              operationArraycopy[j]=operationArraycopy[j]*operationArry[i][j];
            }
          }
          return operationArraycopy
      };
      return service;
    }]);
});