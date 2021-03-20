package com.unicom.urban.management.web.project.user;

import com.unicom.urban.management.dao.time.TimePlanRepository;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "123433356")
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimePlanRepository timePlanRepository;

    @Test
    @Transactional
    public void saveUser() throws Exception {
        String username = UUID.randomUUID().toString();
        mockMvc.perform(post("/user/save")
                .param("name", "liukai")
                .param("username", username)
                .param("mobileNumber", "13384614120")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0"));
    }

    @Test
    public void saveTimePlan() {
        TimePlan timePlan = new TimePlan();
        timePlan.setStartTime(LocalDateTime.now());
        timePlan.setEndTime(LocalDateTime.now());
        timePlan.setSts(TimePlan.Status.ENABLE);

        timePlanRepository.save(timePlan);

    }

    @Test
    @Transactional
    public void queryTimePlan() {
        List<TimePlan> all = timePlanRepository.findAll();

        for (TimePlan timePlan : all) {
            System.out.println(timePlan);
        }

    }

}