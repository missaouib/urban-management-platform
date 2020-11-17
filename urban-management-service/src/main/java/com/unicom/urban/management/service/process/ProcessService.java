package com.unicom.urban.management.service.process;

import com.unicom.urban.management.dao.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;





}
