import request from '@/utils/request'

let SupplierApi = {
  /*供应商列表*/
  supplierList(data, errorback) {
    return request._postBody('/shop/supplier/supplier/index', data, errorback);
  },
  /*添加供应商*/
  toaddSupplier(data, errorback) {
    return request._get('/shop/supplier/supplier/add', data, errorback);
  },
  /*添加供应商*/
  addSupplier(data, errorback) {
    return request._post('/shop/supplier/supplier/add', data, errorback);
  },
  /*供应商编辑*/
  toEditSupplier(data, errorback) {
    return request._get('/shop/supplier/supplier/edit', data, errorback);
  },
  /*供应商设置*/
  setSupplier(data, errorback) {
    return request._postBody('/shop/supplier/supplier/setting', data, errorback);
  },
  /*供应商设置*/
  getsetSupplier(data, errorback) {
    return request._get('/shop/supplier/supplier/setting', data, errorback);
  },
  /*供应商编辑*/
  editSupplier(data, errorback) {
    return request._postBody('/shop/supplier/supplier/edit', data, errorback);
  },
  /*删除供应商*/
  deleteSupplier(data, errorback) {
    return request._post('/shop/supplier/supplier/delete', data, errorback);
  },
  /*提现记录*/
  cashList(data, errorback) {
    return request._post('/shop/supplier/supplier/cash', data, errorback);
  },
  /*提现审核*/
  cashSubmit(data, errorback) {
    return request._post('/shop/supplier/supplier/submit', data, errorback);
  },
  /*提现确认打款*/
  cashMoney(data, errorback) {
    return request._post('/shop/supplier/supplier/money', data, errorback);
  },
  /*开启禁止*/
  supplierRecycle(data, errorback) {
    return request._post('/shop/supplier/supplier/recycle', data, errorback);
  },
  /*店铺明细*/
  Capital(data, errorback) {
    return request._post('/shop/supplier/supplier/balance', data, errorback);
  },
  /*店铺明细*/
  printingList(data, errorback) {
    return request._post('/shop/supplier/printing/index', data, errorback);
  },
  deletePrinting(data, errorback) {
    return request._post('/shop/supplier/printing/delete', data, errorback);
  },
  getPrinting(data, errorback) {
    return request._get('/shop/supplier/printing/add', data, errorback);
  },
  addPrinting(data, errorback) {
    return request._post('/shop/supplier/printing/add', data, errorback);
  },
  getEditPrinting(data, errorback) {
    return request._get('/shop/supplier/printing/edit', data, errorback);
  },
  EditPrinting(data, errorback) {
    return request._post('/shop/supplier/printing/edit', data, errorback);
  },
}
export default SupplierApi;
