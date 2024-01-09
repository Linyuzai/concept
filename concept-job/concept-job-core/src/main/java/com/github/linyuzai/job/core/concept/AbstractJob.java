package com.github.linyuzai.job.core.concept;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class AbstractJob implements Job {

    private String id;

    private String name;

    private String description;

    private Map<String, String> params;
}
