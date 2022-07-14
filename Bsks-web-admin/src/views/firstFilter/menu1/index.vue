<template>
<div class="app-container">
  <div class="buttongroup">
  <el-col :span="3"><el-button type="primary" plain size="medium" @click="getFilterRule()">获取初筛规则</el-button></el-col>
  <el-col :span="3"><el-button type="primary" plain size="medium" @click="updateFilterRule()">更新初筛规则</el-button></el-col>
  </div>
  <table border="1" cellspacing="0" cellpadding="0" align="center" :model="firstFilterRule" v-loading="listLoading" id="">
			<tr>
				<td class="td1">规则名称</td>
				<td class="td2">规则描述</td>
				<td class="td3">对象</td>
        <td class="td4">风险控制</td>
			</tr>

			<tr>
				<td class="td1">逾期记录</td>
				<td class="td2">
          近
          <!-- <input class="filterUserInput1" v-model="firstFilterRule.filterText1" placeholder="请输入逾期年数"/> -->
          <input class="filterUserInput1" v-model="firstFilterRule.limitOverdueYears" placeholder="请输入逾期年数"/>
          年逾期
          <input class="filterUserInput2" v-model="firstFilterRule.limitOverdueTimes" placeholder="请输入逾期次数"/>
          次以上（金额小于
          <input class="filterUserInput3" v-model="firstFilterRule.limitOverdueMoney" placeholder="金额"/>
          元
          <input class="filterUserInput4" v-model="firstFilterRule.limitPayoffDays" placeholder="天数"/>
          天内还清的除外）
        </td>
				<td class="td3">个人客户</td>
        <td class="td4">拒绝</td>
			</tr>

			<tr>
				<td class="td1">个人客户工作状态异常</td>
				<td class="td2">状态为失业
          <!-- <el-select v-model="firstFilterRule.value" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select> -->
        </td>
				<td class="td3">个人客户</td>
        <td class="td4">拒绝</td>
			</tr>

      <tr>
				<td class="td1">个人客户被列入失信人名单</td>
				<td class="td2">个人客户被列入当前严重违法失信被执行人名单，未执行完毕的。</td>
				<td class="td3">个人客户</td>
        <td class="td4">拒绝</td>
			</tr>

      <tr>
				<td class="td1">个人客户年龄</td>
				<td class="td2">小于
          <input class="filterUserInput5" v-model="firstFilterRule.limitAge" placeholder="请输入年龄"/>
          岁
          
        </td>
				<td class="td3">个人客户</td>
        <td class="td4">拒绝</td>
			</tr>
</table>

</div>

</template>

<script>
import { getFilterRulePage, getFilterRule, updateFilterRule } from '@/api/firstFilter'

export default {
  data() {
      return {
        
        listLoading: true,
        
        id: '',
        value: '',
        firstFilterRule: {
        },
        
      }
  },
  created() {
    this.getFilterRulePage()
  },
  methods: {
    getFilterRulePage() {
      this.listLoading = true
      getFilterRulePage({ data:this.firstFilterRule}).then(response => {
        this.firstFilterRule = response.data
        this.listLoading = false
      })
    },
    getFilterRule(){
      if(this.filterText1==''||this.filterText2==''||this.filterText3==''||this.filterText4==''||this.filterText5==''){
        const h = this.$createElement;
            this.$message({
              message: h('p', null, [
                h('span', null, '请将初筛规则补充完整'),
                h('i', { style: 'color: teal' })
              ])
            })
      }else{
        getFilterRule({limitOverdueYears: this.limitOverdueYears, limitOverdueTimes: this.limitOverdueTimes, limitOverdueMoney: this.limitOverdueMoney, limitPayoffDays:this.limitPayoffDays, limitAge:this.limitAge}).then(response => {
          //this.firstFilterList = response.data.firstFilterList
          this.getFilterRulePage()
          this.firstFilterRule = response.data
          console.log(this.firstFilterRule)
          this.listLoading = false
          
        })
      }
    },
    updateFilterRule(){
      if(this.filterText1==''||this.filterText2==''||this.filterText3==''||this.filterText4==''||this.filterText5==''){
        const h = this.$createElement;
            this.$message({
              message: h('p', null, [
                h('span', null, '请将初筛规则补充完整'),
                h('i', { style: 'color: teal' })
              ])
            })
      }else{
        updateFilterRule(this.firstFilterRule).then(response => {
        //this.firstFilterList = response.data.firstFilterList
        this.getFilterRulePage()
        this.firstFilterRule = response.data
        this.listLoading = false
        })
      }
    }
  },
}
</script>

<style lang="scss" scoped>
  table {
    border: 1px solid gray;
    height: 150px;
    width: 100%;
  }
  tr td {
    border: 1px solid gray;
  }
  tr {
    height: 100px;
    /**color: rgb(182, 179, 179);*/
    color: gray;
    text-align: center;
  }
  .td1 {
    width: 20%;
  }
  .td2 {
    width: 40%;
    text-align: left;
  }
  .td3 {
    width: 20%;
  }
  .td4 {
    width: 20%;
  }
  .filterUserInput1 {
    width: 120px;
    height: 35px;
    border: 1px solid gray;
  }
  .filterUserInput2 {
    width: 120px;
    height: 35px;
    border: 1px solid gray;
  }
  .filterUserInput3 {
    width: 120px;
    height: 35px;
    border: 1px solid gray;
  }
   .filterUserInput4 {
    width: 120px;
    height: 35px;
    border: 1px solid gray;
  }
  .filterUserInput5 {
    width: 120px;
    height: 35px;
    border: 1px solid gray;
  }
  .buttongroup {
    margin-bottom: 50px;
  }

</style>