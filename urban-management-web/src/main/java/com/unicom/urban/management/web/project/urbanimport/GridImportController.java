package com.unicom.urban.management.web.project.urbanimport;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.util.FileUploadUtil;
import com.unicom.urban.management.common.util.JsonUtils;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.entity.KV;
import com.unicom.urban.management.pojo.entity.Publish;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.service.publish.PublishService;
import com.unicom.urban.management.service.user.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网格导入管理
 *
 * @author liubozhi
 */
@RestController
@ResponseResultBody
public class GridImportController {

    @Autowired
    private PublishService releaseService;
    @Autowired
    private FileUploadUtil fileUploadUtil;
    @Autowired
    private UserService userService;

    @Value("${gis.api.url}")
    private String url;
    @GetMapping("/gImport")
    public ModelAndView cImport() {
        return new ModelAndView(SystemConstant.PAGE + "/urbanImport/gImport");
    }
    /**
     * 网格导入
     * @param request 要导入的文件request
     * @param layerName 网格名称
     * @param layerSettingType 部件或网格
     * @param shpType 预留参数
     * @return
     */
    @RequestMapping("/gridImport")
    public Result gridImport(HttpServletRequest request, String layerName, String layerSettingType, String shpType) {
        String layerId = "";
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
            paramMap.put("layerSettingType", layerSettingType);
            StringBody paramBody = new StringBody(JsonUtils.objectToJson(paramMap));
            //接口常规参数结束------------------------------------------------------

            reqEntity.addPart("paramBody", paramBody);
            HttpEntity httpEntity = reqEntity.build();
            httpPost.setEntity(httpEntity);

            HttpResponse response = httpclient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                byte[] b = new byte[2];
                HttpEntity resEntity = response.getEntity();
                InputStream inputStream = resEntity.getContent();

                while ((inputStream.read(b)) != -1) {
                    layerId = new String(b);
                }

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
        Publish publish = new Publish();
        publish.setLayerId(layerId);
        KV kv = new KV();
        kv.setId("28526efe-3db5-415b-8c7a-d0e3a49cab8f");
        publish.setKv(kv);
        publish.setName(layerName);
        User user = userService.findOne(SecurityUtil.getUserId());
        publish.setUser(user);
        releaseService.save(publish);

        return Result.success();
    }

    /**
     * 查询publish表中是有有网格
     * @return
     */
    private boolean checkPublish() {
        boolean f = false;
        return f;
    }
}
