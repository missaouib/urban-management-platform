package com.unicom.urban.management.web.project.dept;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.DeptDTO;
import com.unicom.urban.management.pojo.dto.UserIdListDTO;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.service.dept.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * 部门管理
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping
    public ModelAndView dept() {
        return new ModelAndView(SystemConstant.PAGE + "/dept/dept");
    }


    /**
     * 查询部门树
     */
    @GetMapping("/selectdepttree")
    public ModelAndView selectDeptTree() {
        return new ModelAndView(SystemConstant.PAGE + "/dept/selectdepttree");
    }

    /**
     * 新增
     */
    @PostMapping("/dept")
    public void dept(DeptDTO deptDTO){
        deptService.save(deptDTO);
    }

    /**
     * 删除
     */
    @PostMapping("/del")
    public void del(DeptDTO deptDTO){
        deptService.del(deptDTO.getId());
    }

    /**
     * 获取单个
     */
    @GetMapping("/deptOne")
    public DeptVO deptOne(String id){
        return deptService.findOneVO(id);
    }
    /**
     * 获取机构树
     */
    @GetMapping("/deptTree")
    public List<DeptVO> deptTree(){
        return deptService.getAll();
    }
}
