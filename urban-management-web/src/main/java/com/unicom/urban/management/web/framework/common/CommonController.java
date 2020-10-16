package com.unicom.urban.management.web.framework.common;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用请求Controller
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class CommonController {


    @Autowired
    private FileUploadUtil fileUploadUtil;


    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public Map<String, Object> uploadFile(MultipartFile file) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String uploadFile = fileUploadUtil.uploadFileToFastDFS(file);
        map.put("url", uploadFile);
        return map;
    }


    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
//    @GetMapping("common/download")
//    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
//        try {
//            if (!FileUtils.isValidFilename(fileName)) {
//                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
//            }
//            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
//            String filePath = RuoYiConfig.getDownloadPath() + fileName;
//
//            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//            FileUtils.setAttachmentResponseHeader(response, realFileName);
//            FileUtils.writeBytes(filePath, response.getOutputStream());
//            if (delete) {
//                FileUtils.deleteFile(filePath);
//            }
//        } catch (Exception e) {
//            log.error("下载文件失败", e);
//        }
//    }


}
