<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--添加店员-->
      <div class="common-form">编辑门店</div>
      <el-form-item
        label="门店名称"
        prop="name"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          class="max-w460"
          v-model="form.name"
          placeholder="请输入门店名称"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="账号"
        prop="userName"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          :disabled="form.isMain == 1"
          class="max-w460"
          v-model="form.userName"
          placeholder="请输入账号"
        ></el-input>
      </el-form-item>
      <el-form-item label="登录密码" prop="password">
        <el-input
          v-model="form.password"
          placeholder="请输入登录密码"
          type="password"
          class="max-w460"
        ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="form.confirmPassword"
          placeholder="请输入确认密码"
          type="password"
          class="max-w460"
        >
        </el-input>
      </el-form-item>
      <el-form-item
        label="联系人"
        prop="linkName"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          class="max-w460"
          v-model="form.linkName"
          placeholder="请输入联系人"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="联系电话"
        prop="linkPhone"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          class="max-w460"
          v-model="form.linkPhone"
          placeholder="请输入联系电话"
        ></el-input>
      </el-form-item>
      <el-form-item label="门店logo">
        <el-row>
          <el-button icon="Picture" @click="openUpload('logo')"
            >选择图片</el-button
          >
          <div v-if="form.logo != ''" class="img">
            <img :src="form.logo" width="100" height="100" />
          </div>
        </el-row>
      </el-form-item>
      <el-form-item
        label="地址"
        prop="address"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          class="max-w460"
          v-model="form.address"
          placeholder="请输入联系人"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="门店坐标"
        prop="coordinate"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-button v-if="tx_key" icon="LocationInformation" @click="mapClick()"
          >选择经纬度</el-button
        >
        <div class="d-s-c">
          <div class="ml10"></div>
          <div class="max-w460">
            <el-input v-model="form.coordinate"></el-input>
          </div>
        </div>
        <div>
          <a href="https://lbs.qq.com/getPoint/" target="_blank"
            >腾讯地图坐标获取</a
          >
        </div>
        <div class="tips" style="color: red">
          未申请腾讯地图key，在腾讯地图里面搜索地址获取经纬度，例40.002749,116.323467
        </div>
      </el-form-item>
      <el-form-item label="商家介绍" prop="description">
        <el-input
          rows="6"
          type="textarea"
          v-model="form.description"
          class="max-w460"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态" v-if="userType == 0 && form.isMain == 0">
        <el-radio-group v-model="form.isRecycle">
          <el-radio :label="0">开启</el-radio>
          <el-radio :label="1">禁止</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="营业状态">
        <el-radio-group v-model="form.status">
          <el-radio :label="0">营业中</el-radio>
          <el-radio :label="1">停止营业</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc">取消</el-button>
        <el-button
          size="small"
          type="primary"
          @click="onSubmit"
          :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>
    <!--上传图片组件-->
    <Upload
      v-if="isupload"
      :isupload="isupload"
      :type="type"
      @returnImgs="returnImgsFunc"
      >上传图片</Upload
    >
    <Map
      :open="open_map"
      :mapData="mapData"
      :tx_key="tx_key"
      @close="closeDialogFunc($event, 'map')"
    ></Map>
  </div>
</template>

<script>
import SupplierApi from "@/api/supplier.js";
import Upload from "@/components/file/Upload.vue";
import { formatModel } from "@/utils/base.js";
import Map from "./dialog/Map.vue";

import { useUserStore } from "@/store";
const { userInfo } = useUserStore();
export default {
  components: {
    /*上传组件*/
    Upload,

    Map,
  },
  data() {
    return {
      /*form表单数据*/
      form: {
        shopSupplierId: 0,
        userName: "",
        logo: "",
        businessId: 0,
        name: "",
        linkName: "",
        linkPhone: "",
        address: "",
        description: "",
        userId: 0,
        storeType: 10,
        coordinate: "",
        provinceId: "",
        cityId: "",
        regionId: "",
        status: 0,
        isRecycle: 0,
        isMain: 0,
        category_set: "",
      },
      loading: false,
      /*是否上传图片*/
      isupload: false,
      type: "logo",
      /*省市区*/
      userType: "",
      tx_key: "",
      open_map: false,
      mapData: {},
    };
  },
  created() {
    this.form.shopSupplierId = this.$route.query.shopSupplierId;
    this.getData();
    this.getBaseInof();
  },
  methods: {
    async getBaseInof() {
      this.userType = userInfo.userType;
      console.log(this.userType);
    },
    /*打开地图*/
    mapClick() {
      this.mapData.coordinate = this.form.coordinate;
      this.mapData.address = this.form.address;
      this.open_map = true;
    },
    closeDialogFunc(e, f) {
      if (f == "map") {
        if (e && e.type != "error" && e.params.coordinate) {
          this.form.address = e.params.address;
          this.form.coordinate = e.params.coordinate;
        }
        this.open_map = false;
      }
    },
    /*获取参数*/
    getData() {
      let self = this;
      SupplierApi.toEditSupplier(
        {
          shopSupplierId: self.form.shopSupplierId,
        },
        true
      )
        .then((res) => {
          self.form = formatModel(self.form, res.data.model);
          self.form.coordinate =
            res.data.model.latitude + "," + res.data.model.longitude;
          if (res.data.model) {
            self.form.userName = res.data.model.userName;
          }
          self.tx_key = res.data.key;
        })
        .catch((error) => {});
    },

    /*添加用户*/
    onSubmit() {
      let self = this;
      let form = self.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          SupplierApi.editSupplier(form, true)
            .then((data) => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage({
                  message: "恭喜你，门店修改成功",
                  type: "success",
                });
                self.$router.push("/supplier/supplier/index");
              } else {
                self.loading = false;
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },
    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
    },
    /*上传*/
    openUpload(e) {
      this.type = e;
      this.isupload = true;
    },

    /*获取图片*/
    returnImgsFunc(e) {
      if (e != null && e.length > 0) {
        if (this.type == "logo") {
          this.form.logo = e[0].filePath;
        }
      }
      this.isupload = false;
    },
    /*获取经纬度*/
    getMapdataFunc(e) {
      this.form.coordinate = e.data[0].toFixed(6) + "," + e.data[1].toFixed(6);
    },
    /*选择的地址*/
    choseFunc(e) {
      this.form.coordinate = e.location.lat + "," + e.location.lng;
      this.form.address = e.address;
    },
    /*初始化城市id*/
    initCity() {
      this.form.cityId = "";
    },

    /*初始化区id*/
    initRegion() {
      this.form.regionId = "";
    },
  },
};
</script>

<style lang="scss" scoped>
.tips {
  color: #ff0000;
}

.basic-setting-content {
}

.product-add {
  padding-bottom: 50px;
}

.img {
  margin-top: 10px;
}
</style>
