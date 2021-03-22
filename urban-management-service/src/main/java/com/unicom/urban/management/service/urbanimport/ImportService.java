package com.unicom.urban.management.service.urbanimport;

import com.unicom.urban.management.common.properties.GisServiceProperties;
import com.unicom.urban.management.common.util.JsonUtils;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.service.publish.PublishService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/* 导入service
 *
 * @author liubozhi
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ImportService {
    @Autowired
    private GisServiceProperties gisServiceProperties;
    @Autowired
    private PublishService publishService;

    public String toGisGetLayerId(String layerName, String coordinate,String shpType) {

        // 创建集合接受文件
        HttpClient httpclient = new DefaultHttpClient();
        String layerId = "";

        try {
            HttpPost httpPost = new HttpPost(gisServiceProperties.getUrl() + "/urbanImport");
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();

            //接口常规参数开始-----------------------------------------
            Map<String, String> paramMap = new HashMap<>();

            paramMap.put("layerName", layerName);
            paramMap.put("layerSettingType", "1");
            paramMap.put("coordinate", coordinate);
            paramMap.put("shpType", shpType);
            StringBody paramBody = new StringBody(JsonUtils.objectToJson(paramMap));
            //接口常规参数结束------------------------------------------------------

            reqEntity.addPart("paramBody", paramBody);
            HttpEntity httpEntity = reqEntity.build();
            httpPost.setEntity(httpEntity);

            HttpResponse response = httpclient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                byte[] b = new byte[40];
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
        return layerId;
    }

    /**
     * 读取数据类型
     * @param contentFeatureCollection
     * @return
     */
    public String getShpType(ContentFeatureCollection contentFeatureCollection) {
        try {
            SimpleFeatureIterator features =  contentFeatureCollection.features();
            while (features.hasNext()) {
                SimpleFeature next = features.next();

                //坐标系转换
                Geometry geometry = (Geometry) next.getDefaultGeometry();
                // Point MultiPoint Polygon MultiPolygon LineString  MultiLineString
                return geometry.getGeometryType();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkPublish(String name) {
        return publishService.findByName(name);
    }
}
