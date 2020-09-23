# 短信发送consumer微服务 [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-down-consumer.git)

## 介绍
> 短信发送consumer是基于Kafka的微服务.提供监听kafka推送消息进行短信发送.
> 短信发送consumer已集成短信发送starter,实现短信发送功能.
>
> - 目前支持以下短信网关
> 1. 大宇网络
> 2. 融合云信
> 3. 三网短信
> 4. 一信通短信
> - 微服务使用方式:
> 1. 基于Config Server使用
> 2. 基于源码使用
> - 记录日志方式:
> 1. kafka日志
> 2. mongodb日志

## 使用

### 一. 基于Config Server的使用方法

#### 1. 配置文件（例如：firebirds-down-consumer-dev.yml）

参考下文: 三.配置

#### 2. 执行 maven package

#### 3. 启动jar命令

```cmd
java -jar firebirds-down-consumer-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev 
```
### 二.基于源码的使用方法

#### 1. git地址:

#### http://192.168.30.124/wangzj129-grouppath/develop/firebirds-down-consumer.git

#### 2. resource里 新建application.yml

参考下文: 三.配置

#### 3. 执行 maven package

#### 4. 启动jar命令

```cmd
java -jar firebirds-down-consumer-1.0.0-SNAPSHOT.jar
```
### 三.配置

#### kafka配置
```yml
sanctr:
  sms:
    sendTopic: XXX #kafka消息头
spring:
  kafka: #kafka配置
    bootstrap-servers: 127.0.0.1:9092
    consumer:
      group-id: firebirds-down-consumer #消费组id
```

#### 日志配置
##### 1. kafka记录日志
```yml
Topic: 
  xxx #自定义kafka消息头
```
##### 2. mongodb记录日志
```yml
spring:
  data: #MongoDB数据源
    mongodb:
      uri: mongodb://root:password@127.0.0.1:32107/your_data_name?authSource=admin
      database: your_data_name
      username: root
      password: password
      authentication-database: admin 
```
#### 短息配置
##### 1. 大宇网络
```yml
sanctr:
    sms: 
        type: dayu
        dayu: #下列属性必填
          appKey: xxx
          appSecret: xxx
          sendUrl: xxx
```

##### 2. 融合云信
```yml
sanctr:
    sms: 
        type: rhyx
        rhyx: #下列属性必填
          cpcode: xxx
          key: xxx
          sendUrl: xxx
```

##### 3. 三网短信
```yml
sanctr:
     sms:
        type: sw
        sw: #下列属性必填
          account: xxx
          msg: xxx
          password: xxx
          sendUrl: xxx
```

##### 4.  一信通短信
```yml
sanctr:
    sms: 
       type: yxt
       yxt: #下列属性必填
           spCode: xxx
           loginName: xxx 
           password: xxx
           sendUrl: xxx
```


###  四.kafka接收消息格式
#### 1. 大宇
```json
    {
        "type":"dayu", 					//大宇网关类型（dayu）
        "numberPhones":"13952554484",	//手机号(多手机号逗号隔开,例"130xx,156xxx")
        "content":"aabb",				//短信内容
        "params":{						//网关参数
            "extend":"aabb",
            "signName":"aabb",
            "template":"aabb",
            "param":{
                "xx":"xx",
                "xx":"xx",
                "xx":"xx"
            }
        }
    }
```

#### 2. 融合云信
```json

    {
	    "type":"rhyx", 					//融合云信网关类型（rhyx）
	    "numberPhones":"18888888888",	//手机号(多手机号逗号隔开,例"130xx,156xxx")
	    "content":"aabb",				//短信内容
	    "params":{ 						//网关参数
	        "extend":"aabb",
	        "template":"aabb",
	        "param":{
	            "msg":"XXX"
	        }
	    }
	}
```
#### 3. 三网网关
```json
    {
	    "type":"sw",					//三网网关类型（sw）
	    "numberPhones":"13952554484",	//手机号
	    "content":"aabb",				//短信内容
	    "params":{}						//网关参数(无)
	}
```
#### 4. 一信通网关
```json
   {
	    "type":"yxt",					//一信通网关类型（yxt）
	    "numberPhones":"13952554484",	//手机号
	    "content":"aabb",				//短信内容
	    "params":{}						//网关参数(无)
	}
```

## 示例程序
示例程序位于 [基座SpringBoot工程模版](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)