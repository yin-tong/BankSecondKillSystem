import request from '@/utils/request'
// import qs from 'qs'

const ip = 'http://101.43.122.162:10003'

export function getOrderData(params) {
  return request({
    url: '/order/user/getSelfOrder',
    method: 'get',
    params
  })
}

export function deleteOrderData(params) {
  return request({
    url: '/order/user/deleteSelfOrder',
    method: 'get',
    params
  })
}