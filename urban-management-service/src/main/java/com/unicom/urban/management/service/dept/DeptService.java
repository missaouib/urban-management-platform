package com.unicom.urban.management.service.dept;

import com.unicom.urban.management.dao.dept.DeptRepository;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.vo.DeptVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门管理service
 *
 * @author liukai
 */
@Service
public class DeptService {

    @Autowired
    private DeptRepository deptRepository;

    public List<DeptVO> getAll(){
        List<Dept> all = deptRepository.findAll();
        List<DeptVO> deptVOS = new ArrayList<>();
        all.forEach(d->{
            DeptVO deptVO = DeptVO.builder()
                    .id(d.getId())
                    .deptName(d.getDeptName()).build();
            deptVOS.add(deptVO);
        });
        return deptVOS;
    }












}
