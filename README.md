# diy-rpc

## 介绍

这是一个基于Netty的RPC框架、

这个项目涉及到了jdk的动态代理屏蔽调用rpc接口的实现细节，Netty的一些常用工具类，序列化反序列化，SPI机制，Sping等，是一个用来学习实践的轮子

这个项目是参考Guide哥的rpc项目：**[guide-rpc-framework](https://github.com/Snailclimb/guide-rpc-framework)**

目前初步完成所有整体的逻辑，还差多种负载均衡的策略以及序列化方式的实现，另外后面还会考虑将nacos注册中心加入到项目中，并使用配置文件的方式配置注册中心，序列化方式等

## 目录结构

```txt
卷 系统 的文件夹 PATH 列表
卷序列号为 D5EC-3327
C:.
├─.idea #idea文件
│  ├─inspectionProfiles
│  └─libraries
├─diy-rpc-commons #这个模块下面存放通用的一些工具类，常量
│  └─src
│      └─main
│          ├─java
│          │  └─cuit
│          │      └─epoch
│          │          └─pymjl
│          │              ├─annotations
│          │              ├─enums
│          │              ├─exception
│          │              ├─factory
│          │              ├─spi
│          │              └─utils
│          │                  └─concurrent
│          │                      └─threadpool
│          └─resources
│              └─META-INF
├─diy-rpc-core	#这是项目的核心代码，下面包含了整个框架的Socket和Netty两种实现
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─cuit
│      │  │      └─epoch
│      │  │          └─pymjl
│      │  │              ├─annotations #自定义注解
│      │  │              ├─compress #压缩的接口实现，采用gzip
│      │  │              │  └─gzip
│      │  │              ├─config #负载均衡的策略
│      │  │              ├─loadbalance
│      │  │              │  └─loadbalancer
│      │  │              ├─proxy #jdk的动态代理
│      │  │              ├─registry #这是提供zookeeper相关服务的包
│      │  │              │  ├─provider
│      │  │              │  │  └─impl
│      │  │              │  └─zookeeper
│      │  │              ├─remote #这是网络传输的包，包含Netty和Socket两种方式
│      │  │              │  ├─constants #常量
│      │  │              │  ├─entity #实体类
│      │  │              │  ├─handler #代理类
│      │  │              │  └─transport #网络传输的实现
│      │  │              │      ├─netty
│      │  │              │      │  ├─client
│      │  │              │      │  ├─codec
│      │  │              │      │  └─server
│      │  │              │      └─socket
│      │  │              ├─serialize #序列化方式
│      │  │              │  └─kyro
│      │  │              └─spring #Spring相关，主要是使用前置后置处理器，以及自定义扫描包，类
│      │  └─resources
│      │      └─META-INF #这里存放zookeeper的配制文件，以及SPI机制的文件
│      │          └─diy-rpc  
│      └─test #一些单元测试
│          ├─java
│          │  └─cuit
│          │      └─epoch
│          │          └─pymjl
│          │              ├─compress
│          │              ├─loadbalance
│          │              ├─registry
│          │              ├─serialize
│          │              ├─spi
│          │              └─transport
│          └─resources
│              └─META-INF
│                  └─diy-rpc
├─diy-rpc-example-client #样例代码客户端
│  └─src
│      └─main
│          ├─java
│          │  └─cuit
│          │      └─epoch
│          │          └─pymjl
│          │              └─controller
│          └─resources
│              └─META-INF
├─diy-rpc-example-server #样例代码服务端
│  └─src
│      └─main
│          ├─java
│          │  └─cuit
│          │      └─epoch
│          │          └─pymjl
│          │              └─service
│          │                  └─impl
│          └─resources
│              └─META-INF
├─diy-rpc-learn-demo #这个模块是在学习框架过程中写的各种测试demo，包括netty的心跳机制，传输，spi等等
│  └─src
│      └─main
│          ├─java
│          │  └─cuit
│          │      └─epoch
│          │          └─pymjl
│          │              ├─annotations #自定义注解的测试样例
│          │              │  ├─entity
│          │              │  └─reflection
│          │              ├─curatordemo #zookeeper客户端Curator的测试样例
│          │              │  └─curator
│          │              ├─kryodemo #序列化方式kryo的测试样例
│          │              │  └─entity
│          │              ├─nettydemo #netty实现一个简单的rpc（传输对象）的样例
│          │              │  ├─client #客户端
│          │              │  ├─codec #编解码器
│          │              │  ├─entity #实体类
│          │              │  └─server #服务端
│          │              ├─nettyidledemo #netty的心跳机制测试demo
│          │              │  ├─handler
│          │              │  └─transport
│          │              ├─proxydemo #jdk的动态代理和cglib的测试demo
│          │              │  ├─dynamicproxy
│          │              │  │  ├─cglib 
│          │              │  │  └─jdk
│          │              │  └─staticproxy #静态代理demo
│          │              │      └─impl
│          │              └─spi #spi机制的测试demo
│          │                  └─impl
│          └─resources
│              └─META-INF
│                  ├─diy-rpc #这是模仿dubbo实现的spi机制的配制文件
│                  └─services #这是java自带的spi机制的配制文件
└─diy-rpc-public #这是存放公共接口的模块，即给diy-rpc-example-client模块调用的rpc接口
    └─src
        └─main
            └─java
                └─cuit
                    └─epoch
                        └─pymjl
                            └─service

```



