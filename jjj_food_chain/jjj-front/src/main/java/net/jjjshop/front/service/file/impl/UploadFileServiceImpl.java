package net.jjjshop.front.service.file.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.file.UploadFile;
import net.jjjshop.common.mapper.file.UploadFileMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.file.UploadFileService;
import org.springframework.stereotype.Service;

/**
 * 文件库记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class UploadFileServiceImpl extends BaseServiceImpl<UploadFileMapper, UploadFile> implements UploadFileService {

    /**
     * 保存图片
     * @param file
     * @return
     */
    public boolean addFile(UploadFile file){
        return this.save(file);
    }
}
