package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Getter
@RequiredArgsConstructor
public class ZipFilePluginEntry implements ZipPluginEntry {

    protected final Object id;

    protected final String name;

    protected final Plugin plugin;

    protected final String path;

    @Override
    public Plugin.Content getContent() {
        if (isDirectory()) {
            return null;
        }
        return new EntryContent();
    }

    protected boolean isDirectory() {
        return name.endsWith("/");
    }

    public class EntryContent implements Plugin.Content {

        @Override
        public InputStream getInputStream() throws IOException {
            ZipFile zf = new ZipFile(path);
            ZipEntry entry = zf.getEntry(name);
            if (entry == null) {
                throw new PluginException("Plugin entry not found");
            }
            InputStream is = zf.getInputStream(entry);
            return new EntryInputStream(is, zf);
        }
    }

    @RequiredArgsConstructor
    public static class EntryInputStream extends InputStream {

        private final InputStream is;

        private final Closeable closeable;

        @Override
        public int read(byte[] b) throws IOException {
            return is.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return is.read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {
            return is.skip(n);
        }

        @Override
        public int available() throws IOException {
            return is.available();
        }

        @Override
        public void close() throws IOException {
            try {
                is.close();
            } finally {
                closeable.close();
            }
        }

        @Override
        public synchronized void mark(int readlimit) {
            is.mark(readlimit);
        }

        @Override
        public synchronized void reset() throws IOException {
            is.reset();
        }

        @Override
        public boolean markSupported() {
            return is.markSupported();
        }

        @Override
        public int read() throws IOException {
            return is.read();
        }
    }
}
