package com.unicom.urban.management.dao.statistics;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.Statistics;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 网格
 *
 * @author jiangwen
 */
public interface StatisticsRepository extends CustomizeRepository<Statistics, String> {

    List<Statistics> findAllByEvent_IdOrderByStartTimeDesc(String eventId);


    /**
     * 根据sort字段排序
     *
     * @param eventId 事件id
     * @return list
     */
    List<Statistics> findAllByEvent_IdOrderBySortDesc(String eventId);

    /**
     * 根据事件id查询该事件最后环节的实体
     * 事件id和结束时间为空
     *
     * @param eventId 事件id
     * @return 环节
     */
    Statistics findByEvent_IdAndEndTimeIsNull(String eventId);


    List<Statistics> findAllByUser_IdAndEndTimeIsNotNull(String userId);

    /**
     * 已挂账列表
     *
     * @param hang 1
     * @return list
     */
    List<Statistics> findAllByHang(Integer hang);

    /**
     * 已挂账列表
     *
     * @param cancel 1
     * @return list
     */
    List<Statistics> findAllByCancel(Integer cancel);

    List<Statistics> findByNotOperate(Integer notOperate);

    /**
     * 按期结案数 ——
     *
     * @param gridId 网格id
     * @return 数据
     */
    @Query(value = "SELECT s,e,g FROM Statistics s LEFT JOIN Event e on s.event = e.id LEFT JOIN e.grid g on e.grid.id = g.id WHERE s.inTimeClose= 1 and g.id = ?1 and e.createTime between ?2 and ?3 ")
    List<Statistics> findAllByInTimeClose(String gridId, LocalDateTime startTime, LocalDateTime endTime);


    List<Statistics> findAllByEventInAndTaskNameAndDisposeUnit_Id(Collection<Event> event, String taskName, String disposeUnit_id);


    /**
     * 结案数 ——
     *
     * @param gridId 网格id
     * @return 数据
     */
    @Query(value = "SELECT s,e,g FROM Statistics s LEFT JOIN Event e on s.event = e.id LEFT JOIN e.grid g on e.grid.id = g.id WHERE s.close = 1 and g.id = ?1 and e.createTime between ?2 and ?3 ")
    List<Statistics> findAllByClose(String gridId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 应结案数 按照规范按时结案的事件 能够结案的事件一定先立案了 ——
     *
     * @param gridId 网格id
     * @return 数据
     */
    @Query(value = "SELECT s,e,g FROM Statistics s LEFT JOIN Event e on s.event = e.id LEFT JOIN e.grid g on e.grid.id = g.id WHERE (s.close = 1 or s.toClose = 1) and g.id = ?1 and e.createTime between ?2 and ?3 ")
    List<Statistics> findAllByCloseOrToClose(String gridId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 监督举报核实数 受理员上报 并且 值班长立案的事件
     *
     * @param gridId 网格id
     * @return 数据
     */
    @Query(value = "SELECT s,e,g FROM Statistics s LEFT JOIN Event e on s.event = e.id LEFT JOIN e.grid g on e.grid.id = g.id WHERE s.event.id in (SELECT ss.event.id FROM Statistics ss where ss.publicReport = 1) and s.inst = 1 and g.id = ?1 and e.createTime between ?2 and ?3 ")
    List<Statistics> findAllByPublicReportAndInst(String gridId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 立案数 派遣员派遣并且值班长立案的数量 因为立案的先决条件是派遣 所以直接查询立案 ——
     *
     * @param gridId 网格id
     * @return 数据
     */
    @Query(value = "SELECT s,e,g FROM Statistics s LEFT JOIN Event e on s.event = e.id LEFT JOIN e.grid g on e.grid.id = g.id WHERE s.inst = 1 and g.id = ?1 and e.createTime between ?2 and ?3 ")
    List<Statistics> findAllByInst(String gridId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 挂账数 理解：应派遣 应该是派遣员决定派遣的事件，在值班长立案之后应该由派遣员决定是派遣还是作废一类的操作，其中包含挂账
     *
     * @param gridId 网格id
     * @return 数据
     */
    @Query(value = "SELECT s,e,g FROM Statistics s LEFT JOIN Event e on s.event = e.id LEFT JOIN e.grid g on e.grid.id = g.id WHERE s.hang = 1 and g.id = ?1 ")
    List<Statistics> findAllByHang(String gridId);

    /**
     * 高发区域
     *
     * @param
     * @return 数据
     */
    @Query(value = "SELECT (SUM(ifnull(inst,0))) as totalInst,sum(ifnull(`close`,0)) as totalClose,g.id as gridId  from `event` ev,grid g,statistics st where g.id=ev.grid_id and ev.id=st.event_id GROUP BY g.id", nativeQuery = true)
    List<Map<String, Object>> findHotGrid();

    /* -------------------------------------------------------------- */

    /**
     * report : int //上报
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @Query(value = "SELECT s,e FROM Statistics s LEFT JOIN Event e on s.event = e.id WHERE s.report = 1 and e.createTime between ?1 and ?2 ")
    List<Statistics> findByReport(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * operate : int //受理 批转值班长 触发
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @Query(value = "SELECT s,e FROM Statistics s LEFT JOIN Event e on s.event = e.id WHERE s.operate = 1 and e.createTime between ?1 and ?2 ")
    List<Statistics> findByOperate(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * inst : int //立案 值班长 立案
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @Query(value = "SELECT s,e FROM Statistics s LEFT JOIN Event e on s.event = e.id WHERE s.inst = 1 and e.createTime between ?1 and ?2 ")
    List<Statistics> findByInst(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * dispatch : int //派遣 派遣员 已派遣 派遣数 所有 专业 应处置
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @Query(value = "SELECT s,e FROM Statistics s LEFT JOIN Event e on s.event = e.id WHERE s.dispatch = 1 and e.createTime between ?1 and ?2 ")
    List<Statistics> findByDispatch(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * dispose : int //处置 //专业部门
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @Query(value = "SELECT s,e FROM Statistics s LEFT JOIN Event e on s.event = e.id WHERE s.dispose = 1 and e.createTime between ?1 and ?2 ")
    List<Statistics> findByDispose(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * checkNum : int //核查数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @Query(value = "SELECT s,e FROM Statistics s LEFT JOIN Event e on s.event = e.id WHERE s.checkNum = 1 and e.createTime between ?1 and ?2 ")
    List<Statistics> findByCheckNum(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * close : int //结案
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @Query(value = "SELECT s,e FROM Statistics s LEFT JOIN Event e on s.event = e.id WHERE s.close = 1 and e.createTime between ?1 and ?2 ")
    List<Statistics> findByClose(LocalDateTime startTime, LocalDateTime endTime);
    /**
     * 岗位案件数量
     *
     * @return int
     */
    @Query(value = "select sum(1) from statistics where report_patrol_name_id=?1  or verify_patrol_name_id=?1  or operate_human_name_id=?1  or send_verify_human_name_id=?1  or send_check_human_name=?1  or inst_human_name_id=?1  or close_human_name_id=?1  or dispatch_human_name_id=?1  or dispose_unit_name_id=?1  or check_patrol_name=?1  or check_trans_human_name=?1 ",nativeQuery = true)
    Integer findTakeCase(String userId);
    /**
     * 办理案件数量
     *
     * @return int
     */
    @Query(value = "select sum(inst) from statistics where report_patrol_name_id=?1  or verify_patrol_name_id=?1  or operate_human_name_id=?1  or send_verify_human_name_id=?1  or send_check_human_name=?1  or inst_human_name_id=?1  or close_human_name_id=?1  or dispatch_human_name_id=?1  or dispose_unit_name_id=?1  or check_patrol_name=?1  or check_trans_human_name=?1 ",nativeQuery = true)
    Integer findInstCase(String userId);
    /**
     * 结案件数量
     *
     * @return int
     */
    @Query(value = "select sum(close) from statistics where report_patrol_name_id=?1  or verify_patrol_name_id=?1  or operate_human_name_id=?1  or send_verify_human_name_id=?1  or send_check_human_name=?1  or inst_human_name_id=?1  or close_human_name_id=?1  or dispatch_human_name_id=?1  or dispose_unit_name_id=?1  or check_patrol_name=?1  or check_trans_human_name=?1",nativeQuery = true)
    Integer findCloseCase(String userId);
}
