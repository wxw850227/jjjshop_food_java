<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--清理缓存-->
      <div class="common-form">清理缓存</div>
      <el-form-item label="数据缓存:">
        <span>清理所有数据缓存</span>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="primary" @click="onSubmit">清理</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import SettingApi from "@/api/setting.js";

export default {
  data() {
    return {
      form: {},
    };
  },
  created() {},
  methods: {
    //提交表单
    onSubmit() {
      let self = this;
      SettingApi.clearCache({}, true)
        .then((data) => {
          ElMessage({
            message: "恭喜你，清理成功",
            type: "success",
          });
          self.$router.push("/setting/clear/index");
        })
        .catch((error) => {});
    },
    //监听复选框选中
    handleCheckedCitiesChange(val) {},
  },
};
</script>

<style>
.tips {
  color: #ccc;
}
</style>
