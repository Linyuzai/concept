package com.github.linyuzai.cloud.web.core.result;

import lombok.*;

/**
 * 用于返回值的包装，提供额外的数据信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooleanWebResult implements WebResult<Boolean> {

    private static final long serialVersionUID = 1L;

    private Boolean result;

    private String message;

    private Object object;
}
