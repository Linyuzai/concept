package com.github.linyuzai.plugin.jar.extension;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.regex.Pattern;

/**
 * {@link URLStreamHandler} for Spring Boot loader {@link ExJarFile}s.
 */
public class ExJarHandler extends URLStreamHandler {

    // NOTE: in order to be found as a URL protocol handler, this class must be public,
    // must be named Handler and must be in a package ending '.jar'

    private static final String JAR_PROTOCOL = "jar:";

    private static final String FILE_PROTOCOL = "file:";

    //private static final String TOMCAT_WARFILE_PROTOCOL = "war:file:";

    private static final String SEPARATOR = "!/";

    private static final Pattern SEPARATOR_PATTERN = Pattern.compile(SEPARATOR, Pattern.LITERAL);

    private static final String CURRENT_DIR = "/./";

    private static final Pattern CURRENT_DIR_PATTERN = Pattern.compile(CURRENT_DIR, Pattern.LITERAL);

    private static final String PARENT_DIR = "/../";

    private final ExJarFile jarFile;

    public ExJarHandler(ExJarFile jarFile) {
        this.jarFile = jarFile;
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        if (this.jarFile != null && isUrlInJarFile(url, this.jarFile)) {
            return ExJarConnection.get(url, this.jarFile);
        }
        return ExJarConnection.get(url, getRootJarFileFromUrl(url));
    }

    private boolean isUrlInJarFile(URL url, ExJarFile jarFile) throws MalformedURLException {
        // Try the path first to save building a new url string each time
        return url.getPath().startsWith(jarFile.getURL().getPath())
                && url.toString().startsWith(jarFile.getURL().toString());
    }

    @Override
    protected void parseURL(URL context, String spec, int start, int limit) {
        if (spec.regionMatches(true, 0, JAR_PROTOCOL, 0, JAR_PROTOCOL.length())) {
            setFile(context, getFileFromSpec(spec.substring(start, limit)));
        } else {
            setFile(context, getFileFromContext(context, spec.substring(start, limit)));
        }
    }

    private String getFileFromSpec(String spec) {
        int separatorIndex = spec.lastIndexOf("!/");
        if (separatorIndex == -1) {
            throw new IllegalArgumentException("No !/ in spec '" + spec + "'");
        }
        try {
            new URL(spec.substring(0, separatorIndex));
            return spec;
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid spec URL '" + spec + "'", ex);
        }
    }

    private String getFileFromContext(URL context, String spec) {
        String file = context.getFile();
        if (spec.startsWith("/")) {
            return trimToJarRoot(file) + SEPARATOR + spec.substring(1);
        }
        if (file.endsWith("/")) {
            return file + spec;
        }
        int lastSlashIndex = file.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            throw new IllegalArgumentException("No / found in context URL's file '" + file + "'");
        }
        return file.substring(0, lastSlashIndex + 1) + spec;
    }

    private String trimToJarRoot(String file) {
        int lastSeparatorIndex = file.lastIndexOf(SEPARATOR);
        if (lastSeparatorIndex == -1) {
            throw new IllegalArgumentException("No !/ found in context URL's file '" + file + "'");
        }
        return file.substring(0, lastSeparatorIndex);
    }

    private void setFile(URL context, String file) {
        String path = normalize(file);
        String query = null;
        int queryIndex = path.lastIndexOf('?');
        if (queryIndex != -1) {
            query = path.substring(queryIndex + 1);
            path = path.substring(0, queryIndex);
        }
        setURL(context, JAR_PROTOCOL, null, -1, null, null, path, query, context.getRef());
    }

    private String normalize(String file) {
        if (!file.contains(CURRENT_DIR) && !file.contains(PARENT_DIR)) {
            return file;
        }
        int afterLastSeparatorIndex = file.lastIndexOf(SEPARATOR) + SEPARATOR.length();
        String afterSeparator = file.substring(afterLastSeparatorIndex);
        afterSeparator = replaceParentDir(afterSeparator);
        afterSeparator = replaceCurrentDir(afterSeparator);
        return file.substring(0, afterLastSeparatorIndex) + afterSeparator;
    }

    private String replaceParentDir(String file) {
        int parentDirIndex;
        while ((parentDirIndex = file.indexOf(PARENT_DIR)) >= 0) {
            int precedingSlashIndex = file.lastIndexOf('/', parentDirIndex - 1);
            if (precedingSlashIndex >= 0) {
                file = file.substring(0, precedingSlashIndex) + file.substring(parentDirIndex + 3);
            } else {
                file = file.substring(parentDirIndex + 4);
            }
        }
        return file;
    }

    private String replaceCurrentDir(String file) {
        return CURRENT_DIR_PATTERN.matcher(file).replaceAll("/");
    }

    @Override
    protected int hashCode(URL u) {
        return hashCode(u.getProtocol(), u.getFile());
    }

    private int hashCode(String protocol, String file) {
        int result = (protocol != null) ? protocol.hashCode() : 0;
        int separatorIndex = file.indexOf(SEPARATOR);
        if (separatorIndex == -1) {
            return result + file.hashCode();
        }
        String source = file.substring(0, separatorIndex);
        String entry = canonicalize(file.substring(separatorIndex + 2));
        try {
            result += new URL(source).hashCode();
        } catch (MalformedURLException ex) {
            result += source.hashCode();
        }
        result += entry.hashCode();
        return result;
    }

    @Override
    protected boolean sameFile(URL u1, URL u2) {
        if (!u1.getProtocol().equals("jar") || !u2.getProtocol().equals("jar")) {
            return false;
        }
        int separator1 = u1.getFile().indexOf(SEPARATOR);
        int separator2 = u2.getFile().indexOf(SEPARATOR);
        if (separator1 == -1 || separator2 == -1) {
            return super.sameFile(u1, u2);
        }
        String nested1 = u1.getFile().substring(separator1 + SEPARATOR.length());
        String nested2 = u2.getFile().substring(separator2 + SEPARATOR.length());
        if (!nested1.equals(nested2)) {
            String canonical1 = canonicalize(nested1);
            String canonical2 = canonicalize(nested2);
            if (!canonical1.equals(canonical2)) {
                return false;
            }
        }
        String root1 = u1.getFile().substring(0, separator1);
        String root2 = u2.getFile().substring(0, separator2);
        try {
            return super.sameFile(new URL(root1), new URL(root2));
        } catch (MalformedURLException ex) {
            // Continue
        }
        return super.sameFile(u1, u2);
    }

    private String canonicalize(String path) {
        return SEPARATOR_PATTERN.matcher(path).replaceAll("/");
    }

    public ExJarFile getRootJarFileFromUrl(URL url) throws IOException {
        String spec = url.getFile();
        int separatorIndex = spec.indexOf(SEPARATOR);
        if (separatorIndex == -1) {
            throw new MalformedURLException("Jar URL does not contain !/ separator");
        }
        String name = spec.substring(0, separatorIndex);
        return getRootJarFile(name);
    }

    private ExJarFile getRootJarFile(String name) throws IOException {
        try {
            if (!name.startsWith(FILE_PROTOCOL)) {
                throw new IllegalStateException("Not a file URL");
            }
            File file = new File(URI.create(name));
            return new ExJarFile(file);
        } catch (Exception ex) {
            throw new IOException("Unable to open root Jar file '" + name + "'", ex);
        }
    }
}
