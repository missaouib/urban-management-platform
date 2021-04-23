package com.unicom.urban.management.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * gisService 接口配置
 *
 * @author liukai
 */
@Getter
@Setter
@Component
public class GisServiceProperties {

    @Value("${gis.api.url}")
    private String url;


}
