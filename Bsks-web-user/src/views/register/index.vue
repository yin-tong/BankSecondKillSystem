<template>
  <div>
    <div class="test">
      <h2 style="text-align: center;">注册</h2>
      <el-form ref="registerForm" :model="registerForm" :rules="registerFormRules" label-width="120px">
        <el-form-item label="电话号码:" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入您的电话号码" />
        </el-form-item>
        <el-form-item label="密码:" prop="password">
          <el-input
            v-model="registerForm.password"
            placeholder="请设置您的密码密码"
            type="password"
          />
        </el-form-item>
        <!-- 确认密码框 -->
        <el-form-item label="确认密码:" prop="confirm_pwd">
          <el-input
            v-model="registerForm.confirm_pwd"
            placeholder="请再次确认密码"
            type="password"
          />
        </el-form-item>
        <el-form-item label="姓名:" prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入您的姓名" />
        </el-form-item>
        <el-form-item label="性别:">
          <el-select v-model="registerForm.gender" placeholder="请输入您的性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
            <el-option label="保密" value="保密" />
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号:" prop="identityId">
          <el-input v-model="registerForm.identityId" placeholder="请输入您的身份证号" />
        </el-form-item>
        <el-form-item label="年龄:" prop="age">
          <el-input v-model="registerForm.age" placeholder="请输入您的年龄" />
        </el-form-item>
        <el-form-item label="所在地区:">
          <el-input v-model="registerForm.address" placeholder="请输入所在地区（具体到市）" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-button type="primary" style="width:100%" @click="cancel()">取消</el-button>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" style="width:100%" @click="updateRegisterForm()">确认注册</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>

import { updateRegisterForm } from '@/api/account'

export default {
  data() {
    const validatePhone = (rule, value, callback) => {
      const regexMobile = /^((\+\d{2}-)?0\d{2,3}-\d{7,8})|((\+\d{2}-)?(\d{2,3}-)?([1][3,4,5,6,7,8,9][0-9]\d{8}))$/
      if (regexMobile.test(value)) {
        callback()
      } else {
        callback(new Error('请输入正确的手机号码'))
      }
    }
    const validateName = (rule, value, callback) => {
      if (!value) {
        callback(new Error('姓名不能为空'))
      } else {
        callback()
      }
    }
    const validateIdentityId = (rule, value, callback) => {
      if (!value) {
        callback(new Error('身份证号不能为空'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error('密码不能为空'))
      } else {
        callback()
      }
    }
    const validateConfirm_pwd = (rule, value, callback) => {
      if (!value) {
        callback(new Error('密码不能为空'))
      } else if (value !== this.registerForm.password) {
        callback(new Error('输入的密码不一致'))
      } else {
        callback()
      }
    }
    const validateAge = (rule, value, callback) => {
      if (!value) {
        callback(new Error('年龄不能为空'))
      } else {
        callback()
      }
    }
    return {
      // 用户信息
      registerForm: {
        accountId: '', // 用户ID
        phone: '', // 电话号码
        password: '', // 密码
        confirm_pwd: '', // 确认密码
        name: '', // 姓名
        roleName: '', // 角色名
        identityId: '', // 身份证号码
        gender: '', // 性别
        address: '', // 地址
        age: ''
      },
      change: 0, // 默认值
      registerFormRules: {
        phone: [{ required: true, trigger: 'blur', validator: validatePhone }],
        name: [{ required: true, trigger: 'blur', validator: validateName }],
        identityId: [{ required: true, trigger: 'blur', validator: validateIdentityId }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        confirm_pwd: [{ required: true, trigger: 'blur', validator: validateConfirm_pwd }],
        age: [{ required: true, trigger: 'blur', validator: validateAge }]
      }
    }
  },
  methods: {
    // 上传用户注册的数据
    cancel() {
      this.$router.push({
        path: '/login'
      })
    },
    updateRegisterForm() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          updateRegisterForm(this.registerForm).then((response) => {
            this.$message.success(response.message)
            // console.log('注册成功')
            this.cancel()
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}

.test{
  margin-left: 25%;
  margin-right: 25%;
}
</style>
