<template>
	<view class="pop-spec" v-if="!loading">
		<view class="pop-bg" @click.stop="closeFunc(false)"></view>
		<view class="good-detail-modal" :data-theme="theme()" :class="theme() || ''">
			<view class="icon iconfont icon-guanbi1" @click.stop="closeFunc(false)"></view>
			<!-- 商品详情模态框 begin -->
			<view class="product-images" id="product-images">
				<image v-if="detail.image" :src="detail.image[0].filePath" class="product-image" mode="aspectFill">
				</image>
			</view>
			<view class="product-name f-s-0">
				<view class="text-ellipsis">{{ detail.productName || "" }}</view>
			</view>
			<view class="good_basic">
				<view class="f22 gray9">已售{{ detail.productSales || "" }}</view>
				<view class="gray6 sell-point" style="line-height: 1.5" v-if="detail.sellingPoint">
					{{ detail.sellingPoint || "" }}
				</view>
			</view>
			<view class="product-specs">
				<view class="property" v-if="detail.specType == 20">
					<view class="title"><text class="name">规格</text></view>
					<view class="values">
						<view class="value" @click.stop="selecedtSpec(value, key)"
							v-for="(value, key) in detail.skuList" :key="key" :class="{ default: value.checked }">
							{{ value.specName }}
						</view>
					</view>
				</view>
				<view class="property" v-if="detail.productAttrList.length > 0"
					v-for="(item, index) in detail.productAttrList" :key="index">
					<view class="title">
						<text class="name">{{ item.attributeName || "" }}</text>
					</view>
					<view class="values">
						<view class="value" @click.stop="selectAttr(value, key, index)" v-if="value != ''"
							v-for="(value, key) in item.attributeValue" :key="key"
							:class="{ default: form.showSku.attr[index] == key }">
							{{ value }}
						</view>
					</view>
				</view>
				<view class="property" v-if="detail.productFeed.length > 0">
					<view class="title"><text class="name">加料</text></view>
					<view class="values">
						<view class="value" @click.stop="selectFeed(item, index)"
							v-for="(item, index) in detail.productFeed" :key="index"
							:class="{ default: form.showSku.feed[index] != null }">
							+{{ item.feedName }}
						</view>
					</view>
				</view>
			</view>
			<!--详情内容-->
			<view class="product-content">
				<view class="border-b-e">
					<view class="group-hd d-s-c"><text class="min-name pr f30 fb">详情</text></view>
				</view>
				<view class="content-box">
					<view class="" v-html="detail.content"></view>
				</view>
			</view>
			<view class="spec-bottom">
				<view class="action">
					<view class="d-c d-a-s">
						<view class="d-s-c">
							<text class="f42 theme-price">￥</text>
							<text class="f48 mr16 theme-price">{{ price || 0 }}</text>
							<text class="f22 gray9" v-if="bagType != 1">打包费￥{{ bagPrice() || 0 }}</text>
						</view>
						<view class="f22 gray3">{{ describe() || "" }}</view>
					</view>
					<view class="btn-group">
						<image @click.stop="sub()" class="add-image"
							:src="'/static/icon/cart/reduce-' + theme() + '.png'">
						</image>
						<view class="number">{{ form.showSku.sum }}</view>
						<image v-if="stock > 0" @click.stop="add()" class="add-image"
							:src="'/static/icon/cart/add-' + theme() + '.png'" mode=""></image>
						<image v-else @click.stop="add()" class="add-image" src="/static/icon/cart/add-null.png"
							mode="">
						</image>
					</view>
				</view>
				<view>
					<view class="addCart-btn" @tap="confirmFunc">
						<view>加入购物车</view>
					</view>
				</view>
			</view>
			<!-- 商品详情模态框 end -->
		</view>
	</view>
</template>

<script>
	import utils from "@/common/utils.js";
	export default {
		components: {},
		data() {
			return {
				detail: {
					productAttrList: [],
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
				productId: "",
				isContentVideoPlay: false,
				isMPH5: false,
				loading: true,
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
		props: ["productModel"],
		created(e) {

			let form = this.productModel;

			this.productId = form.productId;
			this.delivery = form.delivery;
			this.bagType = form.bagType;
			this.dinnerType = form.dinnerType;
			this.cartType = form.cartType;
			this.tableId = form.tableId;
			this.shopSupplierId = form.shopSupplierId;
			this.clock = false;
			this.getData();
			//#ifdef H5
			if (this.isWeixin()) {
				this.isMPH5 = true;
			}
			//#endif
		},
		methods: {
			closeFunc(e) {
				this.$emit("close");
			},
			openVideo(name) {
				if (name == "video") {
					this.isVideoPlay = true;
					this.isContentVideoPlay = false;
				} else {
					this.isVideoPlay = false;
					this.isContentVideoPlay = true;
				}
			},
			bagPrice: function() {
				let price = this.form.showSku.bagPrice * this.form.showSku.sum;
				return price.toFixed(2) || 0;
			},
			goback() {
				uni.navigateBack({
					success: () => {
						delta: 1; //返回的页面数，如果 delta 大于现有页面数，则返回到首页。默认为1
					},

					//失败回调直接返回首页
					fail: () => {
						uni.switchTab({
							url: "/pages/index/index", //路径为测试数据，填写小程序真实路径就行
						});
					},
				});
			},
			getData() {
				let self = this;
				uni.showLoading({
					title: "加载中...",
				});
				self._get(
					"product/product/detail", {
						productId: self.productId,
						tableId: self.tableId || 0,
						shopSupplierId: self.shopSupplierId || 0,
					},
					(res) => {

						uni.hideLoading();
						if (!res.data.detail) {
							self.showError("商品已下架", () => {
								self.closeFunc(false);
							});
							return;
						}

						/*详情内容格式化*/
						res.data.detail.content = utils.format_content(
							res.data.detail.content
						);

						self.detail = res.data.detail;
						self.loading = false;
						this.showGoodDetailModal();
					}
				);
			},
			showGoodDetailModal() {
				let self = this;
				console.log(self.detail.skuList, 'e')
				self.detail.skuList.forEach((item, index) => {
					item.checked = false;
				});
				let obj = {
					specData: self.detail.skuList,
					detail: self.detail,
					shopSupplierId: self.detail.shopSupplierId,
					productSpecArr: self.specData != null ? new Array(self.specData.specAttr.length) : [],
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
				if (self.form.detail.productAttrList == "") {
					return;
				}

				self.form.detail.productAttrList.forEach((item, index) => {
					if (!self.form.showSku.attr[index]) {
						self.selectAttr(item.attributeValue[0], 0, index);
					}
				});
				console.log(1)
			},
			describe: function() {
				let spaceName = this.spaceName;
				if (spaceName != "") {
					spaceName += ";";
				}
				let attrName = this.attrName.join(";") || "";
				if (attrName != "") {
					attrName += ";";
				}
				let feedName = this.feedName.join(",") || "";
				if (feedName != "") {
					feedName += ";";
				}
				return spaceName + attrName + feedName || "";
			},
			/*初始化*/
			initShowSku() {
				this.productFeedList = this.form.detail.productFeedList
				this.form.showSku.skuImage = this.form.detail.image[0].filePath;
				this.form.showSku.productPrice = this.form.detail.productPrice;
				this.form.showSku.bagPrice = this.form.detail.bagPrice;
				this.form.showSku.productSkuId = [];
				this.form.showSku.attr = [];
				this.form.showSku.feed = [];
				this.form.showSku.feed.length = this.form.detail.productFeedList.length;
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
				if (this.form.detail.productAttrList != null) {
					for (let i = 0; i < this.form.detail.productAttrList.length; i++) {
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

				}

				self.clock = true;
				uni.showLoading({
					title: "",
					mask: true,
				});
				self._postBody(
					url,
					params,
					function(res) {
						self.clock = false;
						uni.hideLoading();
						self.$emit("close", self.form.showSku.sum, res);
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
	@import "./spec.scss";
</style>