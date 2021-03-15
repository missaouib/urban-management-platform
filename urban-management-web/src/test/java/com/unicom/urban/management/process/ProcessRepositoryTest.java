package com.unicom.urban.management.process;

import com.unicom.urban.management.WebApplication;
import com.unicom.urban.management.annotation.WithMockCustomUser;
import com.unicom.urban.management.dao.process.ProcessRepository;
import com.unicom.urban.management.pojo.entity.Process;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = WebApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
@WithMockCustomUser(id = "1", username = "admin", name = "超级管理员")
public class ProcessRepositoryTest {

    @Autowired
    private ProcessRepository processRepository;

    @Test
    @Transactional
    public void search() {
        List<Process> all = processRepository.findAll();
//        all.get(0).getNode().getId();
    }


}
