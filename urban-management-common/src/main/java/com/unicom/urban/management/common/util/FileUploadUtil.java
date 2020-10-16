package com.unicom.urban.management.common.util;

import cn.hutool.core.io.FileUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    /**
     * 上传文件到Fastdfs
     */
    public String uploadFileToFastDFS(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
        return storePath.getFullPath();
    }

    /**
     * 上传文件到服务器
     */
    public String uploadFileToLocal(MultipartFile file) throws IOException {
        String tmpDirPath = FileUtil.getTmpDirPath();
        String path = tmpDirPath + file.getOriginalFilename();
        File dest = new File(path);
        file.transferTo(dest);
        return path;
    }

}
