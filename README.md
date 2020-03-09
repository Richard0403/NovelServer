# NovelServer
### 一个免费的小说服务器， 基于spring boot， 个人业余所做

客户端目前只做了Android端，目前仍在线上运行 地址[悠然读书]:http://reader.ifinder.cc

客户端项目地址 https://github.com/Richard0403/NovelAndroid

**至于书籍的数据，我这里有很多，差不多有8G的内容（可以导出sql文件）， 如果你需要， 请联系我 QQ985507966**


## 功能
  1. 小说阅读
  2. 小说分类
  3. 评论留言区
  4. 基本的登录注册
  5. 阅读时长换积分
  6. 积分兑换
  7. 管理员，修改分类
  
## 功能很简单，但基本的小说业务功能都在，业余学习spring boot 所做

## 要运行需要配置如下信息
#### novel目录下的 application.properties
```
#七牛key
qiniu.accessKey = /** 七牛的 accessKey **/
qiniu.secretKey = /** 七牛的 secretKey **/
qiniu.bucket = /** 七牛的 bucket **/
qiniu.link = /** 七牛的 link **/

#极光推送
jiguang.appKey = /** 极光的 appKey **/
jiguang.secret = /** 极光的 secret **/

#数据库相关信息
spring.datasource.driverClassName = com.mysql.cj.jdbc.Driver
spring.datasource.url = jdbc:mysql://xxx.cn:3306/DataBaseName?autoReconnect=true&useSSL=true&useUnicode=true&characterEncoding=utf-8
spring.datasource.username = /** 数据库账号 **/
spring.datasource.password = /** 数据库密码 **/
```

正常运行后，会自动自动新建数据表， 但是user_role表需要手动创建角色
1	ROLE_USER
2	ROLE_ADMIN
