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
import java.util.function.Supplier;

/**
 * 基于 Jackson 的 json 文件路由仓库
 */
@Getter
public class JacksonLocalRouterRepository extends LocalRouterRepository {

    private final ObjectMapper objectMapper;

    public JacksonLocalRouterRepository(Supplier<String> pathSupplier) {
        this(new ObjectMapper(), pathSupplier);
    }

    public JacksonLocalRouterRepository(ObjectMapper objectMapper, Supplier<String> pathSupplier) {
        super(pathSupplier);
        this.objectMapper = config(objectMapper);
    }

    protected ObjectMapper config(ObjectMapper objectMapper) {
        return objectMapper.registerModule(new SimpleModule()
                .addAbstractTypeMapping(Router.class, PathPatternRouter.class));
    }

    @SneakyThrows
    @Override
    public Collection<? extends Router> read(InputStream is) {
        //初始化没有数据时直接返回空列表
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
