import request from '@/utils/request'

let AppSettingApi = {
  /*小程序*/
  appwxDetail(data, errorback) {
    return request._get('/shop/appSetting/appWx/index', data, errorback);
  },
  /*小程序*/
  editAppWx(data, errorback) {
    return request._post('/shop/appSetting/appWx/index', data, errorback);
  },
  /*公众号*/
  appmpDetail(data, errorback) {
    return request._get('/shop/appsetting/appmp/index', data, errorback);
  },
  /*公众号*/
  editAppMp(data, errorback) {
    return request._post('/shop/appsetting/appmp/index', data, errorback);
  },
  /*app*/
  appDetail(data, errorback) {
    return request._get('/shop/appSetting/app/index', data, errorback);
  },
  /*app*/
  editApp(data, errorback) {
    return request._post('/shop/appSetting/app/index', data, errorback);
  },
  /*app支付宝支付*/
  payDetail(data, errorback) {
    return request._get('/shop/appSetting/app/pay', data, errorback);
  },
  /*app支付宝支付*/
  editPay(data, errorback) {
    return request._postBody('/shop/appSetting/app/pay', data, errorback);
  },
  uploadP12(data, errorback) {
    return request._upload('/shop/appSetting/app/uploadP12', data, errorback);
  },
}

export default AppSettingApi;
