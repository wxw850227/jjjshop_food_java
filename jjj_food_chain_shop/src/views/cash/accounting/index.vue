<template>
    <div v-loading="loading" style="min-height: 400px;">
      <!--搜索表单-->
      <div class="common-seach-wrap">
        <el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item label="选择店铺"   v-auth="'/auth/shop'">
            <el-select size="small" v-model="searchForm.style_id" placeholder="请选择">
              <el-option label="全部" value=""></el-option>
              <!-- <el-option v-for="(item, index) in exStyle" :key="index" :label="item.name" :value="item.value"></el-option> -->
            </el-select>
          </el-form-item>
          <el-form-item><el-button size="small" type="primary" icon="el-icon-search" @click="onSubmit">查询</el-button></el-form-item>
        </el-form>
      </div>
      <!--汇总-->
      <Total v-if="!loading"></Total>
      <!--访问统计-->
      <Transaction v-if="!loading"></Transaction>
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
        dataModel:{},
        searchForm:{
          style_id:''
        }
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
        CashApi.settled({}, true)
          .then(res => {
            Object.assign(self.dataModel, res.data);
            self.loading = false;
          })
          .catch(error => {});
      }
    }
  }
</script>

<style>
</style>
