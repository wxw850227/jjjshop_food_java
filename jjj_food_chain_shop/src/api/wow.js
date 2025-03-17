import request from '@/utils/request'

let WowApi = {
  /*获取数据*/
  getData(data, errorback) {
    return request._post('/shop/plus/wow/wow/index', data, errorback);
  },
  /*保存数据*/
  saveData(data, errorback) {
    return request._post('/shop/plus/wow/wow/saveData', data, errorback);
  },
}
export default WowApi;
