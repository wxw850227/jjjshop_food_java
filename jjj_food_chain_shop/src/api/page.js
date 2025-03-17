import request from '@/utils/request'

let PageApi = {
    /*编辑页面*/
    editHome(data, errorback) {
        return request._get('/shop/page/page/home', data, errorback);
    },
    /*编辑页面*/
    editPage(data, errorback) {
        return request._get('/shop/page/page/edit', data, errorback);
    },
    /*保存编辑页面*/
    SavePage(data, errorback) {
        return request._post('/shop/page/page/edit', data, errorback);
    },
    /*广告列表*/
    menuList(data, errorback) {
      return request._postBody('/shop/page/page/mymenu/index', data, errorback);
    },
    /*添加广告*/
    addMenu(data, errorback) {
      return request._postBody('/shop/page/page/mymenu/add', data, errorback);
    },
    /*广告详情*/
    menuDetail(data, errorback) {
      return request._get('/shop/page/page/mymenu/detail', data, errorback);
    },
    /*修改广告*/
    editMenu(data, errorback) {
      return request._postBody('/shop/page/page/mymenu/edit', data, errorback);
    },
    /*删除广告*/
    deleteMenu(data, errorback) {
      return request._post('/shop/page/page/mymenu/delete', data, errorback);
    },
}

export default PageApi;
