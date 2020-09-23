# 基座SpringBoot-工程模版 [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template.git)


## 介绍

> 基座SpringBoot-工程模版中包含了各个组件的使用方式，现阶段已集成
> - 用户组织结构权限组件：模板中已经包含了使用方式已经拓展方式。
> - AOP日志组件：可使用注解或引用方式使用，日志组件由Mongo库支撑。
> - 短信组件: 支持四种短信网关（大宇，三网，融合云信，一信通）。
> - CAS客户端组件：模板中包含了CAS登录与退出配置文件使用方式。
> - OAUTH客户端组件：主要以Security + OAUTH2方式实现，可重写UiSecurityConfig根- 据自己项目自定义拦截方式，模板中包含了登录退出配置文件以及重写方式。
> - 认证之后的客户端初始化组件: 认证客户端登录后自动获取用户，租户，权限，部门等信息。
> - 缓存组件包:两级缓存组件 第一级缓存使用内存(同时支持 Ehcache 2.x、Ehcache 3.x 和 Caffeine)，第二级缓存使用 Redis(推荐)/Memcached 
> - 邮件发送组件：支持QQ邮箱，163邮箱

## 使用

### (一)使用CAS

#### 组件
- ##### CAS 认证客户端依赖
- ##### 注：CAS与OAUTH2只能选择其一使用
```xml
<dependency>
    <groupId>cn.unicom.hlj.snr.firebird</groupId>
    <artifactId>firebirds-plume-client-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
#### 配置
#####  `application.properties`或`application.yml`中必须使用的属性
```xml
cas:
  clientEnable: true
  server-url-prefix: http://localhost:9099/firebirds-plume-template
  server-login-url: http://localhost:9099/firebirds-plume-template/login
  client-host-url: http://localhost:8080
  server-logout-url: http://localhost:9099/firebirds-plume-template/logout
  validation-type: cas3
  ignore-Filters: /logs/*|/sms/*   #可忽略文件
  single-logout:
    enabled: true
```

#### 使用方法
- ##### 登录方法
```java
		@RequestMapping(value = "login",
	            method = RequestMethod.GET,
	            produces = "application/json;charset=utf-8")
	    public String login(HttpSession session,
	                        HttpServletRequest request, HttpServletResponse response) {
	        //返回的登录用户信息
	        //通过cas client获取
	        Object object = request.getSession().getAttribute("_const_cas_assertion_");
	        if(null != object) {
	            Assertion assertion = (Assertion) object;
	            String loginName = assertion.getPrincipal().getName();
	            //获取属性值，为一个Map类型。
	            Map<String, Object> att = assertion.getPrincipal().getAttributes();
	            logger.info("登陆用户名:{}" ,loginName);
	            logger.info("登陆返回的属性:{}" ,att);
	            return loginName+"---登录成功";
	        }
```

- ##### 退出方法
```java
	@RequestMapping(value = "loginout",
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8")
    public void logins(HttpSession session,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        //单点登录的session值去除
        session.removeAttribute("_const_cas_assertion_");
        session.invalidate();
        //单点登录登出service=自定义页面
        response.sendRedirect(casout_server_logout_url+"?service=http://www.baidu.com");
        logger.info("退出登录成功");
    }
```

### (二)使用AOP日志

#### 组件
- ##### 引入AOP日志组件
```xml
		<!-- 日志组件 -->
        <dependency>
            <groupId>cn.unicom.hlj.snr.firebird</groupId>
            <artifactId>firebirds-pinions-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        
       <!-- 日志AOP组件（引用AOP组件可不使用日志组件依赖） -->
        <dependency>
            <groupId>cn.unicom.hlj.snr.firebird</groupId>
            <artifactId>firebirds-pinions-web-aop-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        
        <!-- kafka，mongo二选一引入 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
```

#### 配置
#####  `application.properties`或`application.yml`中必须使用的属性
```xml
          MongoLogServiceImpl   #使用Mongo存储日志
          KafkaLogServiceImpl   #使用Kafka存储日志
          flat                  #flat为扁平化存储开关 true，false
          pointcut: execution(* cn.unicom.hlj.snr.firebird.eye.sample.controller.LogAopController.*(..))    AOP可指定保存日志controller
```
#### 使用方法

- ##### 引用类
```java
  @Autowired
   private LogService logService;
```
- ##### 日志方法
```java
  @RequestMapping(value = "logSample",
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8")
    public void logMongo() {
        System.out.println("--------sample logMongo----------");
        sampleLogService.sampleAddLog();
    }
```
- ##### AOP日志方法
```java
  @RequestMapping(value = "logAopSample",
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8")
    public ResultDTO logAopSample(String pathStr) {
        System.out.println("----------sample logAopSample--------------");
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData("logAopSample");
        resultDTO.setError(ErrorCodeEnum.SUCCESS,"执行成功");
        //...Method Content...
        return resultDTO;
    }

    @Log(logType = "LOG_AOP", operationType = "insert", logSubType = "操作日志", details = "Insert into log_aop")
    @RequestMapping(value = "logAop",
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8")
    public ResultDTO logAop() {
        System.out.println("----------sample logAop--------------");
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData("logAop");
        resultDTO.setError(ErrorCodeEnum.SUCCESS,"执行成功");
        return resultDTO;
    }
```

### (三)使用短信

#### 组件
- ##### 引入短信发送组件
```xml
		<!--  配置短信发送组件 -->
        <dependency>
            <groupId>cn.unicom.hlj.snr.firebird</groupId>
            <artifactId>firebirds-down-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        <!--大宇短信需要导入-->
        <dependency>
            <groupId>com</groupId>
            <artifactId>taobao-sdk-java-auto</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
#### 配置
#####  `application.properties`或`application.yml`中必须使用的属性
```yml
 #大宇短信配置
  sms:
    type: dayu
    dayu:
      appKey: xxx
      appSecret: xxx
      sendUrl: xxx

  #融合云信短信配置
  sms:
    type: rhyx
    rhyx:
      cpcode: xxx
      key: xxx
      sendUrl: xxx

  #一信通短信配置
  sms:
    type: yxt
    yxt:
      spCode: xxx
      loginName: xxx
      password: xxx
      sendUrl: xxx

  #三网短信配置
  sms:
    type: sw
    sw:
      account: xxx
      msg: xxx
      password: xxx
      sendUrl: xxx
```
#### 使用方法
- ##### 引用类

```java
  //短信包引入
    @Autowired
    private Sender sender;
```
- ##### 短信使用方法

```java
//实例化你要使用的网关属性（例如DAYU）
AggregateParam dayuParam = new AggregateParam();

dayuParam.setExtend("extend");
Map<String,Object> map = new LinkedHashMap<>();
map.put("xx","xx");
map.put("xx","xx");
map.put("xx","xx");
dayuParam.setParam(map);
dayuParam.setTemplate("template");
dayuParam.setSignName("signName");
  //单条发送
 sender.singleSend("18888888888","发送内容",dayuParam);

 List<String> lists = new ArrayList<String>();
 lists.add("18888888888");
 lists.add("17777777777");
//群发
sender.batchSend(lists,"发送内容",dayuParam)
```

- ###### KAFKA短信使用方法
```java
  //JSON格式传送
 String json = "{\"numberPhones\":\"18888888888,17777777777\",\"content\":\"aabb\",\"params\":{\"extend\":\"aabb\",\"template\":\"aabb\"\n" +
                "                            ,\"param\":{\"XXX\":\"XXX\",\"XXX\":\"XXX\",\"XXX\":\"XXX\"}}}";
        //${sanctr.sms.sendTopic} 可配置属性
        ListenableFuture future = kafkaTemplate.send("SMS_TOPIC", json);
```
### 短信日志功能
- #### 注入SenderLogService Bean开启日志功能
```java
    //开启日志
    @Bean
    public SenderLogService senderLogService() {
        return new SenderLogServiceImpl();
    }
```
#### 1.kafka日志
```yml
#默认为kafka记录日志，使用需要自定义消费者来消费日志，配置
Topic:
  LOG_MESSAGE_RESULT 短信返回结果日志
```
#### 2.mongodb日志
```xml
<!--mongodb日志需要修改pom文件，打开mongdb依赖,并配置mongodb相关yml-->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

### (四)使用OAUTH

#### 组件
- ##### OAUTH 认证客户端依赖
- ##### 注：CAS与OAUTH2只能选择其一使用
- ##### 授权码模式
```xml
<!--  OAUTH2 认证客户端 -->
<dependency>
  <groupId>cn.unicom.hlj.snr.firebird</groupId>
  <artifactId>firebirds-plume-oauth2-client-starter</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

#### UiSecurityConfig 重写
```java
@Configuration
@Order(3)
public class SecurityConfig extends UiSecurityConfig {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.antMatcher("/**").authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout() //提供注销的支持。这是在使用WebSecurityConfigurerAdapter时自动应用的。
                    .logoutUrl("/xx/logout") //触发注销发生的URL(默认为 /logout)。如果启用了CSRF保护(默认)，那么请求也必须是POST。
                    //service= 退出后页面地址
                    .logoutSuccessUrl("http://localhost:9099/firebirds-plume/logout?service=http://127.0.0.1:10011/sanctr/talrashas-brace-template/v1/welcome") //注销后要重定向到的URL。默认是/login?logout。
                .and()
                .oauth2Login();
    }
}
```
#### 配置
#####  `application.properties`或`application.yml`中必须使用的属性
```xml
spring:
  security:
    oauth2:
      client:
        registration:
          customa:
            #客户端ID
            client-id: 3716504419831808
            #客户端密码
            client-secret: 123456
            scope: read,write
            authorization-grant-type: authorization_code
            #重定向地址注意：/login/oauth2/code/ 为固定格式code后可自定义
            redirect-uri: http://127.0.0.1:10011/sanctr/talrashas-brace-template/v1/login/oauth2/code/welcome
        provider:
          customa:
            #获取CODE
            authorization-uri: http://localhost:9099/firebirds-plume/oauth2.0/authorize
            #获取TOKEN
            token-uri: http://localhost:9099/firebirds-plume/oauth2.0/accessToken
            #刷新TOKEN
            user-info-uri: http://localhost:9099/firebirds-plume/oauth2.0/profile
            user-name-attribute: id
  thymeleaf:
    cache: false
```

#### 使用方法
- ##### 登录方法
```java
        @Autowired
        private OAuth2AuthorizedClientService authorizedClientService;

		@GetMapping("/welcome")
	    public ResultDTO welcome(OAuth2AuthenticationToken authentication){
        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientService.loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        System.out.println("获取TOKEN----"+accessToken.getTokenValue());
        System.out.println("获取PrincipalName----"+authorizedClient.getPrincipalName());
        System.out.println("获取用户信息----"+authentication.getPrincipal());
        return new ResultDTO(ErrorCodeEnum.SUCCESS, "欢迎登录");
    }
```

- ##### 退出方法
```java
	@GetMapping("/xx/logout")
    public String logout(){

        System.out.println("logout---------------------已经注销");
        return "logout";
    }
```

- ##### 重定向获取CODE
```java

    @GetMapping(value="/login/oauth2/code/welcome", produces = "application/json;charset=utf-8")
    public ResultDTO welcome(Model model) {
        return new ResultDTO(ErrorCodeEnum.SUCCESS, "欢迎光临");
    }
```

- ##### 客户端模式
```java
    http://localhost:9099/firebirds-plume/oauth2.0/accessToken?grant_type=client_credentials&client_id=3716504419831808&client_secret=123456
    参数说明：
    grant_type=client_credentials   固定
    client_id                       客户端ID
    client_secret                   客户端秘钥
    

```
### (五)认证之后的客户端初始化

#### 组件
- ##### 登陆后自动获取人员相关信息并存入session中
```xml
        <!--  获取部门、角色、权限 存入session 引入依赖无需其他操作-->
        <dependency>
            <groupId>cn.unicom.hlj.snr.firebird</groupId>
            <artifactId>firebirds-plume-after-login-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```

#### 使用方法
- ##### 需要配置数据库
```java
		System.out.println("SESSION_用户信息------"+request.getSession().getAttribute("FE_USER"));
        System.out.println("SESSION_部门------"+request.getSession().getAttribute("FE_DEPT_ID_LIST"));
        System.out.println("SESSION_角色权限------"+request.getSession().getAttribute("FE_USER_ROLE_PERMISSION_RELATION_LIST"));
        System.out.println("SESSION_租户名单------"+request.getSession().getAttribute("FE_TENANT_ID_LIST"));
```

### (六)缓存组件包

#### 组件
- ##### 引入依赖使用组件
```xml
<dependency>
    <groupId>cn.unicom.hlj.snr.gem</groupId>
    <artifactId>gem-invigorating</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
<!-- redis可选依赖   -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
- ##### 配置文件说明
```yml
	#100存储最大数量, 缓存过期时间单位可设置m,s,h,d 时分秒天 default为默认设置，可新增自定义配置
	caffeine:
	  region:
	    default : 100, 3600s
	    user : 1000, 7200s   #user 为自定义配置
	j2cache:
	  allow-null-values: true #是否允许null值缓存
	  default_cache_null_object: true #是否启用null对象缓存
	  serialization: json #序列化方式json
	  #缓存清除模式
	  #active:主动清除，二级缓存过期主动通知各节点清除，优点在于所有节点可以同时收到缓存清除
	  #passive:被动清除，一级缓存过期进行通知各节点清除一二级缓存
	  #blend:两种模式一起运作，对于各个节点缓存准确性以及及时性要求高的可以使用（推荐使用前面两种模式中一种）
	  cache-clean-mode: active
	  #一级缓存类型
	  L1:
	    provider_class: caffeine
	  #redis客户端类型
	  redis-client: lettuce
	  #开启二级缓存
	  l2-cache-open: true
	  #可以使用springRedis进行广播通
	  broadcast: none
	  #二级缓存类型
	  L2:
	    provider_class: lettuce
	    #用于获取redis配置
	    config_section: lettuce

	#lettuce缓存命名空间
	lettuce:
	  namespace: cache
	  mode: single
	  #基于region+_key存储
	  storage: generic
	  hosts: 127.0.0.1:6379
	  password: testing
	  database: 0
	  #redis : 连接单个 Redis 服务
	  #rediss : 使用 SSH 连接单个 Redis 服务
	  #redis-sentinel : 连接到 Redis Sentinel 集群（结合 sentinelMasterId 进行使用）
	  #redis-cluster : 连接到 Redis Cluster
	  scheme: redis

	#配置redis集群只需要把 scheme 属性改为 redis-cluster 并配置hosts节点,修改broadcast属性即可
	#例：
	#hosts: 127.0.0.1:6379,127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005
	#scheme:redis-cluster
    #broadcast: cn.unicom.hlj.snr.gem.invigorating.support.redis.SpringRedisPubSubPolicy
    
```

- #### 使用方法
- 1.使用CacheChannel 方式
```Java
	@Autowired
	private CacheChannel cacheChannel;

	@GetMapping("/cacheText")
    public ResultDTO cacheText(){

        cacheChannel.set("cacheText", "test", "123");
        CacheObject cacheObject = cacheChannel.get("cacheText", "test");
        Assert.isTrue(cacheObject.getValue().equals("123"), "失败！");

        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData("cacheText");
        resultDTO.setError(ErrorCodeEnum.SUCCESS,"执行成功");
        return resultDTO;
    }
```
- 2.注解方式
```Java
import org.springframework.cache.annotation.Cacheable;	
    //插入缓存
	@GetMapping("/openCacheText")
    @Cacheable(cacheNames="openCacheText",key = "'str:' + #p0 ")
    public ResultDTO openCacheText(String str){
        System.out.println("str---"+str);
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData("openCacheText");
        resultDTO.setError(ErrorCodeEnum.SUCCESS,"执行成功");
        return resultDTO;
    }

	//删除缓存
	@GetMapping("/evictCacheText")
    @CacheEvict(cacheNames={"openCacheText"} ,key = "'str:' + #p0 ")
    public ResultDTO evictCacheText(String str){
        System.out.println("evict------"+str);
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData("evictCacheText");
        resultDTO.setError(ErrorCodeEnum.SUCCESS,"执行成功");
        return resultDTO;
    }
```


### (七)邮件发送

#### 组件
-  ##### 引入依赖使用组件
```xml
		<!--邮件发送starter-->
        <dependency>
            <groupId>cn.unicom.hlj.snr.talrashas</groupId>
            <artifactId>talrashas-stride-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
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
        <!--jdbc-->
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
```
- ##### 配置文件说明
```yml
snr:
  mail:
    # true时开启日志存储功能  默认false
    logSwitch: true
    # 日志存储MongoDB时的表名 默认LOG_MAIL_SENDER 可配置
    logTypeName: LOG_MAIL_SENDER_COPY
    
spring:
	mail:
    default-encoding: UTF-8
    host: smtp.qq.com
    # 这是客户端认证密码，邮件网页的登录密码是12345abcde
    password: xxx
    username: xxx
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

- #### 使用方法
```Java
	// 引用接口
    @Autowired
    private MailSenderService mailSenderService;

    /**
     * @Description 邮件模板发送
     */
    @RequestMapping(value = "mailTemplate",
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8")
    public void mailTemplate() throws SmsException, IOException {
        Map<String,String> map = new HashMap<String,String>();
        map.put("测试模板","我要测试");
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
        mailSenderService.sendMailByTemplateType(
                "xxx@qq.com",
                "TestType1",
                "测试一下",
                map
        );
    }


    /**
     * @Description 自行维护邮件模板发送（无需依赖mysql相关dependency）
     */
    @RequestMapping(value = "personalBehaviorMailTemplate",
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8")
    public void personalBehaviorMailTemplate() throws Exception {
        Map<String,String> map = new HashMap<String,String>();
        map.put("测试模板1","我要测试1");
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
        mailSenderService.sendMailByTemplateContent(
                "xxx@qq.com",
                        BeetlString(),
                "测试一下1", map);
    }

    /**
     * @Description beetl模板引擎
     */
    public String BeetlString() throws Exception {
        //new一个模板资源加载器
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        /* 使用Beetl默认的配置。
         * Beetl可以使用配置文件的方式去配置，但由于此处是直接上手的例子
         */
        Configuration config = Configuration.defaultConfiguration();
        //Beetl的核心GroupTemplate
        GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, config);
        //我们自定义的模板，其中${title}就Beetl默认的占位符
        String testTemplate="<html>\n" +
                "<head>\n" +
                "\t<title>${title}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<h1>${name}</h1>\n" +
                "</body>\n" +
                "</html>";
        Template template = groupTemplate.getTemplate(testTemplate);
        template.binding("title","This is a test template Email.");
        template.binding("name", "测试");
        //渲染字符串
        String str = template.render();
        return str;
    }


    /**
     * @Description 非邮件模板推送
     */
    @RequestMapping(value = "mailNoTemplate",
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8")
    public void mailNoTemplate() throws Exception {
        Map<String,String> map = new HashMap<String,String>();
        map.put("测试模板2","我要测试2");
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
        mailSenderService.sendMailByContentStr(
                "xxx@qq.com",
                "邮件内容2",
                "测试一下2");
    }
```