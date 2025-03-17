<template>
  <div v-loading="loading">
    <!--添加商城-->
    <div class="common-level-rail">
      <el-button type="primary" @click="addClick">添加平台</el-button>
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          size="small"
          :data="tableData"
          style="width: 100%"
          row-key="access_id"
          border
          default-expand-all
          v-loading="loading"
        >
          <el-table-column prop="appId" label="平台ID"></el-table-column>
          <el-table-column prop="appName" label="平台名称"></el-table-column>
          <el-table-column prop="userName" label="超管账号"></el-table-column>
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-checkbox
                v-model="scope.row.isRecycle"
                @change="(checked) => statusChange(checked, scope.row)"
                >启用
              </el-checkbox>
            </template>
          </el-table-column>
          <el-table-column prop="expireTime" label="过期时间"></el-table-column>
          <el-table-column prop="createTime" label="添加时间"></el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-link
                :href="targetLinkAddress()"
                target="_blank"
                type="primary"
                size="small"
                >进入平台</el-link
              >
              <el-link
                class="ml10"
                @click="editClick(scope.row)"
                type="primary"
                size="small"
                >编辑</el-link
              >
              <el-link
                class="ml10"
                @click="deleteClick(scope.row)"
                type="primary"
                size="small"
                >删除</el-link
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!--添加-->
    <Add
      v-if="open_add"
      :open_add="open_add"
      @closeDialog="closeDialogFunc($event, 'add')"
    ></Add>

    <!--编辑-->
    <Edit
      v-if="open_edit"
      :open_edit="open_edit"
      :curModel="curModel"
      @closeDialog="closeDialogFunc($event, 'edit')"
    >
    </Edit>

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
</template>
<script>
import ShopApi from "@/api/shop.js";
import Edit from "./part/Edit.vue";
import Add from "./part/Add.vue";
import { deepClone } from "@/utils/base.js";
export default {
  components: {
    /*编辑组件*/
    Edit: Edit,
    Add: Add,
  },
  data() {
    return {
      /*是否正在加载*/
      loading: true,
      /*表格数据*/
      tableData: [],
      /*一页多少条*/
      pageSize: 20,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*是否打开添加弹窗*/
      open_add: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      curModel: {},
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    targetLinkAddress() {
      let url = window.location.hostname;
      return `http://${url}/shop/#/login`;
    },

    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.page = val;
      self.loading = true;
      self.getData();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },

    /*获取列表*/
    getData() {
      let self = this;
      ShopApi.shopList(
        {
          pageIndex: self.curPage,
          pageSize: self.pageSize,
        },
        true
      )
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
          self.tableData.forEach(function (message) {
            message.status = message.status === 1 ? true : false;
            message.isRecycle = message.isRecycle === 1 ? false : true;
            message.weixinService = message.weixinService === 1 ? true : false;
            message.isChain = message.isChain === 1 ? true : false;
          });
        })
        .catch((error) => {});
    },

    /*打开添加*/
    addClick() {
      this.open_add = true;
    },

    /*打开编辑*/
    editClick(e) {
      this.open_edit = true;
      this.curModel = deepClone(e);
      this.curModel.isChain = e.isChain === true ? 1 : 0;
      if (this.curModel.expireTime == 0) {
        this.curModel.expireTime = "";
        this.curModel.noExpire = true;
      } else {
        this.curModel.noExpire = false;
      }
    },

    /*关闭*/
    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_add = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
      if (f == "edit") {
        this.open_edit = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
    },

    /*启用*/
    statusChange: function (checked, row) {
      let self = this;
      self.loading = true;
      ShopApi.updateStatus(
        {
          appId: row.appId,
        },
        true
      )
        .then((data) => {
          self.loading = false;
          row.isRecycle = checked;
        })
        .catch((error) => {
          self.loading = false;
          row.isRecycle = !checked;
        });
    },
    /*启用*/
    /*删除商城*/
    deleteClick(row) {
      let self = this;
      ElMessageBox.confirm("删除后不可恢复，确认删除该记录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          self.loading = true;
          ShopApi.deleteShop(
            {
              appId: row.appId,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage({
                  message: data.msg,
                  type: "success",
                });
                self.getTableList();
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {});
    },

    /*进入商城*/
    storeEnter(e) {
      let self = this;
      ShopApi.storeEnter(
        {
          appId: e,
        },
        true
      )
        .then((data) => {})
        .catch((error) => {});
    },
  },
};
</script>
<style>
.el-link.is-underline:hover {
  opacity: 0.8;
}

.el-link.is-underline:hover:after {
  display: none;
}
</style>
