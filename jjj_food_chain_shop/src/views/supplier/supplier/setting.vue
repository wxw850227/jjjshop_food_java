<template>
  <div class="pb50">
    <el-form size="small" ref="form" :model="form" label-width="150px">
      <el-form-item
        label="店内营业时间"
        prop="storeTimeList"
        :rules="[
          { required: true, message: '请选择店内营业时间', trigger: 'change' },
        ]"
      >
        <el-col :span="11">
          <el-time-picker
            is-range
            v-model="form.storeTimeList"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            placeholder="选择时间范围"
            format="HH:mm"
            value-format="HH:mm"
          >
          </el-time-picker>
        </el-col>
      </el-form-item>
      <el-form-item
        label="自提营业时间"
        prop="pickTimeList"
        :rules="[
          { required: true, message: '请选择自提营业时间', trigger: 'change' },
        ]"
      >
        <el-col :span="11">
          <el-time-picker
            is-range
            v-model="form.pickTimeList"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            placeholder="选择时间范围"
            format="HH:mm"
            value-format="HH:mm"
          >
          </el-time-picker>
        </el-col>
      </el-form-item>
      <el-form-item
        label="外卖营业时间"
        prop="deliveryTimeList"
        :rules="[
          { required: true, message: '请选择外卖营业时间', trigger: 'change' },
        ]"
      >
        <el-col :span="11">
          <el-time-picker
            is-range
            v-model="form.deliveryTimeList"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            placeholder="选择时间范围"
            format="HH:mm"
            value-format="HH:mm"
          >
          </el-time-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="外卖配送">
        <el-checkbox-group v-model="form.deliverySetList">
          <el-checkbox
            v-for="(item, index) in deliverType"
            :label="item.value"
            :key="index"
            >{{ item.name }}</el-checkbox
          >
        </el-checkbox-group>
        <div class="tips">注：外卖配送方式至少选择一个</div>
      </el-form-item>
      <el-form-item label="店内配置">
        <el-checkbox-group v-model="form.storeSetList">
          <el-checkbox
            v-for="(item, index) in storeType"
            :label="item.value"
            :key="index"
            >{{ item.name }}</el-checkbox
          >
        </el-checkbox-group>
        <div class="tips">注：店内用餐方式至少选择一个</div>
      </el-form-item>
      <el-form-item label="外卖包装费类型">
        <el-radio-group v-model="form.bagType">
          <el-radio :label="0">按商品收费</el-radio>
          <el-radio :label="1">按单收费</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        v-if="form.bagType == 1"
        label="包装费"
        :rules="[{ required: true, message: '请输入包装费' }]"
        prop="bagPrice"
      >
        <div class="d-s-c">
          <div class="max-w60">
            <el-input v-model="form.bagPrice" type="number"></el-input>
          </div>
          <div class="ml10">元</div>
        </div>
      </el-form-item>
      <el-form-item label="店内包装费类型">
        <el-radio-group v-model="form.storebagType">
          <el-radio :label="0">按商品收费</el-radio>
          <el-radio :label="1">按单收费</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        v-if="form.storebagType == 1"
        label="包装费"
        :rules="[{ required: true, message: '请输入包装费' }]"
        prop="bagPrice"
      >
        <div class="d-s-c">
          <div class="max-w60">
            <el-input v-model="form.storebagPrice" type="number"></el-input>
          </div>
          <div class="ml10">元</div>
        </div>
      </el-form-item>
      <el-form-item
        label="外卖配送范围"
        :rules="[{ required: true, message: '请输入外卖配送范围' }]"
        prop="deliveryDistance"
      >
        <div class="d-s-c">
          <div class="max-w60">
            <el-input
              class=""
              v-model="form.deliveryDistance"
              type="number"
            ></el-input>
          </div>
          <div class="ml10">km</div>
        </div>
      </el-form-item>
      <el-form-item
        label="最低消费"
        :rules="[{ required: true, message: '请输入最低消费' }]"
        prop="minMoney"
      >
        <div class="d-s-c">
          <div class="max-w60">
            <el-input v-model="form.minMoney" type="number"></el-input>
          </div>
          <div class="ml10">元</div>
        </div>
      </el-form-item>
      <el-form-item
        label="外卖配送费"
        :rules="[{ required: true, message: '请输入外卖配送费' }]"
        prop="shippingFee"
      >
        <div class="d-s-c">
          <div class="max-w60">
            <el-input v-model="form.shippingFee" type="number"></el-input>
          </div>
          <div class="ml10">元</div>
        </div>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" @click="cancelFunc">取消</el-button>
        <el-button
          size="small"
          type="primary"
          @click="onSubmit"
          :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
import SupplierApi from "@/api/supplier.js";
import { formatModel } from "@/utils/base.js";
export default {
  data() {
    return {
      /*表单对象*/
      form: {
        shopSupplierId: 0,
        /*活动时间*/
        storeTimeList: [],
        pickTimeList: [],
        deliveryTimeList: [],
        bagType: 0,
        bagPrice: 0,
        storebagType: 0,
        storebagPrice: 0,
        deliveryDistance: "",
        deliverySetList: [],
        storeSetList: [],
        minMoney: 0,
        shippingFee: 0,
      },
      deliverType: [
        {
          name: "外卖配送",
          value: 10,
        },
        {
          name: "到店自提",
          value: 20,
        },
      ],
      storeType: [
        {
          name: "打包带走",
          value: 30,
        },
        {
          name: "店内就餐",
          value: 40,
        },
      ],
      /*是否正在加载*/
      loading: false,
    };
  },
  created() {
    this.form.shopSupplierId = this.$route.query.shopSupplierId;
    this.getData();
  },
  methods: {
    /*获取参数*/
    getData() {
      let self = this;
      SupplierApi.getsetSupplier(
        {
          shopSupplierId: self.form.shopSupplierId,
        },
        true
      )
        .then((res) => {
          self.form = formatModel(self.form, res.data);
          self.form.deliverySetList = res.data.deliverySetList;
          // 转成整数，兼容组件
          for (let i = 0; i < self.form.deliverySetList.length; i++) {
            self.form.deliverySetList[i] = parseInt(
              self.form.deliverySetList[i]
            );
          }
          self.form.storeSetList = res.data.storeSetList;
          // 转成整数，兼容组件
          for (let i = 0; i < self.form.storeSetList.length; i++) {
            self.form.storeSetList[i] = parseInt(self.form.storeSetList[i]);
          }
          console.log(self.form);
        })
        .catch((error) => {});
    },
    /*提交表单*/
    onSubmit() {
      let self = this;
      let params = self.form;
      if (params.deliverySetList.length < 1) {
        ElMessage({
          message: "外卖配送至少选择一种！",
          type: "warning",
        });
        return;
      }
      if (params.storeSetList.length < 1) {
        ElMessage({
          message: "店内配置至少选择一种！",
          type: "warning",
        });
        return;
      }
      self.$refs.form.validate((valid) => {
        if (valid) {
          console.log(params);
          self.loading = true;
          SupplierApi.setSupplier(params, true)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "恭喜你，设置成功",
                type: "success",
              });
              self.cancelFunc();
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },
    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
    },
  },
};
</script>

<style>
.max-w60 {
  max-width: 60px;
}
</style>
