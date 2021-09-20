# atcrowdfunding
尚硅谷尚筹网Java项目实战开发教程(含SSM框架,微服务架构,封捷主讲)

后台管理系统
1.1 搭建环境
  使用 Maven 作为构建管理和依赖管理工具。
  使用 SpringMVC 作为 Web 层框架。
    普通请求： 返回响应体通常为一个页面
    Ajax 请求： 返回响应体通常为一个 JSON 数据
  使用 MyBatis 作为持久化层框架。
  使用 MyBatis 的 PageHelper 插件实现数据的分页显示。
    Admin 数据
    Role 数据
  使用 Spring 提供的容器管理项目中的组件。
    XxxHandler
    XxxService
    XxxMapper
    XxxInterceptor
    XxxExceptionResolve
  前端技术
    Boostrap 作为前端样式框架
    使用 layer 作为弹层组件
    使用 zTree 在页面上显示树形结构
  借助 SpringMVC 提供的异常映射机制实现项目中错误消息的统一管理
    基于注解
    基于 XML
  通过对请求消息头信息的判断在给出异常处理结果时实现了普通请求和 Ajax 请求的兼容

2 前台会员系统
2.1 搭建环境
  搭建环境
  SpringBoot+SpringCloud
  SpringBoot
  SpringSession
  Thymeleaf
  Redis
  MyBatis

SpringCloud
  Eureka： 注册中心
  Feign： 远程接口的声明式调用
  Ribbon： 客户端负载均衡
  Zuul： 网关， ZuulFilter 过滤

2.2 用户登录、 注册
  调用第三方接口给用户手机发送短信验证码
  使用 BCryptPasswordEncoder 实现带盐值的加密
  使用 SpringSession 解决分布式环境下 Session 不一致问题
  使用 Redis 作为 SpringSession 的 Session 库
  在 Zuul 中使用 ZuulFilter 实现登录状态检查
  在 Zuul 中配置访问各个具体微服务的路由规则
  
  
  https://www.bilibili.com/video/BV1bE411T7oZ?spm_id_from=333.788.b_636f6d6d656e74.32
