import {
	createRouter,
	createWebHashHistory
} from 'vue-router';
import {
	setupPermissions
} from './permissions';
export const constantRoutes = [
	{
		path: '/login',
		name: 'login',
		meta: {
			title: '登录'
		},
		component: () => import('@/views/login/index.vue'),
	},
	{
		path: '/',
		name: 'Main',
		meta: {
			title: '母版'
		},
		component: () => import('@/views/layout/Main.vue'),
		children: [
			{
				path: '/Home',
				name: 'Home',
				meta: {
					title: '首页',
					topTree: '/Home'
				},
				component: () => import('@/views/home/Home.vue'),
			},
			// {
			// 	path: '/plugs/plugs/Index',
			// 	name: 'plugs_index',
			// 	meta: {
			// 		title: '插件管理'
			// 	},
			// 	component: () =>
			// 		import('@/views/plugs/plugs/Index.vue')
			// },
			{
				path: '/access/Index',
				name: 'access_Index',
				meta: {
					title: '后台权限'
				},
				component: () =>
					import('@/views/access/Index.vue')
			},
			{
				path: '/shop/Index',
				name: 'shop_Index',
				meta: {
					title: '商城'
				},
				component: () => import('@/views/shop/Index.vue')
			},
			{
				path: '/password/Index',
				name: 'password_Index',
				meta: {
					title: '修改密码'
				},
				component: () => import('@/views/password/Index.vue')
			},
			{
				path: '/message/Index',
				name: 'message_Index',
				meta: {
					title: '消息设置'
				},
				component: () => import('@/views/message/Index.vue')
			},
			// {
			// 	path: '/region/Index',
			// 	name: 'region_Index',
			// 	meta: {
			// 		title: '地区列表'
			// 	},
			// 	component: () =>import('@/views/region/Index.vue')
			// },
			// {
			// 	path: '/region/add',
			// 	name: 'region_add',
			// 	meta: {
			// 		title: '地区新增'
			// 	},
			// 	component: () =>import('@/views/region/add.vue')
			// },
			// {
			// 	path: '/region/edit',
			// 	name: 'region_edit',
			// 	meta: {
			// 		title: '地区修改'
			// 	},
			// 	component: () =>import('@/views/region/edit.vue')
			// },
			{
				path: '/setting/Index',
				name: 'setting_Index',
				meta: {
					title: '系统设置'
				},
				component: () => import('@/views/setting/index.vue')
			},

		]
	},
];
const router = createRouter({
	history: createWebHashHistory(),
	routes: constantRoutes,
});
export function setupRouter(app) {
	setupPermissions(router);
	app.use(router);
	return router;
}

export default router;
