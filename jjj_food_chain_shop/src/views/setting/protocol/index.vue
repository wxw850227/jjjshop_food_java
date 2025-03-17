<template>
  <div class="product-add" v-if="!loading">
    <el-tabs v-model="activeName">
      <el-tab-pane label="用户协议" name="service"></el-tab-pane>
      <el-tab-pane label="隐私协议" name="privacy"></el-tab-pane>
    </el-tabs>
    <!--用户协议-->
    <Service
      v-show="activeName == 'service'"
      :settingData="settingData"
      @update-service="handleUpdateService"
    ></Service>
    <!--隐私协议-->
    <Privacy
      v-show="activeName == 'privacy'"
      :settingData="settingData"
      @update-privacy="handleUpdatePrivacy"
    ></Privacy>
  </div>
  <div class="common-button-wrapper">
    <el-button type="primary" @click="onSubmit" :loading="loading"
      >提交</el-button
    >
  </div>
</template>
<script>
import SettingApi from "@/api/setting.js";
import Service from "./part/service.vue";
import Privacy from "./part/privacy.vue";
export default {
  components: {
    Service,
    Privacy,
  },
  data() {
    return {
      activeName: "service",
      /*是否正在加载*/
      loading: true,
      /*form表单数据*/
      form: {
        service: "",
        privacy: "",
      },
    };
  },
  mounted() {
    this.getParams();
  },
  methods: {
    handleUpdateService(updatedService) {
      this.form.service = updatedService;
    },
    handleUpdatePrivacy(updatedPrivacy) {
      this.form.privacy = updatedPrivacy;
    },
    /*获取配置数据*/
    getParams() {
      let self = this;
      SettingApi.protocolDetail({}, true)
        .then((res) => {
          let vars = res.data;
          self.settingData = vars;
          self.loading = false;
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    onSubmit() {
      let self = this;
      let params = self.form;
      console.log(params, "params");
      self.loading = true;
      SettingApi.editProtocol(params, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "恭喜你，设置成功",
            type: "success",
          });
          self.getParams();
        })
        .catch((error) => {
          self.loading = false;
        });
    },
  },
};
</script>
<style></style>
