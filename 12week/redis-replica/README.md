- 启动

redis-server redis6379.conf

redis-server redis6380.conf

- redis-cli 连接

redis-cli -p 6379

redis-cli -p 6380

- 增加slave

redis6380> slaveof 127.0.0.1 6379

- 如果要在配置文件中设置为replica

replicaof 127.0.0.1 6380

replicaof ::1 6380

- 使用info 查看Redis信息

info

- set只能在主redis 上做设置，在从上set会报错 
(error) READONLY You can't write against a read only replica




