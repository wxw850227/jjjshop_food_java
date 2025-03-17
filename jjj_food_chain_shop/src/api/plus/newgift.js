import request from '@/utils/request'

let NewgiftApi = {
  getNewgift(data, errorback) {
    return request._get('/shop/plus/Newgift/index', data, errorback);
  },
  editNewgift(data, errorback) {
    return request._post('/shop/plus/Newgift/index', data, errorback);
  },
}

export default NewgiftApi;
