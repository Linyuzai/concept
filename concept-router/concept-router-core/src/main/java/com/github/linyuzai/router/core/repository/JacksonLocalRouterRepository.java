package com.github.linyuzai.router.core.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.linyuzai.router.core.concept.PathPatternRouter;
import com.github.linyuzai.router.core.concept.Router;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

@Getter
public class JacksonLocalRouterRepository extends LocalRouterRepository {

    private final ObjectMapper objectMapper;

    public JacksonLocalRouterRepository() {
        this.objectMapper = config(new ObjectMapper());
    }

    public JacksonLocalRouterRepository(ObjectMapper objectMapper) {
        this.objectMapper = config(objectMapper);
    }

    public JacksonLocalRouterRepository(String path, ObjectMapper objectMapper) {
        super(path);
        this.objectMapper = config(objectMapper);
    }

    protected ObjectMapper config(ObjectMapper objectMapper) {
        return objectMapper.registerModule(new SimpleModule()
                .addAbstractTypeMapping(Router.class, PathPatternRouter.class));
    }

    @SneakyThrows
    @Override
    public Collection<? extends Router> read(InputStream is) {
        if (is.available() == 0) {
            return Collections.emptyList();
        }
        return objectMapper.readValue(is, new TypeReference<Collection<? extends Router>>() {
        });
    }

    @SneakyThrows
    @Override
    public void write(OutputStream os, Collection<? extends Router> routers) {
        os.write(objectMapper.writeValueAsString(routers).getBytes(StandardCharsets.UTF_8));
    }
}
