
## Dubbo知识点整理
    
    注册中心zookeeper存储的根结点是对应 <dubbo:register group="dubbo"/>中的group属性。
    

服务消费者会从zookeeper上拿到一份服务列表，并存储在本地文件中。这里如果一台机子部署多个服务的话，即多个jvm进程，这时有可能会发生这个本地文件发生竞争，而抛出异常。但dubbo对于这块异常是会重试的，所以不用担心。　



