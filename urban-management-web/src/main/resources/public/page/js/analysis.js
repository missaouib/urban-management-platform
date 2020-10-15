/**
 * Created by Administrator on 2017/4/25.
 */
function myEcharts(){
    salesMyEcharts();
    monthOrderMyEcharts();
    returnRankMyEcharts();
    salesProportionMyEcharts();
    profitProportionMyEcharts();
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
                data: []
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis:  {
                type: 'value'
            },
            yAxis: {
                type: 'category',
                data: ['放燃仓库','小型仓库','大型仓库','防腐仓库','防爆仓库']
            },
            series: [
                {
                    name: '使用率',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: [75, 85, 88, 96, 99],
                    itemStyle: {
                        normal: {
                            color:"#96d596"
                        }
                    }
                }
            ]
        };
        myChart.setOption(option);
    });
}
function salesMyEcharts(){
    var salesMyChart = echarts.init($("#container5")[0]);
    var option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['爆炸物品','化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹']
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
                name:'爆炸物品',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'化学品',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 110,],


            },
            {
                name:'射钉器材',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 34, 190],


            },
            {
                name:'印章',
                type:'line',
                data:[320, 30, 301, 34, 90, 330, 320,120, 302, 201, 334, 90],


            },
            {
                name:'机修',
                type:'line',
                data:[20, 302, 301, 334, 390, 330, 30,120, 32, 201, 334, 10,],


            },
            {
                name:'散装加油',
                type:'line',
                data:[120, 132, 101, 34, 90, 30, 20,10, 132, 21, 134, 190],


            },
            {
                name:'典当',
                type:'line',
                data:[20, 302, 301, 334, 390, 330, 30,120, 32, 201, 334, 10,],


            },
            {
                name:'烟花爆竹',
                type:'line',
                data:[120, 132, 101, 34, 90, 30, 20,10, 132, 21, 134, 190],


            }
        ]
    };
    salesMyChart.setOption(option);
}
function monthOrderMyEcharts(){
    var monthOrderMyChart = echarts.init($("#container4")[0]);
    var option = {
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['1','2','3','4','5','6','7','8','9','10','11','12']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'仓库数量',
                type:'line',
                stack: '（个）',
                data:[120, 132, 101, 134, 90, 230, 210, 132, 101, 134, 90, 230, 210, 132, 101, 134, 90, 230, 210, 132, 101, 134, 90, 230, 132, 101, 134, 90, 230, 210, 132, 101, 134, 90, 230]
            }
        ]
    };
    monthOrderMyChart.setOption(option);
}
function returnRankMyEcharts(){
    var returnRank = echarts.init($("#container1")[0]);
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
                name:'各区域仓库占比',
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
    returnRank.setOption(option);
}
function salesProportionMyEcharts(){
    var salesProportion = echarts.init($("#container")[0]);
    var option = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['大型仓库','中型仓库','小型仓库','防燃仓库','防爆仓库','防腐蚀仓库','其他']
        },
        //toolbox: {
        //    show : true,
        //    feature : {
        //        mark : {show: true},
        //        dataView : {show: true, readOnly: false},
        //        magicType : {
        //            show: true,
        //            type: ['pie', 'funnel'],
        //            option: {
        //                funnel: {
        //                    x: '25%',
        //                    width: '50%',
        //                    funnelAlign: 'left',
        //                    max: 1548
        //                }
        //            }
        //        },
        //        restore : {show: true},
        //        saveAsImage : {show: true}
        //    }
        //},
        calculable : true,
        series : [
            {
                name:'会议类型',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {
                        value:124,
                        name:'大型仓库',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:86,
                        name:'中型仓库'
                    },
                    {
                        value:9,
                        name:'小型仓库'
                    },
                    {
                        value:39,
                        name:'防燃仓库'
                    },
                    {
                        value:86,
                        name:'防爆仓库'
                    },
                    {
                        value:9,
                        name:'防腐蚀仓库'
                    },
                    {
                        value:39,
                        name:'其他'
                    }

                ]
            }
        ]
    };
    salesProportion.setOption(option);
}
function profitProportionMyEcharts(){
    var profitProportion = echarts.init($("#container2")[0]);
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
                name:'危险品生产量（吨）',
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
    profitProportion.setOption(option);
}


