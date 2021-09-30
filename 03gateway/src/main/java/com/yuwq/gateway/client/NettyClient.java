package com.yuwq.gateway.client;

import com.yuwq.gateway.inbound.HttpInboundInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.List;

public class NettyClient {


    //请求服务器端口
    private int port;

    //请求服务器IP
    private String ip;


    public NettyClient(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    public void run() throws Exception {
        //接待线程Acceptor
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        try {
            //设置react模型启动
            Bootstrap bs = new Bootstrap();
            bs.option(ChannelOption.SO_KEEPALIVE, true);
            bs.group(bossGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                                 @Override
                                 protected void initChannel(SocketChannel channel) throws Exception {
                                     channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG))
                                             .addLast(new ClientHandler());

                                 }
                             }
                    );
            //开启客户端
            ChannelFuture cf = bs.connect(ip,port).sync();

            //发送客户端请求
            cf.channel().writeAndFlush(Unpooled.copiedBuffer("this a  request".getBytes("utf-8")));

            cf.channel().closeFuture().sync(); //主线程 wait 阻塞后续逻辑执行
        } finally {
            //资源释放
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            new NettyClient(8088,"127.0.0.1").run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
