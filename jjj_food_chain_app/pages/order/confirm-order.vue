<template>
	<view :data-theme="theme()" :class="theme() || ''">
		<view class="wrap" v-if="!loading">
			<view class="right" v-if="cartType == 0">
				<template v-for="(item, index) in deliverySet" :key="index+'1'">
					<view class="takeout d-c-c" v-if="item == '10'" :class="tabType == 0 ? 'active' : ''"
						@click="tabFunc(0)">
						<view class="d-c-c">
							<text class="icon-waimai1 icon iconfont top-icon"></text>
							<text>配送</text>
						</view>
					</view>
				</template>
				<template v-for="(item, index) in deliverySet" :key="index+'2'">
					<view class="dinein d-c-c" v-if="item == '20'" :class="tabType == 1 ? 'active' : ''"
						@click="tabFunc(1)">
						<view class="d-c-c">
							<text class="icon-wodedianpu icon iconfont top-icon"></text>
							<text>自取</text>
						</view>
					</view>
				</template>
			</view>
			<view class="right" v-if="cartType == 1">
				<template v-for="(item, index) in storeSet" :key="item">
					<view class="takeout d-c-c" v-if="item == '30'" :class="tabType == 3 ? 'active' : ''"
						@click="tabFunc(3)">
						<view class="d-c-c">
							<text class="icon-waimai icon iconfont top-icon"></text>
							<text>打包</text>
						</view>
					</view>
				</template>
				<template v-for="(item, index) in storeSet" :key="item">
					<view class="dinein d-c-c" v-if="item == '40'" :class="tabType == 4 ? 'active' : ''"
						@click="tabFunc(4)">
						<view class="d-c-c">
							<text class="icon-dianneijiucan icon iconfont top-icon"></text>
							<text>堂食</text>
						</view>
					</view>
				</template>
			</view>
			<view class="header" v-if="cartType == 0">
				<view class="headr_top">
					<view class="flex-1" style="width: 100%;" v-if="tabType != 1">
						<view class="left overflow-hidden">
							<view class="overflow-hidden w-b-a" style="width: 600rpx;"
								@click="gotoPage('/pages/user/address/storeaddress?shopSupplierId=' + options.shopSupplierId)">
								<template v-if="Address != null">
									<view class="f32 fb gray3 mb20" style="word-break: break-all;">
										{{ Address.detail + Address.address}}
									</view>
									<view class="f28 gray3">{{Address.name}}({{Address.phone}})</view>
								</template>
								<template v-else>
									请选择配送地址
								</template>
							</view>
						</view>
					</view>
					<view class="header_bottom d-b-c" v-if="tabType == 1">
						<view class="shop-info" style="width: 430rpx;">
							<view class="f32 fb mb10">{{supplier.name}}
							</view>
							<view class="gray9 shop-address">{{supplier.address}}</view>
						</view>
						<view class="d-c d-c-c">
							<view class="icon iconfont icon-address shop-pop-icon"></view>
						</view>
					</view>
				</view>
				<view class="d-b-c meal_item" v-if="tabType == 1 && delivery != 10">
					<view class="f28">预留电话</view>
					<input v-model="phone" type="text" class="header-input" placeholder="请输入" />
				</view>
				<view class="d-b-c meal_item" v-if="tabType == 1 && delivery != 10">
					<view class="f28">取餐时间</view>
					<view class="">
						<text class="f32 theme-price">{{ estitime }}</text>
					</view>
				</view>
				<view class="d-b-c meal_item" @click="timepick()" v-if="tabType == 0 && delivery == 10">
					<view class="f28">预计送达时间</view>
					<view class="">
						<text class="theme-price f32">{{ wmtime }}</text>
						<text class="icon iconfont icon-jiantou"></text>
					</view>
				</view>
			</view>
			<!--购买的产品-->
			<view class="vender">
				<view class="left d-s-c">
					<view class="store-name">
						<text>{{ supplier.name }}</text>
					</view>
				</view>
				<view class="list">
					<view class="item d-b-c" v-for="(item, index) in ProductData" :key="index">
						<view class="info d-s-s">
							<view class="cover">
								<image :src="item.productImage" mode="aspectFill"></image>
							</view>
							<view class="flex-1">
								<view class=" f30 fb mb16">{{ item.productName }}</view>
								<view class="num-wrap pl-30 gray9 f26">{{ item.describ }}</view>
								<view class="f26 gray9">×{{ item.productNum }}</view>
							</view>
						</view>
						<view class="" style="height: 148rpx;text-align: right;">
							<view class="f32 order_item mb16">¥{{ item.price }}</view>

						</view>
					</view>
				</view>
				<!--总数-->
				<view class="other_box">
					<view class="item">
						<text class="key">商品小计：</text>
						<text class="f32">￥{{ OrderData.orderTotalPrice }}</text>
					</view>
					<view class="item">
						<text class="key">包装费用：</text>
						<text class="f32">￥{{ OrderData.orderBagPrice }}</text>
					</view>
					<view class="item" v-if="tabType != 1 && OrderData.expressPrice != 0">
						<text class="key">配送费：</text>
						<view>
							<text class="gray9 f32 text-d-line"
								v-if="OrderData.totalExpressPrice != OrderData.expressPrice">{{OrderData.totalExpressPrice}}</text>
							<text class="f32">{{ OrderData.expressPrice }}</text>
						</view>
					</view>
					<view class="item" v-if="OrderData.tableNo">
						<text class="key">桌号：</text>
						<text class="f32">{{ OrderData.tableNo }}</text>
					</view>
				</view>
				<view class="total-box">
					共{{ orderTotalNum }}件商品 小计
					<text class="f32 fb ml15">￥{{ OrderData.orderPayPrice }}</text>
				</view>
			</view>
			<view class="remarks">
				<view class="d-b-c item">
					<view class="mr20">备注:</view>
					<input class="flex-1" type="text" v-model="remark" placeholder="请填写您的其他要求" />
				</view>
			</view>
			<!--底部支付-->
			<view class="foot-pay-btns">
				<view class="price" v-if="!OrderData.forcePoints">
					¥
					<text class="num">{{ OrderData.orderPayPrice }}</text>
				</view>
				<button class="theme-btn" @click="SubmitOrder">提交订单</button>
			</view>
			<timepicker :isTimer="isTimer" @close="closetimer"></timepicker>
		</view>
	</view>
</template>

<script>
	import timepicker from "@/components/timepicker/timepicker";
	import {
		pay
	} from "@/common/pay.js";
	export default {
		components: {
			timepicker,
		},
		data() {
			return {
				/*是否加载完成*/
				loading: true,
				options: {},
				indicatorDots: true,
				autoplay: true,
				interval: 2000,
				duration: 500,
				tabType: 0,
				/*商品id*/
				productId: "",
				/*商品数量*/
				productNum: "",
				/*商品数据*/
				ProductData: [],
				/*订单数据数据*/
				OrderData: [],
				// 是否存在收货地址
				existAddress: false,
				/*默认地址*/
				Address: {
					region: [],
				},
				extractStore: {},
				lastExtract: {},
				productSkuId: 0,
				/*配送方式*/
				/* 10配送20自提30打包40店内 */
				delivery: 0,
				/*自提店id*/
				storeId: 1,
				linkman: "",
				phone: "",
				remark: "",
				deliverySetting: [],
				/*消息模板*/
				temlIds: [],
				/*是否显示支付宝支付，只有在h5，非微信内打开才显示*/
				showAlipay: false,
				takeout_address: {},
				isTimer: false,
				mealtime: "",
				wmtime: "",
				estitime: "",
				is_pack: 1,
				supplier: {},
				dinnerType: 10,
				cartType: 0,
				storeSet: [],
				deliverySet: [],
				tableId: 0,
				minMoney: 0,
				orderTotalNum: '',
			};
		},
		onLoad(options) {
			let self = this;

			self.options = options;
			self.cartType = options.cartType;
			self.tableId = options.tableId || 0;
			self.dinnerType = options.dinnerType;
			self.delivery = parseFloat(options.delivery);
			this.getData();

			// self.getData();
		},
		onShow() {
			this.$fire.on("takeout", function(e) {
				self.takeout_address = e;
				self.orderType = "takeout";
			});
		},
		methods: {
			/**/
			hasType(e) {
				if (this.deliverySetting.indexOf(e) != -1) {
					return true;
				} else {
					return false;
				}
			},
			changeTime(n) {},
			getTime(type) {
				let myDate = new Date();
				let myhours = myDate.getHours(); //获取当前小时数(0-23)
				if (myhours < 10) {
					myhours = "0" + myhours;
				}
				let myminute = myDate.getMinutes(); //获取当前分钟数(0-59)
				if (myminute < 10) {
					myminute = "0" + myminute;
				}
				let wmhours = myDate.getHours();
				let wmminute = myDate.getMinutes() + 15;
				if (wmminute >= 60) {
					wmminute = wmminute - 60;
					wmhours = wmhours + 1;
				}
				if (wmminute < 10) {
					wmminute = "0" + wmminute;
				}
				if (wmhours < 10) {
					wmhours = "0" + wmhours;
				}
				if (type == "my") {
					return myhours + ":" + myminute;
				} else if (type == "wm") {
					return wmhours + ":" + wmminute;
				}
			},
			/*获取数据*/
			getData() {
				let self = this;
				uni.showLoading({
					title: "加载中",
				});
				self.loading = true;
				let callback = function(res) {
					self.OrderData = res.data.orderInfo;
					self.minMoney = res.data.supplier.minMoney;
					self.temlIds = res.data.templateArr;
					self.orderTotalNum = res.data.orderTotalNum;
					self.existAddress = self.OrderData.existAddress;
					self.Address = self.OrderData.address;
					self.extractStore = self.OrderData.extractStore;
					self.lastExtract = self.OrderData.lastExtract;
					self.ProductData = res.data.productList;
					self.supplier = res.data.supplier;
					self.linkman = res.data.orderInfo.lastExtract.linkman;
					self.phone = res.data.orderInfo.lastExtract.phone;
					self.deliverySet = res.data.supplier.deliverySetList;
					self.storeSet = res.data.supplier.storeSetList;
					if (self.OrderData.delivery == 10) {
						self.tabType = 0;
					}
					if (self.OrderData.delivery == 20) {
						self.tabType = 1;
					}
					if (self.OrderData.delivery == 30) {
						self.tabType = 3;
					}
					if (self.OrderData.delivery == 40) {
						self.tabType = 4;
					}
					if (self.cartType == 0) {
						if (self.deliverySet.indexOf(self.delivery) == -1) {
							if (self.deliverySet[0] == 10) {
								console.log("执行");
								self.tabFunc(0, true);
							} else {
								self.tabFunc(1, true);
							}
						}
					} else {
						self.delivery = parseFloat(self.delivery);
						if (self.storeSet.indexOf(self.delivery) == -1) {
							if (self.storeSet[0] == 30) {
								self.tabFunc(3, true);
							} else {
								self.tabFunc(4, true);
							}
						}
					}
					self.wmtime = self.getTime("wm");
					self.mealtime = self.getTime("my");
					self.estitime = self.getTime("wm");
					self.deliverySetting = self.OrderData.deliverySetting;
					self.loading = false;
					if (self.OrderData.delivery == 30) {
						self.tabType = 3;
					}
					if (self.OrderData.delivery == 20) {
						self.tabType = 1;
					}
				};

				// 请求的参数
				let params = {
					delivery: self.delivery || 0,
					storeId: 1,
					mealtime: "",
					paySource: self.getPlatform(),
				};
				if (self.tableId) {
					params.tableId = self.tableId;
				}
				//直接购买
				if (self.options.orderType === "buy") {
					self._get(
						"order/order/buy",
						Object.assign({}, params, {
							productId: self.options.productId,
							productNum: self.options.productNum,
							productSkuId: self.options.productSkuId,
						}),
						function(res) {
							callback(res);
						}
					);
				}
				// 购物车结算
				else if (self.options.orderType === "cart") {
					self._postBody(
						"order/order/toCart",
						Object.assign({}, params, {
							cartIds: self.options.cartIds || 0,
							shopSupplierId: self.options.shopSupplierId || 0,
							orderType: self.options.cartType,
							tableId: self.tableId>0?self.tableId:0,
						}),
						function(res) {
							callback(res);
						},
						function(err) {
							if (self.tabType == 1) {
								self.tabFunc(0);
							} else if (self.tabType == 0) {
								self.tabFunc(1);
							}
						}
					);
				}
			},

			/*选择配送方式*/
			tabFunc(e, flag) {
				if (e == 0) {
					if (this.minMoney * 1 > this.OrderData.orderPayPrice * 1) {
						this.showError("未满足最低配送费用!");
						return;
					}
				}
				/* 0外卖 1自取  3打包 4店内 */
				this.tabType = e;
				if (e == 0) {
					this.delivery = 10;
					this.dinnerType = 10;
				} else if (e == 1) {
					this.delivery = 20;
					this.dinnerType = 20;
				} else if (e == 3) {
					this.delivery = 30;
					this.dinnerType = 30;
				} else if (e == 4) {
					this.delivery = 40;
					this.dinnerType = 30;
				}
				if (!flag) {
					console.log("切换");
					console.log("切换type", this.tabType);
					this.getData();
				}
			},

			/*提交订单*/
			SubmitOrder() {
				let self = this;
				if (this.loading) {
					return;
				}

				let _linkman = null;
				let _phone = null;
				if (self.$refs != null) {
					if (self.$refs.getShopinfoData != null) {
						_phone = self.$refs.getShopinfoData.phone;
						_linkman = self.$refs.getShopinfoData.linkman;
					}
				}
				console.log(this.tabType, "type");
				if (self.tabType == 1) {
					self.delivery = 20;
					self.dinnerType = 20;
				}
				let params = {
					delivery: self.delivery,
					storeId: 1,
					linkman: self.linkman,
					phone: self.phone,
					remark: self.remark,
					mealtime: self.mealtime,
					shopSupplierId: self.options.shopSupplierId,
					paySource: self.getPlatform(),
				};
				if (self.delivery == 10) {
					params.mealtime = self.wmtime;
				}
				if (self.tabType == 1 && self.delivery != 10) {
					params.mealtime = self.getTime("my");
				}

				// 创建订单-直接下单
				let url = "";
				if (self.options.orderType === "buy") {
					url = "order/order/buy";
					params = Object.assign(params, {
						productId: self.options.productId,
						productNum: self.options.productNum,
						productSkuId: self.options.productSkuId,
					});
				}

				// 创建订单-购物车结算
				if (self.options.orderType === "cart") {
					url = "order/order/cart";
					params = Object.assign(params, {
						cartIds: self.options.cartIds || 0,
						dinnerType: self.dinnerType,
						shopSupplierId: self.options.shopSupplierId || 0,
						orderType: self.options.cartType,
            tableId: self.tableId>0?self.tableId:0,
					});
				}
				let callback = function() {
					self.loading = true;
					uni.showLoading({
						title: "加载中",
						mask: true,
					});
					self._postBody(
						url,
						params,
						function(result) {
							let pages =
								"/pages/order/cashier?orderType=10&orderId=" + result.data;
							console.log("order", result);
							self.gotoPage(pages, "reLaunch");
						},
						(err) => {
							self.loading = false;
						}
					);
				};

				self.subMessage(self.temlIds, callback);
			},
			timepick() {
				this.isTimer = true;
			},
			closetimer(e) {
				if (e != "") {
					this.wmtime = e;
					this.mealtime = e;
				}
				this.isTimer = false;
			},
			packTypeFunc(n) {
				this.is_pack = n;
			},
		},
	};
</script>

<style lang="scss">
	.headr_top {
		min-height: 140rpx;
		padding-bottom: 20rpx;
		box-sizing: border-box;
	}

	.header {
		width: 100%;
		box-sizing: border-box;
		padding: 35rpx;
		padding-bottom: 0;
		background-color: #ffffff;
		// box-shadow: 0 0 0.06rem 0 rgba(0, 0, 0, 0.1);
		border-radius: 30rpx;
		overflow: hidden;
		position: relative;
		z-index: 20;
	}

	.left {
		flex: 1;
		display: flex;
		flex-direction: column;

		.store-name {
			width: 100%;
			display: flex;
			justify-content: flex-start;
			align-items: center;
			font-size: 26rpx;
			margin-bottom: 30rpx;

			.iconfont {
				margin-left: 10rpx;
				line-height: 100%;
			}
		}

		.store-location {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			color: $text-color-assist;
			font-size: $font-size-sm;

			.iconfont {
				vertical-align: middle;
				display: table-cell;
				color: $color-primary;
				line-height: 100%;
			}
		}
	}

	.wrap {
		padding: 25rpx;
		padding-bottom: 140rpx;
		// @include background_linear('background_color', 'opacify_background_0', 180deg, 0, 100%);
	}

	.icon-box .icon-zhifubao {
		color: #1296db;
		font-size: 50rpx;
	}

	.order_item {
		width: 150rpx;
		text-align: right;
	}

	.other_box {
		border-radius: 0;
		box-shadow: 0;
	}

	.other_box .item {
		height: 88rpx;
		box-sizing: border-box;
		line-height: 88rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: 1px solid #eeeeee;
	}

	.header_bitem {}

	.f-r {
		float: right;
	}

	.meal_item {
		height: 92rpx;
		line-height: 92rpx;
		border-top: 1px solid #eee;

		.icon-jiantou {
			font-size: 22rpx;
			margin-left: 26rpx;
			color: #666;
		}
	}

	.remarks {
		margin: 26rpx 0;
		height: 96rpx;
		line-height: 96rpx;
		background-color: #ffffff;
		border-radius: 10rpx;
		padding-left: 42rpx;
		padding-right: 30rpx;

		.icon-jiantou {
			font-size: 22rpx;
			margin-left: 26rpx;
			color: #666;
		}
	}

	.pack_item {
		text-align: center;
		height: 80rpx;
		line-height: 80rpx;
	}

	.pack_item.active .icon-xuanze {
		@include font_color('font_color');
	}

	.right {
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		font-size: 32rpx;
		color: #282828;
		height: 68rpx;
		width: 100%;
		position: relative;
		margin-top: 20rpx;

		.dinein,
		.takeout {
			position: relative;
			display: flex;
			align-items: flex-start;
			width: 344rpx;
			height: 150rpx;
			box-sizing: border-box;
			padding-top: 25rpx;
			position: absolute;
			z-index: 0;
			top: -18rpx;
			background-color: #ECECEC;

			&.active {
				z-index: 1;
				width: 490rpx;
				height: 150rpx;
				font-weight: bold;
				// @include font_color('font_color');
				background-color: #ffffff !important;
				flex-shrink: 0;
			}
		}

		.takeout {
			justify-content: flex-start;

			padding-left: 78rpx;
			left: 0;
			border-top-left-radius: 30rpx;

			&.active {
				padding-left: 153rpx;
				border-top-right-radius: 150rpx;
				border-top-left-radius: 30rpx;
			}
		}

		.dinein {
			right: 0;
			justify-content: flex-end;
			padding-right: 65rpx;
			border-top-right-radius: 30rpx;

			&.active {
				padding-right: 150rpx;
				border-top-left-radius: 150rpx;
				border-top-right-radius: 30rpx;
			}
		}
	}

	.foot-pay-btns button {
		padding: 0 50rpx;
		height: 84rpx;
		line-height: 84rpx;
		border-radius: 50rpx;
	}

	.discount {
		display: inline-block;
		font-size: 24rpx;
		color: #fff;
		border-radius: 5rpx;
		width: 40rpx;
		height: 40rpx;
		line-height: 40rpx;
		text-align: center;
		background: #fa301b;
		margin-right: 5rpx;
		font-weight: 400;
	}

	.discount.discount-s {
		width: 32rpx;
		height: 32rpx;
		line-height: 32rpx;
		margin-right: 8rpx;
		font-size: 20rpx;
	}

	.right .active .top-icon {
		display: block;
	}

	.right .top-icon {
		display: none;
		font-size: 40rpx;
		color: #333;
		margin-right: 10rpx;
	}

	.header-input {
		font-size: 26rpx;
		text-align: right;
		flex: 1;
		margin-left: 20rpx;
	}

	.shop-info {
		width: 430rpx;
		margin-right: 20rpx;

		.shop-address {
			line-height: 1.5;
			font-size: 24rpx;
		}
	}

	.shop-pop-distance {
		white-space: nowrap;
		font-size: 20rpx;
		color: #333;
		// width: 152rpx;
		height: 38rpx;
		line-height: 38rpx;
		padding: 0 18rpx;
		background: #FFFFFF;
		box-shadow: 0rpx 3rpx 7rpx 0rpx rgba(0, 0, 0, 0.25);
		border-radius: 19rpx;
		margin-bottom: 20rpx;
	}

	.icon.shop-pop-icon {
		width: 58rpx;
		height: 58rpx;
		background: #FFFFFF;
		box-shadow: 0rpx 3rpx 7rpx 0rpx rgba(4, 0, 0, 0.15);
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 32rpx;
		color: #333;
		border-radius: 50%;
	}

	.sup_img {
		width: 44rpx;
		height: 44rpx;
		background: rgba(0, 0, 0, 0);
		opacity: 1;
		border-radius: 50%;
		margin-right: 10rpx;
		display: block;
	}
</style>
