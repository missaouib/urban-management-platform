package com.unicom.urban.management.common.properties;

import com.github.tobato.fastdfs.FdfsClientConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = FdfsClientConstants.ROOT_CONFIG_PREFIX)
public class FastProperties {

    private String nginxUrl;

}
