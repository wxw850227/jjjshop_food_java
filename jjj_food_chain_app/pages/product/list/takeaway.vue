<template>
	<view class="container" :data-theme="theme()" :class="theme() || ''">
		<view class="main">
			<view class="nav">
				<view class="header">
					<view v-if="orderType == 'takein'" class="left">
						<view class="store-name">
							<text class="fb">{{ supplier.name }}</text>
						</view>
						<view class="gray9 f24"><text
								class="icon iconfont icon-diliweizhi"></text>距离您{{ supplier.distance }}</view>
					</view>
					<view v-if="orderType == 'takeout'" class="left">
						<view class="store-name">
							<text class="fb">{{ supplier.name }}</text>
						</view>
						<view class="d-s-c f24" style="margin-right: 90rpx;" v-if="addressDetail">
							<view class="text-ellipsis gray9 d-s-c">
								<text class="icon iconfont icon-dizhi"></text>
								配送地址：{{addressDetail || '请选择收货地址'}}
							</view>
							<view class="theme-notice f-s-0"
								@click="gotoPage('/pages/user/address/storeaddress?shopSupplierId=' + shopSupplierId)">
								更换地址
							</view>
						</view>
						<view class="d-s-c f24" style="margin-right: 90rpx;" v-else
							@click="gotoPage('/pages/user/address/storeaddress?shopSupplierId='+shopSupplierId)">
							<view class="text-ellipsis gray9 d-s-c"><text class="icon iconfont icon-dizhi"></text>
								请选择收货地址
							</view>
						</view>
					</view>
					<view class="dinner-right">
						<template v-for="(item, index) in deliverySet" :key="'a-'+index">
							<view class="dinner_type" v-if="item == '10'" :class="{active:orderType == 'takeout'}"
								@tap="takout">
								<text>配送</text>
							</view>
						</template>
						<template v-for="(item, index) in deliverySet" :key="'b-'+index">
							<view class="dinner_type" v-if="item == '20'" :class="{active:orderType == 'takein'}"
								@tap="takein">
								<text>自取</text>
							</view>
						</template>
					</view>
				</view>
			</view>
			<view class="content">
				<scroll-view class="menus" :style="'height:' + scrollviewHigh + 'px;'"
					:scroll-into-view="menuScrollIntoView" scroll-y>
					<view class="category-wrapper">
						<template v-for="(item, index) in productList" :key="index">
							<view v-if="item.products.length != 0" class="menu d-s-c pr" :id="`menu-${item.categoryId}`"
								:class="{ current: item.categoryId === currentCateId }"
								@tap="handleMenuTap(item.categoryId)">
								<image v-if="item.productImage" class="f-s-0 menu-imgs" :src="item.productImage"
									mode="aspectFill"></image>
								<view class="text-ellipsis">{{ item.name }}</view>
								<view class="menu-cartNum" v-if="item.productNum">{{item.productNum}}</view>
							</view>
						</template>
						<view class="menu-bottom"></view>
					</view>
				</scroll-view>
				<!-- goods list begin -->
				<scroll-view class="goods pr" :style="'height:' + scrollviewHigh + 'px;'" scroll-y
					:scroll-top="cateScrollTop" @scroll="handleGoodsScroll">
					<view class="goods-wrapper">
						<view class="list" :style="'padding-bottom:'+(scrollviewHigh*2 - 238)+'rpx;'">
							<!-- category begin -->
							<template v-for="(item, index) in productList" :key="index">
								<view class="category" v-if="item.products.length != 0" :id="`cate-${item.categoryId}`">
									<view class="title">
										<text>{{ item.name }}</text>
									</view>
									<view class="goods-items">
										<!-- 商品 begin -->
										<view class="good" @click="gotoDetail(good)"
											v-for="(good, key) in item.products" :key="key">
											<view class="image-boxs">
												<view class="sallsell-out" v-if="good.productStock<=0">
													<view class="sallsell-out-btn">当前售罄</view>
												</view>
												<image :src="good.productImage" class="image"></image>
											</view>
											<view class="product-info">
												<view class="ww100">
													<view class="name text-ellipsis">{{ good.productName }}</view>
													<view class="tips text-ellipsis">{{ good.sellingPoint }}</view>
												</view>
												<view class="price_and_action">
													<view>
														<text class="f24 theme-price">￥</text>
														<text class="f34 theme-price">{{ good.productPrice }}</text>
														<text class="linprice"
															v-if="good.productPrice * 1 != good.linePrice * 1">￥{{ good.linePrice * 1 }}</text>
													</view>
													<view class="btn-group">
														<image @tap.stop="reduceFunc(good)" v-if="good.cartNum"
															class="add-image"
															:src="'/static/icon/cart/reduce-'+theme()+'.png'" mode="">
														</image>
														<view class="number" v-if="good.cartNum != 0">
															{{ good.cartNum }}
														</view>
														<image @tap.stop="addCart(good)" v-if="good.productStock > 0"
															class="add-image"
															:src="'/static/icon/cart/add-'+theme()+'.png'" mode="">
														</image>
														<image v-if="good.productStock <= 0" class="add-image"
															src="/static/icon/cart/add-null.png" mode="">
														</image>
													</view>
												</view>
											</view>
										</view>
										<!-- 商品 end -->
									</view>
								</view>
							</template>
							<!-- category end -->
						</view>
					</view>
				</scroll-view>
				<!-- goods list end -->
			</view>
			<!-- 购物车栏 begin -->
			<view class="cart-box" @click="openCartPopup(0)">
				<view class="mark">
					<view class="icon iconfont icon-gouwudai cart-view"></view>
					<view class="tag" v-if="cartTotalNum">{{ cartTotalNum }}</view>
				</view>
				<view class="price" v-if="cartTotalNum">
					<view>
						<text class="f22 ">￥</text>
						<text class="f36">{{ totalPrice }}</text>
					</view>
					<view class="gray9 f22" v-if="totalBagPrice != 0">
						<text class="">包含包装费￥</text>
						{{ totalBagPrice }}
					</view>
				</view>
				<view v-else class="flex-1 f32 white">未选购商品</view>
				<button class="pay-btn" :disabled="!cartTotalNum" @click.stop="toPay"
					v-if="minMoneyDiff <= 0 || orderType != 'takeout'">去结算</button>
				<button class="btn-gray" disabled
					v-if="minMoneyDiff > 0 && totalPrice == 0 && orderType == 'takeout'">{{ '￥' + minMoney + '起送' }}</button>
				<button class=" btn-gray" disabled
					v-if="minMoneyDiff > 0 && totalPrice != 0 && orderType == 'takeout'">{{ '还差￥' + minMoneyDiff + '起送' }}</button>
			</view>
			<!-- 购物车栏 end -->
		</view>
		<!-- 购物车详情popup -->
		<popup-layer direction="top" :show-pop="cartPopupVisible" class="cart-popup" v-if="cartTotalNum > 0"
			@closeCallBack="closeCallBack">
			<template #content>
				<view class="cart-popup pr">
					<view class="top d-b-c">
						<view class="f30 gray3 d-s-c">
							购物车
							<view class="f22 gray3" v-if="totalBagPrice">（打包费 <text
									class="theme-price">￥{{totalBagPrice}}</text>）</view>
						</view>
						<view @tap="handleCartClear" class="d-c-c"><text
								class="icon iconfont icon-shanchu1"></text>清空购物车</view>
					</view>
					<scroll-view class="cart-list" scroll-y>
						<view class="wrapper">
							<template v-for="(item, index) in cartList" :key="index">
								<view class="item" v-if="item.productNum > 0">
									<view class="d-s-c ww100">
										<view class="cart-image">
											<image style="" :src="item.productImage" mode="aspectFill"></image>
										</view>
										<view class="left">
											<view>
												<view class="name text-ellipsis">{{ item.product.productName }}</view>
												<view class="gray9">{{ item.describ }}</view>
											</view>
											<view class="center">
												<text>￥</text>
												<text class="f34">{{ item.price }}</text>
												<text class="f24 gray9 ml10"
													v-if="bagType != 1">包装费￥{{ item.bagPrice }}</text>
											</view>
										</view>
									</view>
									<view class="right">
										<image @tap="cartReduce(item)" class="btn-image"
											:src="'/static/icon/cart/reduce-'+theme()+'.png'" mode=""></image>
										<view class="number">{{ item.productNum }}</view>
										<image @tap="cartAdd(item)" class="btn-image"
											:src="'/static/icon/cart/add-'+theme()+'.png'" mode=""></image>
									</view>
								</view>
							</template>
						</view>
					</scroll-view>
				</view>
			</template>
		</popup-layer>
		<!-- 购物车详情popup -->
		<!-- 商品规格详情 -->
		<prospec v-if="isDetail" :productModel="productModel" @close="closeGoodDetailModal"></prospec>
		<view class="loading" v-if="loading">
			<image src="/static/images/loading.gif"></image>
		</view>
	</view>
</template>

<script>
	import modal from '@/components/modal/modal';
	import popupLayer from '@/components/popup-layer/popup-layer';
	import prospec from './popup/spec.vue';
	//#ifdef H5
	import jweixin from 'weixin-js-sdk';
	//#endif
	export default {
		components: {
			modal,
			popupLayer,
			prospec,
		},
		data() {
			return {
				loadingClock: false,
				isDetail: false,
				isLoading: true,
				supplier: {
					name: '',
					businessStatus: 0
				},
				shopSupplierId: 0,
				loading: true,
				currentCateId: 0, //默认分类
				cateScrollTop: 0,
				menuScrollIntoView: '',
				/* 分类定位初始化 */
				sizeCalcState: false,
				productList: [],
				/* 商品详情数据 */
				productModel: {},
				//
				clock: false,
				totalPrice: 0,
				/* 购物车弹窗 */
				cartTotalNum: 0,
				cartProductId: 0,
				cartPopupVisible: false,
				/* 弹窗购物车列表 */
				cartList: [],
				cartType: 0,
				phoneHeight: 0,
				/*可滚动视图区域高度*/
				scrollviewHigh: 0,
				/* 添加商品锁 */
				addclock: false,
				/* 收货地址 */
				addressDetail: '',
				longitude: 0,
				latitude: 0,
				addressId: 0,
				/* ---- */
				/* 是否收取打包费 */
				bagType: 1,
				totalBagPrice: 0,
				// orderType 与 dinnerType 重复待删除
				orderType: '',
				/* 10配送20自提30店内40外卖 */
				dinnerType: 20,
				// 配送方式
				deliverySet: [],
				isFirst: true,
				/* 起送 */
				minMoney: 0,
				minMoneyDiff: 0,
				linePrice: 0,
				scrollLast: 0,
			};
		},
		onLoad(e) {
			let self = this;
			self.orderType = e.orderType;
			if (self.orderType == 'takeout') {
				self.dinnerType = 10;
			} else {
				self.dinnerType = 20;
			}
			self.shopSupplierId = uni.getStorageSync('selectedId') ? uni.getStorageSync('selectedId') : 0;
		},
		onShow() {
			let self = this;
			self.isDetail = false;
			self.init();
		},
		methods: {
			stopClick() {
				return false;
			},
			/* 获取用户登录状态 */
			getUserInfo() {
				let self = this;
				self.loading = true;
				self.isLogin = false;
				self._get("index/userInfo", {}, function(res) {
					/* 已登录 */
					if (res.data) {
						self.isLogin = true;
					}
					if (self.dinnerType == 10 && res.data) {
						if (res.data.addressDefault && res.data.addressId) {
							self.latitude = res.data.addressDefault.latitude;
							self.longitude = res.data.addressDefault.longitude;
							self.addressDetail = res.data.addressDefault.detail;
							self.getCategory(true);
						} else {
							self.getcityData();
						}
					} else {
						/* 未登录去获取当前定位 */
						self.getcityData();
					}
				});
			},
			init() {
				//页面初始化
				this.addclock = false;
				this.clock = false;
				this.loadingClock = false;
				this.loading = true;
				this.isLoading = true;
				this.productList = [];
				this.sizeCalcState = false;
				this.getUserInfo();
			},
			scrollInit() {
				let self = this;
				if (self.scrollviewHigh) {
					return
				}
				uni.getSystemInfo({
					success(res) {
						self.phoneHeight = res.windowHeight;
						// 计算组件的高度
						let view = uni.createSelectorQuery().select('.nav');
						view.boundingClientRect(data => {
							let h = self.phoneHeight - data.height;
							self.scrollviewHigh = h;
						}).exec();
					}
				});
			},
			/* 获取商品类型 */
			getCategory(flag) {
				let self = this;
				this.sizeCalcState = false;
				let delivery = self.orderType == "takeout" ? 10 : 20;
				self._postBody(
					"product/category/index", {
						type: 0,
						/* 0外卖，1店内 */
						shopSupplierId: self.shopSupplierId,
						longitude: self.longitude,
						latitude: self.latitude,
						delivery: delivery,
						ordertype: 0,
						tableId: 0,
						cartType: self.cartType || 0,
					},
					function(res) {
						self.supplier = res.data.supplier;
						self.minMoney = (res.data.supplier.minMoney * 1).toFixed(2);
						self.productList = res.data.list;
						if (!self.currentCateId) {
							self.currentCateId = self.productList[0].categoryId;
						}
						self.deliverySet = res.data.supplier.deliverySetList;
						if (self.isFirst) {
							if (self.orderType) {
								let orderType = self.orderType == 'takeout' ? 10 : 20;
								if (self.deliverySet.indexOf(orderType) == -1) {
									let content = ''
									if (orderType == 20) {
										content = '当前门店不支持自取，已为您切换为配送';
										self.orderType = 'takeout';
									} else {
										content = '当前门店不支持配送，已为您切换为自取';
										self.orderType = 'takein';
									}
									uni.showModal({
										title: '提示',
										content,
										showCancel: false
									})

								}
							}
							self.isFirst = false;
						}
						self.addressId = res.data.addressId;
						self.bagType = res.data.supplier.bagType;
						self.loading = false;
						self.isLoading = false;
						self.$nextTick(function() {
							self.scrollInit();
						});
						if (self.isLogin && flag) {
							self.getCart();
						}
					},
					function(err) {
						self.showError(err.msg, () => {
							self.gotoPage("/pages/index/index");
						});
					}
				);
			},
			reCart(res) {
				let self = this;
				self.loadingClock = false;

				if (!res.data.cartInfo) {
					return;
				}
				self.cartTotalNum = res.data.cartInfo.cartTotalNum;
				self.linePrice = res.data.cartInfo.totalLineMoney;
				self.totalPrice = res.data.cartInfo.totalPayPrice;
				self.totalBagPrice = res.data.cartInfo.totalBagPrice;
				self.minMoneyDiff = res.data.cartInfo.minMoneyDiff;
				if (!self.cartList || self.cartList == "") {
					self.cartPopupVisible = false;
				}
			},
			addCart(goods) {
				let self = this;
				if (goods.specTypes == 20) {
					self.gotoDetail(goods);
					return;
				}
				if (self.addclock) {
					return;
				}
				if (goods.limitNum != 0 && goods.limitNum <= goods.cartNum) {
					uni.showToast({
						icon: "none",
						title: "超过限购数量",
					});
					return;
				}
				if (goods.productStock <= 0 || goods.productStock <= goods.cartNum) {
					uni.showToast({
						icon: "none",
						title: "没有更多库存了",
					});
					return;
				}

				let params = {
					productId: goods.productId,
					productNum: 1,
					productSkuId: goods.skuList[0].productSkuId,
					attr: "",
					feed: "",
					describ: "",

					price: goods.skuList[0].productPrice,
					bagPrice: goods.skuList[0].bagPrice,
					shopSupplierId: goods.shopSupplierId,
					cartType: 0,
					dinnerType: self.dinnerType,
					productPrice: goods.skuList[0].linePrice,
					delivery: self.orderType == "takeout" ? 10 : 20,
				};
				self.addclock = true;
				uni.showLoading({
					title: "",
					mask: true
				});
				self._postBody(
					"order/cart/add",
					params,
					function(res) {
						uni.hideLoading();
						console.log(res, 'res.data.cartInfo')
						self.reCart(res);
						self.addclock = false;
						self.getCategory(false);
					},
					function(err) {
						uni.hideLoading();
						self.addclock = false;
					}
				);
			},
			reduceFunc(goods) {
				let self = this;
				if (self.addclock || goods.cartNum <= 0) {
					return;
				}
				if (goods.specTypes == 20) {
					self.openCartPopup(goods.productId);
					return;
				}
				let productId = goods.productId;
				self.addclock = true;
				uni.showLoading({
					title: "",
				});
				self._postBody(
					"order/cart/productSub", {
						productId: productId,
						type: "down",
						cartType: 0,
						dinnerType: self.dinnerType,
						shopSupplierId: self.shopSupplierId,
						delivery: self.orderType == "takeout" ? 10 : 20,
					},
					function(res) {
						uni.hideLoading();
						self.reCart(res);
						self.addclock = false;
						self.getCategory(false);
					},
					function() {
						uni.hideLoading();
						self.addclock = false;
					}
				);
			},
			getCartLoading() {
				console.log("getCartLoading");
				let self = this;
				self._postBody(
					"order/cart/lists", {
						shopSupplierId: self.shopSupplierId,
						cartType: 0,
						delivery: self.orderType == 'takeout' ? 10 : 20,
						productId: self.cartProductId || 0,
					},
					function(res) {
						self.cartList = res.data.productList;
						self.isLoading = true;
						self.reCart(res);
						uni.hideLoading();
					},
					(err) => {
						uni.hideLoading();
					}
				);
			},
			getCart() {
				let self = this;

				if (!self.isLogin) {
					return;
				}

				return new Promise((resolve, reject) => {
					uni.showLoading({
						title: "",
					});
					self._postBody(
						"order/cart/lists", {
							shopSupplierId: self.shopSupplierId,
							cartType: 0,
							delivery: self.orderType == 'takeout' ? 10 : 20,
							productId: self.cartProductId || 0,
						},
						function(res) {
							uni.hideLoading();
							self.isLoading = true;
							self.reCart(res);
							self.cartList = res.data.productList;
							resolve(true);
						},
						(err) => {
							uni.hideLoading();
						}
					);
				});
			},
			/* 购物车商品添加 */
			cartAdd(goods) {
				let self = this;
				if (self.addclock) {
					return;
				}
				self.addclock = true;
				let productId = goods.productId;
				let totalNum = 1;
				uni.showLoading({
					title: "",
				});
				self._postBody(
					"order/cart/sub", {
						productId: productId,
						totalNum: totalNum,
						cartId: goods.cartId,
						type: "up",
						cartType: 0,
						dinnerType: self.dinnerType,
						shopSupplierId: self.shopSupplierId,
						delivery: self.orderType == "takeout" ? 10 : 20,
					},
					function(res) {
						self.addclock = false;
						self.getCategory(false);
						self.getCartLoading();
					},
					function() {
						uni.hideLoading();
						self.addclock = false;
					}
				);
			},
			/* 购物车商品减少 */
			cartReduce(goods) {
				let self = this;
				if (self.addclock) {
					return;
				}
				self.addclock = true;
				let productId = goods.productId;
				uni.showLoading({
					title: "",
				});
				self._postBody(
					"order/cart/sub", {
						productId: productId,
						totalNum: 1,
						cartId: goods.cartId,
						type: "down",
						cartType: 0,
						dinnerType: self.dinnerType,
						shopSupplierId: self.shopSupplierId,
						delivery: self.orderType == "takeout" ? 10 : 20,
					},
					function(res) {
						self.addclock = false;
						self.getCategory(false);
						self.getCartLoading();
					},
					function() {
						uni.hideLoading();
						self.addclock = false;
					}
				);
			},
			takout() {
				if (this.orderType == 'takeout') return;
				this.orderType = 'takeout';
				this.dinnerType = 10;
				this.init();
			},
			takein() {
				if (this.orderType == 'takein') return;
				this.orderType = 'takein';
				this.dinnerType = 20;
				this.init();
			},
			handleMenuTap(id) {
				let self = this;
				//点击菜单项事件
				if (!self.sizeCalcState) {
					self.calcSize();
				}
				self.currentCateId = id;
				self.$nextTick(() => {
					self.cateScrollTop = self.productList.find(item => item.categoryId == id).top;
					if (!self.cateScrollTop && self.cateScrollTop != 0) {
						setTimeout(function() {
							self.handleMenuTap(id)
						}, 300);
					}
				});
			},
			handleGoodsScroll({
				detail
			}) {
				let self = this;
				//商品列表滚动事件
				if (!this.sizeCalcState) {
					this.calcSize();
				}
				this.scrollLast = detail.scrollTop;
				// console.log(this.scrollLast)
				const {
					scrollTop
				} = detail;
				let tabs = this.productList.filter(item => (item.top - 5) <= scrollTop).reverse();
				if (tabs.length > 0) {
					this.currentCateId = tabs[0].categoryId;
				}

			},
			calcSize() {
				let h = 0;
				this.productList.forEach(item => {
					let view = uni.createSelectorQuery().select(`#cate-${item.categoryId}`);
					view.fields({
							size: true
						},
						data => {
							item.top = h;
							if (data != null) {
								h += data.height;
							}

							item.bottom = h;
						}
					).exec();
				});
				this.sizeCalcState = true;
			},
			closeGoodDetailModal(num, res) {
				//关闭饮品详情模态框
				this.isDetail = false;
				this.clock = false;
				if (num) {
					this.reCart(res);
					this.getCategory(false);
				}
			},
			async openCartPopup(n) {
				if (!this.cartTotalNum) {
					return
				}
				if (!this.cartPopupVisible) {
					this.cartList = [];
					this.cartProductId = n;
					//打开/关闭购物车列表popup
					await this.getCart();
					this.cartPopupVisible = !this.cartPopupVisible;
				} else {
					this.cartPopupVisible = !this.cartPopupVisible;
				}
			},
			closeCallBack() {
				this.cartProductId = 0;
				this.cartList = [];
				this.cartPopupVisible = false;
			},
			handleCartClear() {
				//清空购物车
				let self = this;
				uni.showModal({
					title: '提示',
					content: '确定清空购物车么',
					success(res) {
						if (res.confirm) {
							self.clearCart();
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			clearCart() {
				let self = this;
				self._postBody(
					"order/cart/delete", {
						shopSupplierId: self.shopSupplierId,
						cartType: 0,
					},
					function(res) {
						self.cartPopupVisible = false;
						self.cartList = [];
						self.init();
					}
				);
			},
			toPay() {
				let self = this;
				if (self.addressId == 0 && self.orderType == "takeout") {
					uni.showModal({
						title: "提示",
						content: "您还没选择收货地址,请先选择收货地址",
						success() {
							self.gotoPage(
								"/pages/user/address/storeaddress?shopSupplierId=" +
								self.shopSupplierId
							);
						},
					});
					return;
				}
				if (self.loadingClock) {
					return;
				}
				self.loadingClock = true;
				uni.showLoading({
					title: "加载中",
					mask: true,
				});
				self._postBody(
					"order/cart/lists", {
						shopSupplierId: self.shopSupplierId,
						cartType: 0,
						delivery: self.orderType == 'takeout' ? 10 : 20,
					},
					function(res) {
						uni.hideLoading();
						self.reCart(res);
						self.cartList = res.data.productList;
						let arrIds = [];

						self.cartList.forEach((item) => {
							arrIds.push(item.cartId);
						});
						if (arrIds.length == 0) {
							uni.showToast({
								title: "请选择商品",
								icon: "none",
							});
							return false;
						}
						let delivery = self.orderType == "takeout" ? 10 : 20;
						uni.navigateTo({
							url: "/pages/order/confirm-order?orderType=cart&cartIds=" +
								arrIds.join(",") +
								"&delivery=" +
								delivery +
								"&shopSupplierId=" +
								self.shopSupplierId +
								"&cartType=0" +
								"&dinnerType=" +
								delivery,
						});
						self.loadingClock = false;
					},
					() => {
						uni.hideLoading();
						self.loadingClock = false;
					}
				);
			},
			/* 新 打开商品详情弹窗 */
			gotoDetail(e) {
				let self = this;

				let delivery = this.orderType == "takeout" ? 10 : 20;
				self.productModel = {
					productId: e.productId || 0,
					delivery: delivery,
					bagType: self.bagType || 0,
					dinnerType: self.dinnerType || 0,
					cartType: self.cartType || 0,
					tableId: 0,
					shopSupplierId: self.shopSupplierId || 0,
				};
				console.log(self.productModel, '123');
				self.isDetail = true;

			},
			/*获取定位方式*/
			getcityData() {
				let self = this;
				// 第一次，如果是公众号，则初始化微信sdk配置
				// #ifdef H5
				if (self.isWeixin()) {
					uni.showLoading({
						title: '加载中',
						mask: true
					});
					self._post(
						'index/index', {
							url: window.location.href
						},
						function(res) {
							let sign = res.data.signPackage;
							uni.hideLoading();
							self.getWxLocation(sign);
						}
					);
				} else {
					self.getLocation();
				}
				// #endif
				// #ifndef H5
				self.getLocation();
				// #endif
			},
			/*获取用户坐标*/
			getLocation(callback) {
				let self = this;
				uni.getLocation({
					type: 'wgs84',
					success(res) {
						console.log('getLocation')
						console.log(res)
						self.longitude = res.longitude;
						self.latitude = res.latitude;
						self.getCategory(true);
					},
					fail(err) {
						self.longitude = 0;
						self.latitude = 0;
						uni.showToast({
							title: '获取定位失败，请点击右下角按钮打开定位权限',
							duration: 2000,
							icon: 'none'
						});
						self.getCategory(true);
					}
				});
			},
			/* 公众号获取坐标 */
			getWxLocation(signPackage, callback) {
				// #ifdef H5
				let self = this;
				jweixin.config(JSON.parse(signPackage));
				jweixin.ready(function(res) {
					jweixin.getLocation({
						type: 'wgs84',
						success: function(res) {
							self.longitude = res.longitude;
							self.latitude = res.latitude;
							self.getCategory(true);
						},
						fail(err) {
							self.longitude = 0;
							self.latitude = 0;
							self.getCategory(true);
						}
					});
				});
				jweixin.error(function(res) {
					console.log(res);
				});
				// #endif
			},
		}
	};
</script>

<style lang="scss">
	@import './menu.scss';
</style>