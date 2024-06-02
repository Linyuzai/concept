package com.github.linyuzai.plugin.jar.extension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.Manifest;

public interface Archive extends Iterable<Archive.Entry>, AutoCloseable {

	/**
	 * Returns a URL that can be used to load the archive.
	 * @return the archive URL
	 * @throws MalformedURLException if the URL is malformed
	 */
	URL getUrl() throws MalformedURLException;

	/**
	 * Returns the manifest of the archive.
	 * @return the manifest
	 * @throws IOException if the manifest cannot be read
	 */
	Manifest getManifest() throws IOException;

	/**
	 * Returns nested {@link Archive}s for entries that match the specified filters.
	 * @param searchFilter filter used to limit when additional sub-entry searching is
	 * required or {@code null} if all entries should be considered.
	 * @param includeFilter filter used to determine which entries should be included in
	 * the result or {@code null} if all entries should be included
	 * @return the nested archives
	 * @throws IOException on IO error
	 * @since 2.3.0
	 */
	default Iterator<Archive> getNestedArchives(EntryFilter searchFilter, EntryFilter includeFilter)
			throws IOException {
		EntryFilter combinedFilter = (entry) -> (searchFilter == null || searchFilter.matches(entry))
				&& (includeFilter == null || includeFilter.matches(entry));
		List<Archive> nestedArchives = getNestedArchives(combinedFilter);
		return nestedArchives.iterator();
	}

	/**
	 * Returns nested {@link Archive}s for entries that match the specified filter.
	 * @param filter the filter used to limit entries
	 * @return nested archives
	 * @throws IOException if nested archives cannot be read
	 * @deprecated since 2.3.0 for removal in 2.5.0 in favor of
	 * {@link #getNestedArchives(EntryFilter, EntryFilter)}
	 */
	@Deprecated
	default List<Archive> getNestedArchives(EntryFilter filter) throws IOException {
		throw new IllegalStateException("Unexpected call to getNestedArchives(filter)");
	}

	/**
	 * Return a new iterator for the archive entries.
	 * @deprecated since 2.3.0 for removal in 2.5.0 in favor of using
	 * {@link org.springframework.boot.loader.jar.JarFile} to access entries and
	 * {@link #getNestedArchives(EntryFilter, EntryFilter)} for accessing nested archives.
	 * @see Iterable#iterator()
	 */
	@Deprecated
	@Override
	Iterator<Entry> iterator();

	/**
	 * Performs the given action for each element of the {@code Iterable} until all
	 * elements have been processed or the action throws an exception.
	 * @deprecated since 2.3.0 for removal in 2.5.0 in favor of using
	 * {@link org.springframework.boot.loader.jar.JarFile} to access entries and
	 * {@link #getNestedArchives(EntryFilter, EntryFilter)} for accessing nested archives.
	 * @see Iterable#forEach
	 */
	@Deprecated
	@Override
	default void forEach(Consumer<? super Entry> action) {
		Objects.requireNonNull(action);
		for (Entry entry : this) {
			action.accept(entry);
		}
	}

	/**
	 * Creates a {@link Spliterator} over the elements described by this {@code Iterable}.
	 * @deprecated since 2.3.0 for removal in 2.5.0 in favor of using
	 * {@link org.springframework.boot.loader.jar.JarFile} to access entries and
	 * {@link #getNestedArchives(EntryFilter, EntryFilter)} for accessing nested archives.
	 * @see Iterable#spliterator
	 */
	@Deprecated
	@Override
	default Spliterator<Entry> spliterator() {
		return Spliterators.spliteratorUnknownSize(iterator(), 0);
	}

	/**
	 * Return if the archive is exploded (already unpacked).
	 * @return if the archive is exploded
	 * @since 2.3.0
	 */
	default boolean isExploded() {
		return false;
	}

	/**
	 * Closes the {@code Archive}, releasing any open resources.
	 * @throws Exception if an error occurs during close processing
	 * @since 2.2.0
	 */
	@Override
	default void close() throws Exception {

	}

	/**
	 * Represents a single entry in the archive.
	 */
	interface Entry {

		/**
		 * Returns {@code true} if the entry represents a directory.
		 * @return if the entry is a directory
		 */
		boolean isDirectory();

		/**
		 * Returns the name of the entry.
		 * @return the name of the entry
		 */
		String getName();

	}

	/**
	 * Strategy interface to filter {@link Entry Entries}.
	 */
	@FunctionalInterface
	interface EntryFilter {

		/**
		 * Apply the jar entry filter.
		 * @param entry the entry to filter
		 * @return {@code true} if the filter matches
		 */
		boolean matches(Entry entry);

	}

}
