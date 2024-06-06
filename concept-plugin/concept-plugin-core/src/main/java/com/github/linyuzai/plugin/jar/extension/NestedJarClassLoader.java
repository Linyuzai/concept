package com.github.linyuzai.plugin.jar.extension;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.jar.JarFile;

public class NestedJarClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    /**
     * Create a new {@link NestedJarClassLoader} instance.
     *
     * @param urls   the URLs from which to load classes and resources
     * @param parent the parent class loader for delegation
     * @since 2.3.1
     */
    public NestedJarClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            definePackageIfNecessary(name);
        } catch (IllegalArgumentException ex) {
            // Tolerate race condition due to being parallel capable
            if (getPackage(name) == null) {
                // This should never happen as the IllegalArgumentException indicates
                // that the package has already been defined and, therefore,
                // getPackage(name) should not return null.
                throw new AssertionError("Package " + name + " has already been defined but it could not be found");
            }
        }
        return super.loadClass(name, resolve);
    }

    /**
     * Define a package before a {@code findClass} call is made. This is necessary to
     * ensure that the appropriate manifest for nested JARs is associated with the
     * package.
     *
     * @param className the class name being found
     */
    private void definePackageIfNecessary(String className) {
        int lastDot = className.lastIndexOf('.');
        if (lastDot >= 0) {
            String packageName = className.substring(0, lastDot);
            if (getPackage(packageName) == null) {
                try {
                    definePackage(className, packageName);
                } catch (IllegalArgumentException ex) {
                    // Tolerate race condition due to being parallel capable
                    if (getPackage(packageName) == null) {
                        // This should never happen as the IllegalArgumentException
                        // indicates that the package has already been defined and,
                        // therefore, getPackage(name) should not have returned null.
                        throw new AssertionError(
                                "Package " + packageName + " has already been defined but it could not be found");
                    }
                }
            }
        }
    }

    private void definePackage(String className, String packageName) {
        String packageEntryName = packageName.replace('.', '/') + "/";
        String classEntryName = className.replace('.', '/') + ".class";
        for (URL url : getURLs()) {
            try {
                URLConnection connection = url.openConnection();
                if (connection instanceof JarURLConnection) {
                    JarFile jarFile = ((JarURLConnection) connection).getJarFile();
                    if (jarFile.getEntry(classEntryName) != null && jarFile.getEntry(packageEntryName) != null
                            && jarFile.getManifest() != null) {
                        definePackage(packageName, jarFile.getManifest(), url);
                    }
                }
            } catch (IOException ex) {
                // Ignore
            }
        }
    }
}
