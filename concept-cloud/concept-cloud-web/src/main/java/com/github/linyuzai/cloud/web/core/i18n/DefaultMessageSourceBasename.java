package com.github.linyuzai.cloud.web.core.i18n;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.util.HashSet;
import java.util.Set;

/**
 * i18n basename 实现
 */
@RequiredArgsConstructor
public class DefaultMessageSourceBasename implements MessageSourceBasename {

    private final ApplicationContext applicationContext;

    /**
     * 获得所有的 basename
     * <p>
     * resources/i18n/{basename}_(en_US/zh_CN).properties
     * <p>
     * basename 中不能包含 '_'
     */
    @SneakyThrows
    @Override
    public String[] get() {
        Set<String> basenames = new HashSet<>();
        Resource[] resources = applicationContext.getResources("classpath*:i18n/**");
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename == null) {
                continue;
            }
            String basename;
            int indexOf = filename.indexOf("_");
            if (indexOf == -1) {
                basename = filename;
            } else {
                basename = filename.substring(0, indexOf);
            }
            basenames.add("i18n/" + basename);
        }
        return basenames.toArray(new String[0]);
    }
}
