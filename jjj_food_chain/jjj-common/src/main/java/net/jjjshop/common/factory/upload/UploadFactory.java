package net.jjjshop.common.factory.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 文件上传工厂
 */
@Component
public class UploadFactory {

    private static final String packageName = "net.jjjshop.common.factory.upload.impl.%sUploadServiceImpl";

    @Autowired
    private Map<String, UploadFactoryService> uploadMap;//关键在这个，原理：当一个接口有多个实现类的时候，key为实现类名，value为实现类对象

    public UploadFactoryService getService(String storageType) {
        String className = String.format(packageName, storageType);
        return uploadMap.get(className);
    }
}
