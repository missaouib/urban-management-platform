package com.unicom.urban.management.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class FastProperties {

    @Value("${fdfs.nginx-url}")
    private String nginxUrl;

}
