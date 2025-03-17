package net.jjjshop.shop.service.statistics.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.shop.mapper.statistics.ProductRankingMapper;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.service.order.OrderProductService;
import net.jjjshop.shop.service.product.ProductService;
import net.jjjshop.shop.service.statistics.ProductRankingService;
import net.jjjshop.shop.vo.product.ProductVo;
import net.jjjshop.shop.vo.statistics.ProductSaleRankingVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 商品统计数据 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */

@Slf4j
@Service
public class ProductRankingServiceImpl implements ProductRankingService {

    @Autowired
    private ProductRankingMapper productRankingMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private OrderProductService orderProductService;

    /**
     * 获取商品销售榜单
     * @param
     * @return
     */
    @Override
    public List<ProductSaleRankingVo> getSaleRanking() {
        List<ProductSaleRankingVo> result = productRankingMapper.getSaleRanking();
        //去重
        result = result.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProductSaleRankingVo::getProductId))), ArrayList::new));
        result.stream().forEach(e -> {
            e.setImagePath(uploadFileUtils.getImagePathByProductId(e.getProductId()));
            e.setViewTimes(productService.getById(e.getProductId()).getViewTimes());
        });
        return result;
    }

    //销量排行榜
    @Override
    public List<ProductSaleRankingVo> getSaleNumRank(OrderPageParam param) {
        List<ProductSaleRankingVo> result = productRankingMapper.getSaleNumRank(param);
        //去重
        result = result.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProductSaleRankingVo::getProductId))), ArrayList::new));
        result.stream().forEach(e -> {
            e.setImagePath(uploadFileUtils.getImagePathByProductId(e.getProductId()));
        });
        result = result.stream().sorted(Comparator.comparing(ProductSaleRankingVo::getTotalNum).reversed()).collect(Collectors.toList());
        return result;
    }

    //销售额排行榜
    @Override
    public List<ProductSaleRankingVo> getSaleMoneyRank(OrderPageParam param) {
        List<ProductSaleRankingVo> result = productRankingMapper.getSaleMoneyRank(param);
        //去重
        result = result.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProductSaleRankingVo::getProductId))), ArrayList::new));
        result.stream().forEach(e -> {
            e.setImagePath(uploadFileUtils.getImagePathByProductId(e.getProductId()));
            e.setViewTimes(productService.getById(e.getProductId()).getViewTimes());
        });
        result = result.stream().sorted(Comparator.comparing(ProductSaleRankingVo::getTotalPrice).reversed()).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取商品浏览榜单
     * @param
     * @return
     */
    @Override
    public List<ProductVo> getViewsRanking() {
        List<Product> list = productService.list(new LambdaQueryWrapper<Product>()
                .gt(Product::getViewTimes, 0)
                .orderByDesc(Product::getViewTimes)
                .last("LIMIT 0,10"));
        List<ProductVo> result = list.stream().map(e -> {
            ProductVo vo = new ProductVo();
            BeanUtils.copyProperties(e, vo);
            vo.setImagePath(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取商品总数
     * @param
     * @return
     */
    @Override
    public Integer getProductTotal() {
        return productService.count(new LambdaQueryWrapper<Product>()
                .eq(Product::getIsDelete, 0)
        );
    }

    /**
     * 获取商品库存总数
     * @param
     * @return
     */
    @Override
    public Integer getProductStockTotal(Integer productType) {
        return productService.count(new LambdaQueryWrapper<Product>()
                .eq(Product::getIsDelete, 0)
                //产品状态(10上架 20下架 )"
                .eq(Product::getProductStatus, 10)
                .eq(Product::getProductType, productType)
                .lt(Product::getProductStock, 10));
    }


}
