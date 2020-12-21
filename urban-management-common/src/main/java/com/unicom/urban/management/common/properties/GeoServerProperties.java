package com.unicom.urban.management.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:geoServer.properties")
@ConfigurationProperties(prefix = "gis.geoserver")
public class GeoServerProperties {

    private String url;

    private double center_x;

    private double center_y;

    private String layers;

    private int zoom;

    private String epsg;


}
