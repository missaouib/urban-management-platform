package com.unicom.urban.management.web.project.dictdata;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.entity.DictData;
import com.unicom.urban.management.service.dictdata.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * 数据字典
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class DictDataController {

    @Autowired
    private DictDataService dictDataService;

    @GetMapping("/dictdata")
    public ModelAndView dictData() {
        return new ModelAndView(SystemConstant.PAGE + "/dictdata/dictdata");
    }

    @GetMapping("/dictdata/search")
    public Page<DictData> search(@PageableDefault Pageable pageable) {
        return dictDataService.search(pageable);
    }


    @PostMapping("/dictdata/save")
    public void save(@Valid DictData dictData) {

    }


}
