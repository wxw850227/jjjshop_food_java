package net.jjjshop.common.factory.upload;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口类
 */
public interface UploadFactoryService {
    /**
     * 文件上传，返回文件路径
     * @return
     */
    void upload(MultipartFile multipartFile, String saveFileName) throws Exception;
}
