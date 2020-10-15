package com.unicom.urban.management.service.grid;

import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.grid.GridRepository;
import com.unicom.urban.management.mapper.GridMapper;
import com.unicom.urban.management.pojo.dto.GridDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.record.RecordService;
import com.unicom.urban.management.service.release.ReleaseService;
import com.unicom.urban.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private ReleaseService releaseService;
    @Autowired
    private KVService kvService;
    @Autowired
    private UserService userService;

    public Page<GridVO> search(GridDTO gridDTO, Pageable pageable) {
        Page<Grid> page = gridRepository.findAll((Specification<Grid>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            /*if (StringUtils.isNotEmpty(userDTO.getName())) {
                list.add(criteriaBuilder.equal(root.get("name").as(String.class), userDTO.getName()));
            }
            if (StringUtils.isNotEmpty(userDTO.getUsername())) {
                list.add(criteriaBuilder.equal(root.get("username").as(String.class), userDTO.getUsername()));
            }*/
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<GridVO> gridVOList = GridMapper.INSTANCE.gridListToGridVOList(page.getContent());
        return new PageImpl<>(gridVOList, page.getPageable(), page.getTotalElements());
    }

//    public List<GridVO>

    public void save(GridDTO gridDTO) {

        Release release = GridMapper.INSTANCE.gridDTOToRelease(gridDTO);
        KV oneById = kvService.findOneById("28526efe-3db5-415b-8c7a-d0e3a49cab8f");
        release.setKv(oneById);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = formatter.format(new Date());
        release.setReleaseName("网格" + format);
        Release saveRelease = releaseService.save(release);

        Record record = GridMapper.INSTANCE.gridDTOToRecord(gridDTO);
        record.setRelease(saveRelease);
        recordService.save(record);


        Grid grid = GridMapper.INSTANCE.gridDTOToGrid(gridDTO);
        grid.setRelease(saveRelease);
        grid.setSts(StsConstant.INUSE);
        User user = userService.findOne(SecurityUtil.getUserId());
        grid.setUser(user);
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
