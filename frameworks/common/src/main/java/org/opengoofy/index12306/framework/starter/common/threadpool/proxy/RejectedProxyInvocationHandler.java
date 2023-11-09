package org.opengoofy.index12306.framework.starter.common.threadpool.proxy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@AllArgsConstructor
public class RejectedProxyInvocationHandler implements InvocationHandler {
    /**
     * 目标方法
     */
    private final Object target;
    /**
     * 拒绝次数
     */
    private final AtomicLong rejectCount;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        rejectCount.incrementAndGet();
        try {
            log.error("线程池执行拒绝策略，此处模拟报警！");
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
