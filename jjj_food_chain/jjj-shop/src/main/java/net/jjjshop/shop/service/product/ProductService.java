package net.jjjshop.shop.service.product;

import net.jjjshop.common.entity.product.Product;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.param.product.ProductPageParam;
import net.jjjshop.common.param.product.ProductParam;
import net.jjjshop.shop.vo.product.ProductVo;

import java.util.List;
import java.util.Map;

/**
 * 商品记录表 服务类
 * @author jjjshop
 * @since 2022-06-28
 */
public interface ProductService extends BaseService<Product> {
    /**
     * 商品列表
     * @param productPageParam
     * @return
     */
    Map<String,Object> getList(ProductPageParam productPageParam);

    /**
     * 新增商品
     * @param productParam
     * @return
     */
    boolean add(ProductParam productParam);

    /**
     * 获取新增或修改数据
     * @param productId
     * @param scene
     * @return
     */
    Map<String, Object> getBaseData(Integer productId, String scene,Integer type);

    /**
     * 修改商品
     * @param productParam
     * @return
     */
    boolean edit(ProductParam productParam);

    /**
     * 修改商品状态
     * @param productId
     * @param state
     * @return
     */
    boolean setState(Integer productId, Integer state);

    /**
     * 商品删除
     * @param productId
     * @return
     */
    boolean setDelete(Integer productId);
    /**
     * 商品列表
     * @param productIds
     * @return
     */
    List<ProductVo> getListByIds(List<Integer> productIds);
    /**
     * 所有商品列表
     * @param
     * @return
     */
    List<ProductVo> getAll();
}
