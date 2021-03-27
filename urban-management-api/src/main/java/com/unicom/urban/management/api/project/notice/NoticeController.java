package com.unicom.urban.management.api.project.notice;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.dto.NoticeDTO;
import com.unicom.urban.management.pojo.vo.NoticeVO;
import com.unicom.urban.management.service.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通知公告类型
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/search")
    public Page<NoticeVO> search(NoticeDTO noticeDTO, @PageableDefault Pageable pageable) {
        return noticeService.search(noticeDTO, pageable);
    }

}
