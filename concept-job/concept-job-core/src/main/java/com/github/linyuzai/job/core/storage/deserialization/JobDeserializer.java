package com.github.linyuzai.job.core.storage.deserialization;

import com.github.linyuzai.job.core.concept.Job;

public interface JobDeserializer {

    Job deserialize(String serialized);
}
