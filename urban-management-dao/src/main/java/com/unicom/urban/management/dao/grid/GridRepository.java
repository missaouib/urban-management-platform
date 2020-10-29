package com.unicom.urban.management.dao.grid;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Grid;

import java.util.List;

/**
 * 网格
 *
 * @author jiangwen
 */
public interface GridRepository extends CustomizeRepository<Grid, String> {

    /**
     * 根据发布id 和 编辑状态查询所有需要发布的网格
     *
     * @param publishId 发布id
     * @param sts       编辑状态为 编辑中
     * @return 网格list
     */
    List<Grid> findAllByPublish_IdAndRecord_Sts(String publishId, int sts);
    List<Grid> findAllByParentId(String parentId);
    List<Grid> findAllByParentIsNull();

}
