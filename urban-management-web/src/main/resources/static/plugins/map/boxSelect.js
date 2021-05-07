let eventIdListByBox = [];
let vector;
let wkts = [];

function setBoxSelect() {
    let wktFormat = new ol.format.WKT();
    let features = [];
    for (let i = 0; i < wkts.length; i++) {
        let feature = wktFormat.readFeature(wkts[i].split("@")[0]);
        feature.setId(wkts[i].split("@")[1]);
        features.push(feature);
    }
    let vectorSource = new ol.source.Vector({
        features: features
    });
    vector = new ol.layer.Vector({
        source: vectorSource,
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(255, 0, 0, 0.2)'
            })
        })
    });
    map.addLayer(vector);
    let dragBox = new ol.interaction.DragBox({
        //condition : ol.events.condition.always  默认是always
        condition: ol.events.condition.platformModifierKeyOnly
    });
    map.addInteraction(dragBox);
    dragBox.on('boxend', function() {
        let extent = dragBox.getGeometry().getExtent();
        eventIdListByBox = [];
        vectorSource.forEachFeatureIntersectingExtent(extent, function(feature) {
            eventIdListByBox.push(feature.f);
        });
        $("#eventIdListByBox").val(eventIdListByBox);
        $.table.search();
    });
}