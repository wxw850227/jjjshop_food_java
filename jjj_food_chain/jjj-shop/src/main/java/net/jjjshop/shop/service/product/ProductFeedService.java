package net.jjjshop.shop.service.product;

import net.jjjshop.common.entity.product.ProductFeed;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.common.param.product.FeedParam;

/**
 * 加料库 服务类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
public interface ProductFeedService extends BaseService<ProductFeed> {

    Paging<ProductFeed> getList(CommonPageParam param);

    boolean add(FeedParam param);

    boolean edit(FeedParam param);

    boolean delById(Integer feedId);

    boolean deletes(String feedIds);
}
