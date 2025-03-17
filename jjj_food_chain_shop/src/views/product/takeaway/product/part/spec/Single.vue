<template>
  <div>
    <el-form-item
      label="产品价格："
      width="80"
      :rules="[{ required: true, message: '请填写产品价格' }]"
      prop="model.sku[0].productPrice"
    >
      <el-input
        type="number"
        v-model="form.model.sku[0].productPrice"
        class="max-w460"
      ></el-input>
    </el-form-item>
    <el-form-item
      label="包装费："
      :rules="[{ required: true, message: '请填写包装费' }]"
      prop="model.sku[0].bagPrice"
    >
      <el-input
        type="number"
        v-model="form.model.sku[0].bagPrice"
        class="max-w460"
      ></el-input>
    </el-form-item>
    <el-form-item
      label="库存数量："
      :rules="[{ required: true, message: '请填写库存数量' }]"
      prop="model.sku[0].stockNum"
    >
      <el-input
        type="number"
        v-model="form.model.sku[0].stockNum"
        class="max-w460"
      ></el-input>
    </el-form-item>
    <!--    <el-form-item label="成本价："  prop="model.sku[0].cost_price">
      <el-input type="number" v-model="form.model.sku[0].cost_price" class="max-w460"></el-input>
    </el-form-item> -->
  </div>
</template>

<script>
export default {
  inject: ["form"],
};
</script>

<style></style>
