package com.github.linyuzai.job.core.concept;

import com.github.linyuzai.job.core.storage.JobStorage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractJobConcept implements JobConcept {

    private JobStorage jobStorage;

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void add(Object jobOrBean) {
        if (jobOrBean instanceof Job) {
            jobStorage.save((Job) jobOrBean);
        } else {

        }
    }

    @Override
    public void update(Job job) {

    }

    @Override
    public void remove(String jobId) {

    }

    @Override
    public void start(String jobId) {

    }

    @Override
    public void stop(String jobId) {

    }

    @Override
    public Job get(String jobId) {
        return null;
    }
}
