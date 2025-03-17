package net.jjjshop.common.service.settings;

import com.alibaba.fastjson.JSONArray;
import net.jjjshop.common.entity.settings.Region;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 *  服务类
 *
 * @author jjjshop
 * @since 2022-06-24
 */
public interface RegionService extends BaseService<Region> {
    /**
     * 转换前端使用地区格式
     * @return
     */
    List<JSONArray> getRegionForApi();
}
