# 短信发送starter [![GitLab](http://192.168.30.124/assets/favicon-7901bd695fb93edb07975966062049829afb56cf11511236e61bcf425070e36e.png)](http://192.168.30.124/wangzj129-grouppath/develop/firebirds-down-starter.git)


## 介绍
> 短信发送starter目前已集成大宇网关，融合云信网关，三网短信网关，一信通网关，配置后既可使用。
> 主要特性：
> 一. 短信发送方式
>
> 1. 单用户发送
> 2. 多用户发送
>
> 二.记录日志方式
> 1. kafka日志,需启动短信发送consumer微服务监听kafka推送消息存储mongodb数据库中.
> 2. mongodb日志

## 使用

### 组件

- 引用依赖开始使用组件
```xml
        <!--短信发送starter-->
        <dependency>
            <groupId>cn.unicom.hlj.snr.firebird</groupId>
            <artifactId>firebirds-down-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```
- 下列组件按需引入
1. 使用大宇短信时引入依赖
```xml
        <!--大宇短信-->
        <dependency>
            <groupId>com</groupId>
            <artifactId>taobao-sdk-java-auto</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
2. 使用kafka记录日志引入依赖
```xml
        <!--kafka组件-->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
```
3. 使用mongodb记录日志引入依赖
```xml
        <!--mongodb组件-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
```
### 方法

#### 1. 单个手机号发短信
```java
import cn.unicom.hlj.snr.firebird.down.exception.SmsException;
import cn.unicom.hlj.snr.firebird.down.param.AggregateParam;
import cn.unicom.hlj.snr.firebird.down.sender.Sender;

public class TestController {
    //短信接口
    @Autowired
    private Sender sender;

    /**
     * 单个手机号发送接口
     *
     * @param numberPhone    手机号   必填
     * @param content        短信内容 必填
     * @param aggregateParam 配置参数 必填 (参照下文 短信发送参数)
     * @throws IOException  io异常
     * @throws SmsException sms异常
     */
    public void yourMethod_singleSend(String numberPhone, String content, AggregateParam aggregateParam) throws IOException, SmsException {
        //单个手机号发送接口
        String res = sender.singleSend(numberPhone, content, aggregateParam);
        //"0"状态成功
        if ("0".equals(res)) {
            System.out.println("发送成功");
        } else {
            System.out.println("发送失败");
        }
    }
}
```

#### 2. 多个手机号发短信
``` java
import cn.unicom.hlj.snr.firebird.down.exception.SmsException;
import cn.unicom.hlj.snr.firebird.down.param.AggregateParam;
import cn.unicom.hlj.snr.firebird.down.sender.Sender;

public class TestController {
    //短信接口
    @Autowired
    private Sender sender;

    /**
     * 多个手机号发短信
     *
     * @param numberPhones   手机号（List<String>） 必填
     * @param content        短信内容 必填
     * @param aggregateParam 配置参数 必填 (参照下文 短信发送参数)
     * @throws SmsException sms异常
     */
    public void yourMethod_batchSend(List<String> numberPhones, String content, AggregateParam aggregateParam) throws SmsException {
        List<Map<String, String>> list = sender.batchSend(numberPhones, content, aggregateParam);
        // list.size() == 0 成功
        if (list.size() == 0) {
            System.out.println("发送成功");
        } else {
            System.out.println("发送失败");
        }
    }
}
```

- #### 短信发送参数
```java
// 大宇网络
AggregateParam dayuParam = new AggregateParam();
dayuParam.setSignName("");
dayuParam.setTemplate("");
dayuParam.setExtend(""); 
//自定义内容
Map<String,Object> map = new HashMap<>();
map.put("code":"123");
dayuParam.setParam(map); 
    
// 融合云信
AggregateParam rhyxParam = new AggregateParam();
rhyxParam.setTemplate("");
rhyxParam.setExtend("");
//自定义内容
Map<String,Object> map = new HashMap<>();
map.put("code":"123");
rhyxParam.setParam(map); 

// 三网短信
AggregateParam swParam = new AggregateParam();
null

// 一信通短信
AggregateParam yxtParam = new AggregateParam();
null
```
### 开启日志
- #### 注入SenderLogService Bean开启日志功能
```java
    //开启日志
    @Bean
    public SenderLogService senderLogService() {
        return new SenderLogServiceImpl();
    }
```

### 配置
#### 日志配置
##### 1. kafka记录日志
```yml
Topic: 
  xxx #自定义kafka消息头
```
##### 2. mongodb记录日志
```yml
spring:
  data: #MongoDB数据源
    mongodb:
      uri: mongodb://root:password@127.0.0.1:32107/your_data_name?authSource=admin
      database: your_data_name
      username: root
      password: password
      authentication-database: admin 
```
#### 短息配置
##### 1. 大宇网络
```yml
sanctr:
    sms: 
        type: dayu
        dayu: #下列属性必填
          appKey: xxx
          appSecret: xxx
          sendUrl: xxx
```

##### 2. 融合云信
```yml
sanctr:
    sms: 
        type: rhyx
        rhyx: #下列属性必填
          cpcode: xxx
          key: xxx
          sendUrl: xxx
```

##### 3. 三网短信
```yml
sanctr:
     sms:
        type: sw
        sw: #下列属性必填
          account: xxx
          msg: xxx
          password: xxx
          sendUrl: xxx
```

##### 4.  一信通短信
```yml
sanctr:
    sms: 
       type: yxt
       yxt: #下列属性必填
           spCode: xxx
           loginName: xxx 
           password: xxx
           sendUrl: xxx
```
## 注意事项
1. mongodb数据库中日志查看LOG_MESSAGE_RESULT
2. kafka日志查看,mongodb数据库Topic名称为集合名称

## 示例程序

示例程序位于 [基座SpringBoot工程模版](http://192.168.30.124/wangzj129-grouppath/develop/talrashas-brace-template)