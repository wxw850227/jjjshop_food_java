package net.jjjshop.common.factory.upload.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.StorageVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.factory.upload.UploadFactoryService;
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
public class QiNiuUploadServiceImpl implements UploadFactoryService {
    @Lazy
    @Autowired
    private SettingUtils settingUtils;
    /**
     * 文件上传，返回文件路径
     * @return
     */
    public void upload(MultipartFile multipartFile, String saveFileName) throws Exception{
        Configuration cfg = new Configuration(Region.autoRegion());

        StorageVo storageVo;
        // 获取当前的HttpServletRequest对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 请求路径 /api/admin/file/upload/image
        String path = request.getRequestURI();
        if(path.startsWith("/api/admin/file/upload/image")){
            //获取admin端配置
            JSONObject vo = settingUtils.getShopSetting(SettingEnum.SYS_CONFIG.getKey(), 0L);
            JSONObject storage = vo.getJSONObject("storageVo");
            storageVo = JSONObject.toJavaObject(storage, StorageVo.class);
        }else {
            //获取shop端配置
            JSONObject vo = settingUtils.getShopSetting(SettingEnum.STORAGE.getKey(), null);
            storageVo = JSONObject.toJavaObject(vo, StorageVo.class);
        }

        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(storageVo.getQiNiu().getAccessKey(), storageVo.getQiNiu().getSecretKey());
        String upToken = auth.uploadToken(storageVo.getQiNiu().getBucket());

        Response response = uploadManager.put(multipartFile.getBytes(), saveFileName, upToken);
        response.close();
    }
}
