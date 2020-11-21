import $ from 'jquery';
// import BpmnModeler from 'bpmn-js';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import minimapModule from 'diagram-js-minimap';
import propertiesPanelModule from 'bpmn-js-properties-panel';
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda';


$(function () {
    const url = 'http://localhost:8080/process/428a86a6-2a10-11eb-a60b-da1aaa82783c/%E4%BA%8B%E4%BB%B6%E4%B8%8A%E6%8A%A5.bpmn20.xml';
    const container = $('#js-drop-zone');
    const bpmnJS = new BpmnModeler({
        container: '#canvas',
        propertiesPanel: {
            parent: '#js-properties-panel'
        },
        additionalModules: [
            minimapModule,
            propertiesPanelModule,
            propertiesProviderModule
        ],
    });
    openFromUrl(url, bpmnJS);
})

function openFromUrl(url, bpmnJS) {
    $.ajax(url, {dataType: 'json'}).done(async function (xml) {
        console.log(xml);
        try {
            await bpmnJS.importXML(xml.data.xml);
            bpmnJS.get('minimap').open();
            bpmnJS.get('canvas').zoom('fit-viewport');

        } catch (err) {
            console.error(err);
        }
    });
}