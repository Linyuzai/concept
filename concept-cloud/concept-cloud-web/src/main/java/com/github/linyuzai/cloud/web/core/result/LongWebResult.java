package com.github.linyuzai.cloud.web.core.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * result 为 {@link Long} 的结果包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LongWebResult implements WebResult<Long> {

    private static final long serialVersionUID = 1L;

    private Long result;

    private String message;

    private Object object;
}
