<template>
  <div class="app-container">
    
    <el-row :gutter="20">
      <el-col :span="1.5"><el-button type="primary" size="medium" icon="el-icon-refresh-right" @click="getFirstFilterData()">刷新</el-button></el-col>
      <!-- <el-col :span="3"><el-button type="danger" size="medium" icon="el-icon-delete-solid" @click="deleteFirstFilterData()">删除</el-button></el-col>  -->
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
      

      <el-col :span="4">
        <el-input v-model="filterText" placeholder="请输入查询关键字" @keyup.enter.native="filterByMessage()" />
      </el-col>
      <el-col :span="3">
        <el-button type="primary" size="medium" icon="el-icon-search" @click="filterByMessage()">搜索</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="listLoading"
      :data="firstFilterList"
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
      <el-table-column label="身份证号" width="300" align="center">
        <template slot-scope="scope">
          {{ scope.row.identityId }}
        </template>
      </el-table-column>

      <el-table-column label="结果" width="150" align="center">
        <template slot-scope="scope">
          <span :class="scope.row.result==='通过'?'green':'red'">{{ scope.row.result }}</span>
        </template>
      </el-table-column>
      <el-table-column label="拒绝理由" width="250" align="center">
        <template slot-scope="scope">
          {{ scope.row.rejectReason }}
        </template>
      </el-table-column>
      <el-table-column label="初筛时间" align="center">
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
import { getFirstFilterData, changeFirstFilterStatus, firstFilterByUpdateTime, firstFilterByidentityId, firstFilterByidentityIdAndTime} from '@/api/firstFilter'
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
    return {
      value1: '',
      select:[],
      listLoading: true,
      dialogFormVisible: false,
      filterText: '',
      value: '',
      firstFilterList: [],
      totalNumber: null,
      currentPage: 1,
      pageSize: 10,
      firstFilterForm: {
        id: null,
        identityId: null,
        result: '',
        rejectReason: '',
        updateTime: null,
        version: null
      },
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
      options: [{
          value: 'updateTime',
          label: '根据初筛时间查询'
        }, {
          value: 'identityId',
          label: '根据身份证号查询'
        },{
          value: 'updateTimeAndidentityId',
          label: '根据身份证号和时间查询'
        }],
        value: ''
    }
  },
  created() {
    this.getFirstFilterData()
  },
  methods: {
    getFirstFilterData() {
      this.listLoading = true
      getFirstFilterData({currentPage: this.currentPage, pageSize: this.pageSize}).then(response => {
        //this.firstFilterList = response.data.firstFilterList
        this.firstFilterList = response.data.records
        this.totalNumber = response.data.totalNumber
        this.listLoading = false
      })
    },
    switchChange(id, status) {
      changeFirstFilterStatus(id, status).then(response => {
        if (response.code === 20000) {
          console.log('修改状态成功')
        }
      })
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.getFirstFilterData()
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage
      this.getFirstFilterData()
    },
    // 根据条件查询筛选信息
    filterByMessage() {
      let startTime
      let endTime
      startTime = dateFormat(this.value1[0])
      endTime = dateFormat(this.value1[1])

      if(this.value == 'updateTime'){
        firstFilterByUpdateTime({ start: startTime, end: endTime, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          this.firstFilterList = response.data.records
          this.totalNumber = response.data.totalNumber
        })
      }else if(this.value == 'identityId'){
        firstFilterByidentityId({ identityId: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
          this.firstFilterList = response.data.records
          this.totalNumber = response.data.totalNumber
          if(!this.filterText){
            const h = this.$createElement;
            this.$message({
              message: h('p', null, [
                h('span', null, '查询id不能为空'),
                h('i', { style: 'color: teal' })
              ])
            })
          }
        })
      }else if(this.value == 'updateTimeAndidentityId'){
        if(this.value1 != ''&&this.filterText.length != 0){
          firstFilterByidentityIdAndTime({ start: startTime, end: endTime, identityId: this.filterText, currentPage: this.currentPage, pageSize: this.pageSize }).then(response => {
            this.firstFilterList = response.data.records
            this.totalNumber = response.data.totalNumber
            if(!this.filterText){
              const h = this.$createElement;
              this.$message({
                message: h('p', null, [
                  h('span', null, '查询id不能为空'),
                  h('i', { style: 'color: teal' })
                ])
              })
            }
          })
        }else{
          const h = this.$createElement;
              this.$message({
                message: h('p', null, [
                  h('span', null, '请将查询内容补充完整'),
                  h('i', { style: 'color: teal' })
                ])
              })
        }
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

    selectChange(selectFirstFilters) {
      this.select = []
      selectFirstFilters.forEach((item, index) => {
        this.select.push(item.id)
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


