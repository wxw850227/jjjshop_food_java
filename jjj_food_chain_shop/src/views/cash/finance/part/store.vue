<template>
  <div>
    <!--店铺结算-->
    <div class="common-form">账户概况</div>
    <div class="table-wrap">
      <el-row :gutter="20" v-if="detail">
        <el-col :span="6">
          <div class="pb16 grid-content bg-purple">
            <div>营业总额(元)</div>
            <div class="detail_prici">{{ form.total_money }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="pb16 grid-content bg-purple">
            <div>预计收入(元)</div>
            <div class="detail_prici">{{ form.revenue_money }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="pb16 grid-content bg-purple">
            <div>有效订单(元)</div>
            <div class="detail_prici">{{ form.order_count }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="pb16 grid-content bg-purple">
            <div>退款金额(元)</div>
            <div class="detail_prici">{{ form.refund_money }}</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>

</template>

<script>
  export default{
    data() {
      return{
        form:{
          total_money:'',
          revenue_money:'',
          order_count:'',
          refund_money:''
        }
      }

    },
    props:['detail'],
    watch:{
      'detail':function(n,o){
        if(n!=o){
          console.log(n)
          this.form = n;
        }
      }
    },
    methods:{

    }
  }
</script>

<style>
</style>
