<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <div class="common-form">支付方式设置</div>
      <el-form-item label="选择支付方式">
        <div class="paytype">
          <div
            v-for="(item, key, index) in platform"
            :label="item.value"
            :key="index"
          >
            <div class="fb">{{ item.name }}</div>
            <el-checkbox-group v-model="form.payType[key].payType">
              <el-checkbox
                v-for="(pay_item, pay_index) in item.payType"
                :label="payType[pay_item].value"
                :key="pay_index"
                >{{ payType[pay_item].name }}</el-checkbox
              >
            </el-checkbox-group>
          </div>
        </div>
      </el-form-item>
      <div class="common-form">微信支付设置</div>
      <el-form-item label="证书类型">
        <div>
          <el-radio v-model="form.wxPayKind" :label="3">V3</el-radio>
        </div>
      </el-form-item>
      <el-form-item label="微信支付商户号 MCHID">
        <el-input v-model="form.mchid" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="微信支付密钥 APIKEY ">
        <el-input v-model="form.apikey" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="微信支付验签类型">
        <div>
          <el-radio v-model="form.wxSignType" :label=0
          >证书</el-radio
          >
          <el-radio v-model="form.wxSignType" :label=1
          >公钥</el-radio
          >
        </div>
      </el-form-item>
      <el-form-item label="微信证书序列号/公钥ID">
        <el-input
          :rows="4"
          placeholder="微信商户平台-API安全-平台证书/支付公钥-序列号/公钥ID,将序列号/公钥ID的全部内容复制进来"
          v-model="form.wechatpaySerial"
          class="max-w460"
        ></el-input>
        <div class="tips">
          微信商户平台-API安全-V3平台证书-序列号,将序列号的全部内容复制进来
        </div>
      </el-form-item>
      <template v-if="form.wxSignType===1">
        <el-form-item label="支付公钥pub_key.pem">
          <el-input
              type="textarea"
              :rows="4"
              v-model="form.pubKeyPem "
              placeholder="使用文本编辑器打开pub_key.pem文件，将文件的全部内容复制进来"
              class="max-w460"
          ></el-input>
          <div class="tips">
            使用文本编辑器打开pub_key.pem文件，将文件的全部内容复制进来
          </div>
        </el-form-item>
      </template>
      <el-form-item label="apiclient_cert.pem" v-if="form.wxPayKind == 3">
        <el-input
          type="textarea"
          :rows="4"
          placeholder="使用文本编辑器打开apiclient_cert.pem文件，将文件的全部内容复制进来"
          v-model="form.certPem"
          class="max-w460"
        ></el-input>
        <div class="tips">
          使用文本编辑器打开apiclient_cert.pem文件，将文件的全部内容复制进来
        </div>
      </el-form-item>
      <el-form-item label="apiclient_key.pem" v-if="form.wxPayKind == 3">
        <el-input
          type="textarea"
          :rows="4"
          placeholder="使用文本编辑器打开apiclient_key.pem文件，将文件的全部内容复制进来"
          v-model="form.keyPem"
          class="max-w460"
        ></el-input>
        <div class="tips">
          使用文本编辑器打开apiclient_key.pem文件，将文件的全部内容复制进来
        </div>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="primary" @click="onSubmit">提交</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import AppSettingApi from "@/api/appsetting.js";
import { formatModel } from "@/utils/base.js";
export default {
  data() {
    return {
      form: {
        payType: [],
        mchid: "",
        apikey: "",
        alipayAppid: "",
        alipayPublickey: "",
        alipayPrivatekey: "",
        wxCashType: 2,
        aliCashType: 1,
        p12: null,
        wxPayKind: 3,
        certPem: "",
        keyPem: "",
        wechatpaySerial: "",
        wxSignType:0,
        pubKeyPem: "",
      },
      app: {},
      payType: [],
      platform: [],
    };
  },
  created() {
    this.getData();
  },

  methods: {
    /*获取数据*/
    getData() {
      let self = this;
      AppSettingApi.payDetail({}, true)
        .then((res) => {
          self.app = res.data.app;
          self.payType = res.data.payType;
          self.platform = res.data.platform;
          self.form = formatModel(self.form, res.data.app);
          self.form.wxPayKind = 3;
          if (self.app.payTypeJson && self.app.payTypeJson !== "") {
            self.form.payType = JSON.parse(
              JSON.stringify(self.app.payTypeJson)
            );
            Object.getOwnPropertyNames(self.form.payType).forEach(function (
              key
            ) {
              for (let i = 0; i < self.form.payType[key].payType.length; i++) {
                self.form.payType[key].payType[i] = parseInt(
                  self.form.payType[key].payType[i]
                );
              }
            });
          } else {
            self.form.payType = deepClone(res.data.platform);
          }
          console.log(self.form);
        })
        .catch((error) => {
          console.log(error);
        });
    },

    //提交表单
    onSubmit() {
      let self = this;
      let params = self.form;
      AppSettingApi.editPay(params, true)
        .then((data) => {
          ElMessage({
            message: "恭喜你，设置成功",
            type: "success",
          });
        })
        .catch((error) => {});
    },
    /*选择上传图片*/
    fileChange(e) {
      console.log(e);
    },

    /*选择图片之前*/
    onBeforeUploadP12(file) {
      return true;
    },

    /*上传图片*/
    UploadP12(param) {
      let self = this;
      const loading = ElLoading.service({
        lock: true,
        text: "文件上传中,请等待",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      const formData = new FormData();
      formData.append("iFile", param.file);
      AppSettingApi.uploadP12(formData)
        .then((response) => {
          loading.close();
          ElMessage({
            message: "本次上传文件成功",
            type: "success",
          });
          param.onSuccess(); // 上传成功的图片会显示绿色的对勾
        })
        .catch((response) => {
          loading.close();
          ElMessage({
            message: "本次上传文件失败",
            type: "warning",
          });
          param.onError();
        });
    },
  },
};
</script>

<style>
.paytype {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>
