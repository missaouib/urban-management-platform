
# OAuth认证中心客户端starter[![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-plume-oauth2-client-starter.git)


## 介绍

> 本组件为OAuth客户端必须配合服务端[CAS或OAuth认证中心-工程模版(sanctr-firebirds-plume-template )]使用.
>
> OAuth认证中心组件主要依赖Spring Security5来拦截实现.
>
> 实现功能:
>
> 1. OAuth认证登录
>
>    根据OAuth2协议获取code,然后自动根据code获取token,在通过token获取用户信息
>
> 2. 获取认证后登录信息
>
> 3. OAuth认证退出
>
> 4. 重定向获取CODE

## 使用

### 组件
- 引入依赖开始使用
```xml
<!--  OAUTH2 认证客户端 -->
<dependency>
  <groupId>cn.unicom.hlj.snr.firebird</groupId>
  <artifactId>firebirds-plume-oauth2-client-starter</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 方法

#### 一. 拦截器类

##### 1. 继承UiSecurityConfig

##### 2. 配置拦截器

```java
@Configuration
@Order(3)
public class SecurityConfig extends UiSecurityConfig {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //关闭csrf
        http.antMatcher("/**").authorizeRequests() //设置过滤
                .antMatchers("/").permitAll() //无条件允许访问
                .anyRequest().authenticated() //请求都需要用户被认证
                .and()
                .logout() //提供注销的支持。这是在使用WebSecurityConfigurerAdapter时自动应用的。
                .logoutUrl("/xx/logout") //触发注销发生的URL(默认为 /logout)。如果启用了CSRF保护(默认)，那么请求也必须是POST。
                //service= 退出后页面地址
                .logoutSuccessUrl("http://localhost:9099/firebirds-plume-template/logout?service=http://127.0.0.1:8080/sanctr/talrashas-brace-template/v1/welcome") //注销后要重定向到的URL。默认是/login?logout。
                .and()
                .oauth2Login();
    }
}
```


#### 二 .OAUTH2方法
#####  1. 获取认证后登录信息

```java
    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    /**
     * 获取认证后登录信息
     *
     * @param authentication
     * @return
     */
    @GetMapping("/principal")
    public ResultDTO principal(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient authorizedClient =
                this.oAuth2AuthorizedClientService.loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        //获取TOKEN
        String token = accessToken.getTokenValue();
        //获取PrincipalName
        String principalName = authorizedClient.getPrincipalName();
        //获取用户信息
        OAuth2User oAuth2User = authentication.getPrincipal();
        return new ResultDTO(ErrorCodeEnum.SUCCESS, "欢迎登录");
    }
```

##### 2. 退出
```java
    /**
     * 退出
     *
     * @return
     */
    @GetMapping("/logout")
    public String logout() {
        System.out.println("logout----已经注销");
        return "logout";
    }
```

##### 3. 重定向获取CODE
```java
    /**
     * 重定向获取token
     * /login/oauth2/code/ 为固定值code后可任意写
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/login/oauth2/code/自定义", produces = "application/json;charset=utf-8")
    public ResultDTO welcome(Model model) {
        return new ResultDTO(ErrorCodeEnum.SUCCESS, "欢迎光临");
    }
```
#### 配置
```yml
spring:
  security:
    oauth2:
      client:
        registration:
          customa:
            #客户端ID 工程模板新增服务生成,对应RegexRegisteredService表中client-id
            client-id: xxx 
            #客户端密码 工程模板新增服务生成,对应RegexRegisteredService表中client-secret
            client-secret: xxx
            scope: read,write
            authorization-grant-type: authorization_code
            #重定向地址注意：/login/oauth2/code/ 为固定格式code后可自定义
            redirect-uri: http://127.0.0.1:8080/login/oauth2/code/自定义
        provider:
          customa:
            #获取CODE
            authorization-uri: http://localhost:9099/firebirds-plume-template/oauth2.0/authorize
            #获取TOKEN
            token-uri: http://localhost:9099/firebirds-plume-template/oauth2.0/accessToken
            #刷新TOKEN
            user-info-uri: http://localhost:9099/firebirds-plume-template/oauth2.0/profile
            user-name-attribute: id
  thymeleaf:
    cache: false
```


## 示例程序

示例程序位于 [基座SpringBoot工程模版](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)