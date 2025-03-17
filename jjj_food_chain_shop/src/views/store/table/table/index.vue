<template>
  <div class="product">
    <div>
      <el-form
        size="small"
        :inline="true"
        :model="form"
        class="demo-form-inline d-s-c"
      >
        <el-form-item label="编号">
          <el-input v-model="form.search" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="选择区域">
          <el-select size="small" v-model="form.areaId" placeholder="请选择">
            <el-option
              v-for="(item, index) in areaList"
              :key="index"
              :label="item.areaName"
              :value="item.areaId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择类型">
          <el-select size="small" v-model="form.typeId" placeholder="请选择">
            <el-option
              v-for="(item, index) in typeList"
              :key="index"
              :label="item.typeName"
              :value="item.typeId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="onSubmit"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <!--添加产品分类-->
    <div class="common-level-rail">
      <el-button
        size="small"
        type="primary"
        @click="addClick"
        icon="Plus"
        v-auth="'/store/table/table/add'"
      >
        添加桌位</el-button
      >
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          size="small"
          :data="tableData"
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="tableId" label="桌号id"></el-table-column>
          <el-table-column prop="areaName" label="所属区域"></el-table-column>
          <el-table-column prop="typeName" label="桌位类型"></el-table-column>
          <el-table-column prop="tableNo" label="桌位编码"></el-table-column>
          <el-table-column prop="tableNo" label="人数区间">
            <template #default="scope">
              {{ scope.row.minNum }}-{{ scope.row.maxNum }}人
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序"></el-table-column>
          <el-table-column prop="createTime" label="添加时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="190">
            <template #default="scope">
              <el-button
                @click="qrcode(scope.row)"
                type="text"
                size="small"
                v-auth="'/store/table/table/edit'"
              >
                二维码</el-button
              >
              <el-button
                @click="editClick(scope.row)"
                type="text"
                size="small"
                v-auth="'/store/table/table/edit'"
                >编辑
              </el-button>
              <el-button
                @click="deleteClick(scope.row)"
                type="text"
                size="small"
                v-auth="'/store/table/table/delete'"
              >
                删除</el-button
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
      :type="typeList"
      :areaList="areaList"
      :addform="categoryModel"
      @closeDialog="closeDialogFunc($event, 'add')"
    >
    </Add>
    <!--修改-->
    <Edit
      v-if="open_edit"
      :open_edit="open_edit"
      :editform="categoryModel"
      :type="typeList"
      :areaList="areaList"
      @closeDialog="closeDialogFunc($event, 'edit')"
    ></Edit>
    <div class="pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        background
        :current-page="curPage"
        :page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="totalDataNumber"
      ></el-pagination>
    </div>
    <Qrcode :open="isqrcode" :codeId="codeId" @close="closeQrcode"></Qrcode>
  </div>
</template>

<script>
import StoreApi from "@/api/store.js";
import Add from "./add.vue";
import Edit from "./edit.vue";
import qs from "qs";
import Qrcode from "./dialog/Qrcode.vue";
export default {
  components: {
    Add,
    Edit,
    Qrcode,
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
      form: {
        search: "",
        areaId: "",
        typeId: "",
      },
      typeList: [],
      areaList: [],
      source: "wx",
      /*一页多少条*/
      pageSize: 20,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      isqrcode: false,
      codeId: "",
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.getData();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },
    /*获取列表*/
    getData() {
      let self = this;
      self.loading = true;
      let params = self.form;
      params.page = self.curPage;
      params.list_rows = self.pageSize;
      StoreApi.tablelist(params, true)
        .then((data) => {
          self.loading = false;
          self.tableData = data.data.list.records;
          self.totalDataNumber = data.data.list.total;
          self.typeList = data.data.typeList;
          self.areaList = data.data.areaList;
          self.categoryModel = data.data.list.records;
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
        StoreApi.deleteTable({
          tableId: row.tableId,
        }).then((data) => {
          ElMessage({
            message: "删除成功",
            type: "success",
          });
          self.getData();
        });
      });
    },
    qrcode(row) {
      let self = this;
      self.codeId = row.tableId;
      self.isqrcode = true;
    },
    closeQrcode() {
      let self = this;
      self.isqrcode = false;
    },
    onSubmit() {
      this.curPage = 1;
      this.getData();
    },
  },
};
</script>

<style></style>
