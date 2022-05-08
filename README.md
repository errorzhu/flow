# flow - simple , light , pluginable
## 简单,轻量, 插件化的数据同步工具
## 编译
```
mvn clean package
```
## 部署
```
tar -zxvf assemblly/target/flow.-x.y.z.tar.gz
```
## 使用
```
cd flow-x.y.z
cd bin
./flow -config ../examples/default.conf -plugins ../plugins
```
# TODO
1. activemq
2. kafka

