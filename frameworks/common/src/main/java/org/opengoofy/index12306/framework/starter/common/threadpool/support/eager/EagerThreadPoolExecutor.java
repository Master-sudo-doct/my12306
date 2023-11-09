package org.opengoofy.index12306.framework.starter.common.threadpool.support.eager;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EagerThreadPoolExecutor extends ThreadPoolExecutor {
    public EagerThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   TaskQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                   RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }
    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);
    public int getSubmittedTaskCount(){
        return submittedTaskCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }

    @Override
    public void execute(Runnable command) {
        submittedTaskCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException e) {
            TaskQueue taskQueue = (TaskQueue) super.getQueue();
            //任务提交失败，说明任务数量已超过核心线程数，因此使用taskQueue中的retryOffer方法，
            //已timeout为超时时间进行重试，若队列没满则加入队列，否则使用抛出异常的拒绝策略
            try {
                if (!taskQueue.retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("任务队列已满", e);
                }
            } catch (InterruptedException iex) {
                submittedTaskCount.decrementAndGet();
                throw new RejectedExecutionException(iex);
            }
        }catch (Exception ex){
            submittedTaskCount.decrementAndGet();
            throw new RuntimeException(ex);
        }
    }

}
