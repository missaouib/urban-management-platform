package com.unicom.urban.management.service;

import com.unicom.urban.management.annotation.WithMockCustomUser;
import com.unicom.urban.management.pojo.dto.MenuDTO;
import com.unicom.urban.management.service.menu.MenuService;
import com.unicom.urban.management.web.WebApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = WebApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
@WithMockCustomUser(id = "1", username = "admin")
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;


    @Test
    public void save() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName("无线采集子系统");
        menuDTO.setPath("");
        menuService.save(menuDTO);
    }

}
