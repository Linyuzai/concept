package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;

import java.io.File;

/**
 * 匹配前缀 'user.home:' 或 'user_home:' 或 'user-home:' 的 {@link SourceFactory}
 */
public class UserHomeSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{
            "user.home:",
            "user_home:",
            "user-home:"};

    public static final String USER_HOME = System.getProperty("user.home");

    private final SourceFactory factory = new FileSourceFactory();

    @Override
    public Source create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        return factory.create(new File(USER_HOME, path), context);
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
