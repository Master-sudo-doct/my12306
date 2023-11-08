package org.opengoofy.index12306.framework.starter.designpattern.builder;

import java.io.Serializable;

/**
 * 建造者模式抽象接口
 * @param <T>
 */
public interface Builder<T> extends Serializable {
    /**
     * 构建方法
     */
    T build();
}
