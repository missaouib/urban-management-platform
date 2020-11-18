package com.unicom.urban.management.service.evaluate;

import com.unicom.urban.management.dao.evaluate.PositionRepository;
import com.unicom.urban.management.pojo.vo.AcceptorEvaluateVO;
import com.unicom.urban.management.pojo.vo.DispatcherEvaluateVO;
import com.unicom.urban.management.pojo.vo.ShiftForemanEvaluateVO;
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
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public List<SupervisorEvaluateVO> findSupervisorEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String, Object>> mapList = positionRepository.findSupervisorEvaluateByCondition(startTime, endTime);
        List<SupervisorEvaluateVO> list = new ArrayList<>();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                SupervisorEvaluateVO sVO = new SupervisorEvaluateVO();
                sVO.setSupervisorName(map.get("SupervisorName").toString());
                sVO.setGridOnwer(map.get("gridOnwer") == null ? 0 : (Integer.parseInt(map.get("gridOnwer").toString())));
                sVO.setPatrolReport(map.get("patrolReport") == null ? 0 : (Integer.parseInt(map.get("patrolReport").toString())));
                sVO.setValidPatrolReport(map.get("validPatrolReport") == null ? 0 : (Integer.parseInt(map.get("validPatrolReport").toString())));
                sVO.setReportVaildNumRate(map.get("reportVaildNumRate") == null ? (float) 0.0 : Float.parseFloat(map.get("reportVaildNumRate").toString()));
                sVO.setIntimeVerify(map.get("intimeVerify") == null ? 0 : (Integer.parseInt(map.get("intimeVerify").toString())));
                sVO.setNeedVerify(map.get("needVerify") == null ? 0 : (Integer.parseInt(map.get("needVerify").toString())));
                sVO.setInTimeVerifyRate(map.get("inTimeVerifyRate") == null ? (float) 0.0 : Float.parseFloat(map.get("inTimeVerifyRate").toString()));
                sVO.setInTimeCheck(map.get("inTimeCheck;") == null ? 0 : (Integer.parseInt(map.get("inTimeCheck").toString())));
                sVO.setPublicReport(map.get("publicReport") == null ? 0 : (Integer.parseInt(map.get("publicReport").toString())));
                sVO.setInst(map.get("inst") == null ? 0 : (Integer) map.get("inst"));
                sVO.setPublicReportRate(map.get("publicReportRate") == null ? (float) 0.0 : Float.parseFloat(map.get("publicReportRate").toString()));
                sVO.setAggregativeIndicator(map.get("aggregativeIndicator") == null ? (float) 0.0 : Float.parseFloat(map.get("aggregativeIndicator").toString()));
                sVO.setRatingLevel(map.get("ratingLevel").toString());
                list.add(sVO);
            }
        }
        return list;
    }

    /**
     * 受理员岗位评价
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public List<AcceptorEvaluateVO> findAcceptorEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String, Object>> mapList = positionRepository.findAcceptorEvaluateByCondition(startTime, endTime);

        List<AcceptorEvaluateVO> list = new ArrayList<>();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                AcceptorEvaluateVO sVO = new AcceptorEvaluateVO();
                sVO.setOperateHumanNameId(map.get("operateHumanNameId") == null ? "" : map.get("operateHumanNameId").toString());
                sVO.setOperate(map.get("operate") == null ? 0 : (Integer.parseInt(map.get("operate").toString())));
                sVO.setIntimeSendVerify(map.get("intimeSendVerify") == null ? 0 : (Integer.parseInt(map.get("intimeSendVerify").toString())));
                sVO.setNeedSendVerify(map.get("needSendVerify") == null ? 0 : (Integer.parseInt(map.get("needSendVerify").toString())));
                sVO.setSendVerifyRate(map.get("SendVerifyRate") == null ? (float) 0.0 : Float.parseFloat(map.get("SendVerifyRate").toString()));
                sVO.setIntimeSendCheck(map.get("intimeSendCheck") == null ? 0 : (Integer.parseInt(map.get("intimeSendCheck").toString())));
                sVO.setNeedSendCheck(map.get("needSendCheck") == null ? 0 : (Integer.parseInt(map.get("needSendCheck").toString())));
                sVO.setNeedSendCheckRate(map.get("needSendCheckRate") == null ? 0 : (Integer.parseInt(map.get("needSendCheckRate").toString())));
                sVO.setAggregativeIndicator(map.get("aggregativeIndicator") == null ? (float) 0.0 : Float.parseFloat(map.get("aggregativeIndicator").toString()));
                sVO.setRatingLevel(map.get("ratingLevel") == null ? "" : map.get("ratingLevel").toString());
            }
        }
        return list;
    }

    /**
     * 值班长岗位评价
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public List<ShiftForemanEvaluateVO> findShiftForemanEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String, Object>> mapList = positionRepository.findShiftForemanEvaluateByCondition(startTime, endTime);
        List<ShiftForemanEvaluateVO> list = new ArrayList<>();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                ShiftForemanEvaluateVO sVO = new ShiftForemanEvaluateVO();
                sVO.setInstHumanName(map.get("instHumanName") == null ? "" : map.get("instHumanName").toString());
                sVO.setInst(map.get("inst") == null ? 0 : (Integer.parseInt(map.get("inst").toString())));
                sVO.setIntimeInst(map.get("intimeInst") == null ? 0 : (Integer.parseInt(map.get("intimeInst").toString())));
                sVO.setIntimeInstRate(map.get("intimeInstRate") == null ? (float) 0.0 : Float.parseFloat(map.get("intimeInstRate").toString()));
                sVO.setExactInst(map.get("exactInst") == null ? 0 : (Integer.parseInt(map.get("exactInst").toString())));
                sVO.setExactInstRate(map.get("exactInstRate") == null ? (float) 0.0 : Float.parseFloat(map.get("exactInstRate").toString()));
                sVO.setInTimeClose(map.get("inTimeClose") == null ? 0 : (Integer.parseInt(map.get("inTimeClose").toString())));
                sVO.setClose(map.get("close") == null ? 0 : (Integer.parseInt(map.get("close").toString())));
                sVO.setInTimeCloseRate(map.get("inTimeCloseRate") == null ? (float) 0.0 : Float.parseFloat(map.get("inTimeCloseRate").toString()));
                sVO.setAggregativeIndicator(map.get("aggregativeIndicator") == null ? (float) 0.0 : Float.parseFloat(map.get("aggregativeIndicator").toString()));
                sVO.setRatingLevel(map.get("ratingLevel") == null ? "" : map.get("ratingLevel").toString());
            }
        }
        return list;
    }

    /**
     * 派遣员岗位评价
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public List<DispatcherEvaluateVO> findDispatcherEvaluateByCondition(LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String, Object>> mapList = positionRepository.findDispatcherEvaluateByCondition(startTime, endTime);
        List<DispatcherEvaluateVO> list = new ArrayList<>();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                DispatcherEvaluateVO sVO = new DispatcherEvaluateVO();
                sVO.setDispatch(map.get("dispatch") == null ? "" : map.get("dispatch").toString());
                sVO.setToDispatch(map.get("toDispatch") == null ? 0 : (Integer.parseInt(map.get("toDispatch").toString())));
                sVO.setIntimeDispatch(map.get("intimeDispatch") == null ? 0 : (Integer.parseInt(map.get("intimeDispatch").toString())));
                sVO.setNeedDispatch(map.get("needDispatch") == null ? 0 : (Integer.parseInt(map.get("needDispatch").toString())));
                sVO.setIntimeDispatchRate(map.get("intimeDispatchRate") == null ? (float) 0.0 : Float.parseFloat(map.get("intimeDispatchRate").toString()));
                sVO.setAccuracyDispatch(map.get("accuracyDispatch") == null ? 0 : (Integer.parseInt(map.get("accuracyDispatch").toString())));
                sVO.setAccuracyDispatchRate(map.get("accuracyDispatchRate") == null ? (float) 0.0 : Float.parseFloat(map.get("accuracyDispatchRate").toString()));
                sVO.setAggregativeIndicator(map.get("aggregativeIndicator") == null ? (float) 0.0 : Float.parseFloat(map.get("aggregativeIndicator").toString()));
                sVO.setRatingLevel(map.get("ratingLevel") == null ? "" : map.get("ratingLevel").toString());
            }
        }
        return list;
    }

}
