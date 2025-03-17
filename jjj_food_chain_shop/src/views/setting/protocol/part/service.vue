<template>
  <div class="product-add mt30">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <div class="basic-setting-content pl16 pr16">
        <el-form-item label="">
          <div class="edit_container">
            <Uediter
              :text="form.service"
              :config="ueditor.config"
              ref="ue"
              @contentChange="contentChangeFunc"
              :id="ueditor.id"
            >
            </Uediter>
          </div>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import Uediter from "@/components/UE.vue";
import SettingApi from "@/api/setting.js";
export default {
  components: {
    /*编辑器*/
    Uediter,
  },
  data() {
    return {
      /*富文本配置*/
      ueditor: {
        text: "",
        config: {
          initialFrameWidth: 400,
          initialFrameHeight: 500,
        },
        id: "service",
      },
    };
  },
  emits: ["update-service"],
  props: {
    settingData: Object,
  },
  created() {
    this.form = this.settingData;
  },
  methods: {
    /*获取富文本内容*/
    contentChangeFunc(e) {
      this.form.service = e;
      this.$emit("update-service", this.form.service);
    },
  },
};
</script>
<style lang="scss" scoped></style>
