import request from '@/utils/request'

let CashApi = {
  /*首页概况*/
  index(data, errorback) {
    return request._post('/shop/cash/cash/index', data, errorback);
  },
  /*首页概况*/
  settled(data, errorback) {
    return request._post('/shop/cash/settled/index', data, errorback);
  },
  getSettledByDate(data, errorback) {
    return request._post('/shop/cash/settled/data', data, errorback);
  },
  /*订单结算*/
  order(data, errorback) {
    return request._post('/shop/cash/order/index', data, errorback);
  },
  /*订单结算详情*/
  orderDetail(data, errorback) {
    return request._post('/shop/cash/order/detail', data, errorback);
  },
  /*财务对账*/
  financeIndex(data, errorback) {
    return request._post('/shop/cash/finance/index', data, errorback);
  },
  /*对账详情*/
  financeDetail(data, errorback) {
    return request._post('/shop/cash/finance/detail', data, errorback);
  },
  /*订单详情*/
  financeOrderDetail(data, errorback) {
    return request._post('/shop/cash/finance/orderdetail', data, errorback);
  },
  /*导出*/
  financeExport(data, errorback) {
    return request._post('/shop/cash/finance/export', data, errorback);
  },
  /*交易记录*/
  orderRecord(data, errorback) {
    return request._post('/shop/cash/order/record', data, errorback);
  },
  /*交易订单详情*/
  orderRecordDetail(data, errorback) {
    return request._post('/shop/cash/order/recorddetail', data, errorback);
  },
  /*交易订单导出*/
  orderRecordExport(data, errorback) {
    return request._post('/shop/cash/order/export', data, errorback);
  },
  account(data, errorback) {
    return request._post('/shop/cash/account/index', data, errorback);
  },
  /*保存用户提现账户信息*/
  getAccount(data, errorback) {
    return request._get('/shop/cash/account/account', data, errorback);
  },
  /*保存用户提现账户信息*/
  setAccount(data, errorback) {
    return request._post('/shop/cash/account/account', data, errorback);
  },
  /*申请提现*/
  apply(data, errorback) {
    return request._post('/shop/cash/account/apply', data, errorback);
  },
}

export default CashApi;
