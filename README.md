# AndroidFrameworkBase 安卓基础框架
### 本程序是基于Android Studio 2搭建的，致力于快速开发数据管理系统的安卓基础框架。
##### 本程序数据管理及用户管理功能基于LeanCloud提供的数据存储功能，在开发程序前需要在云端创建应用，并创建数据表将对应信息初始化到程序中
本框架修改于AS Navigation Drawer Activity模版，添加了一些常用工具类，用于快速实现开发：
- 添加Activity基类、Fragment基类等类集成常用功能用于快速搭建页面
- 添加用户管理器和示例页面可快速实现用户的登录、注册、自动登录、检验登录等功能
- 添加三种数据实体接口和对应管理器，实现对SQLite数据库、LeanCloud数据库、本地云端数据同步的快速管理，通过将数据转化为实体，可以方便地展示在ListView等控件中
- 更多常用工具类持续更新中……

## 开发准备
#### 复制文件
- 在Android Studio中打开本框架，若成功创建名为“基础框架”的应用则已成功配置
- 创建本框架的副本，作为新应用的基础文件、修改根目录文件夹名为自定义名称
- 在AS中打开副本文件夹，即可开始进行项目的开发

#### 修改包名
- 参考[修改包名](http://www.jianshu.com/p/557e1906db1a)中的方法将com.xmx.androidframeworkbase修改为新应用的包名
- 修改包名后即可尝试打包生成新应用，开始开发调试

#### 修改应用名
- 打开res/values/strings.xml文件，其中包含了一下常用的提示语等字符串，修改app_name的值即可修改应用名

####修改启动界面
- 修改res/values/splash.png为自定义图片，即可在打开APP时看到启动启动界面

#### 云端初始化
- 在[LeanCloud](https://leancloud.cn/)注册帐号，创建一个新应用
- 在云端控制台设置中，在应用Key页面查看应用信息，将对应信息保存在java/Constants类中APP_ID和APP_KEY常量中

#### 用户表初始化
- 在存储页面分别创建：管理用户帐号密码信息的表、管理用户基本数据的表、管理登录日志的日志表。将表名分别存于java根目录Constants类中：USER_INFO_TABLE、USER_DATA_TABLE、LOGIN_LOG_TABLE常量中。这些的表的权限需要设置为无限制
- 用户表初始化后即可实现登录注册功能
- 可以使用java/User/LoginActivity和RegisterActivity并修改res/drawable/login.png文件即可快速实现登录注册界面
- 根据具体需要可以修改Activity的样式等实现自定义效果，或新建Activity并调用UserManager用户管理器中的方法即可实现登录注册功能
- 注册帐号并登录后即可进入应用主界面

## 自定义页面
- 本框架主体全部运行在MainActivity中，通过ViewPager对自定义页面进行管理
- MainActivity的initView方法fragments和titles对应保存着要显示的Fragment和其标题。
- 框架中已添加的SQLFragment、CloudFragment、SyncFragment是分别用于演示SQLite数据库、LeanCloud数据库、本地云端数据同步的Fragment，不将其添加至列表即可不再显示，在java根目录下的SQL、Cloud、Sync可以查看对应实体管理器的使用方法
- 要添加自定义页面，只需创建好Fragment，之后将其添加到对应的列表即可，添加顺序即为滑动显示顺序
