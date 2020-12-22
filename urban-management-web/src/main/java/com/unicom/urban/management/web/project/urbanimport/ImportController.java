package com.unicom.urban.management.web.project.urbanimport;


import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.properties.ShpFileProperties;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.service.component.ComponentService;
import com.unicom.urban.management.service.componentinfo.ComponentInfoService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.publish.PublishService;
import com.unicom.urban.management.service.record.RecordService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

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
    private ComponentService componentService;
    @Autowired
    private GridService gridService;
    @Autowired
    private ShpFileProperties shpFileProperties;
    @Autowired
    private RecordService recordService;
    @Autowired
    private ComponentInfoService componentInfoService;

    public static String TYPE_SHP = "SHP";
//    public String gisShpPath = shpFileProperties.getFilePath();
    public String gisShpPath = "/gis/shp";
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
     *
     * @param request          要导入的文件request
     * @param layerName        网格名称
     * @param layerSettingType 部件或网格
     * @param shpType          预留参数
     * @return
     */
    @RequestMapping("/gridImport")
    public Result gridImport(HttpServletRequest request, String layerName, String layerSettingType, String shpType) throws IOException {
        String layerId = "";

        shpType = "";
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 创建集合接受文件
        List<MultipartFile> fileList = multiRequest.getFiles("file");
        if (!this.validImport(multiRequest)) {
            Result.fail(99, "");
        }
        // 遍历文件导入数据
        File shpFile = null;
        for (MultipartFile multipartFile : fileList) {
            String suffix =
                    multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            String path = "";
            if (TYPE_SHP.equals(suffix.toUpperCase())) {
                if (StringUtils.isNotEmpty(gisShpPath)) {
                    path = gisShpPath + "/" + multipartFile.getOriginalFilename();
                } else {
                    path = multipartFile.getOriginalFilename();
                }
                //MultipartFile转File
                shpFile = new File(path);

                //保存文件路径
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), shpFile);
            } else {
                if (StringUtils.isNotEmpty(gisShpPath)) {
                    path = gisShpPath + "/" + multipartFile.getOriginalFilename();
                } else {
                    path = multipartFile.getOriginalFilename();
                }
                //MultipartFile转File
                File otherFile = new File(path);
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), otherFile);
            }
        }

        /*新增发布*/
        Publish publish = savePublish(layerName, layerId, KV.builder().id(KvConstant.KV_RELEASE_COMPONENT).build());

        /*新增部件*/
        this.readGridFileInfo(layerName,shpFile,publish);

        return Result.success();
    }


    /**
     * 部件导入
     *
     * @param request          要导入的文件request
     * @param layerName        部件名称
     * @param layerSettingType 部件或网格
     * @param shpType          预留参数
     * @param componentTypeId  部件主键
     * @return
     */
    @RequestMapping("/componentImport")
    public Result componentImport(HttpServletRequest request, String layerName, String layerSettingType, String shpType, String componentTypeId) throws IOException {
        String layerId = "";
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 创建集合接受文件
        List<MultipartFile> fileList = multiRequest.getFiles("file");
        // 遍历文件导入数据
        File shpFile = null;
        if (!validImport(multiRequest)) {
            return Result.fail(99, "格式错误，请选择正确文件格式！");
        }
        for (MultipartFile multipartFile : fileList) {
            String suffix =
                    multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            String path = "";
            if (TYPE_SHP.equals(suffix.toUpperCase())) {
                if (StringUtils.isNotEmpty(gisShpPath)) {
                    path = gisShpPath + "/" + multipartFile.getOriginalFilename();
                } else {
                    path = multipartFile.getOriginalFilename();
                }
                //MultipartFile转File
                shpFile = new File(path);

                //保存文件路径
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), shpFile);
            } else {
                if (StringUtils.isNotEmpty(gisShpPath)) {
                    path = gisShpPath + "/" + multipartFile.getOriginalFilename();
                } else {
                    path = multipartFile.getOriginalFilename();
                }
                //MultipartFile转File
                File otherFile = new File(path);
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), otherFile);
            }
        }

        /*新增发布*/
        Publish publish = savePublish(layerName, layerId, KV.builder().id(KvConstant.KV_RELEASE_COMPONENT).build());
        readComponentFileInfo(shpFile,publish,componentTypeId);
        return Result.success();
    }
//    private HttpResponse gisImport(String layerName, String layerSettingType, MultipartHttpServletRequest multiRequest, HttpClient httpclient) throws IOException {
//        // 创建集合接受文件
//        List<MultipartFile> fileList = multiRequest.getFiles("file");
//
//        HttpPost httpPost = new HttpPost(gisServiceProperties.getUrl() + "/urbanImport");
//        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
//
//        for (MultipartFile multipartFile : fileList) {
//            String filePath = fileUploadUtil.uploadFileToLocal(multipartFile);
//            reqEntity.addPart(multipartFile.getName(), new FileBody(new File(filePath)));
//        }
//        //接口常规参数开始-----------------------------------------
//        Map<String, String> paramMap = new HashMap<>();
//
//        paramMap.put("layerName", layerName);
//        paramMap.put("layerSettingType", layerSettingType);
//        StringBody paramBody = new StringBody(JsonUtils.objectToJson(paramMap));
//        //接口常规参数结束------------------------------------------------------
//
//        reqEntity.addPart("paramBody", paramBody);
//        HttpEntity httpEntity = reqEntity.build();
//        httpPost.setEntity(httpEntity);
//
//        return httpclient.execute(httpPost);
//    }

    /*新增部件*/
    private void saveComponent(Publish publish, Record record,ComponentInfo componentInfo,String componentTypeId) {
        Component component = new Component();
        component.setPublish(publish);
        component.setRecord(record);
        component.setComponentInfo(componentInfo);
        component.setCreateBy(SecurityUtil.getUser().castToUser());
        component.setCreateTime(LocalDateTime.now());
        EventType eventType = new EventType();
        eventType.setId(componentTypeId);
        component.setEventType(eventType);
        componentService.saveComponent4Import(component);
    }

    /*新增部件信息*/
    private ComponentInfo saveComponentInfo(String objId, String objName) {
        ComponentInfo componentInfo = new ComponentInfo();
        componentInfo.setObjId(objId);
        componentInfo.setObjName(objName);
        componentInfo.setCreateBy(SecurityUtil.getUser().castToUser());
        componentInfo.setCreateTime(LocalDateTime.now());
        return componentInfoService.save(componentInfo);
    }

    /*新增record*/
    private Record saveRecord(Publish publish, String coordinate) {
        Record record = new Record();
        record.setCoordinate(coordinate);
        record.setPublish(publish);
        record.setCreateTime(LocalDateTime.now());
        record.setCreateBy(SecurityUtil.getUser().castToUser());
        return recordService.save(record);
    }

    /*新增网格*/
    private void saveGrid(String layerName, Publish publish,Record record) {
        Grid grid = new Grid();
        grid.setGridName(layerName);
        grid.setPublish(publish);
        grid.setInitialDate(LocalDateTime.now());
        grid.setTerminationDate(LocalDateTime.now());
        grid.setLevel(4);
        grid.setRecord(record);
        List<User> userList = new ArrayList<>();
        userList.add(SecurityUtil.getUser().castToUser());
        grid.setUserList(userList);
        gridService.save4Import(grid);

    }

    /**
     * 新增发布
     *
     * @param layerName
     * @param layerId
     * @param kv
     */
    private Publish savePublish(String layerName, String layerId, KV kv) {
        Publish publish = new Publish();
        publish.setLayerId(layerId);
        publish.setKv(kv);
        publish.setName(layerName);
        publish.setUser(SecurityUtil.getUser().castToUser());
        return  releaseService.save(publish);
    }

    /**
     * 查询publish表中是有有网格
     *
     * @return
     */
    private boolean checkPublish() {
        boolean f = false;
        return f;
    }

    private boolean validImport(MultipartHttpServletRequest multiRequest) {
        List<MultipartFile> fileList = multiRequest.getFiles("file");
        boolean flag = false;
        for (MultipartFile multipartFile : fileList) {
            String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            if (TYPE_SHP.equals(suffix.toUpperCase())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 读取部件文件信息并创建部件
     * @param shpFile
     * @param publish
     * @throws IOException
     */
    private void readComponentFileInfo(File shpFile,Publish publish,String componentTypeId) throws IOException {
        ShapefileDataStore shapefileDataStore = new ShapefileDataStore(shpFile.toURI().toURL());
        shapefileDataStore.setCharset(StandardCharsets.UTF_8);
        ContentFeatureCollection featureCollection = shapefileDataStore.getFeatureSource().getFeatures();
        SimpleFeatureIterator iterator = featureCollection.features();
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            Collection<Property> properties = feature.getProperties();
            for (Property property : properties) {
                String name = property.getName().toString();
                String value = property.getValue() == null ? "" : property.getValue().toString();
                String objId="";
                String objName="";
                String coordinate = "";
//                if (name.equals("ObjName")) {
//                    objName = value;
//                }
//                if (name.equals("ObjID")) {
//                    objId = value;
//                }
                if (name.equals("the_geom")){
                    coordinate = value;
                    ComponentInfo componentInfo = saveComponentInfo(objId,objName);
                    Record record = saveRecord(publish,coordinate);
                    saveComponent(publish,record,componentInfo,componentTypeId);
                }

            }
        }
        iterator.close();
    }

    private void readGridFileInfo(String layerName,File shpFile,Publish publish) throws IOException {
        ShapefileDataStore shapefileDataStore = new ShapefileDataStore(shpFile.toURI().toURL());
        shapefileDataStore.setCharset(StandardCharsets.UTF_8);
        ContentFeatureCollection featureCollection = shapefileDataStore.getFeatureSource().getFeatures();
        SimpleFeatureIterator iterator = featureCollection.features();
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            Collection<Property> properties = feature.getProperties();
            for (Property property : properties) {
                String name = property.getName().toString();
                String value = property.getValue() == null ? "" : property.getValue().toString();
                String coordinate = "";

                if (name.equals("the_geom")){
                    coordinate = value;
                }
                Record record = saveRecord(publish,coordinate);
                saveGrid(layerName,publish,record);
            }
        }
        iterator.close();
    }
}
