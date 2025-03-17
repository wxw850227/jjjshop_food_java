<template>
  <div class="user">
    <!--搜索表单-->
    <div class="common-seach-wrap">
      <el-form
        size="small"
        :inline="true"
        :model="searchForm"
        class="demo-form-inline"
      >
        <el-form-item label="订单号">
          <el-input
            size="small"
            v-model="searchForm.orderNo"
            placeholder="请输入订单号"
          ></el-input>
        </el-form-item>
        <el-form-item label="配送方式">
          <el-select
            size="small"
            v-model="searchForm.styleId"
            placeholder="请选择"
          >
            <el-option label="全部" value=""></el-option>
            <el-option
              v-for="(item, index) in exStyle"
              :key="index"
              :label="item.name"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起始时间">
          <div class="block">
            <span class="demonstration"></span>
            <el-date-picker
              size="small"
              v-model="searchForm.createTime"
              type="daterange"
              value-format="YYYY-MM-DD"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" icon="Search" @click="onSubmit"
            >查询</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="success" @click="onExport"
            >导出</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-tabs v-model="activeName" @tab-change="handleClick">
          <el-tab-pane label="全部订单" name="all">
            <template #label>
              <span
                >全部订单
                <el-tag size="mini">{{ orderCount.all }}</el-tag></span
              >
            </template>
          </el-tab-pane>
          <el-tab-pane :label="'未付款'" name="payment">
            <template #label>
              <span
                >未付款
                <el-tag size="mini">{{ orderCount.payment }}</el-tag></span
              >
            </template>
          </el-tab-pane>
          <el-tab-pane :label="'进行中'" name="process">
            <template #label>
              <span
                >进行中
                <el-tag size="mini">{{ orderCount.process }}</el-tag></span
              >
            </template>
          </el-tab-pane>
          <el-tab-pane :label="'已取消'" name="cancel">
            <template #label>
              <span
                >已取消
                <el-tag size="mini">{{ orderCount.cancel }}</el-tag></span
              >
            </template>
          </el-tab-pane>
          <el-tab-pane :label="'已完成'" name="complete">
            <template #label>
              <span
                >已完成
                <el-tag size="mini">{{ orderCount.complete }}</el-tag></span
              >
            </template>
          </el-tab-pane>
        </el-tabs>
        <el-table
          size="small"
          :data="tableData.data"
          :span-method="arraySpanMethod"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="orderNo" label="订单信息" width="400">
            <template #default="scope">
              <div class="order-code" v-if="scope.row.isTopRow">
                <span
                  class="state-text"
                  :class="{ 'state-text-red': scope.row.orderSource != 10 }"
                  >{{ scope.row.orderSourceText }}</span
                >
                <span class="c_main">订单号：{{ scope.row.orderNo }}</span>
                <span class="pl16">下单时间：{{ scope.row.createTime }}</span>
                <!--是否取消申请中-->
                <span class="pl16" v-if="scope.row.orderStatus == 21">
                  <el-tag effect="dark" size="mini">取消申请中</el-tag>
                </span>
              </div>
              <template v-else>
                <div
                  class="product-info"
                  v-for="(item, index) in scope.row.product"
                  :key="index"
                >
                  <div class="pic">
                    <img v-img-url="item.imagePath" alt="" />
                  </div>
                  <div class="info">
                    <div class="name gray3 product-name">
                      <span>{{ item.productName }}</span>
                    </div>
                    <div class="gray9" v-if="item.productAttr">
                      {{ item.productAttr }}
                    </div>
                    <div class="orange" v-if="item.refund">
                      {{ item.refund.type }}-{{ item.refund.status }}
                    </div>
                  </div>
                  <div class="d-c-c d-c">
                    <div class="orange">￥ {{ item.productPrice }}</div>
                    <div class="gray3">x{{ item.totalNum }}</div>
                  </div>
                </div>
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="payPrice" label="实付款">
            <template #default="scope">
              <div v-if="!scope.row.isTopRow">
                <div class="orange">{{ scope.row.payPrice }}</div>
                <p class="gray9">(含配送费：{{ scope.row.expressPrice }})</p>
                <p class="gray9">(含包装费：{{ scope.row.bagPrice }})</p>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="" label="买家">
            <template #default="scope">
              <div v-if="!scope.row.isTopRow">
                <div>{{ scope.row.user.nickName }}</div>
                <div class="gray9">ID：({{ scope.row.user.userId }})</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="supplier.name"
            label="门店名称"
          ></el-table-column>
          <el-table-column prop="stateText" label="交易状态">
            <template #default="scope">
              <div v-if="!scope.row.isTopRow">
                {{ scope.row.orderStatusText }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="payType" label="支付方式">
            <template #default="scope">
              <div v-if="!scope.row.isTopRow">
                <span class="gray9">{{ scope.row.payTypeText }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="deliveryType" label="配送方式">
            <template #default="scope">
              <div v-if="!scope.row.isTopRow">
                <span class="green">{{ scope.row.deliveryTypeText }}</span>
                <span v-if="scope.row.deliveryType == 30"
                  >手机号:{{ scope.row.user.mobile }}</span
                >
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="200">
            <template #default="scope">
              <div v-if="!scope.row.isTopRow">
                <el-button
                  @click="addClick(scope.row)"
                  type="text"
                  size="small"
                  v-auth="'/takeout/order/detail'"
                  >订单详情
                </el-button>
                <el-button
                  v-if="
                    scope.row.orderStatus == 10 &&
                    scope.row.payStatus == 20 &&
                    scope.row.refundMoney == 0
                  "
                  @click="refundClick(scope.row)"
                  type="text"
                  size="small"
                  v-auth="'/takeout/Operate/refund'"
                  >退款
                </el-button>
                <el-button
                  v-if="
                    scope.row.orderStatus == 10 &&
                    scope.row.deliveryStatus == 10 &&
                    scope.row.payStatus == 20
                  "
                  @click="cancelClick(scope.row)"
                  type="text"
                  size="small"
                  v-auth="'/takeout/operate/orderCancel'"
                  >取消
                </el-button>
                <el-button
                  v-if="
                    scope.row.orderStatus == 10 &&
                    scope.row.deliveryType == 20 &&
                    scope.row.payStatus == 20
                  "
                  @click="verifyClick(scope.row)"
                  type="text"
                  size="small"
                  v-auth="'/takeout/operate/extract'"
                  >核销
                </el-button>
                <el-button
                  v-if="
                    scope.row.deliverSource == 10 &&
                    scope.row.orderStatus == 10 &&
                    scope.row.deliveryType == 10 &&
                    scope.row.payStatus == 20 &&
                    scope.row.deliveryStatus == 20
                  "
                  @click="verifyClick(scope.row)"
                  type="text"
                  size="small"
                  v-auth="'/takeout/operate/extract'"
                  >确认送达
                </el-button>
                <el-button
                  @click="senDada(scope.row)"
                  v-if="
                    scope.row.payStatus == 20 &&
                    scope.row.deliverStatus == 0 &&
                    scope.row.orderStatus == 10 &&
                    scope.row.deliveryType == 10
                  "
                  type="text"
                  size="small"
                  v-auth="'/takeout/operate/sendOrder'"
                  >{{ deliverName.name }}
                </el-button>
                <el-button
                  v-if="
                    scope.row.payType == 20 &&
                    scope.row.payStatus == 20 &&
                    scope.row.wxDeliveryStatus == 10 &&
                    scope.row.paySource == 'wx' &&
                    isSendWx == true
                  "
                  @click="wxdeliveryClick(scope.row)"
                  link
                  type="primary"
                  size="small"
                  v-auth="'/store/order/wxDelivery'"
                  >小程序发货</el-button
                >
              </div>
            </template>
          </el-table-column>
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
        ></el-pagination>
      </div>
    </div>
    <Cancel
      v-if="open_edit"
      :open_edit="open_edit"
      :orderNo="orderNo"
      :orderId="orderId"
      @closeDialog="closeDialogFunc($event, 'edit')"
    >
    </Cancel>
    <!--处理-->
    <refund
      v-if="open_refund"
      :open_edit="open_refund"
      :orderNo="orderNo"
      :orderId="orderId"
      @closeDialog="closerefundDialogFunc($event, 'edit')"
    >
    </refund>
  </div>
</template>

<script>
import OrderApi from "@/api/order.js";
import Cancel from "./dialog/cancel.vue";
import refund from "./dialog/refund.vue";
import qs from "qs";
import { useUserStore } from "@/store";
const { token } = useUserStore();
export default {
  components: {
    Cancel,
    refund,
  },
  data() {
    return {
      /*切换菜单*/
      activeName: "all",
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
      /*一页多少条*/
      pageSize: 20,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*横向表单数据模型*/
      searchForm: {
        orderNo: "",
        styleId: "",
        createTime: [],
      },
      /*配送方式*/
      exStyle: [],
      /*门店列表*/
      shopList: [],
      /*时间*/
      createTime: [],
      /*统计*/
      orderCount: {
        all: 0,
        payment: 0,
        delivery: 0,
        received: 0,
        cancel: 0,
      },
      /*是否打开编辑弹窗*/
      open_edit: false,
      open_refund: false,
      /*当前编辑的对象*/
      orderNo: 0,
      deliverName: "",
      deliverType: "",
      orderId: 0,
      token,
      /*小程序发货*/
      isSendWx: "",
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    wxdeliveryClick(row) {
      let self = this;
      let orderId = row.orderId;
      ElMessageBox.confirm("确定发货吗?", "提示", {
        type: "warning",
      })
        .then(() => {
          OrderApi.takeoutWxDelivery(
            {
              orderId,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "恭喜你，操作成功",
                type: "success",
              });
              self.getData();
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {
          ElMessage({
            type: "info",
            message: "已取消发货",
          });
        });
    },
    /*跨多列*/
    arraySpanMethod(row) {
      if (row.rowIndex % 2 == 0) {
        if (row.columnIndex === 0) {
          return [1, 8];
        }
      }
    },
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.getData();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },

    /*切换菜单*/
    handleClick(tab, event) {
      let self = this;
      self.curPage = 1;
      self.getData();
    },

    /*获取列表*/
    getData() {
      let self = this;
      let Params = this.searchForm;
      Params.dataType = self.activeName;
      Params.page = self.curPage;
      Params.list_rows = self.pageSize;
      self.loading = true;
      OrderApi.takeOrderlist(Params, true)
        .then((res) => {
          let list = [];
          for (let i = 0; i < res.data.list.records.length; i++) {
            let item = res.data.list.records[i];
            let topitem = {
              orderNo: item.orderNo,
              createTime: item.createTime,
              orderSource: item.orderSource,
              orderSourceText: item.orderSourceText,
              isTopRow: true,
              orderStatus: item.orderStatus,
            };
            list.push(topitem);
            list.push(item);
          }
          self.tableData.data = list;
          self.deliverName = res.data.deliverName;
          self.deliverType = res.data.deliver.default;
          self.totalDataNumber = res.data.list.total;
          self.exStyle = res.data.exStyle;
          self.orderCount = res.data.orderCount;
          self.isSendWx = res.data.isSendWx;
          self.loading = false;
        })
        .catch((error) => {});
    },

    /*打开添加*/
    addClick(row) {
      let self = this;
      let params = row.orderId;
      self.$router.push({
        path: "/takeout/order/detail",
        query: {
          orderId: params,
        },
      });
    },
    /*核销*/
    verifyClick(row) {
      let self = this;
      let extractForm = {};
      ElMessageBox.confirm("是否确认此操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          extractForm.orderId = row.orderId;
          OrderApi.takeExtract(extractForm, true)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "恭喜你，操作成功",
                type: "success",
              });
              self.getData();
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {
          ElMessage({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    senDada(row) {
      let self = this;
      let extractForm = {};
      ElMessageBox.confirm("是否确认此操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          extractForm.orderId = row.orderId;
          OrderApi.sendDada(extractForm, true)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "恭喜你，操作成功",
                type: "success",
              });
              self.getData();
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {
          ElMessage({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    /*搜索查询*/
    onSubmit() {
      this.curPage = 1;
      this.tableData = [];
      this.getData();
    },
    onExport: function () {
      let self = this;
      let search = this.searchForm;
      let Params = {};
      Params.styleId = search.styleId;
      Params.dataType = self.activeName;
      Params.orderNo = search.orderNo;
      Params.createTime = search.createTime;
      Params.tokenshop = self.token;
      console.log(Params);
      let baseUrl = import.meta.env.VITE_BASIC_URL;
      if (!baseUrl) {
        baseUrl = window.location.protocol + "//" + window.location.host;
      }
      //let baseUrl = "http://127.0.0.1:8889";
      window.location.href =
        baseUrl + "/api/shop/takeout/operate/export?" + qs.stringify(Params);
    },
    /*打开取消*/
    cancelClick(item) {
      this.orderNo = item.orderNo;
      this.orderId = item.orderId;
      this.open_edit = true;
    },
    refundClick(item) {
      this.orderNo = item.orderNo;
      this.orderId = item.orderId;
      this.open_refund = true;
    },
    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      if (f == "edit") {
        this.open_edit = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
    },
    /*关闭弹窗*/
    closerefundDialogFunc(e, f) {
      if (f == "edit") {
        this.open_refund = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
    },
  },
};
</script>
<style lang="scss">
.product-info {
  padding: 10px 0;
  border-top: 1px solid #eeeeee;
}

.order-code .state-text {
  padding: 2px 4px;
  border-radius: 4px;
  background: #808080;
  color: #ffffff;
}

.order-code .state-text-red {
  background: red;
}

.table-wrap .product-info:first-of-type {
  border-top: none;
}

.table-wrap .el-table__body tbody .el-table__row:nth-child(odd) {
  background: #f5f7fa;
}
</style>
