package com.unicom.urban.management.service.evaluate;

import com.unicom.urban.management.dao.evaluate.PositionRepository;
import com.unicom.urban.management.pojo.vo.AcceptorEvaluateVO;
import com.unicom.urban.management.pojo.vo.DispatcherEvaluateVO;
import com.unicom.urban.management.pojo.vo.ShiftForemanEvaluateVO;
import com.unicom.urban.management.pojo.vo.SupervisorEvaluateVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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
     * @param time1
     * @param time2
     * @return
     */
    public List<SupervisorEvaluateVO> findSupervisorEvaluateByCondition(String time1, String time2) {
        LocalDateTime startTime = getTimeForBetween(time1, time2).get("startTime");
        LocalDateTime endTime = getTimeForBetween(time1, time2).get("endTime");
        List<Map<String, Object>> mapList = positionRepository.findSupervisorEvaluateByCondition(startTime, endTime);
        List<SupervisorEvaluateVO> list = new ArrayList<>();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                SupervisorEvaluateVO sVO = new SupervisorEvaluateVO();
                sVO.setSupervisorName(map.get("supervisorName").toString());
                sVO.setGridOnwer(map.get("gridOnwer") == null ? "" : (map.get("gridOnwer").toString()));
                sVO.setPatrolReport(map.get("patrolReport") == null ? 0 : (Integer.parseInt(map.get("patrolReport").toString())));
                sVO.setValidPatrolReport(map.get("validPatrolReport") == null ? 0 : (Integer.parseInt(map.get("validPatrolReport").toString())));
                sVO.setReportVaildNumRate(map.get("reportVaildNumRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("reportVaildNumRate").toString())* 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue())) + "%");
                sVO.setIntimeVerify(map.get("intimeVerify") == null ? 0 : (Integer.parseInt(map.get("intimeVerify").toString())));
                sVO.setNeedVerify(map.get("needVerify") == null ? 0 : (Integer.parseInt(map.get("needVerify").toString())));
                sVO.setInTimeVerifyRate(map.get("inTimeVerifyRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("inTimeVerifyRate").toString()) * 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue())) + "%");
                sVO.setInTimeCheck(map.get("inTimeCheck") == null ? 0 : (Integer.parseInt(map.get("inTimeCheck").toString())));
                sVO.setPublicReport(map.get("publicReport") == null ? 0 : (Integer.parseInt(map.get("publicReport").toString())));
                sVO.setInst(map.get("inst") == null ? 0 : Integer.parseInt(map.get("inst").toString()) );
                sVO.setPublicReportRate(map.get("publicReportRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("publicReportRate").toString())* 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue()) ) + "%");
                sVO.setAggregativeIndicator(map.get("aggregativeIndicator") == null ? "0" : (int)((Float.parseFloat(map.get("aggregativeIndicator").toString())) * 100) + "");
                sVO.setRatingLevel(getRatingLevel(Integer.parseInt(sVO.getAggregativeIndicator())));
                list.add(sVO);
            }
        }
        return list;
    }

    /**
     * 受理员岗位评价
     *
     * @param time1
     * @param time2
     * @return
     */
    public List<AcceptorEvaluateVO> findAcceptorEvaluateByCondition(String time1, String time2) {
        LocalDateTime startTime = getTimeForBetween(time1, time2).get("startTime");
        LocalDateTime endTime = getTimeForBetween(time1, time2).get("endTime");
        List<Map<String, Object>> mapList = positionRepository.findAcceptorEvaluateByCondition(startTime, endTime);

        List<AcceptorEvaluateVO> list = new ArrayList<>();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                AcceptorEvaluateVO sVO = new AcceptorEvaluateVO();
                sVO.setOperateHumanNameId(map.get("operateHumanNameId") == null ? "" : map.get("operateHumanNameId").toString());
                sVO.setOperate(map.get("operate") == null ? 0 : (Integer.parseInt(map.get("operate").toString())));
                sVO.setIntimeSendVerify(map.get("intimeSendVerify") == null ? 0 : (Integer.parseInt(map.get("intimeSendVerify").toString())));
                sVO.setNeedSendVerify(map.get("needSendVerify") == null ? 0 : (Integer.parseInt(map.get("needSendVerify").toString())));
                sVO.setSendVerifyRate(map.get("sendVerifyRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("sendVerifyRate").toString()) * 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue())) + "%");
                sVO.setIntimeSendCheck(map.get("intimeSendCheck") == null ? 0 : (Integer.parseInt(map.get("intimeSendCheck").toString())));
                sVO.setNeedSendCheck(map.get("needSendCheck") == null ? 0 : (Integer.parseInt(map.get("needSendCheck").toString())));
                sVO.setNeedSendCheckRate(map.get("needSendCheckRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("needSendCheckRate").toString()) * 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue())) + "%");
                //计算受理数分值
                Integer operate = Integer.parseInt(map.get("operate").toString());
                Integer operateFen = 0;
                if (operate >= 2001) {
                    operateFen = 100;
                } else if (operate >= 1201) {
                    operateFen = 90;
                } else if (operate >= 501) {
                    operateFen = 75;
                } else if (operate >= 301) {
                    operateFen = 60;
                } else if (operate >= 101) {
                    operateFen = 40;
                } else {
                    operateFen = 0;
                }
                //计算综合指标值=受理数分值×25%+核实按时派发率分值×40%+核查按时派发率分值×35%
                double aggregativeIndicator = operateFen * 0.25 + Float.parseFloat(map.get("sendVerifyRate").toString()) * 100 * 0.4 +
                        (Float.parseFloat(map.get("sendVerifyRate").toString())) * 100 * 0.35;
                sVO.setAggregativeIndicator(String.valueOf((int)(aggregativeIndicator)));
                /*计算评价等级*/
                sVO.setRatingLevel(getRatingLevel((int)(aggregativeIndicator)));
                list.add(sVO);
            }
        }
        return list;
    }



    /**
     * 值班长岗位评价
     *
     * @param time1
     * @param time2
     * @return
     */
    public List<ShiftForemanEvaluateVO> findShiftForemanEvaluateByCondition(String time1, String time2) {
        LocalDateTime startTime = getTimeForBetween(time1, time2).get("startTime");
        LocalDateTime endTime = getTimeForBetween(time1, time2).get("endTime");
        List<Map<String, Object>> mapList = positionRepository.findShiftForemanEvaluateByCondition(startTime, endTime);
        List<ShiftForemanEvaluateVO> list = new ArrayList<>();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                ShiftForemanEvaluateVO sVO = new ShiftForemanEvaluateVO();
                sVO.setInstHumanName(map.get("instHumanName") == null ? "" : map.get("instHumanName").toString());
                sVO.setInst(map.get("inst") == null ? 0 : (Integer.parseInt(map.get("inst").toString())));
                sVO.setIntimeInst(map.get("intimeInst") == null ? 0 : (Integer.parseInt(map.get("intimeInst").toString())));
                sVO.setIntimeInstRate(map.get("intimeInstRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("intimeInstRate").toString()) * 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue())) + "%");
                sVO.setExactInst(map.get("exactInst") == null ? 0 : (Integer.parseInt(map.get("exactInst").toString())));
                sVO.setExactInstRate(map.get("exactInstRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("exactInstRate").toString()) * 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue())) + "%");
                sVO.setInTimeClose(map.get("inTimeClose") == null ? 0 : (Integer.parseInt(map.get("inTimeClose").toString())));
                sVO.setClose(map.get("close") == null ? 0 : (Integer.parseInt(map.get("close").toString())));
                sVO.setInTimeCloseRate(map.get("inTimeCloseRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("inTimeCloseRate").toString()) * 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue())) + "%");
                sVO.setAggregativeIndicator(map.get("aggregativeIndicator") == null ? "0" :  String.valueOf((int)((Float.parseFloat(map.get("aggregativeIndicator").toString())))));
                sVO.setRatingLevel(getRatingLevel(Integer.parseInt(sVO.getAggregativeIndicator())));
                list.add(sVO);
            }
        }
        return list;
    }

    /**
     * 派遣员岗位评价
     *
     * @param time1
     * @param time2
     * @return
     */
    public List<DispatcherEvaluateVO> findDispatcherEvaluateByCondition(String time1, String time2) {
        LocalDateTime startTime = getTimeForBetween(time1, time2).get("startTime");
        LocalDateTime endTime = getTimeForBetween(time1, time2).get("endTime");
        List<Map<String, Object>> mapList = positionRepository.findDispatcherEvaluateByCondition(startTime, endTime);
        List<DispatcherEvaluateVO> list = new ArrayList<>();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                DispatcherEvaluateVO sVO = new DispatcherEvaluateVO();
                sVO.setDispatch(map.get("dispatch") == null ? "" : map.get("dispatch").toString());
                sVO.setToDispatch(map.get("toDispatch") == null ? 0 : (Integer.parseInt(map.get("toDispatch").toString())));
                sVO.setIntimeDispatch(map.get("intimeDispatch") == null ? 0 : (Integer.parseInt(map.get("intimeDispatch").toString())));
                sVO.setNeedDispatch(map.get("needDispatch") == null ? 0 : (Integer.parseInt(map.get("needDispatch").toString())));
                sVO.setIntimeDispatchRate(map.get("intimeDispatchRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("intimeDispatchRate").toString()) * 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue())) + "%");
                sVO.setAccuracyDispatch(map.get("accuracyDispatch") == null ? 0 : (Integer.parseInt(map.get("accuracyDispatch").toString())));
                sVO.setAccuracyDispatchRate(map.get("accuracyDispatchRate") == null ? "0.0%" : ((new BigDecimal(Float.parseFloat(map.get("accuracyDispatchRate").toString()) * 100).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue())) + "%");
                sVO.setAggregativeIndicator(map.get("aggregativeIndicator") == null ? "0" :  String.valueOf((int)((Float.parseFloat(map.get("aggregativeIndicator").toString())))));
                sVO.setRatingLevel(getRatingLevel(Integer.parseInt(sVO.getAggregativeIndicator())));
                list.add(sVO);
            }
        }
        return list;
    }

    private Map<String, LocalDateTime> getTimeForBetween(String startTime, String endTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, LocalDateTime> map = new HashMap<>(2);
        if (StringUtils.isNotBlank(startTime)) {
            map.put("startTime", LocalDateTime.parse(startTime, df));
        } else {
            map.put("startTime", LocalDateTime.parse("1970-01-01 00:00:00", df));
        }
        if (StringUtils.isNotBlank(endTime)) {
            map.put("endTime", LocalDateTime.parse(endTime, df));
        } else {
            map.put("endTime", LocalDateTime.now());
        }
        return map;
    }
    /**
     * 计算评价等级
     * @return
     */
    private String getRatingLevel(Integer aggregativeIndicator){
        if (aggregativeIndicator >= 90) {
            return "A";
        } else if (aggregativeIndicator >= 75) {
            return "B";
        } else if (aggregativeIndicator >= 60) {
            return "C";
        } else if (aggregativeIndicator >= 40) {
            return "D";
        } else {
            return "E";
        }
    }
}