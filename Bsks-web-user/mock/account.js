const Mock = require('mockjs')

let data = [
  {
    phone: '18358893506',
    password: '123',
    identity_id: '666666888888885555',
    name: '张三',
    gender: '',
    address: '',
    accountMoney: '88888',
    token: 'aaa666',
    accountId: '3',
    creatTime: '', // 创建时间
    updateTime: '', // 更新时间
    version: ''
  }
]
// var cnt = 50

// for(var i = 0; i < cnt; i++){
//   data.push(
//     Mock.mock({
//       'accountId': '@id',
//       'name': '@string',
//       'phone|100000-699999': 0,
//       'password|100-999': 0,
//       'identity_id|25-99': 0,
//       'address': '@string',
//       'createTime':'@datetime',
//       'updateTime':'@datetime',
//       'version': 1,
//     })
//   )
// }

module.exports = [

  // 用户创建表单ID获取
  // {
  //   url: '/account/getnewId',
  //   type: 'get',
  //   response: config => {
  //     const id  = 1
  //     return {
  //       'code': 20000,
  //       'data': id
  //     }
  //   }
  // },

  // 用户信息表单创建
  {
    url: '/account/updateRegisterForm',
    type: 'post',
    response: config => {

      // const { registerForm } = config.body
      // registerForm.accountId = 3 // Mock.Random.id()
      // data.unshift(registerForm)
      const { data } = config.body

      for (var i = 0; i < data.length; i++) {
        if (data[i].accountId === data.accountId) {
          data[i] = data
          break
        }
      }
      
      return {
        'code': 20000,
        'message': '注册成功'
      }

    }
  },

  // 用户登录
  {
    url: '/account/login',
    type: 'post',
    response: config => {

      const { phone } = config.body
      const { password } = config.body

      for (var i = 0; i < data.length; i++) {
        if (data[i].phone === phone && data[i].password === password) {
          return {
            'code': 20000,
            'data': data[i]
          }
        } else {
          return {
            'code': 20010,
            'message': '电话号码或密码错误',
          }
        }
      }
    }
  },
  // 用户信息表单获取
  {
    url: '/account/accountForm',
    type: 'get',
    response: config => {
      const { accountId } = config.query
      for (var i = 0; i < data.length; i++) {
        if (data[i].accountId == accountId) {
          return {
            'code': 20000,
            'data': data[i]
          }
        }
      }
      return {
        'code': 20010,
        'message': '没有此账户'
      }
    }
  },
  // 用户信息表单更新
  {
    url: '/account/updateAccountForm',
    type: 'post',
    response: config => {

      const { account } = config.body
      for (var i = 0; i < data.length; i++) {
        if (data[i].id === account.id) {
          data[i] = account
          break
        }
      }
      return {
        'code': 20000,
        'message': '信息更新成功'
      }

    }
  },

  // user logout
  {
    url: '/vue-admin-template/user/logout',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      }
    }
  }
]
