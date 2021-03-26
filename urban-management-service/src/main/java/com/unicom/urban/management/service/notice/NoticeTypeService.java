package com.unicom.urban.management.service.notice;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.notice.NoticeTypeRepository;
import com.unicom.urban.management.pojo.dto.NoticeTypeDTO;
import com.unicom.urban.management.pojo.entity.notice.NoticeType;
import com.unicom.urban.management.pojo.vo.NoticeTypeVO;
import org.springframework.beans.BeanUtils;
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
 * 通知公告类型
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class NoticeTypeService {

    @Autowired
    private NoticeTypeRepository noticeTypeRepository;

    @Log(name = "通知公告类型-新增")
    public void save(NoticeTypeDTO noticeTypeDTO) {
        NoticeType noticeType = new NoticeType();
        BeanUtils.copyProperties(noticeTypeDTO, noticeType);
        noticeTypeRepository.save(noticeType);
    }

    public NoticeType findOne(String id) {
        return noticeTypeRepository.getOne(id);
    }

    @Log(name = "通知公告类型-修改")
    public void update(NoticeTypeDTO noticeTypeDTO) {
        NoticeType one = findOne(noticeTypeDTO.getId());
        one.setName(noticeTypeDTO.getName());
        noticeTypeRepository.saveAndFlush(one);
    }

    @Log(name = "通知公告类型-删除")
    public void delete(String id) {
        NoticeType noticeType = findOne(id);
        if (noticeType.hasNotice()) {
            throw new DataValidException("该类型下有消息无法删除");
        }
        noticeType.setDeleted("1");
        noticeTypeRepository.saveAndFlush(noticeType);
    }

    public List<NoticeTypeVO> findAll() {
        List<NoticeType> noticeTypeList = noticeTypeRepository.findAll();
        List<NoticeTypeVO> noticeTypeVOList = new ArrayList<>(noticeTypeList.size());
        BeanUtils.copyProperties(noticeTypeList, noticeTypeVOList);
        return noticeTypeVOList;
    }

    public Page<NoticeTypeVO> search(NoticeTypeDTO noticeTypeDTO, Pageable pageable) {
        Page<NoticeType> page = noticeTypeRepository.findAll((Specification<NoticeType>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<NoticeTypeVO> noticeTypeVOList = new ArrayList<>(page.getContent().size());
        BeanUtils.copyProperties(page.getContent(), noticeTypeVOList);
        return new PageImpl<>(noticeTypeVOList, page.getPageable(), page.getTotalElements());
    }

}
