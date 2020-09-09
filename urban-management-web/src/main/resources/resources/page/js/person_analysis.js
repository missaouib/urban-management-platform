/**
 * Created by Administrator on 2017/9/12.
 */
/**
 * Created by Administrator on 2017/4/25.
 */
$(function(){
    var myChart = echarts.init($("#container")[0]);
    var option = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['爆炸物品','化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹']
        },

        series : [
            {
                name:'从业单位人员占比',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {
                        value:124,
                        name:'爆炸物品',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:86,
                        name:'化学品'
                    },
                    {
                        value:9,
                        name:'射钉器材'
                    },
                    {
                        value:9,
                        name:'印章'
                    },{
                        value:86,
                        name:'机修'
                    },
                    {
                        value:9,
                        name:'散装加油'
                    },
                    {
                        value:9,
                        name:'典当'
                    },{
                        value:9,
                        name:'烟花爆竹'
                    }

                ]
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container2")[0]);
    var option = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['东区','西区','南区','北区',"其他"]
        },

        calculable : false,
        series : [
            {
                name:'各区域人员占比',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {
                        value:124,
                        name:'东区',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:9,
                        name:'西区'
                    },
                    {
                        value:9,
                        name:'南区'
                    },
                    {
                        value:9,
                        name:'北区'
                    },
                    {
                        value:39,
                        name:'其他'
                    }

                ]
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container1")[0]);
    var option = {
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:[]
        },
        //toolbox: {
        //    show : true,
        //    //orient: 'vertical',
        //    x: 'right',
        //    y: 'top',
        //    feature : {
        //        mark : {show: true},
        //        dataView : {show: true, readOnly: false},
        //        magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
        //        restore : {show: true},
        //        saveAsImage : {show: true}
        //    }
        //},
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data:['化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹'],
                rotate:40,interval:0
            }
        ],

        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'从业人员数量（个）',
                type:'bar',
                barWidth:'30',
                data:[927, 785, 652, 521, 157, 245, 121, 157],
                itemStyle: {
                    normal: {
                        color:"#269fec"
                    }
                }

            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container3")[0]);
    var option = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['青年','中年','老年',"其他"]
        },

        calculable : false,
        series : [
            {
                name:'人员年龄段分析',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {
                        value:14,
                        name:'老年',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:9,
                        name:'青年'
                    },
                    {
                        value:9,
                        name:'中年'
                    },
                    {
                        value:39,
                        name:'其他'
                    }

                ]
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container4")[0]);
    var option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹'],
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
                type : 'value'

            }
        ],
        series : [
            {
                name:'化学品',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190]
            },
            {
                name:'射钉器材',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 110]
            },
            {
                name:'印章',
                type:'line',
                data:[214, 254, 236, 134, 236, 365, 320,100, 205, 201, 235, 290]
            },
            {
                name:'机修',
                type:'line',
                data:[222, 253, 301, 334, 254, 356, 421,254, 302, 235, 235, 457]
            },
            {
                name:'散装加油',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190]
            },
            {
                name:'典当',
                type:'line',
                data:[222, 253, 301, 334, 254, 356, 421,254, 302, 235, 235, 457]
            },
            {
                name:'烟花爆竹',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190]
            }

        ]
    };
    myChart.setOption(option);
});