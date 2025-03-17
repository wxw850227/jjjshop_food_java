package net.jjjshop.common.factory.upload.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.common.factory.upload.UploadFactoryService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * 文件本地上传类
 */
@Slf4j
@Service
public class LocalUploadServiceImpl implements UploadFactoryService {
    @Lazy
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    /**
     * 文件上传，返回文件路径
     * @return
     */
    public void upload(MultipartFile multipartFile, String saveFileName) throws Exception{
        // 获取输入流
        InputStream inputStream = multipartFile.getInputStream();
        // 文件保存目录
        File saveDir = new File(springBootJjjProperties.getUploadPath());
        // 判断目录是否存在，不存在，则创建，如创建失败，则抛出异常
        if (!saveDir.exists()) {
            boolean flag = saveDir.mkdirs();
            if (!flag) {
                throw new RuntimeException("创建" + saveDir + "目录失败！");
            }
        }

        File saveFile = new File(saveDir, saveFileName);
        // 保存文件到服务器指定路径
        FileUtils.copyToFile(inputStream, saveFile);
    }

    /**
     * 删除删除的文件
     *
     * @param uploadPath
     * @param saveFileName
     */
    public void deleteQuietly(String uploadPath, String saveFileName) {
        File saveDir = new File(springBootJjjProperties.getUploadPath());
        File saveFile = new File(saveDir, saveFileName);
        log.debug("删除文件：" + saveFile);
        FileUtils.deleteQuietly(saveFile);
    }
}
