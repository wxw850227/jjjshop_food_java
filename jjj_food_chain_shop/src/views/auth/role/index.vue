<template>
  <div class="user">
    <div class="common-level-rail">
      <el-button
        size="small"
        type="primary"
        icon="Plus"
        @click="addClick"
        v-auth="'/auth/role/add'"
        >添加角色</el-button
      >
    </div>

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          size="small"
          :data="tableData"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="roleId" label="角色ID"></el-table-column>
          <el-table-column prop="roleName" label="角色名称"></el-table-column>
          <el-table-column prop="sort" label="排序"></el-table-column>
          <el-table-column prop="createTime" label="添加时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="90">
            <template #default="scope">
              <el-button
                @click="editClick(scope.row)"
                type="text"
                size="small"
                v-auth="'/auth/role/edit'"
                >编辑</el-button
              >
              <el-button
                @click="deleteClick(scope.row)"
                type="text"
                size="small"
                v-auth="'/auth/role/delete'"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import AuthApi from "@/api/auth.js";
export default {
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
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
      self.loading = true;
      AuthApi.roleList({}, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*打开添加*/
    addClick() {
      this.$router.push("/auth/role/add");
    },

    /*打开编辑*/
    editClick(row) {
      let self = this;
      this.$router.push({
        path: "/auth/role/edit",
        query: {
          roleId: row.roleId,
        },
      });
    },

    /*刷新心也*/
    refresh() {
      this.reload();
      // 直接使用reload方法
    },

    /*删除*/
    deleteClick(row) {
      let self = this;
      ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          self.loading = true;
          AuthApi.roleDelete(
            {
              roleId: row.roleId,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage({
                  message: "恭喜你，该角色删除成功",
                  type: "success",
                });
                self.getTableList();
              } else {
                self.loading = false;
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {});
    },
  },
};
</script>

<style></style>
