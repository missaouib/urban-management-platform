package com.unicom.urban.management.dao.event;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.Petitioner;
import org.springframework.data.jpa.repository.Query;

/**
 * 诉求人
 *
 * @author liubozhi
 */
public interface PetitionerRepository extends CustomizeRepository<Petitioner, String> {
}
