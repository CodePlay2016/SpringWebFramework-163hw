# Spring-Web框架单元作业
---
## 题目
> 完成包含以下功能的Web工程，提供一个接口，该接口可以根据需要（扩展名，Accep头）返回一个HTML文档或者JSON数据，要求：
返回的JSON数据是一个用户列表，每个用户包含两个信息：用户Id（userId），用户名（userName），最终的数据类似：
{"userList" : [
{
"userId": 1001,
"userName": "test1"
},
{
"userId": 1002,
"userName": "test2"
}
...
]}
2. 返回的HTML文档基于FreeMarker生成，内容是一个用户列表的表格，对HTML表格不熟悉的同学可以参考 http://www.w3school.com.cn/tiy/t.asp?f=html_tables 了解；
3. 以自己能力为基础，返回的数据可以是接口内直接返回的，也可以是从数据库表里查询出来的（加分）；
4. 尽可能根据项目模板里介绍的内容组织代码及资源。

## 解析
> 本题通过MyController类来实现处理请求响应的过程
> MyController下层与UserService和UserDao相连，可以与数据库交互，上层通过show-servlet.xml与DispatcherServlet相连，与请求和响应进行交互。
> 所使用的数据库还是上一节课的数据库表UserBalance

> 项目的源码在[这里](https://github.com/CodePlay2016/SpringWebFramework-163hw.git)

## 部分代码
1. web.xml:这里使用的servlet名称叫show
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            classpath:application-context.xml </param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>show</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>show</servlet-name>
        <url-pattern>/api/*</url-pattern>
    </servlet-mapping>
</web-app>
```

2. show-servlet.xml,也是DispatcherServlet的配置文件，这是关键代码，定义了通过内容协商视图解析器来解析模型和视图的配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
	    http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <context:component-scan base-package="com.thomas.webhw"/>
    <mvc:annotation-driven />

    <bean id="contentNegotiationManager"
          class="org.springframework.web.accept.ContentNegotiationManagerFactoryBean">
        <!-- 扩展名至mimeType的映射,即 /user.json => application/json -->
        <property name="favorPathExtension" value="true" />
        <!-- 用于开启 /userinfo/123?format=json 的支持 -->
        <property name="favorParameter" value="true" />
        <property name="parameterName" value="format" />
        <!-- 是否忽略Accept Header -->
        <property name="ignoreAcceptHeader" value="false" />
        <property name="mediaTypes">
            <map>
                <entry key="json" value="application/json" />
                <entry key="xml" value="application/xml" />
            </map>
        </property>
        <!-- 默认的content type -->
        <property name="defaultContentType" value="text/html" />
    </bean>
    <!-- freeMarker -->
    <bean id="freemarkerConfig"
          class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
        <property name="templateLoaderPath" value="/freemarker/" />
    </bean>
    <bean
            class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver">
        <property name="contentNegotiationManager" ref="contentNegotiationManager" />
        <property name="viewResolvers">
            <list>
                <bean id="viewResolver"
                      class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
                    <property name="cache" value="true" />
                    <property name="prefix" value="" />
                    <property name="suffix" value=".ftl" />
                    <property name="contentType" value="text/html; charset=utf-8" />
                </bean>
            </list>
        </property>
        <property name="defaultViews">
            <list>
                <!-- JSON -->
                <bean
                        class="org.springframework.web.servlet.view.json.MappingJackson2JsonView" />
                <!-- XML -->
                <bean class="org.springframework.web.servlet.view.xml.MarshallingView">
                    <property name="marshaller">
                        <bean class="org.springframework.oxm.xstream.XStreamMarshaller" />
                    </property>
                </bean>
            </list>
        </property>
    </bean>
</beans>
```

3. UserDao，User数据库访问接口
```java
@Component("UserDao")
public interface UserDao {
    @Select("SELECT * FROM UserBalance")
    public List<User> getUserList();
}
```

3. UserService，User对应的业务逻辑层
```java
@Component
public class UserService {

    @Resource(name = "UserDao")
    private UserDao dao;

    public UserDao getDao() {
        return this.dao;
    }

    public List<User> getUserList() {
        return this.dao.getUserList();
    }
}
```

4. MyController，控制层，调用业务逻辑UserService，返回对应的视图名称和数据
```java
@Controller
public class MyController {
    @Resource
    private UserService service;

	@RequestMapping("/getResource")
        public ModelAndView getResource() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("users");
        mav.addObject("userList", service.getUserList());
        return mav;
    }
}
```

6. users.ftl 使用FreeMarker渲染的视图文件
```xml
<html>
<head>
    <title>userList</title>
</head>
<body>
<table border="1px">
    <thead>
    <tr>
        <td>userId</td>
        <td>userName</td>
    </tr>
    </thead>
    <tbody>
    <#list userList as user>
    <tr>
        <td>${user.userId}</td>
        <td>${user.balance}</td>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
```

## 结果展示

1. html后缀
`http://localhost:8080/api/getResource.html`
![](/home/codeplay2017/研究生/学习/网易/web入门/Spring框架/作业/web/结果-html后缀.png)

2. json后缀
`http://localhost:8080/api/getResource.json`
![](/home/codeplay2017/研究生/学习/网易/web入门/Spring框架/作业/web/结果-json后缀.png)

3. xml后缀
`http://localhost:8080/api/getResource.xml`
![](/home/codeplay2017/研究生/学习/网易/web入门/Spring框架/作业/web/无后缀.png)

4. JSON头
`curl -H 'Accept:application/json' http://localhost:8080/api/getResource`
![](/home/codeplay2017/研究生/学习/网易/web入门/Spring框架/作业/web/结果-json头.png)

5. xml头
`curl -H 'Accept:application/xml' http://localhost:8080/api/getResource`
![](/home/codeplay2017/研究生/学习/网易/web入门/Spring框架/作业/web/结果-xml头.png)
