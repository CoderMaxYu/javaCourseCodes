package com.yuwq.gateway.router;


import java.util.List;
import java.util.Random;

/**
 * 路由接口
 */
public interface HttpRouter {

    String route(List<String> endPoints);

    default String defaultRoute(List<String> urls) {
        int size = urls.size();
        Random random = new Random(System.currentTimeMillis());
        return urls.get(random.nextInt(size));
    }
}
