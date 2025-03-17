export default {
	baseURL: process.env.NODE_ENV === 'development' ? '/api/api' : `${import.meta.env.VITE_BASIC_URL}/api`,
	tokenName: 'tokenadmin',
	strongToken: "jjjSingleAdminToken",
	isDev: process.env.NODE_ENV === 'development' ? true : false,
	contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
	requestTimeout: 50000,
	statusName: 'code',
	messageName: 'msg',
	withCredentials: true,
	responseType: 'json',
};