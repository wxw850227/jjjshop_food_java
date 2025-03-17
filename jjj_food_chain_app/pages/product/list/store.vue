<template>
	<view class="container" :data-theme="theme()" :class="theme() || ''">
		<view class="main">
			<view class="nav">
				<view class="header" :class="{tableHead:tableId != 0 && tableDetail}">
					<view class="left">
						<view class="store-name">
							<text class="fb">{{ supplier.name }}</text>
						</view>
					</view>
					<view class="dinner-right" v-if="!tableId">
						<template v-for="(item, index) in storeSet" :key="item">
							<view class="dinner_type" v-if="item == 30" :class="{active:orderType == 'takeout'}"
								@tap="takout">
								<text>打包</text>
							</view>
						</template>
						<template v-for="(item, index) in storeSet" :key="item">
							<view class="dinner_type" v-if="item == 40" :class="{active:orderType == 'takein'}"
								@tap="takein">
								<text>店内</text>
							</view>
						</template>
					</view>
				</view>
			</view>
			<view class="content">
				<scroll-view class="menus" :style="'height:' + scrollviewHigh + 'px;'"
					:scroll-into-view="menuScrollIntoView" :scroll-with-animation="true" :scroll-animation-duration="1"
					scroll-y>
					<view class="category-wrapper">
						<template v-for="(item, index) in productList" :key="index">
							<view class="menu d-s-c" :id="`menu-${item.categoryId}`"
								:class="{ current: item.categoryId === currentCateId }" v-if="item.products.length != 0"
								@tap="handleMenuTap(item.categoryId)">
								<image v-if="item.productImage" :src="item.productImage" mode="aspectFill"
									class="f-s-0 menu-imgs"></image>
								<text>{{ item.name }}</text>
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
													<view class="name text-ellipsis">
														{{ good.productName }}
													</view>
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
														<image @tap.stop="reduceFunc(good)" v-if="good.cartNum "
															class="add-image"
															:src="'/static/icon/cart/reduce-'+theme()+'.png'" mode="">
														</image>
														<view class="number" v-if="good.cartNum ">
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
			<!-- content end -->
			<!-- 购物车栏 begin -->
			<view class="cart-box" @click.stop="openCartPopup(0)">
				<view class="mark">
					<view class="icon iconfont icon-gouwudai cart-view"></view>
					<view class="tag" v-if="cartTotalNum">{{ cartTotalNum }}</view>
				</view>
				<view class="price" v-if="cartTotalNum">
					<view>
						<text class="f22 ">￥</text>
						<text class="f36">{{ totalPrice }}</text>

					</view>
				</view>
				<view v-else class="flex-1 f32 white">未选购商品</view>
				<template v-if="tableId != 0">
					<button class="pay-btn" @click.stop="toPay"><text>下单</text></button>
				</template>
				<template v-else>
					<button v-if="addorderId == 0" class="pay-btn" @click.stop="toPay"><text>去结算</text></button>
				</template>

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
		<prospec v-if="isDetail" :productModel="productModel" @close="closeGoodDetailModal"></prospec>
		<view class="loading" v-if="loading">
			<image src="/static/images/loading.gif"></image>
		</view>
	</view>
</template>

<script>
	import utils from '@/common/utils.js';
	import prospec from './popup/spec.vue';
	import popupLayer from '@/components/popup-layer/popup-layer';
	export default {
		components: {
			popupLayer,
			prospec,
		},
		data() {
			return {
				loadingClock: false,
				isSearch: false,
				isLogin: false,
				isDetail: false,
				isLoading: true,
				goods: [], //所有商品
				supplier: {
					name: '',
					business_status: 0
				},
				loading: true,
				currentCateId: 0, //默认分类
				cateScrollTop: 0,
				menuScrollIntoView: '',
				cart: [], //购物车
				// good: {}, //当前饮品
				category: {}, //当前饮品所在分类
				cartPopupVisible: false,
				sizeCalcState: false,
				listData: [],
				productList: [],
				productModel: {},
				clock: false,
				cartTotalNum: 0,
				tableId: 0,
				totalPrice: 0,
				totalBagPrice: 0,
				cartList: [],
				orderType: 'takein',
				phoneHeight: 0,
				/*可滚动视图区域高度*/
				scrollviewHigh: 0,
				addclock: false,
				longitude: 0,
				latitude: 0,
				bagType: 1,
				shopSupplierId: 0,
				/* 10配送20自提30店内 */
				dinnerType: 30,
				cartType: 1,

				orderId: 0,
				addorderId: 0,
				discountInfo: [],
				reduce: {},
				reduceDiffValue: 0,
				linePrice: 0,
				isFirst: true,
				storeSet: [],
				num: 1,
				tableDetail: null,
				options: {},
				settleType: 0,
				status: '',
				mealNum: '',
				isShopDetail: false,
				supplierDetail: null,
			};
		},
		onLoad(e) {
			let self = this;
			let scene = utils.getSceneData(e);
			self.shopSupplierId = e.shopSupplierId ? e.shopSupplierId : scene.s_id;
			self.tableId = e.tableId ? e.tableId : scene.tableId;
			self.num = e.num || 0;
			self.mealNum = e.mealNum || 0;
			self.status = e.status;
			self.addorderId = e.orderId || 0;
			uni.setNavigationBarTitle({
				title: self.tableId == 0 ? "快餐模式" : "堂食点餐",
			});
		},
		onShow() {
			let self = this;
			self.isDetail = false;
			self.getUserInfo();
		},
		methods: {
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
					self.init();
				});
			},
			init() {
				//页面初始化
				this.addclock = false;
				this.category = {};
				this.clock = false;
				this.loading = true;
				this.isLoading = true;
				this.productList = [];
				this.sizeCalcState = false;
				this.getCategory(true);
			},
			goBack() {
				if (this.tableId > 0 && this.status == 1) {
					return
				}
				// #ifndef H5
				uni.navigateBack({
					delta: 1
				});
				// #endif
				// #ifdef H5
				history.go(-1);
				// #endif
			},
			scrollInit() {
				let self = this;
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
			takout() {

				if (this.orderType == 'takeout') return;
				this.orderType = 'takeout';
				console.log(this.orderType, '123');
				this.dinnerType = 10;
				this.init();
			},
			takein() {
				if (this.orderType == 'takein') return;
				this.orderType = 'takein';
				this.dinnerType = 20;
				this.init();
			},
			/* 获取商品类型 */
			getCategory(flag) {
				let self = this;
				this.sizeCalcState = false;
				uni.showLoading({
					title: "加载中",
				});
				self._postBody(
					"product/category/index", {
						/* 0外卖分类，1堂食分类 */
						type: 1,
						shopSupplierId: self.shopSupplierId,
						longitude: 0,
						latitude: 0,
						tableId: self.tableId,
						/* 30 打包 40店内 */
						delivery: self.orderType == "takeout" ? 30 : 40,
						orderType: !self.tableId ? 1 : 2,
						cartType: self.cartType || 1,
					},
					function(res) {
						self.supplier = res.data.supplier;

						self.productList = res.data.list;
						if (!self.currentCateId) {
							self.currentCateId = self.productList[0].categoryId;
						}
						self.storeSet = res.data.supplier.storeSetList;

						if (self.tableId) {
							self.orderType = "takein";
						} else if (self.isFirst && self.orderType == "") {
							if (self.storeSet.indexOf(30) != -1) {
								self.orderType = "takeout";
							} else {
								self.orderType = "takein";
							}
							self.isFirst = false;
						}

						self.shopSupplierId = res.data.supplier.shopSupplierId;
						self.bagType = res.data.supplier.storebagType;
						if (self.orderType == "takein") {
							self.bagType = 1;
						}
						self.loading = false;
						self.isLoading = false;
						self.$nextTick(function() {
							self.scrollInit();
						});
						if (self.isLogin && flag) {
							self.getCart();
						}
						uni.hideLoading();
					},
					function() {
						self.gotoPage("/pages/index/index");
					}
				);
			},
			reCart(res) {
				let self = this;

				self.cartTotalNum = res.data.cartInfo.cartTotalNum;
				self.totalPrice = res.data.cartInfo.totalPayPrice;
				self.linePrice = res.data.cartInfo.totalLineMoney;
				self.totalBagPrice = res.data.cartInfo.totalBagPrice;
				self.reduce = res.data.cartInfo.reduce;
				self.reduceDiffValue = res.data.cartInfo.reduceDiffValue;
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
					price: goods.skuList[0].productPrice, //商品价格
					bagPrice: goods.skuList[0].bagPrice,
					shopSupplierId: self.shopSupplierId,
					tableId: self.tableId,
					cartType: 1,
					dinnerType: self.dinnerType,
					productPrice: goods.linePrice, //划线价格
					delivery: self.orderType == "takeout" ? 30 : 40,
				};
				self.addclock = true;

				let url = "order/cart/add";
				self._postBody(
					url,
					params,
					function(res) {
						self.reCart(res);
						uni.hideLoading();
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
				let url = "order/cart/productSub";
				self._postBody(
					url, {
						productId: productId,
						type: "down",
						cartType: 1,
						shopSupplierId: self.shopSupplierId,
						tableId: self.tableId,
						dinnerType: self.dinnerType,
						delivery: self.orderType == "takeout" ? 30 : 40,
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
				let self = this;
				let url = "order/cart/lists";
				let params = {
					shopSupplierId: self.shopSupplierId,
					cartType: 1,
					delivery: self.orderType == "takeout" ? 30 : 40,
					productId: self.cartProductId,
					tableId: self.tableId
				};
				let callBack = function(res) {
					uni.hideLoading();
					self.isLoading = true;
					self.reCart(res);
					self.cartList = res.data.productList;
				};
				self._postBody(
					url,
					params,
					function(res) {
						callBack(res);
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
					let url = "order/cart/lists";
					let params = {
						shopSupplierId: self.shopSupplierId,
						cartType: 1,
						delivery: self.orderType == "takeout" ? 30 : 40,
						productId: self.cartProductId,
						tableId: self.tableId
					};
					let callBack = function(res) {
						uni.hideLoading();
						self.isLoading = true;
						self.reCart(res);
						self.cartList = res.data.productList;
					};
					uni.showLoading({
						title: "",
					});
					self._postBody(
						url,
						params,
						function(res) {
							callBack(res);
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
				let productNum = 1;
				let url = "order/cart/sub";
				self._postBody(
					url, {
						productId: productId,
						productNum: productNum,
						cartId: goods.cartId,
						type: "up",
						cartType: 1,
						dinnerType: self.dinnerType,
						shopSupplierId: self.shopSupplierId,
						delivery: self.orderType == "takeout" ? 30 : 40,
						tableId: self.tableId,
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
				let url = "order/cart/sub";
				self._postBody(
					url, {
						productId: productId,
						productNum: 1,
						cartId: goods.cartId,
						type: "down",
						cartType: 1,
						dinnerType: self.dinnerType,
						shopSupplierId: self.shopSupplierId,
						tableId: self.tableId,
						delivery: self.orderType == "takeout" ? 30 : 40,
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
			//清空购物车
			handleCartClear() {
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
			/*  清空 */
			clearCart() {
				let self = this;
				let url = "order/cart/delete";
				let params = {
					shopSupplierId: self.shopSupplierId,
					cartType: 1,
					tableId: self.tableId
				};
				self._postBody(url, params, function(res) {
					self.cartPopupVisible = false;
					self.cartList = [];
					self.init();
				});
			},
			/* 提交 */
			toPay() {
				let self = this;
				self.fastToPay();
			},
			/* 快餐结算 */
			fastToPay() {
				let self = this;
				uni.showLoading({
					title: "加载中",
					mask: true,
				});
				let delivery = self.orderType == "takeout" ? 30 : 40;
				self._postBody(
					"order/cart/lists", {
						shopSupplierId: self.shopSupplierId,
						cartType: 1,
						delivery: delivery,
						tableId: self.tableId
					},
					function(res) {
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
						uni.hideLoading();
						uni.navigateTo({
							url: "/pages/order/confirm-order?orderType=cart&cartIds=" +
								arrIds.join(",") +
								"&delivery=" +
								delivery +
								"&shopSupplierId=" +
								self.shopSupplierId +
								"&cartType=1" +
								"&dinnerType=30&tableId=" +
								self.tableId,
						});
					}, err => {
						self.loadingClock = false;
					}
				);
			},
			/* 加餐 */
			addpay() {
				let self = this;
				uni.showLoading({
					title: '加载中',
					mask: true
				});
				self.addpayFunc();
			},
			/* 快餐模式加餐 */
			addpayFunc() {
				let self = this;
				self._postBody(
					"order/cart/lists", {
						shopSupplierId: self.shopSupplierId,
						cartType: 1,
						delivery: self.orderType == "takeout" ? 30 : 40,
						tableId: self.tableId
					},
					function(res) {
						self.cartTotalNum = res.data.cartInfo.cartTotalNum;
						self.totalPrice = res.data.cartInfo.totalPrice;
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
						uni.hideLoading();
						let url =
							"/pages/order/addorder?orderType=cart&cartIds=" +
							arrIds.join(",") +
							"&delivery=40&shopSupplierId=" +
							self.shopSupplierId +
							"&cartType=1" +
							"&dinnerType=30&tableId=" +
							self.tableId +
							"&orderId=" +
							self.addorderId;
						self.gotoPage(url);
					}
				);
			},
			/* 新 打开商品详情弹窗 */
			gotoDetail(e) {
				let self = this;
				let delivery = this.orderType == "takeout" ? 30 : 40;
				self.productModel = {
					productId: e.productId || 0,
					delivery: delivery,
					bagType: self.bagType || 0,
					dinnerType: self.dinnerType || 0,
					cartType: self.cartType || 0,
					tableId: self.tableId,
					shopSupplierId: self.shopSupplierId || 0,
				};
				self.isDetail = true;
			},
			//点击菜单项事件
			handleMenuTap(id) {
				let self = this;
				if (!self.sizeCalcState) {
					self.calcSize();
				}
				self.currentCateId = id;
				self.$nextTick(() => {
					self.cateScrollTop = self.productList.find(
						(item) => item.categoryId == id
					).top;
					if (!self.cateScrollTop && self.cateScrollTop != 0) {
						setTimeout(function() {
							self.handleMenuTap(id);
						}, 200);
					}
				});
			},
			//商品列表滚动事件
			handleGoodsScroll({
				detail
			}) {
				if (!this.sizeCalcState) {
					this.calcSize();
				}
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
				if (!this.cartPopupVisible) {
					this.cartList = [];
					this.cartProductId = n;
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
		}
	};
</script>

<style lang="scss">
	@import './menu.scss';

	.top-status {
		padding: 0 26rpx;
		height: 104rpx;
		line-height: 104rpx;
		font-size: 28rpx;
		border-bottom: 1px solid;
		@include border_color('border_color');
		@include background_color('opacify_background_0');
	}
</style>