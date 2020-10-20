package com.unicom.urban.management.web.project.urbanimport;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.util.FileUploadUtil;
import com.unicom.urban.management.common.util.JsonUtils;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.ComponentDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.service.component.ComponentService;
import com.unicom.urban.management.service.grid.GridService;
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
public class ImportController {

    @Autowired
    private PublishService releaseService;
    @Autowired
    private FileUploadUtil fileUploadUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private GridService gridService;

    @Value("${gis.api.url}")
    private String url;
    @GetMapping("/gImport")
    public ModelAndView gImport() {
        return new ModelAndView(SystemConstant.PAGE + "/urbanImport/gImport");
    }
    @GetMapping("/cImport")
    public ModelAndView cImport() {
        return new ModelAndView(SystemConstant.PAGE + "/urbanImport/cImport");
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
        HttpClient httpclient = new DefaultHttpClient();
        try {
            //开始导入文件
            HttpResponse response = gisImport(layerName, layerSettingType,multiRequest, httpclient);

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
        /*新增发布*/
        Publish publish = savePublish(layerName, layerId, KV.builder().id(KvConstant.KV_RELEASE_COMPONENT).build());
        /*新增部件*/
        saveGrid(layerName,publish);
        return Result.success();
    }



    /**
     * 部件导入
     * @param request 要导入的文件request
     * @param layerName 部件名称
     * @param layerSettingType 部件或网格
     * @param shpType 预留参数
     * @param componentTypeId 部件主键
     * @return
     */
    @RequestMapping("/componentImport")
    public Result componentImport(HttpServletRequest request, String layerName, String layerSettingType, String shpType,String componentTypeId) {
        String layerId = "";
        shpType = "";
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 创建集合接受文件
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpResponse response = gisImport(layerName, layerSettingType, multiRequest, httpclient);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                byte[] b = new byte[2];
                HttpEntity resEntity = response.getEntity();
                InputStream inputStream = resEntity.getContent();

                while ((inputStream.read(b)) != -1) {
                    layerId = new String(b);
                }
                /*新增发布*/
                Publish publish = savePublish(layerName, layerId, KV.builder().id(KvConstant.KV_RELEASE_COMPONENT).build());
                /*新增部件*/
                savePublish(layerName, layerId, KV.builder().id(KvConstant.KV_RELEASE_COMPONENT).build());
                saveComponentGrid(publish, componentTypeId);
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

        return Result.success();
    }

    private HttpResponse gisImport(String layerName, String layerSettingType, MultipartHttpServletRequest multiRequest,  HttpClient httpclient) throws IOException {
        // 创建集合接受文件
        List<MultipartFile> fileList = multiRequest.getFiles("file");

        HttpPost httpPost = new HttpPost(url);
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

        return httpclient.execute(httpPost);
    }

    /*新增部件*/
    private void saveComponentGrid(Publish publish,String componentId){
        Component component = new Component();
        component.setPublish(publish);
        Component c = componentService.getOne(componentId);
        component.setComponentType(c.getComponentType());
        component.setSts(0);
        componentService.saveComponent4Import(component);
    }
    /*新增网格*/
    private void saveGrid(String layerName,Publish publish){

        Grid grid = new Grid();
        grid.setGridName(layerName);
        grid.setPublish(publish);
//        grid.setSts(0);
        User user = userService.findOne(SecurityUtil.getUserId());
        grid.setUser(user);
    gridService.save4Import(grid);

    }
    /**
     * 新增发布
     * @param layerName
     * @param layerId
     * @param kv
     */
    private Publish savePublish(String layerName, String layerId, KV kv) {
        Publish publish = new Publish();
        publish.setLayerId(layerId);
        publish.setKv(kv);
        publish.setName(layerName);
        User user = userService.findOne(SecurityUtil.getUserId());
        publish.setUser(user);
        releaseService.save(publish);
        return publish;
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
