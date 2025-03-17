package net.jjjshop.common.cache;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Region;
import net.jjjshop.common.mapper.settings.RegionMapper;
import net.jjjshop.common.vo.RegionVo;
import net.jjjshop.config.constant.CommonRedisKey;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RegionCache {
    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public List<RegionVo> getCacheTree(){
        Map<String,Object> map = getCache();
        return (List<RegionVo>)map.get("tree");
    }

    public List<RegionVo> getCacheAll(){
        Map<String,Object> map = getCache();
        return (List<RegionVo>)map.get("all");
    }

    public Integer getCacheCount(String type){
        Map<String,Object> map = getCache();
        if(type.equals("city")){
            return (Integer)map.get("cityCount");
        }
        return 0;
    }

    /**
     * 清理缓存
     */
    public void clearCache(){
        redisTemplate.delete(CommonRedisKey.getRedisKey(CommonRedisKey.REGION_DATA));
    }

    public Map<String,Object> getCache(){
        Object object = redisTemplate.opsForValue().get(CommonRedisKey.getRedisKey(CommonRedisKey.REGION_DATA));
        if(object != null){
            return (HashMap<String,Object>)object;
        }
        // 所有地区
        List<Region> allList = regionMapper.selectList(new LambdaQueryWrapper<Region>());
        Map<String, Object> result = new HashMap<String, Object>();
        // 树形结构
        // 转成vo
        List<RegionVo> voList = allList.stream().map(e -> {
            RegionVo regionVo = new RegionVo();
            BeanUtils.copyProperties(e, regionVo);
            return regionVo;
        }).collect(Collectors.toList());
        result.put("all", voList);
        // 遍历成树形结构
        List<RegionVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getPid())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        result.put("tree", collect);
        // 地区总数
        result.put("total", allList.size());
        // 省份总数
        result.put("provinceCount", this.getAreaCount(allList, 1));
        result.put("cityCount", this.getAreaCount(allList, 2));
        result.put("areaCount", this.getAreaCount(allList, 3));
        redisTemplate.opsForValue().set(CommonRedisKey.getRedisKey(CommonRedisKey.REGION_DATA), result);
        return result;
    }



    // 根据当前父类 找出子类， 并通过递归找出子类的子类
    private List<RegionVo> getChildList(RegionVo bean, List<RegionVo> voList) {
        return voList.stream()
                //筛选出父节点id == parentId 的所有对象 => list
                .filter(o -> StrUtil.equals(String.valueOf(bean.getId()), String.valueOf(o.getPid())))
                .peek(o -> o.setChildren(getChildList(o, voList)))
                .collect(Collectors.toList());
    }

    /**
     * 查找地区数量
     * @param allList
     * @param level
     * @return
     */
    private Integer getAreaCount(List<Region> allList, int level){
        Integer count = 0;
        for (Region item : allList) {
            if(item.getLevel().intValue() == level){
                count++;
            }
        }
        return count;
    }
}
