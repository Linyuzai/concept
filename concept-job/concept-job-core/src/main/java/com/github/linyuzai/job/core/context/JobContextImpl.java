package com.github.linyuzai.job.core.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class JobContextImpl implements JobContext {

    private final Map<String, String> params;
}
