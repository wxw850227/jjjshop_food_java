<template>
  <el-dialog
    title="添加消息"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form size="small" :model="form" ref="form">
      <el-form-item
        label="消息名称"
        :label-width="formLabelWidth"
        :rules="[{ required: true, message: ' ' }]"
        prop="messageName"
      >
        <el-input
          v-model="form.messageName"
          autocomplete="off"
          placeholder="请输入消息名称"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="名称(英文唯一)"
        :label-width="formLabelWidth"
        :rules="[{ required: true, message: ' ' }]"
        prop="messageEname"
      >
        <el-input
          v-model="form.messageEname"
          autocomplete="off"
          placeholder="请输入消息英文名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="通知对象" :label-width="formLabelWidth">
        <el-select v-model="form.messageTo" placeholder="请选择通知对象">
          <el-option label="会员" :value="10"></el-option>
          <el-option label="商家" :value="20"></el-option>
          <el-option label="供应商" :value="30"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="消息类别" :label-width="formLabelWidth">
        <el-select v-model="form.messageType" placeholder="请选择消息类别">
          <el-option label="订单" :value="10"></el-option>
          <el-option label="分销" :value="20"></el-option>
          <el-option label="通知" :value="30"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="排序" :label-width="formLabelWidth">
        <el-input v-model="form.sort" placeholder="请输入数字"></el-input>
      </el-form-item>
      <el-form-item label="备注" :label-width="formLabelWidth">
        <el-input
          v-model="form.remark"
          autocomplete="off"
          placeholder="请输入备注"
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <el-button type="primary" @click="addClick()" :loading="loading"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script>
import MessageApi from "@/api/message.js";
export default {
  data() {
    return {
      form: {
        status: 0,
        sort: 100,
      },
      categoryList: [],
      sort: "100",
      radio: "1",
      /*左边长度*/
      formLabelWidth: "120px",
      /*是否显示*/
      dialogVisible: false,
      loading: false,
    };
  },
  props: ["open_add"],
  created() {
    this.dialogVisible = this.open_add;
  },
  methods: {
    /*添加插件*/
    addClick() {
      let self = this;
      let params = this.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          MessageApi.addMessage(params, true)
            .then((res) => {
              if (res.code == 1) {
                self.loading = false;
                ElMessage({
                  message: "恭喜你，添加成功",
                  type: "success",
                });
                self.dialogFormVisible(true);
              } else {
                self.loading = false;
              }
            })
            .catch((error) => {});
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

<style></style>
