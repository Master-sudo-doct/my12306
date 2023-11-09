package org.opengoofy.index12306.framework.starter.common.threadpool.support.eager;

import lombok.Setter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 快速消费任务队列 即 发现池内线程大于核心线程数, 不放入阻塞队列, 而是创建非核心线程进行消费任务
 */
public class TaskQueue<R extends Runnable> extends LinkedBlockingQueue<Runnable> {
    @Setter
    private EagerThreadPoolExecutor executor;

    public TaskQueue(int capacity) {
        super(capacity);
    }

    public boolean offer(Runnable runnable) {
        //获取线程池中正在运行的线程数量
        int currentPoolThreadSize = executor.getPoolSize();
        //若核心线程空闲，则将任务加入阻塞队列，被核心线程调度执行
        if (executor.getSubmittedTaskCount() < currentPoolThreadSize) {
            return super.offer(runnable);
        }
        //核心线程已满，当前线程池线程数量小于最大线程数，返回False，入队失败，直接创建非核心线程完成该任务，达到快速消费的目的
        if (currentPoolThreadSize < executor.getMaximumPoolSize()) {
            return false;
        }
        //大于最大线程数，任务加入阻塞队列
        return super.offer(runnable);
    }

    public boolean retryOffer(Runnable runnable, long timeout, TimeUnit unit) throws InterruptedException {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        return super.offer(runnable, timeout, unit);
    }

}
