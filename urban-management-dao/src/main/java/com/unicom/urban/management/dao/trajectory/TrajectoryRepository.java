package com.unicom.urban.management.dao.trajectory;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Trajectory;

import java.util.List;

/**
 * @author 刘博智
 */
public interface TrajectoryRepository extends CustomizeRepository<Trajectory, String> {

    /**
     * 查询个人人员轨迹
     *
     * @param id 人
     * @return 人员轨迹
     */
    List<Trajectory> findAllByUser_IdOrderByCreateTimeAsc(String id);

}
