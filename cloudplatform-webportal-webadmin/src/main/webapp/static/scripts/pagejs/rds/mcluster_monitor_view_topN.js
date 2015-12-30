var split="-";
function refreshChartForSelect(){
    //查询
    var monitorPoint = $('#monitorPointOption').val();
    $('#monitor-view [name="monitor-view"]').each(function(){
            for (var i = 0,len = monitorPoint.length; i < len ; i++){
                if($(this).attr('id') == (monitorPoint[i]+"-monitor-view")){
                    $(this).removeClass('hide');
                    var chart = $("#"+monitorPoint[i]).highcharts();
                    console.info(monitorPoint[i]);
                    setChartData(monitorPoint[i],chart);
                    break
                }
                $(this).addClass('hide');
            }
    });
}
function queryHcluster(){
	$.ajax({
		cache:false,
		type:"get",		
		url:"/hcluster",
		dataType:"json",
		success:function(data){
			if(error(data)) return;
			var hclusterInfo = data.data;
			for(var i=0,len=hclusterInfo.length;i<len;i++){
				var option = $("<option value=\""+hclusterInfo[i].id+"\">"+hclusterInfo[i].hclusterNameAlias+"</option>");
				$(".monitorHclusterOption").append(option);
			}
			initChosen();
			queryMonitorPoint();
		}
	});	
}
function queryMonitorPoint(){
	//getLoading();
	$.ajax({
		cache:false,
		type:"get",		
		url : "/monitor/index/2",
		dataType:"json",
		success:function(data){
			//removeLoading();
			if(error(data)) return;
			var monitor = data.data;
			for(var i=0,len=monitor.length;i<len;i++){
                var monitorPoints = monitor[i].monitorPoint.split(",");
                for(var j=0,len1=monitorPoints.length;j<len1;j++) {
                    var option = $("<option value=\"" + monitor[i].id +split+monitorPoints[j]+ "\">" + monitor[i].titleText +split+ monitorPoints[j]+  "</font></option>");
                    $("#monitorPointOption").append(option);
                    initCharts(monitor[i], monitorPoints[j]);
                }
			}
			initChosen();
		}
	});	
}

function initCharts(data,monitorPoint){
    var id = data.id+split+monitorPoint;
    var viewDemo = $('#monitor-view-demo').clone().removeClass('hide').attr("id",id+"-monitor-view").appendTo($('#monitor-view'));
    var div = $(viewDemo).find('[name="data-chart"]');
    $(div).attr("id",id);
    initChart(div,data.titleText+split+monitorPoint,data.yAxisText,data.tooltipSuffix);
    /*隐藏图表*/
    $('div[name="monitor-view"]').each(function(){
        $(this).addClass("hide");
    });
    var chart = $(div).highcharts();
    //setChartData(id,chart);
    draggable(viewDemo);
}

function initChart(obj,title,ytitle,unit){
    $(obj).highcharts({
        chart: {
            // type: 'areaspline',
            type:'line',
            zoomType: 'x',
            spacingRight: 20
        },
        colors:['#ff66cc','#66ff66','#66ffff','#FFBB33','#C9C','#090','#330000','#CCCC00','#66cc99','#ccff66','#996666','#66cc33'],
        title: {
            text: title
        },
        legend :{
            borderColor: '#000000',
            backgroundColor: '#f9f9f9',
            symbolRadius: '2px',
            borderRadius: '5px',
            itemHoverStyle: {
                Color: '#000000'
            }
        },
        xAxis: {
            type: 'datetime',
            tickPixelInterval:150,
            labels:{
                rotation:0,
                align:'right'
            },
            dateTimeLabelFormats:{
                millisecond: '%H:%M:%S.%L',
                second: '%H:%M:%S',
                minute: '%H:%M',
                hour: '%H:%M',
                day: '%e. %b',
                week: '%e. %b',
                month: '%b \'%y',
                year: '%Y'
            }
        },
        scrollbar:{
	        enabled:true
	    },
        plotOptions: {
        	lineWidth: 0.1,  
            fillOpacity: 0.1,
            // areaspline: {
            //     marker: {
            //         enabled: false,
            //         symbol: 'circle',
            //         radius: 2,
            //         states: {
            //             hover: {
            //                 enabled: true
            //             }
            //         }
            //     }
            // },
            line: {
                marker: {
                    enabled: false,
                    states: {
                        hover: {
                            enabled: true
                        }
                    }
                }
            },
            series:{
            	// lineWidth: 0.5,
            	lineWidth:2,  
                fillOpacity: 0.5,
                states:{
                    hover:{
                        lineWidthPlus:0
                    }
            	}
        	}
        },
        credits:{
            enabled: false
        },
        yAxis: {
            title: {
                text: ytitle
            }
        },
        tooltip: {
            valueSuffix: unit,
            shared: true
        }
    });

} 

function setChartData(indexId,chart){
    var param = indexId.split(split);
	var hclusterId= $('.monitorHclusterOption').val();
	var queryTime= $('#queryTime').val();
	var topN= $('#topN').val();
	if(!queryTime){
		queryTime = 1;
	}
    console.info(indexId);
    chart.showLoading();
    $.ajax({
        cache:false,
        type : "get",
        url : "/monitor/topN/"+hclusterId+"/"+param[0]+"/"+param[1]+"/"+queryTime+"/"+topN,
        dataType : "json",
        contentType : "application/json; charset=utf-8",
        success:function(data){
            chart.hideLoading();
            if(error(data)) return;
            var ydata = data.data;
            for(var i=chart.series.length-1;i>=0;i--){
                chart.series[i].remove(false);
            }
            for(var i=0;i<ydata.length;i++){
                chart.addSeries(ydata[i],false);
            }
            chart.redraw();
        }
    });
}

function draggable(obj){
	 $(obj).sortable({
	        connectWith: '.widget-container-col',
			items:'> .widget-box',
			handle: ace.vars['touch'] ? '.widget-header' : false,
			cancel: '.fullscreen',
			opacity:0.8,
			revert:true,
			forceHelperSize:true,
			placeholder: 'widget-placeholder',
			forcePlaceholderSize:true,
			tolerance:'pointer',
			disabled:true,
			start: function(event, ui) {
				ui.item.parent().css({'min-height':ui.item.height()})
			},
			update: function(event, ui) {
				ui.item.parent({'min-height':''})
			}
	    });
}

function changeDraggable(obj){
	var dgable = $(obj).find('input').val();
	if(dgable == '1'){
		$(obj).closest('[name="monitor-view"]').sortable('disable');
		$(obj).find('input').val(0);
		$(obj).find('i').attr("style","-webkit-transform:rotate(45deg);-moz-transform:rotate(45deg);-o-transform:rotate(45deg);");
	}else{
		$(obj).closest('[name="monitor-view"]').sortable('enable');
		$(obj).find('input').val(1);
		$(obj).find('i').attr("style","-webkit-transform:rotate(90deg);-moz-transform:rotate(90deg);-o-transform:rotate(90deg);");
	}
}

function updateChartSize(obj){
	 setTimeout(function () { 
		 $(obj).closest('.widget-box').find('[name="data-chart"]').highcharts().reflow();
	    }, 1);
}

$(function(){
	$('#nav-search').addClass("hidden");
	queryHcluster();
});
