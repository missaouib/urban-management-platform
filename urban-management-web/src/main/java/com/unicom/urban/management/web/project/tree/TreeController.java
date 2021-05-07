package com.unicom.urban.management.web.project.tree;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.pojo.vo.TreeVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import com.unicom.urban.management.service.grid.GridService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liukai
 */
@RestController
@ResponseResultBody
@RequestMapping("/tree")
public class TreeController {

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private GridService gridService;


    @GetMapping("/eventtype")
    public ModelAndView eventTypeTree(Model model) {
        List<TreeVO> tree = eventTypeService.searchTree();
        model.addAttribute("tree", tree);
        return new ModelAndView(SystemConstant.PAGE + "/tree/eventtype");
    }

    @GetMapping("/dept")
    public ModelAndView deptTree(Model model) {
        List<TreeVO> tree = deptService.searchTree();
        model.addAttribute("tree", tree);
        return new ModelAndView(SystemConstant.PAGE + "/tree/dept");
    }

    @GetMapping("/grid")
    public ModelAndView gridTree(Model model,String id,Integer level) {
        if(null==level){
            level = 4;
        }
        if(StringUtils.isNotBlank(id)){
            level = gridService.getLevel(id);
        }
        List<TreeVO> tree = gridService.searchTree(level);
        model.addAttribute("tree", tree);
        return new ModelAndView(SystemConstant.PAGE + "/tree/grid");
    }

    @GetMapping("/deptRole")
    public ModelAndView deptRole(Model model) {
        List<DeptVO> allAndRoleForTree = deptService.getAllAndRoleForTree();
        List<TreeVO> tree = new ArrayList<>();
        allAndRoleForTree.forEach(d->{
            TreeVO treeVO = new TreeVO();
            treeVO.setId(d.getId());
            treeVO.setName(d.getDeptName());
            treeVO.setParentId(d.getParentId());
            treeVO.setLevelOrNot(d.getLevelOrNot());
            tree.add(treeVO);
        });

        model.addAttribute("tree", tree);
        return new ModelAndView(SystemConstant.PAGE + "/tree/deptRole");
    }


}
