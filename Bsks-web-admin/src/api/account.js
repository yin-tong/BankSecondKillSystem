import request from '@/utils/request'
import qs from 'qs'

const ip = 'http://69.231.129.20:8888'

export function login(data) {
  return request({
    // url: ip+'/account/admin/passwordLogin',
    url: ip+ '/account/admin/passwordLogin',
    method: 'post',
    data: qs.stringify(data)
  })
}

export function logout(params) {
  return request({
    // url: ip + '/account/admin/logout',
    url: '/account/admin/logout',
    method: 'get',
    params
  })
}
//获取管理员账户信息
export function getAccountForm(params) {
  return request({
    // url: ip + '/account/admin/getSelfAccount',
    url: '/account/admin/getSelfAccount',
    method: 'get',
    params
  })
}

//获取管理员账户信息
export function updateAccountForm(data) {
  return request({
    // url: ip + '/account/admin/updateSelfAccount',
    url: '/account/admin/updateSelfAccount',
    method: 'post',
    data: qs.stringify(data)
  })
}