
var center = [115.68192, 41.66462];
var projection = new ol.proj.Projection({
    code: 'EPSG:4326',
    units: 'degrees',
    axisOrientation: 'neu',
    global: true
});
var untiled = new ol.layer.Image({
    source: new ol.source.ImageWMS({
        ratio: 1,
        // url: 'http://192.168.24.203:7880/geoserver/hegang/wms',
        url: 'http://192.168.24.203:7880/geoserver/guyuan20201010/wms',
        params: {
            'VERSION': '1.1.1',
            "LAYERS": 'guyuan20201010:guyuan',
            "exceptions": 'application/vnd.ogc.se_inimage',
        }
    })
});
var source = new ol.source.Vector({wrapX: false});
var fullScreenControl = new ol.control.FullScreen();
var map = new ol.Map({
    target: 'map',
    layers: [
        untiled
    ],
    view: new ol.View({
        center:center,
        zoom:2,
        projection: projection
    })
});
