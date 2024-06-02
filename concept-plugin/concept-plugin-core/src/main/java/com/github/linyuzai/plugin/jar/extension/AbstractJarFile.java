package com.github.linyuzai.plugin.jar.extension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;

/**
 * Base class for extended variants of {@link java.util.jar.JarFile}.
 *
 * @author Phillip Webb
 */
abstract class AbstractJarFile extends java.util.jar.JarFile {

	/**
	 * Create a new {@link AbstractJarFile}.
	 * @param file the root jar file.
	 * @throws IOException on IO error
	 */
	AbstractJarFile(File file) throws IOException {
		super(file);
	}

	/**
	 * Return a URL that can be used to access this JAR file. NOTE: the specified URL
	 * cannot be serialized and or cloned.
	 * @return the URL
	 * @throws MalformedURLException if the URL is malformed
	 */
	abstract URL getUrl() throws MalformedURLException;

	/**
	 * Return the {@link NestedJarFile.Type} of this instance.
	 * @return the jar file type
	 */
	abstract NestedJarFile.Type getType();

	/**
	 * Return the security permission for this JAR.
	 * @return the security permission.
	 */
	abstract Permission getPermission();

	/**
	 * Return an {@link InputStream} for the entire jar contents.
	 * @return the contents input stream
	 * @throws IOException on IO error
	 */
	abstract InputStream getInputStream() throws IOException;

}
