package com.unicom.urban.management.service.trajectory;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.dao.trajectory.TrajectoryRepository;
import com.unicom.urban.management.pojo.dto.TrajectoryDTO;
import com.unicom.urban.management.pojo.entity.Trajectory;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.TrajectoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Log(name = "人员轨迹-新增")
    public void saveTrajectory(TrajectoryDTO trajectoryDTO, User user) {
        Trajectory trajectory = new Trajectory();
        trajectory.setUser(user);
        trajectory.setX(trajectoryDTO.getX());
        trajectory.setY(trajectoryDTO.getY());
        trajectory.setCreateTime(LocalDateTime.now());
        trajectoryRepository.saveAndFlush(trajectory);
    }

    public List<TrajectoryVO> getTrajectoryForOne(String userId){
        List<Trajectory> trajectoryList = trajectoryRepository.findAllByUser_IdOrderByCreateTimeAsc(userId);
        List<TrajectoryVO> trajectoryVOList = new ArrayList<>(trajectoryList.size());
        for (Trajectory trajectory : trajectoryList) {
            TrajectoryVO trajectoryVO = new TrajectoryVO();
            BeanUtils.copyProperties(trajectory, trajectoryVO);
            trajectoryVO.setUserId(trajectory.getUser().getId());
            trajectoryVO.setUserName(trajectory.getUser().getName());
            trajectoryVOList.add(trajectoryVO);
        }
        return trajectoryVOList;
    }
}
