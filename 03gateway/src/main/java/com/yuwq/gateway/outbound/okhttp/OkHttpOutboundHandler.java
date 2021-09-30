package com.yuwq.gateway.outbound.okhttp;

import com.yuwq.gateway.common.Constants;
import com.yuwq.gateway.filter.HeaderHttpResponseFilter;
import com.yuwq.gateway.filter.HttpRequestFilter;
import com.yuwq.gateway.filter.HttpResponseFilter;
import com.yuwq.gateway.httputils.OkHttpUtil;
import com.yuwq.gateway.outbound.HttpNameThreadFactory;
import com.yuwq.gateway.router.HttpRouter;
import com.yuwq.gateway.router.RandomHttpRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;

import javax.xml.ws.Response;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkHttpOutboundHandler {

    //后台服务地址
    private List<String> apiUrls;

    //定义处理线程池
    private ExecutorService proxyService;

    //response过滤器
    HttpResponseFilter responseFilter = new HeaderHttpResponseFilter();

    //路由选择
    HttpRouter router = new RandomHttpRouter();


    public OkHttpOutboundHandler(List<String> apiUrls) {
        this.apiUrls = apiUrls.stream().map(this::formatUrl).collect(Collectors.toList());
        //CPU核心数
        int cores = Runtime.getRuntime().availableProcessors();
        //空闲时间
        long keepAliveTime = 1000;
        //阻塞队列大小
        int queueSize = 2048;

        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy(); //4种拒绝策略之用调用者所在的线程来执行任务，表现为当前页面被阻塞住了，直到当前调用者所在的线程执行完毕
        proxyService = new ThreadPoolExecutor(cores, cores, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new HttpNameThreadFactory(Constants.THREAD_PREFIX),rejectedExecutionHandler);

    }

    public void handle(final FullHttpRequest fullHttpRequest, final ChannelHandlerContext ctx, HttpRequestFilter requestFilter){
        String apiUrl = router.defaultRoute(this.apiUrls);
        final String url = apiUrl+fullHttpRequest.uri();
        requestFilter.filter(fullHttpRequest,ctx);
        proxyService.submit(()->fetchHttpResult(fullHttpRequest,ctx,url));
    }


    //过滤请求后端地址
    private String formatUrl(String backend) {
        return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
    }

    private void fetchHttpResult(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url){
        FullHttpResponse response = null;
        try {
            String  value = OkHttpUtil.get(url);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ctx.write(response);
            ctx.flush();
        }
    }

}
