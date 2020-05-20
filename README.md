# DistributedTransaction  
DistributedTransaction  
测试jar包：transaction.jar  
## 1传统使用方式  
1.1导入transaction.jar包  
![Image text](https://github.com/awuawu/DistributedTransaction/blob/master/src/main/resources/static/img/jar1.png)  
1.2启动类加入注解@EnableAspectJAutoProxy  
![Image text](https://github.com/awuawu/DistributedTransaction/blob/master/src/main/resources/static/img/main1.png)  
1.3在业务方法上加注解@MyTransactional  
![Image text](https://github.com/awuawu/DistributedTransaction/blob/master/src/main/resources/static/img/service1.png)  
## 2微服务方式  
2.1导入transaction.jar包  
![Image text](https://github.com/awuawu/DistributedTransaction/blob/master/src/main/resources/static/img/jar2.png)  
2.2启动类加入注解@EnableAspectJAutoProxy  
![Image text](https://github.com/awuawu/DistributedTransaction/blob/master/src/main/resources/static/img/main2.png)  
2.3在业务方法上加注解@MyTransactional  
![Image text](https://github.com/awuawu/DistributedTransaction/blob/master/src/main/resources/static/img/service21.png)  
![Image text](https://github.com/awuawu/DistributedTransaction/blob/master/src/main/resources/static/img/service22.png)  
![Image text](https://github.com/awuawu/DistributedTransaction/blob/master/src/main/resources/static/img/service23.png)  
