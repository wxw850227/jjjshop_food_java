import request from '@/utils/request'

let TakeOutApi = {
  /*店内概况*/
  takeSurvey(data, errorback) {
    return request._postBody('/shop/takeout/survey/index', data, errorback);
  },
  /*配送管理*/
  deliverList(data, errorback) {
    return request._postBody('/shop/takeout/deliver/index', data, errorback);
  },
  /*确认送达*/
  verify(data, errorback) {
    return request._post('/shop/takeout/deliver/verify', data, errorback);
  },
  /*取消配送*/
  cancel(data, errorback) {
    return request._post('/shop/takeout/deliver/cancel', data, errorback);
  },
  /*取消配送*/
  detail(data, errorback) {
    return request._post('/shop/takeout/deliver/detail', data, errorback);
  },
  /*导出*/
  export(data, errorback) {
    return request._post('/shop/takeout/deliver/export', data, errorback);
  },
}

export default TakeOutApi;
