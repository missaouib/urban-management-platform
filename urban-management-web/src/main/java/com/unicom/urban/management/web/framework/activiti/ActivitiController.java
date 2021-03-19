package com.unicom.urban.management.web.framework.activiti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.dto.ModelDTO;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@ResponseResultBody
@RequestMapping("/activiti")
public class ActivitiController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 创建模型
     */
    @PostMapping("/create")
    public Map<String, Object> create(@Valid ModelDTO modelDTO) {
        String name = modelDTO.getName();

        String description = modelDTO.getDescription();

        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");

        ObjectNode editorNode = objectMapper.createObjectNode();

        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        editorNode.set("stencilset", stencilSetNode);


        ObjectNode metaInfo = objectMapper.createObjectNode();
        metaInfo.put(ModelDataJsonConstants.MODEL_NAME, name);
        metaInfo.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        metaInfo.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);

        Model modelData = repositoryService.newModel();
        modelData.setName(name);
        modelData.setMetaInfo(metaInfo.toString());

        // 保存模型
        repositoryService.saveModel(modelData);

        repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
        Map<String, Object> map = new HashMap<>(1);
        map.put("modelId", modelData.getId());
        return map;

    }

}