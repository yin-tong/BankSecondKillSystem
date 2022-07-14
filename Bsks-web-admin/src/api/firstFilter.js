// export function getInputValue(params){
//     return request({
//       url: '/firstFilter/getInputValue',
//       method: 'get',
//       params
//     })
//   }
//服务于menu2
import request from '@/utils/request'
import qs from 'qs'

const ip = 'http://101.43.122.162:10003'

export function getFirstFilterData(params) {
    return request({
      //url: '/firstFilter/getFirstFilterData',
      // url: ip + '/firstFilter/firstFilterRecord/findAll',
      url: '/firstFilter/firstFilterRecord/findAll',
      method: 'get',
      params
    })
}

export function changeFirstFilterStatus(params) {
    return request({
      url: '/firstFilter/changeFirstFilterStatus',
      method: 'get',
      params
    })
}
  
// export function deleteFirstFilterData(params){
//     return request({
//       url: '/firstFilter/deleteFirstFilterData',
//       method: 'get',
//       params
//     })
// }

export function firstFilterByUpdateTime(params){
  return request({
    // url: '/firstFilter/firstFilterByUpdateTime',
    // url: ip + '/firstFilter/firstFilterRecord/findByDate',
    url: '/firstFilter/firstFilterRecord/findByDate',
    method: 'get',
    params
  })
}


export function firstFilterByidentityId(params){
  return request({
    // url: '/firstFilter/firstFilterByidentityId',
    // url: ip + '/firstFilter/firstFilterRecord/findByIdentityId',
    url: '/firstFilter/firstFilterRecord/findByIdentityId',
    method: 'get',
    params
  })
}

export function firstFilterByidentityIdAndTime(params){
  return request({
    // url: '/firstFilter/firstFilterByidentityId',
    // url: ip + '/firstFilter/firstFilterRecord/findByIdentityIdDate',
    url: '/firstFilter/firstFilterRecord/findByIdentityIdDate',
    method: 'get',
    params
  })
}

export function getFilterRulePage(params) {
  return request({
    //url: '/firstFilter/getFirstFilterData',
    // url: ip + '/firstFilter/firstFilterRule/get',
    url: '/firstFilter/firstFilterRule/get',
    method: 'get',
    params
  })
}

export function getFilterRule(params){
  return request({
    // url: '/firstFilter/firstFilterByidentityId',
    // url: ip + '/firstFilter/firstFilterRule/get',
    url: '/firstFilter/firstFilterRule/get',
    method: 'get',
    params
  })
}

export function updateFilterRule(data){
  return request({
    // url: '/firstFilter/firstFilterByidentityId',
    // url: ip + '/firstFilter/firstFilterRule/update',
    url: '/firstFilter/firstFilterRule/update',
    method: 'post',
    data: qs.stringify(data)
  })
}

  