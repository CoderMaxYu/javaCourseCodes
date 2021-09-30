package com.yuwq.gateway.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            ByteBuf response = (ByteBuf)msg;
            byte[] result = new byte[response.readableBytes()];
            response.readBytes(result);
            String realMsg = new String(result,"utf-8");
            System.out.println("client读取响应数据"+realMsg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("读取响应结束");
        ctx.close();
    }
}
