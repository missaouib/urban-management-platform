# 审计日志consumer微服务 [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-guise.git)


## 介绍
> 审计日志consumer微服务是基于Kafka的微服务
> 审计日志consumer微服务已集成审计日志Starter,实现日志保存至MongoDB(需要在配置文件中配置MongoDB数据源)
>
> - 审计日志consumer微服务两种使用方式:
> 1. 基于Config Server使用
> 2. 基于源码使用

## 使用
### 一. 基于Config Server的使用方法

#### 1. 配置文件（例如：firebirds-pinions-consumer-dev.yml）
```yml
sanctr:
  firebireds:
    pinions:
      topic: xxx #需要监听单个Topic时设置(审计日志Starter推送日志消息时的logType)
      flat: true #Mongo存储扁平化存储开关(true即可MongoDB存储扁平化,false反之.未配置默认为true)
spring:
  kafka: #kafka配置
    bootstrap-servers: 127.0.0.1:9092
    consumer:
      group-id: firebirds-pinions-consumer #消费组id
  data:
    mongodb: #mongodb配置
      uri: mongodb://root:password@127.0.0.1:32107/your_data_name?authSource=admin
      database: your_data_name
      username: root
      password: password
      authentication-database: admin
```

#### 2. 执行 maven package

#### 3. 启动jar命令

```cmd
java -jar firebirds-pinions-consumer-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev 
```
### 二.基于源码的使用方法

#### 1. git地址: 
http://192.168.30.124/wangzj129-grouppath/develop/firebirds-pinions-consumer.git

#### 2. resource包中 新建application.yml

```yml
sanctr:
  firebireds:
    pinions:
      #topics: xxx,xxx #监听多个Topic
      topic: xxx #需要监听单个Topic时设置(审计日志Starter推送日志消息时的logType)
      flat: true #Mongo存储扁平化存储开关(true即可MongoDB存储扁平化,false反之.未配置默认为true)
spring:
  kafka: #kafka配置
    bootstrap-servers: 127.0.0.1:9092
    consumer:
      group-id: firebirds-pinions-consumer #消费组id
  data:
    mongodb: #mongodb配置
      uri: mongodb://root:password@127.0.0.1:32107/your_data_name?authSource=admin
      database: your_data_name
      username: root
      password: password
      authentication-database: admin
```

- #### topics属性:
1. 需要监听多个Topic时,设置多个并用","分隔,同时更改KafkaConsumer类中:@KafkaListener
2. topics属性示例
```java
public class KafkaConsumer {
	//监听单个topic时使用.
    //@KafkaListener( topics = "${sanctr.firebireds.pinions.topic}")
	//监听多个topic时使用.
    @KafkaListener( topics = {"#{'${sanctr.firebireds.pinions.topics}'.split(',')}"})
    public void listen(String msg) {
		//Method Context...
    }
}
```
#### 3. 执行 maven package

#### 4. 启动jar命令
```cmd
java -jar firebirds-pinions-consumer-1.0.0-SNAPSHOT.jar
```

### 日志查看
日志会存储到配置好的MongoDB数据库中,topic为MongoDB中的集合名称.

## 示例程序

示例程序位于 [firebirds-pinions-consumer](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-pinions-consumer)
