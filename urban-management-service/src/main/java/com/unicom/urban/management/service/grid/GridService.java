package com.unicom.urban.management.service.grid;

import com.unicom.urban.management.dao.grid.GridRepository;
import com.unicom.urban.management.mapper.GridMapper;
import com.unicom.urban.management.pojo.dto.GridDTO;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.vo.GridVO;
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

    public void save(GridDTO gridDTO) {
        Grid grid = GridMapper.INSTANCE.gridDTOToGrid(gridDTO);
        gridRepository.save(grid);
    }

    public Grid findOne(GridDTO gridDTO) {
        Grid grid = GridMapper.INSTANCE.gridDTOToGrid(gridDTO);
        return gridRepository.getOne(grid.getId());
    }

    public void update(GridDTO gridDTO) {
        Grid grid = findOne(gridDTO);
        grid.setLayerGridName(gridDTO.getLayerGridName());
        grid.setRemark(gridDTO.getRemark());
        grid.setInitialDate(gridDTO.getInitialDate());
        grid.setDept(gridDTO.getDept());

    }

}
