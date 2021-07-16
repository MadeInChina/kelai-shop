客来商城是基于broadleaf commerce 二次开发的B2C商城，可直接用于生产。broadleaf commerce拥有良好的架构设计，高可定制性，任何功能模块都可替换、扩展，非常的适合中小企业二次开发。

# 演示

前台Demo：http://kelai.lianjieweilai.net/<br/>
管理后台Demo：http://kelai.lianjieweilai.net/admin/login<br/>
管理后台用户名：admin 密码：admin<br/>
# 技术栈

Spring Boot、Spring Security、Solr、 Hibernate 、 Quartz

# 优秀特性

1、可扩展的模块化设计。 几乎每个方面都可以被重写、扩展或修改，以增强或更改默认行为，以最适合您的需求，包括所有的服务、数据访问对象和实体。

2、Solr搜索。 Solr的集成提供了性能良好的、灵活的搜索功能。

3、灵活的促销系统。 高度可配置的促销系统，我们提供几个可应用促销的标准级别：订单级别、订单项级别、运费级别。

# 主要功能

商品管理、订单管理、广告管理、推荐商品管理、CMS管理、促销、门店管理、运费模板、CRM、用户管理、权限管理

首页

![avatar](https://oscimg.oschina.net/oscnet/up-931e13b8b888396c9898395ccd345faea84.png)

商品列表页

![avatar](https://oscimg.oschina.net/oscnet/up-45ba19c9845a03b42ab0be74257ca9e4128.png)

购物车页面

![avatar](https://oscimg.oschina.net/oscnet/up-dae37d78320ca929b4f5dd5bee130a795b8.png)

支付页面

![avatar](https://oscimg.oschina.net/oscnet/up-e1ab9fe0671ced0d3fdd15c75a0b1866ed2.png)

控制面板

![avatar](https://oscimg.oschina.net/oscnet/up-583ba2e0a9f2ee27a354cc920d7f4edd8c1.png)

商品管理

![avatar](https://oscimg.oschina.net/oscnet/up-5d29966d66b4340b824dd0f3d343b2253a0.png)

内容管理

![avatar](https://oscimg.oschina.net/oscnet/up-3ca5d65bbc2af51da99a795cbe3442e3d28.png)

运费模板

![avatar](https://oscimg.oschina.net/oscnet/up-8d3e7ccfabf3b8e03cc5c3f96052fa4b316.png)

系统配置
![avatar](https://oscimg.oschina.net/oscnet/up-3cf321cf411366c3152f8a9d3bd172f174b.png)


# 客来商城在原来基础上新增了如下特性

1、对管理后台进行了汉化

2、增加了支付宝、微信支付方式。

3、可视化页面编辑

4、增加了门店功能及门店自提功能

5、优化了用户订单列表

6、增加了文章管理模块

7、对手机端浏览器做了适配

8、运费模板

# 运行环境

系统：windows、linux

JDK：JDK8

数据库：mysql 5.7

# 如何在本机运行商城
1、下载solr并运行，商城使用solr搜索

2、创建数据库，名称随意

3、使用intellij idea打开项目，在kelai-core/src/main/resources/runtime-properties/common-shared.properties修改数据库链接

4、修改kelai-admin/src/main/resources/runtime-properties/default.properties里的配置，把update改为create

blPU.hibernate.hbm2ddl.auto=update
blEventPU.hibernate.hbm2ddl.auto=update
blSecurePU.hibernate.hbm2ddl.auto=update

5、运行。第一次运行会执行数据的初始化，会比较久，请耐心等待，初始化完毕后把第三步的create改回update

# 未来规划

微信小程序，手机APP，分销，功能优化，交互及界面优化，加入更多门店相关的功能

# 版权 
1.允许用于个人学习研究使用

2.禁止将客来商城代码和资源以任何形式、名义进行出售

3.限制商用，如果商业使用请联系我们，QQ188899987

# 文档地址
http://kelai.lianjieweilai.net/doc/index.html
