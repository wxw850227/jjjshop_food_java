<template>
  <el-dialog
    title="申请提现"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    width="30%"
  >
    <el-form size="small" :model="form" ref="order">
      <el-form-item
        label="提现金额"
        :label-width="formLabelWidth"
        prop="money"
        :rules="[{ required: true, message: '请输入提现金额' }]"
      >
        <el-input
          type="number"
          v-model="form.money"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="打款方式"
        :label-width="formLabelWidth"
        prop="pay_type"
        :rules="[{ required: true, message: '请输入打款方式' }]"
      >
        <el-radio v-model="form.pay_type" :label="10">支付宝</el-radio>
        <el-radio v-model="form.pay_type" :label="20">银行卡</el-radio>
        <!-- <el-radio v-model="form.pay_type" :label="30">微信</el-radio> -->
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <el-button type="primary" @click="submitFunc()" :loading="loading"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script>
import cashApi from "@/api/cash.js";
export default {
  data() {
    return {
      order_id: 0,
      loading: false,
      /*左边长度*/
      formLabelWidth: "100px",
      /*是否显示*/
      dialogVisible: true,
      /*表单*/
      form: {
        money: "",
        pay_type: 10,
      },
    };
  },
  props: ["shop_supplier_id"],
  created() {},
  methods: {
    //获取数据

    /*确认事件*/
    submitFunc(e) {
      let self = this;
      self.$refs.order.validate((valid) => {
        if (valid) {
          self.loading = true;
          self.form.shop_supplier_id = self.shop_supplier_id;
          cashApi
            .apply(self.form, true)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "修改成功",
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
    dialogFormVisible() {
      this.$emit("close", { openDialog: false });
    },
  },
};
</script>

<style></style>
