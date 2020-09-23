# 邮件发送consumer微服务 [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-stride-consumer.git)

## 介绍

> 邮件发送consumer是基于Kafka的微服务，已集成邮件发送starter,实现邮件自动发送功能
>
> 两种实现方式:
>
> 一. 基于Config Server的使用方法
>
> 二.基于源码的使用方法
>
> 主要特性：
>
> 一.提供不同的邮件发送方式
>
>  	1. 邮件模板发送方式一（模板通过邮件发送starter维护）
>  	2. 邮件模板发送方式二（模板由项目组提供）
>  	3. 非邮件模板发送方式
>
> 二.支持不同的邮箱配置方式
> 	QQ邮箱
> 	163邮箱
> 三.邮件发送consumer提供日志记录功能
>
> 四.支持将模板内容存入readis功能


## 使用

### 一. 基于Config Server的使用方法

#### 1. 配置文件（例如：xxx.yml）
```yml
snr:
  mail:
    #kafka消息头
    sendTopic: xxx #默认值：MAIL_SENDER
spring: 
  #kafaka配置
  kafka:
    bootstrap-servers: 127.0.0.1:9092 #server地址
    consumer:
      group-id: talrashas-stride-consumer
```

- ##### 邮件配置(参考下文 三.邮件配置)

#### 2. 启动jar命令

```cmd
java -jar talrashas-stride-consumer-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev 
```

### 二.基于源码的使用方法

#### 1. git地址:http://192.168.30.124/wangzj129-grouppath/develop/talrashas-stride-consumer.git

#### 2. resource里 新建application.yml

```yml
snr:
  mail:
    #kafka消息头
    sendTopic: xxx #默认值：MAIL_SENDER
spring:    
  kafka:
    bootstrap-servers: 127.0.0.1:9092 #server地址
    consumer:
      group-id: talrashas-stride-consumer
```

- ##### 邮件配置(参考下文 三.邮件配置)

#### 3. 执行 maven package

#### 4. 启动jar命令

```cmd
java -jar talrashas-stride-consumer-1.0.0-SNAPSHOT.jar
```

### 三.邮件配置(.yml)

#### 1.QQ邮箱

```yml
spring:
  mail:
    default-encoding: UTF-8
    host: smtp.qq.com
    # 这是客户端认证密码  需登录网页版163邮箱开启POP3/SMTP服务时并记录密码
    password: 客户端密码
    username: 发送方邮箱地址
    protocol: smtp
    test-connection: false
    port: 465
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          ssl:
            enable: true
            trust: ${spring.mail.host}
```

#### 2.163邮箱

```yml
spring:
  mail:
    default-encoding: UTF-8
    host: smtp.163.com
    # 这是客户端认证密码  需登录网页版163邮箱开启POP3/SMTP服务时并记录密码
    password: 客户端密码
    username: 发送方邮箱地址
    protocol: smtp
    test-connection: false
    port: 465
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          ssl:
            enable: true
            trust: ${spring.mail.host}
```

#### 3. 日志功能配置

- 日志功能默认关闭,修改配置文件开启
- 支持向MongoDB的 LOG_MAIL_SENDER 中存入发送的邮件日志,需配置MongoDB连接信息 

```yml
snr:
  mail:
  	# true时开启日志存储功能  默认false
    logSwitch: true
    # 日志存储MongoDB时的表名 默认LOG_MAIL_SENDER 可配置
    logTypeName: LOG_MAIL_SENDER_COPY 
spring:
  # MongoDB连接信息 
  mongo:
    enable: true
    uri: mongodb://username:password@192.168.23.20:32107/your_data_name?authSource=admin
    database: your_data_name
    username: username
    password: password
    authentication-database: admin
```

#### 4.readis配置（只针对 `邮件模板发送方式一` 生效）

> 若配置readis自动开启模板内容缓存功能，未配置仍可正常使用邮件发送功能

```yml
spring:
  redis:
    host: 192.168.23.189
    port: 32716
    password: testing
```

###  四.kafka接收消息格式


#### 1.  邮件模板发送方式一（模板通过邮件发送starter维护）

```json
{
  "mail_send_type": "1", 				//值为1 固定 标识发送方式
  "receive_email": "352181756@qq.com",	//接收人邮箱
  "title_name": "这里是标题名",			//邮件标题
  "mail_template_type": "type1",		//邮件模板类型 对应数据库数据对应
  "param1": "模板中要替换的值",
  "param2": "模板中要替换的值"
}
```
#### 2. 邮件模板发送方式二（模板由项目组提供）

```json
{
  "mail_send_type": "2",				//值为2 固定 标识发送方式
  "receive_email": "352181756@qq.com",	//接收人邮箱
  "title_name": "这里是标题名",			//邮件标题
  "mail_template_content": "邮件发送模板",//邮件模板内容 严格遵循beetl模板引擎使用方式
  "param1": "模板中要替换的值",
  "param2": "模板中要替换的值"
}
```

- 说明：参数param1,param2 实际参数名称和值根据模板而定
例：数据库中通过mail_template_type 匹配到的模板内容为： 我是测试模板名称是${name}内容是${value},那么Kafka消息中param1==>name ,param2==>value

#### 3.  非邮件模板发送方式

```json
{
  "mail_send_type": "3", 				//值为3 固定 标识发送方式
  "receive_email": "352181756@qq.com",	//接收人邮箱
  "title_name": "这里是标题名",			//邮件标题
  "mail_content": "这里是要发送的内容",	  //邮件内容
}
```

#### 基于kafka环境可使用命令行进行测试

- MAIL_SENDER:kafka消息头
- message:上述格式

```cmd
./kafka-console-producer.sh --broker-list ip1:port,ip2:port,ip3:port --topic MAIL_SENDER message
```



## 示例程序

示例程序位于 [基座SpringBoot工程模版](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)

