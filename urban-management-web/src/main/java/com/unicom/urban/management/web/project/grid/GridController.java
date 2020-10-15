package com.unicom.urban.management.web.project.grid;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.GridDTO;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.service.grid.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @PostMapping("/getGridList")
    public List<GridVO> getGridList() {
        return gridService.search();
    }

    @PostMapping("/gridSave")
    public Result gridSave(GridDTO gridDTO) {
        gridService.save(gridDTO);
        return Result.success("新增成功");
    }

}
