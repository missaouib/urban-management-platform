/**
 * Created by Administrator on 2017/4/25.
 */
$(function(){
    var myChart = echarts.init($("#container")[0]);
    var heatData = [];
    for (var i = 0; i < 15; ++i) {
        heatData.push([
            100 + Math.random() * 20,
            24 + Math.random() * 16,
            Math.random()
        ]);
    }
    for (var j = 0; j < 10; ++j) {
        var x = 100 + Math.random() * 16;
        var y = 24 + Math.random() * 12;
        var cnt = 30 * Math.random();
        for (var i = 0; i < cnt; ++i) {
            heatData.push([
                x + Math.random() * 2,
                y + Math.random() * 2,
                Math.random()
            ]);
        }
    }

    option = {
        backgroundColor: '#fff',

        tooltip : {
            trigger: 'item',
            formatter: '{b}'
        },

        series : [
            {
                name: '四川',
                type: 'map',
                mapType: 'china',
                roam: true,
                hoverable: false,
                data:[],
                heatmap: {
                    minAlpha: 0.1,
                    data: heatData
                },
                itemStyle: {
                    normal: {
                        borderColor:'rgba(100,149,237,0.6)',
                        borderWidth:0.5,
                        areaStyle: {
                            color: '#1b1b1b'
                        }
                    }
                }
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container1")[0]);
    var heatData = [];
    for (var i = 0; i < 20; ++i) {
        heatData.push([
            100 + Math.random() * 20,
            24 + Math.random() * 16,
            Math.random()
        ]);
    }
    for (var j = 0; j < 10; ++j) {
        var x = 100 + Math.random() * 16;
        var y = 24 + Math.random() * 12;
        var cnt = 30 * Math.random();
        for (var i = 0; i < cnt; ++i) {
            heatData.push([
                x + Math.random() * 2,
                y + Math.random() * 2,
                Math.random()
            ]);
        }
    }

    option = {
        backgroundColor: '#fff',

        tooltip : {
            trigger: 'item',
            formatter: '{b}'
        },

        series : [
            {
                name: '四川',
                type: 'map',
                mapType: 'china',
                roam: true,
                hoverable: false,
                data:[],
                heatmap: {
                    minAlpha: 0.1,
                    data: heatData
                },
                itemStyle: {
                    normal: {
                        borderColor:'rgba(100,149,237,0.6)',
                        borderWidth:0.5,
                        areaStyle: {
                            color: '#1b1b1b'
                        }
                    }
                }
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container4")[0]);
    var heatData = [];
    for (var i = 0; i < 10; ++i) {
        heatData.push([
            100 + Math.random() * 20,
            24 + Math.random() * 16,
            Math.random()
        ]);
    }
    for (var j = 0; j < 10; ++j) {
        var x = 100 + Math.random() * 16;
        var y = 24 + Math.random() * 12;
        var cnt = 30 * Math.random();
        for (var i = 0; i < cnt; ++i) {
            heatData.push([
                x + Math.random() * 2,
                y + Math.random() * 2,
                Math.random()
            ]);
        }
    }

    option = {
        backgroundColor: '#fff',

        tooltip : {
            trigger: 'item',
            formatter: '{b}'
        },

        series : [
            {
                name: '四川',
                type: 'map',
                mapType: 'china',
                roam: true,
                hoverable: false,
                data:[],
                heatmap: {
                    minAlpha: 0.1,
                    data: heatData
                },
                itemStyle: {
                    normal: {
                        borderColor:'rgba(100,149,237,0.6)',
                        borderWidth:0.5,
                        areaStyle: {
                            color: '#1b1b1b'
                        }
                    }
                }
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
            data:['生产区域人员分布量', '存储区域人员分布量','使用区域人员分布量']
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
                type : 'value/万',

            }
        ],
        series : [
            {
                name:'生产区域人员分布量',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'存储区域人员分布量',
                type:'line',
                data:[210, 102, 501, 534, 390, 1330, 1320,1110, 302, 401, 334, 190,],


            },
            {
                name:'使用区域人员分布量',
                type:'line',
                data:[20, 10, 51, 34, 39, 13, 13,11, 30, 11, 4, 19],


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
            data:['生产区域人员分布预测量', '存储区域人员分布预测量','使用区域人员分布预测量']
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
                name:'生产区域人员分布预测量',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'存储区域人员分布预测量',
                type:'line',
                data:[210, 102, 501, 534, 390, 1330, 1320,1110, 302, 401, 334, 190,],


            },
            {
                name:'使用区域人员分布预测量',
                type:'line',
                data:[20, 10, 51, 34, 39, 13, 13,11, 30, 11, 4, 19],


            }


        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container8")[0]);
    var heatData = [];
    for (var i = 0; i < 10; ++i) {
        heatData.push([
            100 + Math.random() * 20,
            24 + Math.random() * 16,
            Math.random()
        ]);
    }
    for (var j = 0; j < 10; ++j) {
        var x = 100 + Math.random() * 16;
        var y = 24 + Math.random() * 12;
        var cnt = 30 * Math.random();
        for (var i = 0; i < cnt; ++i) {
            heatData.push([
                x + Math.random() * 2,
                y + Math.random() * 2,
                Math.random()
            ]);
        }
    }

    option = {
        backgroundColor: '#fff',

        tooltip : {
            trigger: 'item',
            formatter: '{b}'
        },

        series : [
            {
                name: '四川',
                type: 'map',
                mapType: 'china',
                roam: true,
                hoverable: false,
                data:[],
                heatmap: {
                    minAlpha: 0.1,
                    data: heatData
                },
                itemStyle: {
                    normal: {
                        borderColor:'rgba(100,149,237,0.6)',
                        borderWidth:0.5,
                        areaStyle: {
                            color: '#1b1b1b'
                        }
                    }
                }
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container9")[0]);
    var heatData = [];
    for (var i = 0; i < 10; ++i) {
        heatData.push([
            100 + Math.random() * 20,
            24 + Math.random() * 16,
            Math.random()
        ]);
    }
    for (var j = 0; j < 10; ++j) {
        var x = 100 + Math.random() * 16;
        var y = 24 + Math.random() * 12;
        var cnt = 30 * Math.random();
        for (var i = 0; i < cnt; ++i) {
            heatData.push([
                x + Math.random() * 2,
                y + Math.random() * 2,
                Math.random()
            ]);
        }
    }

    option = {
        backgroundColor: '#fff',

        tooltip : {
            trigger: 'item',
            formatter: '{b}'
        },

        series : [
            {
                name: '四川',
                type: 'map',
                mapType: 'china',
                roam: true,
                hoverable: false,
                data:[],
                heatmap: {
                    minAlpha: 0.1,
                    data: heatData
                },
                itemStyle: {
                    normal: {
                        borderColor:'rgba(100,149,237,0.6)',
                        borderWidth:0.5,
                        areaStyle: {
                            color: '#1b1b1b'
                        }
                    }
                }
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container10")[0]);
    var heatData = [];
    for (var i = 0; i < 20; ++i) {
        heatData.push([
            100 + Math.random() * 20,
            24 + Math.random() * 16,
            Math.random()
        ]);
    }
    for (var j = 0; j < 10; ++j) {
        var x = 100 + Math.random() * 16;
        var y = 24 + Math.random() * 12;
        var cnt = 30 * Math.random();
        for (var i = 0; i < cnt; ++i) {
            heatData.push([
                x + Math.random() * 2,
                y + Math.random() * 2,
                Math.random()
            ]);
        }
    }

    option = {
        backgroundColor: '#fff',

        tooltip : {
            trigger: 'item',
            formatter: '{b}'
        },

        series : [
            {
                name: '四川',
                type: 'map',
                mapType: 'china',
                roam: true,
                hoverable: false,
                data:[],
                heatmap: {
                    minAlpha: 0.1,
                    data: heatData
                },
                itemStyle: {
                    normal: {
                        borderColor:'rgba(100,149,237,0.6)',
                        borderWidth:0.5,
                        areaStyle: {
                            color: '#1b1b1b'
                        }
                    }
                }
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#container11")[0]);
    option = {

        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['生产区域事故分布量', '存储区域事故分布量','使用区域事故分布量']
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
                type : 'value/万',

            }
        ],
        series : [
            {
                name:'生产区域事故分布量',
                type:'line',
                data:[310, 302, 301, 34, 390, 330, 320,10, 302, 201, 334, 190,],


            },
            {
                name:'存储区域事故分布量',
                type:'line',
                data:[210, 102, 501, 534, 390, 1330, 1320,1110, 302, 401, 334, 190,],


            },
            {
                name:'使用区域事故分布量',
                type:'line',
                data:[20, 10, 51, 34, 39, 13, 13,11, 30, 11, 4, 19],


            }


        ]
    };
    myChart.setOption(option);
});