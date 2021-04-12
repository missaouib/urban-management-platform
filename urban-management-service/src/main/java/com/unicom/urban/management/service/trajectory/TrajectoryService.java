package com.unicom.urban.management.service.trajectory;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.dao.trajectory.TrajectoryRepository;
import com.unicom.urban.management.pojo.dto.TrajectoryDTO;
import com.unicom.urban.management.pojo.entity.Trajectory;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.TrajectoryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public List<TrajectoryVO> getTrajectoryForOne(String userId, String startTime, String endTime) {
//        List<Trajectory> trajectoryList = trajectoryRepository.findAllByUser_IdOrderByCreateTimeAsc(userId);
        List<Trajectory> trajectoryList = trajectoryRepository.findAll((Specification<Trajectory>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(startTime)) {
                LocalDateTime startLocalDateTime = LocalDateTime.parse(startTime, df);
                list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(LocalDateTime.class), startLocalDateTime));
            }
            if (StringUtils.isNotBlank(endTime)) {
                LocalDateTime endLocalDateTime = LocalDateTime.parse(endTime, df);
                list.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(LocalDateTime.class), endLocalDateTime));
            }
            list.add(criteriaBuilder.equal(root.get("user").get("id").as(String.class), userId));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, Sort.by(Sort.Order.asc("createTime")));
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
