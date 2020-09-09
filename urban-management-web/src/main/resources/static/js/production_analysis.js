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
                name:'不同类型单位占比',
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
    myChart.on('click',function(){
        openEcharts()
    });
    var option = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'horizontal',
            x : 'left',
            data:['成都市','绵阳市','德阳市','自贡市',"南充市",'广元市','巴中市',"资阳市",
            '攀枝花市','泸州市','遂宁市','内江市']
        },

        calculable : false,
        series : [
            {
                name:'各区域单位占比',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {
                        value:124,
                        name:'成都市',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:9,
                        name:'绵阳市'
                    },
                    {
                        value:9,
                        name:'德阳市'
                    },
                    {
                        value:9,
                        name:'自贡市'
                    },
                    {
                        value:39,
                        name:'南充市'
                    },
                    {
                        value:124,
                        name:'广元市',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:9,
                        name:'巴中市'
                    },
                    {
                        value:9,
                        name:'资阳市'
                    },
                    {
                        value:9,
                        name:'攀枝花市'
                    },
                    {
                        value:39,
                        name:'泸州市'
                    },
                    {
                        value:124,
                        name:'遂宁市',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:9,
                        name:'内江市'
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
                name:'生产量（吨）',
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
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:['化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹']
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
                data : ['其他','北区','南区','西区','东区']
            }
        ],
        series : [
            {
                name:'化学品',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 190,]
            },
            {
                name:'射钉器材',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[120, 132, 101, 134, 90, 230, 210,120, 132, 201, 134, 190]
            },
            {
                name:'印章',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290,]
            },
            {
                name:'机修',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290,]
            },
            {
                name:'散装加油',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[150, 212, 201, 154, 190, 330, 410,150, 212, 201, 154, 190]
            },
            {
                name:'典当',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[80, 83, 91, 934, 120, 130, 120,820, 832, 901, 934, 290]
            },
            {
                name:'烟花爆竹',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[80, 83, 91, 934, 120, 130, 120,820, 832, 901, 934, 290]
            }
        ]
    };
    myChart.setOption(option);
});

$(function(){
    var myChart = echarts.init($("#container7")[0]);
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
                name:'人员数量（万人）',
                type:'bar',
                barWidth:'30',
                data:[39, 25, 35, 47, 36, 25, 36, 52],
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
    var myChart = echarts.init($("#container8")[0]);
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
$(function(){
    var myChart = echarts.init($("#container6")[0]);
    var option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['计划', '实际']
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
                name:'计划',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'实际',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 110,],


            }

        ]
    };
    myChart.setOption(option);
});
    function openEcharts(){
        layer.open({
            type: 2,
            title: '各区域从业单位分析图',
            shade: 0.5,
            skin: 'layui-layer-rim',
            area: ['70%', '60%'],
            shadeClose: true,
            closeBtn: 2,
            content: 'echartsDemo.html'
        });
    }