import request from '@/utils/request'

const ip = 'http://101.43.122.162:10003'

export function getConsumerData(params) {
    return request({
      //url: '/consumer/getConsumerData',
      // url: ip + '/account/admin/getAccount',
      url: '/account/admin/getAccount',
      method: 'get',
      params
    })
}

export function changeConsumerStatus(params) {
    return request({
      url: '/consumer/changeConsumerStatus',
      method: 'get',
      params
    })
}

export function filterConsumerByName(params){
  return request({
    // url: '/consumer/filterConsumerByName',
    // url: ip + '/account/admin/fuzzyQueryByName',
    url: '/account/admin/fuzzyQueryByName',
    method: 'get',
    params
  })
}


export function filterConsumerByidentityId(params){
  return request({
    // url: '/consumer/filterConsumerByidentityId',
    // url: ip + '/account/admin/fuzzyQueryByIdentityId',
    url: '/account/admin/fuzzyQueryByIdentityId',
    method: 'get',
    params
  })
}

export function filterConsumerByPhone(params){
  return request({
    //url: '/consumer/filterConsumerByPhone',
    // url: ip + '/account/admin/fuzzyQueryByPhone',
    url: '/account/admin/fuzzyQueryByPhone',
    method: 'get',
    params
  })
}
  