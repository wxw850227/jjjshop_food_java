<template>
	<view :data-theme="theme()" :class="theme() || ''">
		<scroll-view scroll-y="true" class="scroll-Y" :style="'height:' + scrollviewHigh + 'px;'" lower-threshold="50"
			@scrolltolower="scrolltolowerFunc">
			<view class="address-list">
				<view class="address pt26 p-0-26 bg-white" v-for="(item, index) in storeList" :key="index"
					@click="onSelectedStore(item.shopSupplierId)"
					:class="isShopid(item.shopSupplierId) ? 'active' : ''">
					<view class="info flex-1">
						<view class="user f34 d-b-c">
							<view class="d-s-c">
								<view :class="
                    item.shopSupplierId != selectedId
                      ? 'select-id'
                      : 'select-id icon iconfont icon-tijiaochenggong'
                  ">
								</view>
								<view class="f32 fb gray3">{{ item.name }}</view>
							</view>
							<view class="distance"><text>距离:{{ item.distance }}</text></view>
						</view>
						<view class="d-b-c">
							<view class="text-ellipsis f22 gray6">
								{{ item.address }}
							</view>
							<view class="text-ellipsis f22 gray6" v-if="item.deliveryTimeList.length > 0">
								<text class="icon iconfont icon-time"
									style="color: #333333; font-size: 22rpx; margin-right: 15rpx"></text>
								<text>{{
                  item.deliveryTimeList[0] + "-" + item.deliveryTimeList[1]
                }}</text>
							</view>
						</view>
						<view class="mt16 mb30 d-b-c">
							<view></view>
							<view>
								<text class="shop_status theme-borderbtn" v-if="item.status == 0">营业中</text>
								<text class="shop_status theme-borderbtn" v-else>暂停营业</text>
							</view>
						</view>
					</view>
					<view class="info-right d-b-c">
						<view class="d-s-c gray3">
							<view class="d-s-c" @click="callPhone(item.linkPhone)">
								<view class="icon iconfont icon-002dianhua"></view><text
									class="f26 fb ml18">咨询服务方</text>
							</view>
							<view class="ml25 d-s-c" @click="openmap(item.latitude, item.longitude)">
								<view class="icon iconfont icon-fasong"></view><text class="f26 fb ml18">导航去这里</text>
							</view>
						</view>
						<view class="f26 gray3 theme-btn" @click.stop="onSelectedStore(item.shopSupplierId, true)">去下单
						</view>
					</view>
				</view>
				<!-- 没有记录 -->
				<view class="d-c-c p30" v-if="storeList.length == 0 && !loading">
					<text class="iconfont icon-wushuju"></text>
					<text class="cont">亲，暂无相关记录哦</text>
				</view>
				<!-- <uni-load-more v-else :loadingType="loadingType"></uni-load-more> -->
			</view>
		</scroll-view>
	</view>
</template>

<script>
	// import uniLoadMore from "@/components/uni-load-more.vue";
	//#ifdef H5
	import jweixin from 'weixin-js-sdk';
	//#endif
	export default {
		components: {
			// uniLoadMore,
		},
		data() {
			return {
				/*数据*/
				distance: "",
				loading: true, // 是否正在加载中
				storeList: [], // 门店列表,
				longitude: "",
				latitude: "",
				selectedId: {
					region: {
						city: "",
						storeId: -1,
						distanceUnit: "",
						log: {
							filePath: "",
						},
					},
					storeName: "",
				},
				/* 搜索关键字 */
				storeName: "",
				list_rows: 10,
				lastPage: 0,
				page: 1,
				/*没有更多*/
				no_more: false,
				scrollviewHigh: 0,
				longitude: "",
				latitude: "",
				dinnerType: "",
				signPackages: {}
			};
		},
		computed: {
			/*加载中状态*/
			// loadingType() {
			//   if (this.loading) {
			//     return 1;
			//   } else {
			//     if (this.storeList.length != 0 && this.no_more) {
			//       return 2;
			//     } else {
			//       return 0;
			//     }
			//   }
			// },
		},
		onLoad(options) {
			this.dinnerType = options.dinnerType;
			this.getsign();
		},
		onShow() {
			this.restoreData();
			this.getcityData();
		},
		mounted() {
			this.selectedId = uni.getStorageSync("selectedId") ?
				uni.getStorageSync("selectedId") :
				0;
			this.init();
		},
		methods: {
			//获取签名
			getsign() {
				let self = this;
				self._postBody('index/getSignPackage', {
					url: window.location.href,
					paySource: self.getPlatform(),
				}, function(res) {
					self.signPackages = res.data.signPackage;
					self.mpSign(res.data.signPackage);
				})
			},
			/*获取定位方式*/
			getcityData() {
				let self = this;
				// 第一次，如果是公众号，则初始化微信sdk配置
				// #ifdef H5
				if (self.isWeixin()) {
					let sign = uni.getStorageSync("sign");
					if (!sign) {
						uni.showLoading({
							title: "加载中",
						});
						self._get(
							"index/index", {
								url: window.location.href,
							},
							function(res) {
								uni.setStorageSync("sign", self.signPackages);
								sign = self.signPackages;
								uni.hideLoading();
								self.getWxLocation(sign);
							}
						);
					} else {
						self.getWxLocation(sign);
					}
				} else {
					self.getLocation();
				}
				// #endif
				// #ifndef H5
				self.getLocation();
				// #endif
			},
			/*授权启用定位权限*/
			onAuthorize() {
				let self = this;
				uni.openSetting({
					success(res) {
						if (res.authSetting["scope.userLocation"]) {
							self.isAuthor = true;
							setTimeout(() => {
								// 获取用户坐标
								self.getLocation((res) => {});
							}, 1000);
						}
					},
				});
			},
			/*获取用户坐标*/
			getLocation(callback) {
				let self = this;
				uni.getLocation({
					type: "wgs84",
					success(res) {
						self.longitude = res.longitude;
						self.latitude = res.latitude;
						self.getData();
					},
					fail(err) {
						self.getData();
						uni.showToast({
							title: "获取定位失败，请点击右下角按钮打开定位权限",
							duration: 2000,
							icon: "none",
						});
					},
				});
			},
			isShopid(id) {
				let sId = uni.getStorageSync("selectedId");
				return sId == id;
			},
			/* 公众号获取坐标 */
			getWxLocation(signPackage, callback) {
				// #ifdef H5
				let self = this;
				// var jweixin = require("jweixin-module");
				jweixin.config(signPackage);
				jweixin.ready(function(res) {
					jweixin.getLocation({
						type: "wgs84",
						success: function(res) {
							self.longitude = res.longitude;
							self.latitude = res.latitude;
							self.getData();
						},
						fail(err) {
							self.getData();
						},
					});
				});
				jweixin.error(function(res) {
					console.log(res);
				});
				// #endif
			},
			/*初始化*/
			init() {
				let _this = this;
				uni.getSystemInfo({
					success(res) {
						_this.scrollviewHigh = res.windowHeight;
					},
				});
			},
			/*还原初始化*/
			restoreData() {
				this.storeList = [];
				this.search = "";
				this.no_more = false;
				this.page = 1;
			},
			/*可滚动视图区域到底触发*/
			// scrolltolowerFunc() {
			//   let self = this;
			//   self.bottomRefresh = true;
			//   self.page++;
			//   self.loading = true;
			//   if (self.page > self.lastPage) {
			//     self.loading = false;
			//     self.no_more = true;
			//     return;
			//   }
			//   self.getData();
			// },
			/*获取数据*/
			getData() {
				let self = this;
				self.loading = true;
				uni.showLoading({
					title: "加载中",
				});
				self._postBody(
					"supplier/index/deliveryList", {
						type: self.dinnerType,
						longitude: self.longitude || 0,
						latitude: self.latitude || 0,
					},
					function(res) {
						uni.hideLoading();
						self.loading = false;
						self.storeList = self.storeList.concat(res.data.list);
						self.lastPage = res.data.list.lastPage;
						if (res.data.list.lastPage <= 1) {
							self.no_more = true;
						}
					}
				);
			},
			chooseLocation() {
				let self = this;
				uni.chooseLocation({
					success: function(res) {
						console.log("位置名称：" + res.name);
						console.log("详细地址：" + res.address);
						console.log("纬度：" + res.latitude);
						console.log("经度：" + res.longitude);
						self.longitude = res.longitude;
						self.latitude = res.latitude;
						self.getrecord();
						self.getData();
					},
				});
			},
			search() {
				let self = this;
				self.loading = true;
				self._get(
					"store/store/lists", {
						storeName: self.storeName,
						longitude: self.longitude,
						latitude: self.latitude,
					},
					function(res) {
						self.loading = false;
						self.storeList = res.data.list;
					}
				);
			},
			/**
			 * 选择门店
			 */
			onSelectedStore(e, flag) {
				let self = this;
				self.selectedId = e;
				uni.setStorageSync("selectedId", self.selectedId);
				self.$fire.fire("selectShop", true);
				if (flag) {
					// #ifndef H5
					uni.navigateBack();
					// #endif
					// #ifdef H5
					history.go(-1);
					// #endif
				}
			},
			getrecord() {
				let self = this;
				let storeId = self.selectedId.storeId;
				self._post(
					"user/storelog/record", {
						storeId: storeId,
						longitude: self.longitude,
						latitude: self.latitude,
					},
					function(res) {
						self.distance = res.data.detail.distanceUnit;
					}
				);
			},
			addSotre(e) {
				let self = this;
				let storeId = e.storeId;
				self._post(
					"user/storelog/add", {
						storeId: storeId,
					},
					function(res) {}
				);
			},
		},
	};
</script>

<style lang="scss">
	.info {
		margin-left: 20rpx;
	}

	.address-list {
		padding: 30rpx 0;
	}

	.address-list .address {
		margin-bottom: 22rpx;
	}

	.address-list .address .info-right {
		height: 100rpx;
		border-top: 2rpx solid #eeeeee;
		flex-shrink: 0;
	}

	.foot-btns {
		padding: 0;
	}

	.foot-btns .btn-red {
		width: 100%;
		height: 90rpx;
		line-height: 90rpx;
		border-radius: 0;
	}

	.address_img image {
		width: 154rpx;
		height: 154rpx;
		border-radius: 15rpx;
	}

	.address_tit {
		height: 75rpx;
		line-height: 75rpx;
		color: #959597;
		font-size: 29rpx;
		padding-left: 28rpx;
	}

	.address_btn {
		width: 130rpx;
		height: 60rpx;
		line-height: 60rpx;
		border-radius: 30rpx;
		text-align: center;
		@include background_color("background_color");
		margin-top: 50rpx;
		color: #ffffff;
	}

	.user {
		font-size: 30rpx;
		font-family: PingFang SC;
		font-weight: 400;
		color: #333333;
		opacity: 1;
		margin: 10rpx 0;
	}

	.distance {
		font-size: 22rpx;
		color: #666666;
	}

	.address_search {
		background-color: #f6f6f6;
		width: 460rpx;
		height: 65rpx;
		border-radius: 33rpx;
		margin-left: 29rpx;
		margin-right: 30rpx;
	}

	.address_city {
		display: flex;
		align-items: center;
		margin-left: 33rpx;
	}

	.icon-sousuo {
		margin-left: 22rpx;
		margin-right: 9rpx;
	}

	.icon-jiantoushang {
		font-size: 17rpx;
		display: block;
		transform: rotate(180deg);
		color: #000000;
		margin-left: 22rpx;
	}

	.ml25 {
		margin-left: 25rpx;
	}

	.shop_status {
		padding: 4rpx 8rpx;
		border-radius: 5rpx;
	}

	.shop_status text {
		// color: $dominant-color;
		// border: 1rpx solid $dominant-color;
		padding: 3rpx 8rpx;
		border-radius: 4rpx;
		margin-left: 9rpx;
	}

	.select-id {
		width: 38rpx;
		height: 38rpx;
		border-radius: 50%;
		border: 1rpx solid #d9d9d9;
		box-sizing: border-box;
		margin-right: 24rpx;
	}

	.select-id.icon-tijiaochenggong {
		color: #ffffff;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 22rpx;
		@include border_color("border_color");
		@include background_color("background_color");
	}

	.info-right .theme-btn {
		width: 142rpx;
		border-radius: 30rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 60rpx;
		font-size: 28rpx;
		font-weight: 800;
		color: #ffffff;
	}

	.info-right .icon-fasong {
		width: 51rpx;
		height: 51rpx;
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
		background: #00cec9;
		color: #ffffff;
		box-shadow: 0px 5rpx 5rpx 0px rgba(0, 206, 201, 0.09);
	}

	.info-right .icon-002dianhua {
		color: #ffffff;
		width: 51rpx;
		height: 51rpx;
		background: #ffcc00;
		display: flex;
		justify-content: center;
		align-items: center;
		box-shadow: 0px 5rpx 5rpx 0px rgba(255, 204, 0, 0.09);
		border-radius: 50%;
	}
</style>