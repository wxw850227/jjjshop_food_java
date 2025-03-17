<template>
  <div class="buy-set-content pl16 pr16">
    <!--其他设置-->
    <div class="common-form mt50">其他设置</div>
    <el-form-item
      v-if="form.model.productStatus != 40"
      label="商品状态："
      :rules="[{ required: true, message: '选择商品状态' }]"
      prop="model.productStatus"
    >
      <el-radio-group v-model="form.model.productStatus">
        <el-radio :label="10">上架</el-radio>
        <el-radio :label="20">下架</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="初始销量：">
      <el-input
        type="number"
        min="0"
        v-model="form.model.salesInitial"
        class="max-w460"
      ></el-input>
    </el-form-item>
    <el-form-item
      label="商品排序："
      :rules="[{ required: true, message: ' ' }]"
      prop="model.productSort"
    >
      <el-input
        type="number"
        min="0"
        v-model="form.model.productSort"
        class="max-w460"
      ></el-input>
    </el-form-item>
    <el-form-item
      label="限购数量："
      :rules="[{ required: true, message: ' ' }]"
      prop="model.limitNum"
    >
      <el-input
        type="number"
        min="0"
        v-model="form.model.limitNum"
        class="max-w460"
      ></el-input>
      <div class="gray9">每个会员购买的最大数量，0为不限购</div>
    </el-form-item>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  created() {},
  inject: ["form"],
  methods: {},
};
</script>

<style></style>
