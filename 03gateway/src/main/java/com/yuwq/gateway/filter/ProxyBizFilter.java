package com.yuwq.gateway.filter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.ReferenceCountUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ProxyBizFilter  extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        try {
            FullHttpRequest fullHttpRequest = (FullHttpRequest)o;
            String uri =fullHttpRequest.uri();
            if(uri.contains("test")){ //含有test的请求被拒绝
                FullHttpResponse response = null;
                response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer("无效的请求路径".getBytes("UTF-8")));
                response.headers().set("Content-Type", "application/json");
                response.headers().setInt("Content-Length", response.content().readableBytes());
                ctx.write(response);
                ctx.flush();
            }else{
                //通知下一个handler
                ctx.fireChannelRead(o) ;
            }
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
