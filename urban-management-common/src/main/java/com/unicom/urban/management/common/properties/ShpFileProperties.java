package com.unicom.urban.management.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Configuration
@PropertySource("classpath:shpFile.properties")
@ConfigurationProperties(prefix ="file.shp")
public class ShpFileProperties {

    private String path;

}
