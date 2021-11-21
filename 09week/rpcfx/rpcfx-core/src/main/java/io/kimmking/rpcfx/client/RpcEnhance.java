package io.kimmking.rpcfx.client;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RpcEnhance {

    protected static final Map<Class<?>, String> URL_MAP = new ConcurrentHashMap<>();

    protected static final Map<Class<?>, Object> INSTANCW_MAP = new ConcurrentHashMap<>();

    public static <T> T create(final Class<T> serviceClass, final String url) {
        URL_MAP.putIfAbsent(serviceClass, url);
        T proxyObj = (T) INSTANCW_MAP.get(serviceClass);
        if (proxyObj == null) {
            try {
                proxyObj = new ByteBuddy().subclass(serviceClass)
                        .method(ElementMatchers.anyOf(serviceClass.getMethods()))
                        .intercept(MethodDelegation.to(new RpcAdvice()))
                        .make()
                        .load(serviceClass.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                        .getLoaded()
                        .newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            INSTANCW_MAP.putIfAbsent(serviceClass, proxyObj);
        }

        return proxyObj;
    }

}
