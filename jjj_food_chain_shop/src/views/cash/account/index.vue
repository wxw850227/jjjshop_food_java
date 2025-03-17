<template>
  <div class="home" v-loading="loading">
    <div class="cash cash_t_common cash_t_brief">
      <div class="cash-header">
        <div>
          <span class="cash_t_title">账户概况</span>
        </div>
      </div>
      <div class="cash-body">
        <div class="content">
          <p class="title">
            可用余额(元) <span class="subhead withdrawal ns-text-color"></span>
          </p>
          <p class="money">{{ supplier ? supplier.money : "N/A" }}</p>
          <!-- <p class="money">{{supplier.money}}</p> -->
        </div>
        <div class="d-s-c">
          <el-button
            v-auth="'/cash/account/apply'"
            class="mr10"
            type="primary"
            @click="is_apply = true"
            >提现</el-button
          >
          <el-button
            v-auth="'/cash/account/account'"
            plain
            @click="is_accountmange = true"
            >提现方式</el-button
          >
        </div>
      </div>
    </div>
    <div class="cash cash_t_common cash_t_brief">
      <div class="cash-header">
        <div>
          <span class="cash_t_title">提现概况</span>
        </div>
      </div>
      <!--提现管理-->
      <div class="common-seach-wrap pl16 pt16">
        <el-form size="small" :model="searchForm" class="">
          <el-form-item label="" v-auth="'/auth/shop'">
            选择店铺：
            <el-select
              v-model="searchForm.shop_supplier_id"
              placeholder="请选择"
            >
              <el-option
                v-for="item in supplierList"
                :key="item.shop_supplier_id"
                :label="item.name"
                :value="item.shop_supplier_id"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="">
            开始时间：
            <el-date-picker
              v-model="searchForm.start_day"
              type="date"
              placeholder="开始时间"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="">
            结束时间：
            <el-date-picker
              v-model="searchForm.end_day"
              type="date"
              placeholder="结束时间"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="onSubmit"
              >查询</el-button
            >
          </el-form-item>
        </el-form>
      </div>
      <div class="product-content">
        <div class="table-wrap">
          <el-table
            size="small"
            :data="tableData"
            border
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column
              prop="supplier.name"
              label="门店"
            ></el-table-column>
            <el-table-column prop="money" label="提现金额">
              <template #default="scope">
                <span class="fb orange">{{ scope.row.money }}</span>
              </template>
            </el-table-column>
            <el-table-column label="打款方式">
              <template #default="scope">
                {{ scope.row.pay_type.text }}
              </template>
            </el-table-column>
            <el-table-column label="打款信息">
              <template #default="scope">
                <span v-if="scope.row.pay_type.value == 10"
                  >姓名:{{ scope.row.account.alipay_name }}<br />
                  账号:{{ scope.row.account.alipay_account }}</span
                >
                <span v-if="scope.row.pay_type.value == 20"
                  >姓名:{{ scope.row.account.bank_account }}<br />开户行:{{
                    scope.row.account.bank_name
                  }}
                  <br />账号:{{ scope.row.account.bank_card }}</span
                >
              </template>
            </el-table-column>
            <el-table-column prop="apply_status" label="状态">
              <template #default="scope">
                <span v-if="scope.row.apply_status.value == 10">待审核</span>
                <span v-if="scope.row.apply_status.value == 20">审核通过</span>
                <div v-if="scope.row.apply_status.value == 30">
                  驳回
                  <span class="red">原因：{{ scope.row.reject_reason }}</span>
                </div>
                <span v-if="scope.row.apply_status.value == 40">已打款</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="create_time"
              label="添加时间"
              width="150"
            ></el-table-column>
          </el-table>
        </div>
        <!--分页-->
        <div class="pagination">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
            :current-page="curPage"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="totalDataNumber"
          >
          </el-pagination>
        </div>
      </div>
    </div>
    <!--申请-->
    <Apply
      v-if="is_apply"
      @close="closeApply"
      :shop_supplier_id="searchForm.shop_supplier_id"
    ></Apply>
    <Accountmange
      v-if="is_accountmange"
      @close="closeAccountmange"
      :shop_supplier_id="searchForm.shop_supplier_id"
    >
    </Accountmange>
  </div>
</template>

<script>
import Apply from "../dialog/Apply.vue";
import Accountmange from "../dialog/Accountmange.vue";
import cashApi from "@/api/cash.js";
export default {
  components: {
    Apply,
    Accountmange,
  },
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*一页多少条*/
      pageSize: 15,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*统计信息*/
      supplier: {},
      /*统计数据*/
      tj_data: "",
      /*是否展示申请*/
      is_apply: false,
      is_accountmange: false,
      tableData: [],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      searchForm: {
        start_day: "",
        end_day: "",
        shop_supplier_id: "",
      },
      supplierList: [],
    };
  },
  created() {
    /*获取数据*/
    this.getData();
  },
  methods: {
    /*获取数据*/
    getData() {
      let self = this;
      self.loading = true;
      let Params = self.searchForm;
      Params.page = self.curPage;
      Params.list_rows = self.pageSize;
      cashApi
        .account(Params, true)
        .then((res) => {
          self.supplier = res.data.supplier;
          self.searchForm.shop_supplier_id = res.data.supplier.shop_supplier_id;
          self.tj_data = res.data.tj_data;
          self.tableData = res.data.list.data;
          self.supplierList = res.data.supplierList;
          self.totalDataNumber = res.data.list.total;
          self.loading = false;
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    /*切换菜单*/
    handleClick(tab, event) {
      let self = this;
      self.curPage = 1;
      self.getData();
    },
    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },
    /*选择第几页*/
    handleCurrentChange(val) {
      this.curPage = val;
      this.getData();
    },
    /*打开*/
    onSubmit() {
      this.curPage = 1;
      this.getData();
    },

    /*关闭*/
    closeApply() {
      this.is_apply = false;
    },
    closeAccountmange() {
      this.is_accountmange = false;
    },
    gotoSearch() {
      this.curPage = 1;
      this.getData();
    },
    /* 退保证金*/
    returndeposit() {
      let self = this;
      ElMessageBox.confirm("确认要退吗?", "提示", {
        type: "warning",
      }).then(() => {
        cashApi
          .refund({})
          .then((data) => {
            ElMessage({
              message: "申请成功",
              type: "success",
            });
            self.getData();
          })
          .catch((error) => {
            self.loading = false;
          });
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.apply-container {
  padding: 50px 0;
}

.cash {
  background-color: #fff;
  font: 14px;
}

.cash_t_common {
  margin-top: 15px;
  margin-bottom: 0;
  box-shadow: initial;

  .cash_t_title {
    font-size: 16px;
    font-weight: 600;
  }

  .cash-header {
    padding: 10px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.cash_t_brief .cash-header {
  border-bottom: 0;
  padding-bottom: 0;
}

.cash-header {
  position: relative;
  height: 42px;
  line-height: 42px;
  padding: 0 15px;
  border-bottom: 1px solid #f6f6f6;
  color: #333;
  border-radius: 2px 2px 0 0;
  font-size: 14px;
}

.cash_t_brief .cash_t_title {
  position: relative;
  padding-left: 10px;
}

.cash_t_brief .cash_t_title::before {
  content: "";
  display: inline-block;
  width: 3px;
  height: 14px;
  background-color: #3a8ee6;
  position: absolute;
  left: 0;
  top: 50%;
  border-radius: 5px;
  transform: translateY(-50%);
}

.cash_t_common .cash-body {
  padding: 20px;
}

.cash-body {
  padding-bottom: 0 !important;
  padding-right: 50px !important;
  padding-left: 50px !important;
  flex-wrap: wrap;
  position: relative;
  padding: 10px 15px;
  line-height: 24px;

  .content {
    width: 33.3%;
    display: flex;
    flex-direction: column;
    margin-bottom: 30px;
    justify-content: center;
  }

  .content {
    width: 25%;
    display: flex;
    flex-direction: column;
    margin-bottom: 30px;
    justify-content: center;
  }

  .money {
    font-size: 20px;
    color: #666;
    font-weight: bold;
    margin-top: 10px;
    max-width: 250px;
  }

  .subhead {
    font-size: 12px;
    margin-left: 3px;
    cursor: pointer;
  }
}

.ns-text-color {
  color: #3a8ee6;
}

.date_section {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
</style>
