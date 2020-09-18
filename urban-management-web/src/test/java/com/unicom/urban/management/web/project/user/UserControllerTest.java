package com.unicom.urban.management.web.project.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

}