import request from '@/utils/request'

let QueueApi = {

  /*桌位列表*/
  tablelist(data, errorback) {
    return request._post('/shop/plus/queue/table/index', data, errorback);
  },
  /*桌位设置*/
  tableswitch(data, errorback) {
    return request._post('/shop/plus/queue/table/onwitch', data, errorback);
  },
  /*桌位添加*/
  addTable(data, errorback) {
    return request._post('/shop/plus/queue/table/add', data, errorback);
  },
  /*编辑桌位*/
  editTable(data, errorback) {
    return request._post('/shop/plus/queue/table/edit', data, errorback);
  },
  /*删除桌位*/
  deleteTable(data, errorback) {
    return request._post('/shop/plus/queue/table/delete', data, errorback);
  },
  /*记录列表*/
  recordlist(data, errorback) {
    return request._post('/shop/plus/queue/record/index', data, errorback);
  },
  /*记录列表*/
  recordedit(data, errorback) {
    return request._post('/shop/plus/queue/record/edit', data, errorback);
  },
  /*记录列表*/
  getsetting(data, errorback) {
    return request._get('/shop/plus/queue/setting/index', data, errorback);
  },
  /*记录列表*/
  editsetting(data, errorback) {
    return request._post('/shop/plus/queue/setting/index', data, errorback);
  },
}

export default QueueApi;
