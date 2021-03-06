package com.unicom.urban.management.service.idioms;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.idioms.IdiomsRepository;
import com.unicom.urban.management.mapper.IdiomsMapper;
import com.unicom.urban.management.pojo.entity.Idioms;
import com.unicom.urban.management.pojo.vo.IdiomsVO;
import com.unicom.urban.management.util.SecurityUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

@Service
@Transactional(rollbackOn = Exception.class)
public class IdiomsService {

    private static final String MESSAGE = "此惯用语已存在无需保存";

    @Autowired
    private IdiomsRepository idiomsRepository;

    public void saveIdioms(IdiomsVO idiomsVO) {
        String idiomsValue = this.isIdioms(idiomsVO);
        if (MESSAGE.equals(idiomsValue)) {
            throw new DataValidException(MESSAGE);
        }
        if (idiomsRepository.existsByIdiomsValue(idiomsValue)) {
            throw new DataValidException(MESSAGE);
        }
        Idioms idioms = new Idioms();
        idioms.setIdiomsValue(idiomsValue);
        idioms.setUser(SecurityUtil.getUser().castToUser());
        idiomsRepository.save(idioms);
    }

    public String isIdioms(IdiomsVO idiomsVO) {
        String value = idiomsVO.getIdiomsValue().trim();
        char[] c = value.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        //去除数字字母汉字以外的字符。
        for (char item : c) {
            if (String.valueOf(item).matches("[0-9a-zA-Z\u4e00-\u9fa5]")) {
                stringBuilder.append(item);
            }
        }
        String idiomsValue = stringBuilder.toString();
        if (idiomsRepository.existsByIdiomsValue(idiomsValue)) {
            return MESSAGE;
        }
        return idiomsValue;
    }

    public Page<IdiomsVO> search(IdiomsVO idiomsVO, Pageable pageable) {
        Page<Idioms> page = idiomsRepository.findAll((Specification<Idioms>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(idiomsVO.getIdiomsValue())) {
                list.add(criteriaBuilder.like(root.get("idiomsValue").as(String.class), "%" + idiomsVO.getIdiomsValue() + "%"));

            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<IdiomsVO> idiomsVOList = IdiomsMapper.INSTANCE.idiomsListToIdiomsVOList(page.getContent());
        return new PageImpl<>(idiomsVOList, page.getPageable(), page.getTotalElements());
    }

    public List<String> findAllIdiomsValue() {
        List<Idioms> list = idiomsRepository.findAll();
        List<String> idiomsValueList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Idioms idioms : list) {
                idiomsValueList.add(idioms.getIdiomsValue());
            }
        }
        return idiomsValueList;
    }
}
