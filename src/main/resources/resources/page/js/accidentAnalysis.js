/**
 * Created by Administrator on 2017/4/25.
 */
function myEcharts(){
    salesMyEcharts();
    monthOrderMyEcharts();

    returnRankMyEcharts();
    salesProportionMyEcharts();
    profitProportionMyEcharts();
    container3()
}

function salesMyEcharts(){
    var salesMyChart = echarts.init($("#container5")[0]);
    var option = {

        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['火灾类','腐蚀类','坍塌类','爆炸类','其他']
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
            data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月',"12月"]
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'腐蚀类',
                type:'line',
                stack: '总额（万元）',
                data:[120, 132, 101, 134, 90, 230, 210, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'火灾类',
                type:'line',
                smooth:false,
                itemStyle:{
                    normal:{
                        lineStyle:{
                            width:2,
                            type:'solid'  //'dotted'虚线 'solid'实线
                        }
                    }
                },
                stack: '总额（万元）',
                data:[220, 182, 191, 234, 290, 330, 310, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'坍塌类',
                type:'line',
                stack: '总额（万元）',
                data:[150, 232, 201, 154, 190, 330, 410, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'爆炸类',
                type:'line',
                stack: '总额（万元）',
                data:[320, 332, 301, 334, 390, 330, 320, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'其他',
                type:'line',
                stack: '总额（万元）',
                data:[820, 932, 901, 934, 1290, 1330, 1320, 132, 101, 134, 90, 230, 210]
            },

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
                name:'事故数',
                type:'line',
                stack: '（个）',
                data:[120, 132, 101, 134, 90, 230, 210, 132, 101, 134, 90, 230, 210, 132, 101, 134, 90, 230, 210, 132, 101, 134, 90, 230, 132, 101, 134, 90, 230, 210, 132, 101, 134, 90, 230]
            }
        ]
    };
    monthOrderMyChart.setOption(option);
    monthOrderMyChart.on('click', function (param) {
        edit()
    });
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
            data:['火灾类','腐蚀类','爆炸类','坍塌类','其他']
        },

        calculable : true,
        series : [
            {
                name:'',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {
                        value:124,
                        name:'火灾类',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:86,
                        name:'腐蚀类'
                    },
                    {
                        value:9,
                        name:'爆炸类'
                    },
                    {
                        value:39,
                        name:'坍塌类'
                    },
                    {
                        value:9,
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
            data:['特大事故','极大事故','重大事故','一般事故']
        },
        calculable : true,
        series : [
            {
                name:'事故级别',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {
                        value:124,
                        name:'特大事故',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:86,
                        name:'极大事故'
                    },
                    {
                        value:9,
                        name:'重大事故'
                    },
                    {
                        value:39,
                        name:'一般事故'
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

        calculable : true,
        xAxis : [
            {
                type : 'category',
                data:['化学品','射钉器材','印章','机修','散装加油','典当','烟花爆竹']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'事故数（次）',
                type:'bar',
                barWidth:'30',
                data:[927, 785, 652, 521, 157, 20, 521, 157, 20],
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

function container3(){
    var returnRank = echarts.init($("#container3")[0]);
    var option = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['物品过期','物品损坏','看护不力','其他']
        },

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
                        name:'物品过期',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:86,
                        name:'物品损坏'
                    },
                    {
                        value:9,
                        name:'看护不力'
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

function edit() {
    var Name = "单个物品分析";
    var Href = "from_analysis.html";
    //var data_id = $("#add").attr('data-id');
    window.parent.$.learuntab.myAddTab(Name, Href);
}

