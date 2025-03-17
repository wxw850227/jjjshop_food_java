<template>
  <div class="user">
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          size="small"
          :data="tableData"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column
            prop="messageTypeText"
            label="所属"
          ></el-table-column>
          <el-table-column
            prop="messageName"
            label="通知名称"
          ></el-table-column>
          <el-table-column prop="remark" label="推送规则"></el-table-column>
          <el-table-column
            label="小程序通知"
            v-if="messageTo == 10 || messageTo == 30"
          >
            <template #default="scope">
              <el-checkbox
                v-model="scope.row.wxStatus"
                @change="(checked) => wxStatusChange(checked, scope.row)"
                >启用</el-checkbox
              >
              <el-link
                type="primary"
                :underline="false"
                style="padding-left: 10px"
                @click="wxClick(scope.row)"
                >设置</el-link
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!--小程序-->
    <Wx
      v-if="open_wx"
      :open_wx="open_wx"
      :messageModel="messageModel"
      @closeDialog="closeDialogFunc($event, 'wx')"
    ></Wx>
  </div>
</template>

<script>
import MessageApi from "@/api/message.js";
import Wx from "./settings/Wx.vue";

export default {
  components: {
    Wx,
  },
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
      open_wx: false,
      /*当前编辑的对象*/
      messageModel: {},
    };
  },
  props: ["messageTo"],
  watch: {
    messageTo: function (n, o) {
      if (n != o) {
        /*获取列表*/
        this.getData();
      }
    },
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*获取列表*/
    getData() {
      let self = this;
      MessageApi.messageList(
        {
          messageTo: self.messageTo,
        },
        true
      )
        .then((data) => {
          self.loading = false;
          self.tableData = data.data;
          self.tableData.forEach(function (message) {
            message.wxStatus = message.wxStatus === 1 ? true : false;
            if (message.messageSettingsId == null) {
              message.messageSettingsId = 0;
            }
          });
        })
        .catch((error) => {});
    },
    /*微信小程序消息模板设置*/
    wxClick(item) {
      this.messageModel = item;
      this.open_wx = true;
    },
    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      if (f == "wx") {
        this.open_wx = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
    },
    wxStatusChange: function (checked, row) {
      let self = this;

      if (row.messageSettingsId == 0 || row["wxTemplate"] == null) {
        alert("请先点击右边设置，设置模板规则", "提示", {
          confirmButtonText: "确定",
        });
        row.wxStatus = false;
        return;
      }
      self.loading = true;
      MessageApi.updateSettingsStatus(
        {
          messageType: "wx",
          messageSettingsId: row.messageSettingsId,
        },
        true
      )
        .then((data) => {
          self.loading = false;
          row.wxStatus = checked;
        })
        .catch((error) => {
          self.loading = false;
          row.wxStatus = !checked;
        });
    },
  },
};
</script>

<style>
.operation-wrap {
  border-radius: 8px;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center;
  padding: 15px 15px;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  overflow: hidden;
  background: #909399;
  background-size: 100% 100%;
  color: #fff;
  margin-bottom: 10px;
}
</style>
