<template>
  <div>
    <div class="common-level-rail">
      <el-button size="small" type="primary" icon="Plus" @click="addAd"
        >添加菜单</el-button
      >
    </div>
    <div class="table-wrap">
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="menuId" label="ID" width="100"></el-table-column>
        <el-table-column prop="name" label="菜单名称">
          <template #default="scope">
            <div class="text-ellipsis" :title="scope.row.name">
              {{ scope.row.name }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="图标" width="250">
          <template #default="scope">
            <img v-img-url="scope.row.icon" width="50" height="50" />
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="100"></el-table-column>
        <el-table-column prop="isShow" label="是否显示" width="80">
          <template #default="scope">
            <div @click="changeStatus(scope.row)">
              <el-switch
                v-model="scope.row.isShow"
                active-color="#13ce66"
                inactive-color="#cccccc"
              ></el-switch>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="添加时间"
          width="140"
        ></el-table-column>
        <el-table-column
          prop="updateTime"
          label="更新时间"
          width="140"
        ></el-table-column>
        <el-table-column prop="name" label="操作" width="120">
          <template #default="scope">
            <el-button @click="editAd(scope.row)" type="text" size="small"
              >编辑</el-button
            >
            <el-button
              @click="deleteAd(scope.row)"
              type="text"
              size="small"
              v-if="scope.row.appId > 0"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

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
  </div>
</template>

<script>
import PageApi from "@/api/page.js";
export default {
  components: {},
  data() {
    return {
      loadingInstance: null,
      /*表单数据*/
      tableData: [],
      /*是否打开添加弹窗*/
      open_add: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      userModel: {},
      commentData: [],
      loading: true,
      /*一页多少条*/
      pageSize: 10,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
    };
  },
  created() {
    /*获取列表*/
    this.getTableList();
  },
  methods: {
    /*获取列表*/
    getTableList() {
      let self = this;
      let Params = {};
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      PageApi.menuList(Params, true)
        .then((data) => {
          self.loading = false;
          self.tableData = data.data.records;
          self.totalDataNumber = data.data.total;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*添加*/
    addAd() {
      this.$router.push({
        path: "/page/page/mymenu/add",
      });
    },

    /*编辑*/
    editAd(row) {
      let params = row.menuId;
      this.$router.push({
        path: "/page/page/mymenu/edit",
        query: {
          menuId: params,
        },
      });
    },
    /*修改状态*/
    changeStatus(item) {
      let self = this;
      self.loadingInstance = ElLoading.service({
        lock: true,
        text: "正在处理",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      PageApi.editMenu({
        menuId: item.menuId,
        isShow: item.isShow,
      })
        .then((data) => {
          self.loadingInstance.close();
        })
        .catch((data) => {
          self.loadingInstance.close();
          ElMessage({
            message: "处理失败，请重试",
            type: "warning",
          });
        });
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

    /*删除广告*/
    deleteAd(row) {
      let self = this;
      ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          self.loading = true;
          PageApi.deleteMenu(
            {
              menuId: row.menuId,
            },
            true
          )
            .then((data) => {
              ElMessage({
                message: data.msg,
                type: "success",
              });
              self.loading = false;
              self.getTableList();
            })
            .catch((error) => {});
        })
        .catch(() => {});
    },

    handleClick(tab, event) {},

    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_add = e.openDialog;
        if (e.type == "success") {
          this.getTableList();
        }
      }
      if (f == "edit") {
        this.open_edit = e.openDialog;
        if (e.type == "success") {
          this.getTableList();
        }
      }
    },
  },
};
</script>

<style></style>
