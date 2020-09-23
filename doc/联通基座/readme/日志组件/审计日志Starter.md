# 审计日志Starter [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-pinions-starter.git)

## 介绍
> 通过日志组件，快速实现规范化的日志处理方式，从而降低项目组开发中日志使用的重复开发.
> 支持同步存储和异步存储两种方式:
>
> 1. 同步存储
> - 提供LogService接口通过实现类MongoLogServiceImpl来完成审计日志直接保存到mongo中.
> - MongoDB存储时,提供开关,实现扁平化存储.(不配置默认为true)
> 2. 异步存储
> - 提供LogService接口通过实现类KafkaLogServiceImpl来完成将审计日志推送kafka.需启动审计日志consumer微服务配合使用


## 使用

### 组件

- 引入依赖开始使用组件
```xml
<!--审计日志Starter-->
<dependency>
    <groupId>cn.unicom.hlj.snr.firebird</groupId>
    <artifactId>firebirds-pinions-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
#### 可选组件(必选其一)

##### 1. 同步存储方式

```xml
<!-- 引入mongo组件-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
	<version>2.1.13.RELEASE</version>
</dependency>
```

##### 2. 异步存储方式

```xml
<!-- 引入kafka组件-->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
	<version>2.2.12.RELEASE</version>
</dependency>
```

### 方法

- #### 日志保存方法 
```java
    // 引用接口
    @Autowired
    private LogService logService;
    
    // 保存方法 示例
    public void xxxMethod(String reqParam) {
        //实例化一个auditLogBean 其中 XxxLog 自定义实体
        AuditLogBean auditLogBean = new AuditLogBean();
        //必填  存储到Mongo中的表名称也以此命名建议命名"LOG_XXXX" 
        auditLogBean.setLogType("xxx");
        
        //非必填 自定义实体
        XxxLog xxxLog = new XxxLog();
        //自定义相应的属性参数
         xxxLog.setXxx("");
        //body存储自定义实体
        auditLogBean.setBody(xxxLog);
        
                
        //描述, 日志详情说明
        auditLogBean.setDetails("");
        //日志消费者的消费时间
        auditLogBean.setConsumeTime(new Date());
        //日志消费者的消费时间字符串 yyyyMMddHHmmss
        auditLogBean.setConsumeTimeStr("");
        //日志ID，通过 UUIDGenerator.instance().generate() 自动生成
        auditLogBean.setLogId(UUIDGenerator.instance().generate());
        //日志种类：针对日志类型的分类，比如操作日志、告警日志",bbb服务调用
        auditLogBean.setLogSubType("");
        //跟踪ID，当多条日志需要相同的标识时使用
        auditLogBean.setTraceId("");
        //操作类型，比如登录、退出、增加、删除
        auditLogBean.setOperationType("");
        //日志创建人ID
        auditLogBean.setCreateUserId("");
        //日志创建人名称
        auditLogBean.setCreateUserName("");
        //日志创建人帐号
        auditLogBean.setCreateUserAccount("");
        //日志创建人联系电话
        auditLogBean.setTel("");
        //日志创建时间
        auditLogBean.setCreateTime(new Date());
        //日志创建时间字符串 yyyyMMddHHmmss
        auditLogBean.setCreateTimeStr("");
        //日志创建人所属租户ID
        auditLogBean.setTenantId("");
        //日志创建人所属租户名称
        auditLogBean.setTenantName("");
        //日志推送者的推送时间
        auditLogBean.setPushTime(new Date());
        //日志推送者的推送时间字符串 yyyyMMddHHmmss
        auditLogBean.setPushTimeStr("");
        
        // 保存方法
        logService.save(auditLogBean);
    }
```

### 配置
#### 1. 使用MongoDB存储

```yml
sanctr:
  firebireds:
    pinions:
      flat: true #MongoDB存储时,提供开关,实现扁平化存储
spring:
  data: #MongoDB数据源
    mongodb:
      uri: mongodb://root:password@127.0.0.1:32107/your_data_name?authSource=admin
      database: your_data_name
      username: root
      password: password
      authentication-database: admin 
```

#### 2. 使用kafka异步存储
无需配置,启动[审计日志consumer微服务]配合使用.

## 注意事项
1. 同步MongoDB数据库中查看,AuditLogBean中LogType为集合名称

2. 异步kafka推送消息,启动审计日志consumer微服务,监听推送消息存储到MongoDB数据库,监听topic设置为AuditLogBean中LogType集合名称

3. 当pom文件中同时存在mongo和kafka组件时,默认mongo组件配置,日志会直接保存到mongo中,

   当需要kafka异步方式时@Qualifier("kafkaLogService") 注解来切换

```java
    // 引用接口
    @Qualifier("kafkaLogService")
    @Autowired
    private LogService logService;
```
## 示例程序

示例程序位于 [talrashas-brace-template](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)
