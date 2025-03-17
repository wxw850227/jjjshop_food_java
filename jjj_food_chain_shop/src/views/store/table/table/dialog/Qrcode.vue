<template>
  <div>
    <el-dialog
      title="下载二维码"
      width="600px"
      v-model="dialogVisible"
      @close="dialogFormVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form size="small">
        <el-form-item label="下载类型" :label-width="formLabelWidth">
          <el-radio-group v-model="source">
            <el-radio label="wx">微信小程序</el-radio>
            <el-radio label="mp">公众号，H5网页</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible">取 消</el-button>
          <el-button type="primary" @click="qrcodeClick">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import qs from "qs";
import { useUserStore } from "@/store";
const { token } = useUserStore();
export default {
  data() {
    return {
      status: "",
      reject_reason: "",
      /*左边长度*/
      formLabelWidth: "120px",
      /*是否显示*/
      dialogVisible: false,
      loading: false,
      source: "wx",
      token,
    };
  },
  props: ["open", "codeId"],
  watch: {
    open: function (n, o) {
      this.dialogVisible = this.open;
    },
  },
  created() {},
  methods: {
    qrcodeClick: function () {
      let self = this;
      let Params = {
        id: self.codeId,
        source: self.source,
        tokenshop: self.token,
      };
      let baseUrl = import.meta.env.VITE_BASIC_URL;
      if (!baseUrl) {
        baseUrl = window.location.protocol + "//" + window.location.host;
      }
      window.location.href =
        baseUrl + "/api/shop/store/table/table/qrcode?" + qs.stringify(Params);
    },
    /*关闭弹窗*/
    dialogFormVisible(e) {
      if (e) {
        this.$emit("close", true);
      } else {
        this.$emit("close", false);
      }
    },
  },
};
</script>

<style></style>
