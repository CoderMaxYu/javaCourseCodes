package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Map;

import static io.kimmking.rpcfx.client.RpcAdvice.JSON_TYPE;

public class RpcNettyHandler extends ChannelInboundHandlerAdapter {

    private final String req;
    private final Map<ChannelId, RpcfxResponse> respMap;

    public RpcNettyHandler(final String req, final Map<ChannelId, RpcfxResponse> respMap) {
        this.req = req;
        this.respMap = respMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        System.out.println("----------------------获取响应数据-----------------------");
        FullHttpResponse response = (FullHttpResponse) message;
        ByteBuf buf = response.content();
        String result = buf.toString(CharsetUtil.UTF_8);
        respMap.putIfAbsent(ctx.channel().id(), JSON.parseObject(result, RpcfxResponse.class));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(req.getBytes());
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/", byteBuf);
        request.headers().add(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().add(HttpHeaderNames.HOST, "127.0.0.1");
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, JSON_TYPE);
        ctx.writeAndFlush(request);
    }
}
