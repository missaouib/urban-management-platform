package com.unicom.urban.management.web.framework.activiti;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Tijs Rademakers
 */
@Slf4j
@RestController
@RequestMapping(value = "/service")
public class ModelSaveRestResource implements ModelDataJsonConstants {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @PutMapping("/model/{modelId}/save")
    public void saveModel(@PathVariable String modelId, String name, String description, String json_xml, String svg_xml) throws TranscoderException, IOException {

        Model model = repositoryService.getModel(modelId);

        ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

        modelJson.put(MODEL_NAME, name);
        modelJson.put(MODEL_DESCRIPTION, description);
        model.setMetaInfo(modelJson.toString());
        model.setName(name);

        repositoryService.saveModel(model);

        repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes(StandardCharsets.UTF_8));

        InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes(StandardCharsets.UTF_8));
        TranscoderInput input = new TranscoderInput(svgStream);

        PNGTranscoder transcoder = new PNGTranscoder();
        // Setup output
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(outStream);

        // Do the transformation
        transcoder.transcode(input, output);
        final byte[] result = outStream.toByteArray();
        repositoryService.addModelEditorSourceExtra(model.getId(), result);
        outStream.close();

        byte[] bytes = repositoryService.getModelEditorSource(modelId);
        JsonNode modelNode = new ObjectMapper().readTree(bytes);
        BpmnModel model1 = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        Model modelData = repositoryService.getModel(modelId);
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model1);
        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .addString(processName, new String(bpmnBytes, StandardCharsets.UTF_8))
                .deploy();
        modelData.setDeploymentId(deployment.getId());
        repositoryService.saveModel(modelData);

    }
}
