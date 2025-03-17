<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--小票打印设置-->
      <div class="common-form">小票打印设置</div>

      <el-form-item label="是否开启商户小票打印">
        <div>
          <el-radio v-model="form.sellerOpen" :label="1">开启</el-radio>
          <el-radio v-model="form.sellerOpen" :label="0">关闭</el-radio>
        </div>
      </el-form-item>
      <el-form-item label="选择打印机" v-if="form.sellerOpen == 1">
        <el-select v-model="form.sellerPrinterId" placeholder="请选择">
          <el-option
            v-for="(item, index) in printerList"
            :key="index"
            :label="item.printerName"
            :value="item.printerId + ''"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否开启顾客小票打印">
        <div>
          <el-radio v-model="form.buyerOpen" :label="1">开启</el-radio>
          <el-radio v-model="form.buyerOpen" :label="0">关闭</el-radio>
        </div>
      </el-form-item>
      <el-form-item label="选择打印机二" v-if="form.buyerOpen == 1">
        <el-select v-model="form.buyerPrinterId" placeholder="请选择">
          <el-option
            v-for="(item, index) in printerList"
            :key="index"
            :label="item.printerName"
            :value="item.printerId + ''"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否开启厨房小票打印">
        <div>
          <el-radio v-model="form.roomOpen" :label="1">开启</el-radio>
          <el-radio v-model="form.roomOpen" :label="0">关闭</el-radio>
        </div>
      </el-form-item>
      <el-form-item label="选择打印机三" v-if="form.roomOpen == 1">
        <el-select v-model="form.roomPrinterId" placeholder="请选择">
          <el-option
            v-for="(item, index) in printerList"
            :key="index"
            :label="item.printerName"
            :value="item.printerId + ''"
          ></el-option>
        </el-select>
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
      /*切换菜单*/
      // activeIndex: '1',
      /*form表单数据*/
      form: {
        buyerOpen: "",
        roomOpen: "",
        sellerOpen: "",
        buyerPrinterId: "",
        roomPrinterId: "",
        sellerPrinterId: "",
      },
      checked: false,
      printerList: [],
      loading: false,
    };
  },
  created() {
    this.getData();
  },

  methods: {
    getData() {
      let self = this;
      SettingApi.printingDetail({}, true)
        .then((data) => {
          let vars = data.data.setting;
          self.form.buyerOpen = vars.buyerOpen;
          self.form.sellerOpen = vars.sellerOpen;
          self.form.roomOpen = vars.roomOpen;
          self.form.buyerPrinterId = "" + vars.buyerPrinterId;
          self.form.roomPrinterId = "" + vars.roomPrinterId;
          self.form.sellerPrinterId = "" + vars.sellerPrinterId;
          self.printerList = data.data.printerList;
          console.log("print", self.printerList);
          console.log("var", vars.sellerOpen);
          if (vars.orderStatus != null && vars.orderStatus[0] == 20) {
            self.checked = true;
          }
        })
        .catch((error) => {});
    },
    //提交表单
    onSubmit() {
      let self = this;
      let params = this.form;
      self.loading = true;
      SettingApi.editPrinting(params, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "恭喜你，打印设置成功",
            type: "success",
          });
          // self.$router.push('/setting/Printing');
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    //监听复选框选中
    handleCheckedCitiesChange(e) {
      let self = this;
      if (e) {
        self.form.orderStatus[0] = 20;
      } else {
        self.form.orderStatus = [];
      }
    },
  },
};
</script>

<style>
.tips {
  color: #ccc;
}
</style>
