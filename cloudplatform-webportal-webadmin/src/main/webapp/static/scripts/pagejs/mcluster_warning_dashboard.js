$(function () {
	initPieChart('monitorCluster',1);
	initPieChart('monitorNode',2);
	initPieChart('monitorDb',3);
});

function initPieChart(divName,type){	
    $('#' + divName).highcharts({
        chart: {
            type: 'pie',
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        tooltip: {
	        formatter: function() {
	            return '<b>'+ this.point.name +'</b>: '+ this.point.y;
	        } 
        },
        title: {
       	 	text:''
        },
        plotOptions: {
        	 pie: {
                 allowPointSelect: true,
                 cursor: 'pointer',
                 dataLabels: {
                     enabled: false
                 },
                 showInLegend: true,
                 dataLabels: {
                     enabled: true,
                     format: '{point.name}:{point.y}'
                 }
             },
            series: {
                cursor: 'pointer',
                events: {
                    click: function(e) {
		                location.href = e.point.url;
					}
				}
            }
        },
        credits:{
        	enabled: false
        },
        loading: {
            hideDuration: 100,
            showDuration: 100,
            style: {
            	position: 'absolute',
            	backgroundColor: 'white',
            	opacity: 0.5,
            	textAlign: 'center'
            },
            labelStyle:{
           	 "fontWeight": "bold",
           	 "position": "relative", 
           	 "top": "45%",
           	 "fontSize":"12px"
           }
        }
    });
    setPieChartData(divName,type);
}

function setPieChartData(divName,type){
	var chart = $('#' + divName).highcharts();
    chart.showLoading();
	var url = "/list/mcluster/monitor/" + type;		
	$.ajax({ 
		cache:false,
		type : "get",
		url : "/dashboard/monitor/" + type,
		dataType : "json", 
		contentType : "application/json; charset=utf-8",
		success : function(data) {			
			if(error(data)) return;
			var status = data.data;
			var pieChartData=[{
				data: []
	           }];
			
			pieChartData[0].data.push({name:'正常',color:'green',y:status.nothing,url:url});
			pieChartData[0].data.push({name:'单节点故障',color:'#19A2A2',y:status.general,url:url});
			pieChartData[0].data.push({name:'危险',color:'#FDC43E',y:status.serious,url:url});
			pieChartData[0].data.push({name:'集群不可用',color:'#D15A06',y:status.crash,url:url});
			pieChartData[0].data.push({name:'获取数据超时',color:'#CC0032',y:status.timeout,url:url});
			pieChartData[0].data.push({name:'解析数据错误',color:'#CC0032',y:status.except,url:url});
			
			$('#'+divName+'-nothing').html(status.nothing);
			$('#'+divName+'-general').html(status.general);
			$('#'+divName+'-serious').html(status.serious);
			$('#'+divName+'-crash').html(status.crash);
			$('#'+divName+'-timeout').html(status.timeout);
			$('#'+divName+'-except').html(status.except);
			
			if(chart.series.length != 0){
				chart.series[0].remove(false);
			}
			chart.hideLoading();
			chart.addSeries(pieChartData[0],false);
			chart.redraw();
		}
	});
}


function updateMclusterChart(divName,type){
	setPieChartData(divName,type);
}
