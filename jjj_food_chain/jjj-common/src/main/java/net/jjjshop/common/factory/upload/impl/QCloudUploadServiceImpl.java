package net.jjjshop.common.factory.upload.impl;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
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
 * 文件腾讯云上传类
 */
@Slf4j
@Service
public class QCloudUploadServiceImpl implements UploadFactoryService {
    @Lazy
    @Autowired
    private SettingUtils settingUtils;
    /**
     * 文件上传，返回文件路径
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

        COSCredentials cred = new BasicCOSCredentials(storageVo.getQCloud().getSecretId(), storageVo.getQCloud().getSecretKey());
        Region region = new Region(storageVo.getQCloud().getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);

        PutObjectRequest putObjectRequest = new PutObjectRequest(storageVo.getQCloud().getBucket(), saveFileName, multipartFile.getInputStream(), null);
        cosClient.putObject(putObjectRequest);
    }
}
