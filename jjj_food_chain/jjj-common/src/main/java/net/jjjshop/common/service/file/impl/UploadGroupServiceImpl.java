package net.jjjshop.common.service.file.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.file.UploadGroup;
import net.jjjshop.common.mapper.file.UploadGroupMapper;
import net.jjjshop.common.service.file.UploadGroupService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 文件库分组记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class UploadGroupServiceImpl extends BaseServiceImpl<UploadGroupMapper, UploadGroup> implements UploadGroupService {

}
