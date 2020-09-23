# 邮件发送starter [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-stride-starter.git)

## 介绍

> 邮件发送组件实现通过不同的方式给指定邮箱用户发送消息功能，用户通过相关配置后即可使用。
> 主要特性：
> 一.提供不同的邮件发送方式
>
>  	1. 邮件模板发送方式一（模板通过邮件发送starter维护）
>  	2. 邮件模板发送方式二（模板通过参数自定义）
>  	3. 非邮件模板发送方式
>
> 二.支持不同的邮箱配置方式
> 	QQ邮箱
> 	163邮箱
> 三.提供邮件发送日志记录功能
> 四.提供维护邮件模板的Rest接口
> 五.支持将模板内容存入readis功能

## 使用

### 组件

- 引用依赖开始使用组件

```xml
<!--邮件发送starter-->
<dependency>
    <groupId>cn.unicom.hlj.snr.talrashas</groupId>
    <artifactId>talrashas-stride-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
- 引用以下依赖配合邮件stater组件使用（如果原项目已经引用无需重复引用）

```xml
<!--jdbc-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<!--mybatis-->
 <dependency>
     <groupId>org.mybatis.spring.boot</groupId>
     <artifactId>mybatis-spring-boot-starter</artifactId>
</dependency>
<!--pagehelper-->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
</dependency>
<!--mysql-connector-java 依赖版本问题-->
注意：使用8.0.12版本时，配置文件的  spring.datasource.driverClassName 需要修改为 com.mysql.cj.jdbc.Driver
```
### SQL
-  执行mysqlsql脚本,生成 邮件模板表(snr_ts_mail_template) 

```sql
-- ----------------------------
-- Table structure for snr_ts_mail_template  字符集若不同请自行调整
-- ----------------------------
DROP TABLE IF EXISTS `snr_ts_mail_template`;
CREATE TABLE `snr_ts_mail_template`  (
  `template_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `template_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件模板类型',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮件模板名称',
  `template_context` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮件模板内容',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_user_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(11) NULL DEFAULT 0 COMMENT '用于数据库表的乐观锁',
  `delete_token` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '默认值为1，表示在用',
  PRIMARY KEY (`template_id`) USING BTREE,
  UNIQUE INDEX `UNIQUE_template_type_delete_token`(`template_type`, `delete_token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件模板表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
```
### 邮件发送方式

#### 一. 邮件模板发送 (可对邮件模板维护,参考下文'邮件模板维护')

##### 1. 邮件模板发送方式一（模板通过邮件发送starter维护）

```java
    // 引用接口
	@Autowired
    private MailSenderService mailSenderService;

    /**
     * 发送邮件--通过templateType从数据库中读取模板
     * @param toEmail 接收者邮箱地址 (必填)
     * @param templateType 邮件模板类型 -- 数据查询条件 (必填)
     * @param titleName 邮件标题 (必填)
     * @param parameterMap 模板参数map集合  需包含模板中要替换值  (必填)
     *说明:模板替换方式严格遵循 beetl模板引擎使用方式
     *例： 数据库存储的模板内容为： 我是测试模板我的名称是:${name}
     *   parameterMap 集合中需包含 key为name的值才能进行替换  parameterMap.put("name":"测试模板")
     *   最终发送出去的邮件信息为：我是测试模板我的名称是测试模板
     */
    void sendMailByTemplateType(String toEmail, String 	templateType,String titleName, Map<String, String> parameterMap);

```

##### 2.  邮件模板发送方式二（模板通过参数自定义）

```java
    // 引用接口
	@Autowired
    private MailSenderService mailSenderService;
    /**
     * 发送邮件--模板由项目组提供
     * @param toEmail 接收者邮箱地址  (必填)
     * @param templateContent 模板内容--遵循beetl模板引擎 (必填)
     * @param titleName 邮件标题 (必填)
     * @param parameterMap 模板参数map集合  需包含模板中要替换值  (必填)
     * 说明:模板替换方式严格遵循 beetl模板引擎使用方式
     * 例： 数据库存储的模板内容为： 我是测试模板我的名称是:${name}
     *   parameterMap 集合中需包含 key为name的值才能进行替换  parameterMap.put("name":"测试模板")
     *   最终发送出去的邮件信息为：我是测试模板我的名称是测试模板
     */
    void sendMailByTemplateContent( String toEmail, String templateContent,String titleName, Map<String, String> parameterMap);
```

#### 二. 非邮件模板发送

#####  无需处理直接邮件发送

```java
    // 引用接口
	@Autowired
    private MailSenderService mailSenderService;
   /**
     * 发送邮件--无需处理直接发送
     * @param toEmail 接收者邮箱地址  (必填)
     * @param mailContent 已处理好的邮件内容  (必填)
     * @param titleName 邮件标题  (必填)
     */
    void sendMailByContentStr(String toEmail,String mailContent,String titleName);
```

###  配置

#### 1. 日志功能配置
- 日志功能默认关闭,修改配置文件开启,支持向MongoDB的 `LOG_MAIL_SENDER` 中存入发送的邮件日志,需配置MongoDB连接信息 

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
    username: root
    password: password
    authentication-database: admin
```
#### 2. 163邮箱配置

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

#### 3. qq邮箱配置

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

#### 4. readis配置（只针对 `邮件模板发送方式一` 生效）

> 若配置readis自动开启模板内容缓存功能，未配置仍可正常使用邮件发送功能

```yml
spring:
  redis:
    host: 192.168.23.189
    port: 32716
    password: testing
```

### 邮件模板维护

#### (1) 查询邮件模板接口 
1. 接口类型：POST 

2. 接口地址：/mail-template/list

3. 请求参数

    content-type：application/json;charset=UTF-8
| 名称          | 类型    | 必填 | 描述                                        |
| ------------- | ------- | ---- | ------------------------------------------- |
| template_type | String  | 否   | 模板类型                                    |
| page_size     | Integer | 否   | 每页条数  分页时必填  默认是null 表示不分页 |
| page_num      | Integer | 否   | 当前页数  分页时必填  默认是null 表示不分页 |

4. 返回参数
```json
{
  "ret": "00000",			 				//响应码
  "msg": "执行成功",				  		 //响应描述
  "log_id": 0,								//日志id
  "timestamp": 1597306112,					//时间戳
  "data": [
    {
      "template_id": "8a81809e73e6b47e0173e6b47e130000",//模板id
      "template_type": "Type2",							//模板类型
      "template_name": "mail测试数据2",					 //模板名称
      "template_context":"sdasdasd",					//模板内容
      "create_user_id": "admin",						//创建人id				
      "create_user_name": "admin",						//创建人名称
      "create_time": "2020-8-19 09:45:01",				//创建时间
      "update_user_id": "admin",						//修改人id
      "update_user_name": "admin",						//修改人名称
      "update_time": "2020-8-19 09:45:01",				//修改时间
    }
  ],
  "page_size": 20,		//每页条数  （不分页时 忽略即可）
  "total_elements": 1,	//总条数（不分页时 忽略即可）
  "total_pages": 1,		//总页数（不分页时 忽略即可）
  "page_num": 1,		//当前页数（不分页时 忽略即可）
  "count": 1			//总记录数（不分页时 忽略即可）
}
```

#### (2) 查询邮件模板详情接口
1. 接口类型：GET

2. 接口地址： /mail-template/info/{mailTemplateId}

3. 请求参数: 

   content-type：application/json;charset=UTF-8
   
| 名称           | 类型   | 必填 | 描述   |
| -------------- | ------ | ---- | ------ |
| mailTemplateId | String | 是   | 模板id |

4. 返回参数
```json
{
  "ret": "00000",			 				//响应码
  "msg": "执行成功",				  		 //响应描述
  "log_id": 0,								//日志id
  "timestamp": 1597306112,					//时间戳
  "data": {
    "template_id": "111222333444",			//模板id
    "template_type": "Type1",				//模板类型
    "template_name": "mail测试数据1",  		 //模板名称
    "template_context": "mail测试数据1",  	 //模板内容 严格遵循 beetl模板引擎使用方式  
    "create_user_id": "创建人id",  		 //创建人id
    "create_user_name": "创建人名称",  		//创建人名称
    "create_time": "创建时间",  			//创建时间
    "update_user_id": "修改人id",		 	 //修改人id
    "update_user_name": "修改人名称",  		//修改人名称
    "update_time": "修改时间"				//修改时间
  }
}
```



#### (3) 新增邮件模板接口
1. 接口类型：POST

2. 接口地址： /mail-template/insert

3. 请求参数

   content-type：application/json;charset=UTF-8

| 名称             | 类型   | 必填 | 描述                                    |
| ---------------- | ------ | ---- | --------------------------------------- |
| template_id      | String | 否   | 模板id  选填为空时自动生成uuid          |
| template_type    | String | 是   | 模板类型                                |
| template_name    | String | 是   | 模板名称                                |
| template_context | String | 是   | 模板内容 严格遵循 beetl模板引擎使用方式 |
| create_user_id   | String | 是   | 创建人id                                |
| create_user_name | String | 是   | 创建人名称                              |

4. 返回参数
```json
{
  "ret": "00000",			 		//响应码
  "msg": "执行成功",				  //响应描述
  "log_id": 0,						//日志id
  "timestamp": 1597306112,			//时间戳
  "data": {
    "template_id": "7788994455"		//新增记录的模板id
  }
}
```



#### (4) 修改邮件模板接口
1. 接口类型：POST

2. 接口地址： /mail-template/update

3. 请求参数

   content-type：application/json;charset=UTF-8

| 名称             | 类型   | 必填 | 描述                                    |
| ---------------- | ------ | ---- | --------------------------------------- |
| template_id      | String | 是   | 模板id  选填为空时自动生成uuid          |
| template_type    | String | 否   | 模板类型                                |
| template_name    | String | 否   | 模板名称                                |
| template_context | String | 否   | 模板内容 严格遵循 beetl模板引擎使用方式 |
| update_user_id   | String | 是   | 修改人id                                |
| update_user_name | String | 是   | 修改人名称                              |

4. 返回参数
```json
{
  "ret": "00000",			 //响应码
  "msg": "执行成功",		  //响应描述
  "log_id": 0,				//日志id
  "timestamp": 1597306112,	//时间戳
  "data": {
    "effect_count": 1		//影响的记录数
  }
}
```



#### (5) 删除邮件模板接口
1. 接口类型：GET

2. 接口地址： /mail-template/delete/{mailTemplateId}

3. 请求参数

   content-type：application/json;charset=UTF-8

| 名称           | 类型   | 必填 | 描述   |
| -------------- | ------ | ---- | ------ |
| mailTemplateId | String | 是   | 模板id |

4. 返回参数

```json
{
  "ret": "00000",			 //响应码
  "msg": "执行成功",		  //响应描述
  "log_id": 0,				//日志id
  "timestamp": 1597306112,	//时间戳
  "data": {
    "effect_count": 1		//影响的记录数
  }
}
```
#### 状态码Ret与Msg说明

| Ret码 | Msg说明 |
|:---:|:---:|
|"00000"|"执行成功"|
|"A0001"|"无效的用户输入"|
|"A0002"|"参数缺失,动作执行所需要的数据不存在"|
|"B0001"|"系统执行出错"|

## 示例程序

示例程序位于 [基座SpringBoot工程模版](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)

