package com.unicom.urban.management.web.project.layertype;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 要素库管理 功能相当于网格化管理平台中的  图层分类及属性管理模块   所以暂时叫LayerType  取名并不规范 所以可能会修改
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class LayerTypeController {


    @GetMapping("/layertype")
    public ModelAndView hotline() {
        return new ModelAndView(SystemConstant.PAGE + "/layertype/layertype");
    }

}
