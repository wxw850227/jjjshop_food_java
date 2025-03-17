<template>
  <!--
    	描述：商品管理-商品编辑-规格/库存
    -->
  <div>
    <!--规格设置-->
    <div class="common-form mt50">商品属性</div>
    <!--多规格-->
    <div>
      <div class="mt16">
        <!-- <el-form-item label="属性明细："> -->
        <div class="p-0-30 mb18">
          <div class="d-s-c ">商品属性: <div class="blue ml30" @click="addAttr">添加属性+</div>
          </div>
        </div>
        <!--多规格表格-->
  <div class="mb18 p-0-30" v-for="(item,index) in form.model.productAttrList" :key="index"  v-if="form.model.productAttrList.length>0">
    <div class="d-c-c mb16">
            <div style="width: 100px;"><span class="red">*</span>属性名称：</div>
            <div class="flex-1 ml40"><span class="red">*</span>属性值(至少填写两个)</div>
          </div>
          <div class="d-c-s">
            <div class="d-c-c">
              <el-button class='mr16 mb20' size="mini" icon="Delete" circle
                @click="delIndex(index)"></el-button>
              <div style="width: 100px;">
                <el-form-item label-width="0" :rules="[{ required: true, message: ' ' }]"
                  :prop="'model.productAttrList.'+index+'.attributeName'">
                  <el-autocomplete class="inline-input" size="medium" v-model="item.attributeName"
                    :fetch-suggestions="querySearch" placeholder="如:温度"></el-autocomplete>
                </el-form-item>
              </div>
            </div>
            <div class="flex-1 d-s-s" style="flex-wrap: wrap;">
              <div v-for="(aitem,aindex) in item.attributeValue" :key="aindex" class="ml20 mb20">
                <el-form-item label-width="0" style="margin-bottom: 0;" :rules="[{ required: true, message: ' ' }]"
                  :prop="'model.productAttrList.'+index+'.attributeValue.'+aindex">
                  <el-autocomplete style="width: 100px;" size="medium" v-model="item.attributeValue[aindex]"
                    :fetch-suggestions="(queryString,cb)=>querySearch2(queryString,cb,index)" placeholder="请选择">
                  </el-autocomplete>
                  <el-button v-if="aindex == item.attributeValue.length - 1" class="ml10" size="mini"
                    icon="Plus" circle @click="addAttIndex(index)"></el-button>
                  <el-button class='ml10' size="mini" icon="Delete" v-if="aindex>1" circle
                    @click="delAttIndex(index,aindex)"></el-button>
                </el-form-item>
              </div>
            </div>
          </div>
        </div>
      
        <!-- </el-form-item> -->
      </div>
    </div>

  </div>
</template>

<script>
  export default {
    data() {
      return {
        restaurants: [],
        restaurants2: [],
        formData: {},
        productAttrList:[],
      }
    },
    inject: ['form'],
    methods: {
      addAttr() {
        if (this.form.model.productAttrList == '') {
          this.form.model.productAttrList = []
        }
     	this.$nextTick(() => {
					this.form.model.productAttrList.push({
          attributeName: '',
          attributeValue: ['', '']
        })
				})  
      },
      querySearch(queryString, cb) {
        let self = this;
        if (self.restaurants.length == 0) {
          self.form.attribute.forEach((item, index) => {
            self.restaurants.push({
              value: item.attributeName
            })
          })
        }
        var restaurants = this.restaurants;
        var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
        // 调用 callback 返回建议列表的数据
        cb(results);
      },
      querySearch2(queryString, cb, n) {
        let self = this;
        let list = [];
        let name = self.form.model.productAttrList[n].attributeName;
        if (self.restaurants.length != 0 && name) {
          self.form.attribute.forEach((item, index) => {
            if (item.attributeName == name) {
              list = item.attributeValueList;
              return
            }
          })
        }
        if (list.length > 0) {
          self.restaurants2 = [];
          list.forEach(item => {
            self.restaurants2.push({
              value: item
            })
          })
        }

        var results = this.restaurants2;
        cb(results);
      },
      createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      addAttIndex(n) {
        this.form.model.productAttrList[n].attributeValue.push('')
      },
      delAttIndex(n, i) {
        this.form.model.productAttrList[n].attributeValue.splice(i, 1);
      },
      delIndex(n) {
        this.form.model.productAttrList.splice(n, 1);
      },
    }
  };
</script>

<style lang="scss" scoped>
  .d-c-s {
    display: flex;
    justify-content: center;
    align-items: flex-start;
  }

  .mb20 {
    margin-bottom: 20px;
  }

  .ml40 {
    margin-left: 40px;
  }

  .mr16 {
    margin-right: 16px;
  }
</style>