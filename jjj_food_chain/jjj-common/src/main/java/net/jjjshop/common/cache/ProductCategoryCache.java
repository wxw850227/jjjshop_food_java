package net.jjjshop.common.cache;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.service.product.ProductCategoryService;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.product.CategoryVo;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductCategoryCache {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    /**
     * 清理缓存
     */
    public void clearCache(){
        redisTemplate.delete(String.format(CommonRedisKey.PRODUCT_CATEGORY_DATA, RequestDetailThreadLocal.getRequestDetail().getAppId()));
    }

    /**
     * 获取所有分类,暂时注释
     * @return
     */
    public List<CategoryVo> getCache(){
        List<ProductCategory> list = productCategoryService.list(new LambdaQueryWrapper<ProductCategory>()
                .orderByAsc(ProductCategory::getSort).orderByAsc(ProductCategory::getCreateTime));
        List<CategoryVo> voList = this.transTree(list);
        return voList;
    }

    /**
     * 组装成树形
     * @param list
     * @return
     */
    private List<CategoryVo> transTree(List<ProductCategory> list){
        // 转成vo
        List<CategoryVo> voList = list.stream().map(e -> {
            CategoryVo vo = new CategoryVo();
            BeanUtils.copyProperties(e, vo);
            vo.setImagePath(uploadFileUtils.getFilePath(vo.getImageId()));
            return vo;
        }).collect(Collectors.toList());
        // 遍历成树形结构
        List<CategoryVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        return collect;
    }

    // 根据当前父类 找出子类， 并通过递归找出子类的子类
    private List<CategoryVo> getChildList(ProductCategory bean, List<CategoryVo> voList) {
        List<CategoryVo> list = voList.stream()
                //筛选出父节点id == parentId 的所有对象 => list
                .filter(o -> StrUtil.equals(String.valueOf(bean.getCategoryId()), String.valueOf(o.getParentId())))
                .peek(o -> o.setChildren(getChildList(o, voList)))
                .collect(Collectors.toList());
        if(list.size() == 0){
            return null;
        }
        return list;
    }

    /**
     * 查找子类
     * @return
     */
    public List<Integer> getSubCategoryId(Integer categoryId){
        List<Integer> ids = new ArrayList<>();
        ids.add(categoryId);
        List<CategoryVo> voList = this.getCache();
        voList.forEach(item -> {
            if(item.getCategoryId().intValue() == categoryId.intValue()){
                // 子类
                if(item.getChildren() != null){
                    item.getChildren().forEach(child ->{
                        ids.add(child.getCategoryId());
                    });
                }
            }
        });
        return ids;
    }
}
