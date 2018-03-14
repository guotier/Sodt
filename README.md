<link href="http://statics.1024tools.com/css/markdown/github.css" rel="stylesheet" />

# Sodt框架对二阶段算法和Base理论的实现


Try：尝试执行业务
```sh
并不对业务数据进行修改，如果失败，也对业务本身没有任何影响
```


Confirm：确认提交业务
```sh
此阶段结束，事务真正被提交，业务数据真正被修改。
如果此阶段失败，框架自身会记录此失败的事务，之后会不断进行重试，直至事务最终被提交
此方法需保证幂等
```


Cancel：取消提交事务
```sh
此阶段用以回滚事务。
如果此阶段失败，框架本身会记录此失败的事务，之后会不断进行重试，直至事务最终被回滚
此方法需保证幂等
```


# Sodt框架使用指南


1.在需要分布式事务的工程中引入SODT的二方包

```sh
	<dependency>
	    <groupId>com.lizi</groupId>
	    <artifactId>sodt</artifactId>
	    <version>0.01-SNAPSHOT</version>
	</dependency>
```

2.加载sodt.xml
```sh
	<context-param>
	    <param-name>contextConfigLocation</param-name>
	    <param-value>classpath:sodt.xml</param-value>
	</context-param>
```



3.配置数据库
数据库的作用是记录那些需要重试的事务（当事务状态是confirming或者canceling的时候，事务需要进行重试）

数据库的配置文件是sodt.properties
示例文件：
```sh
##for mysql
#
# 本框架默认对全局事务采用数据库持久化的方案
# 需要为本框架配置数据源
# 默认采用MySql作为数据库
#
# 数据库驱动类
sodt.jdbc.driverClassName=com.mysql.jdbc.Driver
# 数据库连接url
sodt.jdbc.url=jdbc:mysql://192.168.10.161:3306/b2b_guotie?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true
# 数据库用户名和密码
sodt.jdbc.username=root
sodt.jdbc.password=root
# 数据库连接池初始化大小 使用者根据自己的业务来定义大小
sodt.jdbc.initialSize=5
# 数据库连接池队列中最小空闲数 使用者根据自己的业务来定义大小
sodt.jdbc.minIdle=1
# 数据库连接池队列中最大空闲数 使用者根据自己的业务来定义大小
sodt.jdbc.maxIdle=30
# 数据库连接池队列中最大等待数 使用者根据自己的业务来定义大小
sodt.jdbc.maxWait=3000
# 数据库连接池队列中最大的活跃数 使用者根据自己的业务来定义大小
sodt.jdbc.maxActive=50
```


# 发布Sodt服务

	- 一个服务方法要支持SODT服务的话，需要加上@Sodt注解
	- 在Sodt注解里面有两个属性，confirmMethod和cancelMethod，这两个属性对应事务尝试成功或者失败的时候，框架会自动调用的方法，必须配置
	- confirmMethod和cancelMethod两个方法的参数必须和tryMethod的方法参数保持一致

# 后续计划

	- 目前的事务没有配置传播属性，后续会加上这个属性
	- 目前框架只支持dubbo，后续会支持更多的方式，本质上来讲，只要是远程调用，框架都是可以支持的