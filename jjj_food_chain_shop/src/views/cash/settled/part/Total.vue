<template>
  <div class="d-a-c lh30 ww100 pt16">
    <div class="pt30 tc" style="width: 80px;">
      <p class="gray9">今日</p>
      <p class="gray9">昨日</p>
      <p class="gray9">全部</p>
    </div>
    <div class="flex-1 tc">
      <p>销售额（元）</p>
      <p class="f20 fb gray3">{{dataModel.settled.supplier_money.today}}</p>
      <p class="gray">{{dataModel.settled.supplier_money.yesterday}}</p>
      <p class="gray">{{dataModel.settled.supplier_money.total}}</p>
    </div>
    <div class="flex-1 tc">
      <p>待结算金额（元）</p>
      <p class="f20 fb gray3">{{dataModel.settled.settle_money.today}}</p>
      <p class="gray">{{dataModel.settled.settle_money.yesterday}}</p>
      <p class="gray">{{dataModel.settled.settle_money.total}}</p>
    </div>
    <div class="flex-1 tc">
      <p>已结算金额（元）</p>
      <p class="f20 fb gray3">{{dataModel.settled.real_supplier_money.today}}</p>
      <p class="gray">{{dataModel.settled.real_supplier_money.yesterday}}</p>
      <p class="gray">{{dataModel.settled.real_supplier_money.total}}</p>
    </div>
    <div class="flex-1 tc">
      <p>已提现余额(元)</p>
      <p class="f20 fb gray3">{{dataModel.settled.cash_money.today}}</p>
      <p class="gray">{{dataModel.settled.cash_money.yesterday}}</p>
      <p class="gray">{{dataModel.settled.cash_money.total}}</p>
    </div>
    <div class="flex-1 tc">
      <p>退款金额(元)</p>
      <p class="f20 fb gray3">{{dataModel.settled.refund_money.today}}</p>
      <p class="gray">{{dataModel.settled.refund_money.yesterday}}</p>
      <p class="gray">{{dataModel.settled.refund_money.total}}</p>
    </div>
    <div class="flex-1 tc">
      <p>预计收入（元）</p>
      <p class="f20 fb gray3">{{dataModel.settled.income.today}}</p>
      <p class="gray">{{dataModel.settled.income.yesterday}}</p>
      <p class="gray">{{dataModel.settled.income.total}}</p>
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
