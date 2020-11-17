package com.unicom.urban.management.service.evaluate;

import com.unicom.urban.management.dao.evaluate.PositionRepository;
import com.unicom.urban.management.pojo.vo.SupervisorEvaluateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 岗位评价service
 *
 * @author liubozhi
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    /**
     * 监督员岗位评价
     * @param startTime
     * @param endTime
     * @return
     */
    public List<SupervisorEvaluateVO> findSupervisorEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String,Object>> maplist = positionRepository.findSupervisorEvaluateByCondition(startTime, endTime);
        List<SupervisorEvaluateVO> list = new ArrayList<>();
        if (maplist!= null && maplist.size() > 0){
            for (Map<String,Object> map : maplist){
                SupervisorEvaluateVO sVO = new SupervisorEvaluateVO();
                sVO.setSupervisorName(map.get("SupervisorName").toString());
                sVO.setGridOnwer(map.get("gridOnwer") == null ? 0 : (Integer.parseInt(map.get("gridOnwer").toString())) );
                sVO.setPatrolReport(map.get("patrolReport") == null ? 0 :  (Integer.parseInt(map.get("patrolReport").toString())) );
                sVO.setValidPatrolReport(map.get("validPatrolReport")== null ? 0 :  (Integer.parseInt(map.get("validPatrolReport").toString())) );
                sVO.setReportVaildNumRate(map.get("reportVaildNumRate")== null ? (float) 0.0 : Float.parseFloat(map.get("reportVaildNumRate").toString()));
                sVO.setIntimeVerify(map.get("intimeVerify")== null ? 0 :  (Integer.parseInt(map.get("intimeVerify").toString())) );
                sVO.setNeedVerify(map.get("needVerify")== null ? 0 :  (Integer.parseInt(map.get("needVerify").toString())) );
                sVO.setInTimeVerifyRate(map.get("inTimeVerifyRate")== null ? (float) 0.0 : Float.parseFloat(map.get("inTimeVerifyRate").toString()));
                sVO.setInTimeCheck(map.get("inTimeCheck;")== null ? 0 :  (Integer.parseInt(map.get("inTimeCheck").toString())) );
                sVO.setPublicReport(map.get("publicReport")== null ? 0 :  (Integer.parseInt(map.get("publicReport").toString())) );
                sVO.setInst(map.get("inst")== null ? 0 :  (Integer) map.get("inst"));
                sVO.setPublicReportRate(map.get("publicReportRate")== null ? (float) 0.0 : Float.parseFloat(map.get("publicReportRate").toString()));
                sVO.setAggregativeIndicator(map.get("aggregativeIndicator")== null ? (float) 0.0 : Float.parseFloat(map.get("aggregativeIndicator").toString()));
                sVO.setRatingLevel(map.get("ratingLevel").toString());
                list.add(sVO);
            }
        }
        return list;
    }


}
