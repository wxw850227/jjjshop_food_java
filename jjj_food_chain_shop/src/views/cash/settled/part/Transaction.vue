<template>
  <div class="ww100 mt30">
    <el-tabs v-model="activeName">
      <el-tab-pane label="店铺结算" name="first"></el-tab-pane>
    </el-tabs>
    <div class="d-b-c">
      <div>
        <el-date-picker
          size="small"
          v-model="datePicker"
          type="daterange"
          align="right"
          unlink-panels
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="changeDate"
         :shortcuts="shortcuts"
        ></el-date-picker>
      </div>
    </div>
    <div class="">
      <div class="Echarts"><div id="LineChart"></div></div>
    </div>
  </div>
</template>

<script>
import CashApi from '@/api/cash.js';
import { formatDate } from '@/utils/DateTime.js'
import * as echarts from "echarts";
let myChart;
export default {
  data() {
    let endDate=new Date();
    let startDate=new Date();
    startDate.setTime(startDate.getTime()- 3600 * 1000 * 24 * 7);
    return {
      activeName: 'first',
      /*时间快捷选项*/
      
          shortcuts: [
           {
          text: "最近一周",
          value: () => {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            return [start, end];
          },
        },
        {
          text: "最近一个月",
          value: () => {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            return [start, end];
          },
        },
        {
          text: "最近三个月",
          value: () => {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            return [start, end];
          },
        },
        ],
    
      datePicker: [formatDate(startDate,'yyyy-MM-dd'),formatDate(endDate,'yyyy-MM-dd')],
      /*数据对象*/
      dataList: null,
      /*交易统计图表对象*/
      // myChart: null,
      /*图表数据*/
      option: {
        title: {
          //text: 'ECharts 入门示例'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '6%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis'
        },
        yAxis: {}
      }
    };
  },
  props:['shop_supplier_id'],
  watch:{
    'shop_supplier_id':function(n,o){
      if(n!=o){
        console.log(n)
      }
    }
  },
  mounted() {
    this.myEcharts();
  },
  methods: {

    /*选择时间*/
    changeDate() {
      this.getData();
    },

    myEcharts() {
      // 基于准备好的dom，初始化echarts实例
      myChart = echarts.init(document.getElementById('LineChart'));
      /*获取列表*/
      this.getData();
    },

    /*格式数据*/
    createOption() {
      if (!this.loading) {
        let names = [];
        let xAxis = this.dataList.days;
        let series1 = [];
        let series2 = [];
        let series3 = [];
        let series4 = [];
        this.dataList.data.forEach(item => {
          series1.push(item.real_supplier_money);
          series2.push(item.supplier_money);
          series3.push(item.order_count);
          series4.push(item.refund_money);
        });
        names = ['商户结算','支付金额','有效订单','退款金额'];

        // 指定图表的配置项和数据
        this.option.xAxis = {
          type: 'category',
          boundaryGap: false,
          data: xAxis
        };
        this.option.color=["red", "#409EFF",'#E6A23C'];
        this.option.legend = {
          data: [{ name: names[0], color: '#ccc' },{ name: names[1]},{ name: names[2]},{ name: names[3]}]
        };
        this.option.series = [
          {
            name: names[0],
            type: 'line',
            data: series1,
            lineStyle: {
              color: 'red'
            }
          },
          {
            name: names[1],
            type: 'line',
            data: series2,
            lineStyle: {
              color: '#409EFF'
            }
          },          {
            name: names[2],
            type: 'line',
            data: series3,
            lineStyle: {
              color: '#E6A23C'
            }
          },          {
            name: names[3],
            type: 'line',
            data: series4,
            lineStyle: {
              color: '#E6A23C'
            }
          }
        ];

        myChart.setOption(this.option);
        myChart.resize();
      }
    },

    /*获取列表*/
    getData() {
      let self = this;
      self.loading = true;
      CashApi.getSettledByDate(
        {
          search_time: self.datePicker,
          shop_supplier_id:self.shop_supplier_id
        },
        true
      )
        .then(res => {
          self.dataList = res.data;
          self.loading = false;
          self.createOption();
        })
        .catch(error => {});
    }
  }
};
</script>

<style scoped="scoped">
  .Echarts{ box-sizing: border-box;}
  .Echarts>div{ width: 100%; height: 360px; box-sizing: border-box;}
</style>
