package com.unicom.urban.management.web.project.urbanimport;

import com.alibaba.fastjson.JSON;
import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.util.FileUploadUtil;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.service.publish.PublishService;
import com.unicom.urban.management.service.user.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部件导入管理
 *
 * @author liubozhi
 */
@RestController
@ResponseResultBody
public class ComponentImportController {
    @Autowired
    private PublishService releaseService;
    @Autowired
    private FileUploadUtil fileUploadUtil;
    @Autowired
    private UserService userService;

    @Value("${gis.api.url}")
    private String url;

    @PostMapping("/componentImport")
    public Result componentImport(HttpServletRequest request, String layerName, String layerSettingType, String shpType) {
        String layerId = "";
        layerSettingType = "1";
        shpType = "";
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 创建集合接受文件
        List<MultipartFile> fileList = multiRequest.getFiles("file");
//        String url = "http://localhost:8099/api/urbanImport";
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(url);
//            httpPost.setHeader("Content-Type", "multipart/form-data");
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();

            for (MultipartFile multipartFile : fileList) {
                String filePath = fileUploadUtil.uploadFileToLocal(multipartFile);
                reqEntity.addPart(multipartFile.getName(), new FileBody(new File(filePath)));

            }
            //接口常规参数开始-----------------------------------------
            Map<String, String> paramMap = new HashMap<>();

            paramMap.put("layerName", layerName);
            StringBody paramBody = new StringBody(JSON.toJSONString(paramMap));
            //接口常规参数结束------------------------------------------------------

            reqEntity.addPart("paramBody", paramBody);
            HttpEntity httpEntity = reqEntity.build();
            httpPost.setEntity(httpEntity);

            HttpResponse response = httpclient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                System.out.println(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                EntityUtils.consume(resEntity);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {
            }
        }
//        Publish publish = new Publish();
//        publish.setLayerId(layerId);
//        KV kv = new KV();
//        kv.setValue(layerSettingType);
//        publish.setKv(kv);
//        publish.setName(layerName);
//        User user = userService.findOne(SecurityUtil.getUserId());
//        publish.setUser(user);
//        releaseService.save(publish);

        return Result.success();
    }
//    public checkPublish(){
//
//
//
//
//
//
//
//
//    }
}
