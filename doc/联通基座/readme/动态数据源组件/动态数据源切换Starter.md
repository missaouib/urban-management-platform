# 动态数据源切换Starter [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-tarsi-starter.git)

## 介绍
> 动态数据源切换Starter是一款便捷动态数据源切换组件.使用者可以通过对组件的引用,来实现动态切换多数据源,运行时添加数据源的功能,以支持多数据来源的场景,丰富产品能力
> - 提供运行时添加、删除、修改数据源的功能<br>
> 提供DynamicDatasourceService接口,可以动态添加、删除、修改数据源,需提前在配置文件中,添加默认数据源配置,并创建相应的数据源配置存储表.
> - 提供添加自定义注解和请求header中添加参数"Datasource-Code"两种方式切换数据源:<br>
> 数据源切换基于AOP实现,所以,需要先在配置文件中设置切面,然后使用以下两种方式即可实现动态切换数据源.
> 1. 自定义注解方式<br>
> 提供@Datasource注解,作用于类、方法中,通过定义注解中datasourceCode的值来实现切换数据源.
> 2. 请求header中添加参数方式<br>
> 在请求header中增加参数"Datasource-Code",通过定义"Datasource-Code"的值来实现切换数据源.


## 使用

### 组件

- 引入依赖开始使用组件
```xml
<dependencies>
    <dependency>
        <groupId>cn.unicom.hlj.snr.firebird</groupId>
        <artifactId>firebirds-tarsi-starter</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    
    <!-- hikari连接池需要connector版本支持 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.12</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```
### SQL
- 执行mysql脚本,创建 `数据源表 snr_ft_datasource`,`snr_ft_ds_attr`

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for snr_ft_datasource
-- ----------------------------
DROP TABLE IF EXISTS `snr_ft_datasource`;
CREATE TABLE `snr_ft_datasource`  (
  `datasource_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `datasource_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库code',
  `datasource_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改用户id',
  `update_user_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改用户名',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_user_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户名',
  `connection_pool` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接池名称，比如 hikari ，目前只支持 hikari ',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`datasource_id`) USING BTREE,
  UNIQUE INDEX `datasource_code`(`datasource_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据源表，存储每个数据源的属性信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for snr_ft_ds_attr
-- ----------------------------
DROP TABLE IF EXISTS `snr_ft_ds_attr`;
CREATE TABLE `snr_ft_ds_attr`  (
  `attr_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `datasource_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `attr_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr_value` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_user_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_user_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`attr_id`) USING BTREE,
  INDEX `FK_fk_datasource_attr`(`datasource_id`) USING BTREE,
  CONSTRAINT `FK_fk_datasource_attr` FOREIGN KEY (`datasource_id`) REFERENCES `snr_ft_datasource` (`datasource_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
```
### 方法

#### 一.动态数据源维护

##### 1.添加数据源

```java
public class DynamicDatasourceServiceTest {

    @Autowired
    private DynamicDatasourceService dynamicDatasourceService;

    public void addDatasourceTest() {
		
        //实例化一个数据源实体DatasourceConfigBO
        DatasourceConfigBO datasourceConfigBO = new DatasourceConfigBO();
        //唯一约束.数据源代码(切换数据源关键字)
        datasourceConfigBO.setDatasourceCode("dynamic_test");
        //修改用户id
        datasourceConfigBO.setUpdateUserId("8a81809373bd53600173bd5360160001");
        //修改用户名
        datasourceConfigBO.setUpdateUserName("datasourceManager-add");
        //创建用户id
        datasourceConfigBO.setCreateUserId("8a81809373bd53600173bd5360160001");
        //创建用户id
        datasourceConfigBO.setCreateUserName("datasourceManager-add");
        //数据库名称
        datasourceConfigBO.setDatasourceName("dynamic_test");
        //连接池,目前只支持,填写"HikariDataSource"即可
        datasourceConfigBO.setConnectionPool("HikariDataSource");
        //描述
        datasourceConfigBO.setDescription("动态数据源切换组件测试数据库");
		
        //Hikari连接池配置实例(填写需要配置的属性,如果不需配置的属性可不填)
        HikariConfigBO hikariConfigBO = new HikariConfigBO();
        //数据库地址url(必填)
        hikariConfigBO.setJdbcUrl("jdbc:mysql://xxx.xxx.xx.xxx:xxxx/dynamic_test?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&transformedBitIsBoolean=true");
        //数据库用户名(必填)
        hikariConfigBO.setUsername("xxxx");
        //数据库密码(必填)
        hikariConfigBO.setPassword("xxxxxxxxxx");
        //HikariCP将尝试通过仅基于jdbcUrl的DriverManager解析驱动程序，但对于一些较旧的驱动程序，还必须指定driverClassName
        hikariConfigBO.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        
        //从连接池获取连接时最大等待时间,单位毫秒,默认值30秒,至少250ms
        hikariConfigBO.setConnectionTimeout("60000");
        //连接可以在池中的最大闲置时间,单位毫秒,至少10s,默认10分钟,0表示永不超时,该配置不能大于maxLifetime
        hikariConfigBO.setIdleTimeout("60000");
        //连接最大存活时间,单位毫秒,最小允许值30000ms,默认30分钟,推荐设置的比数据库的wait_timeout少几分钟
        hikariConfigBO.setMaxLifetime("120000");
        //连接池中可以保留连接的最大数量,比如:100,则连接池中的连接不能超过100个
        hikariConfigBO.setMaximumPoolSize("10");
        //最小空闲连接数量,默认10个,也就是说,连接池中最多可以保留10个空闲的连接,多的会被关闭
        hikariConfigBO.setMinimumIdle("1");
        //控制从池中获取的连接是否是只读模式,需要数据库支持只读模式
        hikariConfigBO.setReadOnly("false");
        //检测连接是否有效的超时时间,单位毫秒,默认5000ms,最小250ms,不能大于connectionTimeout
        hikariConfigBO.setValidationTimeout("3000");
        //登录超时
        hikariConfigBO.setLoginTimeout("8");
        //为支持catalog概念的数据库设置默认catalog
        hikariConfigBO.setCatalog("");
        //连接泄露检测的最大时间,默认0,最低2000毫秒;也就是说,连接从拿出连接池到还回连接池的总时间,不能超出这个时间,超出的话就判定为泄露
        hikariConfigBO.setLeakDetectionThreshold("");
        //该属性设置一个SQL语句,从连接池获取连接时,先执行改sql,验证连接是否可用,例子:select1如果是使用了JDBC4那么不建议配置这个选项,因为JDBC4使用ping命令,更加高效
        hikariConfigBO.setConnectionTestQuery("");
        //该属性设置一个SQL语句,在将每个新连接创建后,将其添加到池中之前执行该语句
        hikariConfigBO.setConnectionInitSql("");
        //自动提交事务,默认值true
        hikariConfigBO.setAutoCommit("true");
        //是否允许JMX将连接池挂起
        hikariConfigBO.setAllowPoolSuspension("false");
        //如果池无法成功初始化连接,是否快速初始化失败的时间
        hikariConfigBO.setInitializationFailTimeout("");
        //是否在其自己的事务中隔离内部池查询,例如连接活动测试
        hikariConfigBO.setIsolateInternalQueries("false");
        //是否自动注册JMX(MBeans)相关的bean,用于运行时可以修改连接池设置
        hikariConfigBO.setRegisterMbeans("false");
        //连接池的用户定义名称,主要出现在日志记录和JMX管理控制台中以识别池和池配置连接池名称,默认自动生成,Hikari将它一般用于记录日志和JMX中,如果有多个Hikari连接池,建议配置一个有意义的名字
        hikariConfigBO.setPoolName("");
        //该属性为支持模式概念的数据库设置默认模式
        hikariConfigBO.setSchema("");
        //控制从池返回的连接的默认事务隔离级别
        hikariConfigBO.setTransactionIsolation("");
		
        datasourceConfigBO.setHikariConfigBO(hikariConfigBO);
        //以上hikariConfigBO不存在的属性可以添加到一下属性List中(如果没有可不配置)
        List<DatasourceConfigAttrBO> datasourceConfigAttrBOList = new ArrayList<DatasourceConfigAttrBO>();
        DatasourceConfigAttrBO otherValueAttrBO = new DatasourceConfigAttrBO();
        //修改用户id
        otherValueAttrBO.setUpdateUserId("8a81809373bd53600173bd5360160001");
        //修改用户名
        otherValueAttrBO.setUpdateUserName("datasourceManager-add");
        //创建用户id
        otherValueAttrBO.setCreateUserId("8a81809373bd53600173bd5360160001");
        //创建用户名
        otherValueAttrBO.setCreateUserName("datasourceManager-add");
        //属性代码
        otherValueAttrBO.setAttrCode("OtherValue");
        //属性名称
        otherValueAttrBO.setAttrName("其他属性");
        //属性值
        otherValueAttrBO.setAttrValue("OtherAttrValue");
        //描述
        otherValueAttrBO.setDescription("其他属性");
        datasourceConfigAttrBOList.add(otherValueAttrBO);
        datasourceConfigBO.setDatasourceConfigAttrBOList(datasourceConfigAttrBOList);
        ResultDTO resultDTO = dynamicDatasourceService.addDatasourceConfig(datasourceConfigBO);
        //结果码"00000"时,添加成功."B0001"时,"系统执行出错"."A0002"时,"动作执行所需要的数据不存在"参数缺失.
        String code = resultDTO.getRet();
    }
}
```

##### 2.删除数据源

```java
public class DynamicDatasourceServiceTest {
	
    @Autowired
    private DynamicDatasourceService dynamicDatasourceService;
	
    public void deleteDatasourceTest() {

        //根据数据源Code删除数据源
        String datasourceCode = "dynamic_test1";
        
        ResultDTO resultDTO = dynamicDatasourceService.deleteDatasourceConfigByDatasourceCode(datasourceCode);
        String code = resultDTO.getRet();
        //code = "00000":删除成功
        //code = "A0002":数据不存在
    }
}
```

##### 3.修改数据源
```java
public class DynamicDatasourceServiceTest {
	
    @Autowired
    private DynamicDatasourceService dynamicDatasourceService;
	
    public void updateDatasourceTest() {

        //待修改的数据源封装与添加相似封装datasourceConfigBO
        //根据datasourceCode判断,删除原来的,将封装好的重新添加
        //新封装的datasourceConfigBO中的datasourceCode要与待修改保持一致
        String datasourceCode = "dynamic_test";
        DatasourceConfigBO datasourceConfigBO = new DatasourceConfigBO();
        datasourceConfigBO.setDatasourceCode(datasourceCode);
        //完全按照添加数据源方法逻辑,封装实体....
        HikariConfigBO hikariConfigBO = new HikariConfigBO();
        //...
        List<DatasourceConfigAttrBO> datasourceConfigAttrBOList = new ArrayList<DatasourceConfigAttrBO>();
        //...
        datasourceConfigBO.setHikariConfigBO(hikariConfigBO);
        datasourceConfigBO.setDatasourceConfigAttrBOList(datasourceConfigAttrBOList);
        ResultDTO resultDTO = dynamicDatasourceService.updateDatasourceConfig(datasourceConfigBO);
        String code = resultDTO.getRet();
        //code = "00000":修改成功
        //code = "A0002":数据不存在
    }
}
```

返回值ResultDTO状态码Ret与Msg说明

| Ret码 | Msg说明 |
|:---:|:---:|
|"00000"|"执行成功"|
|"A0001"|"无效的用户输入"|
|"A0002"|"参数缺失,动作执行所需要的数据不存在"|
|"B0001"|"系统执行出错"|

#### 二.数据源切换

#####  1.使用自定义注解方式切换数据源

@Datasource注解可以作用于类以及方法上,同时会根据datasourceCode切换数据源.
当同时使用时,优先级为: 方法注解 > 类注解 > header中参数

```java		
@RestController
@RequestMapping("listTest")
@Datasource(datasourceCode = "dynamic_test")
public class SnrFtDatasourceController {

    @Autowired
    private XXXService xxxService;
    //此时同时使用类以及方法注解,最终将判定使用方法上注解中的datasourceCode参数值
	
    @RequestMapping("listAll")
    @Datasource(datasourceCode = "dynamic_test1")
    public List<XXX> listAll() {
        return this.xxxService.listAll();
    }
}
```

##### 2.在请求header中增加参数Datasource-Code切换数据源

需要在请求header中增加参数Datasource-Code,来进行切换数据源

```json
{
    "Content-Type":"application/json;charset=UTF-8",
	"Accept-Language":"zh-cn,zh;q=0.5",
	"Datasource-Code":"default"
}
```


### 配置
```yaml
#切换数据源切面配置遵循execution表达式
sanctr: 
  firebireds:
    tarsi:
      pointcut: execution(public * cn.unicom.hlj.snr.firebird.tarsi.controller.*.*(..))
spring:
  datasource: #Mysql数据源
    url: jdbc:mysql:replication://120.0.0.1/your_data?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&transformedBitIsBoolean=true
    username: root
    password: xxxxx
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      readOnly: false
      connectionTimeout: 60000
      idleTimeout: 60000
      validationTimeout: 3000
      maxLifetime: 120000
      maximumPoolSize: 10
      minimumIdle: 1
      loginTimeout: 5
#      leakDetectionThreshold:
#      connectionTestQuery:
#      connectionInitSql:
#      autoCommit:
#      allowPoolSuspension:
#      initializationFailTimeout:
#      isolateInternalQueries:
#      registerMbeans:
#      poolName:
#      schema:
#      transactionIsolation:
#      ....等等..
```

## 注意事项
1. 添加数据源时,需要确认数据源url,用户名,密码等必要信息的准确性.如果出现错误,将导致切换失败.
2. 如果,切换数据源所使用的datasourceCode在数据库中并不存在的时候,将会使用默认数据源.
3. 目前只支持hikari连接池配置Mysql,hikari需要上述组件中mysql-connector-java的版本支持,如存在低版本需替换.

## 示例程序

示例程序位于 [talrashas-brace-template](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)
