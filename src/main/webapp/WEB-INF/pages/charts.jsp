<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chart</title>
<script src="./js/echarts-all.js"></script>
<script src="./js/jquery-1.7.2.js"></script>
</head>
<body>
	<div id="main" style="height: 500px"></div>
</body>

<script type="text/javascript">
	// 基于准备好的dom，初始化echarts图表
	var myChart = echarts.init(document.getElementById('main'));
	var option = {
		title : {
			text : '综合指标数据图表构建',
			subtext : '仅供参考',
			x : 'center'
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b} : {c} ({d}%)"
		},
		legend : {
			orient : 'vertical',
			x : 'left',
			data : [ '1', '2', '3', '4', '5' ]
		},
		toolbox : {
			show : true,
			feature : {
				mark : {
					show : true
				},
				dataView : {
					show : true,
					readOnly : false
				},
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'left',
							max : 1548
						}
					}
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		calculable : true,
		series : [ {
			name : '数据',
			type : 'pie',
			radius : '55%',
			center : [ '50%', '60%' ],
			data : [ {
				value : 335,
				name : '1'
			}, {
				value : 310,
				name : '2'
			}, {
				value : 234,
				name : '3'
			}, {
				value : 135,
				name : '4'
			}, {
				value : 1548,
				name : '5'
			} ]
		} ]
	};
	var chartsData = jQuery.parseJSON('${data}');
	option.legend.data = chartsData.titles;
	option.series[0].data = chartsData.values;
	myChart.setOption(option);
</script>
</html>