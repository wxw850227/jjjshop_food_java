package net.jjjshop.front.service.file;

import net.jjjshop.common.entity.file.UploadFile;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 文件库记录表 服务类
 * @author jjjshop
 * @since 2022-06-28
 */
public interface UploadFileService extends BaseService<UploadFile> {

    /**
     * 保存文件
     * @param file
     * @return
     */
    boolean addFile(UploadFile file);
}
