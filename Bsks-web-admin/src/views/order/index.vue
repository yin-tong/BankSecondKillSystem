<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="1.5"><el-button type="primary" size="medium" icon="el-icon-refresh-right" @click="getOrderData()">刷新</el-button></el-col>
      <el-col :span="3"><el-button type="danger" size="medium" icon="el-icon-delete-solid" @click="deleteOrderData()">删除</el-button></el-col>
      <el-col :span="4">
        <el-select v-model="value" placeholder="请选择查询条件">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-input v-model="filterText" placeholder="请输入查询关键字" @keyup.enter.native="filterByMessage()"/>
      </el-col>
      <el-col :span="3">
        <el-button type="primary" size="medium" icon="el-icon-search" @click="filterByMessage()">搜索</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="listLoading"
      :data="orderList"
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
      <!-- <el-table-column v-if="false" label="账户id" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.accountId }}
        </template>
      </el-table-column> -->
      <el-table-column label="用户名" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.userName }}
        </template>
      </el-table-column>
      <el-table-column label="手机号码" width="150" align="center">
        <template slot-scope="scope">
          {{ scope.row.phone }}
        </template>
      </el-table-column>
      <el-table-column label="购买数量" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.number }}
        </template>
      </el-table-column>
      <el-table-column label="产品名称" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.productName }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="支付金额" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.payMoney }}</span>
        </template>
      </el-table-column>

      <el-table-column label="抢购结果" align="center">
        <template slot-scope="scope">
          <span :class="scope.row.type==='抢购成功'?'green':'red'">{{ scope.row.type }}</span>
        </template>
      </el-table-column>

      <el-table-column label="下单时间" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.orderTime }}</span>
        </template>
      </el-table-column>

      <el-table-column v-if="false" label="订单创建时间" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="false" label="订单更新时间" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="false" label="状态" width="50" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>

      <el-table-column v-if="false" label="version" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.version }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5, 10, 20, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalNumber">
    </el-pagination>
  </div>
</template>

<script>
import { getOrderData, deleteOrderData, changeOrderStatus, filterOrderByName, filterOrderByProductName, filterOrderByPhone} from '@/api/order'

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
      select:[],
      value: '',
      listLoading: true,
      filterText: '',
      orderList: [],
      totalNumber: null,
      currentPage: 1,
      pageSize: 10,
      orderForm: {
        id: null,
        //accountId: null,
        userName: '',
        phone: '',
        number: null,
        productName: '',
        type:'',
        payMoney: null,
        orderTime: null,
        createTime: null,
        updateTime: null,
        status: null,
        version: null
      },
      options: [{
          value: 'productName',
          label: '根据产品名称查询'
        }, {
          value: 'name',
          label: '根据用户名查询'
        }, {
          value: 'phone',
          label: '根据手机号查询'
        }],
        value: ''
    }
  },
  created() {
    this.getOrderData()
  },
  methods: {
    getOrderData() {
      this.listLoading = true
      getOrderData({currentPage: this.currentPage, pageSize: this.pageSize}).then(response => {
        // this.orderList = response.data.orderList
        this.orderList = response.data.orders
        this.totalNumber = response.data.totalNumber
        this.listLoading = false
      })
    },
    switchChange(id, status) {
      changeOrderStatus(id, status).then(response => {
        if (response.code === 20000) {
          console.log('修改状态成功')
        }
      })
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.getOrderData()
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage
      this.getOrderData()
    },
    // 根据信息查询产品信息
    filterByMessage() {
      if(this.value == 'name'){
        filterOrderByName({ userName: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          // this.orderList = response.data.orderList
          this.orderList = response.data.orders
          this.totalNumber = response.data.totalNumber
          if(!this.filterText){
            const h = this.$createElement;
            this.$message({
              message: h('p', null, [
                h('span', null, '查询内容不能为空'),
                h('i', { style: 'color: teal' })
              ])
            })
          }
        })
      }else if(this.value == 'productName'){
        filterOrderByProductName({ productName: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          // this.orderList = response.data.orderList
          this.orderList = response.data.orders
          this.totalNumber = response.data.totalNumber
          if(!this.filterText){
            const h = this.$createElement;
            this.$message({
              message: h('p', null, [
                h('span', null, '查询内容不能为空'),
                h('i', { style: 'color: teal' })
              ])
            })
          }
        })
      }else if(this.value == 'phone'){
        filterOrderByPhone({ phone: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          //this.orderList = response.data.orderList
          this.orderList = response.data.orders
          this.totalNumber = response.data.totalNumber
          if(!this.filterText){
            const h = this.$createElement;
            this.$message({
              message: h('p', null, [
                h('span', null, '查询内容不能为空'),
                h('i', { style: 'color: teal' })
              ])
            })
          }
        })
      }else if(this.value == ''){
            const h = this.$createElement;
            this.$message({
              message: h('p', null, [
                h('span', null, '请先选择查寻条件'),
                h('i', { style: 'color: teal' })
              ])
            })
      }
      
    },
    selectChange(selectOrders) {
      this.select = []
      selectOrders.forEach((item, index) => {
        this.select.push(item.id)
      })
    },
    deleteOrderData() {
      if (this.select.length === 0) {
        this.$confirm('请先选择要删除的数据', '提示', { type: 'warning' })
        return
      }
      this.$confirm('删除后无法复原，确定删除吗', '提示', { type: 'warning' }).then(() => {
        const orderIds = this.select.toString()
        //console.log(orderIds)
        deleteOrderData({ orderIds }).then(response => {
          this.$message.success(response.message)
          this.getOrderData()
        })
      })
    },
  }

}
</script>

<style lang="scss" scoped>
  .red {
    color: #F56C6C;
  }
  .green {
    color:#67C23A;
  }
</style>


