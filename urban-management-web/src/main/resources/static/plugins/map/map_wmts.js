var gridsetName = 'EPSG:4552';
var gridNames = ['EPSG:4552:0', 'EPSG:4552:1', 'EPSG:4552:2'];
var baseUrl = 'http://192.168.24.203:7880/geoserver/gwc/service/wmts';
var style = '';
var format = 'image/png';
var layerName = 'hegang:hegang';
var projection = new ol.proj.Projection({
    code: 'EPSG:4552',
    units: 'm',
    axisOrientation: 'neu'
});
var resolutions = [13.999999999999998, 6.999999999999999, 3.4999999999999996];
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
            tileSize: [400,400],
            extent: [374503.7646126483,4581664.560955403,626503.7646126483,5572864.560955403],
            origin: [374503.7646126483, 5572864.560955403],
            resolutions: resolutions,
            matrixIds: params['TILEMATRIX']
        }),
        style: params['STYLE'],
        wrapX: true
    });
    return source;
}

var layer = new ol.layer.Tile({
    source: constructSource()
});

var view = new ol.View({
    center: [0, 0],
    zoom: 2,
    projection: projection,
    extent: [374503.7646126483,4581664.560955403,626503.7646126483,5572864.560955403]
});

var map = new ol.Map({
    layers: [layer],
    target: 'map',
    view: view
});