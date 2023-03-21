package com.github.linyuzai.cloud.web.core.result;

import lombok.*;

/**
 * result 为 {@link Boolean} 的结果包装类
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
