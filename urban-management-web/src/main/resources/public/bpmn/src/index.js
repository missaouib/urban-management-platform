import $ from 'jquery';
// import BpmnModeler from 'bpmn-js';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import propertiesPanelModule from 'bpmn-js-properties-panel';
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda';


$(function () {
    const bpmnJS = new BpmnModeler({
        container: '#canvas',
        propertiesPanel: {
            parent: '#js-properties-panel'
        },
        additionalModules: [
            propertiesPanelModule,
            propertiesProviderModule
        ],
    });
    const xml = `<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/processdef">
  <process id="event" name="事件上报" isExecutable="true">
    <startEvent id="sid-A48ED47A-EEF4-419C-A3C7-6EC5F3A91E6A" name="事件上报"></startEvent>
    <userTask id="sid-608DDE8D-47E8-4F01-8ECB-DE3DF1B9A6D4" name="受理员-信息收集" ></userTask>
    <exclusiveGateway id="sid-30EEC8F0-7558-463E-9405-E2DE1FCA5229"></exclusiveGateway>
    <endEvent id="sid-BAA97413-FBDB-4F9C-B315-391C52B194CB"></endEvent>
    <userTask id="sid-F3FE4875-BE03-4DDC-BC81-4DEF3CF3E1EB" name="派遣员-派遣" ></userTask>
    <userTask id="sid-B427AE03-8E97-4B2F-987E-29D778AC9185" name="专业部门" ></userTask>
    <exclusiveGateway id="sid-43BAF8DC-103A-4D1A-8C9E-964BBF648EC6"></exclusiveGateway>
    <userTask id="sid-FC915893-D9BA-448F-AE1C-63B0C53FB1D6" name="受理员-核查" ></userTask>
    <userTask id="sid-4910EFEF-4A30-4670-8BC5-AB58E04C2FC5" name="值班长-立案" ></userTask>
    <exclusiveGateway id="sid-F86559B5-761F-4998-8104-810ACE3C7094"></exclusiveGateway>
    <exclusiveGateway id="sid-E3C09776-C1CE-4068-A57D-49C405AF5C9F"></exclusiveGateway>
    <userTask id="sid-03F34F77-DCCA-4BD8-A06B-CC2608A17890" name="监督员-信息核实"></userTask>
    <exclusiveGateway id="sid-75300A94-430A-4140-A896-C1EDC84A6D13"></exclusiveGateway>
    <userTask id="sid-35BCBA50-2D79-455B-B796-F3899009228B" name="监督员-案件核查"></userTask>
    <userTask id="sid-3DDF6166-6305-4FA5-B0B5-F69150CA3583" name="值班长-结案" ></userTask>
    <endEvent id="sid-5B0DE82A-20F6-476E-8597-454502FD560A"></endEvent>
    <userTask id="sid-AB42268F-F2EA-4872-BF03-EA52DFAACEB1" name="受理员" ></userTask>
    <sequenceFlow id="sid-C349EFC2-2C9D-4942-B777-F018226AEBC6" sourceRef="sid-03F34F77-DCCA-4BD8-A06B-CC2608A17890" targetRef="sid-608DDE8D-47E8-4F01-8ECB-DE3DF1B9A6D4"></sequenceFlow>
    <sequenceFlow id="sid-C4AAD876-2BB1-48A3-BD5E-AE8932429C58" sourceRef="sid-608DDE8D-47E8-4F01-8ECB-DE3DF1B9A6D4" targetRef="sid-30EEC8F0-7558-463E-9405-E2DE1FCA5229"></sequenceFlow>
    <sequenceFlow id="sid-43F41ABF-2868-430D-926C-514AECE90939" sourceRef="sid-AB42268F-F2EA-4872-BF03-EA52DFAACEB1" targetRef="sid-75300A94-430A-4140-A896-C1EDC84A6D13"></sequenceFlow>
    <sequenceFlow id="sid-6DF9E1D0-204D-468E-8392-BFFB7F051733" name="结案" sourceRef="sid-3DDF6166-6305-4FA5-B0B5-F69150CA3583" targetRef="sid-5B0DE82A-20F6-476E-8597-454502FD560A"></sequenceFlow>
    <userTask id="sid-109B53AB-B86D-4049-9231-1568F393411A" name="受理员-案件登记" ></userTask>
    <sequenceFlow id="sid-F366DFEF-B0AC-4BC2-8381-FC771876B143" name="派核实" sourceRef="sid-109B53AB-B86D-4049-9231-1568F393411A" targetRef="sid-03F34F77-DCCA-4BD8-A06B-CC2608A17890"></sequenceFlow>
    <sequenceFlow id="sid-F1E3D770-9626-43FB-92D3-18E60DBC76F5" name="再次核查" sourceRef="sid-75300A94-430A-4140-A896-C1EDC84A6D13" targetRef="sid-35BCBA50-2D79-455B-B796-F3899009228B">
    </sequenceFlow>
    <sequenceFlow id="sid-F4B42A5F-08D7-475F-B127-EFCD78ED8DD7" name="再次派遣" sourceRef="sid-75300A94-430A-4140-A896-C1EDC84A6D13" targetRef="sid-F3FE4875-BE03-4DDC-BC81-4DEF3CF3E1EB">
    </sequenceFlow>
    <userTask id="sid-4A099D45-A4E2-42C4-B747-9DAF5924B7EC" name="派遣员-挂帐审批" ></userTask>
    <userTask id="sid-47944694-361A-4FB8-B37B-59FC21610BE3" name="派遣员-回退审批" ></userTask>
    <exclusiveGateway id="sid-D22848FE-1DB7-4D29-A638-99789EF9AFA0"></exclusiveGateway>
    <exclusiveGateway id="sid-A0651375-BDDA-4CAC-91E5-3F5D30E750D2"></exclusiveGateway>
    <userTask id="sid-B39121B6-06A8-4425-8FEF-379D5346F1B8" name="值班长-作废审批" ></userTask>
    <exclusiveGateway id="sid-8DAA6E25-0018-4A65-9811-70DB2E57385D"></exclusiveGateway>
    <endEvent id="sid-3F313DAD-1453-4CAF-B539-4A3F5F0D830A"></endEvent>
    <userTask id="sid-AF959651-A814-4461-9B86-62B7D3F0C8AB" name="派遣员-延时审批" ></userTask>
    <exclusiveGateway id="sid-A27A1B36-E43B-4CE0-9ABB-CB893F542EA2"></exclusiveGateway>
    <sequenceFlow id="sid-B0676D03-1C72-442C-89D9-1EDC10D03805" sourceRef="sid-AF959651-A814-4461-9B86-62B7D3F0C8AB" targetRef="sid-A27A1B36-E43B-4CE0-9ABB-CB893F542EA2"></sequenceFlow>
    <sequenceFlow id="sid-B5FE7E77-2F78-409C-B8D7-94F104846177" name="核查反馈" sourceRef="sid-35BCBA50-2D79-455B-B796-F3899009228B" targetRef="sid-AB42268F-F2EA-4872-BF03-EA52DFAACEB1"></sequenceFlow>
    <sequenceFlow id="sid-18A79244-9EB1-46F6-8C6A-36A0AA44B399" name="转结案" sourceRef="sid-75300A94-430A-4140-A896-C1EDC84A6D13" targetRef="sid-3DDF6166-6305-4FA5-B0B5-F69150CA3583">
    </sequenceFlow>
    <sequenceFlow id="sid-F20A68E8-1642-4AE8-9CE1-4212B02962D3" sourceRef="sid-30EEC8F0-7558-463E-9405-E2DE1FCA5229" targetRef="sid-03F34F77-DCCA-4BD8-A06B-CC2608A17890">
    </sequenceFlow>
    <sequenceFlow id="sid-959DFF43-FC0D-4EAA-BF50-CAF9C36DD3F4" sourceRef="sid-A48ED47A-EEF4-419C-A3C7-6EC5F3A91E6A" targetRef="sid-E3C09776-C1CE-4068-A57D-49C405AF5C9F"></sequenceFlow>
    <sequenceFlow id="sid-16B3D265-A2F4-45CB-A747-C9CF24C07778" sourceRef="sid-E3C09776-C1CE-4068-A57D-49C405AF5C9F" targetRef="sid-109B53AB-B86D-4049-9231-1568F393411A">
    </sequenceFlow>
    <sequenceFlow id="sid-2C504FB2-CA16-4805-B390-325778262047" sourceRef="sid-E3C09776-C1CE-4068-A57D-49C405AF5C9F" targetRef="sid-608DDE8D-47E8-4F01-8ECB-DE3DF1B9A6D4">
    </sequenceFlow>
    <sequenceFlow id="sid-907C55F1-6C0E-4A84-9D99-2DD1AD8E4B2E" name="派核查" sourceRef="sid-FC915893-D9BA-448F-AE1C-63B0C53FB1D6" targetRef="sid-35BCBA50-2D79-455B-B796-F3899009228B"></sequenceFlow>
    <sequenceFlow id="sid-0EECFDC4-8EFA-4D24-8CB6-6317A48BE97F" sourceRef="sid-4910EFEF-4A30-4670-8BC5-AB58E04C2FC5" targetRef="sid-F86559B5-761F-4998-8104-810ACE3C7094"></sequenceFlow>
    <sequenceFlow id="sid-739E3487-3317-49A2-B15F-4436DD6C887E" name="派遣" sourceRef="sid-F3FE4875-BE03-4DDC-BC81-4DEF3CF3E1EB" targetRef="sid-B427AE03-8E97-4B2F-987E-29D778AC9185">
    </sequenceFlow>
    <sequenceFlow id="sid-737E3818-1D7F-49A0-BAAC-891CB65D4934" sourceRef="sid-4A099D45-A4E2-42C4-B747-9DAF5924B7EC" targetRef="sid-A0651375-BDDA-4CAC-91E5-3F5D30E750D2"></sequenceFlow>
    <sequenceFlow id="sid-860EAA3D-7881-4EB0-BE0D-DD82F3153F4D" sourceRef="sid-B39121B6-06A8-4425-8FEF-379D5346F1B8" targetRef="sid-8DAA6E25-0018-4A65-9811-70DB2E57385D"></sequenceFlow>
    <sequenceFlow id="sid-629EEE51-9575-49AD-B15F-AB55C489FA01" name="通过" sourceRef="sid-8DAA6E25-0018-4A65-9811-70DB2E57385D" targetRef="sid-3F313DAD-1453-4CAF-B539-4A3F5F0D830A">
    </sequenceFlow>
    <sequenceFlow id="sid-C4F7087C-533E-4A22-9896-CBB9A903116B" name="不通过" sourceRef="sid-8DAA6E25-0018-4A65-9811-70DB2E57385D" targetRef="sid-47944694-361A-4FB8-B37B-59FC21610BE3">
    </sequenceFlow>
    <sequenceFlow id="sid-0F5F32CD-DC8E-46A9-9364-4BFE68ED462A" name="通过" sourceRef="sid-A0651375-BDDA-4CAC-91E5-3F5D30E750D2" targetRef="sid-DBBCC1C1-1EA3-454A-8D48-8ABFC2B21C54">
    </sequenceFlow>
    <sequenceFlow id="sid-AFF23328-C45E-4BFC-B7D4-578918C6141F" name="不通过" sourceRef="sid-A0651375-BDDA-4CAC-91E5-3F5D30E750D2" targetRef="sid-B427AE03-8E97-4B2F-987E-29D778AC9185">
    </sequenceFlow>
    <sequenceFlow id="sid-ED52A86B-A97B-4FC2-8806-5200A2203AA7" sourceRef="sid-47944694-361A-4FB8-B37B-59FC21610BE3" targetRef="sid-D22848FE-1DB7-4D29-A638-99789EF9AFA0"></sequenceFlow>
    <sequenceFlow id="sid-E7CB2774-2945-4E26-A4C3-AC4AB4333B3D" name="立案" sourceRef="sid-F86559B5-761F-4998-8104-810ACE3C7094" targetRef="sid-F3FE4875-BE03-4DDC-BC81-4DEF3CF3E1EB">
    </sequenceFlow>
    <sequenceFlow id="sid-BA513A2C-29BF-4C40-9F44-1476A4CFC3B6" name="回退" sourceRef="sid-F86559B5-761F-4998-8104-810ACE3C7094" targetRef="sid-608DDE8D-47E8-4F01-8ECB-DE3DF1B9A6D4">
    </sequenceFlow>
    <sequenceFlow id="sid-C80EEBC4-1FC2-4713-AC0F-BC04A622953C" name="不予受理" sourceRef="sid-30EEC8F0-7558-463E-9405-E2DE1FCA5229" targetRef="sid-BAA97413-FBDB-4F9C-B315-391C52B194CB">
    </sequenceFlow>
    <sequenceFlow id="sid-1123DB20-2510-46A1-B9EB-0807FA223AD3" name="转立案" sourceRef="sid-30EEC8F0-7558-463E-9405-E2DE1FCA5229" targetRef="sid-4910EFEF-4A30-4670-8BC5-AB58E04C2FC5">
    </sequenceFlow>
    <sequenceFlow id="sid-D7D76FC6-03A7-42AA-9F62-C1065E857867" name="通过" sourceRef="sid-A27A1B36-E43B-4CE0-9ABB-CB893F542EA2" targetRef="sid-B427AE03-8E97-4B2F-987E-29D778AC9185">
    </sequenceFlow>
    <sequenceFlow id="sid-9A188072-2E7B-456F-9D6D-81CBCE3F4812" name="通过" sourceRef="sid-D22848FE-1DB7-4D29-A638-99789EF9AFA0" targetRef="sid-F3FE4875-BE03-4DDC-BC81-4DEF3CF3E1EB">
    </sequenceFlow>
    <sequenceFlow id="sid-B31EC12C-F411-4361-8E3E-8BB2A86B7992" name="申请作废" sourceRef="sid-D22848FE-1DB7-4D29-A638-99789EF9AFA0" targetRef="sid-B39121B6-06A8-4425-8FEF-379D5346F1B8"></sequenceFlow>
    <sequenceFlow id="sid-0DF707B9-C06E-478D-832B-1246036DFBAE" name="申请挂帐" sourceRef="sid-43BAF8DC-103A-4D1A-8C9E-964BBF648EC6" targetRef="sid-4A099D45-A4E2-42C4-B747-9DAF5924B7EC">
    </sequenceFlow>
    <sequenceFlow id="sid-1A3CC65B-BA50-4D9C-8CAF-E38AD4E8D833" name="转核查" sourceRef="sid-43BAF8DC-103A-4D1A-8C9E-964BBF648EC6" targetRef="sid-FC915893-D9BA-448F-AE1C-63B0C53FB1D6">
    </sequenceFlow>
    <sequenceFlow id="sid-EDA974E4-5272-402C-A26D-A0E3B6E56EEA" name="申请延时" sourceRef="sid-43BAF8DC-103A-4D1A-8C9E-964BBF648EC6" targetRef="sid-AF959651-A814-4461-9B86-62B7D3F0C8AB">
    </sequenceFlow>
    <sequenceFlow id="sid-A39C8B44-8E56-4854-B12B-C0EC0D4BD835" name="申请回退" sourceRef="sid-43BAF8DC-103A-4D1A-8C9E-964BBF648EC6" targetRef="sid-47944694-361A-4FB8-B37B-59FC21610BE3">
    </sequenceFlow>
    <sequenceFlow id="sid-A5A042BE-30D0-44BC-80F3-9EC65DE3CF0D" sourceRef="sid-B427AE03-8E97-4B2F-987E-29D778AC9185" targetRef="sid-43BAF8DC-103A-4D1A-8C9E-964BBF648EC6"></sequenceFlow>
    <sequenceFlow id="sid-2B6AB4B3-F648-4846-BE15-68F247750A2A" name="不通过" sourceRef="sid-A27A1B36-E43B-4CE0-9ABB-CB893F542EA2" targetRef="sid-B427AE03-8E97-4B2F-987E-29D778AC9185">
    </sequenceFlow>
    <sequenceFlow id="sid-3E749F13-C2E2-4A5B-AC82-E4EBAAF50F2F" name="不通过" sourceRef="sid-D22848FE-1DB7-4D29-A638-99789EF9AFA0" targetRef="sid-B427AE03-8E97-4B2F-987E-29D778AC9185">
    </sequenceFlow>
    <userTask id="sid-DBBCC1C1-1EA3-454A-8D48-8ABFC2B21C54" name="挂帐恢复" ></userTask>
    <sequenceFlow id="sid-8B4AECB5-FF56-4B4D-B3DF-660DF0B2150A" sourceRef="sid-DBBCC1C1-1EA3-454A-8D48-8ABFC2B21C54" targetRef="sid-B427AE03-8E97-4B2F-987E-29D778AC9185"></sequenceFlow>
    <textAnnotation id="sid-142798F0-9DCF-41DB-BD40-60D1B34B7F47">
      <text>监督员上报</text>
    </textAnnotation>
    <textAnnotation id="sid-1E7E2743-7912-48F0-9E8A-357FC42850EF">
      <text>受理员上报</text>
    </textAnnotation>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_event">
    <bpmndi:BPMNPlane bpmnElement="event" id="BPMNPlane_event">
      <bpmndi:BPMNShape bpmnElement="sid-A48ED47A-EEF4-419C-A3C7-6EC5F3A91E6A" id="BPMNShape_sid-A48ED47A-EEF4-419C-A3C7-6EC5F3A91E6A">
        <omgdc:Bounds height="30.0" width="30.0" x="240.6861292981207" y="281.5"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-608DDE8D-47E8-4F01-8ECB-DE3DF1B9A6D4" id="BPMNShape_sid-608DDE8D-47E8-4F01-8ECB-DE3DF1B9A6D4">
        <omgdc:Bounds height="80.0" width="100.0" x="450.0" y="135.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-30EEC8F0-7558-463E-9405-E2DE1FCA5229" id="BPMNShape_sid-30EEC8F0-7558-463E-9405-E2DE1FCA5229">
        <omgdc:Bounds height="40.0" width="40.0" x="630.0" y="155.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-BAA97413-FBDB-4F9C-B315-391C52B194CB" id="BPMNShape_sid-BAA97413-FBDB-4F9C-B315-391C52B194CB">
        <omgdc:Bounds height="28.0" width="28.0" x="636.0" y="75.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-F3FE4875-BE03-4DDC-BC81-4DEF3CF3E1EB" id="BPMNShape_sid-F3FE4875-BE03-4DDC-BC81-4DEF3CF3E1EB">
        <omgdc:Bounds height="80.0" width="100.0" x="975.0" y="135.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-B427AE03-8E97-4B2F-987E-29D778AC9185" id="BPMNShape_sid-B427AE03-8E97-4B2F-987E-29D778AC9185">
        <omgdc:Bounds height="80.0" width="100.0" x="1125.0" y="135.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-43BAF8DC-103A-4D1A-8C9E-964BBF648EC6" id="BPMNShape_sid-43BAF8DC-103A-4D1A-8C9E-964BBF648EC6">
        <omgdc:Bounds height="40.0" width="40.0" x="1155.0" y="455.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-FC915893-D9BA-448F-AE1C-63B0C53FB1D6" id="BPMNShape_sid-FC915893-D9BA-448F-AE1C-63B0C53FB1D6">
        <omgdc:Bounds height="80.0" width="100.0" x="1005.0" y="435.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-4910EFEF-4A30-4670-8BC5-AB58E04C2FC5" id="BPMNShape_sid-4910EFEF-4A30-4670-8BC5-AB58E04C2FC5">
        <omgdc:Bounds height="84.99999999999997" width="99.0" x="735.5" y="132.50000000000003"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-F86559B5-761F-4998-8104-810ACE3C7094" id="BPMNShape_sid-F86559B5-761F-4998-8104-810ACE3C7094">
        <omgdc:Bounds height="40.0" width="40.0" x="885.0" y="155.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-E3C09776-C1CE-4068-A57D-49C405AF5C9F" id="BPMNShape_sid-E3C09776-C1CE-4068-A57D-49C405AF5C9F">
        <omgdc:Bounds height="40.0" width="40.0" x="480.0" y="276.5"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-03F34F77-DCCA-4BD8-A06B-CC2608A17890" id="BPMNShape_sid-03F34F77-DCCA-4BD8-A06B-CC2608A17890">
        <omgdc:Bounds height="80.0" width="100.0" x="600.0" y="390.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-142798F0-9DCF-41DB-BD40-60D1B34B7F47" id="BPMNShape_sid-142798F0-9DCF-41DB-BD40-60D1B34B7F47">
        <omgdc:Bounds height="50.0" width="100.0" x="390.0" y="210.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-1E7E2743-7912-48F0-9E8A-357FC42850EF" id="BPMNShape_sid-1E7E2743-7912-48F0-9E8A-357FC42850EF">
        <omgdc:Bounds height="50.0" width="100.0" x="390.0" y="315.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-75300A94-430A-4140-A896-C1EDC84A6D13" id="BPMNShape_sid-75300A94-430A-4140-A896-C1EDC84A6D13">
        <omgdc:Bounds height="40.0" width="40.0" x="691.6861292981207" y="542.4509063359048"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-35BCBA50-2D79-455B-B796-F3899009228B" id="BPMNShape_sid-35BCBA50-2D79-455B-B796-F3899009228B">
        <omgdc:Bounds height="80.0" width="100.0" x="1005.0" y="735.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-3DDF6166-6305-4FA5-B0B5-F69150CA3583" id="BPMNShape_sid-3DDF6166-6305-4FA5-B0B5-F69150CA3583">
        <omgdc:Bounds height="80.0" width="100.00000000000023" x="416.6861292981207" y="523.2254531679524"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-5B0DE82A-20F6-476E-8597-454502FD560A" id="BPMNShape_sid-5B0DE82A-20F6-476E-8597-454502FD560A">
        <omgdc:Bounds height="28.0" width="28.0" x="241.6861292981207" y="549.4509063359048"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-AB42268F-F2EA-4872-BF03-EA52DFAACEB1" id="BPMNShape_sid-AB42268F-F2EA-4872-BF03-EA52DFAACEB1">
        <omgdc:Bounds height="80.0" width="100.0" x="796.3138707018793" y="523.2254531679524"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-109B53AB-B86D-4049-9231-1568F393411A" id="BPMNShape_sid-109B53AB-B86D-4049-9231-1568F393411A">
        <omgdc:Bounds height="80.0" width="100.0" x="450.0" y="390.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-4A099D45-A4E2-42C4-B747-9DAF5924B7EC" id="BPMNShape_sid-4A099D45-A4E2-42C4-B747-9DAF5924B7EC">
        <omgdc:Bounds height="80.0" width="100.0" x="1275.0" y="735.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-47944694-361A-4FB8-B37B-59FC21610BE3" id="BPMNShape_sid-47944694-361A-4FB8-B37B-59FC21610BE3">
        <omgdc:Bounds height="80.0" width="100.0" x="1350.0" y="495.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-D22848FE-1DB7-4D29-A638-99789EF9AFA0" id="BPMNShape_sid-D22848FE-1DB7-4D29-A638-99789EF9AFA0">
        <omgdc:Bounds height="40.0" width="40.0" x="1590.0" y="455.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-A0651375-BDDA-4CAC-91E5-3F5D30E750D2" id="BPMNShape_sid-A0651375-BDDA-4CAC-91E5-3F5D30E750D2">
        <omgdc:Bounds height="40.0" width="40.0" x="2085.0" y="410.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-B39121B6-06A8-4425-8FEF-379D5346F1B8" id="BPMNShape_sid-B39121B6-06A8-4425-8FEF-379D5346F1B8">
        <omgdc:Bounds height="80.0" width="100.0" x="1560.0" y="630.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-8DAA6E25-0018-4A65-9811-70DB2E57385D" id="BPMNShape_sid-8DAA6E25-0018-4A65-9811-70DB2E57385D">
        <omgdc:Bounds height="40.0" width="40.0" x="1740.0" y="650.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-3F313DAD-1453-4CAF-B539-4A3F5F0D830A" id="BPMNShape_sid-3F313DAD-1453-4CAF-B539-4A3F5F0D830A">
        <omgdc:Bounds height="28.0" width="28.0" x="1845.0" y="656.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-AF959651-A814-4461-9B86-62B7D3F0C8AB" id="BPMNShape_sid-AF959651-A814-4461-9B86-62B7D3F0C8AB">
        <omgdc:Bounds height="80.0" width="100.0" x="1275.0" y="334.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-A27A1B36-E43B-4CE0-9ABB-CB893F542EA2" id="BPMNShape_sid-A27A1B36-E43B-4CE0-9ABB-CB893F542EA2">
        <omgdc:Bounds height="40.0" width="40.0" x="1215.0" y="285.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-DBBCC1C1-1EA3-454A-8D48-8ABFC2B21C54" id="BPMNShape_sid-DBBCC1C1-1EA3-454A-8D48-8ABFC2B21C54">
        <omgdc:Bounds height="80.0" width="100.0" x="1740.0" y="165.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge bpmnElement="sid-B0676D03-1C72-442C-89D9-1EDC10D03805" id="BPMNEdge_sid-B0676D03-1C72-442C-89D9-1EDC10D03805">
        <omgdi:waypoint x="1275.0" y="335.731843575419"></omgdi:waypoint>
        <omgdi:waypoint x="1246.262658227848" y="313.7373417721519"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-0DF707B9-C06E-478D-832B-1246036DFBAE" id="BPMNEdge_sid-0DF707B9-C06E-478D-832B-1246036DFBAE">
        <omgdi:waypoint x="1175.5" y="494.5"></omgdi:waypoint>
        <omgdi:waypoint x="1175.5" y="775.0"></omgdi:waypoint>
        <omgdi:waypoint x="1275.0" y="775.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-ED52A86B-A97B-4FC2-8806-5200A2203AA7" id="BPMNEdge_sid-ED52A86B-A97B-4FC2-8806-5200A2203AA7">
        <omgdi:waypoint x="1400.0" y="495.0"></omgdi:waypoint>
        <omgdi:waypoint x="1400.0" y="475.5"></omgdi:waypoint>
        <omgdi:waypoint x="1590.5" y="475.5"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-F366DFEF-B0AC-4BC2-8381-FC771876B143" id="BPMNEdge_sid-F366DFEF-B0AC-4BC2-8381-FC771876B143">
        <omgdi:waypoint x="550.0" y="430.0"></omgdi:waypoint>
        <omgdi:waypoint x="600.0" y="430.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-3E749F13-C2E2-4A5B-AC82-E4EBAAF50F2F" id="BPMNEdge_sid-3E749F13-C2E2-4A5B-AC82-E4EBAAF50F2F">
        <omgdi:waypoint x="1598.3313343328336" y="466.6686656671664"></omgdi:waypoint>
        <omgdi:waypoint x="1225.0" y="195.72574385510995"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-B31EC12C-F411-4361-8E3E-8BB2A86B7992" id="BPMNEdge_sid-B31EC12C-F411-4361-8E3E-8BB2A86B7992">
        <omgdi:waypoint x="1610.451030927835" y="494.54896907216494"></omgdi:waypoint>
        <omgdi:waypoint x="1610.1028277634962" y="630.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-8B4AECB5-FF56-4B4D-B3DF-660DF0B2150A" id="BPMNEdge_sid-8B4AECB5-FF56-4B4D-B3DF-660DF0B2150A">
        <omgdi:waypoint x="1740.0" y="202.5609756097561"></omgdi:waypoint>
        <omgdi:waypoint x="1225.0" y="177.4390243902439"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-9A188072-2E7B-456F-9D6D-81CBCE3F4812" id="BPMNEdge_sid-9A188072-2E7B-456F-9D6D-81CBCE3F4812">
        <omgdi:waypoint x="1610.5" y="455.5"></omgdi:waypoint>
        <omgdi:waypoint x="1610.5" y="81.0"></omgdi:waypoint>
        <omgdi:waypoint x="1025.0" y="81.0"></omgdi:waypoint>
        <omgdi:waypoint x="1025.0" y="135.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-2C504FB2-CA16-4805-B390-325778262047" id="BPMNEdge_sid-2C504FB2-CA16-4805-B390-325778262047">
        <omgdi:waypoint x="500.3787878787879" y="276.8787878787879"></omgdi:waypoint>
        <omgdi:waypoint x="500.00602409638554" y="215.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-EDA974E4-5272-402C-A26D-A0E3B6E56EEA" id="BPMNEdge_sid-EDA974E4-5272-402C-A26D-A0E3B6E56EEA">
        <omgdi:waypoint x="1187.4123505976095" y="467.4123505976096"></omgdi:waypoint>
        <omgdi:waypoint x="1275.0" y="407.9464882943144"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-A5A042BE-30D0-44BC-80F3-9EC65DE3CF0D" id="BPMNEdge_sid-A5A042BE-30D0-44BC-80F3-9EC65DE3CF0D">
        <omgdi:waypoint x="1175.0665557404327" y="215.0"></omgdi:waypoint>
        <omgdi:waypoint x="1175.4666666666667" y="455.46666666666664"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-1123DB20-2510-46A1-B9EB-0807FA223AD3" id="BPMNEdge_sid-1123DB20-2510-46A1-B9EB-0807FA223AD3">
        <omgdi:waypoint x="669.570895522388" y="175.42910447761193"></omgdi:waypoint>
        <omgdi:waypoint x="735.5" y="175.18401486988847"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-C349EFC2-2C9D-4942-B777-F018226AEBC6" id="BPMNEdge_sid-C349EFC2-2C9D-4942-B777-F018226AEBC6">
        <omgdi:waypoint x="626.4705882352941" y="390.0"></omgdi:waypoint>
        <omgdi:waypoint x="523.5294117647059" y="215.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-629EEE51-9575-49AD-B15F-AB55C489FA01" id="BPMNEdge_sid-629EEE51-9575-49AD-B15F-AB55C489FA01">
        <omgdi:waypoint x="1779.5969387755101" y="670.4030612244898"></omgdi:waypoint>
        <omgdi:waypoint x="1845.000180367047" y="670.071065074279"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-0F5F32CD-DC8E-46A9-9364-4BFE68ED462A" id="BPMNEdge_sid-0F5F32CD-DC8E-46A9-9364-4BFE68ED462A">
        <omgdi:waypoint x="2085.5" y="430.5"></omgdi:waypoint>
        <omgdi:waypoint x="1790.0" y="430.5"></omgdi:waypoint>
        <omgdi:waypoint x="1790.0" y="245.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-C4AAD876-2BB1-48A3-BD5E-AE8932429C58" id="BPMNEdge_sid-C4AAD876-2BB1-48A3-BD5E-AE8932429C58">
        <omgdi:waypoint x="550.0" y="175.16611295681062"></omgdi:waypoint>
        <omgdi:waypoint x="630.4333333333333" y="175.43333333333334"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-BA513A2C-29BF-4C40-9F44-1476A4CFC3B6" id="BPMNEdge_sid-BA513A2C-29BF-4C40-9F44-1476A4CFC3B6">
        <omgdi:waypoint x="905.73046875" y="155.73046875"></omgdi:waypoint>
        <omgdi:waypoint x="905.73046875" y="60.0"></omgdi:waypoint>
        <omgdi:waypoint x="500.0" y="60.0"></omgdi:waypoint>
        <omgdi:waypoint x="500.0" y="135.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-860EAA3D-7881-4EB0-BE0D-DD82F3153F4D" id="BPMNEdge_sid-860EAA3D-7881-4EB0-BE0D-DD82F3153F4D">
        <omgdi:waypoint x="1660.0" y="670.1661129568106"></omgdi:waypoint>
        <omgdi:waypoint x="1740.4333333333334" y="670.4333333333333"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-E7CB2774-2945-4E26-A4C3-AC4AB4333B3D" id="BPMNEdge_sid-E7CB2774-2945-4E26-A4C3-AC4AB4333B3D">
        <omgdi:waypoint x="924.6357142857142" y="175.36428571428573"></omgdi:waypoint>
        <omgdi:waypoint x="975.0" y="175.00709219858157"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-0EECFDC4-8EFA-4D24-8CB6-6317A48BE97F" id="BPMNEdge_sid-0EECFDC4-8EFA-4D24-8CB6-6317A48BE97F">
        <omgdi:waypoint x="834.5" y="175.0"></omgdi:waypoint>
        <omgdi:waypoint x="885.0" y="175.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-C4F7087C-533E-4A22-9896-CBB9A903116B" id="BPMNEdge_sid-C4F7087C-533E-4A22-9896-CBB9A903116B">
        <omgdi:waypoint x="1760.5" y="689.5"></omgdi:waypoint>
        <omgdi:waypoint x="1760.5" y="740.0"></omgdi:waypoint>
        <omgdi:waypoint x="1400.0" y="740.0"></omgdi:waypoint>
        <omgdi:waypoint x="1400.0" y="575.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-AFF23328-C45E-4BFC-B7D4-578918C6141F" id="BPMNEdge_sid-AFF23328-C45E-4BFC-B7D4-578918C6141F">
        <omgdi:waypoint x="2105.5" y="410.5"></omgdi:waypoint>
        <omgdi:waypoint x="2105.5" y="136.0"></omgdi:waypoint>
        <omgdi:waypoint x="1225.0" y="136.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-43F41ABF-2868-430D-926C-514AECE90939" id="BPMNEdge_sid-43F41ABF-2868-430D-926C-514AECE90939">
        <omgdi:waypoint x="796.3138707018793" y="562.9377907500988"></omgdi:waypoint>
        <omgdi:waypoint x="731.571722541464" y="562.5653130925615"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-A39C8B44-8E56-4854-B12B-C0EC0D4BD835" id="BPMNEdge_sid-A39C8B44-8E56-4854-B12B-C0EC0D4BD835">
        <omgdi:waypoint x="1189.6893617021276" y="480.31063829787234"></omgdi:waypoint>
        <omgdi:waypoint x="1350.0" y="534.6609686609687"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-2B6AB4B3-F648-4846-BE15-68F247750A2A" id="BPMNEdge_sid-2B6AB4B3-F648-4846-BE15-68F247750A2A">
        <omgdi:waypoint x="1238.1845637583892" y="288.1845637583893"></omgdi:waypoint>
        <omgdi:waypoint x="1245.5" y="241.0"></omgdi:waypoint>
        <omgdi:waypoint x="1217.7272727272727" y="215.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-18A79244-9EB1-46F6-8C6A-36A0AA44B399" id="BPMNEdge_sid-18A79244-9EB1-46F6-8C6A-36A0AA44B399">
        <omgdi:waypoint x="692.208470654098" y="562.9732476918822"></omgdi:waypoint>
        <omgdi:waypoint x="516.6861292981209" y="563.1695373162115"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-C80EEBC4-1FC2-4713-AC0F-BC04A622953C" id="BPMNEdge_sid-C80EEBC4-1FC2-4713-AC0F-BC04A622953C">
        <omgdi:waypoint x="650.3837209302326" y="155.38372093023256"></omgdi:waypoint>
        <omgdi:waypoint x="650.080923503578" y="102.9997661189953"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-D7D76FC6-03A7-42AA-9F62-C1065E857867" id="BPMNEdge_sid-D7D76FC6-03A7-42AA-9F62-C1065E857867">
        <omgdi:waypoint x="1222.5071942446043" y="297.4928057553957"></omgdi:waypoint>
        <omgdi:waypoint x="1192.5" y="279.0"></omgdi:waypoint>
        <omgdi:waypoint x="1181.7307692307693" y="215.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-B5FE7E77-2F78-409C-B8D7-94F104846177" id="BPMNEdge_sid-B5FE7E77-2F78-409C-B8D7-94F104846177">
        <omgdi:waypoint x="1015.5833406478969" y="735.0"></omgdi:waypoint>
        <omgdi:waypoint x="885.7305300539824" y="603.2254531679524"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-739E3487-3317-49A2-B15F-4436DD6C887E" id="BPMNEdge_sid-739E3487-3317-49A2-B15F-4436DD6C887E">
        <omgdi:waypoint x="1075.0" y="175.0"></omgdi:waypoint>
        <omgdi:waypoint x="1125.0" y="175.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-16B3D265-A2F4-45CB-A747-C9CF24C07778" id="BPMNEdge_sid-16B3D265-A2F4-45CB-A747-C9CF24C07778">
        <omgdi:waypoint x="500.3983957219251" y="316.1016042780749"></omgdi:waypoint>
        <omgdi:waypoint x="500.00531914893617" y="390.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-6DF9E1D0-204D-468E-8392-BFFB7F051733" id="BPMNEdge_sid-6DF9E1D0-204D-468E-8392-BFFB7F051733">
        <omgdi:waypoint x="416.6861292981207" y="563.2788780892681"></omgdi:waypoint>
        <omgdi:waypoint x="269.6861213063053" y="563.4359473664757"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-F4B42A5F-08D7-475F-B127-EFCD78ED8DD7" id="BPMNEdge_sid-F4B42A5F-08D7-475F-B127-EFCD78ED8DD7">
        <omgdi:waypoint x="721.1139145108473" y="551.8786915486314"></omgdi:waypoint>
        <omgdi:waypoint x="992.7470663846284" y="215.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-F1E3D770-9626-43FB-92D3-18E60DBC76F5" id="BPMNEdge_sid-F1E3D770-9626-43FB-92D3-18E60DBC76F5">
        <omgdi:waypoint x="712.1861292981207" y="581.9509063359048"></omgdi:waypoint>
        <omgdi:waypoint x="712.1861292981207" y="775.0"></omgdi:waypoint>
        <omgdi:waypoint x="1005.0" y="775.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-737E3818-1D7F-49A0-BAAC-891CB65D4934" id="BPMNEdge_sid-737E3818-1D7F-49A0-BAAC-891CB65D4934">
        <omgdi:waypoint x="1375.0" y="775.0"></omgdi:waypoint>
        <omgdi:waypoint x="2105.5" y="775.0"></omgdi:waypoint>
        <omgdi:waypoint x="2105.5" y="449.5"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-907C55F1-6C0E-4A84-9D99-2DD1AD8E4B2E" id="BPMNEdge_sid-907C55F1-6C0E-4A84-9D99-2DD1AD8E4B2E">
        <omgdi:waypoint x="1055.0" y="515.0"></omgdi:waypoint>
        <omgdi:waypoint x="1055.0" y="735.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-F20A68E8-1642-4AE8-9CE1-4212B02962D3" id="BPMNEdge_sid-F20A68E8-1642-4AE8-9CE1-4212B02962D3">
        <omgdi:waypoint x="650.4625984251968" y="194.53740157480314"></omgdi:waypoint>
        <omgdi:waypoint x="650.0785854616896" y="390.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-1A3CC65B-BA50-4D9C-8CAF-E38AD4E8D833" id="BPMNEdge_sid-1A3CC65B-BA50-4D9C-8CAF-E38AD4E8D833">
        <omgdi:waypoint x="1155.4166666666667" y="475.4166666666667"></omgdi:waypoint>
        <omgdi:waypoint x="1105.0" y="475.20746887966806"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-959DFF43-FC0D-4EAA-BF50-CAF9C36DD3F4" id="BPMNEdge_sid-959DFF43-FC0D-4EAA-BF50-CAF9C36DD3F4">
        <omgdi:waypoint x="270.68609801371764" y="296.5306354551574"></omgdi:waypoint>
        <omgdi:waypoint x="480.4590690451947" y="296.9590690451947"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>`
    bpmnJS.importXML(xml, function (err) {
        if (!err) {
            console.log('success!');
            // 让图能自适应屏幕
            const canvas = bpmnJS.get('canvas')
            canvas.zoom('fit-viewport')
        } else {
            console.log('something went wrong:', err);
        }
    });
})