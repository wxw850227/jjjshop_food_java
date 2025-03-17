<template>
  <el-dialog
    title="添加类型"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form size="small" :model="form" :rules="formRules" ref="form">
      <el-form-item
        label="类型名称"
        prop="typeName"
        :label-width="formLabelWidth"
      >
        <el-input v-model="form.typeName" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item
        label="最少人数"
        prop="minNum"
        :label-width="formLabelWidth"
      >
        <el-input v-model.number="form.minNum" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item
        label="最多人数"
        prop="maxNum"
        :label-width="formLabelWidth"
      >
        <el-input v-model.number="form.maxNum" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="sort" :label-width="formLabelWidth">
        <el-input v-model.number="form.sort" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <el-button type="primary" @click="addUser" :loading="loading"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script>
import StoreApi from "@/api/store.js";
export default {
  components: {},
  data() {
    return {
      form: {
        typeName: "",
        minNum: 1,
        maxNum: 1,
        sort: 100,
      },
      formRules: {
        typeName: [
          {
            required: true,
            message: "请输入类型名称",
            trigger: "blur",
          },
        ],
        sort: [
          {
            required: true,
            message: "排序不能为空",
          },
          {
            type: "number",
            message: "排序必须为数字",
          },
        ],
      },
      /*左边长度*/
      formLabelWidth: "120px",
      /*是否显示*/
      dialogVisible: false,
      loading: false,
      /*是否上传图片*/
      isupload: false,
    };
  },
  props: ["open_add", "addform"],
  created() {
    this.dialogVisible = this.open_add;
  },
  methods: {
    /*添加用户*/
    addUser() {
      let self = this;
      let params = self.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          StoreApi.addType(params)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "添加成功",
                type: "success",
              });
              self.dialogFormVisible(true);
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },

    /*关闭弹窗*/
    dialogFormVisible(e) {
      if (e) {
        this.$emit("closeDialog", {
          type: "success",
          openDialog: false,
        });
      } else {
        this.$emit("closeDialog", {
          type: "error",
          openDialog: false,
        });
      }
    },
  },
};
</script>

<style>
.img {
  margin-top: 10px;
}
</style>
