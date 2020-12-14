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

    List<Grid> findAllByLevelIn(List<Integer> levels);

    /**
     * 根据网格code查询数据
     *
     * @param gridCode code
     * @return 网格
     */
    Grid findByGridCode(String gridCode);

    /**
     * 查询发布挖的网格
     * 目前网格和区域街道在一个数据表中 所以需要level字段 将来会修改
     *
     * @param level 判断网格
     * @param sts   发布
     * @return 网格
     */
    List<Grid> findAllByLevelAndRecord_Sts(int level, int sts);



    List<Grid> findAllByParent_IdAndGridName(String pId,String gridName);

}
