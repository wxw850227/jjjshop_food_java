<template>
  <div class="pb50" v-loading="loading">
    <div class="product-content">
      <!--基本信息-->
      <div class="common-form">基本信息</div>
      <div class="table-wrap">
        <el-row>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">订单号：</span>
              {{ detail.orderNo }}
            </div>
          </el-col>
          <el-col :span="5">
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
              <span class="gray9">配送方式：</span>
              {{ detail.deliveryTypeText }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9" v-if="detail.deliveryType == 10"
                >配送时间：</span
              >
              <span class="gray9" v-if="detail.deliveryType == 20"
                >取餐时间：</span
              >
              {{ detail.mealtime }}
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
              {{ detail.supplier.name }}
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
          <el-table-column prop="product_name" label="商品" width="400">
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
                      >￥ {{ scope.row.productPrice }}</span
                    >
                    <span class="ml10" v-if="scope.row.isUserGrade == 1">
                      会员折扣价：￥ {{ scope.row.gradeProductPrice }}
                    </span>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="total_num" label="购买数量">
            <template #default="scope">
              <p>x {{ scope.row.totalNum }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="totalPayPrice" label="商品总价(元)">
            <template #default="scope">
              <p>￥ {{ scope.row.totalPayPrice }}</p>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!--收货信息-->
      <div v-if="detail.deliveryType == 10">
        <div class="common-form mt16">收货信息</div>
        <div class="table-wrap">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">收货人：</span>
                {{ detail.address.name }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">收货电话：</span>
                {{ detail.address.phone }}
              </div>
            </el-col>
            <el-col :span="9">
              <div class="pb16">
                <span class="gray9">收货地址：</span>
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
      <template v-if="detail.deliveryType == 20">
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

      <!--无需发货-->
      <template v-if="detail.deliveryType == 30">
        <div class="common-form mt16">用户信息</div>
        <div class="table-wrap">
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">联系手机：</span>
                {{ detail.user.mobile }}
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
                  <span class="gray9">配送类型：</span>
                  {{ deliver.deliverSourceText }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">配送状态：</span>
                  {{ deliver.deliverStatusText }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">配送距离：</span>
                  {{ deliver.distance }}米
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">配送员：</span>
                  {{ deliver.linkman }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">配送电话：</span>
                  {{ deliver.phone }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">配送费用：</span>
                  {{ deliver.price }}
                </div>
              </el-col>
              <el-col :span="5">
                <div class="pb16">
                  <span class="gray9">送达时间：</span>
                  {{ deliver.deliverTime }}
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
      <!--确认送达-->
      <template v-if="deliver.status == 10">
        <el-button size="small" type="primary" @click="onSubmit"
          >确认送达
        </el-button>
      </template>
    </div>
  </div>
</template>

<script>
import TakeOutApi from "@/api/takeout.js";
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
        payStatus: [],
        payType: [],
        deliveryType: [],
        user: {},
        address: [],
        product: [],
        orderStatus: [],
        extract: [],
        extract_store: [],
        express: [],
        delivery_status: [],
        extractClerk: [],
        supplier: {
          name: "",
        },
      },
      /*是否打开添加弹窗*/
      open_add: false,
      /*一页多少条*/
      pageSize: 20,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      order: {},
      deliveryType: 0,
      addressData: {
        name: "",
        phone: "",
        detail: "",
        address: "",
      },
      deliver: {},
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
      const params = this.$route.query.deliverId;
      TakeOutApi.detail(
        {
          deliverId: params,
        },
        true
      )
        .then((data) => {
          self.loading = false;
          self.detail = data.data.detail;
          self.deliver = data.data.deliver;
          if (self.detail.address != null) {
            self.addressData = self.detail.address;
          }
          console.log(self.addressData);
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*确认送达*/
    onSubmit() {
      let self = this;
      let deliverId = this.$route.query.deliverId;
      TakeOutApi.verify(
        {
          deliverId: deliverId,
        },
        true
      )
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "恭喜你，操作成功",
            type: "success",
          });
          self.getParams();
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
