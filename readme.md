# 一个分布式系统实现Demo

## 概述

* 本demo只是为了练习，回顾一下之前的掌握的内容，因长时间不用这块，有点生疏了，所以拿来再熟练一下，大家对这个感兴趣的话，可以来瞧瞧，或者提供改进也是可以的。 大家对这块不熟悉的话，正好也可以掌握一下，如何用反射实现动态方法调用，而且还是分布式的哦！

* 主要采用zookeeper来实现分布式的服务。使用反射来实现任务的执行。
* 本demo只是简单的去实现这个功能，其中有一个小问题（基本类型int、long声明的方法变量，不能用Class.forName获取Class信息，导致反射失败！已经用TODO标出），如果大家能解决的话，可以帮忙解决一下，让我们大家一起进步！


## 流程：

zookeeper来提供客户端发现服务，服务端会向zk提供服务信息、端口。
客户端通过zk来获取服务端信息，然后通过服务端信息使用socket连接到服务端，发送任务。


## 实现：

* Client：客户端主要实现
* Server：服务端主要实现

## 启动

* ClientApp：入口类，启动客户端
* ServerApp：入口类，启动服务端

## 注意

使用本demo的话，请本地启动zookeeper，或者修改zk的地址和端口！
