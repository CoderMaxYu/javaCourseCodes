GC总结
=
    GC,Garbage Collection,一般译为 “垃圾回收”或者“垃圾收集”。
    在Java中，是JVM 的内存管理。
    JVM常见的垃圾回收算法有以下几种:


----
串行GC
- 配置-XX:+UseSerialGC 开启
- 单线程
- 新生代使用标记复制算法，暂停所有用户线程(STW)
- 老年代使用标记整理算法，暂停所有用户线程(STW)
- 适合几百M堆内存的JVM，适用于单CPU环境中，没有线程开销，可以获得最大的效率，例如运行在client模式下的JVM

------
    
------
----
并行GC
- 配置 -XX:+UseParallelGC 开启
- 多线程，通过-XX:ParallelGCThreads=value 来指定GC 线程数，默认值为CPU核心数
- 设置最大停顿时间:-XX:MaxGCPauseMillis=value’
- 设置吞吐量: -XX:GCTimeRatio=N  （ GC时间和用户时间比例 = 1 / (N+1) ）
- jdk8 server 模式下默认的GC收集器
- 新生代使用标记复制(mark-copy)算法，暂停所有用户线程(STW)
- 老年代使用标记整理(mark-sweep-sompact)算法，暂停所有用户线程(STW)
- 在gc 期间，所有的CPU 内核资源都被用来并行清理垃圾，总暂停时间更短
- 在没有执行GC的时候，不创建GC线程，所有资源都被用来处理业务
- 适用于多核服务器，主要目标是增加系统业务处理吞吐量
- Xmx=1g时，Young区最大大小 1024/3=341.3M

------
    
------
----
CMS（Concurrent Mark Sweep）GC
- 配置 -XX:+UseConcMarkSweepGC 开启
- 新生代使用并行STW方式的标记-复制(mark-copy)算法
- 老年代使用并发标记清除(mark-sweep)算法
- 不对老年代进行整理，使用空闲列表(free-lists)来管理内存空间的回收，在mark-and-sweep阶段的大部分工作和应用线程一起并发执行
- 默认情况下CMS使用的并发线程数等于 CPU核心数的 1/4
- 进行老年代的并发回收时，可能会伴随多次年轻代的minor  GC
- 适用于多核CPU服务器，对单次GC 停顿时间要求高的业务场景
- 缺点是老年代碎片不压缩的情况下，内存不连续，在堆内存较大的情况下会造成GC不可预测的暂停时间，
通过参数 -XX:CMSFullGCsBeforeCompation，设置执行多少次不压缩的GC后进行一次压缩
- JDK8，Xmx=1g时，Young区最大大小64M*GC线程数*13/10=332.8M

* CMS GC的6个阶段
    * 1.initial mark ，初始标记，需要STW。目标标记所有的根对象，
    包括跟对象直接引用的对象，以及被年轻代中所有存活对象所引用的对象(Remember Set)。
    * 2.concurrent mark，并发标记。目标是遍历老年代，从第一阶段找到的根对象开始标记所有的存活对象。
    * 3.concurrent preclean,并发预清理。对并发标记阶段因为并行产生的引用关系改变的区域进行脏区标记(card marking)。
    * 4.final remark,最终标记，需要STW。预清理阶段是并发的，还是存在标记不准确的情况，通过STW,可以把前面的脏区的
    引用关系完全理清。通常会在Young区对象比较少的时候执行最终标记，因为如果Young区存在大量对象的情况下，
    很有可能会立即晋升到老年区，会触发执行多次最终标记(STW)。
    * 5.concurrent sweep,并发清除。目标是引用关系确定后，并发清理不再使用的垃圾对象。
    * 6.concurrent reset,并发重置。目标是重置CMS 算法相关的内部数据，为下一次GC循环做准备。

------
    
------
----
 G1(Garbage-First)GC
- 配置 -XX:+UseG1GC 开启
- 设置预期控制GC暂停时间:-XX:MaxGCPauseMillis=200，默认200毫秒
- 目标是为了将STW停顿时间和分布，变成可预期且可配置的
- 不再使用年轻代，老年代的分代模式，将堆划分为多个region（通常2048个），region范围为1M到32M，
可以通过参数-XX:G1HeapRegionSize来进行配置
- 每个小块可能是Eden区，old区，survivor区
- 回收集CS,collection set,增量的方式来进行处理，不需要去整理整个堆空间
- 每次暂停的时候都会收集所有标记为年轻代的region，包含部分的老年代region
- 在并发阶段估算每个小堆块存活对象的总数，垃圾最多的小块会被优先收集，所以叫做G1。
- 一般适用于几十G+的堆内存
* 如果G1GC 触发了Full GC,会导致退化使用串行(Serial)收集器来完成垃圾清理工作，STW时间很长
    * 并发模式失败：G1 启动标记周期，但是在Mixed GC 之前，老年代就被填满了，这时G1放弃标记周期
    解决方法是 增加堆大小，或者调整周期(例如增加线程数-XX:ConcGCThreads等)
    * 晋升失败： 没有足够的内存供存活对象或晋升对象使用，因此触发了Full GC(to-space exhausted/to-space overflow)
        * 增加 -XX:G1ReservePercent 参数的值(同时增大总堆的大小)，增加预留内存量
        * 通过减少 -XX:+InitiatingHeapOccupancyPercent 提前启动标记周期
        * 也可以通过增加 -XX:ConcGCThreads 的值来增加并行标记线程数
    * 巨型对象分配失败：当巨型对象找不到合适的空间进行分配时，就会启动Full GC,来释放空间
        * 增加内存或者增大 -XX:G1HeapRegionSize

* G1GC 常见配置参数
    * -XX:G1NewSizePercent :初始年轻代占整个jvm 堆的大小，默认为5%
    * -XX:G1MaxNewSizePercent: 最大年轻代占整个堆的大小，默认值为60%
    * -XX:G1HeapRegionSize: 设置每个region的大小，单位MB，需要为1,2,4,8,16,32中的某个值，
    默认是堆内存的1/2000，如果设置的值比较大，那么大的对象就可以进入region
    * -XX:ConcGCThreads: 与Java应用一起执行的GC线程数，默认是Java线程数的1/4,减少这个参数的值可能
    提升并行回收的效率，提高系统内部吞吐量。如果这个数值过低，参与回收垃圾的线程不足，也会导致并行
    回收机制耗时加长
    * -XX:+InitiatingHeapOccupancyPercent(IHOP): G1 内部并行回收循环启动阀值，默认为堆的45%，
    类似老年代使用大于等于45%的时候，JVM会启动垃圾回收。这个值决定了在什么时间启动老年代的并行回收。
    * -XX:G1HeapWastePercent: G1停止回收的最小内存大小，默认是堆大小的5%。GC会收集所有的region中的对象，
    但是如果下降到了5%，就会停止下来不再收集。意思是不用每次都把所有的垃圾都回收完，可以遗留少量的下次处理，
    这样降低了单次GC消耗的时间
    * -XX:G1MixedGCCountTarget: 设置并行循环之后需要有多少个混合GC启动，默认值是8个。
    老年代的regions的回收时间通常比年轻代的收集时间要长一些，因此如果混合收集器比较多，可以允许G1延长老年代的收集时间。
    * -XX:+GCTimeRatio: 计算花在Java应用和GC线程上的时间比率，默认是9，和新生代内存的分配比例一致。
    计算公式是 100/(1+GCTimeRatio) ，如果是9，则最多10%的时间会花在GC工作上。 

    

------
    
------