package com.unicom.urban.management.service.idioms;

import com.unicom.urban.management.dao.idioms.IdiomsRepository;
import com.unicom.urban.management.pojo.entity.Idioms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class IdiomsService {
    @Autowired
    private IdiomsRepository idiomsRepository;
    public void save(Idioms idioms){
        idiomsRepository.save(idioms);
    }
}
