import request from '@/utils/request'

const ip = 'http://101.43.122.162:10003'

export function getOrderData(params) {
  return request({
    //url: '/order/getOrderData',
    // url: ip + '/order/admin/getOrder',
    url: '/order/admin/getOrder',
    method: 'get',
    params
  })
}

// export function getOrderDataByFilter(params) {
//   return request({
//     url: '/order/getOrderDataByFilter',
//     method: 'get',
//     params
//   })
// }

export function changeOrderStatus(params) {
  return request({
    url: '/order/changeOrderStatus',
    method: 'get',
    params
  })
}

export function deleteOrderData(params) {
  return request({
    // url: '/order/deleteOrderData',
    // url: ip + '/order/admin/delete',
    url: '/order/admin/delete',
    method: 'get',
    params
  })
}

export function updateOrder(data) {
  return request({
    url: '/order/updateOrder',
    method: 'post',
    data
  })
}

export function filterOrderByName(params){
  return request({
    //url: '/order/filterOrderByName',
    // url: ip + '/order/admin/findByUserName',
    url: '/order/admin/findByUserName',
    method: 'get',
    params
  })
}

export function filterOrderByProductName(params){
  return request({
    //url: '/order/filterOrderByProductName',
    // url: ip + '/order/admin/findByProductName',
    url: '/order/admin/findByProductName',
    method: 'get',
    params
  })
}

export function filterOrderByPhone(params){
  return request({
    // url: ip + '/order/admin/findByPhone',
    url: '/order/admin/findByPhone',
    method: 'get',
    params
  })
}
