package com.github.linyuzai.router.autoconfigure.management;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultVO {

    private boolean success;

    private String message;

    private Object object;
}
