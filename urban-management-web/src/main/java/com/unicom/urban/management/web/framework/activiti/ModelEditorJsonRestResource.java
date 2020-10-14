package com.unicom.urban.management.web.framework.activiti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Tijs Rademakers
 */
@Slf4j
@RestController
@RequestMapping(value = "/service")
public class ModelEditorJsonRestResource implements ModelDataJsonConstants {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/model/{modelId}/json")
    public ObjectNode getEditorJson(@PathVariable String modelId) throws IOException {
        ObjectNode modelNode = null;

        Model model = repositoryService.getModel(modelId);

        if (model != null) {
            if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            } else {
                modelNode = objectMapper.createObjectNode();
                modelNode.put(MODEL_NAME, model.getName());
            }
            ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(repositoryService.getModelEditorSource(model.getId()), StandardCharsets.UTF_8));

            modelNode.put(MODEL_ID, model.getId());
            modelNode.set("model", editorJsonNode);
        }
        return modelNode;
    }
}
