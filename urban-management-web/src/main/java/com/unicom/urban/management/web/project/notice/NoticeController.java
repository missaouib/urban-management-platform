package com.unicom.urban.management.web.project.notice;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.NoticeDTO;
import com.unicom.urban.management.pojo.vo.NoticeVO;
import com.unicom.urban.management.service.notice.NoticeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 通知公告类型
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/toNoticeList")
    public ModelAndView toNoticeList() {
        return new ModelAndView(SystemConstant.PAGE + "/notice/informNotice");
    }

    @GetMapping("/toNoticeTypeList")
    public ModelAndView toNoticeTypeList() {
        return new ModelAndView(SystemConstant.PAGE + "/notice/informNoticeType");
    }

    @GetMapping("/search")
    public Page<NoticeVO> search(NoticeDTO noticeDTO, @PageableDefault Pageable pageable) {
        return noticeService.search(noticeDTO, pageable);
    }

    @PostMapping("/noticeSave")
    public Result noticeSave(@Valid NoticeDTO noticeDTO) {
        noticeService.save(noticeDTO);
        return Result.success("新增成功");
    }

    @PostMapping("/noticeUpdate")
    public Result noticeUpdate(@Valid NoticeDTO noticeDTO) {
        if (StringUtils.isBlank(noticeDTO.getId())) {
            throw new DataValidException("主键不能为空");
        }
        noticeService.update(noticeDTO);
        return Result.success("修改成功");
    }

    @PostMapping("/noticeDelete")
    public Result noticeDelete(@NotNull String id) {
        noticeService.delete(id);
        return Result.success("删除成功");
    }

}
