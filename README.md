# 来都来了，点个Star呗

> 目标不大，看能不能把十几二十行的代码优化成两三行<br/>
顺便对代码设计做些研究，同时也方便自己的开发<br/>
如果能让各位也能节省一点点时间就再好不过了（手动狗头）

- 内容主要来源于日常开发
- 长期维护并同步中央仓库
- 欢迎提bug&火速优化bug

### 手机突然收不到邮件issue的提醒，有问题加V（备注github）：linyuzai1909

# 目录

- 插件化动态加载外部 jar 中的 Class
  - [1.x.x：插件化动态加载外部 jar 中的 Class](../../wiki/Concept-Plugin)
  - [2.x.x：支持嵌套和压缩包，可视化管理页面](../../wiki/Concept-Plugin-2)
  - [3.x.x：支持集群环境，支持动态接口](../../wiki/Concept-Plugin-3)
- 一个注解实现下载接口
  - [1.x.x：一个注解实现下载接口](../../wiki/Concept-Download)
  - [2.x.x：支持异步消费，压缩加密，SpEL](../../wiki/Concept-Download-2)
- 长连接在服务集群场景下的解决方案
  - [1.x.x：一个配置注解实现 WebSocket 集群方案](../../wiki/Concept-WebSocket-LoadBalance)
  - [2.x.x：支持 WebSocket & Netty，支持 Redis & RabbitMQ & Kafka 转发（支持主从切换）](../../wiki/Concept-Connection-LoadBalance)
- [异步回调转为同步返回](../../wiki/Concept-Sync-Waiting)
- [协同开发之动态路由](../../wiki/Concept-Router)
- [基于多个 Kafka & Rabbitmq 构建的事件模型](../../wiki/Concept-Event)
- [属性继承插件（伪 Java 多继承）](../../wiki/Concept-Inherit)
- [MapQueue：支持元素更新的 Queue](../../wiki/Concept-MapQueue)
- Spring Cloud & Spring Boot 基建
  - [模块化项目生成插件（IDEA）](../../wiki/Concept-Cloud-Plugin-Intellij)
  - [全局请求响应拦截](../../wiki/Concept-Cloud-Web)
 
# 计划

- 插件3.0 (插件工厂重构，支持AWS，支持Yaml，插件大小和嵌套限制，插件加载追踪)
- job组件
