package com.yuwq.gateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * 开启服务
 */

public class HttpInboundServer {

    //监听端口
    private int port;

    //内部代理请求服务器地址
    private List<String> proxyServers;


    public HttpInboundServer(int port, List<String> proxyServers) {
        this.port = port;
        this.proxyServers = proxyServers;
    }

    public void run() throws Exception {
        //接待线程Acceptor
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //worker线程池sub
        EventLoopGroup workerGroup = new NioEventLoopGroup(16);


        try {
            //设置react模型启动
            ServerBootstrap bs = new ServerBootstrap();
            bs.option(ChannelOption.SO_BACKLOG, 128) //对应tcp/ip协议listen函数中的backlog参数，函数listen(int socketfd,int backlog)用来初始化服务端可连接队列
                    .childOption(ChannelOption.TCP_NODELAY, true)  //禁用nagle算法
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //心跳，在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
                    .childOption(ChannelOption.SO_REUSEADDR, true) //等待WAIT_CLOSE状态允许新的SOCKET接入
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)  //设置接收缓冲区大小32KB
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)  //设置发送缓冲区大小32KB
                    .childOption(EpollChannelOption.SO_REUSEPORT, true) //支持多个进程或者线程绑定到同一端口
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); //ByteBuf的分配器(重用缓冲区)

            bs.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new HttpInboundInitializer(this.proxyServers)); //绑定线程后启动

            Channel ch = bs.bind(port).sync().channel();
            ch.closeFuture().sync(); //主线程 wait 阻塞后续逻辑执行
        } finally {
            //资源释放
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

}
