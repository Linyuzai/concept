package com.github.linyuzai.job.core.concept;

import com.github.linyuzai.job.core.storage.JobStorage;

public interface JobConcept {

    void initialize();

    void destroy();

    void add(Object jobOrBean);

    void update(Job job);

    void remove(String jobId);

    void start(String jobId);

    void stop(String jobId);

    Job get(String jobId);

    JobStorage getJobStorage();
}
