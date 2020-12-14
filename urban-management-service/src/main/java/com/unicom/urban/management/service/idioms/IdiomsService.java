package com.unicom.urban.management.service.idioms;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.idioms.IdiomsRepository;
import com.unicom.urban.management.mapper.GridMapper;
import com.unicom.urban.management.mapper.IdiomsMapper;
import com.unicom.urban.management.mapper.PublishMapper;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.pojo.vo.IdiomsVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.metamodel.model.domain.SimpleDomainType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sun.reflect.annotation.ExceptionProxy;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class IdiomsService {
    @Autowired
    private IdiomsRepository idiomsRepository;
    public void saveIdioms(IdiomsVO idiomsVO){
        String idiomsValue = idiomsVO.getIdiomsValue().trim();
        Idioms idiomsOld = idiomsRepository.findAllByIdiomsValue(idiomsValue);
        if (ObjectUtils.isNotEmpty(idiomsOld)){
            throw new DataValidException("次惯用语已存在无需保存");
        }
        Idioms idioms = new Idioms();
        idioms.setIdiomsValue(idiomsValue);
        idioms.setUser(SecurityUtil.getUser().castToUser());
        idiomsRepository.save(idioms);
    }

    public Page<IdiomsVO> search(IdiomsVO idiomsVO, Pageable pageable) {
        Page<Idioms> page = idiomsRepository.findAll((Specification<Idioms>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(idiomsVO.getIdiomsValue())) {
                list.add(criteriaBuilder.like(root.get("idiomsVO").get("idioms_value").as(String.class), "%" + idiomsVO.getIdiomsValue() + "%"));

            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<IdiomsVO> idiomsVOList = IdiomsMapper.INSTANCE.idiomsListToIdiomsVOList(page.getContent());
        return new PageImpl<>(idiomsVOList, page.getPageable(), page.getTotalElements());
    }
}
