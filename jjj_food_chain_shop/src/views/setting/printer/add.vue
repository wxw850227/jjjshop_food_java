<template>

  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--添加门店-->
      <div class="common-form">新增小票打印机</div>
      <el-form-item label="打印机名称 " prop="printerName" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.printerName" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="打印机类型 ">
        <el-select v-model="form.printerType" placeholder="请选择">
          <el-option v-for="(item,index) in type" :key="index" :label="item" :value="item">
          </el-option>
        </el-select>
        <div class="tips">目前支持 飞鹅打印机、365云打印</div>
      </el-form-item>


      <!-- 飞鹅打印机 -->
      <div v-if="form.printerType=='飞鹅打印机'">
        <el-form-item label="USER"  prop="feieYun.USER" :rules="[{required: true,message: ' '}]">
          <el-input v-model="form.feieYun.USER" class="max-w460"></el-input>
          <div class="tips">飞鹅云后台注册用户名</div>
        </el-form-item>

        <el-form-item label="UKEY"  prop="feieYun.UKEY" :rules="[{required: true,message: ' '}]">
          <el-input v-model="form.feieYun.UKEY" class="max-w460"></el-input>
          <div class="tips">飞鹅云后台登录生成的UKEY</div>
        </el-form-item>

        <el-form-item label="打印机编号"  prop="feieYun.SN" :rules="[{required: true,message: ' '}]">
          <el-input v-model="form.feieYun.SN" class="max-w460"></el-input>
          <div class="tips">打印机编号为9位数字,查看飞鹅打印机底部贴纸上面的编号</div>
        </el-form-item>
      </div>

      <!-- 365云打印 -->
      <div v-if="form.printerType=='365云打印'">
        <el-form-item label="打印机编号 "  prop="printCenter.deviceNo" :rules="[{required: true,message: ' '}]">
          <el-input v-model="form.printCenter.deviceNo" class="max-w460"></el-input>
        </el-form-item>

        <el-form-item label="打印机秘钥" prop="printCenter.key" :rules="[{required: true,message: ' '}]">
          <el-input v-model="form.printCenter.key" class="max-w460"></el-input>
        </el-form-item>
      </div>
      <el-form-item label="打印联数" prop="printTimes" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.printTimes" type="number" class="max-w460"></el-input>
        <div class="tips">同一订单，打印的次数</div>
      </el-form-item>

      <el-form-item label="排序">
        <el-input v-model="form.sort" type="number" class="max-w460"></el-input>
        <div class="tips">数字越小越靠前</div>
      </el-form-item>

      <!--提交-->
      <div class="common-button-wrapper">
          <el-button type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>



    </el-form>


  </div>

</template>

<script>
  import SettingApi from '@/api/setting.js';

  export default {
    data() {
      return {
        /*切换菜单*/
        // activeIndex: '1',
        /*form表单数据*/
        form: {
          printerName: '',
          printerType: '',
          sort: 1,
          printTimes: '',
          feieYun: {
            USER: '',
            UKEY: '',
            SN: '',
          },
          printCenter: {
            deviceNo: '',
            key: ''
          },
        },
        loading: false,
        type: [],

      };
    },
    created() {
      this.getData();
    },

    methods: {
      getData() {
        SettingApi.printerType({}, true)
          .then(res => {
            this.type = res.data;
          })
          .catch(error => {

          });
      },
      //提交表单
      onSubmit() {
        let self = this;
        let form = self.form;
        if (!self.form.printerType) {
          ElMessage .error('请选择打印机类型!');
          return;
        }
        self.$refs.form.validate((valid) => {
          if (valid) {
            self.loading = true;
            SettingApi.addPrinter(form, true)
              .then(data => {
                self.loading = false;
                ElMessage ({
                  message: '恭喜你，添加成功',
                  type: 'success'
                });
                 self.$router.push('/setting/printer/index');

              }).catch(error => {
                self.loading = false;
              });
          }
        });




      },

    }

  };
</script>

<style scoped>
  .tips {
    color: #ccc;
  }
</style>
