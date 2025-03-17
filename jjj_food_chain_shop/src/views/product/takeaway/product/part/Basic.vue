<template>
  <div class="basic-setting-content pl16 pr16">
    <!--基本信息-->
    <div class="common-form">基本信息</div>
    <el-form-item
      label="商品名称："
      :rules="[{ required: true, message: '请填写商品名称' }]"
      prop="model.productName"
    >
      <el-input v-model="form.model.productName" class="max-w460"></el-input>
    </el-form-item>
    <el-form-item
      label="所属分类："
      :rules="[{ required: true, message: '你选择商品分类' }]"
      prop="model.categoryId"
    >
      <el-select v-model="form.model.categoryId">
        <template v-for="cat in form.category" :key="cat.categoryId">
          <el-option :value="cat.categoryId" :label="cat.name"></el-option>
          <template v-if="cat.child !== undefined">
            <template v-for="two in cat.child" :key="two.categoryId">
              <el-option
                :value="two.categoryId"
                :label="two.name"
                style="padding-left: 30px"
              ></el-option>
              <template v-if="two.child !== undefined">
                <template v-for="three in two.child" :key="three.categoryId">
                  <el-option
                    :value="three.categoryId"
                    :label="three.name"
                    style="padding-left: 60px"
                  ></el-option>
                </template>
              </template>
            </template>
          </template>
        </template>
      </el-select>
    </el-form-item>
    <el-form-item
      label="商品图片："
      :rules="[{ required: true, message: '请上传商品图片' }]"
      prop="model.image"
    >
      <div class="draggable-list">
        <draggable class="wrapper" v-model="form.model.image">
          <transition-group>
            <div
              class="item"
              v-for="(item, index) in form.model.image"
              :key="item.filePath"
            >
              <img v-img-url="item.filePath" />
              <a
                href="javascript:void(0);"
                class="delete-btn"
                @click.stop="deleteImg(index)"
                ><el-icon><Close /></el-icon
              ></a>
            </div>
          </transition-group>
        </draggable>
        <div class="item img-select" @click="openProductUpload">
          <el-icon><Plus /></el-icon>
        </div>
      </div>
    </el-form-item>
    <el-form-item label="商品卖点：">
      <el-input
        type="textarea"
        v-model="form.model.sellingPoint"
        class="max-w460"
      ></el-input>
    </el-form-item>

    <!--商品图片组件-->
    <Upload
      v-if="isProductUpload"
      :config="{ total: 9 }"
      :isupload="isProductUpload"
      @returnImgs="returnProductImgsFunc"
      >上传图片</Upload
    >
  </div>
</template>

<script>
import Upload from "@/components/file/Upload.vue";
// import draggable from 'vuedraggable';
export default {
  components: {
    Upload,
    // draggable
  },
  data() {
    return {
      formData: {},
      isProductUpload: false,
    };
  },
  inject: ["form"],
  created() {
    this.formData = this.form;
    // this['formData'] = this.form;
    console.log(this.form.category);
  },
  methods: {
    /*打开上传图片*/
    openProductUpload: function () {
      this.isProductUpload = true;
    },

    /*上传商品图片*/
    returnProductImgsFunc(e) {
      if (e != null) {
        let imgs = this.form.model.image.concat(e);
        this.form.model["image"] = imgs;
      }
      this.isProductUpload = false;
    },

    /*删除商品图片*/
    deleteImg(index) {
      this.form.model.image.splice(index, 1);
    },
  },
};
</script>

<style></style>
