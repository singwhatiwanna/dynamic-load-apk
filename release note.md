## DL 1.0开发规范：

1. 目前不支持service
2. 目前只支持动态注册广播
3. 目前支持Activity和FragmentActivity，这也是常用的activity
4. 调用Context的时候，请适当使用that，大部分常用api是不需要用that的，但是一些不常用api还是需要用that来访问。that是apk中activity的基类BaseActivity系列中的一个成员，它在apk安装运行的时候指向this，而在未安装的时候指向宿主程序中的代理activity，由于that的动态分配特性，通过that去调用activity的成员方法，在apk安装以后仍然可以正常运行。
5. 慎重使用this，因为this指向的是当前对象，即apk中的activity，但是由于activity已经不是常规意义上的activity，所以this是没有意义的，但是，当this表示的不是Context对象的时候除外，比如this表示一个由activity实现的接口。
6. 目前支持style和系统主题，暂不支持自定义主题




## DL版本更新日志


1. DL 1.0.0     2014.10.31
更新了ui bus，并实现从fragment中启动activity

2. DL 2.0.0     2014.12.07    DL支持多进程模式，使插件运行在单独的DL进程中（代码在lab分支）

3. DL开始支持插件中的so库    2014.12.13 （代码在dev分支）