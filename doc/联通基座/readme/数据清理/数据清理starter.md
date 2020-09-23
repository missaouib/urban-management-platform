# 数据清理starter [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-stride-starter.git)

## 介绍

> 数据库清理组件主要是实现清理某个固定时间点以前的冗余数据，同时支持清理日志记录功能。
> 主要特性：
> 一.支持不同的数据库类型
> 1. MySQL
> 2. MongoDB
> 
> 二.提供数据清理日志记录功能

## 使用

### 组件

- ##### 引入依赖开始使用组件

```xml
<!--数据清理starter-->
<dependency>
    <groupId>cn.unicom.hlj.snr.talrashas</groupId>
    <artifactId>talrashas-allegiance-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
### 方法

####  1. 启动类
- 使用Mysql数据库时,启动类注解中添加exclude忽略MySQL自动配置

```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
```

####  2. 数据清理方法

```java
    // 引用接口
    @Autowired
    DataClearService dataClearService;
    /**
     *  数据清理方法
     * @param message json字符串
     */
    void dataClear(String message);
```

##### 参数说明

| 名称             | 类型   | 必填 | 描述                                                         |
| :--------------- | ------ | ---- | ------------------------------------------------------------ |
| type             | String | 是   | 数据库类型  `MYSQL` 或 `MONGODB`                             |
| table_name       | String | 是   | 表名 多个用 `,` 拼接  例: tableA,tableB                      |
| time_field_name  | String | 否   | 时间筛选条件的字段名与数据库对应 例如 `create_time`   <br />非必填 为空时表示清理表中全部数据 |
| time_field_value | String | 否   | 时间值   如：`2020-02-02 00:00:00`或 `202002020000000 `<br />非必填 为空时表示清理表中全部数据 |

##### 示例
###### 1. MySQL 请求示例

```json
{
  "time_field_name": "create_time",
  "time_field_value": "2010-08-21 15:26:36",
  "type": "MYSQL",
  "table_name": "snr_fe_dept"
}
```

###### 2. MongoDB 请求示例

```json
{
  "time_field_name": "CREATE_TIME_STR",
  "time_field_value": "20200819000000",
  "type": "MONGODB",
  "table_name": "LOG_MAIL_SENDER_copy1,LOG_MAIL_SENDER_COPY3"
}
```



###  配置

#### 1. 日志 功能配置

- 日志功能默认关闭,修改配置文件开启,支持向MongoDB的 `LOG_DATA_CLEAR`中存入数据清理的日志,需配置MongoDB连接信息 

```yml
snr:
  clear:
    logSwitch: true # true时开启日志存储功能  默认false
    logTypeName: LOG_DATA_CLEAR # 日志存储MongoDB时的表名 LOG_DATA_CLEAR 可配置
```

#### 2. MySQL 数据库配置

```yml
spring:
  datasource: #Mysql连接信息 
    url: jdbc:mysql:replication://127.0.0.1:3306/your_data_name?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
    username: root
    password: password

```

#### 3. MongoDB 数据库配置

```yml
spring:
  mongo: # MongoDB连接信息 
    enable: true
    uri: mongodb://username:password@127.0.0.1:32107/your_data_name?authSource=admin
    database: your_data_name
    username: root
    password: password
    authentication-database: admin
```

## 示例程序

示例程序位于 [基座SpringBoot工程模版](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)