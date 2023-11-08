package org.opengoofy.index12306.framework.starter.designpattern.chain;

import org.springframework.core.Ordered;

/**
 * 抽象业务责任链模式
 * @param <T>
 */
public interface AbstractChainHandler<T> extends Ordered {
    /**
     * 执行责任链逻辑
     */
    void handler(T requestParam);
    /**
     * 责任链组件标识
     */
    String mark();
}
