<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="180px">
      <!--基础信息-->
      <Basic></Basic>

      <!--规格设置-->
      <Spec></Spec>

      <!-- 属性设置-->
      <Attr></Attr>

      <!-- 加料设置-->
      <Ingredients></Ingredients>

      <!--商品详情-->
      <Content></Content>

      <!--高级设置-->
      <Buyset></Buyset>

      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc">取消</el-button>
        <el-button size="small" type="primary" @click="onSubmit" :loading="loading">发布</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
  import PorductApi from '@/api/product.js';
  import Basic from '../../takeaway/product/part/Basic.vue';
  import Attr from '../../takeaway/product/part/Attr.vue';
  import Ingredients from '../../takeaway/product/part/Ingredients.vue';
  import Spec from '../../takeaway/product/part/Spec.vue';
  import Content from '../../takeaway/product/part/Content.vue';
  import Buyset from '../../takeaway/product/part/Buyset.vue';
  export default {
    components: {
      /*基础信息*/
      Basic,
      /*规格信息*/
      Spec,
      /* 属性信息*/
      Attr,
      /*加料设置*/
      Ingredients,
      /*商品详情*/
      Content,
      /*高级设置*/
      Buyset
    },
    data() {
      return {
        /*是否正在加载*/
        loading: false,
        /*form表单数据*/
        form: {
          model: {
            /*商品名称*/
            productName: '',
            /*商品分类*/
            categoryId: null,
            /*商品图片*/
            image: [],
            /*商品卖点*/
            sellingPoint: '',
            /*规格类别,默认10单规格，20多规格*/
            specType: 10,
            /*库存计算方式,默认20付款减库存，10下单减库存*/
            deductStockType: 20,
            sku: [{
              productPrice: '',
              stockNum: '',
              productWeight: '',
              costPrice:0,
            }],
                  productAttrList: [],
          productFeedList: [],
            /* 最小购买量 */
            minBuy: 1,
            /* 商品单位 */
            productUnit: '',
            /* 属性*/
            /*商品详情内容*/
            content: '',
            /*商品状态*/
            productStatus: 10,
            /*初始销量*/
            salesInitial: 0,
            /*商品排序，默认100*/
            productSort: 100,
            /*限购数量*/
            limitNum: 0,
            specialId: 0,
          },
          /*商品分类*/
          category: [],
          feed: [],
          special: [],
          /*规格数据*/
          specData: null,
        }
      };
    },
    provide: function() {
      return {
        form: this.form
      }
    },
    created() {

      /*获取基础数据*/
      this.getBaseData();

    },
    methods: {

      /*获取基础数据*/
      getBaseData: function() {
        let self = this;
        PorductApi.storeGetBaseData({}, true)
          .then(res => {
            self.loading = false;
            Object.assign(self.form, res.data);
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /*提交*/
      onSubmit: function() {
        let self = this;
        let params = self.form.model;
        self.$refs.form.validate(valid => {
          if (valid) {
            self.loading = true;
            PorductApi.storeAddProduct(
                params
, true)
              .then(data => {
                self.loading = false;
                ElMessage({
                  message: '添加成功',
                  type: 'success'
                });
                self.$router.push('/product/store/product/index');
              })
              .catch(error => {
                self.loading = false;
              });
          }
        });
      },

      /*取消*/
      cancelFunc() {
        this.$router.back(-1);
      }

    }
  };
</script>

<style lang="scss" scoped>
  .basic-setting-content {}

  .product-add {
    padding-bottom: 100px;
  }
</style>
