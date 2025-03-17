<template>
  <div v-loading="loading">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="180px">
      <!--编辑角色-->
      <div class="common-form">编辑角色</div>
      <el-form-item
        label="角色名称："
        prop="roleName"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          v-model="form.roleName"
          placeholder="请输入角色名称"
          class="max-w460"
        ></el-input>
      </el-form-item>
      <el-form-item label="权限列表：" v-model="form.accessId">
        <el-tree
          :data="data"
          show-checkbox
          node-key="accessId"
          :default-expand-all="true"
          :default-checked-keys="selectMenu"
          :props="defaultProps"
          @check="handleCheckChange"
        ></el-tree>
      </el-form-item>

      <el-form-item label="排序："
        ><el-input
          type="number"
          v-model="form.sort"
          placeholder="请输入排序"
          class="max-w460"
        ></el-input
      ></el-form-item>

      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc">取消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="onSubmit"
          :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
import AuthApi from "@/api/auth.js";

export default {
  data() {
    return {
      /*是否正在加载*/
      loading: true,
      roleId: 0,
      /*表单数据对象*/
      form: {
        accessId: [],
      },
      data: [],
      /*角色列表*/
      roleList: [],
      /*权限选中*/
      selectMenu: [],
      /*权限树菜单重新自定义字段*/
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },
  created() {
    this.roleId = this.$route.query.roleId;
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*修改角色*/
    onSubmit() {
      let self = this;
      let form = self.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          AuthApi.roleEdit(
            {
              roleId: self.form.roleId,
              roleName: self.form.roleName,
              sort: self.form.sort,
              accessId: self.form.accessId,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "修改成功",
                type: "success",
              });
              self.$router.push("/auth/role/index");
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },

    /*获取所有的数据*/
    getData() {
      let self = this;
      AuthApi.roleEditInfo({
        roleId: self.roleId,
      })
        .then((data) => {
          let obj = self.clearData(data.data.menu, data.data.selectMenu);
          self.selectMenu = data.data.selectMenu;
          self.form = data.data.model;
          self.roleList = data.data.roleList;
          self.data = data.data.menu;
          self.form.accessId = self.selectMenu;
          if (self.form.parentId == 0) {
            self.form.parentId = 0 + "";
          }
          self.loading = false;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*清除数据*/
    clearData(list, authlist) {
      let total = 0;
      let leng = list.length;
      for (let i = 0; i < leng; i++) {
        let item = list[i];
        if (item.children != null) {
          let flag = this.clearData(item.children, authlist);
          if (!flag) {
            let _index = authlist.indexOf(item.accessId);
            if (_index >= 0) {
              authlist.splice(_index, 1);
            }
          }
        }
        if (authlist.indexOf(item.accessId) != -1) {
          total++;
        }
      }
      if (total < leng) {
        return false;
      } else {
        return true;
      }
    },

    /*监听选中*/
    handleCheckChange(data, checked) {
      this.form.accessId = checked.checkedKeys.concat(checked.halfCheckedKeys);
    },

    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
    },
  },
};
</script>

<style lang="scss" scoped>
.basic-setting-content {
}

.product-add {
  padding-bottom: 50px;
}

.img {
  margin-top: 10px;
}
</style>
