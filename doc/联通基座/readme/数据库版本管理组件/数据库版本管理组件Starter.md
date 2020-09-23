
# 数据库版本管理组件Starter[![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-talons-starter.git)


## 介绍

> 数据库版本管理组件Starter,使用Flyway管理数据库版本变更
> Flyway优点:
>
> 1. 简单  非常容易安装和学习，同时迁移的方式也很容易被开发者接受。
> 2. 专一 Flyway 专注于搞数据库迁移、版本控制而并没有其它副作用。
> 3. 强大专为连续交付而设计。让Flyway在应用程序启动时迁移数据库。


## 使用

### 组件

- **引入依赖开始使用**
```xml
<dependency>
   <groupId>cn.unicom.hlj.snr.firebird</groupId>
   <artifactId>firebirds-talons-starter</artifactId>
   <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 配置
```yaml
spring:
  #mysql连接
  datasource: 
    url: jdbc:mysql:replication://127.0.0.1/your_data?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
    username: root
    password: your_password
    driver-class-name: com.mysql.jdbc.Driver
  #flyway属性配置(非必填 以下配置均有默认值)  
  flyway: 
    # 启用或禁用 flyway
    enabled: true
    # SQL脚本的目录多个路径使用逗号分隔 默认值 classpath:db/migration
    locations: classpath:db/migration
    # metadata版本控制信息表 默认flyway_schema_history
    table: snr_fe_flyway_schema_history
    # 迁移时数据库非空时 需配置true
    baseline-on-migrate: true
    # 指定 baseline 的版本号,默认值为 1, 低于该版本号的 SQL 文件, migrate 时会被忽略
    baseline-version: 0
    # 是否自动调用验证当你的版本不符合逻辑 比如:你先执行了DML而没有对应的DDL会抛出异常
    validate-on-migrate: false
    # 禁用启动清空数据库操作
    clean-disabled: true
```

### SQL文件管理

#### 1. sql脚本路径
新增sql脚本存放目录 :resources/db/migration(默认目录)
可通过配置文件中spring.flyway.locations属性修改路径

#### 2. sql脚本命名规则

![QQ截图20200821093250](C:\Users\Administrator\Desktop\QQ截图20200821093250.png)

```txt
采用左对齐原则,缺位用0代替的方式比较两个SQL文件的先后顺序
1.0.1.1 比 1.0.1 版本高。
1.0.10 比 1.0.9.4 版本高。
1.0.10 和 1.0.010 版本号一样高, 每个版本号部分的前导 0 会被忽略。

示例名称：
V1.0.0__Add_new_table.sql V 表示 Versioned用于版本升级
R1.0.0__Add_new_table.sql R 表示 Repeatable可重复执行
U1.0.0__Add_new_table.sql U 表示 Undo用于撤销具有相同版本的版本化迁移带来的影响

名称说明：
Prefix 可配置，前缀标识 V R U
Version 标识版本号, 由一个或多个数字构成, 数字之间的分隔符可用点 . 或下划线 _
Separator 可配置, 用于分隔版本标识与描述信息, 默认为两个下划线 __
Description 描述信息, 文字之间可以用下划线 _ 或空格 分隔
Suffix 可配置,后续标识, 默认为 .sql
```

#### 3. sql语句

- 合法的 DDL DML sql语句 示例

```sql
ALTER TABLE `snr_fe_role` 
DROP COLUMN `version`,
MODIFY COLUMN `delete_token` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT;
```

### 常见问题
---
#### 1. 启动过程中由于sql语句错误导致程序报错停止
> 如果新版本的sql语句写错了，可能会导致 flyway 执行失败，会在元数据表中增加一条执行 status 为 0 的记录，只要 status 有为 0 的记录，项目就无法启动。
> 解决方法一：手动去数据库删除这条记录，然后重新启动即可。
> 解决方法二：利用 flyway 的 callback 来实现执行失败，自动删除失败记录。在 `resources/db/migration`目录下添加名为 `afterMigrateError.sql`文件(名称固定)，文件内容为：

```sql
-- SQL 执行失败，清理 flyway 元数据表中失败的执行记录
DELETE IGNORE FROM `${table}` WHERE success = 0;
-- table 变量就是当前项目元数据表的表名称。
```

#### 2. 项目 SQL 迁移应该注意两种情况

(1)初次部署，将设计好的sql脚本放在指定目录下正常启动即可。（注意sql脚本会按照从低到高的版本依次执行）
```txt
//示例 参考上文:SQL文件管理(2.sql脚本命名规则)
V1.0__yueyp_Init_Organization_Table.sql
V1.1__yueyp_Add_Permission_Group_Relevant_Table.sql
```
(2)非初次部署，即数据库中已经存在数据表及数据。这种情况请参考如下处理方式：

> 1. 先导出一份当前项目最新版本的表结构，在`resources/db/migration`目录中创建一个 `V1.0__base_init.sql` 文件，将最新版本的 DDL 以及需要初始化的数据放到这个文件中，这个 sql 文件后期就不要做任何修改
> 2. 配置属性`spring.flyway.baseline-version`值为1.0（与`V1.0__base_init.sql`的version保持一致） 
> 配置该属性后，启动服务会自动跳过V1.0版本执行高于1.0的版本。这样就不会执行V1.0初始化数据表的脚本，原有数据不会有影响。
> 3. 如果有新增的 DDL，则创建一个高版本的 sql 文件，如`V1.1__add_table.sql`，项目启动的时候会自动执行 sql。
>**注意已经执行过的sql脚本，切勿修改！！！ 如有改动应依次新增高版本sql脚本**
#### 3. 注意事项
>配置flway控制版本后,无需手动执行sql.
>
>如测试时需要时常改动,关闭flyway.
>
>当未关闭flyway,手动执行sql后会导致执行失败.

