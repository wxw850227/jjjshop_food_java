import request from '@/utils/request'

let UserApi = {

    /*用户登录*/
    login(data, errorback) {
        return request._post('/shop/passport/login', data, errorback);
    },
    /*退出登录*/
    loginOut(data, errorback) {
        return request._post('/shop/passport/logout', data, errorback);
    },
    /*添加用户*/
    adduser(data, errorback) {
        return request._post('/shop/user/user/add', data, errorback);
    },

    /*修改用户*/
    edituser(data, errorback) {
        return request._post('/shop/user/user/edit', data, errorback);
    },
    /*获取用户*/
    getuser(data, errorback) {
        return request._get('/shop/user/user/edit', data, errorback);
    },
    /*修改用户等级*/
    editUserGrade(data, errorback) {
        return request._post('/shop/user/user/grade', data, errorback);
    },
    /*充值*/
    userRecharge(data, errorback) {
        return request._postBody('/shop/user/user/recharge', data, errorback);
    },
    /*删除用户*/
    deleteuser(data, errorback) {
        return request._post('/shop/user/user/delete', data, errorback);
    },

    /*用户列表*/
    userlist(data, errorback) {
        return request._postBody('/shop/user/user/index', data, errorback);
    },
    /*等级列表*/
    gradelist(data, errorback) {
        return request._post('/shop/user/grade/index', data, errorback);
    },
    /*添加等级*/
    addgrade(data, errorback) {
        return request._post('/shop/user/grade/add', data, errorback);
    },
    /*修改等级*/
    editGrade(data, errorback) {
        return request._post('/shop/user/grade/edit', data, errorback);
    },
    /*删除等级*/
    deletegrade(data, errorback) {
        return request._post('/shop/user/grade/delete', data, errorback);
    },
    /*用户余额*/
    BalanceLog(data, errorback) {
        return request._postBody('/shop/user/balance/log', data, errorback);
    },
    /*充值记录*/
    RechargeOrder(data, errorback) {
        return request._post('/shop/user/Recharge/order', data, errorback);
    },
    /*修改密码*/
    EditPass(data, errorback) {
        return request._post('/shop/passport/editPass', data, errorback);
    },
}

export default UserApi;
