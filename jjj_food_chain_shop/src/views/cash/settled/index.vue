<template>
    <div v-loading="loading" style="min-height: 400px;">
      <!--搜索表单-->
      <div class="common-seach-wrap">
        <el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline"   v-auth="'/auth/shop'">
          <el-form-item label="选择店铺">
            <el-select size="small" v-model="shop_supplier_id" placeholder="请选择" @change="selectId">
              <el-option label="全部" :value="0"></el-option>
              <el-option v-for="(item, index) in supplierList" :key="index" :label="item.name" :value="item.shop_supplier_id"></el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item><el-button size="small" type="primary" icon="el-icon-search" @click="onSubmit">查询</el-button></el-form-item> -->
        </el-form>
      </div>
      <!--汇总-->
      <Total v-if="!loading"></Total>
      <!--访问统计-->
      <Transaction v-if="!loading" :shop_supplier_id='shop_supplier_id'></Transaction>
    </div>
</template>

<script>
  import CashApi from '@/api/cash.js';
  import Total from './part/Total.vue'
  import Transaction from './part/Transaction.vue'
  export default{
    components:{
      Total,
      Transaction,
    },
    data(){
      return{
        /*是否正在加载*/
        loading:true,
        /*数据对象*/
        dataModel:{
          settled:{
            real_supplier_money:{
              today:'',
              yesterday:''
            }
          }
        },
        supplierList:[],
        searchForm:{
          style_id:''
        },
        shop_supplier_id:0
      }
    },
    provide: function () {
      return {
        dataModel: this.dataModel
      }
    },
    created() {
      this.getData();
    },
    methods:{
      /*获取数据*/
      getData() {
        let self = this;
        self.loading = true;
        CashApi.settled({
          shop_supplier_id:self.shop_supplier_id
        }, true)
          .then(res => {
            Object.assign(self.dataModel, res.data);
            self.loading = false;
            self.supplierList = res.data.supplierList;
          })
          .catch(error => {});
      },
      selectId(e){
        this.shop_supplier_id=e;
        this.getData()
      }
    }
  }
</script>

<style>
</style>
