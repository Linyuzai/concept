package com.github.linyuzai.job.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractJob implements Job {

    private String id;

    private String name;

    private String description;

    private String params;
}
