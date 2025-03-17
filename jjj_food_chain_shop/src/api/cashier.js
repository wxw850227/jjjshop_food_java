import request from '@/utils/request'

let CashierApi = {

  /*收银设置*/
  getSetting(data, errorback) {
    return request._get('/shop/plus/cashier/setting/index', data, errorback);
  },
  /*收银设置*/
  editSetting(data, errorback) {
    return request._post('/shop/plus/cashier/setting/index', data, errorback);
  },
  /*收银员列表*/
  userList(data, errorback) {
    return request._post('/shop/plus/cashier/user/index', data, errorback);
  },
  /*收银员添加*/
  addUser(data, errorback) {
    return request._post('/shop/plus/cashier/user/add', data, errorback);
  },
  /*编辑收银员*/
  editUser(data, errorback) {
    return request._post('/shop/plus/cashier/user/edit', data, errorback);
  },
  /*删除收银员*/
  deleteUser(data, errorback) {
    return request._post('/shop/plus/cashier/user/delete', data, errorback);
  },
  /*收银员设置*/
  setUser(data, errorback) {
    return request._post('/shop/plus/cashier/user/set', data, errorback);
  },
}

export default CashierApi;
