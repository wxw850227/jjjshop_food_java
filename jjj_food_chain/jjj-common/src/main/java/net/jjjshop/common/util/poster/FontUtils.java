package net.jjjshop.common.util.poster;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.awt.*;

@Slf4j
@Component
public class FontUtils {
    @Autowired
    private ResourceLoader resourceLoader;

    public Font loadFont(int fontSize) {
        try {
            String fontPath = "/static/image/poster/st-heiti-light.ttc";
            ClassPathResource resource = new ClassPathResource(fontPath);
            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, resource.getInputStream());
            Font dynamicFontPt = dynamicFont.deriveFont(Font.PLAIN, fontSize);
            resource.getInputStream().close();
            return dynamicFontPt;
        } catch (Exception e){
            log.info("获取字体文件异常{}",e.getMessage());
            return new java.awt.Font(null, Font.PLAIN, fontSize);
        }
    }
}
