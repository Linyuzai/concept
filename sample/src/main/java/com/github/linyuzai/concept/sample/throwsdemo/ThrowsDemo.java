package com.github.linyuzai.concept.sample.throwsdemo;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class ThrowsDemo {

    public void demo4throws() throws RuntimeException {
        throw new RuntimeException();
    }
}
