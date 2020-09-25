package com.unicom.urban.management.web.framework.activiti;


import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Slf4j
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@WithMockUser(username = "admin", password = "123433356")
public class FastdfsTest {

//    @Autowired
//    protected FastFileStorageClient storageClient;

    /**
     * 测试文件上传
     *
     * @return
     */
//    @Test
//    public void uploadFileOnly() throws FileNotFoundException {
//        File file = new File("/Users/liukai/IdeaProjects/urban-management-platform/urban-management-common/src/main/java/com/unicom/urban/management/common/constant/SystemConstant.java");
//
//        InputStream inputStream = new FileInputStream(file);
//        FastFile fastFile = new FastFile.Builder()
//                .withFile(inputStream, file.length(), "java")
//                .build();
//        processUploadFileAndMetaData(fastFile);

//
//        log.debug("##上传文件..##");
//        // 上传文件和Metadata
//        StorePath path = storageClient.uploadFile(fastFile);
//        Assert.assertNotNull(path);
//
//        System.out.println("path = " + path.getFullPath());
//
//        if (!fastFile.getMetaDataSet().isEmpty()) {
//            // 验证获取MetaData
//            log.debug("##获取Metadata##");
//            Set<MetaData> fetchMetaData = storageClient.getMetadata(path.getGroup(), path.getPath());
//            assertEquals(fetchMetaData, fastFile.getMetaDataSet());
//        }
//
//        log.debug("##删除文件..##");
//        storageClient.deleteFile(path.getGroup(), path.getPath());


//    }

    @Test
    public void RSAUtilTest() {
        RSA rsa = new RSA();

//获得私钥
        rsa.getPrivateKey();
        System.out.println("rsa.getPrivateKeyBase64() = " + rsa.getPrivateKeyBase64());
//获得公钥
        rsa.getPublicKey();
        System.out.println("rsa.getPublicKeyBase64() = " + rsa.getPublicKeyBase64());

    }

}
