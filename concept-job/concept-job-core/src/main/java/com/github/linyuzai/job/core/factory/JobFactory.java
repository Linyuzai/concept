package com.github.linyuzai.job.core.factory;

import java.util.Collection;

import com.github.linyuzai.job.core.concept.Job;

public interface JobFactory {

    boolean support(Object o);

    Collection<Job> create(Object o);
}
