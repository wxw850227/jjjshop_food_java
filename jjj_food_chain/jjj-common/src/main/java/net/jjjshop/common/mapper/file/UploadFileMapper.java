package net.jjjshop.common.mapper.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.file.UploadFile;

import org.springframework.stereotype.Repository;


/**
 * 文件库记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Repository
public interface UploadFileMapper extends BaseMapper<UploadFile> {


}
