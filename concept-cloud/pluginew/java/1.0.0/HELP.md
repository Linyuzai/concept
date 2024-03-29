# 说明

### 目的

使用该方式搭建项目可以对模块进行任意的组合

假设有`A`，`B`，`C`三个模块

- 我们可以实现`A`，`B`，`C`分别作为一个服务的微服务
- 我们可以实现`AB`合并为一个服务，`C`单独为一个服务的微服务
- 我们可以实现`AC`合并为一个服务，`B`单独为一个服务的微服务
- 我们可以实现`BC`合并为一个服务，`A`单独为一个服务的微服务
- 我们可以实现`ABC`合并为一个单体服务

同时，我们也可以将某种技术抽象出来

如服务间调用，可以实现两个单独的模块`rpc-feign`和`rpc-dubbo`，并可以任意选择其中一种

便于在不修改内部代码的前提下，优化替换各种技术实现

### 适用场景

- 场景1

项目版本较多，不同项目在功能上存在出入，定制化功能用多分支开发难以维护，其他项目难以复用某个定制化功能

这种情况可以将定制化功能作为单独的模块方便任意组合，通过代码设计提供某个模块的插件式集成，就可以灵活地进行选择

- 场景2

想要先使用单体应用快速搭建一个项目，但又担心后续需要大量重构进行微服务改造

该项目的构建理念完美支持上述问题，只要规范编码，可以通过不同的启动服务选择单体或微服务

### 模块

> **application**
>
> 启动模块，通过依赖其他模块将各个功能任意组合成一个服务
>
> 可根据业务需要，自定义其他的启动模块
>
>> application-boot
>>
>> 单体应用启动模块
>
>> application-cloud-gateway
>>
>> 微服务网关启动模块
>
>> application-cloud-sample
>>
>> 微服务示例启动模块
>
>> application-cloud-user
>>
>> 微服务用户启动模块
>
> **basic**
> 
> 基础模块，可在其中定义通用组件
>
>> basic-boot
>>
>> 单体应用基础模块，可在其中定义单体应用的通用组件
>
>> basic-cloud
>>
>> 微服务基础模块，可在其中定义在微服务的通用组件
>
> **domain**
>
> 领域模块，主要用于定义标准化的领域模型
>
>> domain-sample
>>
>> 示例领域，可参考其中的实现，并不强制
>
>> domain-user
>>
>> 用户领域，可直接使用，也可自己另外实现
>
> **module**
>
> 业务模块，主要实现领域模块中的接口以及对应的`Controller`
>
>> module-sample
>>
>> 示例业务
>
>> module-user
>>
>> 用户业务
>
> **login**
>
> 登录模块
>
>> login-username
>>
>> 使用用户名登录的功能模块，也可自行添加`login-phone(手机号登录)`，`login-qrcode(二维码登录)`等其他登录模块
>
> **rpc**
>
> 远程调用模块
>
>> rpc-feign
>>
>> 基于`Feign`的远程调用模块实现，也可以自行添加`rpc-dubbo`，`rpc-grpc`等其他远程调用模块
>
> **token**
>
> `Token`模块
>
>> token-jwt
>>
>> 基于`jwt`实现的`Token`模块，可以自行添加`token-sa`，`token-session`等其他`Token`模块

### build.gradle

使用`allprojects`进行全局配置，每个模块只需要单独配置特有的内容，不需要相同的重复配置

### 掘金专栏

专栏中对于一些功能思路有讲解，还有一些示例

https://juejin.cn/column/7140131104270319629

### 注意

默认扫描路径为`{group}.{artifact}.module.*.config`