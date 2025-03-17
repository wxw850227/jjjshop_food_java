<template>
  <div class="product-add">
    <!--form表单-->
    <el-form ref="form" :model="form" label-width="150px">
      <div class="common-form">平台后台设置</div>
      <el-form-item
        label="平台系统名称"
        :rules="[{ required: true, message: ' ' }]"
        prop="shopName"
      >
        <el-input
          v-model="form.shopName"
          placeholder="平台名称"
          class="max-w460"
        ></el-input>
        <div class="tips">shop端平台名称，显示在登录页</div>
      </el-form-item>
      <el-form-item label="平台登录背景" prop="shopBgImg">
        <el-input
          v-model="form.shopBgImg"
          placeholder="平台登录背景"
          class="max-w460"
        ></el-input>
        <div class="tips">
          shop端平台登录背景，不填则为系统默认登录背景，填写网络地址
        </div>
      </el-form-item>
      <el-form-item label="平台登录logo" prop="shopLogoImg">
        <el-input
          v-model="form.shopLogoImg"
          placeholder="平台登录logo"
          class="max-w460"
        ></el-input>
        <div class="tips">
          shop端平台登录logo，不填则为系统默认登录logo，填写网络地址
        </div>
      </el-form-item>
      <!--提交-->
      <div class="button-wrapper">
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
      /*是否正在加载*/
      loading: false,
      /*form表单数据*/
      form: {
        shopName: "",
        shopBgImg: "",
        shopLogoImg: "",
        cashierName: "",
        cashierBgImg: "",
        weixinService: {
          isOpen: 0,
          appId: "",
          mchId: "",
          apikey: "",
          certPem: "",
          keyPem: "",
        },
      },
    };
  },
  created() {
    this.getParams();
  },

  methods: {
    /*获取配置数据*/
    getParams() {
      let self = this;
      SettingApi.serviceDetail({}, true)
        .then((res) => {
          self.form.shopName = res.data.shopName;
          self.form.shopBgImg = res.data.shopBgImg;
          self.form.shopLogoImg = res.data.shopLogoImg;
          self.form.cashierName = res.data.cashierName;
          self.form.cashierBgImg = res.data.cashierBgImg;
          if (res.data.weixinService) {
            self.form.weixinService = res.data.weixinService;
            self.form.weixinService.isOpen = parseInt(
              self.form.weixinService.isOpen
            );
          }
          self.loading = false;
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    /*提交*/
    onSubmit() {
      let self = this;
      let params = this.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          SettingApi.editService(params, true)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "恭喜你，设置成功",
                type: "success",
              });
              self.$router.push("/setting/Index");
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },
  },
};
</script>
<style>
.tips {
  color: #ccc;
  width: 100%;
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}

input[type="number"] {
  -moz-appearance: textfield;
}

.button-wrapper {
  display: flex;
}
</style>
