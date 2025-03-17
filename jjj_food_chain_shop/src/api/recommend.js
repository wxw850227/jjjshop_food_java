import request from '@/utils/request'

let RecommendApi = {
    /*获取数据*/
    getData(data, errorback) {
        return request._get('/shop/plus/recommend/index', data, errorback);
    },
    /*保存数据*/
    saveData(data, errorback) {
        return request._post('/shop/plus/recommend/index', data, errorback);
    },
}
export default RecommendApi;
