package org.opengoofy.index12306.framework.starter.convention.page;

import lombok.Data;

@Data
public class PageRequest {
    /**
     * 页号
     */
    private Long pageNum;
    /**
     * 一页多少条信息
     */
    private Long pageSize;
}
