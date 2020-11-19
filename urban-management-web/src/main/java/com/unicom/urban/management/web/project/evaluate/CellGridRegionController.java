package com.unicom.urban.management.web.project.evaluate;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.vo.CellGridRegionVO;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
     * 区域评价 1类
     *
     * @return 数据
     */
    @GetMapping("/cellGridRegionOne")
    public Page<CellGridRegionVO> cellGridRegionOne(String startTime, String endTime, @PageableDefault Pageable pageable) {
        return statisticsService.findAllForCellGridRegion(startTime, endTime, pageable);
    }

    /**
     * 区域评价 2类
     *
     * @return 数据
     */
    @GetMapping("/cellGridRegionTwo")
    public Page<CellGridRegionVO> cellGridRegionTwo(String startTime, String endTime, @PageableDefault Pageable pageable) {
        return statisticsService.findAllForCellGridRegion(startTime, endTime, pageable);
    }

}
