package com.github.linyuzai.job.core.concept;

public interface JobConcept {

    void initialize();

    void destroy();

    void add(Object bean);

    void update(Job job);

    void remove(String jobId);

    void start(String jobId);

    void stop(String jobId);

    Job get(String jobId);
}
