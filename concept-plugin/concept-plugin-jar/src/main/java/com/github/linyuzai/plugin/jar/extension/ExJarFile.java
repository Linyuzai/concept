package com.github.linyuzai.plugin.jar.extension;

import com.github.linyuzai.plugin.jar.extension.file.*;
import lombok.Getter;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.security.Permission;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;

/**
 * Extended variant of {@link java.util.jar.JarFile} that behaves in the same way but
 * offers the following additional functionality.
 * <ul>
 * <li>A nested {@link ExJarFile} can be {@link #getNestedJarFile(ZipEntry) obtained} based
 * on any directory entry.</li>
 * <li>A nested {@link ExJarFile} can be {@link #getNestedJarFile(ZipEntry) obtained} for
 * embedded JAR files (as long as their entry is not compressed).</li>
 * </ul>
 */
public class ExJarFile extends JarFile implements Iterable<JarEntry> {

    private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";

    //private static final String PROTOCOL_HANDLER = "java.protocol.handler.pkgs";

    //private static final String HANDLERS_PACKAGE = "org.springframework.boot.loader";

    private static final AsciiBytes META_INF = new AsciiBytes("META-INF/");

    private static final AsciiBytes SIGNATURE_FILE_EXTENSION = new AsciiBytes(".SF");

    private static final String READ_ACTION = "read";

    private final RandomAccessDataFile rootFile;

    private final String pathFromRoot;

    private final RandomAccessData data;

    @Getter
    private final Type type;

    private URL url;

    //private String urlString;

    private final ExJarFileEntries entries;

    private final Supplier<Manifest> manifestSupplier;

    private SoftReference<Manifest> manifest;

    @Getter
    private boolean signed;

    private String comment;

    @Getter
    private volatile boolean closed;

    //private volatile JarFileWrapper wrapper;

    /**
     * Create a new {@link ExJarFile} backed by the specified file.
     *
     * @param file the root jar file
     * @throws IOException if the file cannot be read
     */
    public ExJarFile(File file) throws IOException {
        this(new RandomAccessDataFile(file));
    }

    /**
     * Create a new {@link ExJarFile} backed by the specified file.
     *
     * @param file the root jar file
     * @throws IOException if the file cannot be read
     */
    private ExJarFile(RandomAccessDataFile file) throws IOException {
        this(file, "", file, Type.DIRECT);
    }

    /**
     * Private constructor used to create a new {@link ExJarFile} either directly or from a
     * nested entry.
     *
     * @param rootFile     the root jar file
     * @param pathFromRoot the name of this file
     * @param data         the underlying data
     * @param type         the type of the jar file
     * @throws IOException if the file cannot be read
     */
    private ExJarFile(RandomAccessDataFile rootFile, String pathFromRoot, RandomAccessData data, Type type)
            throws IOException {
        this(rootFile, pathFromRoot, data, null, type, null);
    }

    private ExJarFile(RandomAccessDataFile rootFile, String pathFromRoot, RandomAccessData data, ExJarEntry.Filter filter,
                      Type type, Supplier<Manifest> manifestSupplier) throws IOException {
        super(rootFile.getFile());
        this.rootFile = rootFile;
        this.pathFromRoot = pathFromRoot;
        CentralDirectoryParser parser = new CentralDirectoryParser();
        this.entries = parser.addVisitor(new ExJarFileEntries(this, filter));
        this.type = type;
        parser.addVisitor(centralDirectoryVisitor());
        try {
            this.data = parser.parse(data, filter == null);
        } catch (RuntimeException ex) {
            try {
                this.rootFile.close();
                super.close();
            } catch (IOException ioex) {
            }
            throw ex;
        }
        this.manifestSupplier = (manifestSupplier != null) ? manifestSupplier : () -> {
            try (InputStream inputStream = getInputStream(MANIFEST_NAME)) {
                if (inputStream == null) {
                    return null;
                }
                return new Manifest(inputStream);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    private CentralDirectoryVisitor centralDirectoryVisitor() {
        return new CentralDirectoryVisitor() {

            @Override
            public void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData) {
                ExJarFile.this.comment = endRecord.getComment();
            }

            @Override
            public void visitFileHeader(CentralDirectoryFileHeader fileHeader, long dataOffset) {
                AsciiBytes name = fileHeader.getName();
                if (name.startsWith(META_INF) && name.endsWith(SIGNATURE_FILE_EXTENSION)) {
                    ExJarFile.this.signed = true;
                }
            }

            @Override
            public void visitEnd() {
            }

        };
    }

	/*JarFileWrapper getWrapper() throws IOException {
		JarFileWrapper wrapper = this.wrapper;
		if (wrapper == null) {
			wrapper = new JarFileWrapper(this);
			this.wrapper = wrapper;
		}
		return wrapper;
	}*/

    Permission getPermission() {
        return new FilePermission(this.rootFile.getFile().getPath(), READ_ACTION);
    }

    protected final RandomAccessDataFile getRootJarFile() {
        return this.rootFile;
    }

    RandomAccessData getData() {
        return this.data;
    }

    @Override
    public Manifest getManifest() throws IOException {
        Manifest manifest = (this.manifest != null) ? this.manifest.get() : null;
        if (manifest == null) {
            try {
                manifest = this.manifestSupplier.get();
            } catch (RuntimeException ex) {
                throw new IOException(ex);
            }
            this.manifest = new SoftReference<>(manifest);
        }
        return manifest;
    }

    @Override
    public Enumeration<JarEntry> entries() {
        return new JarEntryEnumeration(this.entries.iterator());
    }

    @Override
    public Stream<JarEntry> stream() {
        Spliterator<JarEntry> spliterator = Spliterators.spliterator(iterator(), size(),
                Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL);
        return StreamSupport.stream(spliterator, false);
    }

    /**
     * Return an iterator for the contained entries.
     *
     * @see Iterable#iterator()
     * @since 2.3.0
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Iterator<JarEntry> iterator() {
        return (Iterator) this.entries.iterator(this::ensureOpen);
    }

    public ExJarEntry getJarEntry(CharSequence name) {
        return this.entries.getEntry(name);
    }

    @Override
    public ExJarEntry getJarEntry(String name) {
        return (ExJarEntry) getEntry(name);
    }

    public boolean containsEntry(String name) {
        return this.entries.containsEntry(name);
    }

    @Override
    public ZipEntry getEntry(String name) {
        ensureOpen();
        return this.entries.getEntry(name);
    }

    public InputStream getInputStream() throws IOException {
        return this.data.getInputStream();
    }

    @Override
    public synchronized InputStream getInputStream(ZipEntry entry) throws IOException {
        ensureOpen();
        if (entry instanceof ExJarEntry) {
            return this.entries.getInputStream((ExJarEntry) entry);
        }
        return getInputStream((entry != null) ? entry.getName() : null);
    }

    public InputStream getInputStream(String name) throws IOException {
        return this.entries.getInputStream(name);
    }

    /**
     * Return a nested {@link ExJarFile} loaded from the specified entry.
     *
     * @param entry the zip entry
     * @return a {@link ExJarFile} for the entry
     * @throws IOException if the nested jar file cannot be read
     */
    public synchronized ExJarFile getNestedJarFile(ZipEntry entry) throws IOException {
        return getNestedJarFile((ExJarEntry) entry);
    }

    /**
     * Return a nested {@link ExJarFile} loaded from the specified entry.
     *
     * @param entry the zip entry
     * @return a {@link ExJarFile} for the entry
     * @throws IOException if the nested jar file cannot be read
     */
    public synchronized ExJarFile getNestedJarFile(ExJarEntry entry) throws IOException {
        try {
            return createJarFileFromEntry(entry);
        } catch (Exception ex) {
            throw new IOException("Unable to open nested jar file '" + entry.getName() + "'", ex);
        }
    }

    private ExJarFile createJarFileFromEntry(ExJarEntry entry) throws IOException {
        if (entry.isDirectory()) {
            return createJarFileFromDirectoryEntry(entry);
        }
        return createJarFileFromFileEntry(entry);
    }

    private ExJarFile createJarFileFromDirectoryEntry(ExJarEntry entry) throws IOException {
        AsciiBytes name = entry.getAsciiBytesName();
        ExJarEntry.Filter filter = (candidate) -> {
            if (candidate.startsWith(name) && !candidate.equals(name)) {
                return candidate.substring(name.length());
            }
            return null;
        };
        return new ExJarFile(this.rootFile, this.pathFromRoot + "!/" + entry.getName().substring(0, name.length() - 1),
                this.data, filter, Type.NESTED_DIRECTORY, this.manifestSupplier);
    }

    private ExJarFile createJarFileFromFileEntry(ExJarEntry entry) throws IOException {
        if (entry.getMethod() != ZipEntry.STORED) {
            throw new IllegalStateException(
                    "Unable to open nested entry '" + entry.getName() + "'. It has been compressed and nested "
                            + "jar files must be stored without compression. Please check the "
                            + "mechanism used to create your executable jar file");
        }
        RandomAccessData entryData = this.entries.getEntryData(entry.getName());
        return new ExJarFile(this.rootFile, this.pathFromRoot + "!/" + entry.getName(), entryData,
                Type.NESTED_JAR);
    }

    @Override
    public String getComment() {
        ensureOpen();
        return this.comment;
    }

    @Override
    public int size() {
        ensureOpen();
        return this.entries.getSize();
    }

    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        super.close();
        if (this.type == Type.DIRECT) {
            this.rootFile.close();
        }
        this.closed = true;
    }

    private void ensureOpen() {
        if (this.closed) {
            throw new IllegalStateException("zip file closed");
        }
    }

	/*String getUrlString() throws MalformedURLException {
		if (this.urlString == null) {
			this.urlString = getURL().toString();
		}
		return this.urlString;
	}*/

    public URL getURL() throws MalformedURLException {
        if (this.url == null) {
            String file = this.rootFile.getFile().toURI() + this.pathFromRoot + "!/";
            file = file.replace("file:////", "file://"); // Fix UNC paths
            this.url = new URL("jar", "", -1, file, new ExJarHandler(this));
        }
        return this.url;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return this.rootFile.getFile() + this.pathFromRoot;
    }

    ExJarEntry.Certification getCertification(ExJarEntry entry) {
        try {
            return this.entries.getCertification(entry);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

	/*public void clearCache() {
		this.entries.clearCache();
	}*/

    protected String getPathFromRoot() {
        return this.pathFromRoot;
    }

    /**
     * Register a {@literal 'java.protocol.handler.pkgs'} property so that a
     * {@link URLStreamHandler} will be located to deal with jar URLs.
     */
	/*public static void registerUrlProtocolHandler() {
		Handler.captureJarContextUrl();
		String handlers = System.getProperty(PROTOCOL_HANDLER, "");
		System.setProperty(PROTOCOL_HANDLER,
				((handlers == null || handlers.isEmpty()) ? HANDLERS_PACKAGE : handlers + "|" + HANDLERS_PACKAGE));
		resetCachedUrlHandlers();
	}*/

    /**
     * Reset any cached handlers just in case a jar protocol has already been used. We
     * reset the handler by trying to set a null {@link URLStreamHandlerFactory} which
     * should have no effect other than clearing the handlers cache.
     */
	/*private static void resetCachedUrlHandlers() {
		try {
			URL.setURLStreamHandlerFactory(null);
		}
		catch (Error ex) {
			// Ignore
		}
	}*/

    /**
     * The type of a {@link ExJarFile}.
     */
    enum Type {

        DIRECT, NESTED_DIRECTORY, NESTED_JAR

    }

    /**
     * An {@link Enumeration} on {@linkplain JarEntry jar entries}.
     */
    private static class JarEntryEnumeration implements Enumeration<JarEntry> {

        private final Iterator<ExJarEntry> iterator;

        JarEntryEnumeration(Iterator<ExJarEntry> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasMoreElements() {
            return this.iterator.hasNext();
        }

        @Override
        public JarEntry nextElement() {
            return this.iterator.next();
        }

    }

}
