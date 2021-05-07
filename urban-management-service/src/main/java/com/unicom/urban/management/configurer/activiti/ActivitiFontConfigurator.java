package com.unicom.urban.management.configurer.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.cfg.AbstractProcessEngineConfigurator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * 为解决diagram中文乱码问题
 *
 * @author liukai
 */
@Slf4j
public class ActivitiFontConfigurator extends AbstractProcessEngineConfigurator {

    private static final String FONT_PATH_WQY = "fonts/Arial Unicode.ttf";

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void beforeInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
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
        processEngineConfiguration.setLabelFontName(fontName);
        processEngineConfiguration.setActivityFontName(fontName);
        processEngineConfiguration.setAnnotationFontName(fontName);
    }

    @Override
    public void configure(ProcessEngineConfigurationImpl processEngineConfiguration) {
        log.debug("Activiti使用字体: {}", processEngineConfiguration.getActivityFontName());
    }
}
