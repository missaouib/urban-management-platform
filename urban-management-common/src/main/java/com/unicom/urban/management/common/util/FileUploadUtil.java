package com.unicom.urban.management.common.util;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author liukai
 * <p>
 * 文件上传工具类
 */
@Component
public class FileUploadUtil extends FileUtils {

    @Autowired
    protected FastFileStorageClient storageClient;

    public void uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
    }

}
