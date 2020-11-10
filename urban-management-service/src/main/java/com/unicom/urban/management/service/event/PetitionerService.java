package com.unicom.urban.management.service.event;

import com.unicom.urban.management.dao.event.PetitionerRepository;
import com.unicom.urban.management.pojo.entity.Petitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 诉求人
 *
 * @author liubohzi
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class PetitionerService {
    @Autowired
    PetitionerRepository petitionerRepository;
    public Petitioner save(Petitioner petitioner){
        return petitionerRepository.save(petitioner);
    }
    public void remove(String id){
        petitionerRepository.deleteById(id);
    }

}
