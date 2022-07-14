import request from '@/utils/request'
import qs from 'qs'

const ip = 'http://69.231.129.20:8888'

export function updateRegisterForm(data) {
  return request({
    url: '/account/user/register',
    method: 'post',
    data: qs.stringify(data)
  })
}

export function login(data) {
  return request({
    url: ip + '/account/user/passwordLogin',
    method: 'post',
    data: qs.stringify(data)
  })
}

export function getAccountForm(params) {
  return request({
    url: '/account/user/getSelfAccount',
    method: 'get',
    params
  })
}

export function updateAccountForm(data) {
  return request({
    url: '/account/user/updateSelfAccount',
    method: 'post',
    data: qs.stringify(data)
  })
}

export function logout(params) {
  return request({
    url: '/account/user/logout', // /account/user/logout
    method: 'get',
    params
  })
}

