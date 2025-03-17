import request from '@/utils/request'

let PointsApi = {
  productList(data, errorback) {
    return request._get('/shop/plus/points/product/index', data, errorback);
  },
  getProduct(data, errorback) {
    return request._get('/shop/plus/points/product/add', data, errorback);
  },
  addProduct(data, errorback) {
    return request._post('/shop/plus/points/product/add', data, errorback);
  },
  getEditProduct(data, errorback) {
    return request._get('/shop/plus/points/product/edit', data, errorback);
  },
  editProduct(data, errorback) {
    return request._post('/shop/plus/points/product/edit', data, errorback);
  },
  categoryList(data, errorback) {
    return request._get('/shop/plus/points/category/index', data, errorback);
  },
  addCategory(data, errorback) {
    return request._post('/shop/plus/points/category/add', data, errorback);
  },
  editCategory(data, errorback) {
    return request._post('/shop/plus/points/category/edit', data, errorback);
  },
  deleteCategory(data, errorback) {
    return request._post('/shop/plus/points/category/delete', data, errorback);
  },
  /*获取兑换记录*/
  getExchange(data, errorback) {
    return request._post('/shop/plus/points/order/index', data, errorback);
  },
  /*详情*/
  orderdetail(data, errorback) {
    return request._post('/shop/plus/points/order/detail', data, errorback);
  },
  /*发货*/
  delivery(data, errorback) {
    return request._post('/shop/plus/points/order/delivery', data, errorback);
  },
  /*核销*/
  extract(data, errorback) {
    return request._post('/shop/plus/points/order/extract', data, errorback);
  },
}

export default PointsApi;
