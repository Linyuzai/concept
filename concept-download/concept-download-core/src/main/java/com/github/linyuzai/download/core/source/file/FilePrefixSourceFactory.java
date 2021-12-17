package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 文件前缀"file:"的工厂 / Factory for file prefix 'file:'
 */
public class FilePrefixSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{"file:"};

    /**
     * Use {@link FileSource}
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source {@link FileSource}
     */
    @Override
    public Source create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        Charset charset = context.getOptions().getCharset();
        return new FileSource.Builder()
                .file(new File(path))
                .charset(charset)
                .build();
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
