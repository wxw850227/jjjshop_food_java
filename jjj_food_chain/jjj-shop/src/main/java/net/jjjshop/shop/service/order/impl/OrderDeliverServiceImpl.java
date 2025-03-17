package net.jjjshop.shop.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderDeliver;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.DeliverySourceEnum;
import net.jjjshop.common.mapper.order.OrderDeliverMapper;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.excel.ExcelUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.service.order.OrderDeliverService;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.supplier.SupplierService;
import net.jjjshop.shop.vo.order.ExportDeliverVo;
import net.jjjshop.shop.vo.order.OrderDeliverVo;
import net.jjjshop.shop.vo.order.OrderVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 外卖配送订单管理 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class OrderDeliverServiceImpl extends BaseServiceImpl<OrderDeliverMapper, OrderDeliver> implements OrderDeliverService {

    @Autowired
    private OrderDeliverMapper orderDeliverMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private OrderUtils orderUtils;

    @Override
    public Paging<OrderDeliverVo> getList(OrderPageParam orderPageParam) {
        Page<OrderDeliver> page = new PageInfo<>(orderPageParam);
        LambdaQueryWrapper<OrderDeliver> wrapper = new LambdaQueryWrapper<>();
        wrapper = this.transWrapper(wrapper, orderPageParam);
        IPage<OrderDeliver> iPage = this.page(page, wrapper);
        IPage<OrderDeliverVo> resultPage = iPage.convert(this::transVo);
        return new Paging(resultPage);
    }

    @Override
    public Map<String,Object> detail(Integer deliveId) {
        Map<String,Object> result = new HashMap<>();
        OrderDeliver item = this.getById(deliveId);
        result.put("deliver",transVo(item));
        OrderVo order = orderService.detail(item.getOrderId());
        result.put("detail",order);
        return result;
    }

    public OrderDeliverVo transVo(OrderDeliver item){
        OrderDeliverVo vo = new OrderDeliverVo();
        BeanUtil.copyProperties(item, vo);
        OrderVo order = orderService.detail(vo.getOrderId());
        vo.setOrders(order);
        //付款时间
        vo.setPayTime(order.getPayTime());
        //发货时间
        vo.setDeliveryTime(order.getDeliveryTime());
        //买家留言
        vo.setBuyerRemark(order.getBuyerRemark());
        vo.setSupplier(supplierService.getById(vo.getShopSupplierId()));
        vo.setSupplier(supplierService.getById(vo.getShopSupplierId()));
        //配送公司(10商家配送20达达30配送员)
        vo.setDeliverSourceText(DeliverySourceEnum.getName(vo.getDeliverSource()));
        //配送状态(待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5)
        vo.setDeliverStatusText(vo.getDeliverStatus() == 1?"待接单":vo.getDeliverStatus() == 2?"待取货":
                vo.getDeliverStatus() == 3?"配送中":vo.getDeliverStatus() == 4?"已完成":"已取消");
        //订单状态(10进行中 20被取消 30已完成)
        vo.setStatusText(vo.getStatus()==10?"进行中":vo.getStatus()==20?"被取消":"已完成");
        vo.setPayPrice(order.getPayPrice());
        return vo;
    }

    //确认送达
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verify(Integer deliveId) {
        OrderDeliver orderDeliver = this.getById(deliveId);
        Order order = orderService.getById(orderDeliver.getOrderId());
        //订单状态10=>进行中，20=>已经取消，30=>已完成
        if (order.getOrderStatus() == 20 || orderDeliver.getStatus() != 10) {
            throw new BusinessException("该订单不满足确认条件");
        }
        //更新配送状态
        //配送状态(待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5)
        orderDeliver.setDeliverStatus(4);
        //配送时间
        orderDeliver.setDeliverTime(new Date());
        //订单状态(10进行中 20被取消 30已完成)
        orderDeliver.setStatus(30);
        this.updateById(orderDeliver);

        // 更新订单状态：已发货、已收货
        //发货状态(10未发货 20已发货)
        order.setDeliveryStatus(20);
        order.setDeliveryTime(new Date());
        //收货状态(10未收货 20已收货)
        order.setReceiptStatus(20);
        //订单状态10=>进行中，20=>已经取消，30=>已完成
        order.setOrderStatus(30);
        orderService.updateById(order);
        //完成订单完成后流程
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderUtils.complete(orderList, order.getAppId());
        return true;
    }

    //取消配送
    @Override
    public boolean cancel(Integer deliveId) {
        OrderDeliver orderDeliver = this.getById(deliveId);
        Order order = orderService.getById(orderDeliver.getOrderId());
        //订单状态10=>进行中，20=>已经取消，30=>已完成
        if (orderDeliver.getDeliverStatus() == 5 || orderDeliver.getDeliverStatus() == 4
                || orderDeliver.getStatus() != 10) {
            throw new BusinessException("该订单不满足取消条件");
        }
        if(orderDeliver.getDeliverSource() == 20){

        }

        //更新配送状态
        //配送状态(待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5)
        orderDeliver.setDeliverStatus(5);
        //订单状态(10进行中 20被取消 30已完成)
        orderDeliver.setStatus(20);
        this.updateById(orderDeliver);

        // 更新订单状态：已发货、已收货
        //发货状态(10未发货 20已发货)
        order.setDeliveryStatus(10);
        //配送状态，待接单＝1,待取货＝2,配送中＝3,已完成＝4
        order.setDeliverStatus(0);
        orderService.updateById(order);
        return true;
    }

    @Override
    public void exportList(OrderPageParam orderPageParam, HttpServletResponse httpServletResponse) {
        LambdaQueryWrapper<OrderDeliver> wrapper = new LambdaQueryWrapper<>();
        wrapper = this.transWrapper(wrapper, orderPageParam);
        List<OrderDeliver> list = this.list(wrapper);
        List<OrderDeliverVo> voList = list.stream().map(this::transVo).collect(Collectors.toList());
        //构建数据
        List<ExportDeliverVo> exportLists = voList.stream().map(this::tranExportsVo).collect(Collectors.toList());
        ExcelUtils.export(httpServletResponse, "配送订单表", exportLists, ExportDeliverVo.class);
    }

    public ExportDeliverVo tranExportsVo(OrderDeliverVo item){
        ExportDeliverVo vo = new ExportDeliverVo();
        BeanUtil.copyProperties(item, vo);
        return vo;
    }

    /**
     * 获取不同状态的订单
     * @param wrapper
     * @return
     */
    private LambdaQueryWrapper<OrderDeliver> transWrapper(LambdaQueryWrapper<OrderDeliver> wrapper,OrderPageParam orderPageParam) {
        //门店ID
        if(orderPageParam.getShopSupplierId() != null && orderPageParam.getShopSupplierId() > 0){
            wrapper.eq(OrderDeliver::getShopSupplierId,orderPageParam.getShopSupplierId());
        }
        wrapper.orderByDesc(OrderDeliver::getCreateTime);
        //查询参数：开始时间
        if (StringUtils.isNotEmpty(orderPageParam.getStartDate())) {
            Date startTime = DateUtil.parse(orderPageParam.getStartDate() + " 00:00:00");
            wrapper.ge(OrderDeliver::getCreateTime, startTime);
        }
        //查询参数：结束时间
        if (StringUtils.isNotEmpty(orderPageParam.getEndDate())) {
            Date endTime = DateUtil.parse(orderPageParam.getEndDate() + " 23:59:59");
            wrapper.le(OrderDeliver::getCreateTime, endTime);
        }
        if(CollectionUtils.isNotEmpty(orderPageParam.getCreateTime()) && orderPageParam.getCreateTime().size() == 2){
            wrapper.between(OrderDeliver::getCreateTime, orderPageParam.getCreateTime().get(0) + " 00:00:00",
                    orderPageParam.getCreateTime().get(1) + " 23:59:59");
        }
        //查询参数：订单号
        if (StringUtils.isNotEmpty(orderPageParam.getOrderNo())) {
            wrapper.like(OrderDeliver::getOrderNo, orderPageParam.getOrderNo());
        }
        return wrapper;
    }
}
