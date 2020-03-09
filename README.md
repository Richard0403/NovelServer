# NovelServer
### 一个免费的小说服务器， 基于spring boot， 个人业余所做

客户端目前只做了Android端， 地址[简书]:https://www.jianshu.com "创作你的创作"

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
