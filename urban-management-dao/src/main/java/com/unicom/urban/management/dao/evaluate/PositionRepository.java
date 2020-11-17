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

    @Query(value = "select DISTINCT(s.user_id),gridCount.userName as SupervisorName, \n" +
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
}
