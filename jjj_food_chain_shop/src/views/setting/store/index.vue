<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="150px">
      <!--添加门店-->
      <div class="common-form">平台设置</div>
      <el-form-item
        label="平台名称"
        :rules="[{ required: true, message: ' ' }]"
        prop="name"
      >
        <el-input
          v-model="form.name"
          placeholder="平台名称"
          class="max-w460"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="平台LOGO"
        :rules="[{ required: true, message: ' ' }]"
        prop="logoUrl"
      >
        <div class="ww100">
          <el-button @click="chooseImg('logoUrl')">选择图片</el-button>
        </div>
        <div class="d-c">
          <img class="mt10" v-img-url="form.logoUrl" :width="100" />
          <div class="gray">建议上传图像大小尺寸为100px*100px</div>
        </div>
      </el-form-item>
      <el-form-item
        label="默认昵称"
        :rules="[{ required: true, message: ' ' }]"
        prop="userName"
      >
        <el-input
          v-model.trim="form.userName"
          placeholder="默认昵称"
          class="max-w460"
        ></el-input>
        <div class="tips">
          小程序和H5端用户默认昵称前缀，和用户id组成默认昵称
        </div>
      </el-form-item>

      <el-form-item
        label="默认头像"
        :rules="[{ required: true, message: '请选择默认头像' }]"
      >
        <div class="ww100">
          <el-button @click="chooseImg('avatarUrl')">选择图片</el-button>
        </div>
        <div class="d-c">
          <img class="mt10" v-img-url="form.avatarUrl" :width="100" />
          <div class="gray">建议上传图像大小尺寸为100px*100px</div>
        </div>
      </el-form-item>
      <div class="common-form">小程序设置</div>
      <el-form-item label="小程序发货" prop="isSendWx">
        <el-checkbox v-model.trim="form.isSendWx">向小程序发货</el-checkbox>
        <div class="tips">后台发货后自动向小程序发货</div>
      </el-form-item>
      <el-form-item
        label="登录LOGO"
        :rules="[{ required: true, message: ' ' }]"
        prop="loginLogo"
      >
        <div class="ww100">
          <el-button @click="chooseImg('loginLogo')">选择图片</el-button>
        </div>
        <div class="d-c">
          <img class="mt10" v-img-url="form.loginLogo" :width="100" />
          <div class="gray">建议上传图像大小尺寸为100px*100px</div>
        </div>
      </el-form-item>
      <el-form-item
        label="授权登录描述"
        :rules="[{ required: true, message: ' ' }]"
        prop="loginDesc"
      >
        <el-input
          v-model.trim="form.loginDesc"
          placeholder="授权登录描述"
          class="max-w460"
        ></el-input>
        <div class="tips">小程序授权登录文字描述</div>
      </el-form-item>
      <el-form-item label="是否开启手机号授权" prop="wxPhone">
        <el-checkbox v-model.trim="form.wxPhone">开启手机号授权</el-checkbox>
        <div class="tips">小程序用户登录是否开启手机号授权</div>
      </el-form-item>
      <div class="common-form">日志记录</div>
      <el-form-item label="是否记录查询日志" prop="isGetLog">
        <el-checkbox v-model="form.isGetLog">是否记录查询日志</el-checkbox>
        <div class="tips">如果记录，日志量会有点大</div>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="primary" @click="onSubmit" :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>
    <!--上传图片-->
    <Upload
      v-if="isupload"
      :isupload="isupload"
      :type="type"
      :config="{ total: 1 }"
      @returnImgs="returnImgsFunc"
    >
    </Upload>
  </div>
</template>

<script>
import SettingApi from "@/api/setting.js";
import Upload from "@/components/file/Upload.vue";
import { formatModel } from "@/utils/base.js";
export default {
  components: {
    Upload,
  },
  data() {
    return {
      /*是否正在加载*/
      loading: false,
      /*form表单数据*/
      form: {
        name: "",
        isGetLog: 0,
        wxPhone: "",
        isSendWx: "",
        loginDesc: "",
        avatarUrl: "",
        userName: "",
        logoUrl: "",
        loginLogo: "",
      },
      /*是否打开图片选择*/
      isupload: false,
    };
  },
  created() {
    this.getParams();
  },

  methods: {
    /*获取配置数据*/
    getParams() {
      let self = this;
      SettingApi.storeDetail({}, true)
        .then((res) => {
          let vars = res.data;
          self.form = formatModel(self.form, vars);
          self.loading = false;
        })
        .catch((error) => {});
    },
    /*提交*/
    onSubmit() {
      let self = this;
      let params = self.form;
      console.log(params);
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          SettingApi.editStore(params, true)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "恭喜你，商城设置成功",
                type: "success",
              });
              self.$router.push("/setting/store/index");
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },
    /*选择图片*/
    chooseImg(e) {
      this.type = e;
      this.isupload = true;
    },
    /*关闭选择图片*/
    returnImgsFunc(e) {
      this.isupload = false;
      if (e != null && e.length > 0) {
        if (this.type == "avatarUrl") {
          this.form.avatarUrl = e[0].filePath;
        } else if (this.type == "loginLogo") {
          this.form.loginLogo = e[0].filePath;
        } else if (this.type == "logoUrl") {
          this.form.logoUrl = e[0].filePath;
        }
      }
    },
  },
};
</script>
<style>
.tips {
  color: #ccc;
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}

input[type="number"] {
  -moz-appearance: textfield;
}
</style>
