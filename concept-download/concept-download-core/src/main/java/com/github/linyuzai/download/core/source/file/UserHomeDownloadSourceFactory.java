package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSource;
import com.github.linyuzai.download.core.source.prefix.PrefixDownloadSourceFactory;

import java.io.File;
import java.nio.charset.Charset;

public class UserHomeDownloadSourceFactory extends PrefixDownloadSourceFactory {

    public static final String[] PREFIXES = new String[]{
            "user.home:",
            "user_home:",
            "user-home:",
            "userHome:",
            "userhome:"};

    public static final String USER_HOME = System.getProperty("user.home");

    @Override
    public DownloadSource create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        Charset charset = context.getOptions().getCharset();
        return new FileDownloadSource.Builder()
                .file(new File(USER_HOME, path))
                .charset(charset)
                .build();
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
