import request from '@/utils/request'

let PlusApi = {

    /*插件列表*/
    plusList(data, errorback) {
        return request._post('/shop/plus/plus/index', data, errorback);
    },
    /*入驻申请列表*/
    saleList(data, errorback) {
        return request._post('/shop/plus/agent/apply/index', data, errorback);
    },
    /*分销商审核*/
    editApplyStatus(data, errorback) {
        return request._post('/shop/plus/agent/apply/editApplyStatus', data, errorback);
    },
    /*分销商列表*/
    agentList(data, errorback) {
        return request._post('/shop/plus/agent/user/index', data, errorback);
    },
    /*查看下级用户*/
    agentUserFans(data, errorback) {
        return request._post('/shop/plus/agent/user/fans', data, errorback);
    },
    /*修改分销商*/
    agentUserEdit(data, errorback) {
        return request._post('/shop/plus/agent/user/edit', data, errorback);
    },
    /*删除分销商*/
    deleteagent(data, errorback) {
        return request._post('/shop/plus/agent/user/delete', data, errorback);
    },
    /*分销订单*/
    agentOrder(data, errorback) {
        return request._post('/shop/plus/agent/order/index', data, errorback);
    },
    /*提现申请*/
    cash(data, errorback) {
        return request._post('/shop/plus/agent/cash/index', data, errorback);
    },
    /*分销商提现审核*/
    cashSubmit(data, errorback) {
        return request._post('/shop/plus/agent/cash/submit', data, errorback);
    },
    /*微信打款*/
    WxPay(data, errorback) {
        return request._post('/shop/plus/agent/cash/wechat_pay', data, errorback);
    },
    /*分销商确认打款*/
    money(data, errorback) {
        return request._post('/shop/plus/agent/cash/money', data, errorback);
    },

    /*分销设置*/
    agentSet(data, errorback) {
        return request._post('/shop/plus/agent/setting/index', data, errorback);
    },
    /*分销设置-基础设置*/
    basic(data, errorback) {
        return request._post('/shop/plus/agent/setting/basic', data, errorback);
    },
    /*分销设置-分销商条件*/
    condition(data, errorback) {
        return request._post('/shop/plus/agent/setting/condition', data, errorback);
    },
    /*分销设置-佣金设置*/
    commission(data, errorback) {
        return request._post('/shop/plus/agent/setting/commission', data, errorback);
    },
    /*分销设置-佣金设置*/
    settlement(data, errorback) {
        return request._post('/shop/plus/agent/setting/settlement', data, errorback);
    },
    /*分销设置-自定义文字*/
    words(data, errorback) {
        return request._post('/shop/plus/agent/setting/words', data, errorback);
    },
    /*分销设置-申请协议*/
    license(data, errorback) {
        return request._post('/shop/plus/agent/setting/license', data, errorback);
    },
    /*分销设置-页面背景图*/
    background(data, errorback) {
        return request._post('/shop/plus/agent/setting/background', data, errorback);
    },
    /*分销设置-模板消息*/
    templateMsg(data, errorback) {
        return request._post('/shop/plus/agent/setting/templateMsg', data, errorback);
    },
    /*分销设置-分销海报*/
    SaveBackground(data, errorback) {
        return request._post('/shop/plus/agent/setting/qrcode', data, errorback);
    },
    /*分销设置-获取分销海报*/
    GetBackground(data, errorback) {
        return request._get('/shop/plus/agent/setting/qrcode', data, errorback);
    },
}

export default PlusApi;
