import request from '@/utils/request'

let PointsApi = {
    /*充值套餐*/
    listPlan(data, errorback) {
        return request._post('/shop/plus/live/plan/index', data, errorback);
    },
    /*添加套餐*/
    addPlan(data, errorback) {
        return request._get('/shop/plus/live/plan/add', data, errorback);
    },
    /*修改套餐*/
    editPlan(data, errorback) {
        return request._post('/shop/plus/live/plan/edit', data, errorback);
    },
    /*删除套餐*/
    deletePlan(data, errorback) {
        return request._get('/shop/plus/live/plan/delete', data, errorback);
    },
    /*充值记录*/
    log(data, errorback) {
        return request._post('/shop/plus/live/plan/log', data, errorback);
    },
}
export default PointsApi;
