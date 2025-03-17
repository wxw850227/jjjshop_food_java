import { defineStore } from 'pinia';
import { setStorage, getStorage } from '@/utils/storageData';
import AuthApi from '@/api/auth.js';
import configObj from "@/config";
let { strongToken, renderMenu, menu } = configObj;
import { handRouterTable, handMenuData } from '@/utils/router';
export const useUserStore = defineStore('main', {
	state: () => {
		return {
			token: getStorage(strongToken),
			userInfo: getStorage('userInfo'),
			list: {},
			menus: getStorage(menu),
			renderMenus: getStorage(renderMenu),
		};
	},
	getters: {},
	actions: {
		bus_on(name, fn) {
			let self = this;
			self.list[name] = self.list[name] || [];
			self.list[name].push(fn);
		},
		// 发布
		bus_emit(name, data) {
			let self = this;
			if (self.list[name]) {
				self.list[name].forEach((fn) => {
					fn(data);
				});
			}
		},
		// 取消订阅
		bus_off(name) {
			let self = this;
			if (self.list[name]) {
				delete self.list[name];
			}
		},
		/**
		 * @description 登录
		 * @param {*} token
		 */
		async afterLogin(info) {
			this.userInfo = this.userInfo || {};
			const { data: { token, appId, isSuper, shopUserId, userName, shopSupplierId, supplierName, logoUrl, userType, } } = info;
			this.token = token;
			// const { data: { app_id, shop_name, shopSupplierId, supplier_name, token, user_name, user_type, version, logoUrl } } = info;
			const { data } = await AuthApi.getRoleList({});
			const { data: { version, shop_name } } = await AuthApi.getUserInfo({});
			let renderMenusList = handMenuData(JSON.parse(JSON.stringify(data)));
			let menusList = handRouterTable(JSON.parse(JSON.stringify(data)));
			setStorage(JSON.stringify(menusList), menu);
			setStorage(JSON.stringify(renderMenusList), renderMenu);
			this.userInfo.shopSupplierId = shopSupplierId;
			this.userInfo.userName = userName;
			this.userInfo.version = version;
			this.userInfo.logoUrl = logoUrl;
			this.userInfo.shopName = shop_name;
			this.userInfo.AppID = appId;
			this.userInfo.shopUserId = shopUserId;
			this.userInfo.isSuper = isSuper;
			this.userInfo.supplierName = supplierName;
			this.userInfo.userType = userType;

			this.renderMenus = renderMenusList;
			this.menus = menusList;
			setStorage(JSON.stringify(token), strongToken);
			setStorage(JSON.stringify(this.userInfo), 'userInfo');
		},
		/**
		 * @description 退出登录
		 * @param {*} token
		 */
		afterLogout() {
			sessionStorage.clear();
			this.token = null;
			this.menus = null;
			this.userInfo = null;
			setStorage(null, 'userInfo');
		},
		/**
		 * @description 更改商城设置名称及其logo
		 * @param {*} token
		 */
		changStore(data) {
			this.userInfo.shopName = data.name;
			this.userInfo.logoUrl = data.logoUrl;
			setStorage(JSON.stringify(this.userInfo), 'userInfo');
		}

	}
});
export default useUserStore;