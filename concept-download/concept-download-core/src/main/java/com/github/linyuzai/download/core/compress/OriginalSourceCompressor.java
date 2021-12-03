package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.writer.SourceWriter;

import java.io.IOException;

public interface OriginalSourceCompressor {

    boolean support(String format, DownloadContext context);

    CompressedSource compress(OriginalSource source, SourceWriter writer, String cachePath, boolean delete, DownloadContext context) throws IOException;
}
