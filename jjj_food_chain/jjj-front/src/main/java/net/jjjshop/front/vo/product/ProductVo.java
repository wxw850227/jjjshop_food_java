

package net.jjjshop.front.vo.product;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.vo.product.ProductImageVo;
import net.jjjshop.common.vo.product.ProductSkuVo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品详情VO")
public class ProductVo extends Product {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片")
    private List<ProductImageVo> image;

    @ApiModelProperty("详情图片")
    private List<ProductImageVo> contentImage;

    @ApiModelProperty("商品sku")
    private List<ProductSkuVo> skuList;

    @ApiModelProperty("商品sku")
    private ProductSkuVo productSku;

    @ApiModelProperty("商品销量")
    private Integer productSales;

    @ApiModelProperty("是否会员折扣")
    private Boolean isUserGrade;

    @ApiModelProperty("商品视频封面")
    private String videoFilePath;

    @ApiModelProperty("商品视频路径")
    private String posterFilePath;

    @ApiModelProperty("预告时间戳")
    private Long previewTimeStamp;

    //编辑使用
    @ApiModelProperty("商品属性列表")
    private JSONArray productAttrList;


    //编辑使用
    @ApiModelProperty("商品加料列表")
    private JSONArray productFeedList;
}
