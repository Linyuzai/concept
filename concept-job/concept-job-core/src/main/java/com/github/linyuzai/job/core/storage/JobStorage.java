package com.github.linyuzai.job.core.storage;

import com.github.linyuzai.job.core.concept.Job;

import java.util.Collection;
import java.util.Collections;

public interface JobStorage {

    default void save(Job job) {
        save(Collections.singletonList(job));
    }

    void save(Collection<? extends Job> jobs);

    Job delete();
}
