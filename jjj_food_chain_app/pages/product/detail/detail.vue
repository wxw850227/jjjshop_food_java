<template>
	<view class="good-detail-modal" :data-theme="theme()" :class="theme() || ''">
		<!-- #ifdef MP-WEIXIN || APP-PLUS -->
		<view class="tc header" :style="
        topBarHeight() == 0
          ? ''
          : 'height:' + topBarHeight() + 'px;padding-top:' + topBarTop() + 'px'
      ">
			<view class="left-icon pr" :style="topBarHeight() == 0 ? '' : 'height:' + topBarHeight() + 'px;'">
				<image class="reg180" @click="goback" src="/static/icon/back-jianatou.png" mode=""
					style="width: 48rpx; height: 48rpx"></image>
			</view>
		</view>
		<!-- #endif -->
		<!-- 商品详情模态框 begin -->
		<view class="cover">
			<image v-if="detail.image" :src="detail.image[0].filePath" class="image" mode="aspectFill"></image>
		</view>
		<view class="bg-white">
			<view class="good_basic">
				<view class="name text-ellipsis">{{ detail.productName || "" }}</view>
				<view class="f22 gray9">已售{{ detail.productSales || "" }}</view>
				<view class="gray6 sell-point" style="line-height: 1.5">{{
          detail.sellingPoint || ""
        }}</view>
			</view>
		</view>
		<view class="detail bg-white">
			<view class="wrapper">
				<view class="properties">
					<view class="property" v-if="detail.specType == 20">
						<view class="title"><text class="name">规格</text></view>
						<view class="values">
							<view class="value" @click="selecedtSpec(value, key)" v-for="(value, key) in detail.skuList"
								:key="key" :class="{ default: value.checked }">
								{{ value.specName }}
							</view>
						</view>
					</view>
					<view class="property" v-if="detail.productAttr.length > 0"
						v-for="(item, index) in detail.productAttr" :key="index">
						<view class="title">
							<text class="name">{{ item.attributeName || "" }}</text>
						</view>
						<view class="values">
							<view class="value" @click="selectAttr(value, key, index)" v-if="value != ''"
								v-for="(value, key) in item.attributeValue" :key="key"
								:class="{ default: form.showSku.attr[index] == key }">
								{{ value }}
							</view>
						</view>
					</view>
					<view class="property" v-if="detail.productFeed.length > 0">
						<view class="title"><text class="name">加料</text></view>
						<view class="values">
							<view class="value" @click="selectFeed(item, index)"
								v-for="(item, index) in detail.productFeed" :key="index"
								:class="{ default: form.showSku.feed[index] != null }">
								+{{ item.feedName }}
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="spec-bottom">
			<view class="action">
				<view class="d-c d-a-s">
					<view class="left mb10">
						<view class="price">
							<text class="f22 fb">￥</text>
							<text class="f36 fb mr12">{{ price }}</text>
							<text class="f24 gray9 text-l-t mr10">￥{{ lineprice }}</text>
							<text v-if="bagType != 1 && delivery != 20 && delivery != 40">
								<text class="f22 gray9">打包费￥{{ bagPrice() }}</text>
							</text>
						</view>
					</view>
					<view class="f22 gray9">{{ describe() }}</view>
				</view>
				<view class="btn-group">
					<button type="default" plain class="btn theme-borderbtn" size="min" hover-class="none"
						@click="sub()">
						<view class="icon icon-jian iconfont iconsami-select"></view>
					</button>
					<view class="number">{{ form.showSku.sum }}</view>
					<button type="primary" class="btn theme-btn" size="min" hover-class="none" @click="add()">
						<view class="icon icon-jia iconfont iconsami-select"></view>
					</button>
				</view>
			</view>
			<view>
				<view class="add-to-cart-btn" @tap="confirmFunc">
					<view>加入购物车</view>
				</view>
			</view>
		</view>
		<!--详情内容-->
		<view class="product-content">
			<view class="border-b-e">
				<view class="group-hd d-s-c"><text class="min-name pr f30 fb">详情</text></view>
			</view>
			<view class="content-box" v-html="detail.content"></view>
		</view>
		<!-- 商品详情模态框 end -->
	</view>
</template>

<script>
	import utils from "@/common/utils.js";
	export default {
		components: {},
		data() {
			return {
				detail: {
					productAttr: [],
					productFeed: [],
					skuList: [],
				},
				/*是否可见*/
				Visible: false,
				/*表单对象*/
				form: {
					attr: [],
					productSkuId: [],
					feed: [],
					detail: {},
					showSku: {
						skuImage: "",
						bagPrice: "",
					},
					shopSupplierId: 0,
				},
				/*当前商品总库存*/
				stock: 0,
				/*选择提示*/
				selectSpec: "",
				/*是否打开过多规格选择框*/
				isOpenSpec: false,
				type: "",
				productPrice: "",
				feedPrice: 0,
				spaceName: "",
				attrName: [],
				feedName: [],
				productLineprice: "",
				delivery: "",
				bagType: 1,
				dinnerType: 20,
				cartType: 0,
				tableId: 0,
				clock: false,
				discountPrice: 0,
				productFeedList: [],
			};
		},
		computed: {
			price: function() {
				if (this.discountPrice) {
					return (
						(this.discountPrice * 1 + this.productPrice * 1) *
						this.form.showSku.sum
					).toFixed(2);
				} else {
					return (
						(this.feedPrice * 1 + this.productPrice * 1) *
						this.form.showSku.sum
					).toFixed(2);
				}
			},
			lineprice: function() {
				return (
					(this.feedPrice * 1 + this.productLineprice * 1) *
					this.form.showSku.sum
				).toFixed(2);
			},
			/*判断增加数量*/
			isadd: function() {
				return (
					this.form.showSku.sum >= this.stock ||
					this.form.showSku.sum >= this.form.detail.limitNum
				);
			},

			/*判断减少数量*/
			issub: function() {
				return this.form.showSku.sum <= 1;
			},
		},
		watch: {},
		onLoad(e) {
			this.productId = e.productId;
			this.delivery = e.delivery;
			this.bagType = e.bagType;
			this.dinnerType = e.dinnerType;
			this.cartType = e.cartType;
			this.tableId = e.tableId || 0;
			this.shopSupplierId = e.shopSupplierId || 0;
		},
		onShow() {
			this.getData();
		},
		methods: {
			bagPrice: function() {
				let price = this.form.showSku.bagPrice * this.form.showSku.sum;
				if (price) {
					return price.toFixed(2);
				} else {
					return 0.0;
				}
			},
			goback() {
				uni.navigateBack();
			},
			getData() {
				let self = this;
				self._get(
					"product/product/detail", {
						productId: self.productId,
						tableId: self.tableId || 0,
						shopSupplierId: self.shopSupplierId || 0,
					},
					(res) => {
						if (!res.data.detail) {
							self.showError("商品已下架", () => {
								uni.navigateBack();
							});
							return;
						}
						if (res.data.detail.productFeed) {
							this.form.detail.productFeed = JSON.parse(
								res.data.detail.productFeed
							);
						}
						/*详情内容格式化*/
						res.data.detail.content = utils.format_content(
							res.data.detail.content
						);
						self.detail = res.data.detail;
						this.showGoodDetailModal();
					}
				);
			},
			showGoodDetailModal() {
				let self = this;
				this.detail.skuList.forEach((item, index) => {
					item.checked = false;
				});
				let obj = {
					specData: this.detail.skuList,
					detail: this.detail,
					shopSupplierId: this.detail.shopSupplierId,
					productSpecArr: this.specData != null ?
						new Array(this.specData.spec_attr.length) :
						[],
					showSku: {
						skuImage: "",
						seckillPrice: 0,
						attr: [],
						productSkuId: [],
						feed: [],
						linePrice: 0,
						seckillStock: 0,
						seckillProductSkuId: 0,
						sum: 1,
					},
				};
				self.form = obj;
				self.spaceName = "";
				self.attrName = [];
				self.feedName = [];
				self.isOpenSpec = true;
				self.initShowSku();
				if (self.form.detail.skuList[0].checked == false) {
					self.selecedtSpec(self.form.detail.skuList[0], 0);
				}
				if (self.form.detail.productAttr == "") {
					return;
				}
				self.form.detail.productAttr = JSON.parse(self.form.detail.productAttr);
				self.form.detail.productAttr.forEach((item, index) => {
					if (!self.form.showSku.attr[index]) {
						self.selectAttr(item.attributeValue[0], 0, index);
					}
				});
			},
			describe: function() {
				let spaceName = this.spaceName;
				if (spaceName != "") {
					spaceName += ";";
				}
				let attrName = this.attrName.join(";");
				if (attrName != "") {
					attrName += ";";
				}
				let feedName = this.feedName.join(",");
				if (feedName != "") {
					feedName += ";";
				}

				return spaceName + attrName + feedName;
			},
			/*初始化*/
			initShowSku() {
				this.productFeedList = this.form.detail.productFeed ?
					JSON.parse(this.form.detail.productFeed) :
					"";
				this.form.showSku.skuImage = this.form.detail.productImage;
				this.form.showSku.productPrice = this.form.detail.productPrice;
				this.form.showSku.bagPrice = this.form.detail.bagPrice;
				this.form.showSku.productSkuId = [];
				this.form.showSku.attr = [];
				this.form.showSku.feed = [];
				this.form.showSku.feed.length = this.form.detail.productFeed.length;
				this.form.showSku.linePrice = this.form.detail.linePrice;
				this.form.showSku.stockNum = this.form.detail.productStock;
				this.form.showSku.sum = 1;
				this.stock = this.form.detail.productStock;
				this.form.detail.productFeed = this.productFeedList;
			},
			/*选择属性*/
			selecedtSpec(item, index) {
				let self = this;
				if (item.checked) {
					item.checked = false;
					self.form.showSku.productSkuId[0] = null;
				} else {
					self.form.detail.skuList.forEach((sitem, sindex) => {
						sitem.checked = false;
					});
					item.checked = true;
					self.form.showSku.productSkuId[0] = item.productSkuId;
					self.spaceName = item.specName;
					self.$set(self.form.showSku, "productPrice", item.productPrice);
					self.$set(self.form.showSku, "bagPrice", item.bagPrice);
					self.$set(self.form.showSku, "linePrice", item.linePrice);
					self.$set(self.form.showSku, "stockNum", item.stockNum);
				}
				if (self.form.showSku.productSkuId[0] == null) {
					self.initShowSku();
					return;
				}
				// 更新商品规格信息
				self.updateSpecProduct();
			},
			/*选择属性*/
			selectAttr(item, index, aindex) {
				let self = this;
				self.$set(self.form.showSku.attr, aindex, index);
				self.attrName[aindex] = item;
				// 更新商品规格信息
				self.updateSpecProduct();
			},
			/*选择加料*/
			selectFeed(item, index) {
				let self = this;
				if (
					self.form.showSku.feed[index] ||
					self.form.showSku.feed[index] === 0
				) {
					self.$set(self.form.showSku.feed, index, null);
					self.feedPrice -= item.price * 1;
					if (item.discountPrice) {
						self.discountPrice -= item.discountPrice;
					} else {
						self.discountPrice -= 0;
					}
					let n = self.feedName.indexOf(item.feedName);
					if (n > -1) {
						self.feedName.splice(n, 1);
					}
				} else {
					self.$set(self.form.showSku.feed, index, index);
					self.feedPrice += item.price * 1;
					if (item.discountPrice) {
						self.discountPrice += item.discountPrice;
					} else {
						self.discountPrice += 0;
					}
					self.feedName.push(item.feedName);
				}
				// 更新商品规格信息
				self.updateSpecProduct();
			},
			updateSpecProduct() {
				this.productPrice = this.form.showSku.productPrice;
				this.productLineprice = this.form.showSku.linePrice;
			},
			/*商品增加*/
			add() {
				if (this.stock <= 0) {
					uni.showToast({
						title: "商品库存不足",
						icon: "none",
						duration: 2000,
					});
					return;
				}
				this.form.showSku.sum++;
			},
			/*商品减少*/
			sub() {
				if (this.stock <= 0) {
					return;
				}
				if (this.form.showSku.sum < 2) {
					uni.showToast({
						title: "商品数量至少为1",
						icon: "none",
						duration: 2000,
					});
					return false;
				}
				this.form.showSku.sum--;
			},
			/*确认提交*/
			confirmFunc() {
				if (this.clock) {
					return;
				}
				if (
					this.form.showSku.productSkuId[0] == null &&
					this.form.detail.specType == 20
				) {
					uni.showToast({
						title: "请选择规格",
						icon: "none",
						duration: 2000,
					});
					return;
				}
				if (this.form.detail.productAttr != null) {
					for (let i = 0; i < this.form.detail.productAttr.length; i++) {
						if (this.form.showSku.attr[i] == null) {
							uni.showToast({
								title: "请选择属性",
								icon: "none",
								duration: 2000,
							});
							return;
						}
					}
				}
				if (this.form.showSku.sum > this.form.showSku.stockNum) {
					uni.showToast({
						title: "商品库存不足",
						icon: "none",
						duration: 2000,
					});
					return;
				}
				this.addCart();
			},
			/*加入购物车*/
			addCart() {
				let self = this;
				let feed = [];
				self.form.showSku.feed.forEach((item, index) => {
					if (item != null) {
						feed.push(item);
					}
				});
				if (feed.length <= 0) {
					feed = "";
				} else {
					feed = feed.join(",");
				}
				let price = "";
				if (self.discountPrice) {
					price = self.discountPrice * 1 + self.productPrice * 1;
				} else {
					price = self.feedPrice * 1 + self.productPrice * 1;
				}
				let params = {
					productId: self.form.detail.productId,
					productNum: self.form.showSku.sum,
					productSkuId: self.form.showSku.productSkuId[0],
					attr: self.form.showSku.attr.join(","),
					feed: feed,
					describe: self.describe(),
					price: price,
					productPrice: self.feedPrice * 1 + self.form.showSku.linePrice * 1,
					bagPrice: self.form.showSku.bagPrice,
					shopSupplierId: self.form.shopSupplierId,
					cartType: self.cartType,
					dinnerType: self.dinnerType,
					delivery: self.delivery,
				};
				let url = "order/cart/add";
				if (this.tableId) {
					params.tableId = this.tableId;
					url = "order/hallCart/add";
				}
				self.clock = true;
				self._postBody(
					url,
					params,
					function(res) {
						self.clock = false;
						// #ifndef H5
						uni.navigateBack();
						// #endif
						// #ifdef H5
						history.go(-1);
						// #endif
					},
					(err) => {
						self.clock = false;
					}
				);
			},
		},
	};
</script>

<style lang="scss">
	@import "./menu.scss";

	.header {
		position: fixed;
		z-index: 2;
		width: 100%;
		left: 0;
		top: 0;
	}

	.spec-bottom {
		position: fixed;
		width: 100%;
		bottom: 0;
		z-index: 2;
	}

	.good-detail-modal {
		padding-bottom: calc(310rpx + env(safe-area-inset-bottom));
	}

	.sell-point {
		margin-top: 26rpx;
	}

	.detail {
		width: 100%;
		padding-top: 24rpx;

		.wrapper {
			width: 100%;
			height: 100%;
			overflow: hidden;
			border: 0;

			.basic {
				padding: 0 26rpx;
				box-sizing: border-box;
				display: flex;
				flex-direction: column;

				.name {
					font-size: $font-size-base;
					color: $text-color-base;
					margin-bottom: 10rpx;
				}

				.tips {
					font-size: $font-size-sm;
					color: $text-color-grey;
				}
			}
		}
	}
</style>