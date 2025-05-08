package com.github.linyuzai.plugin.zip.concept;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface ZipStreamProvider {

    URL getURL();

    InputStream getInputStream() throws IOException;
}
