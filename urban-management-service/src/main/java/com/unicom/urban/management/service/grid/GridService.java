package com.unicom.urban.management.service.grid;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.grid.GridRepository;
import com.unicom.urban.management.mapper.GridMapper;
import com.unicom.urban.management.mapper.TreeMapper;
import com.unicom.urban.management.pojo.dto.AreaDTO;
import com.unicom.urban.management.pojo.dto.GridDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.pojo.vo.TreeVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.publish.PublishService;
import com.unicom.urban.management.service.record.RecordService;
import com.unicom.urban.management.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.*;

/**
 * 网格
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class GridService {

    @Autowired
    private GridRepository gridRepository;
    @Autowired
    private RecordService recordService;
    @Autowired
    private PublishService publishService;
    @Autowired
    private KVService kvService;
    @Autowired
    private UserService userService;
    @Autowired
    private GridService gridService;
    @Autowired
    private EventService eventService;

    public Page<GridVO> search(GridDTO gridDTO, Pageable pageable) {
        Page<Grid> page = gridRepository.findAll((Specification<Grid>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(gridDTO.getGridName())) {
                list.add(criteriaBuilder.like(root.get("gridName").as(String.class), "%" + gridDTO.getGridName() + "%"));
            }
            if (StringUtils.isNotEmpty(gridDTO.getCommunity())) {
                list.add(criteriaBuilder.equal(root.get("parent").get("id").as(String.class), gridDTO.getCommunity()));
            }
            if (StringUtils.isNotEmpty(gridDTO.getStreet())) {
                list.add(criteriaBuilder.equal(root.get("parent").get("parent").get("id").as(String.class), gridDTO.getStreet()));
            }
            if (StringUtils.isNotEmpty(gridDTO.getRegion())) {
                list.add(criteriaBuilder.equal(root.get("parent").get("parent").get("parent").get("id").as(String.class), gridDTO.getRegion()));
            }
            list.add(criteriaBuilder.equal(root.get("record").get("sts").as(Integer.class), StsConstant.RELEASE));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<GridVO> gridVOList = GridMapper.INSTANCE.gridListToGridVOList(page.getContent());
        return new PageImpl<>(gridVOList, page.getPageable(), page.getTotalElements());
    }

    public List<GridVO> search() {
        List<Grid> gridList = gridRepository.findAll((Specification<Grid>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.equal(root.get("record").get("createBy").get("id").as(String.class), SecurityUtil.getUserId()));
            list.add(criteriaBuilder.equal(root.get("record").get("sts").as(Integer.class), StsConstant.EDITING));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        });
        return GridMapper.INSTANCE.gridListToGridVOList(gridList);
    }

    public List<GridVO> searchAll() {
        List<Grid> gridList = gridRepository.findAll((Specification<Grid>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.equal(root.get("record").get("sts").as(Integer.class), StsConstant.RELEASE));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        });
        return GridMapper.INSTANCE.gridListToGridVOList(gridList);
    }

    public GridVO search(String gridId) {
        Grid one = findOne(gridId);
        return GridMapper.INSTANCE.gridToGridVO(one);
    }

    public void save(GridDTO gridDTO) {

        Publish savePublish;
        List<Publish> publishes = publishService.allByKvId();
        if (publishes.size() > 1) {
            throw new DataValidException("网格图层应保持唯一");
        } else {
            if (publishes.size() > 0) {
                savePublish = publishes.get(0);
            } else {
                Publish publish = new Publish();
                KV oneById = kvService.findOneById(KvConstant.KV_RELEASE_GRID);
                publish.setKv(oneById);
                publish.setName("网格");
                savePublish = publishService.save(publish);
            }
        }

        Record record = GridMapper.INSTANCE.gridDTOToRecord(gridDTO);
        record.setPublish(savePublish);
        Record saveRecord = recordService.save(record);

        Grid grid = GridMapper.INSTANCE.gridDTOToGrid(gridDTO);
        grid.setLevel(4);
        grid.setPublish(savePublish);
        String userId = SecurityUtil.getUserId();
        User user = userService.findOne(userId);
//        grid.setDept(user.getDept());
        grid.setRecord(saveRecord);
        gridRepository.save(grid);
    }

    public void delete(String id) {
        List<Grid> ifGrid = gridRepository.findAllByParentId(id);
        if(ifGrid.size()>0){
            throw new DataValidException("区域下有子节点，不能删除");
        }
        gridRepository.deleteById(id);
    }

    public Grid findOne(GridDTO gridDTO) {
        Grid grid = GridMapper.INSTANCE.gridDTOToGrid(gridDTO);
        return gridRepository.getOne(grid.getId());
    }

    public Grid findOne(String id) {
        return gridRepository.getOne(id);
    }

    public void update(GridDTO gridDTO) {
        Grid grid = findOne(gridDTO.getId());
        grid.setGridCode(gridDTO.getGridCode());
        grid.setGridName(gridDTO.getGridName());
        grid.setRemark(gridDTO.getRemark());
        grid.setArea(gridDTO.getArea());
        grid.setInitialDate(gridDTO.getInitialDate());
        grid.setTerminationDate(gridDTO.getTerminationDate());
        gridRepository.saveAndFlush(grid);

        Record record = grid.getRecord();
        record.setCoordinate(gridDTO.getCoordinate());
        recordService.update(record);

    }

    public void saveArea(AreaDTO areaDTO) {
        if (ifAreaName(areaDTO.getPid(), areaDTO.getGridName(), null)) {
            throw new DataValidException("区域名称重复");
        }
        Grid grid = new Grid();
        grid.setGridName(areaDTO.getGridName());
        if (StringUtils.isNotBlank(areaDTO.getPid())) {
            grid.setParent(gridRepository.findById(areaDTO.getPid()).orElse(null));
        }
        grid.setLevel(areaDTO.getLevel());
        gridRepository.save(grid);

    }

    public void updateArea(AreaDTO areaDTO) {
        if (ifAreaName(areaDTO.getPid(), areaDTO.getGridName(), areaDTO.getId())) {
            throw new DataValidException("区域名称重复");
        }
        Optional<Grid> ifGrid = gridRepository.findById(areaDTO.getId());
        if (ifGrid.isPresent()) {
            Grid grid = ifGrid.get();
            grid.setGridName(areaDTO.getGridName());
            if (StringUtils.isNotBlank(areaDTO.getPid())) {
                grid.setParent(gridRepository.findById(areaDTO.getPid()).orElse(null));
            }
            grid.setLevel(areaDTO.getLevel());
            gridRepository.save(grid);
        }
    }

    public void collocation(AreaDTO areaDTO) {
        Optional<Grid> ifParentGrid = gridRepository.findById(areaDTO.getId());
        if (ifParentGrid.isPresent()) {
            Grid area = ifParentGrid.get();
            areaDTO.getGridIds().forEach(g -> {
                Optional<Grid> ifGrid = gridRepository.findById(g);
                if (ifGrid.isPresent()) {
                    Grid grid = ifGrid.get();
                    if (!grid.getParent().getId().equals(g)) {
                        grid.setParent(area);
                        gridRepository.saveAndFlush(grid);
                    }
                }
            });
        }
    }

    private boolean ifAreaName(String pid, String gridName, String id) {
        List<Grid> grids = gridRepository.findAllByParent_IdAndGridName(pid, gridName);
        if (StringUtils.isNotBlank(id)) {
            return grids.size() != 0;
        } else {
            return grids.size() != 0 && (grids.size() != 1 || !id.equals(grids.get(0).getId()));
        }
    }

    public List<Grid> findAllByPublishIdAndRecordSts(String publishId) {
        return gridRepository.findAllByPublish_IdAndRecord_Sts(publishId, StsConstant.EDITING);
    }

    /**
     * 新增网格导入用
     *
     * @param grid 网格实体类
     */
    public void save4Import(Grid grid) {
        gridRepository.saveAndFlush(grid);
    }

    /**
     * 查询网格列表
     *
     * @param parentId
     * @return
     */
    public List<GridVO> findAllByParentId(String parentId) {
        List<Grid> gridList = gridRepository.findAllByParentId(parentId);
        return GridMapper.INSTANCE.gridListToGridVOList(gridList);
    }

    /**
     * 获取所在区域
     *
     * @return
     */
    public List<GridVO> findAllByParentIsNull() {
        List<Grid> gridList = gridRepository.findAllByParentIsNull();
        return GridMapper.INSTANCE.gridListToGridVOList(gridList);
    }

    /**
     * 获取网格的中心点
     *
     * @param gridId 网格id
     * @return 点
     */
    public String getGridCenter(String gridId) {
        Grid grid = this.findOne(gridId);
        String coordinate = grid.getRecord().getCoordinate();
        List<Coordinate> coordinateList = new ArrayList<>();
        /* CoordinateList.add(new Coordinate("经度","纬度")); */
        String regex = "-";
        String regex1 = ",";
        for (String s : coordinate.split(regex)) {
            coordinateList.add(new Coordinate(Double.parseDouble(s.split(regex1)[0]), Double.parseDouble(s.split(regex1)[1])));
        }
        return getCenterOfGravityPoint4(coordinateList);
    }

    /**
     * 获取质心 or 内心
     * <p>
     * 创建多点
     * MultiPoint morePoint = geometryFactory.createMultiPointFromCoords(coordinates);
     * 得到多点的质心
     * Point pt = morePoint.getCentroid();
     * 得到多点内心
     * Point pt = morePoint.getInteriorPoint();
     *
     * @param coordinateList
     * @return
     */
    private String getCenterOfGravityPoint4(List<Coordinate> coordinateList) {
        Coordinate[] coordinates = new Coordinate[coordinateList.size()];
        coordinateList.toArray(coordinates);
        GeometryFactory geometryFactory = new GeometryFactory();
        /* 创建多边形 */
        Polygon polygon = geometryFactory.createPolygon(coordinates);
        /* 得到多边形质心 */
        Point pt = polygon.getCentroid();
        /* 得到多边形内心 */
        double x = polygon.getInteriorPoint().getCoordinates()[0].getX();
        double y = polygon.getInteriorPoint().getCoordinates()[0].getY();
        return x + "-" + y;
    }

    public List<GridVO> findGridAll() {
        List<Grid> gridList = gridRepository.findAll();
        return GridMapper.INSTANCE.gridListToGridVOList(gridList);
    }

    public Grid findByGridCode(String gridCode) {
        return gridRepository.findByGridCode(gridCode);
    }

    private Map<String, Object> findByParentId(String id) {
        Grid grid = this.findOne(id);
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", grid.getId());
        map.put("name", grid.getGridName());
        map.put("level", grid.getLevel());
        return map;
    }

    public void addMapToList(Grid byGridCode, List<Map<String, Object>> mapList) {
        Grid grid = this.findOne(byGridCode.getId());
        Grid parent1 = grid.getParent();
        mapList.add(gridService.findByParentId(parent1.getId()));
        Grid parent2 = parent1.getParent();
        mapList.add(gridService.findByParentId(parent2.getId()));
        Grid parent3 = parent2.getParent();
        mapList.add(gridService.findByParentId(parent3.getId()));
    }

    /**
     * 查询发布挖的网格
     * 目前网格和区域街道在一个数据表中 所以需要level字段 将来会修改
     *
     * @return 网格
     */
    public List<Grid> allByLevelAndRecordSts() {
        return gridRepository.findAllByLevelAndRecord_Sts(4, StsConstant.RELEASE);
    }

    /**
     * 坐标
     *
     * @param eventId id
     * @return 坐标
     */
    public String getGridPolygon(String eventId) {
        Event one = eventService.findOne(eventId);
        return one.getGrid().getRecord().getCoordinate();
    }

    public List<TreeVO> searchTree(List<Integer> levels) {

        List<Grid> grids = gridRepository.findAllByLevelIn(levels);
        return TreeMapper.INSTANCE.gridListToTreeVOList(grids);
    }

    /**
     * 修改指定网格中的人员
     *
     * @param gridId   网格id
     * @param userList 人员id集合
     */
    public void save(String gridId, List<String> userList) {
        Grid grid = findOne(gridId);
        List<User> users = new ArrayList<>(userList.size());
        for (String s : userList) {
            User user = new User(s);
            users.add(user);
        }
        grid.setUserList(users);
        gridRepository.saveAndFlush(grid);
    }

}
