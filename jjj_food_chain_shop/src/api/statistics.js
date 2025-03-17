import request from '@/utils/request'

let StatisticsApi = {
  /*订单数据统计*/
  getOrderTotal(data, errorback) {
    return request._post('/shop/statistics/sales/index', data, errorback);
  },
  /*订单时间段统计*/
  getOrderByDate(data, errorback) {
    return request._postBody('/shop/statistics/sales/order', data, errorback);
  },
  /*商品时间段统计*/
  getProductByDate(data, errorback) {
    return request._postBody('/shop/statistics/sales/product', data, errorback);
  },
  /*会员数据统计*/
  getUserTotal(data, errorback) {
    return request._postBody('/shop/statistics/user/index', data, errorback);
  },
  /*成交占比*/
  getUserScale(data, errorback) {
    return request._post('/shop/statistics/user/scale', data, errorback);
  },
  /*新增会员*/
  getNewUser(data, errorback) {
    return request._postBody('/shop/statistics/user/new_user', data, errorback);
  },
  /*成交会员数*/
  getPayUser(data, errorback) {
    return request._postBody('/shop/statistics/user/pay_user', data, errorback);
  },
  /*供应商统计*/
  getSupplierTotal(data, errorback) {
    return request._post('/shop/statistics/supplier/index', data, errorback);
  },
  /*供应商时间段统计*/
  getSupplierByDate(data, errorback) {
    return request._post('/shop/statistics/supplier/data', data, errorback);
  },
  /*访问统计*/
  getAccessTotal(data, errorback) {
    return request._post('/shop/statistics/access/index', data, errorback);
  },
  /*访问时间段统计*/
  getAccessByDate(data, errorback) {
    return request._post('/shop/statistics/access/data', data, errorback);
  },
  /*订单概况*/
  getOrderDate(data, errorback) {
    return request._get('/shop/statistics/order/index', data, errorback);
  },
  getProDate(data, errorback) {
    return request._get('/shop/statistics/product/index', data, errorback);
  },
}

export default StatisticsApi;
