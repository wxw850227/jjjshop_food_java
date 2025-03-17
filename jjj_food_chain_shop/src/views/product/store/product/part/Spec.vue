<template>
  <div>
    <!--规格设置-->
    <div class="common-form mt50">规格/库存</div>

    <!--减库存方式-->
    <el-form-item label="库存计算方式：">
      <el-radio-group v-model="form.model.deductStockType">
        <el-radio :label="10">下单减库存</el-radio>
        <el-radio :label="20">付款减库存</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="特色分类：">
      <el-radio-group v-model="form.model.specialId">
        <el-radio :label="0">无</el-radio>
        <el-radio
          :label="item.categoryId"
          v-for="(item, index) in form.special"
          :key="index"
          >{{ item.name }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item
      label="商品单位："
      :rules="[{ required: true, message: '请填写商品单位' }]"
      prop="model.productUnit"
    >
      <el-autocomplete
        class="inline-input"
        v-model="form.model.productUnit"
        placeholder="如:大份"
        :fetch-suggestions="querySearch"
      ></el-autocomplete>
    </el-form-item>
    <el-form-item label="产品规格：">
      <el-radio-group v-model="form.model.specType" @change="changeSpec">
        <el-radio
          :label="10"
          v-if="
            !form.isSpecLocked ||
            (form.isSpecLocked && form.model.specType == 10)
          "
          >单规格</el-radio
        >
        <el-radio
          :label="20"
          v-if="
            !form.isSpecLocked ||
            (form.isSpecLocked && form.model.specType == 20)
          "
          >多规格</el-radio
        >
      </el-radio-group>
      <div v-if="form.isSpecLocked" class="red">
        此商品正在参加活动，不能修改规格
      </div>
    </el-form-item>

    <!--单规格-->
    <template v-if="form.model.specType == 10">
      <Single></Single>
    </template>

    <!--多规格-->
    <template v-if="form.model.specType == 20">
      <Many></Many>
    </template>
  </div>
</template>

<script>
import Single from "./spec/Single.vue";
import Many from "./spec/Many.vue";
export default {
  components: {
    /*单规格*/
    Single,
    /*多规格*/
    Many,
  },
  data() {
    return {
      restaurants: [],
    };
  },
  inject: ["form"],
  methods: {
    changeSpec(e) {
      console.log(e);
      if (e == 10) {
        this.form.sku = {
          productPrice: "",
          stockNum: "",
          bagPrice: "",
          costPrice: 0,
          isFull: 0,
        };
      } else if (e == 20) {
        this.form.sku = [];
      }
    },
    querySearch(queryString, cb) {
      let self = this;
      if (self.restaurants.length == 0) {
        self.form.unit.forEach((item, index) => {
          self.restaurants.push({
            value: item.unitName,
          });
        });
      }

      var restaurants = this.restaurants;
      var results = queryString
        ? restaurants.filter(this.createFilter(queryString))
        : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (
          restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) ===
          0
        );
      };
    },
  },
};
</script>

<style></style>
