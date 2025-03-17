<template>
  <div class="d-a-c lh30 ww100 pt16">
    <div class="pt30 tc" style="width: 80px;">
      <p class="gray9">今日</p>
      <p class="gray9">昨日</p>
    </div>
    <div class="flex-1 tc">
      <p>门店总金额（元）</p>
      <p class="f20 fb gray3">{{dataModel.settled.real_supplier_money.today}}</p>
      <p class="gray">{{dataModel.settled.real_supplier_money.yesterday}}</p>
    </div>
    <div class="flex-1 tc">
      <p>待结算金额（元）</p>
      <p class="f20 fb gray3">{{dataModel.settled.real_sys_money.today}}</p>
      <p class="gray">{{dataModel.settled.real_sys_money.yesterday}}</p>
    </div>
    <div class="flex-1 tc">
      <p>累计收入(元)</p>
      <p class="f20 fb gray3">{{dataModel.settled.agent_money.today}}</p>
      <p class="gray">{{dataModel.settled.agent_money.yesterday}}</p>
    </div>
    <div class="flex-1 tc">
      <p>退款金额（元）</p>
      <p class="f20 fb gray3">{{dataModel.settled.refund_money.today}}</p>
      <p class="gray">{{dataModel.settled.refund_money.yesterday}}</p>
    </div>
  </div>
</template>
<script>

export default {
  components:{
  },
  data() {
    return {
      /*是否正在加载*/
      loading: true,

    };
  },
  inject: ['dataModel'],
  created() {

  },
  methods: {


  }
};
</script>

<style>
</style>
