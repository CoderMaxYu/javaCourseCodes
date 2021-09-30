package com.yuwq.gateway.inbound;

import com.yuwq.gateway.filter.HeaderHttpRequestFilter;
import com.yuwq.gateway.filter.HttpRequestFilter;
import com.yuwq.gateway.outbound.okhttp.OkHttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * 定义Inbound hanlder
 * MaxYu
 */

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {


    private final List<String> proxyServers;

    //okhttp outboundhandler
    private OkHttpOutboundHandler outboundHandler;
    //请求头过滤器
    private HttpRequestFilter filter = new HeaderHttpRequestFilter();

    public HttpInboundHandler(List<String> proxyServers) {
        this.proxyServers = proxyServers;
        this.outboundHandler = new OkHttpOutboundHandler(this.proxyServers);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        try {
            FullHttpRequest fullHttpRequest = (FullHttpRequest)o;
            outboundHandler.handle(fullHttpRequest,ctx,filter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //从InBound里读取的ByteBuf要手动释放，还有自己创建的ByteBuf要自己负责释放
            ReferenceCountUtil.release(o);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.flush();  //事件传递
    }
}
