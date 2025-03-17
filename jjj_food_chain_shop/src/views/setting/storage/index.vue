<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--文件上传设置-->
      <div class="common-form">文件上传设置</div>
      <el-form-item label="默认上传方式">
        <div>
          <el-radio v-model="form.current" label="Local" @change="getRadio"
            >本地 (不推荐)</el-radio
          >
          <el-radio v-model="form.current" label="QiNiu" @change="getRadio"
            >七牛云存储</el-radio
          >
          <el-radio v-model="form.current" label="AliYun" @change="getRadio"
            >阿里云OSS</el-radio
          >
          <el-radio v-model="form.current" label="QCloud" @change="getRadio"
            >腾讯云COS</el-radio
          >
        </div>
      </el-form-item>
      <!--七牛云存储-->
      <div v-if="form.current == 'QiNiu'">
        <el-form-item label="存储空间名称 Bucket"
          ><el-input v-model="form.qiNiu.bucket" class="max-w460"></el-input
        ></el-form-item>
        <el-form-item label="ACCESS_KEY AK"
          ><el-input v-model="form.qiNiu.accessKey" class="max-w460"></el-input
        ></el-form-item>
        <el-form-item label="SECRET_KEY SK"
          ><el-input v-model="form.qiNiu.secretKey" class="max-w460"></el-input
        ></el-form-item>
        <el-form-item label="空间域名 Domain">
          <el-input v-model="form.qiNiu.domain" class="max-w460"></el-input>
          <div class="tips">
            请补全http:// 或 https://，例如：http://static.cloud.com
          </div>
        </el-form-item>
      </div>
      <!--阿里云OSS-->
      <div v-if="form.current == 'AliYun'">
        <el-form-item label="存储空间名称 Bucket"
          ><el-input v-model="form.aliYun.bucket" class="max-w460"></el-input
        ></el-form-item>
        <el-form-item label="AccessKeyId"
          ><el-input
            v-model="form.aliYun.accessKeyId"
            class="max-w460"
          ></el-input
        ></el-form-item>
        <el-form-item label="AccessKeySecret"
          ><el-input
            v-model="form.aliYun.accessKeySecret"
            class="max-w460"
          ></el-input
        ></el-form-item>
        <el-form-item label="外网endpoint">
          <el-input v-model="form.aliYun.endpoint" class="max-w460"></el-input>
          <div class="tips">以杭州为例：oss-cn-hangzhou.aliyuncs.com</div>
        </el-form-item>
        <el-form-item label="空间域名 Domain">
          <el-input v-model="form.aliYun.domain" class="max-w460"></el-input>
          <div class="tips">
            请补全http:// 或 https://，例如：http://static.cloud.com
          </div>
        </el-form-item>
      </div>
      <!--腾讯云COS-->
      <div v-if="form.current == 'QCloud'">
        <el-form-item label="存储空间名称 Bucket"
          ><el-input v-model="form.qCloud.bucket"></el-input
        ></el-form-item>
        <el-form-item label="所属地域 Region">
          <el-input v-model="form.qCloud.region"></el-input>
          <div class="tips">
            请填写地域简称，例如：ap-beijing、ap-hongkong、eu-frankfurt
          </div>
        </el-form-item>
        <el-form-item label="SecretId"
          ><el-input v-model="form.qCloud.secretId"></el-input
        ></el-form-item>
        <el-form-item label="SecretKey"
          ><el-input v-model="form.qCloud.secretKey"></el-input
        ></el-form-item>
        <el-form-item label="空间域名 Domain">
          <el-input v-model="form.qCloud.domain"></el-input>
          <div class="tips">
            请补全http:// 或 https://，例如：http://static.cloud.com
          </div>
        </el-form-item>
      </div>

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
      /*切换菜单*/
      // activeIndex: '1',
      /*form表单数据*/
      form: {
        current: "Local",
        maxImage: "",
        maxVideo: "",
        qiNiu: {},
        aliYun: {},
        qCloud: {},
      },
      loading: false,
    };
  },
  created() {
    this.getData();
  },

  methods: {
    getData() {
      let self = this;
      SettingApi.storageDetail({}, true)
        .then((res) => {
          self.form = res.data;
        })
        .catch((error) => {});
    },
    //提交表单
    onSubmit() {
      let self = this;
      self.loading = true;
      let params = this.form;
      SettingApi.editStorage(params, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "恭喜你，上传设置成功",
            type: "success",
          });
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    //监听单选按钮
    getRadio(val) {},
  },
};
</script>

<style>
.tips {
  color: #ccc;
}
</style>
