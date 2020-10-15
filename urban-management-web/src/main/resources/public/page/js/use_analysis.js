/**
 * Created by Administrator on 2017/4/25.
 */
$(function(){
    var myChart = echarts.init($("#container")[0]);
    option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹']
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
                name:'化学品',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'射钉器材',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 110,],


            },
            {
                name:'印章',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 34, 190],


            },
            {
                name:'机修',
                type:'line',
                data:[320, 30, 301, 34, 90, 330, 320,120, 302, 201, 334, 90],


            },
            {
                name:'散装加油',
                type:'line',
                data:[20, 302, 301, 334, 390, 330, 30,120, 32, 201, 334, 10,],


            },
            {
                name:'典当',
                type:'line',
                data:[120, 132, 101, 34, 90, 30, 20,10, 132, 21, 134, 190],
            },
            {
                name:'烟花爆竹',
                type:'line',
                data:[120, 132, 101, 34, 90, 30, 20,10, 132, 21, 134, 190],


            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container1")[0]);
    option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['使用量', '事故量','其它社会因素']
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
                name:'使用量',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'事故量',
                type:'line',
                data:[210, 102, 501, 534, 390, 1330, 1320,1110, 302, 401, 334, 190,],


            },
            {
                name:'其它社会因素',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 110,],


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
            data:['化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹']
        },

        calculable : false,
        series : [
            {
                name:'存储量',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:1335, name:'化学品'},
                    {value:310, name:'射钉器材'},
                    {value:234, name:'印章'},
                    {value:35, name:'机修'},
                    {value:148, name:'散装加油'},
                    {value:148, name:'典当'},
                    {value:148, name:'烟花爆竹'}
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
    var myChart = echarts.init($("#container7")[0]);
    option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['生产计划', '运输计划','存储计划','事故量',"使用量"]
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
                name:'生产计划',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'运输计划',
                type:'line',
                data:[210, 102, 501, 534, 390, 1330, 1320,1110, 302, 401, 334, 190,],


            },
            {
                name:'事故量',
                type:'line',
                data:[20, 10, 51, 34, 39, 13, 13,11, 30, 11, 4, 19],


            },
            {
                name:'存储计划',
                type:'line',
                data:[310, 202, 601, 584, 1390, 1530, 1020,110, 202, 301, 534, 190,],


            },
            {
                name:'使用量',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 110,],


            }

        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container8")[0]);
    option = {

        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹']
        },

        calculable : false,
        series : [
            {
                name:'存储量',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:1335, name:'化学品'},
                    {value:310, name:'射钉器材'},
                    {value:234, name:'印章'},
                    {value:35, name:'机修'},
                    {value:148, name:'散装加油'},
                    {value:148, name:'典当'},
                    {value:148, name:'烟花爆竹'}
                ]
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container9")[0]);
    option = {

        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['过期','质量问题','损坏','社会因素','其它','假货']
        },

        calculable : false,
        series : [
            {
                name:'存储量',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:1335, name:'过期'},
                    {value:310, name:'质量问题'},
                    {value:234, name:'损坏'},
                    {value:35, name:'社会因素'},
                    {value:148, name:'其它'},
                    {value:148, name:'假货'}

                ]
            }
        ]
    };
    myChart.setOption(option);
});