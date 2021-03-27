package com.unicom.urban.management.service.notice;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.dao.notice.NoticeRepository;
import com.unicom.urban.management.pojo.dto.NoticeDTO;
import com.unicom.urban.management.pojo.dto.NoticeTypeDTO;
import com.unicom.urban.management.pojo.entity.notice.Notice;
import com.unicom.urban.management.pojo.entity.notice.NoticeType;
import com.unicom.urban.management.pojo.vo.NoticeTypeVO;
import com.unicom.urban.management.pojo.vo.NoticeVO;
import com.unicom.urban.management.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
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
 * 通知公告
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Log(name = "通知公告-新增")
    public void save(NoticeDTO noticeDTO) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeDTO, notice);
        notice.setCreateBy(SecurityUtil.getUser().castToUser());
        notice.setUpdateBy(SecurityUtil.getUser().castToUser());
        noticeRepository.save(notice);
    }

    public Notice findOne(String id) {
        return noticeRepository.getOne(id);
    }

    @Log(name = "通知公告-修改")
    public void update(NoticeDTO noticeDTO) {
        Notice notice = findOne(noticeDTO.getId());
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setNoticeType(noticeDTO.getNoticeType());
        notice.setUpdateBy(SecurityUtil.getUser().castToUser());
        noticeRepository.saveAndFlush(notice);
    }

    @Log(name = "通知公告-删除")
    public void delete(String id) {
        Notice notice = findOne(id);
        notice.setDeleted("1");
        noticeRepository.saveAndFlush(notice);
    }

    public Page<NoticeVO> search(NoticeDTO noticeDTO, Pageable pageable) {
        Page<Notice> page = noticeRepository.findAll((Specification<Notice>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(noticeDTO.getTitle())) {
                list.add(criteriaBuilder.like(root.get("title").as(String.class), "%" + noticeDTO.getTitle() + "%"));
            }
            if (StringUtils.isNotEmpty(noticeDTO.getContent())) {
                list.add(criteriaBuilder.like(root.get("content").as(String.class), "%" + noticeDTO.getContent() + "%"));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<NoticeVO> noticeVOList = new ArrayList<>(page.getContent().size());
        for (Notice notice : page.getContent()) {
            NoticeVO noticeVO = new NoticeVO();
            BeanUtils.copyProperties(notice, noticeVO);
            noticeVOList.add(noticeVO);
        }
        return new PageImpl<>(noticeVOList, page.getPageable(), page.getTotalElements());
    }

}
