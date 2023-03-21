package com.github.linyuzai.cloud.web.core.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * result 为 {@link String} 的结果包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringWebResult implements WebResult<String> {

    private static final long serialVersionUID = 1L;

    private String result;

    private String message;

    private Object object;
}
