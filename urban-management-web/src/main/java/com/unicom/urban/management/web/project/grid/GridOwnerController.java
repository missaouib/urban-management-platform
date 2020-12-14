package com.unicom.urban.management.web.project.grid;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.GridDTO;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 移动终端配置
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/gridOwner")
public class GridOwnerController {

    @Autowired
    private GridService gridService;
    @Autowired
    private UserService userService;

    @GetMapping("/toList")
    public ModelAndView toList() {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/gridOwner/list");
        //所在区域
        modelAndView.addObject("gridList", gridService.findAllByParentIsNull());
        return modelAndView;
    }

    @GetMapping("/toInsert")
    public ModelAndView toInsert(String id) {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/gridOwner/add");
        modelAndView.addObject("gridId", id);
        return modelAndView;
    }

    @GetMapping("/getGridList")
    public Page<GridVO> getGridList(GridDTO gridDTO, @PageableDefault Pageable pageable) {
        return gridService.search(gridDTO, pageable);
    }

    @GetMapping("/getSupervisorUserList")
    public List<UserVO> getSupervisorUserList(String gridId) {
        return userService.getSupervisorUserList(gridId);
    }

    @GetMapping("/getGridData")
    public GridVO getGridData(String gridId) {
        return gridService.search(gridId);
    }

    @PostMapping("/save")
    public Result save(String gridId, @RequestParam(value = "userList") List<String> userList) {
        gridService.save(gridId, userList);
        return Result.success("配置成功");
    }

}
