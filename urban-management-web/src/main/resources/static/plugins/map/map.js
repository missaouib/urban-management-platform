
var center = [594692, 5239498.31];
var projection = new ol.proj.Projection({
    code: 'EPSG:4552',
    units: 'degrees',
    axisOrientation: 'neu',
    global: true
});
var untiled = new ol.layer.Image({
    source: new ol.source.ImageWMS({
        ratio: 1,
        // url: 'http://192.168.24.203:7880/geoserver/hegang/wms',
        url: 'http://localhost:7880/geoserver/hegang/wms',
        params: {
            'VERSION': '1.1.1',
            "LAYERS": 'hegang:hegang',
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
