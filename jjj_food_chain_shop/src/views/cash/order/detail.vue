<template>
  <div class="pb50" v-loading="loading">
    <!--订单进度-->
    <!--内容-->
    <div class="product-content">
      <div class="tips">
        <div class="tips_tit pb">操作提示</div>
        <div class="tips_txt">订单结算金额 = 店铺结算总金额(含运费)</div>
        <div class="tips_txt">店铺收入金额 = 店铺结算总金额 - 店铺退款金额</div>
      </div>
      <!--店铺结算-->
      <div class="common-form">店铺结算<span class="common-form-data">订单日期:{{ detail.create_time }}</span></div>
      <div class="table-wrap">
        <el-row>
          <el-col :span="6">
            <div class="pb16">
              <div>订单结算金额（元）</div>
              <div class="detail_prici">{{ detail.order_money }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
              <div>支付金额（元）</div>
              <div class="detail_prici">{{ detail.pay_money }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
              <div>配送费金额（元）</div>
              <div class="detail_prici">{{ detail.express_money }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
              <div>包装费（元）</div>
              <div class="detail_prici">{{ order.bagPrice }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <div class="pb16">
              <div>店铺收入金额（元）</div>
              <div class="detail_prici">{{ detail.real_supplier_money }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
              <div>店铺结算总金额（元）</div>
              <div class="detail_prici">{{ detail.supplier_money }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
              <div>店铺退款金额（元）</div>
              <div class="detail_prici">{{ detail.refund_money }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
      <!--商品信息-->
      <div class="common-form mt16">商品信息</div>
      <div class="table-wrap">
        <el-table size="small" :data="order.product" border style="width: 100%">
          <el-table-column prop="product_name" label="商品" width="400">
            <template #default="scope">
              <div class="product-info">
                <div class="pic"><img v-img-url="scope.row.image.filePath" /></div>
                <div class="info">
                  <div class="name">{{ scope.row.product_name }}</div>
                  <div class="gray9" v-if="scope.row.product_attr!=''">{{scope.row.product_attr}}</div>
                  <div class="orange" v-if="scope.row.refund">
                    {{ scope.row.refund.type.text }}-{{ scope.row.refund.status.text }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="product_no" label="商品编码"></el-table-column>
          <el-table-column prop="total_num" label="购买数量">
            <template #default="scope">
              <p>x {{ scope.row.total_num }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="total_pay_price" label="支付金额(元)">
            <template #default="scope">
              <p>￥ {{ scope.row.total_pay_price }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="total_pay_price" label="退款金额(元)">
            <template #default="scope">
              <p v-if="scope.row.refund">{{scope.row.refund.refund_money}}</p>
              <p v-else>0</p>
            </template>
          </el-table-column>
        </el-table>
      </div>

    </div>
    <div class="common-button-wrapper">
      <el-button size="small" type="info" @click="cancelFunc">返回上一页</el-button>
    </div>
  </div>
</template>

<script>
  import CashApi from '@/api/cash.js';
  import {
    deepClone
  } from '@/utils/base.js';
  export default {
    data() {
      return {
        active: 0,
        /*是否加载完成*/
        loading: true,
        /*订单数据*/
        detail: {},
        order: {},
        /*一页多少条*/
        pageSize: 20,
        /*一共多少条数据*/
        totalDataNumber: 0,
        /*当前是第几页*/
        curPage: 1,
      };
    },
    created() {
      /*获取列表*/
      this.getParams();
    },
    methods: {
      /*获取参数*/
      getParams() {
        let self = this;
        // 取到路由带过来的参数
        const params = this.$route.query.settled_id;
        CashApi.orderDetail({
              settled_id: params
            },
            true
          )
          .then(data => {
            self.loading = false;
            self.detail = data.data.model;
            self.order = data.data.order;
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /*取消*/
      cancelFunc() {
        this.$router.back(-1);
      }

    }
  };
</script>
<style>
  .table-wrap {
    padding: 20px;
    padding-top: 0;
  }

  .common-form-data {
    margin-left: 15px;
    font-weight: 500;
  }

  .tips {
    padding: 15px;
    margin-bottom: 20px;
  }

  .tips_tit {
    font-size: 22px;
    margin-bottom: 10px;
  }

  .tips_txt {
    line-height: 25px;
    color: #666;
    font-size: 14px;
  }

  .detail_prici {
    font-size: 20px;
    color: #000;
    font-weight: bold;
    margin-top: 10px;
    max-width: 250px;
  }
</style>
