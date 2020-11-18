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
    @Query(value = "select operate_human_name_id as operateHumanNameId,\n" +
            "operate,#受理数\n" +
            "in_time_send_verify as intimeSendVerify,\n" +
            "need_send_verify as needSendVerify ,\n" +
            "(in_time_send_verify/need_send_verify) as SendVerifyRate,#核实按时派发率\n" +
            "in_time_send_check as intimeSendCheck,need_send_check as needSendCheck,\n" +
            "(in_time_send_check/need_send_check) as needSendCheckRate,#核查按时派发率\n" +
            "(operate*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_check/need_send_check)*0.35) as aggregativeIndicator,#综合指标值=受理数分值×25%+核实按时派发率分值×40%+核查按时派发率分值×35%\n" +
            "(CASE \n" +
            "\tWHEN 90<=(operate*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_check/need_send_check)*0.35)<=100 THEN 'A'\t\n" +
            "\tWHEN 75<=(operate*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_check/need_send_check)*0.35)<90 THEN 'B'\n" +
            "\tWHEN 60<=(operate*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_check/need_send_check)*0.35)<75 THEN 'C'\n" +
            "\tWHEN 40<=(operate*0.25+(in_time_send_verify/need_send_verify)*0.4+(in_time_send_check/need_send_check)*0.35)<60 THEN 'D'\n" +
            "\tELSE 'E' END\n" +
            ")as ratingLevel #评价等级\n" +
            "from  statistics where operate_human_name_id is not null", nativeQuery = true)
    List<Map<String, Object>> findAcceptorEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 值班长岗位评价
     */
    @Query(value = "select \n" +
            "#姓名\n" +
            "inst_human_name_id as instHumanName,\n" +
            "#立案数\n" +
            "inst,\n" +
            "#按时立案数\n" +
            "in_time_inst as intimeInst,\n" +
            "#按时立案率 = 按时立案数/立案数\n" +
            "(in_time_inst/inst) as intimeInstRate,\n" +
            "#准确立案数(立案数  - 作废数)\n" +
            "(inst-cancel) as exactInst,\n" +
            "#准确立案率 = 准确立案数/立案数\n" +
            "((inst-cancel)/inst) as exactInstRate,\n" +
            "#按时结案数\n" +
            "in_time_close as inTimeClose,\n" +
            "#应结案数\n" +
            "`close`,\n" +
            "#按时结案率 按时结案数/结案数\n" +
            " (in_time_close/`close`) as inTimeCloseRate,\n" +
            " ((in_time_inst/inst)*0.25+((inst-cancel)/inst)*0.4+(in_time_close/`close`)*0.35) as aggregativeIndicator,\n" +
            "\n" +
            "(CASE \n" +
            "\tWHEN 90<=((in_time_inst/inst)*0.25+((inst-cancel)/inst)*0.4+(in_time_close/`close`)*0.35)<=100 THEN 'A'\t\n" +
            "\tWHEN 75<=((in_time_inst/inst)*0.25+((inst-cancel)/inst)*0.4+(in_time_close/`close`)*0.35)<90 THEN 'B'\n" +
            "\tWHEN 60<=((in_time_inst/inst)*0.25+((inst-cancel)/inst)*0.4+(in_time_close/`close`)*0.35)<75 THEN 'C'\n" +
            "\tWHEN 40<=((in_time_inst/inst)*0.25+((inst-cancel)/inst)*0.4+(in_time_close/`close`)*0.35)<60 THEN 'D'\n" +
            "\tELSE 'E' END\n" +
            ") as ratingLevel#评价等级 \n" +
            "from  statistics where inst_human_name_id is not null\n", nativeQuery = true)
    List<Map<String, Object>> findShiftForemanEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 派遣员岗位评价
     */
    @Query(value = "SELECT\n" +
            "#姓名\n" +
            "dispatch,\n" +
            "#派遣数\n" +
            "to_dispatch as toDispatch,\n" +
            "#按时派遣数\n" +
            "in_time_dispatch as intimeDispatch,\n" +
            "#应派遣数\n" +
            "need_dispatch as needDispatch,\n" +
            "(in_time_dispatch/need_dispatch) as intimeDispatchRate,\n" +
            "accuracy_dispatch as accuracyDispatch,\n" +
            "(accuracy_dispatch/in_time_dispatch) as accuracyDispatchRate,\n" +
            "(in_time_send_check/need_send_check) as needSendCheckRate,\n" +
            " (to_dispatch*0.2+(in_time_dispatch/need_dispatch)*0.4+(accuracy_dispatch/in_time_dispatch)*0.4) as aggregativeIndicator,\n" +
            "#评价等级\n" +
            "(CASE \n" +
            "\tWHEN 90<=(to_dispatch*0.2+(in_time_dispatch/need_dispatch)*0.4+(accuracy_dispatch/in_time_dispatch)*0.4)<=100 THEN 'A'\t\n" +
            "\tWHEN 75<=(to_dispatch*0.2+(in_time_dispatch/need_dispatch)*0.4+(accuracy_dispatch/in_time_dispatch)*0.4)<90 THEN 'B'\n" +
            "\tWHEN 60<=(to_dispatch*0.2+(in_time_dispatch/need_dispatch)*0.4+(accuracy_dispatch/in_time_dispatch)*0.4)<75 THEN 'C'\n" +
            "\tWHEN 40<=(to_dispatch*0.2+(in_time_dispatch/need_dispatch)*0.4+(accuracy_dispatch/in_time_dispatch)*0.4)<60 THEN 'D'\n" +
            "\tELSE 'E' END\n" +
            ") as ratingLevel\n" +
            "from  statistics where dispatch is not null\n", nativeQuery = true)
    List<Map<String, Object>> findDispatcherEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime);
}
