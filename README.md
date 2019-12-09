# LarkVote
LarkVote投票系统是一套基于规则的企业内部投票系统，由投票管理和投票端两部分组成，投票管理端包括投票创建、投票编辑、规则编辑、投票发起、投票项管理、邀请码生成、结束投票、投票结果统计等功能；投票端包括投票规则展示、投票操作、提交结果等功能。目前该项目还处于初级阶段，欢迎各位大神创建pull request。

## 主要技术
SpringBoot v1.5.3

Thymeleaf v2.1.5

EasyExcel v2.0.0

FastJson v1.2.60

H2Database v1.4.194 

Bootstrap v4.3

## 部署方式
项目使用工具

IntelliJ IDEA、JDK 1.8、apache-maven-3.3.9

使用IDEA导入项目，启动main方法即可，由于本项目使用的是H2数据库，所以不需要安装数据库。
## 平台设计
安全性设计，自定义注解使用数据库的方式做特殊接口安全性。

数据层设计，使用spring-jpa做数据层接口查询，写法简便便于集成。

全局异常设计，统一处理异常转发到error页面。

认证授权设计，使用spring-security做用户认证。

动态投票项设计，支持投票项模板下载，excel导入动态生成投票项数据。

邀请码唯一性设计，每一个投票轮生成的邀请码都是唯一的，不会产生一个邀请码对应多个投票的情况。

## 投票过程

|--------- 投票管理，创建投票项  
|------------- 投票管理，设置投票轮1  
|--------------- 投票管理，动态导入投票项  
|--------------- 投票管理，发起投票  
|--------------- 投票管理，生成投票邀请码  
|--------------- 投票端，用户点击邀请码，根据规则开始进行投票  
|--------------- 投票管理，根据规则，选择结束投票  
|--------------- 投票管理，统计、动态生成统计结果，并依据结果进行划分序列  
|------------- 投票管理，设置投票轮2  
|--------------- 投票管理，使用上一轮投票项  
|--------------- 投票操作如上  
|--------------- 筛选得出结论  

## 注意

1、每一个邀请码对应的地址都可以通过点击下一轮时，进入到下一轮次投票，确保用户投票时的灵活性。

2、每一个轮次结束后，用户都可以看到该轮次的投票统计结果。

3、一个用户电脑只能投一次，确保没有刷票。

4、投票创建人可以导出每一轮次的投票统计结果。

## demo
### 主页
![image](https://raw.githubusercontent.com/hollykunge/Votes/master/img/home.png)
### 列表
![image](https://raw.githubusercontent.com/hollykunge/Votes/master/img/list.png)
### 编辑
![image](https://raw.githubusercontent.com/hollykunge/Votes/master/img/edit.png)
### 邀请码
![image](https://raw.githubusercontent.com/hollykunge/Votes/master/img/code.png)
### 投票1
![image](https://raw.githubusercontent.com/hollykunge/Votes/master/img/vote1.png)
### 投票2
![image](https://raw.githubusercontent.com/hollykunge/Votes/master/img/vote2.png)
### 投票3
![image](https://raw.githubusercontent.com/hollykunge/Votes/master/img/vote3.png)
