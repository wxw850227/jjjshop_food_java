import request from '@/utils/request'

let GroupApi = {
  /*团购列表*/
  grouplist(data, errorback) {
    return request._post('/shop/plus/group/group/index', data, errorback);
  },
  /*添加团购*/
  toAddGroup(data, errorback) {
    return request._get('/shop/plus/group/group/add', data, errorback);
  },
  /*添加团购*/
  addGroup(data, errorback) {
    return request._post('/shop/plus/group/group/add', data, errorback);
  },
  /*团购详情*/
  toEditGroup(data, errorback) {
    return request._get('/shop/plus/group/group/edit', data, errorback);
  },
  /*编辑团购*/
  editGroup(data, errorback) {
    return request._post('/shop/plus/group/group/edit', data, errorback);
  },
  /*删除团购*/
  deleteGroup(data, errorback) {
    return request._post('/shop/plus/group/group/delete', data, errorback);
  },
  /*删除团购*/
  getGroupSetting(data, errorback) {
    return request._get('/shop/plus/group/setting/index', data, errorback);
  },
  /*删除团购*/
  editGroupSetting(data, errorback) {
    return request._post('/shop/plus/group/setting/index', data, errorback);
  },
  /*团购订单*/
  orderList(data, errorback) {
    return request._get('/shop/plus/group/order/index', data, errorback);
  },
  /*订单详情*/
  orderDetail(data, errorback) {
    return request._post('/shop/plus/group/order/detail', data, errorback);
  },
  /*订单详情*/
  groupListData(data, errorback) {
    return request._post('/shop/data/group/index', data, errorback);
  },
}
export default GroupApi;
