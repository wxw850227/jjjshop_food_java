<template>

	<el-main class="infos">
		<el-descriptions :column="1" border size="small">
			<el-descriptions-item label="id">{{data.optLogId}}</el-descriptions-item>
			<el-descriptions-item label="用户名">{{data.userName}}</el-descriptions-item>
			<el-descriptions-item label="真实姓名">{{data.realName}}</el-descriptions-item>
			<el-descriptions-item label="url">{{data.url}}</el-descriptions-item>
			<el-descriptions-item label="ip">{{data.ip}}</el-descriptions-item>
			<el-descriptions-item label="添加时间">{{data.createTime}}</el-descriptions-item>
		</el-descriptions>
		<el-collapse v-model="activeNames" style="margin-top: 20px;">
			<el-collapse-item title="内容" name="1">
				<el-alert :title="data.content" :type="typeMap[data.level]" :closable="false"></el-alert>
			</el-collapse-item>
			<el-collapse-item title="agent" name="2">
				<div class="code">
				{{ data.agent }}
				</div>
			</el-collapse-item>
		</el-collapse>
	</el-main>
</template>

<script>
	export default {
		data() {
			return {
				data: {},
				activeNames: ['1'],
				typeMap: {
					'info': "info",
					'warn': "warning",
					'error': "error"
				}
			}
		},
		methods: {
			setData(data){
				this.data = data
			}
		}
	}
</script>

<style scoped>
.infos{background-color: transparent;}
	.code {background: #848484;padding:15px;color: #fff;font-size: 12px;border-radius: 4px;}
</style>
