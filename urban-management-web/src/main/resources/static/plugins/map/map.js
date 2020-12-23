

var center = [center_x, center_y];
var projection = new ol.proj.Projection({
    code: epsg,
    units: 'degrees',
    axisOrientation: 'neu',
    global: true
});
var untiled = new ol.layer.Image({
    source: new ol.source.ImageWMS({
        ratio: 1,
        url: url,
        params: {
            'VERSION': '1.1.1',
            "LAYERS": layers,
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
        zoom:zoom,
        projection: projection
    })
});
map.addControl(fullScreenControl)
var layerMap = new ol.layer.Vector({
    source: new ol.source.Vector(),
});
map.addLayer(layerMap);