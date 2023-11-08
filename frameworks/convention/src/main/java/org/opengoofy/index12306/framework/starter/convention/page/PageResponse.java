package org.opengoofy.index12306.framework.starter.convention.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
@Data
public class PageResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 当前页码
     */
    private Long pageNum;
    /**
     * 每页显示数量
     */
    private Long pageSize;
    /**
     * 总数据条数
     */
    private Long total;
    private List<T> record;

    public PageResponse(Long pageNum, Long pageSize, Long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    public PageResponse(Long pageNum, Long pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = 0L;
    }

    public PageResponse<T> setRecord(List<T> record) {
        this.record = record;
        return this;
    }

    /**
     * 类型转换器
     * @param mapper
     * @return
     * @param <R>
     */
    public <R> PageResponse<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecord().stream().map(mapper).collect(Collectors.toList());
        return ((PageResponse<R>) this).setRecord(collect);
    }

}
