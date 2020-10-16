package com.unicom.urban.management.web.project.component;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.ComponentDTO;
import com.unicom.urban.management.pojo.entity.KV;
import com.unicom.urban.management.pojo.vo.ComponentTypeVO;
import com.unicom.urban.management.pojo.vo.ComponentVO;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.pojo.vo.PublishVO;
import com.unicom.urban.management.service.component.ComponentService;
import com.unicom.urban.management.service.componenttype.ComponentTypeService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 部件管理
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class ComponentController {

    private final ComponentTypeService componentTypeService;

    private final ComponentService componentService;

    private final KVService kvService;

    private final GridService gridService;

    private final PublishService publishService;

    @Autowired
    public ComponentController(ComponentTypeService componentTypeService, ComponentService componentService, KVService kvService, GridService gridService, PublishService publishService) {
        this.componentTypeService = componentTypeService;
        this.componentService = componentService;
        this.kvService = kvService;
        this.gridService = gridService;
        this.publishService = publishService;
    }


    @GetMapping("/component")
    public ModelAndView component() {
        return new ModelAndView(SystemConstant.PAGE + "/component/component");
    }

    @GetMapping("/component/import")
    public ModelAndView componentImport() {
        return new ModelAndView(SystemConstant.PAGE + "/component/import");
    }

    @GetMapping("/componentType")
    public List<ComponentTypeVO> componentTypeList(){
        return componentTypeService.getComponentTypeList();
    }

    @PostMapping("/componentList")
    public void saveComponentList(List<ComponentDTO> dtos){
        componentService.saveComponent(dtos);
    }
    @PostMapping("/component")
    public void saveComponent(ComponentDTO dto){
        componentService.saveComponent(dto);
    }



    @GetMapping("/componentList")
    public List<ComponentVO> componentList(ComponentDTO dto){
        return componentService.getComponentList(dto);
    }

    @GetMapping("/objState")
    public List<KV> objState(){
        return kvService.getKv("componentInfo","objState");
    }

    @GetMapping("/dataSource")
    public List<KV> dataSource(){
        return kvService.getKv("componentInfo","dataSource");
    }

    @GetMapping("/grid")
    public List<GridVO> grd(){
        return gridService.searchAll();
    }

    @GetMapping("/componentTypePublish")
    public List<PublishVO> componentList(String typeId){
       return publishService.searchTypeId(typeId);
    }


}
