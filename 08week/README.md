# 08周作业说明 超越分库分表/分布式事务

### 2（必做）设计对前面的订单表数据进行水平分库分表，拆分 2 个库，每个库 16 张表。并在新结构在演示常见的增删改查操作。
https://github.com/CoderMaxYu/javaCourseCodes/tree/main/08week/shardingJdbc

### 6（必做）基于 hmily TCC 或 ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo（二选一），提交到 Github。
https://github.com/CoderMaxYu/javaCourseCodes/tree/main/08week/hmily-tcc-demo

- bank2 增加金额： http://localhost:8802/bank2/transfer?amount=20.0
- bank1 账户余额：http://localhost:8801/bank1/get?name=zs
- bank1转账给bank2: http://localhost:8801/bank1/transfer?name=zs&amout=60.0



