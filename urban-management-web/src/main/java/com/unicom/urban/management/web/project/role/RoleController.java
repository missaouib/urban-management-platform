package com.unicom.urban.management.web.project.role;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.RoleDTO;
import com.unicom.urban.management.pojo.vo.RoleVO;
import com.unicom.urban.management.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@ResponseResultBody
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ModelAndView role() {
        return new ModelAndView(SystemConstant.PAGE + "/role/role");
    }

    @GetMapping("/add")
    public ModelAndView add() {
        return new ModelAndView(SystemConstant.PAGE + "/role/add");
    }


    @GetMapping("/search")
    public Page<RoleVO> search(RoleDTO roleDTO, @PageableDefault Pageable pageable) {
        return roleService.search(roleDTO, pageable);
    }


    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable String id, Model model) {
        RoleVO role = roleService.findById(id);

        model.addAttribute("role", role);

        return new ModelAndView(SystemConstant.PAGE + "/role/edit");
    }

//    @PostMapping("/update")
//    public void updateUser(User user) {
//        userService.updateUser(user);
//    }

    @PostMapping("/save")
    public void save(@Valid RoleDTO roleDTO) {
        roleService.saveRole(roleDTO);
    }

//    @PostMapping("/remove")
//    public void deleteRole(String ids) {
//        roleService.(ids);
//    }


}
