package com.github.linyuzai.job.core.factory;

import com.github.linyuzai.job.core.concept.Job;
import com.github.linyuzai.job.core.concept.MethodJob;
import com.github.linyuzai.job.core.context.JobContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MethodJobFactory implements JobFactory {

    @Override
    public boolean support(Object o) {
        return o instanceof JobContext;
    }

    @Override
    public Collection<Job> create(Object o) {
        JobContext context = (JobContext) o;
        List<Job> jobs = new ArrayList<>();
        jobs.add(new MethodJob(null, null));
        return jobs;
    }
}
