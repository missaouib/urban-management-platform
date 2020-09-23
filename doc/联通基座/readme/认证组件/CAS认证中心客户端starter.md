# CAS认证中心客户端starter[![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-plume-client-starter.git)

## 介绍

> 本组件为CAS认证中心客户端必须搭配服务端[CAS或OAuth认证中心-工程模版(sanctr-firebirds-plume-template )]使用.
>
> 实现功能:
>
> 1. CAS认证登录
>
>    拦截请求接口,未登录时,将跳转至服务端认证登录
>
> 2. 获取认证登录信息 
>
> 3. CAS认证退出



## 使用

### 组件
- 引入CAS认证客户端依赖使用组件
```xml
<!--CAS认证中心客户端starter-->
<dependency>
    <groupId>cn.unicom.hlj.snr.firebird</groupId>
    <artifactId>firebirds-plume-client-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 方法
- #### 获取认证后登录信息
```java
   /**
     * 获取认证后登录信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/principal",
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8")
    public String principal(HttpServletRequest request) {
        Object object = request.getSession().getAttribute("_const_cas_assertion_");
        if (null != object) {
            Assertion assertion = (Assertion) object;
            //通过cas client获取 principal
            String loginName = assertion.getPrincipal().getName();
            //获取属性值，为一个Map类型。
            Map<String, Object> att = assertion.getPrincipal().getAttributes();
            System.out.println("登陆用户名:" + loginName);
            System.out.println("登陆返回的属性:" + att);
            return loginName + "---登录成功";
        }
        return null;
    }
```

- #### 退出方法
```java
    @Autowired
    private CasClientConfigurationProperties casClientConfigurationProperties;

    @RequestMapping(value = "/loginout",
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8")
    public void logins(HttpSession session, HttpServletResponse response) throws IOException {
        //单点登录的session值去除
        session.removeAttribute("_const_cas_assertion_");
        session.invalidate();
        //单点登录登出service=application.yml client-host-url属性
        response.sendRedirect(casClientConfigurationProperties.getServerLogoutUrl() + "?service=" +casClientConfigurationProperties.getClientHostUrl() );
        System.out.println("退出登录成功");
    }
```

### 配置
```yml
server:
  port: 8111
cas:
  clientEnable: true
  # http://localhost:9099/firebirds-plume-template #CAS或OAuth认证中心-工程模版服务地址
  server-url-prefix: http://localhost:9099/firebirds-plume-template
  server-login-url: http://localhost:9099/firebirds-plume-template/login
  server-logout-url: http://localhost:9099/firebirds-plume-template/logout
  client-host-url: http://localhost:8111 #当前地址端口
  validation-type: cas3 #验证类型cas3
  ignore-Filters: /js/*|/css/*   #可忽略文件
  single-logout:
    enabled: true
  
```

## 示例程序

示例程序位于 [CAS或OAuth认证中心-工程模版](http://192.168.30.124/wangzj129-grouppath/develop/sanctr-firebirds-plume-template.git)

