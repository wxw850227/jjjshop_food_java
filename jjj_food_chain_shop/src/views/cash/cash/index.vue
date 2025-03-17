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
            可用余额(元)
            <span class="subhead withdrawal ns-text-color" @click="onSubmit"
              >提现</span
            >
          </p>
          <p class="money">{{ supplier.money }}</p>
        </div>
      </div>
    </div>
    <div class="cash cash_t_common cash_t_brief">
      <div class="cash-header">
        <div>
          <span class="cash_t_title">提现概况</span>
        </div>
      </div>
      <div class="date_section">
        <div class="block mr10">
          <el-date-picker
            v-model="searchForm.start_day"
            type="date"
            placeholder="开始时间"
          >
          </el-date-picker>
        </div>
        <div class="block mr10">
          <el-date-picker
            v-model="searchForm.end_day"
            type="date"
            placeholder="结束时间"
          >
          </el-date-picker>
        </div>
        <el-button @click="gotoSearch">搜索</el-button>
      </div>
      <el-tabs v-model="searchForm.flow_type" @tab-click="handleClick">
        <el-tab-pane label="全部" name="0"></el-tab-pane>
        <el-tab-pane label="收入" name="10"></el-tab-pane>
        <el-tab-pane label="支出" name="20"></el-tab-pane>
      </el-tabs>
      <template>
        <el-table :data="tableData" stripe style="width: 100%; margin: 30px 0">
          <el-table-column prop="money" label="金额（元）"> </el-table-column>
          <el-table-column prop="type" label="收支类型">
            <template #default="scope">
              <span v-if="scope.row.flow_type == 10" class="red">收入</span>
              <span v-if="scope.row.flow_type == 20">支出</span>
            </template>
          </el-table-column>
          <el-table-column prop="create_time" label="时间"></el-table-column>
          <el-table-column prop="describe" label="说明"></el-table-column>
        </el-table>
      </template>
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
        ></el-pagination>
      </div>
    </div>
    <!--申请-->
    <Apply v-if="is_apply" @close="closeApply"></Apply>
  </div>
</template>

<script>
import Apply from "../dialog/Apply.vue";
import cashApi from "@/api/cash.js";
export default {
  components: {
    Apply,
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
      tableData: [],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      searchForm: {
        start_day: "",
        end_day: "",
        flow_type: 0,
      },
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
        .index(Params, true)
        .then((res) => {
          self.supplier = res.data.supplier;
          self.tj_data = res.data.tj_data;
          self.tableData = res.data.cash_list.data;
          self.totalDataNumber = res.data.cash_list.total;
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
      this.is_apply = true;
    },

    /*关闭*/
    closeApply() {
      this.is_apply = false;
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
  display: flex;
  justify-content: space-around;
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
