<template>
  <el-dialog
    title="修改管理员"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <!--form表单-->
    <el-form
      size="small"
      ref="form"
      :model="form"
      :rules="formRules"
      :label-width="formLabelWidth"
      v-loading="loading"
    >
      <el-form-item label="用户名" prop="userName"
        ><el-input v-model="form.userName" placeholder="请输入用户名"></el-input
      ></el-form-item>
      <el-form-item label="所属角色" prop="accessId">
        <el-select v-model="form.accessId" :multiple="true">
          <el-option
            v-for="item in roleList"
            :value="item.roleId"
            :key="item.roleId"
            :label="item.roleName"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="登录密码" prop="password"
        ><el-input
          v-model="form.password"
          placeholder="请输入登录密码"
          type="password"
        ></el-input
      ></el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword"
        ><el-input
          v-model="form.confirmPassword"
          placeholder="请输入确认密码"
          type="password"
        ></el-input
      ></el-form-item>
      <el-form-item label="姓名" prop="realName"
        ><el-input v-model="form.realName"></el-input
      ></el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit" :loading="loading"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script>
import AuthApi from "@/api/auth.js";
export default {
  data() {
    return {
      /*左边长度*/
      formLabelWidth: "120px",
      /*是否显示*/
      loading: false,
      /*是否显示*/
      dialogVisible: false,
      /*form表单对象*/
      form: {
        shopUserId: 0,
        userName: "",
        realName: "",
        roleId: [],
        password: "",
        confirmPassword: "",
        accessId: [],
      },
      /*当前角色*/
      accessId: [],
      /*角色对象*/
      roleList: [],
      /*form验证*/
      formRules: {
        userName: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
        accessId: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
        confirmPassword: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
        realName: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
      },
    };
  },
  props: ["open", "shopUserId", "roleList"],
  watch: {
    open: function (n, o) {
      if (n != o) {
        this.dialogVisible = this.open;
        this.roleList.forEach((item) => {
          this.form.roleId.push(item.roleId);
        });
        this.getData();
      }
    },
  },
  created() {},
  methods: {
    /*获取数据*/
    getData() {
      let self = this;
      AuthApi.userEditInfo({
        shopUserId: this.shopUserId,
      })
        .then((res) => {
          self.loading = false;
          self.form = res.data;
          let obj = res.data;
          obj.accessId = res.data.roleArr;
          obj.password = "";
          self.form = obj;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*修改*/
    onSubmit() {
      let self = this;
      self.loading = true;
      let params = self.form;
      params.roleId = self.form.accessId;
      AuthApi.userEdit(params, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "恭喜你，修改成功",
            type: "success",
          });
          self.dialogFormVisible(true);
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*关闭弹窗*/
    dialogFormVisible(e) {
      if (e) {
        this.$emit("close", {
          type: "success",
          openDialog: false,
        });
      } else {
        this.$emit("close", {
          type: "error",
          openDialog: false,
        });
      }
    },
  },
};
</script>

<style></style>
