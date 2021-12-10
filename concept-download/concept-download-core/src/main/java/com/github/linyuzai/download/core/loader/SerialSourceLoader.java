package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

import java.io.IOException;

public class SerialSourceLoader implements SourceLoader {

    @Override
    public void load(Source source, DownloadContext context) throws IOException {
        source.load(context);
    }
}
