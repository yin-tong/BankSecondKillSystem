const Mock = require('mockjs')

let data = []
var cnt = 50

for(var i = 0; i < cnt; i++){
  data.push(
    Mock.mock({
      'id': '@id',
      'accountId':'@id',
      'userName': '@cname',
      'phone|10000000000-19999999999': 0,
      'number|0-10': 0,
      'productName':'@string',
      'payMoney|0-1000.2': 0,
      //'orderCode': 0,
      //'quantity|0-10': 0,
      'createTime':'@datetime',
      'updateTime': '@datetime',
      //'remarks': '@string',
      'status|0-1': 0, 
      'version': 1,
    })
  )
}

module.exports = [
    //获取所有产品信息
    {
      url: '/order/getOrderData',
      type: 'get',
      response: config => {
        const { currentPage } = config.query
        const { pageSize } = config.query
        return {
            'code': 20000,
            'data': {
              'totalNumber': data.length,
              'orderList': data.filter((item, index) => index < currentPage * pageSize && index >= pageSize * (currentPage - 1))
            }
        }     
      }
    },
    // {
    //   url: '/order/getOrderData',
    //   type: 'get',
    //   response: config => { 
    //     const { accountId } = config.query
    //     for(var i =0 ; i< data.length;i++){
    //       if (data[i].accountId == accountId){
    //         return {
    //           'code': 20000,
    //           'data': data[i]
    //         }
    //       }
    //     }
    //     return {
    //       'code': 20010,
    //       'message': ''
    //     }
    //   }
    // },

    //根据信息过滤查询产品
    {
      url: '/order/filterOrderByName',
      type: 'get',
      response: config => {
        // const { startTime } = config.query
        // const { endTime } = config.query
        const { orderName } = config.query
        const { currentPage } = config.query
        const { pageSize } = config.query

        // 分页
        const data1 = data.filter((item, index) => item.userName.includes(orderName))
        const data2 = data1.filter((item, index) => index < currentPage * pageSize && index >= pageSize * (currentPage - 1))
        return {
            'code': 20000,
            'data': {
              'totalNumber': data2.length,
              'orderList': data2
            }
        }     
      }
    },

    // 改变产品状态
    {
      url: '/order/changeOrderStatus',
      type: 'get',
      response: config => {
        const { id } = config.query
        const { status } = config.query
        data.some(order => {
          if( order.id === id){
            order.status = Number(status)
          }
        })
        return {
            'code': 20000,
            'message': '产品更新状态成功'
        }     
      }
    },

    // 根据id批量删除删除产品
    {
      url: '/order/deleteOrderData',
      type: 'get',
      response: config => {
        const { ids } = config.query
        if (!ids) {
          return {
            code: 20002,
            message: '参数不正确'
          }
        }
        ids.forEach((item, index) => {
          data = data.filter(order => order.id !== item)
        })
        return {
            'code': 20000,
            'message': '订单删除成功'
        }     
      }
    },

    // 更新产品信息
    {
      url: '/order/updateOrder',
      type: 'post',
      response: config => {
        const { order } = config.body
        for (var i = 0; i<data.length; i++){
          if(data[i].id === order.id){
            data[i] = order
            break
          }
        }
        return {
            'code': 20000,
            'message': '产品信息更新成功'
        }     
      }
    }
]