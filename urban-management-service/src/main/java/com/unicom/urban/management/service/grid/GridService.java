package com.unicom.urban.management.service.grid;

import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.common.exception.BaseException;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.grid.GridRepository;
import com.unicom.urban.management.mapper.GridMapper;
import com.unicom.urban.management.pojo.dto.GridDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.publish.PublishService;
import com.unicom.urban.management.service.record.RecordService;
import com.unicom.urban.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

    public Page<GridVO> search(GridDTO gridDTO, Pageable pageable) {
        Page<Grid> page = gridRepository.findAll((Specification<Grid>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<GridVO> gridVOList = GridMapper.INSTANCE.gridListToGridVOList(page.getContent());
        return new PageImpl<>(gridVOList, page.getPageable(), page.getTotalElements());
    }

    public List<GridVO> search() {
        List<Grid> gridList = gridRepository.findAll((Specification<Grid>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.equal(root.get("record").get("").get("id").as(String.class), SecurityUtil.getUserId()));
            list.add(criteriaBuilder.equal(root.get("record").get("sts").as(Integer.class), StsConstant.EDITING));
            list.add(criteriaBuilder.equal(root.get("sts").as(Integer.class), StsConstant.INUSE));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        });
        return GridMapper.INSTANCE.gridListToGridVOList(gridList);
    }

    public void save(GridDTO gridDTO) {

        Publish saveRelease;
        List<Publish> publishes = publishService.allByKvId();
        if (publishes.size() > 1) {
            throw new BaseException("网格图层应保持唯一");
        } else {
            if (publishes.size() > 0) {
                saveRelease = publishes.get(0);
            } else {
                Publish release = GridMapper.INSTANCE.gridDTOToRelease(gridDTO);
                KV oneById = kvService.findOneById("28526efe-3db5-415b-8c7a-d0e3a49cab8f");
                release.setKv(oneById);
                release.setName("网格");
                saveRelease = publishService.save(release);
            }
        }

        Record record = GridMapper.INSTANCE.gridDTOToRecord(gridDTO);
        record.setRelease(saveRelease);
        recordService.save(record);

        Grid grid = GridMapper.INSTANCE.gridDTOToGrid(gridDTO);
        grid.setPublish(saveRelease);
        grid.setSts(StsConstant.INUSE);
        User user = userService.findOne(SecurityUtil.getUserId());
        grid.setDept(user.getDept());
        gridRepository.save(grid);
    }

    public Grid findOne(GridDTO gridDTO) {
        Grid grid = GridMapper.INSTANCE.gridDTOToGrid(gridDTO);
        return gridRepository.getOne(grid.getId());
    }

    public void update(GridDTO gridDTO) {
        Grid grid = findOne(gridDTO);
        grid.setRemark(gridDTO.getRemark());
        grid.setInitialDate(gridDTO.getInitialDate());

    }

}
