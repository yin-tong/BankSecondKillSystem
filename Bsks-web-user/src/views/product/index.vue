<template>
  <div class="app-container">
    <el-row :gutter="20">
      <div class="handbutton">
        <el-col :span="14">
          <el-button type="primary" size="medium" icon="el-icon-refresh-right" @click="getProductData()">刷新</el-button>
        </el-col>
      </div>
    </el-row>
    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      class="el-table"
      :data="productList"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      @selection-change="selectChange"
    >
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
      <el-table-column label="价格" width="150" align="center">
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
      <el-table-column label="存量" width="170" align="center">
        <template slot-scope="scope">
          {{ scope.row.quantity }}
        </template>
      </el-table-column>
      <el-table-column label="秒杀时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.killTime }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button type="info" size="small" @click="showProductDialog(scope.row)">详情</el-button>
          <el-button type="primary" :disabled="new Date(scope.row.killTime).getTime() - new Date().getTime()>0 || scope.row.quantity == 0" size="small" @click="buy(scope.row)">抢购</el-button>
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
    <!-- 产品详情表单 -->
    <el-dialog title="详细内容" :visible.sync="dialogFormVisible">
      <el-form :model="productForm">
        <el-col :span="12">
          <el-form-item label="产品序号:">
            <el-input v-model="productForm.id" autocomplete="off" style="width: 60%" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="名称:">
            <el-input
              v-model="productForm.name"
              autocomplete="off"
              style="width:60%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="抢购价格:" label-width="formLabelWidth">
            <el-input v-model="productForm.price" autocomplete="off" style="width:60%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="比率:" label-width="formLabelWidth">
            <el-input v-model="productForm.rate" autocomplete="off" style="width:60%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="限购数量:" label-width="formLabelWidth">
            <el-input v-model="productForm.limitedQuantity" autocomplete="off" style="width:60%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数量:" label-width="formLabelWidth">
            <el-input v-model="productForm.quantity" autocomplete="off" style="width:60%" />
          </el-form-item>
        </el-col>
        <el-form-item label="抢购时间:" label-width="formLabelWidth">
          <el-input v-model="productForm.killTime" autocomplete="off" style="width:60%" />
        </el-form-item>
        <el-form-item label="详情描述:" label-width="formLabelWidth">
          <el-input v-model="productForm.describe1" autocomplete="off" style="width:85%" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 抢购弹窗 -->
    <el-dialog title="购买" :visible.sync="dialogBuyVisible">
      <el-form ref="killForm" :model="killForm">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="产品名称" label-width="formLabelWidth">
              <el-input v-model="killForm.productName" autocomplete="off" style="width:60%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品价格" label-width="formLabelWidth">
              <el-input v-model="killForm.price" autocomplete="off" style="width:60%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品限购数量" label-width="formLabelWidth">
              <el-input v-model="killForm.limitedQuantity" autocomplete="off" style="width:50%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="购买数量" label-width="formLabelWidth">
              <el-input-number v-model="killForm.number" :min="0" :max="killForm.limitedQuantity" label="描述文字" @change="handleChange" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支付金额" label-width="formLabelWidth">
              <el-input v-model="killForm.payMoney" autocomplete="off" :disabled="true" style="width:75%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户余额" label-width="formLabelWidth">
              <el-input v-model="killForm.money" autocomplete="off" style="width:75%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="danger" :disabled="change == 0" @click="killnew()">立即抢购</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getProductData, kill, getKillVoucher } from '@/api/product'
import { getAccountForm } from '@/api/account'

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
    return {
      listLoading: true,
      filterText: '',
      select: [],
      // 查询数据
      productList: [],
      currentPage: 1,
      totalNumber: 0,
      pageSize: 10,
      gettime: '', // 当前时间
      change: 0, // 秒杀按钮禁用
      buttonBuy: -1, // 抢购弹窗弹出按钮禁用
      x: '',
      voucher: '',
      // 时间框
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            }
          }
        ]
      },
      value1: '',
      // 表单数据
      dialogFormTitle: '',
      dialogFormVisible: false,
      dialogBuyVisible: false,
      addButtonVisible: false,
      updateButtonVisible: false,
      productForm: {
        id: '',
        name: '',
        price: '',
        rate: '',
        limitedQuantity: '',
        quantity: '',
        describe1: '',
        killTime: '',
        status: '',
        createTime: '',
        updateTime: '',
        version: ''
      },
      killForm: {
        productId: '',
        productName: '',
        payPassword: '',
        limitedQuantity: 0,
        number: 0,
        price: '',
        payMoney: '',
        money: '',
        orderTime: ''
      },
      form: {
        id: '',
        name: '', // 姓名
        phone: '', // 电话号码
        password: '', // 密码
        identityId: '', // 身份照号
        age: '', // 年龄
        gender: '', // 性别
        address: '', // 地址
        roleName: '', // 身份
        money: '', // 账户余额
        version: ''
      }
    }
  },
  created() {
    this.getProductData()
    this.getAccountData()
    this.change = -2
  },
  methods: {
    // 秒杀
    killnew() {
      if (this.killForm.number === 0) {
        const h = this.$createElement
        this.$message({
          message: h('p', null, [
            h('span', null, '抢购数量为0,抢购失败'),
            h('i', { style: 'color: teal' })
          ])
        })
      } else {
        getKillVoucher({ accountId: this.form.id, productId: this.killForm.productId, number: this.killForm.number }).then(response => {
          this.voucher = response.data
          kill(this.voucher).then(response => {
            this.$message.success(response.message)
          })
        })
      }
      this.dialogBuyVisible = false
    },
    // 展示商品信息
    showProductDialog(product) {
      this.productForm = product
      this.dialogFormVisible = true
    },
    // 获取用户信息
    getAccountData() {
      getAccountForm({ accountId: this.$store.getters.accountId }).then(response => { this.form = response.data })
      // console.log(this.$store.getters.accountId)
    },
    // 抢购界面弹出
    buy(product) {
      this.change = 1
      this.killForm.number = 0
      this.killForm.productName = product.name
      this.killForm.productId = product.id
      this.killForm.price = product.price
      this.killForm.limitedQuantity = product.limitedQuantity
      this.killForm.payMoney = this.killForm.number * this.killForm.price
      this.killForm.money = this.form.money
      this.killForm.payPassword = this.form.password
      this.dialogBuyVisible = true

      // 判断按钮是否要显示
      var leftTime = new Date(product.killTime).getTime() - new Date().getTime()
      if (leftTime > 0 || this.payMoney > this.form.money || this.killForm.number === 0) {
        this.change = 0
      }
      // console.log(this.change)
    },
    handleChange(value) {
      this.killForm.number = value
      this.killForm.payMoney = this.killForm.number * this.killForm.price
      if (this.killForm.payMoney > this.form.money) {
        // alert('账户余额不足')
        // console.log('账户余额不足')
        const h = this.$createElement
        this.$message({
          message: h('p', null, [
            h('span', null, '账户余额不足'),
            h('i', { style: 'color: teal' })
          ])
        })
        this.change = 0
        // console.log(this.change)
      } else {
        this.change = 1
        // console.log(this.change)
      }
    },
    // 获取商品信息
    getProductData() {
      this.listLoading = true
      getProductData({
        currentPage: this.currentPage,
        pageSize: this.pageSize
      }).then((response) => {
        this.productList = response.data
        this.totalNumber = this.productList.length
        this.listLoading = false
      })
    },
    // 选择框改变
    selectChange(selectProducts) {
      this.select = []
      selectProducts.forEach((item, index) => {
        this.select.push(item.id)
      })
    },
    // 表单取消操作
    doCancel() {
      this.productForm = []
      this.getProductData()
      this.dialogFormVisible = false
      this.addButtonVisible = false
      this.updateButtonVisible = false
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

<style lang="scss" scoped>
.handbutton{
  margin-bottom: 50px;
}

.el-table{
  width: 100%;
  .el-table__header-wrapper table,.el-table__body-wrapper table{
    width: 100% !important;
  }
  .el-table__body, .el-table__footer, .el-table__header{
    table-layout: auto;
  }
}
</style>
