package com.github.linyuzai.concept.sample.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserImpl implements User {

    private String id;

    @Override
    public String getName() {
        return "name:" + id;
    }
}
