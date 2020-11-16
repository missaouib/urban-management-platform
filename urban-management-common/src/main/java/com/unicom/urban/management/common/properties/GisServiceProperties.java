package com.unicom.urban.management.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * gisService 接口配置
 *
 * @author liukai
 */
@Getter
@Setter
@Component
@Configuration
@PropertySource("classpath:gisServer.properties")
@ConfigurationProperties(prefix = "gis.api")
public class GisServiceProperties {

    private String url;


}
