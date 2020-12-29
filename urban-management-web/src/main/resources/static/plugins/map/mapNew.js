var gridsetName = 'EPSG:4552';
var gridNames = ['EPSG:4552:0', 'EPSG:4552:1', 'EPSG:4552:2', 'EPSG:4552:3'];
var baseUrl = 'http://192.168.24.203:7881/geoserver/gwc/service/wmts';
var style = '';
var format = 'image/vnd.jpeg-png8';
var infoFormat = 'text/html';
var layerName = 'hegang:hegang';
var projection = new ol.proj.Projection({
    code: 'EPSG:4552',
    units: 'm',
    axisOrientation: 'neu'
});
var resolutions = [5.6, 2.8, 1.4, 0.7];
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
            tileSize: [256,256],
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
            // id: data,
            // name: name,
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
