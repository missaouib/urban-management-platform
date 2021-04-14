package com.unicom.urban.management.service.event;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.FastdfsToken;
import com.unicom.urban.management.dao.event.EventRepository;
import com.unicom.urban.management.dao.eventcondition.EventConditionRepository;
import com.unicom.urban.management.dao.statistics.StatisticsRepository;
import com.unicom.urban.management.mapper.EventButtonMapper;
import com.unicom.urban.management.mapper.EventConditionMapper;
import com.unicom.urban.management.mapper.EventMapper;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.Process;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.*;
import com.unicom.urban.management.service.activiti.ActivitiService;
import com.unicom.urban.management.service.depttimelimit.DeptTimeLimitService;
import com.unicom.urban.management.service.eventcondition.EventConditionService;
import com.unicom.urban.management.service.eventfile.EventFileService;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.process.ProcessService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import com.unicom.urban.management.util.SecurityUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventConditionRepository eventConditionRepository;
    @Autowired
    private EventConditionService eventConditionService;
    @Autowired
    private WorkService workService;
    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private DeptTimeLimitService deptTimeLimitService;
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private PetitionerService petitionerService;
    @Autowired
    private GridService gridService;
    @Autowired
    private EventFileService eventFileService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private KVService kvService;
    @Autowired
    private TaskProcessingService taskProcessingService;


    public Page<EventVO> search(EventDTO eventDTO, Pageable pageable) {
        Specification<Event> specification = (root, query, criteriaBuilder) -> {


            if (!query.getResultType().equals(Long.class)) {
                root.fetch("eventType", JoinType.LEFT);
            }
            List<Predicate> list = new ArrayList<>();


            if(StringUtils.isNotBlank(eventDTO.getQuerySts())){
                List<String> eventIds = statisticsService.findEventIds();
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                eventIds.forEach(in::value);
                list.add(in);
            }
            if (eventDTO.getClose() != null) {
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("eventSate").get("id"));
                eventDTO.getClose().forEach(in::value);
                list.add(in);
            }

            if (StringUtils.isNotBlank(eventDTO.getEventId())) {
                list.add(criteriaBuilder.equal(root.get("id").as(String.class), eventDTO.getEventId()));
            }
            if (StringUtils.isNotBlank(eventDTO.getUserName())) {
                list.add(criteriaBuilder.equal(root.get("user").get("username").as(String.class), eventDTO.getUserName()));
            }

            if (eventDTO.getSts() != null) {
                list.add(criteriaBuilder.equal(root.get("sts").as(Integer.class), eventDTO.getSts()));
            }

            if (StringUtils.isNotBlank(eventDTO.getRepresent())) {
                list.add(criteriaBuilder.like(root.get("represent").as(String.class), "%" + eventDTO.getRepresent() + "%"));
            }

            if (StringUtils.isNotBlank(eventDTO.getGrid())) {
                list.add(criteriaBuilder.equal(root.get("grid").get("id").as(String.class), eventDTO.getGrid()));
            }

            if (StringUtils.isNotBlank(eventDTO.getEventTypeId())) {
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("eventType").get("id"));
                List<String> type = eventTypeService.getComponentTypeIds(eventDTO.getEventTypeId());
                type.forEach(in::value);
                list.add(in);
            }

            if (StringUtils.isNotBlank(eventDTO.getConditionId())) {
                list.add(criteriaBuilder.equal(root.get("condition").get("id").as(String.class), eventDTO.getConditionId()));
            }

            if (StringUtils.isNotBlank(eventDTO.getEventSourceId())) {
                list.add(criteriaBuilder.equal(root.get("eventSource").get("id").as(String.class), eventDTO.getEventSourceId()));
            }

            if (StringUtils.isNotBlank(eventDTO.getEventCondition())) {
                list.add(criteriaBuilder.equal(root.get("condition").get("parent").get("id").as(String.class), eventDTO.getEventCondition()));
            }

            if (StringUtils.isNotBlank(eventDTO.getTimeType())) {
                list.add(criteriaBuilder.equal(root.get("timeLimit").get("id").as(String.class), eventDTO.getTimeType()));
            }

            if (eventDTO.getTaskName() != null && eventDTO.getTaskName().size() != 0) {
                /* 查询当前登陆人所拥有的任务 */
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                List<String> type;
                if (StringUtils.isBlank(eventDTO.getSupervision())) {
                    type = workService.queryTaskByAssigneeAndTaskName(SecurityUtil.getUserId(), eventDTO.getTaskName());
                } else {
                    type = workService.queryTaskByTaskName(eventDTO.getTaskName().get(0));
                }

                if (type.size() == 0) {
                    type = Collections.singletonList("");
                }
                type.forEach(in::value);
                list.add(in);
            }

            List<String> eventIdListByBox = eventDTO.getEventIdListByBox();
            if (eventIdListByBox != null && eventIdListByBox.size() > 0) {
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                eventIdListByBox.forEach(in::value);
                list.add(in);
            }

            if (StringUtils.isNotBlank(eventDTO.getUserId())) {
                /* 查询当前登陆人所拥有的任务 */
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                List<String> type = activitiService.queryTask(eventDTO.getUserId());
                if (type.size() == 0) {
                    type = Collections.singletonList("");
                }
                type.forEach(in::value);
                list.add(in);
            }

            if (StringUtils.isNotBlank(eventDTO.getMe())) {
                List<String> eventIdByMe = statisticsService.getEventIdByMe();
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                if (eventIdByMe.size() == 0) {
                    eventIdByMe = Collections.singletonList("");
                }
                eventIdByMe.forEach(in::value);
                list.add(in);
            }

            if (StringUtils.isNotBlank(eventDTO.getHang())) {
                List<String> eventIdByMe = statisticsService.getEventIdByHang();
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                if (eventIdByMe.size() == 0) {
                    eventIdByMe = Collections.singletonList("");
                }
                eventIdByMe.forEach(in::value);
                list.add(in);
            }

            if (StringUtils.isNotBlank(eventDTO.getCancel())) {
                List<String> eventIdByMe = statisticsService.getEventIdByCancel();
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                if (eventIdByMe.size() == 0) {
                    eventIdByMe = Collections.singletonList("");
                }
                eventIdByMe.forEach(in::value);
                list.add(in);
            }
            /*无效案件*/
            if (eventDTO.getNotOperate() != null && eventDTO.getNotOperate() == 1) {
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                List<String> eventId = statisticsService.findEventIdByNotOperate(eventDTO.getNotOperate());
                if (eventId.size() == 0) {
                    eventId = Collections.singletonList("");
                }
                eventId.forEach(in::value);
                list.add(in);

            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        Page<Event> page = eventRepository.findAll(specification, pageable);
        List<EventVO> eventVOList = new ArrayList<>();
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Coordinate> coordinateList = new ArrayList<>();
        List<Process> processes = processService.findAll();
        /*循环添加vo*/
        page.getContent().forEach(e -> {
            EventVO eventVO = EventMapper.INSTANCE.eventToEventVO(e);
            if (eventVO.getX() > 0 && eventVO.getY() > 0) {
                coordinateList.add(new Coordinate(eventVO.getX(), eventVO.getY()));
            }
            if (e.getSupervises().size() > 0) {
                /*督办*/
                eventVO.setSupSts("1");
            } else {
                /*未督办*/
                eventVO.setSupSts("0");
            }
            eventVO.setEventTypeName(e.getEventType().getParent().getParent().getName() + "-" + e.getEventType().getParent().getName() + "-" + e.getEventType().getName());
            /*查询事件步骤endTime为null的  如果有取出这条 附加到vo信息*/
            List<Statistics> collect1 = e.getStatisticsList().stream().filter(s -> s.getEndTime() == null).collect(Collectors.toList());
            if (collect1.size() > 0) {
                Statistics statistics = collect1.get(0);
                eventVO.setTaskName(statistics.getTaskName());
                String format = simpleDateFormat.format(statistics.getStartTime());
                eventVO.setStartTime(format);
                int hangDurationHours = 0;
                int delayedHours = 0;
                if ("专业部门".equals(statistics.getTaskName())) {
                    int timeLimit = statistics.getDeptTimeLimit().getTimeLimit();
                    eventVO.setTimeLimit(timeLimit);
                    String timeType = statistics.getDeptTimeLimit().getTimeType().getValue();
                    eventVO.setTimeType(timeType);
                    if (statistics.getHangDuration() != null) {
                        hangDurationHours += statistics.getHangDuration();
                    }
                    if (statistics.getDelayedHours() != null) {
                        delayedHours += statistics.getDelayedHours();
                    }
                } else {
                    if (Optional.ofNullable(statistics.getProcessTimeLimit()).isPresent()) {
                        int timeLimit = statistics.getProcessTimeLimit().getTimeLimit();
                        eventVO.setTimeLimit(timeLimit);
                        String timeType = statistics.getProcessTimeLimit().getTimeType().getValue();
                        eventVO.setTimeType(timeType);
                    }
                }
                LocalDateTime startTime = statistics.getStartTime();
                Integer timeLimit = eventVO.getTimeLimit();
                DecimalFormat df = new DecimalFormat("#.00");
                String timeType = StringUtils.isNotBlank(eventVO.getTimeType()) ? eventVO.getTimeType() : "";
                String time = "";
                switch (timeType) {
                    case "工作日":
                    case "天":
                        if (hangDurationHours == 0 && delayedHours == 0) {
                            time = String.valueOf(timeLimit);
                        } else {
                            time = df.format(((double) ((timeLimit * 24) + hangDurationHours + delayedHours)) / 24);
                        }
                        eventVO.setTimeLimitStr(time + timeType);

                        eventVO.setEndTimeStr(simpleDateFormat.format(startTime.plusDays(timeLimit).plusHours(hangDurationHours).plusHours(delayedHours)));
                        break;
                    case "工作时":
                    case "小时":
                        time = String.valueOf((timeLimit + hangDurationHours + delayedHours));
                        eventVO.setTimeLimitStr(timeLimit + timeType);
                        eventVO.setEndTimeStr(simpleDateFormat.format(startTime.plusHours(timeLimit).plusHours(hangDurationHours).plusHours(delayedHours)));
                        break;
                    case "分钟":
                        time = String.valueOf((timeLimit + (hangDurationHours * 60) + (delayedHours * 60)));

                        eventVO.setEndTimeStr(simpleDateFormat.format(startTime.plusMinutes(timeLimit).plusHours(hangDurationHours).plusHours(delayedHours)));
                        break;
                    default:
                        eventVO.setEndTimeStr("暂无");
                        break;
                }
                eventVO.setTimeLimitStr(time + timeType);
                eventVO.setDeptName(Optional.ofNullable(statistics.getDisposeUnit()).map(Dept::getDeptName).orElse(""));
                eventVO.setTimeLimitLong(taskProcessingService.betWeenTime(startTime,LocalDateTime.now(),timeType,timeLimit,0)[1]);
            } else {
                /*如果事件步骤没有endTime为null的  证明事件已经完成 获取结束时间最近的那条步骤 附加到vo信息*/
                List<Statistics> collect = e.getStatisticsList().stream()
                        .sorted(Comparator.comparing(Statistics::getEndTime, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
                if (collect.size() > 0) {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    eventVO.setCloseTime(df.format(collect.get(0).getEndTime()));
                    eventVO.setSts(collect.get(0).getSts());

                    eventVO.setTaskName(collect.get(0).getTaskName());
                    String format = simpleDateFormat.format(collect.get(0).getStartTime());
                    eventVO.setStartTime(format);
                }
            }
            if (eventVO.getTaskName() != null) {
                List<String> urls = processes.stream().filter(p -> eventVO.getTaskName().equals(p.getNodeName())).map(Process::getUrl).collect(Collectors.toList());
                if (urls.size() > 0) {
                    eventVO.setUrl(urls.get(0));
                }
            }
            List<Statistics> notOperateCollect = e.getStatisticsList();
            for (Statistics s : notOperateCollect) {
                if (s.getNotOperate() != null && s.getNotOperate() == 1) {
                    eventVO.setStatisticsId(s.getId());
                }
            }

            eventVOList.add(eventVO);
        });
        if (coordinateList.size() > 0) {
            String center = getCenterOfMultiPoint(coordinateList);
            for (EventVO eventVO : eventVOList) {
                eventVO.setCenterPoint(center);
            }
        }

        return new PageImpl<>(eventVOList, page.getPageable(), page.getTotalElements());
    }

    private String getCenterOfMultiPoint(List<Coordinate> coordinateList) {
        Coordinate[] coordinates = new Coordinate[coordinateList.size()];
        coordinateList.toArray(coordinates);
        GeometryFactory geometryFactory = new GeometryFactory();
        /* 创建多点 */
        MultiPoint morePoint = geometryFactory.createMultiPointFromCoords(coordinates);
        /* 得到多边形内心 */
        double x = morePoint.getCentroid().getCoordinates()[0].getX();
        double y = morePoint.getCentroid().getCoordinates()[0].getY();
        return x + "-" + y;
    }


    public List<EventConditionVO> findEventConditionByEventType(String eventTypeId) {
        List<EventCondition> list = eventConditionRepository.findAllByEventTypeId_IdAndTypeAndParentIsNull(eventTypeId, 1);
        if (list != null) {
            return EventConditionMapper.INSTANCE.eventConditionListToEventConditionVOList(list);
        }
        return null;
    }

    public List<EventConditionVO> findConditionValueByRegion(String region) {
        List<EventCondition> list = eventConditionRepository.findAllByParent_Id(region);
        if (list != null) {
            return EventConditionMapper.INSTANCE.eventConditionListToEventConditionVOList(list);
        }
        return null;
    }

    public List<DeptTimeLimitVO> findDeptTimeLimitByCondition(String conditionId) {
        return deptTimeLimitService.findDeptTimeLimitByCondition(conditionId);
    }

    public DeptTimeLimitVO findDeptTimeLimit(String deptTimeLimitId) {
        return deptTimeLimitService.findDeptTimeLimit(deptTimeLimitId);
    }

    /**
     * 获取按钮
     *
     * @param eventId 事件id
     * @return 按钮
     */
    public List<EventButtonVO> getButton(String eventId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        if (statistics != null) {
            List<EventButton> eventButtons = activitiService.queryButton(statistics.getTaskId());
            return EventButtonMapper.INSTANCE.eventButtonListToEventButtonVOList(eventButtons);
        } else {
            return null;
        }
    }

    /**
     * 案件采集保存
     *
     * @param eventDTO 事件参数
     */
    public void saveTemp(EventDTO eventDTO) {
        if (existsByEventCode(eventDTO.getEventCode())) {
            throw new DataValidException("案件号已存在，将为你修改新的编号");
        }
        eventDTO.setSts(0);
        Event event = EventMapper.INSTANCE.eventDTOToEvent(eventDTO);
        event.setUser(SecurityUtil.getUser().castToUser());
        if (StringUtils.isNotBlank(eventDTO.getObjId())) {
            Component component = new Component();
            component.setId(eventDTO.getObjId());
            event.setComponent(component);
        }
        eventRepository.save(event);
    }

    /**
     * 保存事件
     *
     * @param eventDTO 事件参数
     */
    public void save(EventDTO eventDTO) {
        if (existsByEventCode(eventDTO.getEventCode())) {
            throw new DataValidException("案件号已存在，将为你修改新的编号");
        }
        Event event = EventMapper.INSTANCE.eventDTOToEvent(eventDTO);
        if (StringUtils.isNotBlank(eventDTO.getObjId())) {
            Component component = new Component();
            component.setId(eventDTO.getObjId());
            event.setComponent(component);
        }
        event.setUser(SecurityUtil.getUser().castToUser());

        Petitioner petitioner = new Petitioner();
        petitioner.setName(eventDTO.getPeopleName());
        petitioner.setSex(eventDTO.getSex());
        petitioner.setPhone(eventDTO.getPetitionerPhone());
        Petitioner saveP = petitionerService.save(petitioner);
        event.setPetitioner(saveP);

        List<EventFile> eventFileList = new ArrayList<>();
        List<EventFile> eventFileListImage = eventFileService.joinEventFileListToObjet(eventDTO.getImageUrlList(), EventFile.FileType.IMAGE);
        List<EventFile> eventFileListVideo = eventFileService.joinEventFileListToObjet(eventDTO.getVideoUrlList(), EventFile.FileType.VIDEO);
        List<EventFile> eventFileListMusic = eventFileService.joinEventFileListToObjet(eventDTO.getMusicUrlList(), EventFile.FileType.AUDIO);
        eventFileList.addAll(eventFileListImage);
        eventFileList.addAll(eventFileListVideo);
        eventFileList.addAll(eventFileListMusic);
        event.setEventFileList(eventFileList);

        Event save = eventRepository.save(event);
        /* 受理员保存 */
        if (eventDTO.getInitSts() != null && eventDTO.getInitSts() == 2) {
            workService.caseAcceptanceByDispatch(save.getId());
        }
        /* 受理员核实 */
        if (eventDTO.getInitSts() != null && eventDTO.getInitSts() == 3) {
            eventDTO.setId(save.getId());
            eventDTO.setEventFileList(null);
            workService.caseAcceptanceByReceive(eventDTO);
        }
    }

    /**
     * 案件登记-修改
     *
     * @param eventDTO 数据
     */
    public void registerUpdate(EventDTO eventDTO) {
        Event one = findOne(eventDTO.getId());
        one.setRepresent(eventDTO.getRepresent());
        one.setLocation(eventDTO.getLocation());
        one.setX(eventDTO.getX());
        one.setY(eventDTO.getY());
        Grid grid = gridService.findOne(eventDTO.getGridId());
        one.setGrid(grid);
        EventType eventType = eventTypeService.findOne(eventDTO.getEventTypeId());
        one.setEventType(eventType);
        DeptTimeLimit deptTimeLimit = deptTimeLimitService.findOne(eventDTO.getTimeLimitId());
        one.setTimeLimit(deptTimeLimit);
        EventCondition eventCondition = eventConditionService.findOne(eventDTO.getConditionId());
        one.setCondition(eventCondition);
        KV oneById = kvService.findOneById(eventDTO.getRecTypeId());
        one.setRecType(oneById);
        one.setComponentObjId(eventDTO.getComponentObjId());
        List<EventFile> eventFileList = new ArrayList<>();
        List<EventFile> eventFileListImage = eventFileService.joinEventFileListToObjet(eventDTO.getImageUrlList(), EventFile.FileType.IMAGE);
        List<EventFile> eventFileListVideo = eventFileService.joinEventFileListToObjet(eventDTO.getVideoUrlList(), EventFile.FileType.VIDEO);
        List<EventFile> eventFileListMusic = eventFileService.joinEventFileListToObjet(eventDTO.getMusicUrlList(), EventFile.FileType.AUDIO);
        eventFileList.addAll(eventFileListImage);
        eventFileList.addAll(eventFileListVideo);
        eventFileList.addAll(eventFileListMusic);
        eventFileList.addAll(one.getEventFileList());
        one.setEventFileList(eventFileList);
        eventRepository.saveAndFlush(one);
    }

    private boolean existsByEventCode(String eventCode) {
        return eventRepository.existsByEventCode(eventCode);
    }

    /**
     * 受理员完成任务 并且 激活监督员(领取任务)核实
     *
     * @param eventDTO 数据
     */
    public void completeByReceptionist(EventDTO eventDTO) {
        workService.completeByReceptionist(eventDTO);
    }

    /**
     * 监督员信息核实
     *
     * @param eventId 事件id
     * @param button  按钮
     */
    public void completeByVerification(String eventId, String button) {
        workService.completeByVerificationist(eventId, button);
    }

    /**
     * 监督员案件核查
     *
     * @param eventId 事件id
     * @param button  按钮
     */
    public void completeByInspect(String eventId, String button) {
        workService.completeByInspect(eventId, button);
    }

    /**
     * 受理员 核实反馈
     * 受理开启值班长流程
     *
     * @param eventDTO 事件
     */
    public void completeByReceptionistForDo(EventDTO eventDTO) {
        workService.completeByReceptionistForDo(eventDTO);
    }

    /**
     * 受理员 核实反馈
     * 不予受理直接结束
     *
     * @param eventDTO 数据
     */
    public void completeByReceptionistForNotDo(EventDTO eventDTO) {
        workService.completeByReceptionistForNotDo(eventDTO);
    }

    /**
     * 受理员完成任务 并且 激活监督员(领取任务)核查
     * 需要先领取任务
     *
     * @param eventDTO 事件
     */
    public void completeByReceptionistWithClaim(EventDTO eventDTO) {
        /*workService.claimByReceptionist(eventDTO.getId());*/
        workService.completeByReceptionist(eventDTO);
    }

    public String createCode(String eventTypeId) {
        String eventCode;
        //查询当天最大序号
        Integer maxNum = eventRepository.findMaxNum();
        EventType eventType = eventTypeService.getEventType(eventTypeId);
        String level = eventType.getLevel();
        String code = eventType.getCode();
        int type = eventType.getType();
        String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        if (level.length() == 1) {
            level = 0 + level;
        }
        if (code.length() == 1) {
            code = 0 + code;
        }
        //部件（简称C）或事件（E）+大类代码+小类代码+××××××××××（年：4位，月：2位，日：2位，序号：2位）即C01012019041101
        String maxNumStr;
        if (maxNum == null) {
            maxNum = 0;
        }
        maxNum++;
        if (maxNum < 10) {
            maxNumStr = "000" + maxNum;
        } else if (maxNum < 100) {
            maxNumStr = "00" + maxNum;
        } else if (maxNum < 1000) {
            maxNumStr = "0" + maxNum;
        } else {
            maxNumStr = String.valueOf(maxNum);
        }
        if (type == 1) {
            eventCode = "C" + level + code + now + maxNumStr;
        } else {
            eventCode = "E" + level + code + now + maxNumStr;
        }
        return eventCode;
    }

    /**
     * findOne
     *
     * @param eventId 事件id
     * @return 事件
     */
    public Event findOne(String eventId) {
        return eventRepository.findById(eventId).orElse(new Event());
    }

    /**
     * findOne
     *
     * @param eventId 事件id
     * @return 事件
     */
    public EventOneVO findOneToVo(String eventId) {
        int ts = (int) (System.currentTimeMillis() / 1000);
        Event one = eventRepository.findById(eventId).orElse(new Event());
        EventOneVO eventOneVO = EventMapper.INSTANCE.eventToEventOneVO(one);
        eventOneVO.setId(eventId);
        eventOneVO.setEventTypeId(one.getEventType().getId());
        eventOneVO.setEventTypeStr(one.getEventType().getParent().getParent().getName() + "-" + one.getEventType().getParent().getName() + "-" + one.getEventType().getName());
        eventOneVO.setTimeLimitId(one.getTimeLimit().getId());
        eventOneVO.setTimeLimitStr(one.getTimeLimit().getTimeLimit() + one.getTimeLimit().getTimeType().getValue());
        eventOneVO.setCommunity(one.getGrid().getParent().getGridName());
        eventOneVO.setCommunityId(one.getGrid().getParent().getId());
        eventOneVO.setStreet(one.getGrid().getParent().getParent().getGridName());
        eventOneVO.setStreetId(one.getGrid().getParent().getParent().getId());
        eventOneVO.setEventRegion(one.getGrid().getParent().getParent().getParent().getGridName());
        eventOneVO.setEventRegionId(one.getGrid().getParent().getParent().getParent().getId());
        eventOneVO.setRegionIdStr(one.getCondition().getParent().getId());
        eventOneVO.setRegionStr(one.getCondition().getParent().getRegion());
        eventOneVO.setConditionId(one.getCondition().getId());
        eventOneVO.setConditionValue(one.getCondition().getConditionValue());
        eventOneVO.setGridId(one.getGrid().getId());
        eventOneVO.setGirdStr(one.getGrid().getGridName());
        eventOneVO.setLevel(one.getTimeLimit().getLevel().getValue());
        eventOneVO.setEventButtonVOS(this.getButton(eventId));
        eventOneVO.setRecTypeId(one.getRecType().getId());
        eventOneVO.setRecTypeStr(one.getRecType().getValue());
        eventOneVO.setUserId(Optional.ofNullable(one.getUser()).map(User::getId).orElse(""));
        eventOneVO.setUserName(Optional.ofNullable(one.getUser()).map(User::getName).orElse(""));
//        if (one.getComponent() != null) {
//            if (one.getComponent().getComponentInfo() != null) {
//                eventOneVO.setObjId(one.getComponent().getComponentInfo().getObjId());
//            }
//        }
        eventOneVO.setObjId(one.getComponentObjId());
        Optional<Petitioner> petitioner = Optional.ofNullable(one.getPetitioner());
        eventOneVO.setPetitionerName(petitioner.map(Petitioner::getName).orElse(""));
        eventOneVO.setPetitionerPhone(petitioner.map(Petitioner::getPhone).orElse(""));
        eventOneVO.setPetitionerSex(petitioner.map(Petitioner::getSex).orElse(""));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        eventOneVO.setCDate(df.format(one.getCreateTime()));
        eventOneVO.setEventSourceId(one.getEventSource().getId());
        eventOneVO.setEventSourceStr(one.getEventSource().getValue());
        eventOneVO.setDoBySelf(one.getDoBySelf());
        List<Map<String, Object>> fileList = new ArrayList<>();
        one.getEventFileList().forEach(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("url", f.getFilePath() + "?token=" + FastdfsToken.getToken(f.getFilePath().substring(f.getFilePath().indexOf("/")+1), ts) + "&ts=" + ts);
            map.put("type", f.getFileType().getValue());
            map.put("taskName", "上报");
            map.put("management", f.getManagement());
            fileList.add(map);
        });
        /*处置前*/
        one.getStatisticsList().stream().filter(s -> taskName(s.getTaskName())).sorted(Comparator.comparing(Statistics::getStartTime)).forEach(s -> s.getEventFileList().forEach(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("url", f.getFilePath() + "?token=" + FastdfsToken.getToken(f.getFilePath().substring(f.getFilePath().indexOf("/")+1), ts) + "&ts=" + ts);
            map.put("type", f.getFileType().getValue());
            map.put("taskName", s.getTaskName());
            map.put("management", 1);
            fileList.add(map);
        }));
        /*处置后*/
        one.getStatisticsList().stream().filter(s -> !taskName(s.getTaskName())).sorted(Comparator.comparing(Statistics::getStartTime)).forEach(s -> s.getEventFileList().forEach(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("url", f.getFilePath() + "?token=" + FastdfsToken.getToken(f.getFilePath().substring(f.getFilePath().indexOf("/")+1), ts) + "&ts=" + ts);
            map.put("type", f.getFileType().getValue());
            map.put("taskName", s.getTaskName());
            map.put("management", 0);
            fileList.add(map);
        }));

        if (one.getSupervises().size() > 0) {
            /*督办*/
            eventOneVO.setSupSts("1");
        } else {
            /*未督办*/
            eventOneVO.setSupSts("0");
        }

        eventOneVO.setFile(fileList);
        Optional<Dept> dept = Optional.ofNullable(one.getEventType().getDept());
        eventOneVO.setDeptId(dept.map(Dept::getId).orElse(""));
        eventOneVO.setDeptName(dept.map(Dept::getDeptName).orElse(""));

        List<Statistics> statisticsList = statisticsService.findAllByEventIdOrderBySort(eventId);
        if (statisticsList.size() > 1) {
            Statistics statistics = statisticsList.get(1);
            if ("派遣员-延时审批".equals(statisticsList.get(0).getTaskName())) {
                eventOneVO.setDelayedHours(statistics.getDelayedHours());
            }
            if ("值班长-作废审批".equals(statisticsList.get(0).getTaskName())) {
                eventOneVO.setTaskDeptName(Optional.ofNullable(statisticsList.get(2).getDisposeUnitName()).map(Dept::getDeptName).orElse(""));
            }
            eventOneVO.setTaskDeptName(Optional.ofNullable(statistics.getDisposeUnitName()).map(Dept::getDeptName).orElse(""));
            eventOneVO.setOpinions(statistics.getOpinions());
        }


        return eventOneVO;
    }


    private boolean taskName(String taskName) {
        List<String> taskNames = Arrays.asList("受理员-信息收集", "值班长-立案", "派遣员-派遣", "值班长-作废审批", "受理员-案件登记", "监督员-信息核实");
        return taskNames.stream().anyMatch(t -> t.equals(taskName));
    }

    /**
     * 受理员上报
     *
     * @param event 需要操作的实体
     */
    public Event update(Event event) {
        return eventRepository.saveAndFlush(event);
    }

    /**
     * 案件采集修改
     *
     * @param eventDTO 需要操作的实体
     */
    public void updateTemp(EventDTO eventDTO) {
        eventDTO.setSts(0);
        Event event = EventDTOToEvent(eventDTO);
        if (StringUtils.isNotBlank(eventDTO.getObjId())) {
            Component component = new Component();
            component.setId(eventDTO.getObjId());
            event.setComponent(component);
        }
        this.update(event);
    }

    public Event EventDTOToEvent(EventDTO eventDTO) {
        Event event = eventRepository.findById(eventDTO.getId()).orElse(new Event());
        if (StringUtils.isNotBlank(eventDTO.getEventCode())) {
            event.setEventCode(eventDTO.getEventCode());
        }
        if (StringUtils.isNotBlank(eventDTO.getEventTypeId())) {
            EventType eventType = new EventType();
            eventType.setId(eventDTO.getEventTypeId());
            event.setEventType(eventType);
        }
        if (StringUtils.isNotBlank(eventDTO.getConditionId())) {
            EventCondition condition = new EventCondition();
            condition.setId(eventDTO.getConditionId());
            event.setCondition(condition);
        }
        if (StringUtils.isNotBlank(eventDTO.getTimeLimitId())) {
            DeptTimeLimit deptTimeLimit = new DeptTimeLimit();
            deptTimeLimit.setId(eventDTO.getTimeLimitId());
            event.setTimeLimit(deptTimeLimit);
        }
        if (StringUtils.isNotBlank(eventDTO.getGridId())) {
            Grid grid = new Grid();
            grid.setId(eventDTO.getGridId());
            event.setGrid(grid);
        }
        if (StringUtils.isNotBlank(eventDTO.getLocation())) {
            event.setLocation(eventDTO.getLocation());
        }
        if (StringUtils.isNotBlank(eventDTO.getUserId())) {
            User user = new User();
            user.setId(eventDTO.getUserId());
            event.setUser(user);
        }
        if (StringUtils.isNotBlank(eventDTO.getEventSourceId())) {
            KV eventSource = new KV();
            eventSource.setId(eventDTO.getEventSourceId());
            event.setEventSource(eventSource);
        }
        if (eventDTO.getX() != null) {
            event.setX(eventDTO.getX());
        }
        if (eventDTO.getY() != null) {
            event.setY(eventDTO.getY());
        }
        if (StringUtils.isNotBlank(eventDTO.getRecTypeId())) {
            KV recType = new KV();
            recType.setId(eventDTO.getRecTypeId());
            event.setRecType(recType);
        }
        if (eventDTO.getDoBySelf() == null) {
            event.setDoBySelf(null);
        } else {
            event.setDoBySelf(eventDTO.getDoBySelf());
        }
        if (eventDTO.getEventFileList() != null) {
            event.setEventFileList(eventDTO.getEventFileList());
        }
        if (StringUtils.isNotBlank(eventDTO.getRepresent())) {
            event.setRepresent(eventDTO.getRepresent());
        }
        if (StringUtils.isNotBlank(eventDTO.getLocation())) {
            event.setLocation(eventDTO.getLocation());
        }
        if (eventDTO.getSts() == null) {
            event.setSts(null);
        } else {
            event.setSts(eventDTO.getSts());
        }
        return event;
    }

    /**
     * 案件采集删除
     *
     * @param ids 主键数组
     */
    public void remove(String ids) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            Event event = this.findOne(id);
            event.setPetitioner(null);
            this.update(event);
            eventRepository.deleteById(id);
        }
    }

    /**
     * 案件采集自处理
     *
     * @param eventDTO 数据
     */
    public void saveAutoReport(EventDTO eventDTO) {
        Event event = this.EventDTOToEvent(eventDTO);
        event.setUser(SecurityUtil.getUser().castToUser());
        if (StringUtils.isNotBlank(eventDTO.getObjId())) {
            Component component = new Component();
            component.setId(eventDTO.getObjId());
            event.setComponent(component);
        }
        Event saved = eventRepository.save(event);
        workService.saveAutoReport(saved.getId());
    }

    /**
     * 案件采集监督员上报
     */
    public void saveReport(EventDTO eventDTO) {
        eventDTO.setSts(null);
        Event event = this.EventDTOToEvent(eventDTO);
        event.setUser(SecurityUtil.getUser().castToUser());
        if (StringUtils.isNotBlank(eventDTO.getObjId())) {
            Component component = new Component();
            component.setId(eventDTO.getObjId());
            event.setComponent(component);
        }
        Event saved = eventRepository.save(event);
        workService.superviseReporting(saved.getId());
    }

    /**
     * 案件采集页表页上报
     */
    public void reportOnList(String ids) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            Event event = this.findOne(id);
            event.setUser(SecurityUtil.getUser().castToUser());
            event.setSts(null);
            Event saved = eventRepository.saveAndFlush(event);
            if (event.getDoBySelf() != null && event.getDoBySelf()) {
                workService.saveAutoReport(saved.getId());
            } else {
                workService.superviseReporting(saved.getId());
            }
        }
    }

    /**
     * 无效案件
     *
     * @param eventDTO 数据
     */
    public void completeForInvalidCases(EventDTO eventDTO) {
        workService.completeForInvalidCases(eventDTO);
    }

    /**
     * 结案存档
     *
     * @param eventDTO 数据
     */
    public void completeForClosingAndFiling(EventDTO eventDTO) {
        workService.completeForClosingAndFiling(eventDTO);
    }

    /**
     * 附件上传
     *
     * @param eventDTO 数据
     */
    public void uploadFiles(EventDTO eventDTO) {
        List<EventFile> eventFileList = new ArrayList<>();
        if (eventDTO.getId() != null) {/*修改*/

            //处置前的图片上传
            if (CollectionUtils.isNotEmpty(eventDTO.getImageUrlList())) {
                List<EventFile> fileList = eventFileService.joinEventFileListToObjet(eventDTO.getImageUrlList(), EventFile.FileType.IMAGE);
                eventFileList.addAll(fileList);
            }
            //处置前的视频上传
            if (CollectionUtils.isNotEmpty(eventDTO.getVideoUrlList())) {
                List<EventFile> fileList = eventFileService.joinEventFileListToObjet(eventDTO.getVideoUrlList(), EventFile.FileType.VIDEO);
                eventFileList.addAll(fileList);
            }
            //处置前的视频上传
            if (CollectionUtils.isNotEmpty(eventDTO.getMusicUrlList())) {
                List<EventFile> fileList = eventFileService.joinEventFileListToObjet(eventDTO.getMusicUrlList(), EventFile.FileType.AUDIO);
                eventFileList.addAll(fileList);
            }
            //处置后的图片上传
            if (CollectionUtils.isNotEmpty(eventDTO.getImageUrlListAfter())) {
                List<EventFile> eventFileListAfter = eventFileService.joinEventFileListToObjetAfter(eventDTO.getImageUrlListAfter(), EventFile.FileType.IMAGE);
                eventFileList.addAll(eventFileListAfter);
            }//处置后的视频上传
            if (CollectionUtils.isNotEmpty(eventDTO.getVideoUrlListAfter())) {
                List<EventFile> eventFileListAfter = eventFileService.joinEventFileListToObjetAfter(eventDTO.getVideoUrlListAfter(), EventFile.FileType.VIDEO);
                eventFileList.addAll(eventFileListAfter);
            }//处置后的音频上传
            if (CollectionUtils.isNotEmpty(eventDTO.getMusicUrlListAfter())) {
                List<EventFile> eventFileListAfter = eventFileService.joinEventFileListToObjetAfter(eventDTO.getMusicUrlListAfter(), EventFile.FileType.AUDIO);
                eventFileList.addAll(eventFileListAfter);
            }
            Event event = eventRepository.findById(eventDTO.getId()).orElse(new Event());
            List<EventFile> files = event.getEventFileList();
            if (CollectionUtils.isNotEmpty(files)) {
                eventFileList.addAll(files);
            }
        } else {/*新增*/
            //处置前的图片上传
            if (CollectionUtils.isNotEmpty(eventDTO.getImageUrlList())) {
                List<EventFile> fileList = eventFileService.joinEventFileListToObjet(eventDTO.getImageUrlList(), EventFile.FileType.IMAGE);
                eventFileList.addAll(fileList);
            }
            //处置前的视频上传
            if (CollectionUtils.isNotEmpty(eventDTO.getVideoUrlList())) {
                List<EventFile> fileList = eventFileService.joinEventFileListToObjet(eventDTO.getVideoUrlList(), EventFile.FileType.VIDEO);
                eventFileList.addAll(fileList);
            }
            //处置前的音频上传
            if (CollectionUtils.isNotEmpty(eventDTO.getMusicUrlList())) {
                List<EventFile> fileList = eventFileService.joinEventFileListToObjet(eventDTO.getMusicUrlList(), EventFile.FileType.AUDIO);
                eventFileList.addAll(fileList);
            }
            //处置后的图片上传
            if (CollectionUtils.isNotEmpty(eventDTO.getImageUrlListAfter())) {
                List<EventFile> eventFileListAfter = eventFileService.joinEventFileListToObjetAfter(eventDTO.getImageUrlListAfter(), EventFile.FileType.IMAGE);
                eventFileList.addAll(eventFileListAfter);
            }
            //处置后的视频上传
            if (CollectionUtils.isNotEmpty(eventDTO.getVideoUrlListAfter())) {
                List<EventFile> eventFileListAfter = eventFileService.joinEventFileListToObjetAfter(eventDTO.getVideoUrlListAfter(), EventFile.FileType.VIDEO);
                eventFileList.addAll(eventFileListAfter);
            }
            //处置后的音频上传
            if (CollectionUtils.isNotEmpty(eventDTO.getMusicUrlListAfter())) {
                List<EventFile> eventFileListAfter = eventFileService.joinEventFileListToObjetAfter(eventDTO.getMusicUrlListAfter(), EventFile.FileType.AUDIO);
                eventFileList.addAll(eventFileListAfter);
            }
        }
        eventDTO.setEventFileList(eventFileList);
    }

    public EventVO getShowPoint(String eventId) {
        Event event = this.findOne(eventId);
        return EventMapper.INSTANCE.eventToEventVO(event);
    }

    public long findAllByConditionId(String id) {
        return eventRepository.countEventByConditionId(id);
    }

    public List<EventVO> similarCasesForEvent(double lng, double lat) {
        List<Event> all = eventRepository.findAll();
        List<Event> eventList = new ArrayList<>();
        for (Event event : all) {
            if (similarCases(lng, lat, event.getX(), event.getY())) {
                eventList.add(event);
            }
        }
        return EventMapper.INSTANCE.eventListToEventVOList(eventList);
    }

    private boolean similarCases(double lng, double lat, double eventLng, double eventLat) {
        double radLat1 = rad(lat);
        double radLat2 = rad(eventLat);
        double a = radLat1 - radLat2;
        double b = rad(lng) - rad(eventLng);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        double earthRadius = 6378.137;
        s = s * earthRadius * 1000;
        double componentDistance = 3;
        return s < componentDistance;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 为手机端新增接口 保存事件后直接上报
     *
     * @param eventDTO 数据
     */
    public void saveTempForApi(EventDTO eventDTO) {
        eventDTO.setSts(0);
        this.uploadFiles(eventDTO);
        Event event = EventMapper.INSTANCE.eventDTOToEvent(eventDTO);
        event.setUser(SecurityUtil.getUser().castToUser());
        if (StringUtils.isNotBlank(eventDTO.getObjId())) {
            Component component = new Component();
            component.setId(eventDTO.getObjId());
            event.setComponent(component);
        }
        Event save = eventRepository.save(event);
        this.reportOnList(save.getId());
    }

    /**
     * 转应急
     *
     * @param eventId id
     */
    public void changeUrgent(String eventId) {
        Event event = this.findOne(eventId);
        if (event.getUrgent().equals(0)) {
            event.setUrgent(1);
        } else {
            event.setUrgent(0);
        }
        eventRepository.saveAndFlush(event);
    }

    /**
     * 查找转应急状态
     *
     * @param eventId id
     */
    public int findUrgent(String eventId) {
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        String taskName = "值班长-立案";
        if (taskName.equals(statistics.getTaskName())) {
            Event event = this.findOne(eventId);
            return event.getUrgent() == null ? 0 : event.getUrgent();
        } else {
            return 9;
        }
    }

    /**
     * 大屏 问题来源
     * 上报来源
     *
     * @return 数量
     */
    public Map<String, Object> getCountByEventSource(){
        long countByEventSourceKey0 = getCountByEventSourceKey(0);
        long countByEventSourceKey1 = getCountByEventSourceKey(1);
        Map<String, Object> map = new HashMap<>(2);
        map.put("supervise", countByEventSourceKey0);
        map.put("hotline", countByEventSourceKey1);
        return map;
    }

    private long getCountByEventSourceKey(int key){
        return eventRepository.countEventByEventSource_FieldNameAndEventSource_TableNameAndEventSource_Key("eventSource", "event", key);
    }

}
