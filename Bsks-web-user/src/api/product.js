import request from '@/utils/request'
// import qs from 'qs'
const ip = 'http://101.43.122.162:10003'

// export function addProduct(data) {
//   return request({
//     url: '/product/addProduct',
//     method: 'post',
//     data: qs.stringify(data)
//   })
// }

export function getProductData(params) {
  return request({
    url: '/product/user/getProducts',
    method: 'get',
    params
  })
}

// --------------------------------------------

export function kill(params) {
  return request({
    url: '/killServer/kill/' + params,
    method: 'get'
  })
}

export function getKillVoucher(params) {
  return request({
    url: '/killServer/getKillVoucher',
    method: 'get',
    params
  })
}
