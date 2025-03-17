<template>
  <div class="pb50" v-loading="loading">
    <div class="product-content">
      <!--基本信息-->
      <div class="common-form">基本信息</div>
      <div class="table-wrap">
        <el-row>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">订单来源：</span>
              {{ detail.orderSourceText }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">订单号：</span>
              {{ detail.orderNo }}
            </div>
          </el-col>
          <el-col :span="5" v-if="detail.user">
            <div class="pb16">
              <span class="gray9">买家：</span>
              {{ detail.user.nickName }}
              <span>用户ID：({{ detail.user.userId }})</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">订单金额 (元)：</span>
              {{ detail.orderPrice }}
            </div>
          </el-col>
          <el-col :span="5" v-if="detail.expressPrice > 0">
            <div class="pb16">
              <span class="gray9">配送费 (元)：</span>
              {{ detail.expressPrice }}
            </div>
          </el-col>
          <el-col :span="5" v-if="detail.bagPrice > 0">
            <div class="pb16">
              <span class="gray9">包装费 (元)：</span>
              {{ detail.bagPrice }}
            </div>
          </el-col>
          <el-col :span="5" v-if="detail.serviceMoney > 0">
            <div class="pb16">
              <span class="gray9">服务费 (元)：</span>
              {{ detail.serviceMoney }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16" v-if="detail.couponMoney > 0">
              <span class="gray9">优惠券抵扣 (元)：</span>
              {{ detail.couponMoney }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16" v-if="detail.pointsMoney > 0">
              <span class="gray9">积分抵扣 (元)：</span>
              {{ detail.pointsMoney }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16" v-if="detail.fullreduceMoney > 0">
              <span class="gray9">满减金额 (元)：</span>
              {{ detail.fullreduceMoney }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16" v-if="detail.discountMoney > 0">
              <span class="gray9">优惠金额 (元)：</span>
              {{ detail.discountMoney }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">实付款金额 (元)：</span>
              {{ detail.payPrice }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">支付方式：</span>
              {{ detail.payTypeText }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">用餐方式：</span>
              {{ detail.deliveryTypeText }}
            </div>
          </el-col>
          <el-col :span="5" v-if="detail.mealtime">
            <div class="pb16">
              <span class="gray9">取餐时间：</span>
              {{ detail.mealtime }}
            </div>
          </el-col>
          <el-col :span="5" v-if="detail.tableNo">
            <div class="pb16">
              <span class="gray9">桌号：</span>
              {{ detail.tableNo }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">交易状态：</span>
              {{ detail.orderStatusText }}
            </div>
          </el-col>
        </el-row>
      </div>
      <!--商户信息-->
      <div class="common-form mt16">门店信息</div>
      <div class="table-wrap">
        <el-row>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">门店名称：</span>
              {{ detail.supplierName }}
            </div>
          </el-col>
        </el-row>
      </div>
      <!--商品信息-->
      <div class="common-form mt16">商品信息</div>
      <div class="table-wrap">
        <el-table
          size="small"
          :data="detail.product"
          border
          style="width: 100%"
        >
          <el-table-column prop="productName" label="商品" width="400">
            <template #default="scope">
              <div class="product-info">
                <div class="pic"><img v-img-url="scope.row.imagePath" /></div>
                <div class="info">
                  <div class="name">{{ scope.row.productName }}</div>
                  <div class="gray9" v-if="scope.row.productAttr != ''">
                    {{ scope.row.productAttr }}
                  </div>
                  <div class="orange" v-if="scope.row.refund">
                    {{ scope.row.refund.type }}-{{ scope.row.refund.status }}
                  </div>
                  <div class="price">
                    <span
                      :class="{
                        'text-d-line': scope.row.isUserGrade == 1,
                        gray6: scope.row.isUserGrade != 1,
                      }"
                      >￥ {{ scope.row.linePrice }}</span
                    >
                    <span class="ml10" v-if="scope.row.isUserGrade == 1">
                      会员折扣价：￥ {{ scope.row.gradeProductPrice }}
                    </span>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="totalNum" label="购买数量">
            <template #default="scope">
              <p>x {{ scope.row.totalNum }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="totalPrice" label="商品总价(元)">
            <template #default="scope">
              <p>￥ {{ scope.row.totalPrice }}</p>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!--收货信息-->
      <div v-if="detail.deliveryType.value == 10">
        <div class="common-form mt16">配送信息</div>
        <div class="table-wrap">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">联系人：</span>
                {{ detail.address.name }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">联系电话：</span>
                {{ detail.address.phone }}
              </div>
            </el-col>
            <el-col :span="9">
              <div class="pb16">
                <span class="gray9">联系地址：</span>
                {{ detail.address.detail }}{{ detail.address.address }}
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="25">
              <div class="pb16">
                <span class="gray9">备注：</span>
                {{ detail.buyerRemark }}
              </div>
            </el-col>
          </el-row>
        </div>
      </div>

      <!--自提门店信息-->
      <template v-if="detail.deliveryType.value == 20">
        <div class="common-form mt16">自提用户信息</div>
        <div class="table-wrap" v-if="detail.extract">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">联系电话：</span>
                {{ detail.extract.phone }}
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="25">
              <div class="pb16">
                <span class="gray9">备注：</span>
                {{ detail.buyerRemark }}
              </div>
            </el-col>
          </el-row>
        </div>
      </template>

      <!--付款信息-->
      <div class="table-wrap" v-if="detail.payStatus == 20">
        <div class="common-form mt16">付款信息</div>
        <div class="table-wrap">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">应付款金额：</span>
                {{ detail.payPrice }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">支付方式：</span>
                {{ detail.payTypeText }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">支付流水号：</span>
                {{ detail.transactionId }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">付款状态：</span>
                {{ detail.payStatusText }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">付款时间：</span>
                {{ detail.payTime }}
              </div>
            </el-col>
            <el-col :span="5" v-if="detail.refundMoney > 0">
              <div class="pb16">
                <span class="gray9">退款金额：</span>
                {{ detail.refundMoney }}
              </div>
            </el-col>
          </el-row>
        </div>
      </div>

      <!--发货信息-->
      <div v-if="detail.deliveryType == 10">
        <div>
          <div class="common-form mt16">配送信息</div>
          <div class="table-wrap">
            <el-row>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">配送状态：</span>
                  {{ detail.deliveryStatus }}
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>

      <!--取消信息-->
      <div
        class="table-wrap"
        v-if="detail.orderStatus == 20 && detail.cancelRemark != ''"
      >
        <div class="common-form mt16">取消信息</div>
        <div class="table-wrap">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">商家备注：</span>
                {{ detail.cancelRemark }}
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <div class="common-button-wrapper">
      <el-button size="small" type="info" @click="cancelFunc"
        >返回上一页</el-button
      >
    </div>
  </div>
</template>

<script>
import OrderApi from "@/api/order.js";
import { deepClone } from "@/utils/base.js";
export default {
  components: {},
  data() {
    return {
      active: 0,
      /*是否加载完成*/
      loading: true,
      /*订单数据*/
      detail: {
        orderId: 0,
        payStatus: [],
        payStatusText: "",
        payType: [],
        deliveryType: [],
        user: {},
        address: [],
        product: [],
        orderStatus: [],
        extract: [],
        deliveryStatus: [],
        supplierName: "",
        supplier: {
          name: "",
        },
      },
    };
  },
  created() {
    /*获取列表*/
    this.getParams();
  },
  methods: {
    next() {
      if (this.active++ > 4) this.active = 0;
    },
    /*获取参数*/
    getParams() {
      let self = this;
      // 取到路由带过来的参数
      const params = this.$route.query.orderId;
      OrderApi.storeOrderdetail(
        {
          orderId: params,
        },
        true
      )
        .then((data) => {
          self.loading = false;
          self.detail = data.data;
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
    },
  },
};
</script>
<style></style>
