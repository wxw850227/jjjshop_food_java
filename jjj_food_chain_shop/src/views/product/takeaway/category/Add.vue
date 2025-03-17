<template>
  <el-dialog
    title="添加分类"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form size="small" :model="form" :rules="formRules" ref="form">
      <el-form-item label="分类名称" prop="name" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item
        label="分类图片"
        prop="imageId"
        :label-width="formLabelWidth"
      >
        <el-row>
          <el-button type="primary" @click="openUpload">选择图片</el-button>
          <div v-if="form.imageId != ''" class="img">
            <img :src="filePath" width="100" height="100" />
          </div>
        </el-row>
      </el-form-item>

      <el-form-item label="分类排序" prop="sort" :label-width="formLabelWidth">
        <el-input-number
          v-model="form.sort"
          controls-position="right"
          :min="1"
          style="width: 50%"
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
    <!--上传图片组件-->
    <Upload
      v-if="isupload"
      :isupload="isupload"
      :type="type"
      @returnImgs="returnImgsFunc"
      >上传图片</Upload
    >
  </el-dialog>
</template>

<script>
import PorductApi from "@/api/product.js";
import Upload from "@/components/file/Upload.vue";
export default {
  components: {
    Upload,
  },
  data() {
    return {
      form: {
        parentId: "0",
        name: "",
        sort: 100,
        imageId: "",
      },
      formRules: {
        name: [
          {
            required: true,
            message: "请输入分类名称",
            trigger: "blur",
          },
        ],
        imageId: [
          {
            required: true,
            message: "请上传分类图片",
            trigger: "blur",
          },
        ],
        sort: [
          {
            required: true,
            message: "分类排序不能为空",
          },
          {
            type: "number",
            message: "分类排序必须为数字",
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
  props: ["open_add", "addform"],
  created() {
    this.dialogVisible = this.open_add;
  },
  methods: {
    /*添加用户*/
    addUser() {
      let self = this;
      let params = self.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          PorductApi.takeCatAdd(params)
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
    /*上传*/
    openUpload(e) {
      this.type = e;
      this.isupload = true;
    },
    /*获取图片*/
    returnImgsFunc(e) {
      if (e != null && e.length > 0) {
        this.filePath = e[0].filePath;
        this.form.imageId = e[0].fileId;
      }
      this.isupload = false;
    },
  },
};
</script>

<style>
.img {
  margin-top: 10px;
}
</style>
