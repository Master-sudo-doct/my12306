package org.opengoofy.index12306.framework.starter.common.threadpool.proxy;

import org.apache.commons.lang3.ThreadUtils;
import org.opengoofy.index12306.framework.starter.common.toolkit.ThreadUtil;

import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 拒绝策略代理工具类
 */
public class RejectProxyUtil {

    public static RejectedExecutionHandler createProxy(RejectedExecutionHandler rejectedExecutionHandler,
                                                       AtomicLong rejectedNum) {
        // 动态代理模式: 增强线程池拒绝策略，比如：拒绝任务报警或加入延迟队列重复放入等逻辑
        return (RejectedExecutionHandler) Proxy.newProxyInstance(rejectedExecutionHandler.getClass().getClassLoader(),
                new Class[]{RejectedExecutionHandler.class},
                new RejectedProxyInvocationHandler(rejectedExecutionHandler, rejectedNum));
    }

    /**
     * 测试动态代理
     *
     * @param args
     */
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 5, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10));
        ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        AtomicLong rejectNum = new AtomicLong();
        RejectedExecutionHandler rejectedExecutionHandler = RejectProxyUtil.createProxy(callerRunsPolicy, rejectNum);
        threadPoolExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);
        for (int i = 0; i < 20; i++) {
            threadPoolExecutor.submit(
                    () -> {
                        ThreadUtil.sleep(10000L);
                    });
        }
    }
}
