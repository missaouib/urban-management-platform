package com.unicom.urban.management.service.component;

import com.unicom.urban.management.dao.component.ComponentRepository;
import com.unicom.urban.management.mapper.ComponentMapper;
import com.unicom.urban.management.mapper.MenuMapper;
import com.unicom.urban.management.pojo.dto.ComponentDTO;
import com.unicom.urban.management.pojo.entity.Component;
import com.unicom.urban.management.pojo.entity.Menu;
import com.unicom.urban.management.pojo.vo.ComponentVO;
import com.unicom.urban.management.pojo.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 顾志杰
 * @date 2020/10/14-9:26
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ComponentService {

    private final ComponentRepository componentRepository;

    @Autowired
    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    /**
     * 获取所有部件信息
     *
     * @param component 查询条件
     * @return list
     */
    public List<ComponentVO> getComponentList(ComponentDTO component) {
        List<Component> componentList = componentRepository.findAll((Specification<Component>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        });
        return ComponentMapper.INSTANCE.componentListToComponentVOList(componentList);
    }

    /**
     * 获取所有部件信息（分页）
     *
     * @param component 查询条件
     * @param pageable  分页信息
     * @return page
     */
    public Page<ComponentVO> getComponentList(ComponentDTO component, Pageable pageable) {
        Page<Component> page = componentRepository.findAll((Specification<Component>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<ComponentVO> componentVOList = ComponentMapper.INSTANCE.componentListToComponentVOList(page.getContent());

        return new PageImpl<>(componentVOList, page.getPageable(), page.getTotalElements());
    }

    /**
     * 获取预览地址
     *
     * @param component 条件
     * @return string
     */
    public String getComponentPreview(ComponentDTO component) {
        return componentRepository.findById(component.getId()).map(Component::getUrl).orElse("");
    }

    /**
     * 获取部件
     * @param component 条件
     * @return T
     */
    public ComponentVO getComponent(ComponentDTO component) {
        Optional<Component> byId = componentRepository.findById(component.getId());
        if (byId.isPresent()) {
            Component form = byId.orElse(new Component());
            return ComponentMapper.INSTANCE.componentToComponentVO(form);
        }
        throw new RuntimeException("部件不存在");
    }

}
