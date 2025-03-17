import request from '@/utils/request'

let PushApi = {
  /*发送推送消息*/
  send(data, errorback) {
    return request._post('/shop/plus/market/article/index', data, errorback);
  },
  /*包邮设置*/
  shipping(data, errorback) {
    return request._post('/shop/plus/market/article/index', data, errorback);
  },
  /*获取用户*/
  getList(data, errorback) {
    return request._post('/shop/plus/market/article/index', data, errorback);
  },
}
export default PushApi;
