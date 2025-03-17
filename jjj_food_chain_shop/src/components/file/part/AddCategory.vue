<template>
  <el-dialog
    :title="title"
    v-model="dialogVisible"
    width="30%"
    :before-close="handleClose" :append-to-body="true">
    <el-form size="small" :model="form" ref="form" label-width="100px" class="demo-ruleForm">
      <el-form-item
        label="分类名称"
        prop="groupName"
        :rules="[
          { required: true, message: '名字不能为空'}
        ]"
      >
        <el-input type="age" v-model="form.groupName" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" @click="handleClose">取消</el-button>
        <el-button size="small" type="primary" @click="submitForm()">提交</el-button>
      </el-form-item>
    </el-form>

  </el-dialog>
</template>

<script>
import FileApi from '@/api/file.js';
export default {
  data(){
    return {
      /*是否显示*/
      dialogVisible:true,
      title: '添加分类',
      /*from表单模型*/
      form:{
        groupName:'',
        groupId: 0
      }
    }
  },
  props:['category', 'fileType'],
  created() {
    if(this.category != null){
      this.form.groupName = this.category.groupName;
      this.form.groupId = this.category.groupId;
      this.title = '编辑分类';
    }else{
      this.form.groupName = '';
      this.form.groupId = 0;
      this.title = '添加分类';
    }
  },
  methods:{

    /*添加图片类别*/
    addCategory: function() {
      let self = this;
      FileApi.addCategory({
        groupName: self.form.groupName,
        groupType: self.fileType
      },).then(data => {
          ElMessage ({
            message: '添加成功',
            type: 'success'
          });
          self.handleClose({status:'success'});
        })
        .catch(error => {
          ElMessage .error('添加失败');
        });
    },

    /*修改图片类别*/
    editCategory: function() {
      let self = this;
      let param = {
        groupName: this.form.groupName,
        groupId: this.form.groupId
      };
      FileApi.editCategory(param).then(data => {
          ElMessage ({
            message: '修改成功',
            type: 'success'
          });
          self.handleClose({status:'success'});
        })
        .catch(error => {
          ElMessage .error('修改失败');
        });
    },

    /*提交*/
    submitForm() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if(this.category && this.category.groupId != null){
            this.editCategory();
          }else{
            this.addCategory();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },


    /*关闭弹窗*/
    handleClose(param){
      this.dialogVisible=false;
      this.$emit('closeCategory',param);
    }
  }

}

</script>

<style>
</style>
