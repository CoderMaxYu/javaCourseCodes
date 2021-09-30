package com.yuwq.gateway.inbound;

import com.yuwq.gateway.filter.ProxyBizFilter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;


/**
 * 初始化channel
 */
public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
    private List<String> proxyServers;

    public HttpInboundInitializer(List<String> proxyServers) {
        this.proxyServers = proxyServers;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //获得管道
        ChannelPipeline cp = channel.pipeline();
        //http 编码解码器
        cp.addLast(new HttpServerCodec());
        //post 请求message body 解析
        cp.addLast(new HttpObjectAggregator(1024 * 1024));
        //自定义业务过滤器
        cp.addLast(new ProxyBizFilter());
        //业务处理handler
        cp.addLast(new HttpInboundHandler(this.proxyServers));
    }
}
