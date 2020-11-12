package com.unicom.urban.management.common.configurer.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Activiti流程引擎配置类
 *
 * @author liukai
 */
@Slf4j
@Configuration
public class ActivitiProcessEngineConfiguration {

    private static final String FONT_PATH_WQY = "fonts/Arial Unicode.ttf";
    private static final String FONT_NAME = "宋体";


    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        StrongUuidGenerator uuidGenerator = new StrongUuidGenerator();
        processEngineConfiguration.setIdGenerator(uuidGenerator);
        processEngineConfiguration.getDbSqlSessionFactory().setIdGenerator(uuidGenerator);

        /**
         * 为解决diagram中文乱码问题
         */
        Font font = null;
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(FONT_PATH_WQY)) {
            assert resourceAsStream != null;
            font = Font.createFont(Font.TRUETYPE_FONT, resourceAsStream);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        assert font != null;


        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);

        String fontName = font.getFontName();

        System.out.println("fontName = " + fontName);

        processEngineConfiguration.setLabelFontName(fontName);
        processEngineConfiguration.setActivityFontName(fontName);
        processEngineConfiguration.setAnnotationFontName(fontName);
        return processEngineConfiguration;
    }

}
