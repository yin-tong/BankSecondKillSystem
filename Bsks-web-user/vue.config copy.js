module.exports ={
  devServer:{
    port:9528,//如果端口号被占用，会自动提升1
    host:"10.210.122.80",//主机名，127.0.0.1真机0.0.0.0
    https:false,//协议
    open:true,//启动服务时自动打开浏览器访问
    proxy:{
      [process.env.VUE_APP_API_ROOT]:{
        //目标服务器，代理访问到 http://localhost:9528
        target:process.env.VUE_APP_API_SERVISE_PATH,
        //开启代理，在本地会创建一个虚拟服务器，然后发送请求的数据
        //并同时接受请求的数据，这样服务段和服务端进行数据的交互就不会有跨越问题
        changeOrgin:true,//开启代理
        pathRewrite:{
          //会将/dev-api替换为'',也就是/dev-api会移除，
          //如/dev-api/db.json代理到 http://localhost:9528/db.json
          ['^' +process.env.VUE_APP_API_ROOT]:"",
        }
      }
    }
  },
  lintOnSave:false,//关闭格式检查
  productionSourceMap:false//打包时不会生成.map文件，加快打包速度
}