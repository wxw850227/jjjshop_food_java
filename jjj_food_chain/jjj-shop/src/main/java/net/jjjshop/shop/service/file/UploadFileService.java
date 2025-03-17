package net.jjjshop.shop.service.file;

import net.jjjshop.common.entity.file.UploadFile;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.file.UploadFilePageParam;
import net.jjjshop.shop.vo.file.UploadFileVo;

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

    /**
     * 文件列表
     * @param uploadFilePageParam
     * @return
     */
    Paging<UploadFileVo> getList(UploadFilePageParam uploadFilePageParam);
}
