import request from '@/utils/request'

let LiveApi = {
    /*直播列表*/
    getList(data, errorback) {
        return request._post('/shop/plus/live/wx/index', data, errorback);
    },
    /*直播列表同步*/
    syn(data, errorback) {
        return request._post('/shop/plus/live/wx/syn', data, errorback);
    },
    /*置顶设置*/
    settop(data, errorback) {
        return request._post('/shop/plus/live/wx/settop', data, errorback);
    },
    /*同步设置*/
    setSyn(data, errorback) {
        return request._post('/shop/plus/live/wx/setSyn', data, errorback);
    },
    /*创建直播*/
    addRoom(data, errorback) {
        return request._post('/shop/plus/live/wx/add', data, errorback);
    },
    /*编辑直播*/
    editRoom(data, errorback) {
        return request._post('/shop/plus/live/wx/edit', data, errorback);
    },
    /*删除直播*/
    deleteRoom(data, errorback) {
        return request._post('/shop/plus/live/wx/delete', data, errorback);
    },
    /*商品管理*/
    productList(data, errorback) {
        return request._post('/shop/plus/live/product/index', data, errorback);
    },
    /*添加商品*/
    addProduct(data, errorback) {
        return request._post('/shop/plus/live/product/add', data, errorback);
    },
    /*编辑商品*/
    editProduct(data, errorback) {
        return request._post('/shop/plus/live/product/edit', data, errorback);
    },
    /*编辑商品*/
    auditProduct(data, errorback) {
        return request._post('/shop/plus/live/product/audit', data, errorback);
    },
    /*删除商品*/
    deleteProduct(data, errorback) {
        return request._post('/shop/plus/live/product/delete', data, errorback);
    },
    /*审核通过商品*/
    listProduct(data, errorback) {
        return request._post('/shop/plus/live/product/list', data, errorback);
    },
    /*直播间商品*/
    liveProduct(data, errorback) {
        return request._post('/shop/plus/live/LiveProduct/index', data, errorback);
    },
    /*导入直播间商品*/
    liveAddProduct(data, errorback) {
        return request._post('/shop/plus/live/LiveProduct/add', data, errorback);
    },
    /*推送商品*/
    pushProduct(data, errorback) {
        return request._post('/shop/plus/live/LiveProduct/push', data, errorback);
    },
    /*上下架商品*/
    onSale(data, errorback) {
        return request._post('/shop/plus/live/LiveProduct/onsale', data, errorback);
    },
    /*上下架商品*/
    deleteLiveProduct(data, errorback) {
        return request._post('/shop/plus/live/LiveProduct/delete', data, errorback);
    },

}

export default LiveApi;