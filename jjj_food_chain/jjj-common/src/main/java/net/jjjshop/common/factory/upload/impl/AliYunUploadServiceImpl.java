package net.jjjshop.common.factory.upload.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.upload.UploadFactoryService;
import net.jjjshop.common.settings.vo.StorageVo;
import net.jjjshop.common.util.SettingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件七牛上传类
 */
@Slf4j
@Service
public class AliYunUploadServiceImpl implements UploadFactoryService {
    @Lazy
    @Autowired
    private SettingUtils settingUtils;
    /**
     * 阿里云OSS文件上传，返回文件路径
     * @return
     */
    public void upload(MultipartFile multipartFile, String saveFileName) throws Exception{
        StorageVo storageVo;
        // 获取当前的HttpServletRequest对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 请求路径 /api/admin/file/upload/image
        String path = request.getRequestURI();
        if(path.startsWith("/api/admin/file/upload/image")){
            //获取admin端配置
            JSONObject vo = settingUtils.getSetting(SettingEnum.SYS_CONFIG.getKey(), 0L);
            JSONObject storage = vo.getJSONObject("storageVo");
            storageVo = JSONObject.toJavaObject(storage, StorageVo.class);
        }else {
            //获取shop端配置
            JSONObject vo = settingUtils.getSetting(SettingEnum.STORAGE.getKey(), null);
            storageVo = JSONObject.toJavaObject(vo, StorageVo.class);
        }

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(storageVo.getAliYun().getEndpoint(),
                storageVo.getAliYun().getAccessKeyId(), storageVo.getAliYun().getAccessKeySecret());

        // 上传文件流。
        ossClient.putObject(storageVo.getAliYun().getBucket(), saveFileName, multipartFile.getInputStream());

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
