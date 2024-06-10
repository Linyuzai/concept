package com.github.linyuzai.plugin.jar.extension;

import com.github.linyuzai.plugin.jar.extension.file.AsciiBytes;
import com.github.linyuzai.plugin.jar.extension.file.StringSequence;

import java.io.*;
import java.net.*;
import java.security.Permission;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * {@link java.net.JarURLConnection} used to support {@link ExJarFile#getURL()}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @author Rostyslav Dudka
 */
public class ExJarConnection extends JarURLConnection {

    //private static ThreadLocal<Boolean> useFastExceptions = new ThreadLocal<>();

    private static final FileNotFoundException FILE_NOT_FOUND_EXCEPTION = new FileNotFoundException(
            "Jar file or entry not found");

    private static final IllegalStateException NOT_FOUND_CONNECTION_EXCEPTION = new IllegalStateException(
            FILE_NOT_FOUND_EXCEPTION);

    private static final String SEPARATOR = "!/";

    private static final URL EMPTY_JAR_URL;

    static {
        try {
            EMPTY_JAR_URL = new URL("jar:", null, 0, "file:!/", new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) throws IOException {
                    // Stub URLStreamHandler to prevent the wrong JAR Handler from being
                    // Instantiated and cached.
                    return null;
                }
            });
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static final JarEntryName EMPTY_JAR_ENTRY_NAME = new JarEntryName(new StringSequence(""));

    //private static final JarURLConnection NOT_FOUND_CONNECTION = JarURLConnection.notFound();

    private final ExJarFile jarFile;

    private Permission permission;

    private URL jarFileUrl;

    private final JarEntryName jarEntryName;

    private JarEntry jarEntry;

    public ExJarConnection(URL url, ExJarFile jarFile, JarEntryName jarEntryName) throws IOException {
        // What we pass to super is ultimately ignored
        super(EMPTY_JAR_URL);
        this.url = url;
        this.jarFile = jarFile;
        this.jarEntryName = jarEntryName;
    }

    @Override
    public void connect() throws IOException {
        /*if (this.jarFile == null) {
            throw FILE_NOT_FOUND_EXCEPTION;
        }*/
        if (!this.jarEntryName.isEmpty() && this.jarEntry == null) {
            this.jarEntry = this.jarFile.getJarEntry(getEntryName());
            if (this.jarEntry == null) {
                throwFileNotFound(this.jarEntryName, this.jarFile);
            }
        }
        this.connected = true;
    }

    @Override
    public JarFile getJarFile() throws IOException {
        connect();
        return this.jarFile;
    }

    @Override
    public URL getJarFileURL() {
        /*if (this.jarFile == null) {
            throw NOT_FOUND_CONNECTION_EXCEPTION;
        }*/
        if (this.jarFileUrl == null) {
            this.jarFileUrl = buildJarFileUrl();
        }
        return this.jarFileUrl;
    }

    private URL buildJarFileUrl() {
        try {
            String spec = this.jarFile.getURL().getFile();
            if (spec.endsWith(SEPARATOR)) {
                spec = spec.substring(0, spec.length() - SEPARATOR.length());
            }
            if (!spec.contains(SEPARATOR)) {
                return new URL(spec);
            }
            return new URL("jar:" + spec);
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public JarEntry getJarEntry() throws IOException {
        if (this.jarEntryName == null || this.jarEntryName.isEmpty()) {
            return null;
        }
        connect();
        return this.jarEntry;
    }

    @Override
    public String getEntryName() {
        /*if (this.jarFile == null) {
            throw NOT_FOUND_CONNECTION_EXCEPTION;
        }*/
        return this.jarEntryName.toString();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        /*if (this.jarFile == null) {
            throw FILE_NOT_FOUND_EXCEPTION;
        }*/
        if (this.jarEntryName.isEmpty() && this.jarFile.getType() == ExJarFile.Type.DIRECT) {
            throw new IOException("no entry name specified");
        }
        connect();
        InputStream inputStream = (this.jarEntryName.isEmpty() ? this.jarFile.getInputStream()
                : this.jarFile.getInputStream(this.jarEntry));
        if (inputStream == null) {
            throwFileNotFound(this.jarEntryName, this.jarFile);
        }
        return inputStream;
    }

    private void throwFileNotFound(Object entry, ExJarFile jarFile) throws FileNotFoundException {
        /*if (Boolean.TRUE.equals(useFastExceptions.get())) {
            throw FILE_NOT_FOUND_EXCEPTION;
        }*/
        throw new FileNotFoundException("JAR entry " + entry + " not found in " + jarFile.getName());
    }

    @Override
    public int getContentLength() {
        long length = getContentLengthLong();
        if (length > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) length;
    }

    @Override
    public long getContentLengthLong() {
        /*if (this.jarFile == null) {
            return -1;
        }*/
        try {
            if (this.jarEntryName.isEmpty()) {
                return this.jarFile.size();
            }
            JarEntry entry = getJarEntry();
            return (entry != null) ? (int) entry.getSize() : -1;
        } catch (IOException ex) {
            return -1;
        }
    }

    @Override
    public Object getContent() throws IOException {
        connect();
        return this.jarEntryName.isEmpty() ? this.jarFile : super.getContent();
    }

    @Override
    public String getContentType() {
        return (this.jarEntryName != null) ? this.jarEntryName.getContentType() : null;
    }

    @Override
    public Permission getPermission() throws IOException {
        /*if (this.jarFile == null) {
            throw FILE_NOT_FOUND_EXCEPTION;
        }*/
        if (this.permission == null) {
            this.permission = this.jarFile.getPermission();
        }
        return this.permission;
    }

    @Override
    public long getLastModified() {
        if (this.jarFile == null || this.jarEntryName.isEmpty()) {
            return 0;
        }
        try {
            JarEntry entry = getJarEntry();
            return (entry != null) ? entry.getTime() : 0;
        } catch (IOException ex) {
            return 0;
        }
    }

    /*static void setUseFastExceptions(boolean useFastExceptions) {
        //JarURLConnection.useFastExceptions.set(useFastExceptions);
    }*/

    public static ExJarConnection get(URL url, ExJarFile jarFile) throws IOException {
        StringSequence spec = new StringSequence(url.getFile());
        int index = indexOfRootSpec(spec, jarFile.getPathFromRoot());
        if (index == -1) {
            throw new IllegalArgumentException("'" + SEPARATOR + "' not found: " + url);
			/*return (Boolean.TRUE.equals(useFastExceptions.get()) ? NOT_FOUND_CONNECTION
					: new JarURLConnection(url, null, EMPTY_JAR_ENTRY_NAME));*/
        }
        int separator;
        while ((separator = spec.indexOf(SEPARATOR, index)) > 0) {
            JarEntryName entryName = JarEntryName.get(spec.subSequence(index, separator));
            ExJarEntry jarEntry = jarFile.getJarEntry(entryName.toCharSequence());
            if (jarEntry == null) {
                throw new IllegalArgumentException("Jar entity not found: " + entryName);
                //return JarURLConnection.notFound(jarFile, entryName);
            }
            jarFile = jarFile.getNestedJarFile(jarEntry);
            index = separator + SEPARATOR.length();
        }
        JarEntryName jarEntryName = JarEntryName.get(spec, index);
        if (!jarEntryName.isEmpty() && !jarFile.containsEntry(jarEntryName.toString())) {
            throw new IllegalArgumentException("Jar entity not found: " + jarEntryName);
        }
        /*if (Boolean.TRUE.equals(useFastExceptions.get()) && !jarEntryName.isEmpty()
                && !jarFile.containsEntry(jarEntryName.toString())) {
            return NOT_FOUND_CONNECTION;
        }*/
        return new ExJarConnection(url, jarFile, jarEntryName);
    }

    private static int indexOfRootSpec(StringSequence file, String pathFromRoot) {
        int separatorIndex = file.indexOf(SEPARATOR);
        if (separatorIndex < 0 || !file.startsWith(pathFromRoot, separatorIndex)) {
            return -1;
        }
        return separatorIndex + SEPARATOR.length() + pathFromRoot.length();
    }

    /*private static JarURLConnection notFound() {
        try {
            return notFound(null, null);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }*/

    /*private static JarURLConnection notFound(JarFile jarFile, JarEntryName jarEntryName) throws IOException {
        if (Boolean.TRUE.equals(useFastExceptions.get())) {
            return NOT_FOUND_CONNECTION;
        }
        return new JarURLConnection(null, jarFile, jarEntryName);
    }*/

    /**
     * A JarEntryName parsed from a URL String.
     */
    public static class JarEntryName {

        private final StringSequence name;

        private String contentType;

        JarEntryName(StringSequence spec) {
            this.name = decode(spec);
        }

        private StringSequence decode(StringSequence source) {
            if (source.isEmpty() || (source.indexOf('%') < 0)) {
                return source;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length());
            write(source.toString(), bos);
            // AsciiBytes is what is used to store the JarEntries so make it symmetric
            return new StringSequence(AsciiBytes.toString(bos.toByteArray()));
        }

        private void write(String source, ByteArrayOutputStream outputStream) {
            int length = source.length();
            for (int i = 0; i < length; i++) {
                int c = source.charAt(i);
                if (c > 127) {
                    try {
                        String encoded = URLEncoder.encode(String.valueOf((char) c), "UTF-8");
                        write(encoded, outputStream);
                    } catch (UnsupportedEncodingException ex) {
                        throw new IllegalStateException(ex);
                    }
                } else {
                    if (c == '%') {
                        if ((i + 2) >= length) {
                            throw new IllegalArgumentException(
                                    "Invalid encoded sequence \"" + source.substring(i) + "\"");
                        }
                        c = decodeEscapeSequence(source, i);
                        i += 2;
                    }
                    outputStream.write(c);
                }
            }
        }

        private char decodeEscapeSequence(String source, int i) {
            int hi = Character.digit(source.charAt(i + 1), 16);
            int lo = Character.digit(source.charAt(i + 2), 16);
            if (hi == -1 || lo == -1) {
                throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
            }
            return ((char) ((hi << 4) + lo));
        }

        CharSequence toCharSequence() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name.toString();
        }

        boolean isEmpty() {
            return this.name.isEmpty();
        }

        String getContentType() {
            if (this.contentType == null) {
                this.contentType = deduceContentType();
            }
            return this.contentType;
        }

        private String deduceContentType() {
            // Guess the content type, don't bother with streams as mark is not supported
            String type = isEmpty() ? "x-java/jar" : null;
            type = (type != null) ? type : guessContentTypeFromName(toString());
            type = (type != null) ? type : "content/unknown";
            return type;
        }

        static JarEntryName get(StringSequence spec) {
            return get(spec, 0);
        }

        static JarEntryName get(StringSequence spec, int beginIndex) {
            if (spec.length() <= beginIndex) {
                return EMPTY_JAR_ENTRY_NAME;
            }
            return new JarEntryName(spec.subSequence(beginIndex));
        }

    }

}
