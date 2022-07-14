<template>
  <div class="app-container">
    <el-row :gutter="20">
      <div class="handbutton">
        <el-col :span="1.5"><el-button type="primary" size="medium" icon="el-icon-refresh-right" @click="getOrderData()">刷新</el-button></el-col>
        <el-col :span="3"><el-button type="danger" size="medium" icon="el-icon-delete-solid" @click="deleteOrderData()">删除</el-button></el-col>
      </div>
    </el-row>
    <!-- 表格 -->
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
      <el-table-column label="用户名" width="150" align="center">
        <template slot-scope="scope">
          {{ scope.row.userName }}
        </template>
      </el-table-column>
      <el-table-column label="电话" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.phone }}
        </template>
      </el-table-column>
      <el-table-column label="购买数量" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.number }}
        </template>
      </el-table-column>
      <el-table-column label="产品名称" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.productName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付金额" width="180" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.payMoney }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.orderTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" width="150" label="抢购结果">
        <template slot-scope="scope">
          <span :class="scope.row.type === '抢购成功'?'green':'red'">{{ scope.row.type }}</span>
        </template>
			</el-table-column>
    </el-table>
    <el-pagination
      :current-page="currentPage"
      :page-sizes="[5, 10, 20, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalNumber"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { getOrderData, deleteOrderData } from '@/api/order'

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
      select: [],
      listLoading: true,
      filterText: '',
      orderList: [],
      totalNumber: null,
      currentPage: 1,
      pageSize: 10,
      orderForm: {
        id: null,
        accountId: null,
        userName: '',
        phone: null,
        number: null,
        productName: '',
        payMoney: null,
        createTime: null,
        updateTime: null,
        status: null,
        version: null
      }
    }
  },
  created() {
    this.getOrderData()
  },
  methods: {
    getOrderData() {
      this.listLoading = true
      getOrderData({ accountId: this.$store.getters.accountId, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
        this.orderList = response.data.orders
        this.totalNumber = response.data.totalNumber
        this.listLoading = false
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
        deleteOrderData({ orderIds: this.select.toString() }).then(response => {
          this.$message.success(response.message)
          this.getOrderData()
        })
      })
    }
  }

}
</script>

<style lang="scss" scoped>
.handbutton{
  margin-bottom: 50px;
}
.green{
  color: #67C23A;
}
.red{
  color: #F56C6C;
}

</style>
