<template>
  <div class="product">
    <!--添加产品分类-->
    <div class="common-level-rail">
      <el-button
        size="small"
        type="primary"
        @click="addClick"
        icon="Plus"
        v-auth="'/store/table/type/add'"
        >添加类型</el-button
      >
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          size="small"
          :data="tableData"
          row-key="typeId"
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="sort" label="排序"></el-table-column>
          <el-table-column
            prop="typeName"
            label="类型名称"
            width="180"
          ></el-table-column>
          <el-table-column prop="maxNum" label="人数区间" width="180">
            <template #default="scope">
              {{ scope.row.minNum }}-{{ scope.row.maxNum }}人
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="添加时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="100">
            <template #default="scope">
              <el-button
                @click="editClick(scope.row)"
                type="text"
                size="small"
                v-auth="'/store/table/type/edit'"
                >编辑</el-button
              >
              <el-button
                @click="deleteClick(scope.row)"
                type="text"
                size="small"
                v-auth="'/store/table/type/delete'"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!--添加-->
    <Add
      v-if="open_add"
      :open_add="open_add"
      :addform="categoryModel"
      @closeDialog="closeDialogFunc($event, 'add')"
    >
    </Add>
    <!--修改-->
    <Edit
      v-if="open_edit"
      :open_edit="open_edit"
      :editform="categoryModel"
      @closeDialog="closeDialogFunc($event, 'edit')"
    ></Edit>
  </div>
</template>

<script>
import StoreApi from "@/api/store.js";
import Add from "./add.vue";
import Edit from "./edit.vue";
export default {
  components: {
    Add,
    Edit,
  },
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
      /*是否打开添加弹窗*/
      open_add: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      categoryModel: {
        model: "",
      },
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    handleClick() {
      this.getData();
    },
    /*获取列表*/
    getData() {
      let self = this;
      self.loading = true;
      StoreApi.typelist({}, true)
        .then((data) => {
          self.loading = false;
          self.tableData = data.data;
          self.categoryModel = data.data;
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    /*打开添加*/
    addClick() {
      this.open_add = true;
    },

    /*打开编辑*/
    editClick(item) {
      this.categoryModel.model = item;
      this.open_edit = true;
    },

    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_add = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
      if (f == "edit") {
        this.open_edit = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
    },
    /*删除分类*/
    deleteClick(row) {
      let self = this;
      ElMessageBox.confirm("删除后不可恢复，确认删除该记录吗?", "提示", {
        type: "warning",
      }).then(() => {
        StoreApi.deleteType({
          typeId: row.typeId,
        }).then((data) => {
          ElMessage({
            message: "删除成功",
            type: "success",
          });
          self.getData();
        });
      });
    },
  },
};
</script>

<style></style>
