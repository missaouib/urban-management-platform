let selectSingleClick = new ol.interaction.Select();
map.addInteraction(selectSingleClick);
selectSingleClick.on('select', function (e) {
    let features = e.target.getFeatures().getArray();
    let data = features[0].H.data.id;
    selectSingleClickFun(data);
});

function selectSingleClickFun(data){
    if(data === null || data === undefined || data === ""){
        $("#eventIdListByBox").val("");
    }else{
        $("#eventIdListByBox").val(data);
    }
    $.table.search();
}