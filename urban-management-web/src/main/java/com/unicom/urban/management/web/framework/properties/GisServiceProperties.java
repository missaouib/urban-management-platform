package com.unicom.urban.management.web.framework.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * gisService 接口配置
 *
 * @author liukai
 */
@Component
@PropertySource("classpath:gisServer.properties")
@ConfigurationProperties(prefix = "gis.api")
@Getter
@Setter
public class GisServiceProperties {

    private String url;


}
