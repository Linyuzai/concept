package com.github.linyuzai.router.autoconfigure.banner;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

public class ConceptRouterBanner {

    private static final String NAME = "Concept Router";

    private static final String VERSION = "v0.5.0";

    @SneakyThrows
    public static void print() {
        ClassPathResource resource = new ClassPathResource("concept/router/banner.txt");
        try (InputStream is = resource.getInputStream()) {
            byte[] bytes = new byte[is.available()];
            int read = is.read(bytes);
            String banner = new String(bytes);
            System.out.println(build(banner, NAME, VERSION));
        }
    }

    public static String build(String banner, String name, String version) {
        StringBuilder builder = new StringBuilder("\n");
        builder.append(banner);
        String tag = " :: " + name + " :: ";
        builder.append("\n").append(tag);
        int count = 70 - tag.length() - version.length();
        for (int i = 0; i < count; i++) {
            builder.append(" ");
        }
        return builder.append(version).append("\n").toString();
    }
}
