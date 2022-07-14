import request from '@/utils/request'
import qs from 'qs'
const ip = 'http://101.43.122.162:10003'

export function addProduct(data) {
  return request({
    // url: ip+'/product/admin/addProduct',
    url: '/product/admin/addProduct',
    method: 'post',
    data: qs.stringify(data)
  })
}

export function getProductData(params) {
  return request({
    // url: ip+'/product/admin/getProduct',
    url: '/product/admin/getProduct',
    method: 'get',
    params
  })
}

export function filterProductByName(params) {
  return request({
    //url: '/product/filterProductByName',
    // url: ip + '/product/admin/findByName',
    url: '/product/admin/findByName',
    method: 'get',
    params
  })
}

export function filterProductByKillTime(params) {
  return request({
    //url: '/product/filterProductByKillTime',
    // url: ip + '/product/admin/findByDate',
    url: '/product/admin/findByDate',
    method: 'get',
    params
  })
}

export function filterProductByKillTimeAndName(params) {
  return request({
    //url: '/product/filterProductByKillTimeAndName',
    // url: ip + '/product/admin/findByDateAndName',
    url: '/product/admin/findByDateAndName',
    method: 'get',
    params
  })
}

export function changeProductStatus(params) {
  return request({
    // url: ip + '/product/admin/updateProductStatus',
    url: '/product/admin/updateProductStatus',
    method: 'get',
    params
  })
}

export function deleteProducts(params) {
  return request({
    // url: '/product/deleteProducts',
    // url: ip + '/product/admin/deleteProduct',
    url: '/product/admin/deleteProduct',
    method: 'get',
    params
  })
}

export function updateProduct(data) {
  return request({
    // url: '/product/updateProduct',
    // url: ip + '/product/admin/updateProduct',
    url: '/product/admin/updateProduct',
    method: 'post',
    data: qs.stringify(data)
    // data: qs.stringify(data)
  })
}
