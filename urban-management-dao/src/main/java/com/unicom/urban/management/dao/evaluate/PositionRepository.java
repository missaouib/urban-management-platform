package com.unicom.urban.management.dao.evaluate;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Statistics;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 岗位评价
 * @author liubozhi
 */
public interface PositionRepository extends CustomizeRepository<Statistics, LocalDateTime> {

    /**
     * 监督员岗位评价
     */
    @Query(value = "select su.`name` as supervisorName, \n" +
            "            g.grid_name as gridOnwer, \n" +
            "            #监督员上报数：监督员巡查上报的部件和事件问题数\n" +
            "            sum(patrol_report) as patrolReport,\n" +
            "            #监督员有效上报数：监督员上报数中，经监督中心审核立案的案件数\n" +
            "            sum(valid_patrol_report) as validPatrolReport,\n" +
            "            #监督员有效上报率：监督员有效上报数/监督员上报数×100%。\n" +
            "            (sum(valid_patrol_report)/sum(report)) as reportVaildNumRate,\n" +
            "            #按时核实数：监督员在规定的工作时限内完成核实的问题数。\n" +
            "            sum(in_time_verify) as inTimeVerify,\n" +
            "            #应核实数：监督举报数中，应核实的部件和事件问题数\n" +
            "            sum(need_verify) as needVerify, \n" +
            "            #按时核实率：按时核实数/应核实数×100%\n" +
            "            (sum(in_time_verify)/sum(need_verify)) as inTimeVerifyRate,\n" +
            "            #按时核查数：监督员在规定的工作时限内完成核查并回复的次数\n" +
            "            sum(in_time_check) as inTimeCheck,\n" +
            "            #漏报数：经确认的监督员应上报而未上报的问题数\n" +
            "            sum(st.verify) as publicReport,\n" +
            "            #立案数：经审核后符合立案条件，确定立案并应派遣给专业部门处置的案件数\n" +
            "            (sum(st.verify)+sum(st.patrol_report))as inst,\n" +
            "            #漏报率：漏报数/立案数×100%\n" +
            "            (sum(st.verify)/(sum(st.verify)+sum(st.patrol_report))) as publicReportRate,\n" +
            "            #综合指标值=监督员有效上报率分值×40%+漏报率分值×20%+按时核实率分值×20%+按时核查率分值×20%\n" +
            "            ((sum(valid_patrol_report)*100*0.4+(1-(sum(public_report)/sum(inst)))*100*0.2+(sum(in_time_verify)/sum(need_verify))*100*0.2)+ (sum(in_time_check)/sum(need_send_check))*100*0.2) as aggregativeIndicator,\n" +
            "            (CASE \n" +
            "            WHEN 90<=((sum(valid_patrol_report)*100*0.4+(1-(sum(public_report)/sum(inst)))*100*0.2+(sum(in_time_verify)/sum(need_verify))*100*0.2)+ (sum(in_time_check)/sum(need_send_check))*100*0.2)<=100 THEN 'A'\n" +
            "            WHEN 75<=((sum(valid_patrol_report)*100*0.4+(1-(sum(public_report)/sum(inst)))*100*0.2+(sum(in_time_verify)/sum(need_verify))*100*0.2)+ (sum(in_time_check)/sum(need_send_check))*100*0.2)<90 THEN 'B'\n" +
            "            WHEN 60<=((sum(valid_patrol_report)*100*0.4+(1-(sum(public_report)/sum(inst)))*100*0.2+(sum(in_time_verify)/sum(need_verify))*100*0.2)+ (sum(in_time_check)/sum(need_send_check))*100*0.2)<75 THEN 'C'\n" +
            "            WHEN 40<=((sum(valid_patrol_report)*100*0.4+(1-(sum(public_report)/sum(inst)))*100*0.2+(sum(in_time_verify)/sum(need_verify))*100*0.2)+ (sum(in_time_check)/sum(need_send_check))*100*0.2)<60 THEN 'D'\n" +
            "             ELSE 'E' END ) as ratingLevel\n" +
            "            from grid g,statistics st,`event` ev,sys_user su where  \n" +
            "            (su.id=st.report_patrol_name_id or su.id=st.check_patrol_id or su.id=st.verify_patrol_name_id) and\n" +
            "            g.id=ev.grid_id and ev.id=st.event_id  and ev.create_time between ?1 and ?2 GROUP BY su.`name`,g.id \n", nativeQuery = true)
    List<Map<String,Object>> findSupervisorEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 受理员岗位评价
     */
    @Query(value = "select su.`name` as operateHumanNameId,\n" +
            "sum(operate) as operate,#受理数\n" +
            "sum(in_time_send_verify) as intimeSendVerify,\n" +
            "sum(need_send_verify) as needSendVerify ,\n" +
            "(sum(in_time_send_verify)/sum(need_send_verify)) as sendVerifyRate,#核实按时派发率\n" +
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
            "from  statistics st,sys_user su,`event` ev where st.event_id=ev.id and  (su.id=st.operate_human_id or su.id=st.send_check_human_name) and ev.create_time between ?1 and ?2 GROUP BY su.`name`", nativeQuery = true)
    List<Map<String, Object>> findAcceptorEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 值班长岗位评价
     */
    @Query(value = "select \n" +
            "#姓名\n" +
            "su.name as instHumanName,\n" +
            "#立案数\n" +
            "SUM(inst) as inst,\n" +
            "#按时立案数\n" +
            "sum(in_time_inst) as intimeInst,\n" +
            "#按时立案率 = 按时立案数/立案数×100%\n" +
            "sum(in_time_inst)/sum(inst) as intimeInstRate,\n" +
            "#准确立案数(立案数  - 作废数)\n" +
            "sum(inst)-ifnull(sum(cancel),0) as exactInst,\n" +
            "#准确立案率 = 准确立案数/立案数×100%\n" +
            "(sum(inst)-ifnull(sum(cancel),0))/sum(inst) as exactInstRate,\n" +
            "#按时结案数\n" +
            "sum(in_time_close) as inTimeClose,\n" +
            "#应结案数\n" +
            "sum(`close`) as close,\n" +
            "#按时结案率 按时结案数/结案数×100%。\n" +
            " sum(in_time_close)/sum(`close`) as inTimeCloseRate,\n" +
            " #综合指标值 = 按时立案率分值×25%+准确立案率分值×40%+按时结案率分值×35%\n" +
            " ((sum(in_time_close)/sum(`close`))*0.25+((sum(inst)-ifnull(sum(cancel),0))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35) as aggregativeIndicator,\n" +
            "(CASE \n" +
            "\tWHEN 90<=((sum(in_time_close)/sum(`close`))*0.25+((sum(inst)-ifnull(sum(cancel),0))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35)<=100 THEN 'A'\t\n" +
            "\tWHEN 75<=((sum(in_time_close)/sum(`close`))*0.25+((sum(inst)-ifnull(sum(cancel),0))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35)<90 THEN 'B'\n" +
            "\tWHEN 60<=((sum(in_time_close)/sum(`close`))*0.25+((sum(inst)-ifnull(sum(cancel),0))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35)<75 THEN 'C'\n" +
            "\tWHEN 40<=((sum(in_time_close)/sum(`close`))*0.25+((sum(inst)-ifnull(sum(cancel),0))/sum(inst))*0.4+(sum(in_time_close)/sum(`close`))*0.35)<60 THEN 'D'\n" +
            "\tELSE 'E' END\n" +
            ") as ratingLevel#评价等级 \n" +
            "from  statistics st,sys_user su,`event` ev where (st.inst_human_name_id=su.id or st.close_human_name_id=su.id) and st.event_id=ev.id and ev.create_time between ?1 and ?2 GROUP BY su.name\n", nativeQuery = true)
    List<Map<String, Object>> findShiftForemanEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 派遣员岗位评价
     */
    @Query(value = "SELECT\n" +
            "#姓名\n" +
            "su.name as dispatch,\n" +
            "#派遣数\n" +
            "sum(dispatch) as toDispatch,\n" +
            "#按时派遣数\n" +
            "sum(in_time_dispatch) as intimeDispatch,\n" +
            "#应派遣数\n" +
            "sum(need_dispatch) as needDispatch,\n" +
            "#按时派遣率：按时派遣数/应派遣数×100%\n" +
            "(sum(in_time_dispatch)/sum(need_dispatch)) as intimeDispatchRate,\n" +
            "#准确派遣数（派遣数-返工数）\n" +
            "sum(need_dispatch)-ifnull(sum(rework),0) as accuracyDispatch,\n" +
            "#准确派遣率：准确派遣数/应派遣数×100%\n" +
            "(sum(need_dispatch)-ifnull(sum(rework),0))/sum(need_dispatch) as accuracyDispatchRate,\n" +
            "#综合指标值=派遣数分值×20%+按时派遣率×40%+准确派遣率分值×40%\n" +
            " (sum(to_dispatch)*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*0.4+(sum(need_dispatch)-ifnull(sum(rework),0))/sum(need_dispatch)*0.4) as aggregativeIndicator,\n" +
            "#评价等级\n" +
            "(CASE \n" +
            "\tWHEN 90<=(sum(to_dispatch)*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*0.4+(sum(need_dispatch)-ifnull(sum(rework),0))/sum(need_dispatch)*0.4)<=100 THEN 'A'\t\n" +
            "\tWHEN 75<=(sum(to_dispatch)*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*0.4+(sum(need_dispatch)-ifnull(sum(rework),0))/sum(need_dispatch)*0.4)<90 THEN 'B'\n" +
            "\tWHEN 60<=(sum(to_dispatch)*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*0.4+(sum(need_dispatch)-ifnull(sum(rework),0))/sum(need_dispatch)*0.4)<75 THEN 'C'\n" +
            "\tWHEN 40<=(sum(to_dispatch)*0.2+(sum(in_time_dispatch)/sum(need_dispatch))*0.4+(sum(need_dispatch)-ifnull(sum(rework),0))/sum(need_dispatch)*0.4)<60 THEN 'D'\n" +
            "\tELSE 'E' END\n" +
            ") as ratingLevel\n" +
            "from  statistics st,sys_user su,`event` ev where dispatch is not null and st.dispatch_human_name_id=su.id and st.event_id=ev.id and ev.create_time between ?1 and ?2 GROUP BY su.name", nativeQuery = true)
    List<Map<String, Object>> findDispatcherEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);
}
