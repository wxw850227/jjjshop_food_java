package net.jjjshop.front.util.order;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.OrderSourceEnum;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.vo.product.ProductBuyVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 普通订单结算
 */
@Slf4j
@Component
@Scope("prototype")
public class MasterOrderSettledUtils extends OrderSettledUtils {

    @Autowired
    private OrderService orderService;

    public Map<String, Object> settlement(User user, List<ProductBuyVo> productList, OrderBuyParam orderBuyParam){
        super.init();
        // 差异化规则
        this.orderSource.setSource(OrderSourceEnum.MASTER.getValue());
        return super.settlement(user, productList, orderBuyParam);
    }

    public void validateProductList(){
        for(ProductBuyVo product:this.productList){
            // 判断商品是否下架
            if(product.getProductStatus() != 10){
                throw new BusinessException(String.format("很抱歉，商品 [%s] 已下架", product.getProductName()));
            }
            // 判断商品库存
            if(product.getProductNum() > product.getSku().getStockNum()){
                throw new BusinessException(String.format("很抱歉，商品 [%s] 库存不足", product.getProductName()));
            }
            // 是否是会员商品
            if(StringUtils.isNotEmpty(product.getGradeIds())){
                String[] gradeIdArr = product.getGradeIds().split(",");
                if(!Arrays.asList(gradeIdArr).contains("" + this.user.getGradeId())){
                    throw new BusinessException("很抱歉，此商品仅特定会员可购买");
                }
            }
            // 判断是否超过限购数量
            if(product.getLimitNum() > 0){
                Integer hasNum = orderService.getHasBuyOrderNum(this.user.getUserId(), product.getProductId());
                if(hasNum + product.getProductNum() > product.getLimitNum()){
                    throw new BusinessException("很抱歉，购买超过此商品最大限购数量");
                }
            }
        }
    }
}
