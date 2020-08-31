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
            orient :'horizontal',
            x : 'left',
            data:['炸药','雷管','危险化学品','烟花爆竹',"易燃品",'其它']
        },

        calculable : false,
        series : [
            {
                name:'各类危险品生产产量占比',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {
                        value:124,
                        name:'雷管',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:86,
                        name:'炸药'
                    },
                    {
                        value:9,
                        name:'危险化学品'
                    },
                    {
                        value:9,
                        name:'烟花爆竹'
                    },
                    {
                        value:9,
                        name:'易燃品'
                    },
                    {
                        value:39,
                        name:'其它'
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
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'horizontal',
            x : 'left',
            data:['成都','绵阳','德阳','南充',"宜宾",'自贡','泸州','资阳']
        },

        calculable : false,
        series : [
            {
                name:'各区域危险品生产产量占比',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {
                        value:124,
                        name:'成都',
                        itemStyle:{
                            normal:{
                                color:""
                            }
                        }
                    },
                    {
                        value:86,
                        name:'绵阳'
                    },
                    {
                        value:9,
                        name:'德阳'
                    },
                    {
                        value:9,
                        name:'南充'
                    },
                    {
                        value:9,
                        name:'宜宾'
                    },
                    {
                        value:39,
                        name:'自贡'
                    },
                    {
                        value:39,
                        name:'泸州'
                    },
                    {
                        value:39,
                        name:'资阳'
                    }

                ]
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container2")[0]);
    option = {

        tooltip: {
            trigger: 'item'
        },

        calculable: true,
        grid: {
            borderWidth: 0,
            y: 80,
            y2: 60
        },
        xAxis: [
            {
                type: 'category',
                show: false,
                data: ['炸药', 'B雷管', '危险化学品', '烟花爆竹', '易燃品', '其它']
            }
        ],
        yAxis: [
            {
                type: 'value',
                show: false
            }
        ],
        series: [
            {
                name: '统计',
                type: 'bar',
                itemStyle: {
                    normal: {
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                                '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                                '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                                '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                            ];
                            return colorList[params.dataIndex]
                        },
                        label: {
                            show: true,
                            position: 'top',
                            formatter: '{b}\n{c}'
                        }
                    }
                },
                data: [12,21,10,4,12,5,6,5,25,23,7],
                markPoint: {
                    tooltip: {
                        trigger: 'item',
                        backgroundColor: 'rgba(0,0,0,0)',
                        formatter: function(params){
                            return '<img src="'
                                + params.data.symbol.replace('image://', '')
                                + '"/>';
                        }
                    },
                    data: [
                        {xAxis:0, y: 350, name:'炸药', symbolSize:20},
                        {xAxis:1, y: 350, name:'雷管', symbolSize:20},
                        {xAxis:2, y: 350, name:'危险化学品', symbolSize:20},
                        {xAxis:3, y: 350, name:'烟花爆竹', symbolSize:20},
                        {xAxis:4, y: 350, name:'易燃品', symbolSize:20},
                        {xAxis:5, y: 350, name:'其它', symbolSize:20}

                    ]
                }
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container3")[0]);
    option = {

        tooltip: {
            trigger: 'item'
        },

        calculable: true,
        grid: {
            borderWidth: 0,
            y: 80,
            y2: 60
        },
        xAxis: [
            {
                type: 'category',
                show: false,
                data:['成都','绵阳','德阳','南充',"宜宾",'自贡','泸州','资阳']
            }
        ],
        yAxis: [
            {
                type: 'value',
                show: false
            }
        ],
        series: [
            {
                name: '统计',
                type: 'bar',
                itemStyle: {
                    normal: {
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                                '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                                '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                                '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                            ];
                            return colorList[params.dataIndex]
                        },
                        label: {
                            show: true,
                            position: 'top',
                            formatter: '{b}\n{c}'
                        }
                    }
                },
                data: [12,21,10,4,12,5,6,5,25,23,7],
                markPoint: {
                    tooltip: {
                        trigger: 'item',
                        backgroundColor: 'rgba(0,0,0,0)',
                        formatter: function(params){
                            return '<img src="'
                                + params.data.symbol.replace('image://', '')
                                + '"/>';
                        }
                    },
                    data: [
                        {xAxis:0, y: 350, name:'成都', symbolSize:20},
                        {xAxis:1, y: 350, name:'绵阳', symbolSize:20},
                        {xAxis:2, y: 350, name:'德阳', symbolSize:20},
                        {xAxis:3, y: 350, name:'资阳', symbolSize:20},
                        {xAxis:4, y: 350, name:'自贡', symbolSize:20},
                        {xAxis:5, y: 350, name:'宜宾', symbolSize:20},
                        {xAxis:5, y: 350, name:'泸州', symbolSize:20}

                    ]
                }
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container4")[0]);
    option = {
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:['炸药', '雷管','危险品','易燃品','烟花爆竹','其它']
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
                data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
            }
        ],
        series : [
            {
                name:'炸药',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 190,]
            },
            {
                name:'雷管',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[120, 132, 101, 134, 90, 230, 210,120, 132, 201, 134, 190]
            },
            {
                name:'烟花爆竹',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290,]
            },
            {
                name:'易燃品',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:[150, 212, 201, 154, 190, 330, 410,150, 212, 201, 154, 190]
            },
            {
                name:'其它',
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
    var myChart = echarts.init($("#container5")[0]);
    option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['炸药', '雷管','危险品','易燃品','烟花爆竹','其它']
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
                name:'炸药',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'烟花爆竹',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 334, 110,],


            },
            {
                name:'易燃品',
                type:'line',
                data:[320, 302, 301, 334, 390, 330, 320,120, 302, 201, 34, 190],


            },
            {
                name:'其它',
                type:'line',
                data:[320, 30, 301, 34, 90, 330, 320,120, 302, 201, 334, 90],


            },
            {
                name:'危险品',
                type:'line',
                data:[20, 302, 301, 334, 390, 330, 30,120, 32, 201, 334, 10,],


            },
            {
                name:'雷管',
                type:'line',
                data:[120, 132, 101, 34, 90, 30, 20,10, 132, 21, 134, 190],


            }
        ]
    };
    myChart.setOption(option);
});

$(function(){
    var myChart = echarts.init($("#container6")[0]);
    option = {

        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter: function (params){
                return params[0].name + '<br/>'
                    + params[0].seriesName + ' : ' + params[0].value + '<br/>'
                    + params[1].seriesName + ' : ' + (params[1].value + params[0].value);
            }
        },


        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : ['单位一','单位二','单位三','单位四','单位五','单位六']
            }
        ],
        yAxis : [
            {
                type : 'value',
                boundaryGap: [0, 0.1]
            }
        ],
        series : [
            {
                name:'单位一',
                type:'bar',
                stack: 'sum',
                barCategoryGap: '50%',
                itemStyle: {
                    normal: {
                        color: 'tomato',
                        barBorderColor: 'tomato',
                        barBorderWidth: 6,
                        barBorderRadius:0,
                        label : {
                            show: true, position: 'insideTop'
                        }
                    }
                },
                data:[260, 200, 220, 120, 100, 80]
            },
            {
                name:'单位二',
                type:'bar',
                stack: 'sum',
                itemStyle: {
                    normal: {
                        color: '#fff',
                        barBorderColor: 'tomato',
                        barBorderWidth: 6,
                        barBorderRadius:0,
                        label : {
                            show: true,
                            position: 'top',
                            formatter: function (params) {
                                for (var i = 0, l = option.xAxis[0].data.length; i < l; i++) {
                                    if (option.xAxis[0].data[i] == params.name) {
                                        return option.series[0].data[i] + params.value;
                                    }
                                }
                            },
                            textStyle: {
                                color: 'tomato'
                            }
                        }
                    }
                },
                data:[40, 80, 50, 80,80, 70]
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