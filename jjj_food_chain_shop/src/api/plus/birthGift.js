import request from '@/utils/request'

let BirthGiftApi = {
  getBirthGift(data, errorback) {
    return request._get('/shop/plus/BirthGift/index', data, errorback);
  },
  editBirthGift(data, errorback) {
    return request._post('/shop/plus/BirthGift/index', data, errorback);
  },
}

export default BirthGiftApi;
