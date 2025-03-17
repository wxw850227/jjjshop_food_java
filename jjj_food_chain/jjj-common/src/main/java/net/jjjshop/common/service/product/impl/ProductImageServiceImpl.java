package net.jjjshop.common.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductImage;
import net.jjjshop.common.mapper.product.ProductImageMapper;
import net.jjjshop.common.service.product.ProductImageService;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.product.ProductImageVo;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品图片记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductImageServiceImpl extends BaseServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {

    @Autowired
    private UploadFileUtils uploadFileUtils;

    /**
     * 根据id和图片类型获取图片
     * @param productId  商品id
     * @param imageType  图片类型
     * @return
     */
    public List<ProductImageVo> getListByProductId(Integer productId, Integer imageType){
        List<ProductImage> imageList = this.list(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, productId)
                .orderByAsc(ProductImage::getId));
        return imageList.stream().map(e -> {
            ProductImageVo productImageVo = new ProductImageVo();
            BeanUtils.copyProperties(e, productImageVo);
            productImageVo.setFilePath(uploadFileUtils.getFilePath(e.getImageId()));
            return productImageVo;
        }).collect(Collectors.toList());
    }
}
