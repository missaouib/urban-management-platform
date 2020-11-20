package com.unicom.urban.management.dao.evaluate;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.vo.SupervisorEvaluateVO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 岗位评价
 * @author liubozhi
 */
public interface PositionRepository extends CustomizeRepository<Statistics, String> {
    /**
     * 监督员上报数
     */
    @Query(value = "SELECT count(ev.event_code) as patrolReport from grid g,`event` ev where g.user_id=ev.user_id AND sts is null AND g.grid_id=?1 AND ev.create_time BETWEEN ?2 and ?3", nativeQuery = true)
    SupervisorEvaluateVO findPatrolReport(String gridId,LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 监督员有效上报数
     */
    @Query(value = "SELECT count(st.inst) as validPatrolReport from grid g,statistics st,`event` ev where g.user_id = st.operate_human_name_id AND st.inst=1 AND ev.id=st.event_id and g.grid_id=?1 AND ev.create_time BETWEEN ?2 and ?3", nativeQuery = true)
    SupervisorEvaluateVO findValidPatrolReport(String gridId,LocalDateTime startTime, LocalDateTime endTime);
    /**
     * 按时核实数
     */
    @Query(value = "SELECT sum(in_time_verify) from statistics st LEFT JOIN `event` ev on st.event_id=ev.id LEFT JOIN grid g ON ev.grid_id=g.id where in_time_verify=1 AND g.grid_id=?1 AND ev.create_time BETWEEN ?2 and ?3", nativeQuery = true)
    SupervisorEvaluateVO findIntimeVerify(String gridId,LocalDateTime startTime, LocalDateTime endTime);
    /**
     * 应核实数
     */
    @Query(value = "SELECT sum(need_verify) from statistics st LEFT JOIN `event` ev on st.event_id=ev.id LEFT JOIN grid g ON ev.grid_id=g.id where need_verify=1 AND g.grid_id=?1 AND ev.create_time BETWEEN ?2 and ?3", nativeQuery = true)
    SupervisorEvaluateVO findINeedVerify(String gridId,LocalDateTime startTime, LocalDateTime endTime);
    /**
     * 按时核查数
     */
//    @Query(value = "SELECT sum(need_verify) from statistics st LEFT JOIN `event` ev on st.event_id=ev.id LEFT JOIN grid g ON ev.grid_id=g.id where need_verify=1 AND g.grid_id=?1 AND ev.create_time BETWEEN ?2 and ?3", nativeQuery = true)
//    SupervisorEvaluateVO findIntimeCheck(String gridId,LocalDateTime startTime, LocalDateTime endTime);
    /**
     * 监督员岗位评价
     */
    @Query(value = "select DISTINCT(s.user_id),gridCount.userName as supervisorName, \n" +
            "gridCount.gridOnwer as gridOnwer, \n" +
            "patrol_report as patrolReport,\n" +
            "valid_patrol_report as validPatrolReport,\n" +
            "(valid_patrol_report/report) as reportVaildNumRate,\n" +
            "in_time_verify as inTimeVerify,\n" +
            "need_verify as needVerify, \n" +
            "(in_time_verify/need_verify) as inTimeVerifyRate,\n" +
            "in_time_check as inTimeCheck,\n" +
            "inst as inst,\n" +
            "public_report as publicReport,\n" +
            "(public_report/valid_report) as publicReportRate,\n" +
            "((valid_patrol_report+valid_public_report)*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_verify/need_send_verify)*0.35) as aggregativeIndicator,\n" +
            "(CASE \n" +
            "\tWHEN 90<=((valid_patrol_report+valid_public_report)*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_verify/need_send_verify)*0.35)<=100 THEN 'A'\t\n" +
            "\tWHEN 75<=((valid_patrol_report+valid_public_report)*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_verify/need_send_verify)*0.35)<90 THEN 'B'\n" +
            "\tWHEN 60<=((valid_patrol_report+valid_public_report)*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_verify/need_send_verify)*0.35)<75 THEN 'C'\n" +
            "\tWHEN 40<=((valid_patrol_report+valid_public_report)*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_verify/need_send_verify)*0.35)<60 THEN 'D'\n" +
            " ELSE 'E' END )as ratingLevel\n" +
            "from (SELECT g.user_id as userId,s.name as userName,count(g.user_id) as gridOnwer from grid g,sys_user s where g.user_id=s.id  GROUP BY user_id,grid_name) as gridCount LEFT JOIN statistics s on gridCount.userId = s.user_id", nativeQuery = true)
    List<Map<String,Object>> findSupervisorEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 受理员岗位评价
     */
    @Query(value = "select su.`name` as operateHumanNameId,\n" +
            "sum(operate),#受理数\n" +
            "sum(in_time_send_verify) as intimeSendVerify,\n" +
            "sum(need_send_verify) as needSendVerify ,\n" +
            "(sum(in_time_send_verify)/sum(need_send_verify)) as SendVerifyRate,#核实按时派发率\n" +
            "sum(in_time_send_check) as intimeSendCheck,sum(need_send_check) as needSendCheck,\n" +
            "(sum(in_time_send_check)/sum(need_send_check)) as needSendCheckRate,#核查按时派发率\n" +
            "(sum(operate)*0.25+(sum(in_time_send_verify)/sum(need_send_verify))*0.4+(sum(in_time_send_check)/sum(need_send_check))*0.35) as aggregativeIndicator,#综合指标值=受理数分值×25%+核实按时派发率分值×40%+核查按时派发率分值×35%\n" +
            "(CASE \n" +
            "\tWHEN 90<=((CASE \n" +
            "\tWHEN sum(operate) > 2001 THEN 100\n" +
            "\t\tWHEN 1201<=sum(operate) <= 2000 THEN 90\n" +
            "\t\tWHEN 501<=sum(operate) <= 1200 THEN 75\n" +
            "\t\tWHEN 301<=sum(operate) <= 500 THEN 60\n" +
            "\t\tWHEN 100<=sum(operate)THEN 0\n" +
            "END)*0.25+(sum(in_time_send_verify)/sum(need_send_verify))*0.4+(sum(in_time_send_check)/sum(need_send_check))*0.35)<=100 THEN 'A'\t\n" +
            "\tWHEN 75<=((CASE \n" +
            "\tWHEN sum(operate) > 2001 THEN 100\n" +
            "\t\tWHEN 1201<=sum(operate) <= 2000 THEN 90\n" +
            "\t\tWHEN 501<=sum(operate) <= 1200 THEN 75\n" +
            "\t\tWHEN 301<=sum(operate) <= 500 THEN 60\n" +
            "\t\tWHEN 100<=sum(operate)THEN 0\n" +
            "END)*0.25+(sum(in_time_send_verify)/sum(need_send_verify))*0.4+(sum(in_time_send_check)/sum(need_send_check))*0.35)<90 THEN 'B'\n" +
            "\tWHEN 60<=((CASE \n" +
            "\tWHEN sum(operate) > 2001 THEN 100\n" +
            "\t\tWHEN 1201<=sum(operate) <= 2000 THEN 90\n" +
            "\t\tWHEN 501<=sum(operate) <= 1200 THEN 75\n" +
            "\t\tWHEN 301<=sum(operate) <= 500 THEN 60\n" +
            "\t\tWHEN 100<=sum(operate)THEN 0\n" +
            "END)*0.25+(sum(in_time_send_verify)/sum(need_send_verify))*0.4+(sum(in_time_send_check)/sum(need_send_check))*0.35)<75 THEN 'C'\n" +
            "\tWHEN 40<=((CASE \n" +
            "\tWHEN sum(operate) > 2001 THEN 100\n" +
            "\t\tWHEN 1201<=sum(operate) <= 2000 THEN 90\n" +
            "\t\tWHEN 501<=sum(operate) <= 1200 THEN 75\n" +
            "\t\tWHEN 301<=sum(operate) <= 500 THEN 60\n" +
            "\t\tWHEN 100<=sum(operate)THEN 0\n" +
            "END)*0.25+(sum(in_time_send_verify)/sum(need_send_verify))*0.4+(sum(in_time_send_check)/sum(need_send_check))*0.35)<60 THEN 'D'\n" +
            "\tELSE 'E' END\n" +
            ")as ratingLevel #评价等级\n" +
            "from  statistics st,sys_user su,`event` ev where st.operate_human_name_id is not null and st.operate_human_name_id=su.id and st.event_id=ev.id AND ev.create_time BETWEEN ?1 and ?2 GROUP BY st.operate_human_name_id", nativeQuery = true)
    List<Map<String, Object>> findAcceptorEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 值班长岗位评价
     */
    @Query(value = "select \n" +
            "#姓名\n" +
            "su.name as instHumanName,\n" +
            "#立案数\n" +
            "SUM(inst),\n" +
            "#按时立案数\n" +
            "sum(in_time_inst) as intimeInst,\n" +
            "sum(in_time_inst)/sum(inst) as intimeInstRate,\n" +
            "#准确立案数(立案数  - 作废数)\n" +
            "sum(inst)-sum(cancel) as exactInst,\n" +
            "(sum(inst)-sum(cancel))/sum(inst) as exactInstRate,\n" +
            "#按时结案数\n" +
            "sum(in_time_close) as inTimeClose,\n" +
            "#应结案数\n" +
            "sum(`close`),\n" +
            " (sum(inst)-sum(cancel))/sum(`close`) as inTimeCloseRate,\n" +
            " (sum(in_time_close)/sum(`close`)*0.25+((sum(inst)-sum(cancel))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35) as aggregativeIndicator,\n" +
            "(CASE \n" +
            "\tWHEN 90<=(sum(in_time_close)/sum(`close`)*0.25+((sum(inst)-sum(cancel))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35) <=100 THEN 'A'\t\n" +
            "\tWHEN 75<=(sum(in_time_close)/sum(`close`)*0.25+((sum(inst)-sum(cancel))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35) <90 THEN 'B'\n" +
            "\tWHEN 60<=(sum(in_time_close)/sum(`close`)*0.25+((sum(inst)-sum(cancel))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35) <75 THEN 'C'\n" +
            "\tWHEN 40<=(sum(in_time_close)/sum(`close`)*0.25+((sum(inst)-sum(cancel))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35) <60 THEN 'D'\n" +
            "\tELSE 'E' END\n" +
            ") as ratingLevel#评价等级 \n" +
            "from  statistics st,sys_user su,`event` ev where st.inst_human_name_id is not null and st.inst_human_name_id=su.id and st.event_id=ev.id AND ev.create_time BETWEEN ?1 and ?2 GROUP BY st.inst_human_name_id\n", nativeQuery = true)
    List<Map<String, Object>> findShiftForemanEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 派遣员岗位评价
     */
    @Query(value = "SELECT\n" +
            "su.name as dispatch,\n" +
            "sum(to_dispatch) as toDispatch,\n" +
            "sum(in_time_dispatch) as intimeDispatch,\n" +
            "sum(need_dispatch) as needDispatch,\n" +
            "(sum(in_time_dispatch)/sum(need_dispatch)) as intimeDispatchRate,\n" +
            "sum(accuracy_dispatch) as accuracyDispatch,\n" +
            "(sum(accuracy_dispatch)/sum(in_time_dispatch)) as accuracyDispatchRate,\n" +
            "(sum(in_time_send_check)/sum(need_send_check)) as needSendCheckRate,\n" +
            " (sum(to_dispatch)*100*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*100*0.4+(sum(accuracy_dispatch)/sum(in_time_dispatch))*100*0.4) as aggregativeIndicator,\n" +
            "#评价等级\n" +
            "(CASE \n" +
            "\tWHEN 90<=(sum(to_dispatch)*100*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*100*0.4+(sum(accuracy_dispatch)/sum(in_time_dispatch))*100*0.4)<=100 THEN 'A'\t\n" +
            "\tWHEN 75<=(sum(to_dispatch)*100*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*100*0.4+(sum(accuracy_dispatch)/sum(in_time_dispatch))*100*0.4)<90 THEN 'B'\n" +
            "\tWHEN 60<=(sum(to_dispatch)*100*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*100*0.4+(sum(accuracy_dispatch)/sum(in_time_dispatch))*100*0.4)<75 THEN 'C'\n" +
            "\tWHEN 40<=(sum(to_dispatch)*100*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*100*0.4+(sum(accuracy_dispatch)/sum(in_time_dispatch))*100*0.4)<60 THEN 'D'\n" +
            "\tELSE 'E' END\n" +
            ") as ratingLevel\n" +
            "from  statistics st,sys_user su ,`event` ev where dispatch is not null and st.dispatch=su.id and st.event_id=ev.id AND ev.create_time BETWEEN ?1 and ?2 GROUP BY dispatch", nativeQuery = true)
    List<Map<String, Object>> findDispatcherEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);
}
