package com.unicom.urban.management.web.project.activiti;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "123433356")
public class ProcessDefinitionControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void processDefinitionList() throws Exception {
        mockMvc
                .perform(get("/processdef/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0"));
    }

}