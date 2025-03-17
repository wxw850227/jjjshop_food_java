<template>
  <el-dialog title="提示" :model-value="isPopup" width="80%" :before-close="handleClose" v-loading="loading">
    <el-container>
      <div class="p30 ww100">
        <el-container v-if="!loading">
          <el-aside width="400px" class='border'>
            <div class="common-form">订单信息</div>
            <div class="p-0-20">
              <span>订单类型：{{detail.delivery_type.text}}</span>
              <el-divider></el-divider>
              <span v-if="detail.user">用户信息：{{detail.user.nickName}}</span>
              <el-divider></el-divider>
              <span>下单时间：{{detail.create_time}}</span>
              <el-divider></el-divider>
              <span>订单编号：{{detail.order_no}}</span>
              <el-divider></el-divider>
              <span>实付金额：{{detail.pay_price}}</span>
              <el-divider></el-divider>
              <span>支付状态：{{detail.pay_status.text}}</span>
              <el-divider></el-divider>
              <span>付款方式：{{detail.pay_type.text}}</span>
              <el-divider></el-divider>
              <span>订单状态：{{detail.order_status.text}}</span>
              <el-divider></el-divider>
              <span>备注：{{detail.buyer_remark}}</span>
            </div>
          </el-aside>
          <el-main class="ww100 p-0-20 main-box">
            <el-header height='430px'  class='border mb16'>
              <div class="common-form">订单费用</div>
              <div>
                <span>商品价格：{{detail.total_price}}</span>
                <el-divider></el-divider>
                <span>包装费：{{detail.bagPrice}}</span>
                <el-divider></el-divider>
                <span>配送费：{{detail.express_price}}</span>
                <el-divider></el-divider>
                <span>服务费：{{detail.service_money}}</span>
                <el-divider></el-divider>
                <span>优惠：{{(detail.order_price - detail.pay_price).toFixed(2)}}</span>
                <el-divider></el-divider>
                <span>商户实际收入：{{(detail.pay_price - detail.refund_money).toFixed(2)}}</span>
              </div>
            </el-header>
            <el-footer  class='border' height="400px">
              <div class="common-form">商品信息</div>
              <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
                <el-table-column prop="product_name" label="商品名称"></el-table-column>
                <el-table-column prop="product_id" label="商品ID"></el-table-column>
                <el-table-column prop="productPrice" label="商品价格"></el-table-column>
                <el-table-column prop="total_num" label="数量"></el-table-column>
                <el-table-column prop="total_price" label="小计(元)"></el-table-column>
              </el-table>
            </el-footer>
          </el-main>
        </el-container>
      </div>
    </el-container>
  </el-dialog>
</template>

<script>
  import CashApi from '@/api/cash.js';
  export default {
    data() {
      return {
        dialogVisible: false,
        loading: true,
        tableData: [],
        detail: {}

      }

    },
    props: ['isPopup', 'order_id'],
    watch: {
      'isPopup': function(n, o) {
        if (n != o) {
          this.getdata();
        }
      }
    },
    methods: {
      getdata() {
        let self = this;
        // 取到路由带过来的参数
        self.loading = true;
        CashApi.orderRecordDetail({
              order_id: self.order_id
            },
            true
          )
          .then(data => {
            self.loading = false;
            self.tableData = data.data.detail.product;
            self.detail = data.data.detail;
          })
          .catch(error => {
            self.loading = false;
          });
      },
      handleClose(done) {
        this.$emit('close')
      }
    }
  }
</script>

<style scoped>
  .el-aside.border{
    padding: 20px;
  }
  .main-box.p-0-20{
    padding: 0 20px;
  }
</style>
