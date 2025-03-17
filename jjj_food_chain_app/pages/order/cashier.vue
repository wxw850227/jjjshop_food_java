<template>
	<view :data-theme='theme()' :class="theme() || ''">
		<view class="tc buy-checkout-top">
			<view class="f32 mb20">待支付</view>
			<view class="redA8 f60 fb">￥{{payPrice || ''}}</view>
		</view>
		<view class="buy-checkout p-0-30">
			<view v-for="(item,index) in checkedPay" :key='index'>
				<view v-if="item==20" :class="payType == 20 ? 'item active' : 'item'" @tap="payTypeFunc(20)">
					<view class="d-s-c">
						<view class="icon-box d-c-c mr10"><span class="icon iconfont icon-weixin"></span></view>
						<text class="key">微信支付：</text>
					</view>
					<view class="icon-box d-c-c"><span class="icon iconfont icon-xuanze"></span></view>
				</view>
			</view>
			<view v-if="orderType!=40" class="item">
				<view class="d-s-c">
					<view class="icon-box d-c-c mr10"><span class="icon iconfont icon-yue"></span></view>
					<text class="key">余额抵扣：(剩余：{{balance}})</text>
				</view>
				<switch :color="getThemeColor()" style="transform:scale(0.7);margin-right: -20rpx;"
					:checked="balanceType" @change="switch2Change" />
			</view>
		</view>
		<view class="bottom-btn theme-btn" @click="submit">立即支付</view>
	</view>
</template>

<script>
	import {
		pay
	} from '@/common/pay.js';
	export default {
		data() {
			return {
				balance: '',
				balanceType: false,
				type: 0,
				loading: true,
				orderId: 0,
				// 10 普通订单 20积分兑换 30会员卡 40充值 50券包  60骑手 70团购
				orderType: 0,
				payType: 0,
				checkedPay: [],
				payPrice: '',
				hasBanlance: false
			}
		},
		computed: {},
		onLoad(e) {
			let self = this;
			this.orderId = e.orderId;
			if (e.orderType) {
				this.orderType = e.orderType;
			}
			this.getData()
		},
		methods: {
			getData() {
				let self = this;
				self.loading = true;
				uni.showLoading({
					title: '加载中'
				});
				let url = 'user/order/toPay';
				if (self.orderType == 30) {
					url = 'user/UserCard/toPay';
				}
				if (self.orderType == 40) {
					url = 'balance/plan/toPay';
				}
				if (self.orderType == 50) {
					url = 'plus/package/Package/toPay';
				}
				if (self.orderType == 60) {
					url = 'plus/driver/apply/toPay';
				}
				if (self.orderType == 70) {
					url = 'plus/group/Order/toPay';
				}
				let params = {
					orderId: self.orderId,
					paySource: self.getPlatform()
				}
				self._get(
					url, params,
					function(res) {
						self.loading = false;
						let list = [];
						self.payPrice = res.data.payPrice;
						self.balance = res.data.balance || '';
						self.checkedPay = res.data.payTypes;
						self.hasBanlance = res.data.useBalance;
						if (self.checkedPay.length > 0) {
							self.payType = self.checkedPay[1];
							console.log('payType', self.payType);
						} else {
							self.payType = 0;
						}
						uni.hideLoading();
					})
			},
			switch2Change(e) {
				this.balanceType = e.detail.value;
				console.log('balancetype', this.balanceType);
			},
			submit() {
				let self = this;
				self.loading = true;
				uni.showLoading({
					title: '加载中'
				});
				let url = 'user/order/pay';
				if (self.orderType == 30) {
					url = 'user/UserCard/pay';
				}
				if (self.orderType == 40) {
					url = 'balance/plan/pay';
				}
				if (self.orderType == 50) {
					url = 'plus/package/Package/pay';
				}
				if (self.orderType == 60) {
					url = 'plus/driver/apply/pay';
				}
				if (self.orderType == 70) {
					url = 'plus/group/Order/pay';
				}
				let useBalance = self.balanceType == true ? 1 : 0;
				if (self.payPrice == 0) {
					useBalance = 1;
				}
				let payType = self.payType;
				if (payType == 10) {
					payType = 0;
				}
				let params = {
					orderId: self.orderId,
					paySource: self.getPlatform(),
					payType: payType,
					useBalance: useBalance,
				}
				self._postBody(url, params,
					function(res) {
						self.loading = false;
						uni.hideLoading();
						console.log(res);
						pay(res, self, self.paySuccess, self.payError);
					})
			},
			paySuccess(result) {
				let self = this;
				if (self.orderType == 30 || self.orderType == 40 || self.orderType == 50) {
					uni.showModal({
						title: '提示',
						content: '支付成功',
						success() {
							// #ifndef H5
							uni.navigateBack({
								delta: parseInt(1)
							});
							// #endif
							// #ifdef H5
							history.go(-1);
							// #endif
						}
					})
				} else if (self.orderType == 60) {
					uni.showModal({
						title: '提示',
						content: '支付成功',
						success() {
							self.gotoPage('/pages/user/index/index')
						}
					})

				} else if (self.orderType == 70) {
					self.gotoPage('/pages/order/group/pay-success?orderId=' + result.data.orderId, 'reLaunch');
				} else {
					self.gotoPage('/pages/order/pay-success/pay-success?orderId=' + result.data.orderId, 'reLaunch');
				}
			},
			payError(result) {
				let self = this;
				let url = '/pages/order/myorder';
				if (self.orderType == 30 || self.orderType == 40 || self.orderType == 50) {
					uni.showModal({
						title: '提示',
						content: '支付失败',
						success() {
							// #ifndef H5
							uni.navigateBack({
								delta: parseInt(1)
							});
							// #endif
							// #ifdef H5
							history.go(-1);
							// #endif
						}
					})
				} else if (self.orderType == 60) {
					uni.showModal({
						title: '提示',
						content: '支付失败',
						success() {
							self.gotoPage('/pages/user/index/index')
						}
					})
				} else if (self.orderType == 70) {
					self.gotoPage('/pages/order/group/detail?orderId=' + result.data.orderId, 'reLaunch');
				} else {
					self.gotoPage('/pages/order/order-detail?orderId=' + result.data.orderId, 'redirect');
				}
			},
			payTypeFunc(n) {
				this.payType = n;
			}
		}
	}
</script>

<style lang="scss">
	.icon-box {
		.icon.iconfont.icon-xuanze {
			font-size: 36rpx;
		}
	}

	.buy-checkout-top {
		padding: 100rpx 0;
	}

	.buy-checkout {
		background-color: #FFFFFF;
		margin: 0 24rpx;
		border-radius: $uni-border-radius-base;

		.item {
			border-bottom: 1px solid #eee;
			padding-left: 0;
			padding-right: 0;
			margin: 0 20rpx;
		}

		.item:last-child {
			border: none;
		}
	}

	.bottom-btn {
		position: fixed;
		bottom: 0;
		@include background_color("background_color");
		@include font_color('text_color2');
		width: 710rpx;
		margin: 20rpx;
		box-sizing: border-box;
		font-size: 30rpx;
		height: 96rpx;
		border-radius: 72rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
</style>