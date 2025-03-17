<template>
  <el-dialog
    title="添加类型"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form size="small" :model="form" :rules="formRules" ref="form">
      <el-form-item
        label="桌位编号"
        prop="tableNo"
        :label-width="formLabelWidth"
      >
        <el-input v-model="form.tableNo" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item
        label="类型名称"
        prop="typeId"
        :label-width="formLabelWidth"
      >
        <el-select v-model="form.typeId" placeholder="类型名称">
          <el-option
            v-for="item in type"
            :key="item.typeId"
            :label="item.typeName"
            :value="item.typeId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="所属区域"
        prop="areaId"
        :label-width="formLabelWidth"
      >
        <el-select v-model="form.areaId" placeholder="所属区域">
          <el-option
            v-for="item in areaList"
            :key="item.areaId"
            :label="item.areaName"
            :value="item.areaId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="排序" prop="sort" :label-width="formLabelWidth">
        <el-input-number
          v-model="form.sort"
          :min="1"
          controls-position="right"
          style="width: 100%"
          autocomplete="off"
        ></el-input-number>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <el-button type="primary" @click="addUser" :loading="loading"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script>
import StoreApi from "@/api/store.js";
export default {
  components: {},
  data() {
    return {
      form: {
        tableNo: "",
        areaId: "",
        typeId: "",
        sort: 100,
      },
      formRules: {
        tableNo: [
          {
            required: true,
            message: "请输入桌位编号",
            trigger: "blur",
          },
        ],
        areaId: [
          {
            required: true,
            message: "请选择类型名称",
            trigger: "blur",
          },
        ],
        typeId: [
          {
            required: true,
            message: "请选择所属区域",
            trigger: "blur",
          },
        ],
        sort: [
          {
            required: true,
            message: "排序不能为空",
          },
          {
            type: "number",
            message: "排序必须为数字",
          },
        ],
      },
      /*左边长度*/
      formLabelWidth: "120px",
      /*是否显示*/
      dialogVisible: false,
      loading: false,
      /*是否上传图片*/
      isupload: false,
    };
  },
  props: ["open_add", "addform", "type", "areaList"],
  created() {
    this.dialogVisible = this.open_add;
  },
  methods: {
    /*添加用户*/
    addUser() {
      let self = this;
      let params = self.form;
      console.log(params);
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          StoreApi.addTable(params)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "添加成功",
                type: "success",
              });
              self.dialogFormVisible(true);
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },

    /*关闭弹窗*/
    dialogFormVisible(e) {
      if (e) {
        this.$emit("closeDialog", {
          type: "success",
          openDialog: false,
        });
      } else {
        this.$emit("closeDialog", {
          type: "error",
          openDialog: false,
        });
      }
    },
  },
};
</script>

<style>
.img {
  margin-top: 10px;
}
</style>
