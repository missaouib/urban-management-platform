var gridsetName = 'EPSG:4552';
var gridNames = ['EPSG:4552:0', 'EPSG:4552:1', 'EPSG:4552:2', 'EPSG:4552:3', 'EPSG:4552:4', 'EPSG:4552:5', 'EPSG:4552:6', 'EPSG:4552:7'];
var baseUrl = 'http://218.10.223.8:9050/geoserver/gwc/service/wmts';
var style = '';
var format = 'image/png';
var infoFormat = 'text/html';
var layerName = 'hegang:hegang8';
var projection = new ol.proj.Projection({
    code: 'EPSG:4552',
    units: 'm',
    axisOrientation: 'neu'
});



var resolutions = [13.999999999999998, 6.999999999999999, 3.4999999999999996, 1.7499999999999998, 0.8749999999999999, 0.43749999999999994, 0.21874999999999997, 0.10937499999999999];
baseParams = ['VERSION','LAYER','STYLE','TILEMATRIX','TILEMATRIXSET','SERVICE','FORMAT'];

params = {
    'VERSION': '1.0.0',
    'LAYER': layerName,
    'STYLE': style,
    'TILEMATRIX': gridNames,
    'TILEMATRIXSET': gridsetName,
    'SERVICE': 'WMTS',
    'FORMAT': format
};

function constructSource() {
    var url = baseUrl+'?'
    for (var param in params) {
        if (baseParams.indexOf(param.toUpperCase()) < 0) {
            url = url + param + '=' + params[param] + '&';
        }
    }
    url = url.slice(0, -1);

    var source = new ol.source.WMTS({
        url: url,
        layer: params['LAYER'],
        matrixSet: params['TILEMATRIXSET'],
        format: params['FORMAT'],
        projection: projection,
        tileGrid: new ol.tilegrid.WMTS({
            tileSize: [512,512],
            extent: [374503.7646126483,4581664.560955403,626817.3646126483,5570848.560955403],
            origin: [374503.7646126483, 5570848.560955403],
            resolutions: resolutions,
            matrixIds: params['TILEMATRIX']
        }),
        style: params['STYLE'],
        wrapX: true
    });
    return source;
}

var untiled = new ol.layer.Tile({
    source: constructSource()
});

var view = new ol.View({
    center: [0, 0],
    zoom: 17,
    resolutions: resolutions,
    projection: projection,
    extent: [374503.7646126483,4581664.560955403,626817.3646126483,5570848.560955403]
});

var map = new ol.Map({
    layers: [untiled],
    target: 'map',
    view: view
});
map.getView().fit([593489.95, 5237489.95,
    598010.0499999999, 5247510.05], map.getSize());
var fullScreenControl = new ol.control.FullScreen();
var scaleLineControl = new ol.control.ScaleLine();

map.addControl(fullScreenControl);
map.addControl(scaleLineControl);
var layerMap = new ol.layer.Vector({
    source: new ol.source.Vector(),
});
map.addLayer(layerMap);
function setPointForCenter(x, y) {
    let point = new ol.Feature({
        geometry: new ol.geom.Point([x, y]),
        data: {
            coordinate: x + "," + y
        }
    });
    layerMap.getSource().addFeature(point);
    point.setStyle(new ol.style.Style({
        image: new ol.style.Icon({
            src: '../img/dwpoint5.png'
        })
    }));
}

/*
*   测距
* */
//定义矢量数据源
var source = new ol.source.Vector();
//定义矢量图层
var ranging = new ol.layer.Vector({
    source: source,
    style: new ol.style.Style({
        fill: new ol.style.Fill({
            color: 'rgba(255,255,255,0.2)'
        }),
        stroke: new ol.style.Stroke({
            color: '#e21e0a',
            width: 2
        }),
        image: new ol.style.Circle({
            radius: 5,
            fill: new ol.style.Fill({
                color: '#ffcc33'
            })
        })
    })
});
//将矢量图层添加到地图中
map.addLayer(ranging);


//实例化鼠标位置控件
var mousePositionControl = new ol.control.MousePosition({
    coodrdinateFormat: ol.coordinate.createStringXY(4), //坐标格式
    //地图投影坐标系
    projection: new ol.proj.Projection({
        code: 'EPSG:4326', //投影编码
        units: 'degrees',
        axisOrientation: 'neu'
    }),
    //className:'tip',
    target: document.getElementById('tip'), //显示鼠标位置信息的目标容器
    undefinedHTML: '&nbsp;' //未定义坐标标记
});

//添加鼠标位置控件
map.addControl(mousePositionControl);

//实例化鹰眼控件
var overviewMapControl = new ol.control.OverviewMap({
    //在鹰眼中相同坐标系下不通数据源的图层
    layers: [
        new ol.layer.Tile({
            source: new ol.source.XYZ({
                'url': 'https://{a-c}.tiles.mapbox.com/v3/mapbox.blue-marble-topo-bathy-jan/{z}/{x}/{y}.png'
            })
        })
    ],
    collapseLabel: '\u00BB',
    lable: '\u00AB',
    collapsed: false,
});
//添加鹰眼
// map.addControl(overviewMapControl);

//创建一个WGS84球体对象
var wgs84Sphere = new ol.Sphere(6378137);
//创建一个当前要绘制的对象
var sketch = new ol.Feature();
//创建一个帮助提示框对象
var helpTooltipElement;
//创建一个帮助提示信息对象
var helpTooltip;
//创建一个测量提示框对象
var measureTooltipElement;
//创建一个测量提示信息对象
var measureTooltip;
//继续绘制多边形的提示信息
var continuePolygonMsg = '单击以继续绘制多边形，双击结束绘制';
//继续绘制线段的提示信息
var continueLineMsg = '单击以继续绘制直线，双击结束绘制';

var pointerMoveHandler

function setMove() {
    //鼠标移动触发的函数
    pointerMoveHandler = map.on('pointermove', (evt) => {
        //Indicates if the map is currently being dragged.
        //Only set for POINTERDRAG and POINTERMOVE events. Default is false.
        //如果是平移地图则直接结束
        if (evt.dragging) {
            return;
        }
        //帮助提示信息
        var helpMsg = '单击开始';

        if (sketch) {
            //获取绘图对象的几何要素
            var geom = sketch.getGeometry();
            //如果当前绘制的几何要素是多线段，则将绘制提示信息设置为多线段绘制提示信息
            if (geom instanceof ol.geom.Polygon) {
                helpMsg = continuePolygonMsg;
            } else if (geom instanceof ol.geom.LineString) {
                helpMsg = continueLineMsg;
            }
        }
        //设置帮助提示要素的内标签为帮助提示信息
        helpTooltipElement.innerHTML = helpMsg;
        //设置帮助提示信息的位置
        helpTooltip.setPosition(evt.coordinate);
        //移除帮助提示要素的隐藏样式
        $(helpTooltipElement).removeClass('hidden');
    });
    return pointerMoveHandler;
}
//触发pointermove事件
// map.on('pointermove', pointerMoveHandler);


function delMove() {
    ol.Observable.unByKey(pointerMoveHandler);
}

//当鼠标移除地图视图的时为帮助提示要素添加隐藏样式
$(map.getViewport()).on('mouseout', function() {
    $(helpTooltipElement).addClass('hidden');
});

//获取大地测量复选框
var geodesicCheckbox = document.getElementById('geodesic');
//获取类型
var typeSelect = document.getElementById('type');
//定义一个交互式绘图对象
var draw;

//添加交互式绘图对象的函数
function addInteraction() {
    // 获取当前选择的绘制类型
    var type = typeSelect.value == 'area' ? 'Polygon' : 'LineString';
    //创建一个交互式绘图对象
    draw = new ol.interaction.Draw({
        //绘制的数据源
        source: source,
        //绘制类型
        type: type,
        //样式
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(255,255,255,0.2)'
            }),
            stroke: new ol.style.Stroke({
                color: 'rgba(0,0,0,0.5)',
                lineDash: [10, 10],
                width: 2
            }),
            image: new ol.style.Circle({
                radius: 5,
                stroke: new ol.style.Stroke({
                    color: 'rgba(0,0,0,0.7)'
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(255,255,255,0.2)'
                })
            })
        })
    });
    //将交互绘图对象添加到地图中
    // map.addInteraction(draw);

    //创建测量提示框
    createMeasureTooltip();
    //创建帮助提示框
    createHelpTooltip();

    //定义一个事件监听
    var listener;
    //定义一个控制鼠标点击次数的变量
    var count = 0;
    //绘制开始事件
    draw.on('drawstart', function(evt) {
        //The feature being drawn.
        sketch = evt.feature;
        //提示框的坐标
        var tooltipCoord = evt.coordinate;
        //监听几何要素的change事件
        //Increases the revision counter and dispatches a 'change' event.

        listener = sketch.getGeometry().on('change', function(evt) {
            //The event target.
            //获取绘制的几何对象
            var geom = evt.target;
            //定义一个输出对象，用于记录面积和长度
            var output;
            if (geom instanceof ol.geom.Polygon) {
                map.removeEventListener('singleclick');
                map.removeEventListener('dblclick');
                //输出多边形的面积
                output = formatArea(geom);
                //获取多变形内部点的坐标
                tooltipCoord = geom.getInteriorPoint().getCoordinates();
            } else if (geom instanceof ol.geom.LineString) {
                //输出多线段的长度
                output = formatLength(geom);
                //获取多线段的最后一个点的坐标
                tooltipCoord = geom.getLastCoordinate();
            }

            //设置测量提示框的内标签为最终输出结果
            measureTooltipElement.innerHTML = output;
            //设置测量提示信息的位置坐标
            measureTooltip.setPosition(tooltipCoord);
        });

        //地图单击事件
        map.on('singleclick', function(evt) {
            //设置测量提示信息的位置坐标，用来确定鼠标点击后测量提示框的位置
            measureTooltip.setPosition(evt.coordinate);
            //如果是第一次点击，则设置测量提示框的文本内容为起点
            if (count == 0) {
                measureTooltipElement.innerHTML = "起点";
            }
            //根据鼠标点击位置生成一个点
            var point = new ol.geom.Point(evt.coordinate);
            //将该点要素添加到矢量数据源中
            source.addFeature(new ol.Feature(point));
            //更改测量提示框的样式，使测量提示框可见
            measureTooltipElement.className = 'tooltip tooltip-static';
            //创建测量提示框
            createMeasureTooltip();
            //点击次数增加
            count++;
        });

        //地图双击事件
        map.on('dblclick', function(evt) {
            var point = new ol.geom.Point(evt.coordinate);
            source.addFeature(new ol.Feature(point));
        });
    }, this);
    //绘制结束事件
    draw.on('drawend', function(evt) {
        count = 0;
        //设置测量提示框的样式
        measureTooltipElement.className = 'tooltip tooltip-static';
        //设置偏移量
        measureTooltip.setOffset([0, -7]);
        //清空绘制要素
        sketch = null;
        //清空测量提示要素
        measureTooltipElement = null;
        //创建测量提示框
        createMeasureTooltip();
        //移除事件监听
        ol.Observable.unByKey(listener);
        //移除地图单击事件
        map.removeEventListener('singleclick');
    }, this);
}

//创建帮助提示框
function createHelpTooltip() {
    //如果已经存在帮助提示框则移除
    if (helpTooltipElement) {
        helpTooltipElement.parentNode.removeChild(helpTooltipElement);
    }
    //创建帮助提示要素的div
    helpTooltipElement = document.createElement('div');
    //设置帮助提示要素的样式
    helpTooltipElement.className = 'tooltip hidden';
    //创建一个帮助提示的覆盖标注
    helpTooltip = new ol.Overlay({
        element: helpTooltipElement,
        offset: [15, 0],
        positioning: 'center-left'
    });
    //将帮助提示的覆盖标注添加到地图中
    map.addOverlay(helpTooltip);
}
//创建测量提示框
function createMeasureTooltip() {
    //创建测量提示框的div
    measureTooltipElement = document.createElement('div');
    measureTooltipElement.setAttribute('id', 'lengthLabel');
    //设置测量提示要素的样式
    measureTooltipElement.className = 'tooltip tooltip-measure';
    //创建一个测量提示的覆盖标注
    measureTooltip = new ol.Overlay({
        element: measureTooltipElement,
        offset: [0, -15],
        positioning: 'bottom-center'
    });
    //将测量提示的覆盖标注添加到地图中
    map.addOverlay(measureTooltip);
}
//测量类型发生改变时触发事件
typeSelect.onchange = function() {
    //移除之前的绘制对象
    map.removeInteraction(draw);
    //重新进行绘制
    addInteraction();
};

//格式化测量长度
var formatLength = function(line) {
    //定义长度变量
    var length;
    //如果大地测量复选框被勾选，则计算球面距离
    if (geodesicCheckbox.checked) {
        //获取坐标串
        var coordinates = line.getCoordinates();
        //初始长度为0
        length = 0;
        //获取源数据的坐标系
        var sourceProj = map.getView().getProjection();
        //进行点的坐标转换
        for (var i = 0; i < coordinates.length - 1; i++) {
            //第一个点
            var c1 = ol.proj.transform(coordinates[i], sourceProj, 'EPSG:4326');
            //第二个点
            var c2 = ol.proj.transform(coordinates[i + 1], sourceProj, 'EPSG:4326');
            //获取转换后的球面距离
            //Returns the distance from c1 to c2 using the haversine formula.
            length += wgs84Sphere.haversineDistance(c1, c2);
        }
    } else {
        //计算平面距离
        length = Math.round(line.getLength() * 100) / 100;
    }
    //定义输出变量
    var output;
    //如果长度大于1000，则使用km单位，否则使用m单位
    if (length > 1000) {
        output = (Math.round(length / 1000 * 100) / 100) + ' ' + 'km'; //换算成KM单位
    } else {
        output = (Math.round(length * 100) / 100) + ' ' + 'm'; //m为单位
    }
    return output;
};

//格式化测量面积
var formatArea = function(polygon) {
    //定义面积变量
    var area;
    //如果大地测量复选框被勾选，则计算球面面积
    if (geodesicCheckbox.checked) {
        //获取初始坐标系
        var sourceProj = map.getView().getProjection();
        var geom = polygon.clone().transform(sourceProj, 'EPSG:4326');
        //获取多边形的坐标系
        var coordinates = geom.getLinearRing(0).getCoordinates();
        //获取球面面积
        area = Math.abs(wgs84Sphere.geodesicArea(coordinates));
    } else {
        //获取平面面积
        area = polygon.getArea();
    }
    //定义输出变量
    var output;
    //当面积大于10000时，转换为平方千米，否则为平方米
    if (area > 10000) {
        output = (Math.round(area / 1000000 * 100) / 100) + ' ' + 'km<sup>2</sup>';
    } else {
        output = (Math.round(area * 100) / 100) + ' ' + 'm<sup>2</sup>';
    }
    return output;
};
//添加交互绘图对象
addInteraction();


/*
*  开启和关闭 测距事件 清空已完成测距
* */
$(".cejv").click(function() {
    setMove()
    // console.log(singleClick)
    // ol.Observable.unByKey(singleClick);
    map.addInteraction(draw);
    $(this).addClass('roleBox');
    $(".removeCejv").removeClass('roleBox');
    $("#type").removeAttr("disabled","disabled")
});
$("#type").change(function() {
    setMove()
    addInteraction();
    map.addInteraction(draw);
});

$(".removeCejv").click(function() {
    delMove();
    map.removeInteraction(draw);
    $(this).addClass('roleBox');
    $(".cejv").removeClass('roleBox');
    $("#type").attr("disabled","disabled")
});

$(".clean_cejv").click(function() {
    ranging.getSource().clear();
    $(".ol-overlay-container").remove()
})