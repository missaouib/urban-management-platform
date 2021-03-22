package com.unicom.urban.management.service.trajectory;

import com.unicom.urban.management.dao.trajectory.TrajectoryRepository;
import com.unicom.urban.management.pojo.dto.TrajectoryDTO;
import com.unicom.urban.management.pojo.entity.Trajectory;
import com.unicom.urban.management.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 轨迹记录service
 *
 * @author liubozhi
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class TrajectoryService {

    @Autowired
    private TrajectoryRepository trajectoryRepository;

    public void saveTrajectory(TrajectoryDTO trajectoryDTO, User user) {
        Trajectory trajectory = new Trajectory();
        trajectory.setUser(user);
        trajectory.setX(trajectoryDTO.getX());
        trajectory.setY(trajectoryDTO.getY());
        trajectoryRepository.saveAndFlush(trajectory);
    }
}
