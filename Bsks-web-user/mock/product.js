const Mock = require('mockjs')

let data = []
var cnt = 50

for(var i = 0; i < cnt; i++){
  data.push(
    Mock.mock({
      'id': '@id',
      'name': '@string',
      'price|100-200.2': 0,
      'rate|0-1.2': 0,
      'limitedQuantity|1-5':0,
      'quantity|100000-200000': 0,
      'describe1': '@string',
      'killTime': '@datetime',
      'status|0-1': 0,
      'createTime':'@datetime',
      'updateTime':'@datetime',
      'version': 1,
    })
  )
}

module.exports = [

    // 秒杀
    {
      url: '/product/kill',
      type: 'post',
      response: config => {
        const { accountId } = config.body
        const { productId } = config.body
        const { payPassword } = config.body
        const { number } = config.body
        if(payPassword !== '123'){
          return {
            'code': 20001,
            'message': '密码错误'
          }  
        }
        data.some((item,index) => {
          if (item.id === productId){
            item.quantity = item.quantity - number
          }
        })
        return {
          'code': 20000,
          'message': '抢购成功'
        }  
      }
    },

    // 增加产品
    {
      url: '/product/addProduct',
      type: 'post',
      response: config => {
        const { product } = config.body
        product.id = Mock.Random.id()
        data.unshift(product)
        return {
          'code': 20000,
          'message': '添加商品成功'
        }  
      }
    },

    // 获取所有产品信息
    {
      url: '/product/getProdouctData',
      type: 'get',
      response: config => {
        const { currentPage } = config.query
        const { pageSize } = config.query
        return {
            'code': 20000,
            'data': {
              'totalNumber': data.length,
              'productList': data.filter((item, index) => index < currentPage * pageSize && index >= pageSize * (currentPage - 1))
            }
        }     
      }
    },

    //根据信息过滤查询产品
    {
      url: '/product/getProductDataByFilter',
      type: 'get',
      response: config => {
        const { startTime } = config.query
        const { endTime } = config.query
        const { productName } = config.query
        const { currentPage } = config.query
        const { pageSize } = config.query

        let data1 = []
        if (startTime === ''){
          // 根据产品名称查询
          data1 = data.filter((item, index) => item.name.includes(productName))
        } else {
          if (productName === ''){
            // 根据时间查询
            data1 = data.filter((item, index) => item.killTime > startTime && item.killTime < endTime)
          } else {
            // 根据时间，姓名查询
            data1 = data.filter((item, index) => item.killTime > startTime && item.killTime < endTime && item.name.includes(productName))
          }
        }
        // 分页
        const data2 = data1.filter((item, index) => index < currentPage * pageSize && index >= pageSize * (currentPage - 1))
        return {
            'code': 20000,
            'data': {
              'totalNumber': data2.length,
              'productList': data2
            }
        }     
      }
    },

    // 改变产品状态
    {
      url: '/product/changeProductStatus',
      type: 'get',
      response: config => {
        const { id } = config.query
        const { status } = config.query
        data.some(product => {
          if( product.id === id){
            product.status = Number(status)
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
      url: '/product/deleteProducts',
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
          data = data.filter(product => product.id !== item)
        })
        return {
            'code': 20000,
            'message': '产品删除成功'
        }     
      }
    },

    // 更新产品信息
    {
      url: '/product/updateProduct',
      type: 'post',
      response: config => {
        const { product } = config.body
        for (var i = 0; i<data.length; i++){
          if(data[i].id === product.id){
            data[i] = product
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