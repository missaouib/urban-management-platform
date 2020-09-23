# CAS或OAuth认证中心-工程模版 [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/sanctr-firebirds-plume-template.git)


## 介绍
> CAS工程模版
> 1. 支持多种登录方式：用户名密码、短信、钉钉、微信
> 2. 支持单机版和集群版本部署架构
> 3. 自动记录密码登录错误次数，最大5次
> 4. 自动记录登录日志
> 5. 基于内存数据库的ticket及token存储
> 6. 基于 REST API 接口动态添加认证客户端
> 7. 自动支持RSA、AES等加密算法，支持SHA256、MD5等摘要算法
> 8. 自动密码强度检测，避免弱口令

## 快速配置 

### CAS和OAuth认证服务配置(以下必须配置)
-   CAS和OAuth认证服务配置

```yml
#spring相关
spring.thymeleaf.cache=false
spring.thymeleaf.mode=HTML
spring.profiles.active=dev

#cas-server配置
cas.server.name=http://localhost:9099
cas.server.prefix=${cas.server.name}/firebirds-plume-template
#oauth协议
cas.authn.oauth.refreshToken.timeToKillInSeconds=2592000
cas.authn.oauth.code.timeToKillInSeconds=300
cas.authn.oauth.code.numberOfUses=1
cas.authn.oauth.accessToken.releaseProtocolAttributes=true
cas.authn.oauth.accessToken.timeToKillInSeconds=7200
cas.authn.oauth.accessToken.maxTimeToLiveInSeconds=28800
cas.authn.oauth.grants.resourceOwner.requireServiceHeader=false
cas.authn.oauth.userProfileViewType=NESTED
#退出后重定向
cas.logout.followServiceRedirects=true
cas.slo.disabled=false
cas.slo.asynchronous=true
cas.tgc.secure=false
cas.warningCookie.secure=false
cas.webflow.session.storage=true

#默认模板
cas.theme.defaultThemeName=default
#开启记住我
cas.ticket.tgt.rememberMe.enabled=true
#tgt用户没有对系统进行操作的过期时间
cas.ticket.tgt.timeToKillInSeconds=7200
#tgt最大生存时间
cas.ticket.tgt.maxTimeToLiveInSeconds=28800
#过期时间配置
cas.ticket.pt.timeToKillInSeconds=1000

```
- 配置数据源

```yml
spring.datasource.url=jdbc:mysql:replication://127.0.0.1:3306/your_data_name?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.log-abandoned=true
spring.datasource.remove-abandoned=true
spring.datasource.remove-abandoned-timeout=60
spring.datasource.initialize=false
spring.datasource.sqlScriptEncoding=UTF-8
spring.datasource.hikari.readOnly=false
spring.datasource.hikari.connectionTimeout=60000
spring.datasource.hikari.idleTimeout=60000
spring.datasource.hikari.validationTimeout=3000
spring.datasource.hikari.maxLifetime=120000
spring.datasource.hikari.loginTimeout=5
spring.datasource.hikari.maximumPoolSize=10
spring.datasource.hikari.minimumIdle=1
```
- 配置动态服务(数据库存储service)

```yml
#create、create-drop、update、validate四种类型可供选择
cas.serviceRegistry.jpa.ddlAuto=update
cas.serviceRegistry.jpa.dialect=org.hibernate.dialect.MySQL57Dialect
cas.serviceRegistry.jpa.driverClass=com.mysql.jdbc.Driver
cas.serviceRegistry.jpa.user=root
cas.serviceRegistry.jpa.password=password
cas.serviceRegistry.jpa.url=jdbc:mysql:replication://127.0.0.1:3306/your_data_name?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
cas.serviceRegistry.jpa.pool.maxSize=18
cas.serviceRegistry.jpa.pool.maxWait=2000
cas.serviceRegistry.jpa.pool.minSize=6
cas.serviceRegistry.jpa.pool.suspension=false
cas.serviceRegistry.jpa.pool.timeoutMillis=1000
#自动扫描服务配置
cas.serviceRegistry.watcherEnabled=true
#120秒扫描一遍
cas.serviceRegistry.schedule.repeatInterval=120000
```

- 用户名密码登录
```yml
#是否开启验证码
sanctr.user-pwd.enable-captcha=true
#用户表
sanctr.user-pwd.user-table=snr_fe_user
#用户表主键字段
sanctr.user-pwd.primary-key-field=user_id
#用户表密码字段
sanctr.user-pwd.password-field=password
#查询用户表条件列
sanctr.user-pwd.condition-fields=user_account, tel
#查询用户表结果列
sanctr.user-pwd.select-fields=user_id, user_account, user_name, tenant_id
#最后一次登录时间
sanctr.user-pwd.lastLogin-time-field=last_reg_time
#登录次数
sanctr.user-pwd.login-times-field=reg_num_times
#最大重试次数
sanctr.user-pwd.max-retry-count=5
#用户锁定时间
sanctr.user-pwd.lock-time=1800

#日志存储类型
sanctr.log-storage=mongo
#日志记录表
sanctr.log-table=log_base
```

### 加解密(按需配置)
- 用于用户名密码登录时密码的传输
```yml
#前后台密码传输时的加解密方式(也可以不配置使用明文传输) base64,rsa,aes
sanctr.gem.moratorium.secureService=base64

#为rsa时需要配置公私钥,密码使用公钥加密再传递到后台
sanctr.gem.moratorium.privateKey=privateKey
sanctr.gem.moratorium.publicKey=publicKey

#为aes时需要配置秘钥与偏移量secureKey：秘钥,ivParameterSpec：偏移量
#密码加密后再传递到后台
sanctr.gem.moratorium.secureKey=secureKey
sanctr.gem.moratorium.ivParameterSpec=ivParameterSpec

#密码解密后得到真正的密码，还需将密码与用户登录账号进行摘要计算 password + userAccount。
#即用户注册时保存到数据库中的密码是实际密码 + 登录账号 进行摘要计算
#可选值md5,sha256(默认为md5)
sanctr.gem.moratorium.digestService=sha256
```

### Redis(按需配置)
#### 1. redis依赖(对于redis存储ticket、session、缓存时必须)

```xml
<!-- 引入redis基础依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version>${springboot.version}</version>
    <exclusions>
        <exclusion>
            <artifactId>logback-classic</artifactId>
            <groupId>ch.qos.logback</groupId>
        </exclusion>
    </exclusions>
</dependency>
```

```yml
#redis配置
#开启redis
spring.redis.enable=true
spring.redis.host=ip
spring.redis.port=port
spring.redis.password=password
spring.redis.timeout=20000
spring.redis.pool.max-active=-1
spring.redis.pool.max-idle=-1
spring.redis.pool.max-wait=-1
spring.redis.pool.min-idle=0
```

#### 2. redis存储ticket依赖(非必需)
```xml
<!-- redis存储ticket依赖-->
<dependency>
    <groupId>org.apereo.cas</groupId>
    <artifactId>cas-server-support-redis-ticket-registry</artifactId>
    <version>${cas.version}</version>
</dependency>
```
```yml
#redis存储ticket配置
cas.ticket.registry.redis.host=ip
cas.ticket.registry.redis.port=port
cas.ticket.registry.redis.password=password
cas.ticket.registry.redis.pool.maxActive=100
cas.ticket.registry.redis.pool.maxIdle=20
cas.ticket.registry.redis.pool.maxWait=3000
```
#### 3. redis存储session依赖(非必需)

```xml
<dependency>
    <groupId>org.apereo.cas</groupId>
    <artifactId>cas-server-webapp-session-redis</artifactId>
    <version>${cas.version}</version>
</dependency>
```

```yml
#redis存储session配置
cas.webflow.autoconfigure=true
cas.webflow.alwaysPauseRedirect=false
cas.webflow.refresh=true
cas.webflow.redirectSameState=false
cas.webflow.session.lockTimeout=30
cas.webflow.session.compress=false
cas.webflow.session.maxConversations=5
spring.session.store-type=redis
```
### 登录日志(按需配置)
- 引入mongodb依赖，记录登录日志

```xml
<!--日志-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
    <version>${springboot.version}</version>
    <exclusions>
        <exclusion>
            <artifactId>logback-classic</artifactId>
            <groupId>ch.qos.logback</groupId>
        </exclusion>
    </exclusions>
</dependency>
```
```yml
#mongodb配置，记录登录日志
spring.mongo.enable=true #开启mongo
spring.data.mongodb.uri=mongodb://root:password@127.0.0.1:3306/your_data_name?authSource=admin
spring.data.mongodb.database=your_data_name
spring.data.mongodb.username=root
spring.data.mongodb.password=password
spring.data.mongodb.authentication-database=admin
```
### 钉钉认证依赖(按需配置)
```xml
<!-- 钉钉认证时必须依赖 -->
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>alibaba-dingtalk-service-sdk</artifactId>
    <version>1.0.1</version>
</dependency>
```

```yml
#钉钉认证配置
#开启钉钉
sanctr.oauth.dingtalk.enable=true
#是否开启钉钉的强认证
sanctr.oauth.dingtalk.authentication=false
#钉钉开发者id
sanctr.oauth.dingtalk.appId=your_appId
#钉钉开发者秘钥
sanctr.oauth.dingtalk.secret=your_secret
#钉钉官方扫码地址
sanctr.oauth.dingtalk.qrUrl=https://oapi.dingtalk.com/connect/qrconnect?response_type=code&scope=snsapi_login
#扫码成功后的回调地址
sanctr.oauth.dingtalk.callBackUrl=http://127.0.0.1:8080/dingtalk/callback
#获取钉钉用户信息地址
sanctr.oauth.dingtalk.userInfoUrl=https://oapi.dingtalk.com/sns/getuserinfo_bycode
#用户与钉钉关联表
sanctr.oauth.dingtalk.user-table=snr_fe_dingtalk_user
#用户与钉钉关联表查询列
sanctr.oauth.dingtalk.select-fields=user_id, nick, openid, unionid
#用户与钉钉关联表条件列
sanctr.oauth.dingtalk.condition-fields=openid, unionid_
```
### 短信认证(按需配置)
- 引入短信认证依赖
```xml
<!-- 短信发送组件 -->
<dependency>
    <groupId>cn.unicom.hlj.snr.firebird</groupId>
    <artifactId>firebirds-down-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!--大鱼短信需要导入-->
<dependency>
    <groupId>com</groupId>
    <artifactId>taobao-sdk-java-auto</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

- 短信认证配置(使用短信登录需同时使用下文的[本地缓存])

```yml
#开启短信认证
sanctr.sms.login.authenticate=true
#短信网关配置 dayu、rhyx、yxt、sw，大鱼、融合云信、一信通、三网。四种只能选择一种
sanctr.sms.type=dayu
#大鱼短信配置
sanctr.sms.dayu.appKey=11111111
sanctr.sms.dayu.appSecret=15ab3ab2f7e9d8bc56dBBBB3d8c92e93
sanctr.sms.dayu.sendUrl=http://gw.api.taobao.com/router/rest

#融合云信短信配置
#sanctr.sms.rhyx.cpcode=11111111
#sanctr.sms.rhyx.key=9d433888301ad7BBBBf63ebe1a12538f
#sanctr.sms.rhyx.sendUrl=http://106.74.72.240:8000/umcinterface/sendtempletmsg

#一信通短信配置
#sanctr.sms.rhyx.spCode=11111111
#sanctr.sms.rhyx.loginName=admin
#sanctr.sms.rhyx.password=vGDkGsAAAAGfn0
#sanctr.sms.rhyx.sendUrl=https://api.ums86.com:9600/sms/Api/Send.do

#三网短信配置
#sanctr.sms.sw.account=11111111
#sanctr.sms.sw.msg=admin
#sanctr.sms.sw.password=vGDkGsAAAAGfn0
#sanctr.sms.sw.sendUrl=https://XXX/

#短信登录配置(以下参数短信网关中有就填写)
#签名
sanctr.sms.login.sign_name=百度
#模板
sanctr.sms.login.template=SMS_20200731
#参数名
sanctr.sms.login.key=code
#正文内容
sanctr.sms.login.send_content=您的验证码为${code}
#拓展
sanctr.sms.login.extend=0001
```
### 本地缓存(按需配置)
- 缓存配置

```xml
<dependency>
    <groupId>net.oschina.j2cache</groupId>
    <artifactId>j2cache-core</artifactId>
    <version>2.8.0-release</version>
</dependency>
<dependency>
    <groupId>net.oschina.j2cache</groupId>
    <artifactId>j2cache-spring-boot-starter</artifactId>
    <version>2.8.0-release</version>
</dependency>
```

```yml
#开启缓存
invigorate.enable=true
#100存储最大数量, 缓存过期时间单位可设置m,s,h,d 时分秒天 default为默认设置，可新增自定义配置
caffeine.region.default=100, 600s
#重发短信验证码的时间间隔
caffeine.region.sendSmsCode=100, 60s
#短信验证码的有效时间
caffeine.region.smsCode=100, 300s
#开启spring cache支持
j2cache.open-spring-cache=true
#是否允许null值缓存
j2cache.allow-null-values=true
#是否启用null对象缓存
j2cache.default_cache_null_object=true
#序列化方式json
j2cache.serialization=json
#缓存清除模式
#active:主动清除，二级缓存过期主动通知各节点清除，优点在于所有节点可以同时收到缓存清除
#passive:被动清除，一级缓存过期进行通知各节点清除一二级缓存
#blend:两种模式一起运作，对于各个节点缓存准确性以及及时性要求高的可以使用（推荐使用前面两种模式中一种）
j2cache.cache-clean-mode=blend
#none关闭消息通知
j2cache.broadcast=none
#一级缓存类型
j2cache.L1.provider_class=caffeine
```
- 二级缓存redis配置

```yml
#开启二级缓存
j2cache.l2-cache-open=true
#redis客户端类型
j2cache.redis-client=jedis
#二级缓存类型
j2cache.L2.provider_class=net.oschina.j2cache.cache.support.redis.SpringRedisProvider
#用于获取redis配置
j2cache.L2.config_section=redis
#lettuce缓存命名空间
redis.namespace=cache
redis.mode=single
#基于region+_key存储
redis.storage=generic
redis.hosts=192.168.23.189:32716
redis.password=password
redis.database=0
#redis : 连接单个 Redis 服务
#rediss : 使用 SSH 连接单个 Redis 服务
#redis-sentinel : 连接到 Redis Sentinel 集群（结合 sentinelMasterId 进行使用）
#redis-cluster : 连接到 Redis Cluster
#开启redis的ssl
redis.ssl=true
```

## 使用

### 注:

### 1. 以下接口无界面操作,建议使用postman工具或其他方式调用接口

### 2.启动完成,访问http://localhost:9099/firebirds-plume-template/login

用户名密码数据库中查看表:snr_fe_user

动态管理服务查看表:RegexRegisteredService

#### (一) 动态管理服务

##### (1) 新增服务

1. 接口类型：POST
2. 接口地址： /service/insert
3. 请求参数
   
    | 参数 | 类型 | 必填 | 描述 | 备注 |
    | ---- | ----| ---- | ---- | ---- |
    | name | String | 是 | 服务名称 | 服务名称 |
    | service_id | String | 是 | 唯一的服务id | 1.正则表达 2.服务地址 |
    | expression_type | String | 否 | 服务类型 | 默认为regex,也可设置为oauth |
    | theme | String | 否 | 模板 | 模板位置resources-templates          默认default |
    | supported_grant_types | HashSet<String\> | 否 | 许可类型 | expressionType为oauth时必填,取值范围authorization_code, password, client_credentials, refresh_token |
    | supported_response_types | HashSet<String\> | 否 | 响应类型 | expressionType为oauth时必填,取值范围token, code |



(1)CAS参数示例

```json
{
  "service_id": "http://localhost:8112.*",
  "name": "测试2"
}
```

(2)OAuth参数示例

```json
{
  "service_id": "http://localhost:8112.*",
  "name": "测试2",
  "expression_type": "oauth",
  "supported_grant_types": ["authorization_code", "password", "client_credentials", "refresh_token"],
  "supported_response_types": ["token", "code"]
}
```
4. 返回参数
```json
{
    "ret": "00000",
    "msg": "执行成功",
    "log_id": 0,
    "timestamp": 1594962910,
    "data": {
        "id": 1,
        "service_id": "^(https|http|imaps)://.*",
        "name": "test",
        "theme": "default",
        "client_id": "3716504419831808",
        "client_secret": "8b9ddacd19cc46b296c513ba205f1dae",
        "supported_grant_types": [
            "client_credentials",
            "authorization_code"
        ],
        "supported_response_types": [
            "code",
            "token"
        ]
    }
}
```

##### (2) 分页查询服务
1. 接口类型：POST
2. 接口地址： /service/list
3. 请求参数
   
    | 参数 | 类型 | 必填 | 描述 | 备注 |
    | ---- | ----| ---- | ---- | ---- |
    | page_num | int | 是 | 页码 | 从1开始，默认为1  |
    | page_size | int | 是 | 每页条数 | 默认为20 |
    | id | int | 否 | 唯一id |  |
    | service_id | String | 否 |  唯一serviceId |   |
    | client_id | String | 否 |  唯一clientId |   |

4. 返回参数
```json
{
    "ret": "00000",
    "msg": "执行成功",
    "log_id": 0,
    "timestamp": 1594963532,
    "data": [
        {
            "id": 10,
            "service_id": "^(https|http)://.*",
            "name": "http",
            "theme": "default",
            "client_id": "1703777799245824",
            "client_secret": "e4af589d298040a482d3d64d3a5b4776",
            "supported_grant_types": [
                "refresh_token",
                "password",
                "client_credentials",
                "authorization_code"
            ],
            "supported_response_types": [
                "code",
                "token"
            ]
        },
        {
            "id": 11,
            "service_id": "^(https|http|imaps)://.*",
            "name": "test",
            "theme": "default",
            "client_id": "3716504419831808",
            "client_secret": "8b9ddacd19cc46b296c513ba205f1dae",
            "supported_grant_types": [
                "client_credentials",
                "authorization_code"
            ],
            "supported_response_types": [
                "code",
                "token"
            ]
        }
    ],
    "page_size": 20,
    "total_elements": 2,
    "total_pages": 1,
    "page_num": 1,
    "count": 2
}
```

##### (3) 通过id查询服务
1. 接口类型：POST
2. 接口地址： /service/info
3. 请求参数
   
    | 参数 | 类型 | 必填 | 描述 | 备注 |
    | ---- | ----| ---- | ---- | ---- |
    | id | int | 是 | 唯一id |  以json的格式传参 |

4. 返回参数
```json
{
    "ret": "00000",
    "msg": "执行成功",
    "log_id": 0,
    "timestamp": 1594964042,
    "data": {
        "id": 10,
        "service_id": "^(https|http)://.*",
        "name": "http",
        "theme": "default",
        "client_id": "1703777799245824",
        "client_secret": "e4af589d298040a482d3d64d3a5b4776",
        "supported_grant_types": [
            "refresh_token",
            "password",
            "client_credentials",
            "authorization_code"
        ],
        "supported_response_types": [
            "code",
            "token"
        ]
    }
}
```

##### (4) 通过serviceId查询服务
1. 接口类型：POST
2. 接口地址： /service/info-by-service-id
3. 请求参数
   
    | 参数 | 类型 | 必填 | 描述 | 备注 |
    | ---- | ----| ---- | ---- | ---- |
    | service_id | String | 是 | 唯一serviceId |  以json的格式传参 |

4. 返回参数
```json
{
    "ret": "00000",
    "msg": "执行成功",
    "log_id": 0,
    "timestamp": 1594964042,
    "data": {
        "id": 10,
        "service_id": "^(https|http)://.*",
        "name": "http",
        "theme": "default",
        "client_id": "1703777799245824",
        "client_secret": "e4af589d298040a482d3d64d3a5b4776",
        "supported_grant_types": [
            "refresh_token",
            "password",
            "client_credentials",
            "authorization_code"
        ],
        "supported_response_types": [
            "code",
            "token"
        ]
    }
}
```

##### (5) 通过id删除服务
1. 接口类型：POST
2. 接口地址： /service/delete
3. 请求参数
   
    | 参数 | 类型 | 必填 | 描述 | 备注 |
    | ---- | ----| ---- | ---- | ---- |
    | id | int | 是 | 唯一id |  以json的格式传参 |

4. 返回参数
```json
{
    "ret": "00000",
    "msg": "执行成功",
    "log_id": 0,
    "timestamp": 1594964420
}
```

##### (6) 通过serviceId删除服务
1. 接口类型：POST
2. 接口地址： /service/delete-by-service-id
3. 请求参数
   
    | 参数 | 类型 | 必填 | 描述 | 备注 |
    | ---- | ----| ---- | ---- | ---- |
    | service_id | String | 是 | 唯一serviceId |  以json的格式传参 |

4. 返回参数
```json
{
    "ret": "00000",
    "msg": "执行成功",
    "log_id": 0,
    "timestamp": 1594964420
}
```

##### (7)更新服务信息(1.0)
1. 接口类型：POST
2. 接口地址： /service/update
3. 请求参数
   
    | 参数 | 类型 | 必填 | 描述 | 备注 |
    | ---- | ----| ---- | ---- | ---- |
    | id | int | 是 | 唯一id |  以json的格式传参 |

4. 返回参数
```json
{
    "ret": "00000",
    "msg": "执行成功",
    "log_id": 0,
    "timestamp": 1594964634
}
```
#### (二) 钉钉登录

##### (1) 访问钉钉
1. 接口类型：GET
2. 接口地址： /dingtalk/qr-code
3. 请求参数：

    | 参数 | 类型 | 必填 | 描述 | 备注 |
    | ---- | ----| ---- | ---- | ---- |
    | source | String | 是 |  登录页地址信息 |  window.location.href 获取 |
    | execution | String | 是 |  流程执行器key |  document.getElementsByName("execution")[0].value获取 |
注：调用接口需先配置钉钉相关信息，调用成功后会重定向到钉钉扫码页面，授权后回调到自定义的地址。

##### (2) 钉钉授权回调
1. 接口类型：GET
2. 接口地址： /dingtalk/callback
注：根据钉钉回调的code以及扫码登录时的传参，回调钉钉callBack页面并自动提交表单。


#### (三) 短信登录

##### (1) 发送短信验证码
1. 接口类型：POST
2. 接口地址： /send-login-sms-code
3. 请求参数：

    | 参数 | 类型 | 必填 | 描述 | 备注 |
    | ---- | ----| ---- | ---- | ---- |
    | phoneNo | String | 是 |  手机号 |  |
4. 返回参数
```json
{
    "ret": "00000",
    "msg": "执行成功",
    "log_id": 0,
    "timestamp": 1594964634
}
```


## 注意事项
1. 如果不使用配置中心可将bootstrap.properties相关配置文件删除,自行创建application.properties进行配置

2. 当配置spring.datasource与cas.serviceRegistry.jpa两个数据源

   必须配置spring.datasource.initialize=false

3. jpa自动建表registeredserviceimpl_props失败。

   检查数据库 show global variables like "innodb_large_prefix"; 

   设置 set global innodb_large_prefix = ON;

4. 关闭redis二级缓存设置

   j2cache.l2-cache-open=false 

   j2cache.L2.provider_class=none
   
5. 使用短信认证时必须设置

   gem.invigorating.enable=true 

   sanctr.sms.login.authenticate=true
   
6. 登录时使用了jquery.js和bootstrapValidator.min.js，可根据具体需要引入不同版本的jquery.js和bootstrapValidator.min.js

7. 使用nginx配置集群server
  
   如果未配置使用redis存储session，可以将upstream的策略设置为ip_hash