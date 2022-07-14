<template>
  <div class="app-container">
    
    <el-row :gutter="20">
      <el-col :span="1.5"><el-button type="primary" size="medium" icon="el-icon-refresh-right" @click="getConsumerData()">刷新</el-button></el-col>
      <!-- <el-col :span="3"><el-button type="danger" size="medium" icon="el-icon-delete-solid" @click="deleteConsumerData()">删除</el-button></el-col>  -->
      <!--Select选择器-->
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
        <el-input v-model="filterText" placeholder="请输入查询关键字" @keyup.enter.native="filterByMessage()" />
      </el-col>
      <el-col :span="3">
        <el-button type="primary" size="medium" icon="el-icon-search" @click="filterByMessage()">搜索</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="listLoading"
      :data="consumerList"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      @selection-change="selectChange"
    >
      <!-- <el-table-column align="center" type="selection" /> -->
      <el-table-column v-if="false" align="center" label="id" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="手机号码" width="150" align="center">
        <template slot-scope="scope">
          {{ scope.row.phone }}
        </template>
      </el-table-column>
      <el-table-column v-if="false" label="密码" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.password }}</span>
        </template>
      </el-table-column>
      <el-table-column label="身份证号" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.identityId }}
        </template>
      </el-table-column>

      <el-table-column label="姓名" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="性别" width="50" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.gender }}</span>
        </template>
      </el-table-column>
      <el-table-column label="家庭住址" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.address }}
        </template>
      </el-table-column>

      <!-- <el-table-column label="工作状态" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column> -->
      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.updateTime }}
        </template>
      </el-table-column>
      
      </el-table-column>
      <el-table-column v-if="false" align="center" width="150" label="操作">
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
import { getConsumerData, filterConsumerByName, filterConsumerByidentityId, filterConsumerByPhone} from '@/api/consumer'

export default {
  //filters: {
    // statusFilter(status) {
    //   const statusMap = {
    //     published: 'success',
    //     draft: 'gray',
    //     deleted: 'danger'
    //   }
    //   return statusMap[status]
    // }
  //},
  data() {
    return {
      select:[],
      listLoading: true,
      dialogFormVisible: false,
      filterText: '',
      value: '',
      consumerList: [],
      totalNumber: null,
      currentPage: 1,
      pageSize: 10,
      consumerForm: {
        id: null,
        phone: '',
        password: null,
        identityId: null,
        name: '',
        gender: null,
        address: '',
        //status: '',
        createTime: null,
        updateTime: null,
        version: null
      },
      options: [{
          value: 'name',
          label: '根据姓名查询'
        }, {
          value: 'phone',
          label: '根据手机号查询'
        }, {
          value: 'identityId',
          label: '根据身份证号查询'
        }],
        value: ''
    }
  },
  created() {
    this.getConsumerData()
  },
  methods: {
    getConsumerData() {
      this.listLoading = true
      getConsumerData({currentPage: this.currentPage, pageSize: this.pageSize}).then(response => {
        //this.consumerList = response.data.consumerList
        this.consumerList = response.data.accounts
        this.totalNumber = response.data.totalNumber
        this.listLoading = false
      })
    },
    // switchChange(id, status) {
    //   changeConsumerStatus(id, status).then(response => {
    //     if (response.code === 20000) {
    //       console.log('修改状态成功')
    //     }
    //   })
    // },
    handleSizeChange(size) {
      this.pageSize = size
      this.getConsumerData()
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage
      this.getConsumerData()
    },
    // 根据用户名查询产品信息
    filterByMessage() {
      //console.log(this.value)
      if(this.value == 'name'){
        //consumerName改为了accountName
        filterConsumerByName({ accountName: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          this.consumerList = response.data.accounts
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
      }else if(this.value == 'identityId'){
        filterConsumerByidentityId({ identityId: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          this.consumerList = response.data.accounts
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
        filterConsumerByPhone({ phone: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          this.consumerList = response.data.accounts
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

    selectChange(selectConsumers) {
      this.select = []
      selectConsumers.forEach((item, index) => {
        this.select.push(item.id)
      })
    },
    // deleteConsumerData() {
    //   if (this.select.length === 0) {
    //     this.$confirm('请先选择要删除的数据', '提示', { type: 'warning' })
    //     return
    //   }
    //   this.$confirm('删除后无法复原，确定删除吗', '提示', { type: 'warning' }).then(() => {
    //     deleteConsumerData({ ids: this.select }).then(response => {
    //       this.$message.success(response.message)
    //       this.getConsumerData()
    //     })
    //   })
    // },
  }

}
</script>

<style lang="scss" scoped>

</style>


