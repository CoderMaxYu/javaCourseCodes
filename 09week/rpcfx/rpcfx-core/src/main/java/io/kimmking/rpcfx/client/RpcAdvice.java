package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import okhttp3.MediaType;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static io.kimmking.rpcfx.client.RpcEnhance.URL_MAP;

public final class RpcAdvice {

    private static final Map<ChannelId, RpcfxResponse> CHANNEL_RESP_MAP = new ConcurrentHashMap<>();

    private static final NioEventLoopGroup WORK_GROUP = new NioEventLoopGroup(4);
    private static final Bootstrap BOSS_GROUP = new Bootstrap();

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
        BOSS_GROUP.group(WORK_GROUP).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true);
    }

    public static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

    @RuntimeType
    public Object proxyInvoke(@Origin Method method, @AllArguments Object[] params) {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(method.getDeclaringClass().getName());
        request.setMethod(method.getName());
        request.setParams(params);

        RpcfxResponse response = post(request, URL_MAP.get(method.getDeclaringClass()));;

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException

        return JSON.parse(Objects.requireNonNull(response).getResult().toString());
    }

    private RpcfxResponse post(RpcfxRequest req, String url) {
        String reqJson = JSON.toJSONString(req);

        final String handleUrl = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        String[] splitUrl = handleUrl.split("/");
        String[] hostAndPort = splitUrl[splitUrl.length - 1].split(":");

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client

        Channel channel = BOSS_GROUP.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new HttpClientCodec());
                socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
                socketChannel.pipeline().addLast(new HttpContentDecompressor());
                socketChannel.pipeline().addLast(new RpcNettyHandler(reqJson, CHANNEL_RESP_MAP));
            }
        }).remoteAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1]))
                .connect()
                .syncUninterruptibly()
                .addListener(future1 -> {
                    if (future1.isSuccess()) {
                        System.out.println("---------------------------连接成功--------------------------");
                        System.out.println("req json: " + reqJson);
                    }
                }).channel();
        ChannelId channelId = channel.id();
        channel.closeFuture().syncUninterruptibly();
        RpcfxResponse response = CHANNEL_RESP_MAP.get(channelId);
        CHANNEL_RESP_MAP.remove(channelId);
        return response;
    }
}
