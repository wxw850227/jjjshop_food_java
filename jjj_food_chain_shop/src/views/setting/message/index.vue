<template>
  <div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="会员通知" name="user"> </el-tab-pane>
    </el-tabs>

    <!--会员通知-->
    <Message v-if="activeName == 'user'" :messageTo="10"></Message>
  </div>
</template>
<script>
import Message from "./Message.vue";
export default {
  components: {
    /*编辑组件*/
    Message,
  },
  data() {
    return {
      /*参数*/
      messageTo: 10,
      /*当前选中*/
      activeName: "user",
    };
  },
  created() {
    if (this.$route.query.type != null) {
      this.activeName = this.$route.query.type;
    }
  },
  methods: {},
};
</script>
