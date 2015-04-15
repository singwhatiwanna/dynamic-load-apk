#关于sample
这个目录包含了DL架构的sample，目前共有两个：main和depend_on_interface

##main
演示了DL的所有基本功能，建议刚接触DL架构的朋友先看这个sample。
共包含一个Host和两个Plugin。
其中Plugin-A是主要的Plugin，建议先看这个。
Plugin-B使用了前面介绍的第三种开发模式。

##depend_on_interface
这个sample演示了第二种开发模式，通过预装先定的接口（放在doi-common里），可以实现Plugin访问宿主。



