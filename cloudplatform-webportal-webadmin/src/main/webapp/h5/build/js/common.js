//测试开关
var keyTestBySelf = function(){
/*    var token= AppObj.getToken();
    resquest.setRequestHeader('authtoken',token);
    resquest.setRequestHeader('clientType','android');*/
}
	
	
var myScroll;
// var items_per_page=7,scroll_in_progress=false;
var Common=function(){
	this.s={};
	this.s.current_page=1;
	this.s.items_per_page=7;
	this.s.scroll_in_progress=false;
};
Common.prototype={
	scrollInite:function(refresh,initePage){

		var that=this;
		var pullDownAction=function(){
		    initePage('refresh');
		    $('#wrapper > #scroller > ul').data('page', 1);
		    // Since "topOffset" is not supported with iscroll-5
		    $('#wrapper > .scroller').css({top:0});
		}
		var pullUpAction=function(callback) {
		    that.s.current_page++;
		    initePage('refresh',that.s.current_page);
		    if (callback) {
		        callback();
		    }
		}
		var pullActionCallback=function(){
		    if (pullDownEl && pullDownEl.className.match('loading')) {
		        pullDownEl.className = 'pullDown';
		        pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉加载';
		        myScroll.scrollTo(0, parseInt(pullUpOffset)*(-1), 60);
		    } else if (pullUpEl && pullUpEl.className.match('loading')) {
		        $('.pullUp').removeClass('loading').html('');
		    }
		}
		var pullActionDetect = {
		    count:0,
		    limit:10,
		    check:function(count) {
		        if (count) {
		            pullActionDetect.count = 0;
		        }
		        // Detects whether the momentum has stopped, and if it has reached the end - 200px of the scroller - it trigger the pullUpAction
		        setTimeout(function() {
		            if (myScroll.y <= (myScroll.maxScrollY + 60) && pullUpEl && !pullUpEl.className.match('loading')) {
		                $('.pullUp').addClass('loading').html('<span class="pullUpIcon"></span><span class="pullUpLabel">加载中...</span>');
		                pullUpAction();
		            } else if (pullActionDetect.count < pullActionDetect.limit) {
		                pullActionDetect.check();
		                pullActionDetect.count++;
		            }
		        }, 200);
		    }
		}

		var trigger_myScroll=function(offset) {
		    pullDownEl = document.querySelector('#wrapper .pullDown');
		    if (pullDownEl) {
		        pullDownOffset = pullDownEl.offsetHeight;
		    } else {
		        pullDownOffset = 0;
		    }
		    pullUpEl = document.querySelector('#wrapper .pullUp');  
		    if (pullUpEl) {
		        pullUpOffset = pullUpEl.offsetHeight;
		    } else {
		        pullUpOffset = 0;
		    }
		    
		    if ($('#wrapper ul > li').length < that.s.items_per_page) {
		        // If we have only 1 page of result - we hide the pullup and pulldown indicators.
		        $('#wrapper .pullDown').hide();
		        $('#wrapper .pullUp span').hide();
		        offset = 0;
		    } else if (!offset) {
		        // If we have more than 1 page of results and offset is not manually defined - we set it to be the pullUpOffset.
		        offset = pullUpOffset;
		    }
		    
		    myScroll = new IScroll('#wrapper', {
		        probeType:1,
		        tap:true,
		        click:false,
		        preventDefaultException:{tagName:/.*/},
		        mouseWheel:true,
		        scrollbars:true,
		        fadeScrollbars:true,
		        interactiveScrollbars:false,
		        keyBindings:false,
		        deceleration:0.0002,
		        startY:(parseInt(offset)*(-1))
		    });
		    myScroll.on('scrollStart', function () {
		        that.s.scroll_in_progress = true;
		    });
		    myScroll.on('scroll', function () {
		        that.s.scroll_in_progress = true;
		        
		        if ($('#wrapper ul > li').length >=that.s.items_per_page) {
		            if (this.y >= 5 && pullDownEl && !pullDownEl.className.match('flip')) {
		                pullDownEl.className = 'pullDown flip';
		                pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开刷新';
		                // $(pullDownEl.querySelector('.pullDownLabel')).html('下拉加载<i class="icon loading"></i>');
		                this.minScrollY = 0;
		            } else if (this.y <= 5 && pullDownEl && pullDownEl.className.match('flip')) {
		                pullDownEl.className = 'pullDown';
		                pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉加载';
		                // $(pullDownEl.querySelector('.pullDownLabel')).html('下拉加载<i class="icon loading"></i>');
		                this.minScrollY = -pullDownOffset;
		            }
		            pullActionDetect.check(0);
		            
		        }
		    });
		    myScroll.on('scrollEnd', function () {
		        setTimeout(function() {
		            that.s.scroll_in_progress = false;
		        }, 100);
		        if ($('#wrapper ul > li').length >=that.s.items_per_page) {
		            if (pullDownEl && pullDownEl.className.match('flip')) {
		                pullDownEl.className = 'pullDown loading';
		                pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开刷新';
		                // $(pullDownEl.querySelector('.pullDownLabel')).html('松开刷新<i class="icon loading"></i>');
		                pullDownAction();
		            }
		            // We let the momentum scroll finish, and if reached the end - loading the next page
		            pullActionDetect.check(0);
		        }
		    });
		    
		    // In order to prevent seeing the "pull down to refresh" before the iScoll is trigger - the wrapper is located at left:-9999px and returned to left:0 after the iScoll is initiated
		    setTimeout(function() {
		        $('#wrapper').css({left:0});
		    }, 500);
		}

		//入口
		setTimeout(function(){
            if(refresh){
                myScroll.refresh();
                pullActionCallback();
            }else {
                if (myScroll) {
                    myScroll.destroy();
                    $(myScroll.scroller).attr('style', ''); // Required since the styles applied by IScroll might conflict with transitions of parent layers.
                    myScroll = null;
                }
                trigger_myScroll();
            }
        },1000);
	},
	freshOnly:function(initePage,freshType,id){
		try{
			document.createEvent("TouchEvent");
			var _touchStartY,_touchEndY,_touchMove=false;
			var _target=document.getElementById(id).parentNode.parentNode;
			_target.addEventListener('touchstart',function(event){
			    var touch = event.touches[0];
			    _touchStartY=touch.clientY;
			},false);
			_target.addEventListener('touchmove', function(event) {
				event.preventDefault();
				console.log(111);
				var touch = event.touches[0];
				if(touch.clientY-_touchStartY<=0){//
					_touchMove=false;
					console.log(111);
					$("#scroller").css({
						"transition-timing-function":'cubic-bezier(0.1, 0.57, 0.1, 1)',
						"transition-duration": '0ms',
						"transform":"translate(0px, -200px) translateZ(0px)"
					});

				}else{
					_touchMove=true;
					$('.pullDown').fadeIn('fast');
					if(touch.clientY-_touchStartY>=40){
					    $('.pullDown').addClass('flip');
					    $('.pullDownLabel').html('松开刷新');
					}else{
					    $('.pullDown').removeClass('flip');
					    $('.pullDownLabel').html('下拉加载');
					}
				}
			},false);
			_target.addEventListener('touchend',function(event){
			    _touchEndY=event.changedTouches[0].clientY;
			    if(_touchMove){
			        if(_touchEndY-_touchStartY>=80){
			        	if(freshType){
			        		var _ajax=initePage(freshType);
			        	}else{
			        		var _ajax=initePage();
			        	}
			            
			            $('.pullDown').addClass('loading');
			            _ajax.done(function(){
			            	$('.pullDownLabel').html('加载成功');
			            	$('.pullDown').removeClass('flip').removeClass('loading').fadeOut();
			                setTimeout(function(){
				                $('.pullDownLabel').html('下拉加载')
				            },1000)
			            });
			        }else if(_touchEndY-_touchStartY>=40){
			        	setTimeout(function(){
			                $('.pullDown').removeClass('flip').removeClass('loading').fadeOut();
			                $('.pullDownLabel').html('下拉加载');
			            },1000)
			        }else{
			            $('.pullDown').removeClass('flip').removeClass('loading').fadeOut();
			            $('.pullDownLabel').html('下拉加载');
			        }
			    }
			},false);
		}catch(e){
			$.afui.toast({
                message:'系统版本太低，不支持触摸事件！'+e.message,
                position:"tr",
                autoClose:false, 
                type:"warn"
            });
		}
		
	},
	translateStatus:function(key){
		var statusArray={
			'0':'<span class="self-status-icon icon-fail">未审核</span>',
			'1':'<span class="self-status-icon icon-success">运行中</span>',
			'2':'<span class="self-status-icon icon-success">创建中</span>',
			'3':'<span class="self-status-icon icon-fail">创建失败</span>',
			'4':'<span class="self-status-icon icon-fail">审核失败</span>',
			'5':'<span class="self-status-icon icon-fail">异常</span>',
			'6':'<span class="self-status-icon icon-success">正常</span>',
			'7':'<span class="self-status-icon icon-success">启动中</span>',
			'8':'<span class="self-status-icon icon-success">停止中</span>',
			'9':'<span class="self-status-icon icon-fail">已停止</span>',
			'10':'<span class="self-status-icon icon-success">删除中</span>',
			'11':'<span class="self-status-icon icon-fail">已删除</span>',
			'12':'<span class="self-status-icon icon-fail">不存在</span>',
			'13':'<span class="self-status-icon icon-fail">危险</span>',
			'14':'<span class="self-status-icon icon-fail">严重危险</span>',
			'15':'<span class="self-status-icon icon-fail">禁用</span>',
			'FAILD':'<span class="self-status-icon icon-fail">备份失败</span>',
			'SUCCESS':'<span class="self-status-icon icon-success">备份成功</span>',
			'BUILDING':'<span class="self-status-icon icon-success">备份中</span>',
			'ABNORMAL':'<span class="self-status-icon icon-fail">备份异常</span>',
			'null':'<span class="self-status-icon icon-fail">未知</span>'
		}
		return statusArray[key]
	},
	date:function(format, timestamp){
		var a;
		if(timestamp == null){
			return "---";
		}else{
			var jsdate=new Date(timestamp);
		}
	    var pad = function(n, c){
	        if((n = n + "").length < c){
	            return new Array(++c - n.length).join("0") + n;
	        } else {
	            return n;
	        }
	    };
	    var txt_weekdays = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
	    var txt_ordin = {1:"st", 2:"nd", 3:"rd", 21:"st", 22:"nd", 23:"rd", 31:"st"};
	    var txt_months = ["", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
	    var f = {
	        // Day
	        d: function(){return pad(f.j(), 2)},
	        D: function(){return f.l().substr(0,3)},
	        j: function(){return jsdate.getDate()},
	        l: function(){return txt_weekdays[f.w()]},
	        N: function(){return f.w() + 1},
	        S: function(){return txt_ordin[f.j()] ? txt_ordin[f.j()] : 'th'},
	        w: function(){return jsdate.getDay()},
	        z: function(){return (jsdate - new Date(jsdate.getFullYear() + "/1/1")) / 864e5 >> 0},
	        // Week
	        W: function(){
	            var a = f.z(), b = 364 + f.L() - a;
	            var nd2, nd = (new Date(jsdate.getFullYear() + "/1/1").getDay() || 7) - 1;
	            if(b <= 2 && ((jsdate.getDay() || 7) - 1) <= 2 - b){
	                return 1;
	            } else{
	                if(a <= 2 && nd >= 4 && a >= (6 - nd)){
	                    nd2 = new Date(jsdate.getFullYear() - 1 + "/12/31");
	                    return date("W", Math.round(nd2.getTime()/1000));
	                } else{
	                    return (1 + (nd <= 3 ? ((a + nd) / 7) : (a - (7 - nd)) / 7) >> 0);
	                }
	            }
	        },
	        // Month
	        F: function(){return txt_months[f.n()]},
	        m: function(){return pad(f.n(), 2)},
	        M: function(){return f.F().substr(0,3)},
	        n: function(){return jsdate.getMonth() + 1},
	        t: function(){
	            var n;
	            if( (n = jsdate.getMonth() + 1) == 2 ){
	                return 28 + f.L();
	            } else{
	                if( n & 1 && n < 8 || !(n & 1) && n > 7 ){
	                    return 31;
	                } else{
	                    return 30;
	                }
	            }
	        },
	       
	        // Year
	        L: function(){var y = f.Y();return (!(y & 3) && (y % 1e2 || !(y % 4e2))) ? 1 : 0},
	        //o not supported yet
	        Y: function(){return jsdate.getFullYear()},
	        y: function(){return (jsdate.getFullYear() + "").slice(2)},
	       
	        // Time
	        a: function(){return jsdate.getHours() > 11 ? "pm" : "am"},
	        A: function(){return f.a().toUpperCase()},
	        B: function(){
	            // peter paul koch:
	            var off = (jsdate.getTimezoneOffset() + 60)*60;
	            var theSeconds = (jsdate.getHours() * 3600) + (jsdate.getMinutes() * 60) + jsdate.getSeconds() + off;
	            var beat = Math.floor(theSeconds/86.4);
	            if (beat > 1000) beat -= 1000;
	            if (beat < 0) beat += 1000;
	            if ((String(beat)).length == 1) beat = "00"+beat;
	            if ((String(beat)).length == 2) beat = "0"+beat;
	            return beat;
	        },
	        g: function(){return jsdate.getHours() % 12 || 12},
	        G: function(){return jsdate.getHours()},
	        h: function(){return pad(f.g(), 2)},
	        H: function(){return pad(jsdate.getHours(), 2)},
	        i: function(){return pad(jsdate.getMinutes(), 2)},
	        s: function(){return pad(jsdate.getSeconds(), 2)},
	        //u not supported yet
	       
	        // Timezone
	        //e not supported yet
	        //I not supported yet
	        O: function(){
	            var t = pad(Math.abs(jsdate.getTimezoneOffset()/60*100), 4);
	            if (jsdate.getTimezoneOffset() > 0) t = "-" + t; else t = "+" + t;
	            return t;
	        },
	        P: function(){var O = f.O();return (O.substr(0, 3) + ":" + O.substr(3, 2))},
	        //T not supported yet
	        //Z not supported yet
	       
	        // Full Date/Time
	        c: function(){return f.Y() + "-" + f.m() + "-" + f.d() + "T" + f.h() + ":" + f.i() + ":" + f.s() + f.P()},
	        //r not supported yet
	        U: function(){return Math.round(jsdate.getTime()/1000)}
	    };
	       
	    return format.replace(/[\\]?([a-zA-Z])/g, function(t, s){
	        if( t!=s ){
	            // escaped
	            ret = s;
	        } else if( f[s] ){
	            // a date function exists
	            ret = f[s]();
	        } else{
	            // nothing special
	            ret = s;
	        }
	        return ret;
	    });
	},
	chartInit:function(id,title,data){
		var option={
		    title:{
		        text:title,
		        x:'center'
		    },
		    color:[ '#32cd32', '#87cefa', '#d21313', '#ff7f50', '#6495ed', '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0', 
		    '#1e90ff', '#ff6347', '#7b68ee', '#00fa9a', '#ffd700', '#6b8e23', '#ff00ff', '#3cb371', '#b8860b', '#30e0e0' ],
		    legend: {
		        orient : 'horizontal',
		        x : 'left',
		        y:'bottom',
		        data:['正常','单节点故障','危险','集群不可用','获取数据超时','解析数据错误'],
		        itemGap:20,
		        textStyle:{
		        	align:'center'
		        }
		    },
		    series : [
		        {
		            type:'pie',
		            radius : ['50%', '70%'],
		            itemStyle : {
		                normal : {
		                    label : {
		                        show : false
		                    },
		                    labelLine : {
		                        show : false
		                    }
		                },
		                emphasis : {
		                    label : {
		                        show : false,
		                        position : 'center',
		                        textStyle : {
		                            fontSize : '30',
		                            fontWeight : 'bold'
		                        }
		                    }
		                }
		            },
		            data:[
		                {value:data.nothing, name:'正常'},
		                {value:data.general, name:'单节点故障'},
		                {value:data.serious, name:'危险'},
		                {value:data.crash, name:'集群不可用'},
		                {value:data.timeout, name:'获取数据超时'},
		                {value:data.except, name:'解析数据错误'}
		            ]
		        }
			]
		}
		var myChart = echarts.init(document.getElementById(id));
		myChart.setOption(option);	
	},
	urlParameter:function(parameter){
	    var reg = new RegExp("(^|&)" + parameter + "=([^&]*)(&|$)"); //构造含有目标参数的正则表达式对象
	    var r = window.location.search.substr(1).match(reg);  
	    if (r != null) return unescape(r[2]); return null;     
	},
	selfHeaderNav:function(){
		var _selfItems=$('.self-downItems');
		var _selfTab=$('.self-tab'),_pages=$('.pages');
		$('#header').unbind('click').click(function(event) {
		    event.stopPropagation();
		    var _this=$(this).children('span.self-down');
		    if(_this.length>0){
		    	if(_this.hasClass('up')){//收回
			        _this.removeClass('roll').removeClass('up').addClass('down');
			        _selfItems.slideUp(function() {
			            _selfItems.addClass('self-hidden')
			        });
			        _selfTab.removeClass('self-hidden');
			        _pages.removeClass('self-hidden');   
			    }else{//展开
			        _this.addClass('roll').removeClass('down').addClass('up');
			        _selfTab.addClass('self-hidden');
			        _pages.addClass('self-hidden');
			        _selfItems.slideDown(function() {
			            _selfItems.removeClass('self-hidden')
			        });
			    }
		    }else{
		    } 
		});
		$('.self-downItems').children('ul').unbind('click').bind('click',function(event) {
		    event.stopPropagation();
		    var _target=$(event.target).closest('li');
		    var _text=_target.text();
		    var _href=_target.attr('self-href');
		    window.location.href=_href+'.html'
		});
		$('header').unbind('click').bind('click',function(event) {
		    if($(event.target).hasClass('backButton back')){
		        $(this).addClass('self-hidden')
		        $('.self-header').removeClass('self-hidden');
		        $('.self-tab').removeClass('self-hidden');
		    }
		});
		$('#mainHeaderGoback').unbind('click').bind('click',function(event) {
			AppObj.goBackHome();
		});
	}
};