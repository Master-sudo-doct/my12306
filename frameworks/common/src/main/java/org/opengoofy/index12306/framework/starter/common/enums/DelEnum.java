package org.opengoofy.index12306.framework.starter.common.enums;

/**
 * 删除标记枚举
 */
public enum DelEnum {
    /**
     * 正常状态
     */
    NORMAL(0);

    private final Integer statusCode;

    DelEnum(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStrStatusCode(){
        return String.valueOf(statusCode);
    }

    @Override
    public String toString() {
        return "DelEnum{" +
                "statusCode=" + statusCode +
                '}';
    }
}
