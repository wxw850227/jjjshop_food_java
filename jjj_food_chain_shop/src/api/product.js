import request from '@/utils/request'

let ProductApi = {

    /*店内分类管理*/
    storeCatList(data, errorback) {
        return request._postBody('/shop/product/store/category/index', data, errorback);
    },
    /* 外卖分类管理*/
    takeCatList(data, errorback) {
        return request._postBody('/shop/product/takeaway/category/index', data, errorback);
    },

    /*店内特殊分类管理*/
    storeCatSP(data, errorback) {
        return request._postBody('/shop/product/store/category/list', data, errorback);
    },
    /* 外卖特殊分类管理*/
    takeCatSP(data, errorback) {
        return request._postBody('/shop/product/takeaway/category/list', data, errorback);
    },
    /*分类添加*/
    storeCatAdd(data, errorback) {
        return request._postBody('/shop/product/store/category/add', data, errorback);
    },
    /*分类删除*/
    storeCatDel(data, errorback) {
        return request._postBody('/shop/product/store/category/delete', data, errorback);
    },
    /*分类修改*/
    storeCatEdit(data, errorback) {
        return request._postBody('/shop/product/store/category/edit', data, errorback);
    },
    /*状态修改*/
    storeCatSet(data, errorback) {
        return request._post('/shop/product/store/category/set', data, errorback);
    },
    /*分类添加*/
    takeCatAdd(data, errorback) {
        return request._postBody('/shop/product/takeaway/category/add', data, errorback);
    },
    /*分类删除*/
    takeCatDel(data, errorback) {
        return request._post('/shop/product/takeaway/category/delete', data, errorback);
    },
    /*分类修改*/
    takeCatEdit(data, errorback) {
        return request._postBody('/shop/product/takeaway/category/edit', data, errorback);
    },
    /*状态修改*/
    takeCatSet(data, errorback) {
        return request._post('/shop/product/takeaway/category/set', data, errorback);
    },


    /*产品列表*/
    takeProductList(data, errorback) {
        return request._postBody('/shop/product/takeaway/product/index', data, errorback);
    },
    storeProductList(data, errorback) {
        return request._postBody('/shop/product/store/product/index', data, errorback);
    },

    /*产品选择列表*/
    takeChooseLists(data, errorback) {
        return request._post('/shop/data/takeaway/product/lists', data, errorback);
    },
    storeChooseLists(data, errorback) {
        return request._post('/shop/data/store/product/lists', data, errorback);
    },
    /*产品选择列表*/
    chooseLists(data, errorback) {
        return request._post('/shop/data/product/lists', data, errorback);
    },
    /*规格选择列表*/
    takeChooseSpec(data, errorback) {
        return request._post('/shop/data/takeaway/product/spec', data, errorback);
    },
    storeChooseSpec(data, errorback) {
        return request._post('/shop/data/store/product/spec', data, errorback);
    },
    /*新增产品*/
    storeAddProduct(data, errorback) {
        return request._postBody('/shop/product/store/product/add', data, errorback);
    },
    takeAddProduct(data, errorback) {
        return request._postBody('/shop/product/takeaway/product/add', data, errorback);
    },

    /*产品基础数据*/
    storeGetBaseData(data, errorback) {
        return request._get('/shop/product/store/product/add', data, errorback);
    },
    takeGetBaseData(data, errorback) {
        return request._get('/shop/product/takeaway/product/add', data, errorback);
    },
    /*删除产品*/
    storeDelProduct(data, errorback) {
        return request._post('/shop/product/store/product/delete', data, errorback);
    },
    takeDelProduct(data, errorback) {
        return request._post('/shop/product/takeaway/product/delete', data, errorback);
    },
    /*产品基础数据*/
    storeGetEditData(data, errorback) {
        return request._get('/shop/product/store/product/edit', data, errorback);
    },
    takeGetEditData(data, errorback) {
        return request._get('/shop/product/takeaway/product/edit', data, errorback);
    },
    /*新增产品*/
    storeEditProduct(data, errorback) {
        return request._postBody('/shop/product/store/product/edit', data, errorback);
    },
    takeEditProduct(data, errorback) {
        return request._postBody('/shop/product/takeaway/product/edit', data, errorback);
    },
    //上下架
    takeStateProduct(data, errorback) {
        return request._post('/shop/product/takeaway/product/state', data, errorback);
    },
    //上下架
    storeStateProduct(data, errorback) {
        return request._post('/shop/product/store/product/state', data, errorback);
    },
    /*新增规格值*/
    addSpecValue(data, errorback) {
        return request._post('/shop/product/spec/addSpecValue', data, errorback);
    },
    /*商品列表不带分页*/
    storeGetList(data, errorback) {
        return request._post('/shop/data/store/product/lists', data, errorback);
    },
    takeGetList(data, errorback) {
        return request._post('/shop/data/takeaway/product/lists', data, errorback);
    },
    /*商品列表不带分页*/
    getActiveProductList(data, errorback) {
        return request._post('/shop/plus/fans/product/lists', data, errorback);
    },
    /*商品评论列表*/
    getCommentList(data, errorback) {
        return request._post('/shop/product/comment/index', data, errorback);
    },
    /*获取评论详情*/
    getComment(data, errorback) {
        return request._post('/shop/product/comment/detail', data, errorback);
    },
    /*获取评论详情*/
    editComment(data, errorback) {
        return request._post('/shop/product/comment/edit', data, errorback);
    },
    /*删除评论*/
    delComment(data, errorback) {
        return request._post('/shop/product/comment/delete', data, errorback);
    },
    /*属性库*/
    AttributeList(data, errorback) {
        return request._postBody('/shop/product/expand/attr/index', data, errorback);
    },
    addAttribute(data, errorback) {
        return request._postBody('/shop/product/expand/attr/add', data, errorback);
    },
    editAttribute(data, errorback) {
        return request._postBody('/shop/product/expand/attr/edit', data, errorback);
    },
    deleteAttribute(data, errorback) {
        return request._post('/shop/product/expand/attr/delete', data, errorback);
    },
    deleteAttributes(data, errorback) {
        return request._post('/shop/product/expand/attr/deletes', data, errorback);
    },
    /*加料库*/
    FeedList(data, errorback) {
        return request._postBody('/shop/product/expand/feed/index', data, errorback);
    },
    addFeed(data, errorback) {
        return request._postBody('/shop/product/expand/feed/add', data, errorback);
    },
    editFeed(data, errorback) {
        return request._postBody('/shop/product/expand/feed/edit', data, errorback);
    },
    deleteFeed(data, errorback) {
        return request._post('/shop/product/expand/feed/delete', data, errorback);
    },
    deleteFeeds(data, errorback) {
        return request._post('/shop/product/expand/feed/deletes', data, errorback);
    },
    /*规格库*/
    SpecList(data, errorback) {
        return request._postBody('/shop/product/expand/spec/index', data, errorback);
    },
    addSpec(data, errorback) {
        return request._postBody('/shop/product/expand/spec/add', data, errorback);
    },
    editSpec(data, errorback) {
        return request._postBody('/shop/product/expand/spec/edit', data, errorback);
    },
    deleteSpec(data, errorback) {
        return request._post('/shop/product/expand/spec/delete', data, errorback);
    },
    deleteSpecs(data, errorback) {
        return request._post('/shop/product/expand/spec/deletes', data, errorback);
    },
    /*单位库*/
    UnitList(data, errorback) {
        return request._postBody('/shop/product/expand/unit/index', data, errorback);
    },
    addUnit(data, errorback) {
        return request._postBody('/shop/product/expand/unit/add', data, errorback);
    },
    editUnit(data, errorback) {
        return request._postBody('/shop/product/expand/unit/edit', data, errorback);
    },
    deleteUnit(data, errorback) {
        return request._post('/shop/product/expand/unit/delete', data, errorback);
    },
    deleteUnits(data, errorback) {
        return request._post('/shop/product/expand/unit/deletes', data, errorback);
    },
    /*打印标签*/
    LabelList(data, errorback) {
        return request._post('/shop/product/expand/Label/index', data, errorback);
    },
    addLabel(data, errorback) {
        return request._post('/shop/product/expand/Label/add', data, errorback);
    },
    editLabel(data, errorback) {
        return request._post('/shop/product/expand/Label/edit', data, errorback);
    },
    deleteLabel(data, errorback) {
        return request._post('/shop/product/expand/Label/delete', data, errorback);
    },

}

export default ProductApi;
