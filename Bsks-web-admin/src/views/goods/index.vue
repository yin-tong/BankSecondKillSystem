<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="1.5"><el-button type="primary" size="medium" icon="el-icon-refresh-right" @click="getProductData()">刷新</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" size="medium" icon="el-icon-circle-plus-outline" @click="addShowProductDialog()">增加</el-button></el-col>
      <el-col :span="3"><el-button type="danger" size="medium" icon="el-icon-delete-solid" @click="deleteProducts()">删除</el-button></el-col>
      <el-col :span="8">
        <!-- 时间选择器 -->
        <el-date-picker
          v-model="value1"
          unlink-panels
          type="datetimerange"
          range-separator="~"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
        />
      </el-col>
      <el-col :span="6">
        <el-input v-model="filterText" placeholder="根据产品名称查询" @keyup.enter.native="filterByMessage()" />
      </el-col>
      <el-col :span="3">
        <el-button type="primary" size="medium" icon="el-icon-search" @click="filterByMessage()">搜索</el-button>
      </el-col>
    </el-row>
    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      :data="productList"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      @selection-change="selectChange"
    >
      <el-table-column align="center" type="selection" />
      <el-table-column v-if="false" align="center" label="id" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="名称" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="价格" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="比率" width="110" align="center">
        <template slot-scope="scope">
          {{ scope.row.rate }}
        </template>
      </el-table-column>
      <el-table-column label="限购数量" width="110" align="center">
        <template slot-scope="scope">
          {{ scope.row.limitedQuantity }}
        </template>
      </el-table-column>
      <el-table-column label="数量" width="110" align="center">
        <template slot-scope="scope">
          {{ scope.row.quantity }}
        </template>
      </el-table-column>
      <el-table-column label="秒杀时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.killTime }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" :active-value="0" :inactive-value="1" active-color="#00FF00" inactive-color="#dadde5" @change="switchChange(scope.row.id,scope.row.status)" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" round @click="updateShowProductDialog(scope.row)">详情/修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页插件 -->
    <el-pagination
      :current-page="currentPage"
      :page-sizes="[5, 10, 20, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalNumber"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <!-- 产品表单 -->
    <el-dialog :title="dialogFormTitle" :visible.sync="dialogFormVisible">
      <el-form ref="productForm" :model="productForm" :rules="productFormRules">
        <el-form-item v-if="false" label="id">
          <el-input v-model="productForm.id" autocomplete="off" />
        </el-form-item>
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="productForm.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="价格"  prop="price">
          <el-input type="number" v-model.number="productForm.price"  autocomplete="off" />
        </el-form-item>
        <el-form-item label="比率" prop="rate">
          <el-input type="number" v-model.number="productForm.rate" autocomplete="off" />
        </el-form-item>
        <el-form-item label="限购数量" prop="limitedQuantity">
          <el-input type="number" v-model.number="productForm.limitedQuantity" autocomplete="off" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input type="number" v-model.number="productForm.quantity" autocomplete="off" />
        </el-form-item>
        <el-form-item label="秒杀时间" prop="killTime">
          <el-date-picker
            v-model="productForm.killTime"
            type="datetime"
            start-placeholder="抢购日期"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="productForm.status" :active-value="1" :inactive-value="0" active-color="#00FF00" inactive-color="#dadde5" />
        </el-form-item>
        <el-form-item label="描述" prop="describe1">
          <el-input v-model="productForm.describe1" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="doCancel()">取 消</el-button>
        <el-button v-show="addButtonVisible" type="primary" @click="addProduct()">增加</el-button>
        <el-button v-show="updateButtonVisible" type="primary" @click="updateProduct()">更新</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { getProductData, addProduct, deleteProducts, changeProductStatus, updateProduct, filterProductByName, filterProductByKillTime, filterProductByKillTimeAndName } from '@/api/product'
import { dateFormat } from '@/utils'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    // 表单名称验证规则
    const validateName = (rule, value, callback) => {
      if (!value) {
        callback(new Error('名称不能为空'))
      } else {
        callback()
      }
    }
    // 表单价格验证规则
    const validatePrice = (rule, value, callback) => {
      if (!value || isNaN(value) || value <= 0) {
        callback(new Error('请输入正数'))
      }
      callback()
    }
    // 表单利率验证规则
    const validateRate = (rule, value, callback) => {
      if (!value || isNaN(value) || value <= 0) {
        callback(new Error('只能输入正数'))
      }
      callback()
    }
    // 表单抢购数量验证规则
    const validateLimitedQuantity = (rule, value, callback) => {
      var t = /^[1-9]+[0-9]*]*$/
      if (!value || !t.test(value)) {
        callback(new Error('请输入正整数'))
      }
      callback()
    }
    // 表单数量验证规则
    const validateQuantity = (rule, value, callback) => {
      var t = /^[1-9]+[0-9]*]*$/
      if (!value || !t.test(value)) {
        callback(new Error('请输入正整数'))
      }
      callback()
    }
    // 表单秒杀时间验证规则
    const validateKillTime = (rule, value, callback) => {
      if (!value) {
        callback(new Error('秒杀时间不能为空'))
      } else {
        callback()
      }
    }
    const validateDescribe1 = (rule, value, callback) => {
      if (!value) {
        callback(new Error('描述不能为空'))
      } else {
        callback()
      }
    }

    return {
      listLoading: true,
      filterText: '',
      select: [],
      // 查询数据
      productList: [],
      currentPage: 1,
      totalNumber: 0,
      pageSize: 10,
      // 时间框
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      value1: '',
      // 表单数据
      dialogFormTitle: '',
      dialogFormVisible: false,
      addButtonVisible: false,
      updateButtonVisible: false,
      productForm: {},
      productFormRules: {
        name: [{ required: true, trigger: 'blur', validator: validateName }],
        price: [{ required: true, trigger: 'blur', validator: validatePrice }],
        rate: [{ required: true, trigger: 'blur', validator: validateRate }],
        limitedQuantity: [{ required: true, trigger: 'blur', validator: validateLimitedQuantity }],
        quantity: [{ required: true, trigger: 'blur', validator: validateQuantity }],
        describe1: [{ required: true, trigger: 'blur', validator: validateDescribe1 }],
        killTime: [{ required: true, trigger: 'blur', validator: validateKillTime }]
      }
    }
  },
  created() {
    this.getProductData()
  },
  methods: {
    // 获取商品信息
    getProductData() {
      this.listLoading = true
      getProductData({ currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
        this.productList = response.data.products
        //console.log(this.productList)
        this.totalNumber = response.data.totalNumber
        this.listLoading = false
      })
    },
    // 增加商品
    addProduct() {
      this.$refs.productForm.validate(valid => {
        if (valid) {
          addProduct(this.productForm).then(response => {
            this.$message.success(response.message)
            this.doCancel()
            this.getProductData()
          })
        }
      })
    },
    // 根据信息查询产品信息
    filterByMessage() {
      let startTime
      let endTime
      startTime = dateFormat(this.value1[0])
      endTime = dateFormat(this.value1[1])
      if (this.value1 === '' && this.filterText.length === 0) {
        this.$confirm('时间和名称不能都为空', '提示', { type: 'warning' })
        return
      } else if(this.value1 === '' && this.filterText.length != 0){
        filterProductByName({ productName: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          // this.productList = response.data.productList  
          this.productList = response.data.products
          this.totalNumber = response.data.totalNumber    
        })  
      }else if(this.value1 != '' && this.filterText.length === 0){
        filterProductByKillTime({ start: startTime, end: endTime, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          this.productList = response.data.products
          this.totalNumber = response.data.totalNumber    
        })
      }else if(this.value1 != ''&&this.filterText.length != 0){
        filterProductByKillTimeAndName({ start: startTime, end: endTime, productName: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          this.productList = response.data.products
          this.totalNumber = response.data.totalNumber    
        })
      }
      
    },
    // 选择框改变
    selectChange(selectProducts) {
      this.select = []
      selectProducts.forEach((item, index) => {
        this.select.push(item.id)
      })
    },
    // 批量删除
    deleteProducts() {
      if (this.select.length === 0) {
        this.$confirm('请先选择要删除的数据', '提示', { type: 'warning' })
        return
      }
      this.$confirm('删除后无法复原，确定删除吗', '提示', { type: 'warning' }).then(() => {
        //console.log(this.select.toString())
        deleteProducts({productIds: this.select.toString()} ).then(response => {
          this.$message.success(response.message)
          this.getProductData()
        })
      })
    },
    // 显示增加商品的表单
    addShowProductDialog() {
      this.productForm = {}
      this.addButtonVisible = true
      this.dialogFormTitle = '增加产品信息'
      this.dialogFormVisible = true
    },
    // 显示更新商品的表单
    updateShowProductDialog(product) {
      this.updateButtonVisible = true
      this.dialogFormTitle = '更新/增加产品信息'
      this.productForm = product
      //console.log(this.productForm)
      this.dialogFormVisible = true
    },
    // 表单取消操作
    doCancel() {
      this.productForm = []
      this.getProductData()
      this.dialogFormVisible = false
      this.addButtonVisible = false
      this.updateButtonVisible = false
    },
    // 更新商品信息
    updateProduct() {
      this.$refs.productForm.validate(valid => {
        if (valid) {
          updateProduct(this.productForm).then(response => {
            this.$message.success(response.message)
            this.dialogFormVisible = false
            this.doCancel()
            this.getProductData()
          })
        }
      })
    },
    // 更新商品状态
    switchChange(id, status) {
      this.$confirm('确定要改变状态吗', '提示', { type: 'warning' }).then(() => {
        changeProductStatus({ id: id, status: status }).then(response => {
          this.$message.success(response.message)
          this.getProductData()
        })
      }).catch(() => {
        this.productList.some(product => {
          if (product.id === id) {
            product.status = product.status === 0 ? 1 : 0
          }
        })
      })
    },
    // 页数量改变
    handleSizeChange(size) {
      this.pageSize = size
      this.getProductData()
    },
    // 页数改变
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage
      this.getProductData()
    }
  }

}
</script>

<style>

</style>
