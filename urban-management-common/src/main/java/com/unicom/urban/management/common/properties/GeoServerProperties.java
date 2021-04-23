package com.unicom.urban.management.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * geoserver
 *
 * @author 顾志杰
 * @date 2020/12/21-19:41
 */
@Getter
@Setter
@Component
public class GeoServerProperties {

    @Value("${gis.geoserver.url}")
    private String url;

    @Value("${gis.geoserver.center_x}")
    private double center_x;

    @Value("${gis.geoserver.center_y}")
    private double center_y;

    @Value("${gis.geoserver.layers}")
    private String layers;

    @Value("${gis.geoserver.zoom}")
    private int zoom;

    @Value("${gis.geoserver.epsg}")
    private String epsg;

}
