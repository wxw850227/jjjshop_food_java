<template>
  <el-dialog
    title="提现方式"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    width="30%"
  >
    <el-form size="small" :model="form" ref="order">
      <!--      <el-form-item label="提现方式" :label-width="formLabelWidth" prop="pay_type"
        :rules="[{ required: true, message: '请输入提现方式' }]">
        <el-radio v-model="form.pay_type" :label="10">支付宝</el-radio>
        <el-radio v-model="form.pay_type" :label="20">银行卡</el-radio>
      </el-form-item> -->

      <h3>支付宝：</h3>
      <el-form-item
        label="支付宝真实姓名"
        :label-width="formLabelWidth"
        prop="alipay_name"
        :rules="[{ required: true, message: '请输入支付宝真实姓名' }]"
      >
        <el-input
          type="text"
          v-model="form.alipay_name"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="支付宝账号"
        :label-width="formLabelWidth"
        prop="alipay_account"
      >
        <el-input
          type="text"
          v-model="form.alipay_account"
          autocomplete="off"
        ></el-input>
      </el-form-item>

      <h3>银行卡：</h3>
      <el-form-item
        label="开户行名称"
        :label-width="formLabelWidth"
        prop="bank_name"
      >
        <el-input
          type="text"
          v-model="form.bank_name"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="银行开户名"
        :label-width="formLabelWidth"
        prop="bank_account"
      >
        <el-input
          type="text"
          v-model="form.bank_account"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="银行卡号"
        :label-width="formLabelWidth"
        prop="bank_card"
      >
        <el-input
          type="number"
          v-model="form.bank_card"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="真实姓名"
        :label-width="formLabelWidth"
        prop="real_name"
      >
        <el-input
          type="text"
          v-model="form.real_name"
          autocomplete="off"
        ></el-input>
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
      formLabelWidth: "120px",
      /*是否显示*/
      dialogVisible: true,
      /*表单*/
      form: {},
    };
  },
  props: ["shop_supplier_id"],
  created() {
    this.getParams();
  },
  methods: {
    //获取数据
    getParams() {
      let self = this;
      cashApi
        .getAccount(
          {
            shop_supplier_id: self.shop_supplier_id,
          },
          true
        )
        .then((res) => {
          if (res.code == 1) {
            if (res.data.model != null) {
              self.form = res.data.model;
            }
            self.loading = false;
          } else {
            console.log();
          }
        })
        .catch((error) => {});
    },
    /*确认事件*/
    submitFunc(e) {
      let self = this;
      self.$refs.order.validate((valid) => {
        if (valid) {
          self.loading = true;
          self.form.shop_supplier_id = self.shop_supplier_id;
          cashApi
            .setAccount(self.form, true)
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
      this.$emit("close", {
        openDialog: false,
      });
    },
  },
};
</script>

<style></style>
