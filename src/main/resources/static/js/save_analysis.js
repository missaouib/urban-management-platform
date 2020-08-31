/**
 * Created by Administrator on 2017/4/25.
 */
$(function(){
    var myChart = echarts.init($("#container")[0]);
    option = {
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:['生产','存储','运输']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                data : ['化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'生产',
                type:'bar',
                data:[20, 12, 31, 45, 39, 21, 20,28]
            },
            {
                name:'存储',
                type:'bar',
                stack: '广告',
                data:[120, 132, 101, 134, 90, 230, 210,86]
            },
            {
                name:'运输',
                type:'bar',
                stack: '广告',
                data:[2, 2, 1, 4, 3, 5, 1,6]
            }


        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container1")[0]);
    option = {
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:['危化品仓库', '危化品生产单位','危化品使用单位']
        },

        calculable : true,
        xAxis : [
            {
                type : 'value'
            }
        ],
        yAxis : [
            {
                type : 'category',
                data : ['高新区','新都区','成化区','青羊区','金牛区','龙泉区','锦江区']
            }
        ],
        series : [
            {
                name:'危化品仓库',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[320, 302, 301, 334, 390, 330, 320]
            },
            {
                name:'危化品生产单位',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[120, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'危化品使用单位',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[220, 182, 191, 234, 290, 330, 310]
            }

        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container4")[0]);
    option = {

        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['高新区','新都区','成化区','青羊区','金牛区','龙泉区','锦江区']
        },

        calculable : false,
        series : [
            {
                name:'存储量',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:335, name:'高新区'},
                    {value:2310, name:'新都区'},
                    {value:234, name:'成化区'},
                    {value:135, name:'青羊区'},
                    {value:148, name:'金牛区'},
                    {value:148, name:'龙泉区'},
                    {value:148, name:'锦江区'}
                ]
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container5")[0]);
    option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['高新区','新都区','成化区','青羊区','金牛区','龙泉区','锦江区']
        },

        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
            }
        ],
        yAxis : [
            {
                type : 'value',

            }
        ],
        series : [
            {
                name:'高新区',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'新都区',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 110,],


            },
            {
                name:'成化区',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 34, 190],


            },
            {
                name:'青羊区',
                type:'line',
                data:[320, 30, 301, 34, 90, 330, 320,120, 302, 201, 334, 90],


            },
            {
                name:'龙泉区',
                type:'line',
                data:[20, 302, 301, 334, 390, 330, 30,120, 32, 201, 334, 10,],


            },
            {
                name:'金牛区',
                type:'line',
                data:[120, 132, 101, 34, 90, 30, 20,10, 132, 21, 134, 190],
            },
            {
                name:'锦江区',
                type:'line',
                data:[120, 132, 101, 34, 90, 30, 20,10, 132, 21, 134, 190],


            }
        ]
    };
    myChart.setOption(option);
});


$(function(){
    var myChart = echarts.init($("#container7")[0]);
    option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['生产', '存储','使用']
        },

        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
            }
        ],
        yAxis : [
            {
                type : 'value',

            }
        ],
        series : [
            {
                name:'生产',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'存储',
                type:'line',
                data:[210, 102, 501, 534, 390, 1330, 1320,1110, 302, 401, 334, 190,],


            },
            {
                name:'使用',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 110,],


            }

        ]
    };
    myChart.setOption(option);
});