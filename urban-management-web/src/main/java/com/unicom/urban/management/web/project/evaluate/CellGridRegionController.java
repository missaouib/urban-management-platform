package com.unicom.urban.management.web.project.evaluate;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.vo.CellGridRegionVO;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 区域评价
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/evaluate")
public class CellGridRegionController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 区域评价 1类
     *
     * @return 页面
     */
    @GetMapping("/toCellGridRegionOne")
    public ModelAndView toCellGridRegionOne() {
        return new ModelAndView(SystemConstant.PAGE + "/evaluate/cellGridRegionOne");
    }

    /**
     * 区域评价 2类
     *
     * @return 页面
     */
    @GetMapping("/toCellGridRegionTwo")
    public ModelAndView toCellGridRegionTwo() {
        return new ModelAndView(SystemConstant.PAGE + "/evaluate/cellGridRegionTwo");
    }

    /**
     * 区域评价
     *
     * @return 数据
     */
    @GetMapping("/cellGridRegion")
    public List<CellGridRegionVO> cellGridRegion() {
        return statisticsService.findAllForCellGridRegion();
    }

}
