<template>
  <div class="line">
    <div class="test">
    <h2 style="text-align: center;">管理员信息</h2>
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item  label="姓名:">
        <el-input v-model="form.name" placeholder="请输入您的姓名" />
      </el-form-item>
      <el-form-item label="性别:">
        <el-select v-model="form.gender" placeholder="请选择您的性别">
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
          <el-option label="保密" value="保密" />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄:">
        <el-input v-model="form.age" placeholder="请输入您的年龄" />
      </el-form-item>
      <el-form-item label="电话号码:">
        <el-input v-model="form.phone" placeholder="请输入您的电话号码" />
      </el-form-item>
      <el-form-item  label="身份证号:">
        <el-input v-model="form.identityId" placeholder="请输入您的身份证号" />
      </el-form-item>
      <el-form-item label="账户余额:">
        <el-input v-model="form.money" placeholder="请输入账户余额" />
      </el-form-item>
      <el-form-item label="所在地区:">
        <el-input v-model="form.address" placeholder="请输入您所在地区（具体到市）" />
      </el-form-item>
      <el-form-item>
        <el-button class="right" type="primary" :disabled="flagchange == 0" @click="updateAccountForm()">确认修改</el-button>
      </el-form-item>
    </el-form>
    </div>
  </div>
</template>

<script>

import { getAccountForm, updateAccountForm } from '@/api/account'
import { getAccountId } from '@/utils/auth'

export default {
  data() {
    return {
      // 用户信息
      //accountId: this.$store.getters.accountId,
      form: {},
      flagchange: 0 // 默认值
    }
  },
  watch: {
    // 监视表单中所有属性的变化
    form: {
      deep: true, // 深度监听
      handler() {
        this.flagchange++
      }
    }
  },
  created() {
    this.getAccountData()
    this.onEditDepart()
  },
  methods: {
    // 获取用户信息
    
    getAccountData() {
      //const accountId = this.data.accountId
      console.log(this.accountId)
      getAccountForm({ accountId: this.$store.getters.accountId }).then(response => { 
        this.form = response.data
      })
    },
    // 初始化检测数据
    onEditDepart() {
      this.flagchange = -1
    },
    // 上传用户更新的数据
    updateAccountForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          updateAccountForm(this.form ).then((response) => {
            this.$message.success(response.message)
            getAccountForm({ accountId: this.$store.getters.accountId }).then(response => { 
              this.form = response.data
            })
          })
        }
      })
    },
    save() {
      // 判断是否有更改
      if (this.flagchange > 0) {
        this.$confirm('是否确定保存本次修改内容?', '提示', { type: 'warning' }).then(() => {
          //account???
          updateAccountForm({ account: this.form }).then(response => {
            this.$message.success(response.message)
            this.getAccountData()
          })
        })
      }
    }
  }
}
</script>

<style scoped>
.line{
  position: absolute;
  /* text-align: center; */
  width: 100%;
}

.right{
  float: right;
  margin-right: 10px;
}

.test {
  margin-left: 25%;
  margin-right: 25%;
}


</style>
