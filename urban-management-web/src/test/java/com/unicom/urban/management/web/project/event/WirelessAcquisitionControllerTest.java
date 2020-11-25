package com.unicom.urban.management.web.project.event;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
@WithMockUser(username = "admin", password = "123433356")
@WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl")
public class WirelessAcquisitionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void toWirelessAcquisitionSaveTest() throws Exception {
        String eventTypeId = "0366469a-25ee-431b-8db1-91db0e71365a";

        // get请求接口-无参数
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/event/toWirelessAcquisitionSave/" + eventTypeId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        log.info("返回结果:{}, contentLength:{} ", new String(mvcResult.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8),
                mvcResult.getResponse().getContentLength());
    }
    @Test
    @Transactional
    public void findEventConditionByEventTypeTest() throws Exception {
        String eventTypeId = "0366469a-25ee-431b-8db1-91db0e71365a";

        // get请求接口-无参数
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/event/findEventConditionByEventType/" + eventTypeId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        log.info("返回结果:{}, contentLength:{} ", new String(mvcResult.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8),
                mvcResult.getResponse().getContentLength());
    }
    @Test
    @Transactional
    public void getDeptTimeLimitByLevelTest() throws Exception {
        String eventTypeId = "0366469a-25ee-431b-8db1-91db0e71365a";
        String levelId = "fd533d5b-b5d1-4fbc-b4cd-4ced27b7b0c5";
        // get请求接口-无参数
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/event/getDeptTimeLimitByLevel/" + eventTypeId+"/"+ levelId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        log.info("返回结果:{}, contentLength:{} ", new String(mvcResult.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8),
                mvcResult.getResponse().getContentLength());
    }

    @Test
    @Transactional
    public void findAllByKvIdTest() throws Exception {
        String kvId = "454310fe-7b45-49b0-9910-36e777e6fdcc";
        // get请求接口-无参数
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/event/findAllByKvId/" + kvId))
                .andReturn();
        log.info("返回结果:{}, contentLength:{} ", new String(mvcResult.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8),
                mvcResult.getResponse().getContentLength());
    }
    @Test
    @Transactional
    public void findHotGridTest() throws Exception {
        // get请求接口-无参数
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/statistics/findHotGrid/"))
                .andReturn();
        log.info("返回结果:{}, contentLength:{} ", new String(mvcResult.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8),
                mvcResult.getResponse().getContentLength());
    }
    @Test
    @Transactional
    public void findEventSourceTest() throws Exception {
        // get请求接口-无参数
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/statistics/findEventSource/"))
                .andReturn();
        log.info("返回结果:{}, contentLength:{} ", new String(mvcResult.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8),
                mvcResult.getResponse().getContentLength());
    }
}
