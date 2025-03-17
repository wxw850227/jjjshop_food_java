<template>
  <div class="product-add">
    <div class="common-form">腾讯地图KEY设置</div>
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="150px">
      <el-form-item label="腾讯地图KEY">
        <el-input v-model.trim="form.txKey" class="max-w460 mr10"></el-input>
        <a
          href="https://lbs.qq.com/console/mykey.html?console=mykey"
          target="_blank"
          >腾讯地图KEY申请</a
        >
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="primary" @click="onSubmit" :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
import SettingApi from "@/api/setting.js";

export default {
  data() {
    return {
      /*form表单数据*/
      form: {
        txKey: "",
      },
      loading: false,
    };
  },
  created() {
    this.getParams();
  },

  methods: {
    getParams() {
      let self = this;
      SettingApi.mapDetail({}, true)
        .then((data) => {
          if (data.code == 1) {
            self.form.txKey = data.data.txKey;
          }
        })
        .catch((error) => {});
    },
    onSubmit() {
      let self = this;
      self.loading = true;
      let form = self.form;
      SettingApi.editMap(form, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "恭喜你，设置成功",
            type: "success",
          });
          self.$router.push("/setting/map/index");
        })
        .catch((error) => {
          self.loading = false;
        });
    },
  },
};
</script>

<style scoped>
.tips {
  color: #ccc;
}
</style>
