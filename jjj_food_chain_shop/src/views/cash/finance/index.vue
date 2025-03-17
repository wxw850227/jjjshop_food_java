<template>
  <div class="user clearfix">
    <div class="common-seach-wrap ww100 fr">
      <el-form size="small" :inline="true" :model="formInline" class="demo-form-inline d-s-c">

        <div class="date_section d-b-c">
          <div class="flex-1">
            <el-form-item label="选择店铺"   v-auth="'/auth/shop'">
              <el-select size="small" v-model="formInline.shop_supplier_id" placeholder="请选择" @change="selectId">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="(item, index) in supplierList" :key="index" :label="item.name" :value="item.shop_supplier_id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="查询日期">
               <el-radio-group v-model="formInline.type" size="medium">
                    <el-radio-button label="1" >今天</el-radio-button>
                    <el-radio-button label="2">近七天</el-radio-button>
                    <el-radio-button label="3">近十五天</el-radio-button>
                    <el-radio-button label="4">自定义时间</el-radio-button>
                  </el-radio-group>
            </el-form-item>
          </div>
          <div>
            <el-form-item label="起始时间" v-if="formInline.type==4">
              <div class="block">
                <span class="demonstration"></span>
                <el-date-picker
                  size="small"
                  v-model="formInline.time"
                  type="daterange"
                  value-format="YYYY-MM-DD"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                ></el-date-picker>
              </div>
            </el-form-item>
          </div>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="onSubmit">查询</el-button>
          </el-form-item>
        </div>

      </el-form>
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="time" label="日期"></el-table-column>
          <el-table-column prop="order_count" label="有效订单数"></el-table-column>
          <el-table-column prop="total_money" label="营业总额"></el-table-column>
          <el-table-column prop="refund_money" label="退款金额"></el-table-column>
          <el-table-column prop="revenue_money" label="预计收入"></el-table-column>
          <el-table-column prop="create_time" label="添加时间"></el-table-column>
          <el-table-column prop='settled_id' fixed="right" label="操作" width="120" >
            <template #default="scope">
                <el-button  @click="addClick(scope.row)" type="text" size="small" v-auth="'/cash/finance/detail'">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!--分页-->
      <div class="pagination">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background :current-page="curPage"
          :page-size="pageSize" layout="total, prev, pager, next, jumper"
          :total="totalDataNumber">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
  import CashApi from '@/api/cash.js';
  export default {
    components: {},
    data() {
      return {
        /*是否加载完成*/
        loading: true,
        /*列表数据*/
        tableData: [],
        /*门店列表数据*/
        storeList: [],
        /*一页多少条*/
        pageSize: 20,
        /*一共多少条数据*/
        totalDataNumber: 0,
        /*当前是第几页*/
        curPage: 1,
        formInline: {
          type:1,
          shop_supplier_id:0,
          time:'',
        },
        is_settled:'全部',

        /*是否打开添加弹窗*/
        open_add: false,
        /*是否打开编辑弹窗*/
        open_edit: false,
        /*当前编辑的对象*/
        userModel: {},
        supplierList:[]
      };
    },
    created() {

      /*获取列表*/
      this.getTableList();

    },
    methods: {

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

      /*获取列表*/
      getTableList() {
        let self = this;
        let Params = this.formInline;
        Params.page = self.curPage;
        Params.list_rows = self.pageSize;
        self.loading = true;
        CashApi.financeIndex(Params, true)
          .then(res => {
            self.loading = false;
            self.tableData = res.data.list.data;
            self.supplierList = res.data.supplierList;
            self.totalDataNumber = res.data.list.total;
          })
          .catch(error => {
            self.loading = false;
          });
      },
      getIssettled(val){
        this.formInline.is_settled=val;
        console.log(this.formInline.is_settled)
      },
      /*搜索查询*/
      onSubmit() {
        this.curPage = 1;
        this.getTableList();
      },
    /*打开添加*/
    addClick(row) {
      let self = this;
      let params = row.finance_id;
      self.$router.push({
        path: '/cash/finance/detail',
        query: {
          finance_id: params
        }
      });
    },
    selectId(e){
      this.formInline.shop_supplier_id=e;
      this.getData()
    }
    }
  };
</script>

<style>
  .date_section {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-bottom: 18px;
  }
  .demo-form-inline{
    display: flex;
    align-items: center;
  }
</style>
