package com.unicom.urban.management.web.project.grid;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.AreaDTO;
import com.unicom.urban.management.pojo.dto.GridDTO;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.service.grid.GridService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 网格管理
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
@RequestMapping("/grid")
public class GridController {

    @Autowired
    private GridService gridService;

    @GetMapping("/grid")
    public ModelAndView grid() {
        return new ModelAndView(SystemConstant.PAGE + "/grid/grid");
    }

    @GetMapping("/toGridSave")
    public ModelAndView toGridSave() {
        return new ModelAndView(SystemConstant.PAGE + "/grid/gridSave");
    }

    @GetMapping("/toArea")
    public ModelAndView toArea() {
        return new ModelAndView(SystemConstant.PAGE + "/area/area");
    }

    @GetMapping("/toCollocation")
    public ModelAndView toCollocation(Model model, String id) {
        model.addAttribute("id", id);
        return new ModelAndView(SystemConstant.PAGE + "/area/collocation");
    }

    @GetMapping("/getGridList")
    public List<GridVO> getGridList() {
        return gridService.search();
    }

    @PostMapping("/gridSave")
    public void gridSave(@Valid GridDTO gridDTO) {
        gridService.save(gridDTO);
    }

    @PostMapping("/gridUpdate")
    public void gridUpdate(@Valid GridDTO gridDTO) {
        gridService.update(gridDTO);
    }

    @GetMapping("/getGridOne/{gridId}")
    public GridVO getGridOne(@PathVariable String gridId) {
        return gridService.search(gridId);
    }

    @PostMapping("/gridDelete")
    public void gridDelete(String gridId) {
        gridService.delete(gridId);
    }

    @GetMapping("/getGridCenter")
    public Result getGridCenter(String gridId) {
        return Result.success(gridService.getGridCenter(gridId));
    }

    @GetMapping("/getGridTree")
    public Result getGridTree(String gridId) {
        return Result.success(gridService.searchTree(Arrays.asList(1, 2, 3)));
    }


    @PostMapping("/area")
    public void area(@Valid AreaDTO areaDTO) {
        if (StringUtils.isNotBlank(areaDTO.getGridId())) {
            gridService.updateArea(areaDTO);
        } else {
            gridService.saveArea(areaDTO);
        }
    }

}
