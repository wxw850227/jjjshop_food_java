package net.jjjshop.shop.mapper.statistics;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.vo.statistics.ProductSaleRankingVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRankingMapper extends BaseMapper {

    /**
     * 获取商品销售排行
     * @param
     * @return
     */
    List<ProductSaleRankingVo> getSaleRanking();

    List<ProductSaleRankingVo> getSaleNumRank(OrderPageParam param);

    List<ProductSaleRankingVo> getSaleMoneyRank(OrderPageParam param);

    JSONObject getDataDetail(OrderPageParam param);
}
