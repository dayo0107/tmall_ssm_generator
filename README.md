# tmall_ssm_generator
SSM project
## 项目介绍
模仿天猫商城  分为前台商城和后台管理两大部分
## 环境
idea2018.1.4 mysql8.0（alibaba连接池） tomcat9.0 jdk10

## 注意事项

1.此项目环境与原项目的开发环境不同，相互之间无法正常部署，请自行选择，非要部署需要自行更改很多jar包版本和设置。

2.因为边学边模仿边修改的，和原项目代码有些不同，但功能大致相同，增加了后台管理员登陆和注销功能

3.已知bug：
     
    firstupload
    一、前台jsp中 我的订单、购物车、结算页面有bug.
       原因: 均是在客户端jsp页面中的“删除的操作”并没有把客户端上的数据进行删除或更新，
             而是使用.hide()隐藏，点击其他按钮可能会把隐藏的数据再次显示。
       解决办法：可用重定向再次刷新解决此问题。
    
    二、前台右上角购物车数量没有实时更新，需要刷新页面或跳转。
        解决办法：可用fly.jsp解决
        
    version1.1
    修复imgAndInfo.jsp加入购物车后 top.jsp的购物车数量不改变的bug
原项目来源：http://how2j.cn/k/tmall_ssm/tmall_ssm-1399/1399.html?p=52696
## 表关系图
（后台管理员登陆使用的表 admin_ 单独,无关联）
<img src="https://github.com/dayo0107/tmall_ssm_generator/blob/master/biao.png"/>
## 前台需要登陆的功能流程图
<img src="https://github.com/dayo0107/tmall_ssm_generator/blob/master/cart.png"/>
