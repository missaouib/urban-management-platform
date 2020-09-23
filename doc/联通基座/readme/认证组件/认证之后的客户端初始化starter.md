# 认证之后的客户端初始化starter [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-plume-after-login-starter)

## 介绍
> 本组件必须配合CAS认证中心客户端starter组件或OAuth认证中心客户端starter组件使用.认证成功后根据登录信息调用用户组织机构组件获取用户的所属部门、拥有的角色、权限等信息，并存入session.
>
> 需配置用户组织机构组件相关数据源.用于获取用户相关信息.

## 使用

### 组件

- 引入依赖开始使用组件
```xml
<!--认证之后的客户端初始化starter-->
<dependency>
    <groupId>cn.unicom.hlj.snr.firebird</groupId>
    <artifactId>firebirds-plume-after-login-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 用户组织机构 -->
<dependency>
    <groupId>cn.unicom.hlj.snr.firebird</groupId>
    <artifactId>firebirds-eye-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
### 示例
```java
//导入Repository
import cn.unicom.hlj.snr.gem.ruby.repository.Repository;

public class YouClass{
    
    @Autowired
    private Repository repository;

    // 获取用户信息
    public void readUser() {
        UserBean userBean = repository.readUser();
    }

    // 获取用户部门id集合
    public void readDeptIdList() {
        List<String> deptIdList = repository.readDeptIdList();
    }

    // 获取用户权限id集合
    public void readPermissionIdList() {
        List<String> permissionIdList = repository.readPermissionIdList();
    }

    // 获取租户id集合
    public void readTenantIdList() {
        List<String> tenantIdList = repository.readTenantIdList();
    }
}
```
### 配置
```yml
spring:
  datasource:
    url: jdbc:mysql:replication://127.0.0.1/your_data_name?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
    username: root
    password: password
    driver-class-name: com.mysql.jdbc.Driver
```

## 示例程序

示例程序位于 [基座SpringBoot工程模版](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)