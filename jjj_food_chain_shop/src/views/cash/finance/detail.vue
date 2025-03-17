<template>
  <div class="pb50" v-loading="loading">
    <!--订单进度-->
    <!--内容-->
    <div class="product-content">
      <store :detail="detail"></store>
      <div class="common-form">交易概况</div>
      <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
          <el-tab-pane label="外卖统计" name="0"></el-tab-pane>
          <el-tab-pane label="店内统计" name="1"></el-tab-pane>
        </el-tabs>
      <div class="table-wrap">
        <el-row :gutter="20"  v-if="activeName==0">
          <el-col :span="6">
            <div class="pb16 grid-content bg-purple">
              <div>营业总额(元)</div>
              <div class="detail_prici">{{ delivery.total_price }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16 grid-content bg-purple">
              <div>预计收入(元)</div>
              <div class="detail_prici">{{ delivery.revenue_money }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16 grid-content bg-purple">
              <div>有效订单(元)</div>
              <div class="detail_prici">{{ delivery.order_count }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16 grid-content bg-purple">
              <div>退款金额(元)</div>
              <div class="detail_prici">{{ delivery.refund_money }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="activeName==1">
          <el-col :span="6">
            <div class="pb16 grid-content bg-purple">
              <div>营业总额(元)</div>
              <div class="detail_prici">{{ store.total_price }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16 grid-content bg-purple">
              <div>预计收入(元)</div>
              <div class="detail_prici">{{ store.revenue_money }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16 grid-content bg-purple">
              <div>有效订单(元)</div>
              <div class="detail_prici">{{ store.order_count }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16 grid-content bg-purple">
              <div>退款金额(元)</div>
              <div class="detail_prici">{{ store.refund_money }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="common-form">订单概况</div>
      <!--内容-->
      <div class="product-content">
        <div class="table-wrap">
          <el-button size="small" type="success" class="mb18" @click="onExport">导出</el-button>
          <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="order_no" label="订单号"></el-table-column>
            <el-table-column prop="delivery_type.text" label="订单类型"></el-table-column>
            <el-table-column prop="order_price" label="应收金额"></el-table-column>
            <el-table-column label="优惠金额">
              <template #default="scope">
                {{(scope.row.order_price - scope.row.pay_price).toFixed(2)}}
              </template>
            </el-table-column>
            <el-table-column prop="pay_price" label="实付金额"></el-table-column>
            <el-table-column label="预计收入">
              <template #default="scope">
                {{(scope.row.pay_price - scope.row.refund_money).toFixed(2)}}
              </template>
            </el-table-column>
            <el-table-column prop="bagPrice" label="包装费(元)"></el-table-column>
            <el-table-column prop="express_price" label="配送费(元)"></el-table-column>
            <el-table-column prop="state_text" label="订单状态"></el-table-column>
            <el-table-column prop='order_id' fixed="right" label="操作" width="120" >
              <template #default="scope">
                  <el-button  @click="addClick(scope.row)" type="text" size="small">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!--分页-->
        <div class="pagination">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background :current-page="curPage"
            :page-size="pageSize" layout="total, prev, pager, next, jumper"
            :total="talDataNumber">
          </el-pagination>
        </div>
      </div>
    </div>
    <div class="common-button-wrapper">
      <el-button size="small" type="info" @click="cancelFunc">返回上一页</el-button>
    </div>
    <popup :isPopup='isPopup' :order_id='order_id' @close="closepop"></popup>
  </div>
</template>

<script>
  import CashApi from '@/api/cash.js';
  import store from './part/store.vue';
  import popup from './part/popup.vue';
  import qs from 'qs';
  import { useUserStore } from '@/store';
  const { userInfo } = useUserStore();
  const { token } = useUserStore();
  import {
    deepClone
  } from '@/utils/base.js';
  export default {
    components:{
      store,
      popup
    },
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
        talDataNumber: 0,
        /*当前是第几页*/
        curPage: 1,
        activeName: '',
        tableData:[],
        deliverylist:[],
        storelist:[],
        delivery:{},
        store:{},
        order_id:{},
        isPopup:false,
		token,
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
        self.loading = true;
        const params = this.$route.query.finance_id;
        CashApi.financeDetail({
              finance_id: params
            },
            true
          )
          .then(data => {
            self.loading = false;
            self.detail = data.data.detail;
            self.store = data.data.store.detail;
            self.delivery = data.data.delivery.detail;
            self.storelist = data.data.store.list.data;
            self.deliverylist = data.data.delivery.list.data;
            self.deliverytotalDataNumber = data.data.delivery.list.total;
            self.storetotalDataNumber = data.data.store.list.total;
            if(self.activeName==0){
              self.tableData = self.deliverylist;
              self.talDataNumber = self.deliverytotalDataNumber;
            }else if(self.activeName==1){
              self.tableData = self.storelist;
              self.talDataNumber = self.storetotalDataNumber;
            }
            self.order = data.data.order;
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /*取消*/
      cancelFunc() {
        this.$router.back(-1);
      },
      /*选择第几页*/
      handleCurrentChange(val) {
        let self = this;
        self.curPage = val;
        self.loading = true;
        self.getTableList();
      },

      /*每页多少条*/
      handleSizeChange(val) {
        this.curPage = 1;
        this.pageSize = val;
        this.getTableList();
      },
      handleClick(e){
        this.getParams();
      },
      addClick(e){
        this.isPopup=true;
        this.order_id=e.order_id;
      },
      closepop(){
        this.isPopup=false;
      },
      onExport(){
        let self = this ;
        let baseUrl = window.location.protocol + '//' + window.location.host;
        let params = {
          order_type:self.activeName,
          finance_id:this.$route.query.finance_id,
		  token:this.token
        }
        window.location.href = baseUrl + '/api/shop/cash.finance/export?' + qs.stringify(params);
      }
    }
  };
</script>
<style lang="scss">
  .el-row {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .el-col {
    border-radius: 4px;
  }

  .grid-content {
    padding: 20px;
    border-radius: 4px;
    min-height: 36px;
  }

  .bg-purple {
    background: #f4f4f4;
  }

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
