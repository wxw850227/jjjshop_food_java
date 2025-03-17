package net.jjjshop.common.vo.product;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.Product;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品分类VO")
public class ProductVo extends Product {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片主图")
    private String imagePath;

    @ApiModelProperty("商品分类名称")
    private String categoryName;

    //编辑使用
    @ApiModelProperty("商品单独折扣")
    private JSONObject aloneGradeEquityJson;

    //编辑使用
    @ApiModelProperty("商品sku")
    private List<ProductSkuVo> sku;

    //skuList
    @ApiModelProperty("商品sku")
    List<JSONObject> specList;

    //编辑使用
    @ApiModelProperty("商品图片集合")
    private List<ProductImageVo> image;
}
