# 审计日志Web-Aop-Starter [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-pinions-web-aop-starter.git)


## 介绍
> 通过SpringAOP实现获取切面日志的功能组件
> - 使用日志的方式
> 1. 方法级别自定义Log注解作为切面,实现日志存储
> 2. 自定义AOP切面pointcut,实现日志存储
> - 日志存储方式:
> 1. 同步存储
> - 直接存储到MongoDB中
> - MongoDB存储时,提供开关,实现扁平化存储.(默认true)
> 2. 异步存储
> - 日志推送至kafka中,启动审计日志consumer微服务对推送到kafka中的消息进行监听并存储.



## 使用

### 组件

- 引入依赖开始使用组件
```xml
<!--审计日志Web-Aop-Starter -->
<dependency>
  <groupId>cn.unicom.hlj.snr.firebird</groupId>
  <artifactId>firebirds-pinions-web-aop-starter</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

#### 可选组件(必选其一)

##### 1.  同步存储
```xml
    <!--MongoDB数据源-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
        <version>2.1.13.RELEASE</version>
    </dependency>
```
##### 2.  异步存储
```xml
    <!--kafka组件-->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
        <version>2.2.12.RELEASE</version>
    </dependency>
```

### 方法

#### 1. @Log注解 新增日志 
- @Log注解属性说明
1. logType日志类型（存储到MongoDB后此字段值为相应集合名称,默认值:"LOG_BASE_AUDIT"）

2. operationType 操作类型（例:add、delete、update、select等）默认:""

3. logSubType 日志种类 (例:操作日志、告警日志,服务调用等) 默认:""

4. details 描述 (例:aop实现日志存储) 默认:""


```java
import cn.unicom.hlj.snr.firebird.pinions.aop.Log;
public class YourExample {
    //示例
    @Log(logType = "",operationType = "", logSubType = "",details = "")
    public void yourMethod(String reqParam) {
    }
}
```
#### 2. 自定义AOP 新增日志
- 下文配置中:pointcut: execution(* cn.unicom.hlj.snr.firebird.*.*.*(..))属性配置AOP切入点
```java
public class YourExample {
    //示例
    @GetMapping("yourMethod")
    public void yourMethod() {
     System.out.println("--aop日志存储--")
    }
}
````
### 配置
```yml
sanctr:
  firebireds:
    pinions:
      flat: true #Mongo存储扁平化存储开关 默认true
      pointcut: execution(* cn.unicom.hlj.snr.firebird.*.*.*(..)) #框架配置AOP的切入点配置(execution表达式)根据需求自定义配置
spring:
  data:
    mongodb: #mongodb配置
      uri: mongodb://root:password@127.0.0.1:32107/your_data_name?authSource=admin
      database: your_data_name
      username: root
      password: password
      authentication-database: admin
```

## 注意事项

1. 同步存储在MongoDB数据库中查看,@Log(logType="") logType日志类型为集合名称

2. 异步kafka推送消息,启动审计日志consumer微服务,监听推送消息存储到MongoDB数据库,监听topic设置为AuditLogBean中LogType集合名称

3. @Log注解和自定义AOP切面pointcut可同时配置使用.生成的日志将按照实体类AuditLogBean<HttpLog>属性进行封装存储(详情参考下文:日志实体说明)

4. 当pom文件中同时存在kafka和mongo组件时,默认存储到Mongo中

   需要kafka方式时,引入LogService,添加@Qualifier("kafkaLogService")切换

```java
  //示例  
  @Autowired
  @Qualifier("kafkaLogService")
  private LogService logService;
```


## 日志实体说明
### 1. AuditLogBean属性说明

|属性|类型|说明|
|:--------:|:--------:|:--------|
|logId|String|日志ID，通过 UUIDGenerator.instance().generate() 自动生成|
|logType|String|日志类型（日志类型默认值:"LOG_BASE_AUDIT",存储到MongoDB后此字段值为相应集合名称）与注解Log中的logType一致|
|logSubType|String|日志种类：针对日志类型的分类，比如操作日志、告警日志",bbb服务调用,与注解Log中的logSubType一致|
|operationType|String|操作类型，比如登录、退出、增加、删除，与Log注解的operationType一致|
|createUserId|String|日志创建人ID|
|createUserName|String|日志创建人名称|
|createUserAccount|String|日志创建人账号|
|tel|String|日志创建人联系电话|
|createTime|Date|日志创建时间|
|createTimeStr|String|日志创建时间字符串yyyyMMddHHmmss|
|pushTime|Date|日志推送者的推送时间|
|pushTimeStr|String|日志推送者的推送时间字符串yyyyMMddHHmmss|
|consumeTime|Date|日志消费者的消费时间|
|consumeTimeStr|String|日志消费者的消费时间字符串yyyyMMddHHmmss|
|tenantId|String|日志创建人所属租户Id|
|tenantName|String|日志创建人所属租户名称|
|traceId|String|跟踪ID，当多条日志需要相同的标识时使用|
|details|String|日志详情说明，与Log注解的details一致|

### 2. HttpLog属性说明

| 属性 | 类型 | 说明 |
|:--------:|:--------:|:--------|
|platformType|String|平台类型（可以理解为客户端类型）|
|formId|String|表单ID，貌似在工作流中使用|
|formName|String|表单名称，貌似在工作流中使用|
|regIp|String|客户端IP|
|statusCode|Integer|http应答状态码|
|requestPath|String|请求路径|
|qualifiedCassName|String|类的全限定定名|
|methodName|String|调用的方法名|
|queryParams|String|请求参数字符串|
|requestBody|String|请求体字符串，拦截aop中方法的所有参数|
|responseBody|String|应答体字符串|
|responseTime|Date|应答时间|
|responseTimeStr|String|应答时间字符串格式yyyyMMddHHmmss|

## 示例程序
示例程序位于 [基座SpringBoot工程模版](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)