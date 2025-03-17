import request from '@/utils/request'

let FullreduceApi = {
    /*列表*/
    getList(data, errorback) {
        return request._post('/shop/plus/fullreduce/index', data, errorback);
    },
    /*添加*/
    add(data, errorback) {
        return request._post('/shop/plus/fullreduce/add', data, errorback);
    },
    /*修改*/
    edit(data, errorback) {
        return request._post('/shop/plus/fullreduce/edit', data, errorback);
    },
    /*删除*/
    del(data, errorback) {
        return request._post('/shop/plus/fullreduce/delete', data, errorback);
    },
}

export default FullreduceApi;
