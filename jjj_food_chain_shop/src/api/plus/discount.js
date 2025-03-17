import request from '@/utils/request'

let DiscountApi = {
    /*列表*/
    getDiscount(data, errorback) {
        return request._get('/shop/plus/Discount/index', data, errorback);
    },
    /*列表*/
    setDiscount(data, errorback) {
        return request._post('/shop/plus/Discount/index', data, errorback);
    },
    /*添加*/
    add(data, errorback) {
        return request._post('/shop/plus/table/table/add', data, errorback);
    },
    /*修改*/
    edit(data, errorback) {
        return request._post('/shop/plus/table/table/edit', data, errorback);
    },
    /*删除*/
    del(data, errorback) {
        return request._post('/shop/plus/table/table/delete', data, errorback);
    },
}

export default DiscountApi;
