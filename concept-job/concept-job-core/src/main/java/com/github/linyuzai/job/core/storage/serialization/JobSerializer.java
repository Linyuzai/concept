package com.github.linyuzai.job.core.storage.serialization;

import com.github.linyuzai.job.core.concept.Job;

public interface JobSerializer {

    String serialize(Job job);
}
