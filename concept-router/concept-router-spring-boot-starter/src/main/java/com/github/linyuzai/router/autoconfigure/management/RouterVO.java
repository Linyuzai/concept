package com.github.linyuzai.router.autoconfigure.management;

import lombok.Data;

@Data
public class RouterVO {

    private String id;

    private String serviceId;

    private String pathPattern;

    private String serverAddress;

    private Boolean forced;

    private Boolean enabled;
}
