package com.unicom.urban.management.web.project.notice;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.NoticeTypeDTO;
import com.unicom.urban.management.pojo.vo.NoticeTypeVO;
import com.unicom.urban.management.service.notice.NoticeTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/noticeType")
public class NoticeTypeController {

    @Autowired
    private NoticeTypeService noticeTypeService;

    @GetMapping("/getNoticeTypeList")
    public List<NoticeTypeVO> getNoticeTypeList() {
        return noticeTypeService.findAll();
    }

    @GetMapping("/search")
    public Page<NoticeTypeVO> search(NoticeTypeDTO noticeTypeDTO, @PageableDefault Pageable pageable) {
        return noticeTypeService.search(noticeTypeDTO, pageable);
    }

    @PostMapping("/noticeTypeSave")
    public Result noticeTypeSave(@Valid NoticeTypeDTO noticeTypeDTO) {
        noticeTypeService.save(noticeTypeDTO);
        return Result.success("新增成功");
    }

    @PostMapping("/noticeTypeUpdate")
    public Result noticeTypeUpdate(@Valid NoticeTypeDTO noticeTypeDTO) {
        if (StringUtils.isBlank(noticeTypeDTO.getId())) {
            throw new DataValidException("主键不能为空");
        }
        noticeTypeService.update(noticeTypeDTO);
        return Result.success("修改成功");
    }

    @PostMapping("/noticeTypeDelete")
    public Result noticeTypeDelete(@NotNull String id) {
        noticeTypeService.delete(id);
        return Result.success("删除成功");
    }

}
