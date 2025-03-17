import request from '@/utils/request'

let SharingApi = {
  /*活动列表*/
  activeList(data, errorback) {
    return request._post('/shop/plus/assemble/active/index', data, errorback);
  },
  /*添加分类*/
  addCategory(data, errorback) {
    return request._post('/shop/plus/sharing/category/add', data, errorback);
  },

}
export default SharingApi;
