/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.linyuzai.plugin.jar.extension;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * Provides access to entries from a {@link NestedJarFile}. In order to reduce memory
 * consumption entry details are stored using arrays. The {@code hashCodes} array stores
 * the hash code of the entry name, the {@code centralDirectoryOffsets} provides the
 * offset to the central directory record and {@code positions} provides the original
 * order position of the entry. The arrays are stored in hashCode order so that a binary
 * search can be used to find a name.
 * <p>
 * A typical Spring Boot application will have somewhere in the region of 10,500 entries
 * which should consume about 122K.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public class NestedJarFileEntries implements CentralDirectoryVisitor, Iterable<NestedJarEntry> {

	private static final Runnable NO_VALIDATION = () -> {
	};

	private static final String META_INF_PREFIX = "META-INF/";

	private static final Name MULTI_RELEASE = new Name("Multi-Release");

	private static final int BASE_VERSION = 8;

	private static final int RUNTIME_VERSION;

	static {
		int version;
		try {
			Object runtimeVersion = Runtime.class.getMethod("version").invoke(null);
			version = (int) runtimeVersion.getClass().getMethod("major").invoke(runtimeVersion);
		}
		catch (Throwable ex) {
			version = BASE_VERSION;
		}
		RUNTIME_VERSION = version;
	}

	private static final long LOCAL_FILE_HEADER_SIZE = 30;

	private static final char SLASH = '/';

	private static final char NO_SUFFIX = 0;

	protected static final int ENTRY_CACHE_SIZE = 25;

	private final NestedJarFile jarFile;

	private final NestedJarEntryFilter filter;

	private RandomAccessData centralDirectoryData;

	private int size;

	private int[] hashCodes;

	private Offsets centralDirectoryOffsets;

	private int[] positions;

	private Boolean multiReleaseJar;

	private NestedJarEntryCertification[] certifications;

	private final Map<Integer, FileHeader> entriesCache = Collections
			.synchronizedMap(new LinkedHashMap<Integer, FileHeader>(16, 0.75f, true) {

				@Override
				protected boolean removeEldestEntry(Map.Entry<Integer, FileHeader> eldest) {
					return size() >= ENTRY_CACHE_SIZE;
				}

			});

	NestedJarFileEntries(NestedJarFile jarFile, NestedJarEntryFilter filter) {
		this.jarFile = jarFile;
		this.filter = filter;
		if (RUNTIME_VERSION == BASE_VERSION) {
			this.multiReleaseJar = false;
		}
	}

	@Override
	public void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData) {
		int maxSize = endRecord.getNumberOfRecords();
		this.centralDirectoryData = centralDirectoryData;
		this.hashCodes = new int[maxSize];
		this.centralDirectoryOffsets = Offsets.from(endRecord);
		this.positions = new int[maxSize];
	}

	@Override
	public void visitFileHeader(CentralDirectoryFileHeader fileHeader, long dataOffset) {
		AsciiBytes name = applyFilter(fileHeader.getName());
		if (name != null) {
			add(name, dataOffset);
		}
	}

	private void add(AsciiBytes name, long dataOffset) {
		this.hashCodes[this.size] = name.hashCode();
		this.centralDirectoryOffsets.set(this.size, dataOffset);
		this.positions[this.size] = this.size;
		this.size++;
	}

	@Override
	public void visitEnd() {
		sort(0, this.size - 1);
		int[] positions = this.positions;
		this.positions = new int[positions.length];
		for (int i = 0; i < this.size; i++) {
			this.positions[positions[i]] = i;
		}
	}

	int getSize() {
		return this.size;
	}

	private void sort(int left, int right) {
		// Quick sort algorithm, uses hashCodes as the source but sorts all arrays
		if (left < right) {
			int pivot = this.hashCodes[left + (right - left) / 2];
			int i = left;
			int j = right;
			while (i <= j) {
				while (this.hashCodes[i] < pivot) {
					i++;
				}
				while (this.hashCodes[j] > pivot) {
					j--;
				}
				if (i <= j) {
					swap(i, j);
					i++;
					j--;
				}
			}
			if (left < j) {
				sort(left, j);
			}
			if (right > i) {
				sort(i, right);
			}
		}
	}

	private void swap(int i, int j) {
		swap(this.hashCodes, i, j);
		this.centralDirectoryOffsets.swap(i, j);
		swap(this.positions, i, j);
	}

	@Override
	public Iterator<NestedJarEntry> iterator() {
		return new EntryIterator(NO_VALIDATION);
	}

	Iterator<NestedJarEntry> iterator(Runnable validator) {
		return new EntryIterator(validator);
	}

	boolean containsEntry(CharSequence name) {
		return getEntry(name, FileHeader.class, true) != null;
	}

	NestedJarEntry getEntry(CharSequence name) {
		return getEntry(name, NestedJarEntry.class, true);
	}

	InputStream getInputStream(String name) throws IOException {
		FileHeader entry = getEntry(name, FileHeader.class, false);
		return getInputStream(entry);
	}

	InputStream getInputStream(FileHeader entry) throws IOException {
		if (entry == null) {
			return null;
		}
		InputStream inputStream = getEntryData(entry).getInputStream();
		if (entry.getMethod() == ZipEntry.DEFLATED) {
			inputStream = new ZipInflaterInputStream(inputStream, (int) entry.getSize());
		}
		return inputStream;
	}

	RandomAccessData getEntryData(String name) throws IOException {
		FileHeader entry = getEntry(name, FileHeader.class, false);
		if (entry == null) {
			return null;
		}
		return getEntryData(entry);
	}

	private RandomAccessData getEntryData(FileHeader entry) throws IOException {
		// aspectjrt-1.7.4.jar has a different ext bytes length in the
		// local directory to the central directory. We need to re-read
		// here to skip them
		RandomAccessData data = this.jarFile.getData();
		byte[] localHeader = data.read(entry.getLocalHeaderOffset(), LOCAL_FILE_HEADER_SIZE);
		long nameLength = Bytes.littleEndianValue(localHeader, 26, 2);
		long extraLength = Bytes.littleEndianValue(localHeader, 28, 2);
		return data.getSubsection(entry.getLocalHeaderOffset() + LOCAL_FILE_HEADER_SIZE + nameLength + extraLength,
				entry.getCompressedSize());
	}

	private <T extends FileHeader> T getEntry(CharSequence name, Class<T> type, boolean cacheEntry) {
		T entry = doGetEntry(name, type, cacheEntry, null);
		if (!isMetaInfEntry(name) && isMultiReleaseJar()) {
			int version = RUNTIME_VERSION;
			AsciiBytes nameAlias = (entry instanceof NestedJarEntry) ? ((NestedJarEntry) entry).getAsciiBytesName()
					: new AsciiBytes(name.toString());
			while (version > BASE_VERSION) {
				T versionedEntry = doGetEntry("META-INF/versions/" + version + "/" + name, type, cacheEntry, nameAlias);
				if (versionedEntry != null) {
					return versionedEntry;
				}
				version--;
			}
		}
		return entry;
	}

	private boolean isMetaInfEntry(CharSequence name) {
		return name.toString().startsWith(META_INF_PREFIX);
	}

	private boolean isMultiReleaseJar() {
		Boolean multiRelease = this.multiReleaseJar;
		if (multiRelease != null) {
			return multiRelease;
		}
		try {
			Manifest manifest = this.jarFile.getManifest();
			if (manifest == null) {
				multiRelease = false;
			}
			else {
				Attributes attributes = manifest.getMainAttributes();
				multiRelease = attributes.containsKey(MULTI_RELEASE);
			}
		}
		catch (IOException ex) {
			multiRelease = false;
		}
		this.multiReleaseJar = multiRelease;
		return multiRelease;
	}

	private <T extends FileHeader> T doGetEntry(CharSequence name, Class<T> type, boolean cacheEntry,
			AsciiBytes nameAlias) {
		int hashCode = AsciiBytes.hashCode(name);
		T entry = getEntry(hashCode, name, NO_SUFFIX, type, cacheEntry, nameAlias);
		if (entry == null) {
			hashCode = AsciiBytes.hashCode(hashCode, SLASH);
			entry = getEntry(hashCode, name, SLASH, type, cacheEntry, nameAlias);
		}
		return entry;
	}

	private <T extends FileHeader> T getEntry(int hashCode, CharSequence name, char suffix, Class<T> type,
			boolean cacheEntry, AsciiBytes nameAlias) {
		int index = getFirstIndex(hashCode);
		while (index >= 0 && index < this.size && this.hashCodes[index] == hashCode) {
			T entry = getEntry(index, type, cacheEntry, nameAlias);
			if (entry.hasName(name, suffix)) {
				return entry;
			}
			index++;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T extends FileHeader> T getEntry(int index, Class<T> type, boolean cacheEntry, AsciiBytes nameAlias) {
		try {
			long offset = this.centralDirectoryOffsets.get(index);
			FileHeader cached = this.entriesCache.get(index);
			FileHeader entry = (cached != null) ? cached
					: CentralDirectoryFileHeader.fromRandomAccessData(this.centralDirectoryData, offset, this.filter);
			if (CentralDirectoryFileHeader.class.equals(entry.getClass()) && type.equals(NestedJarEntry.class)) {
				entry = new NestedJarEntry(this.jarFile, index, (CentralDirectoryFileHeader) entry, nameAlias);
			}
			if (cacheEntry && cached != entry) {
				this.entriesCache.put(index, entry);
			}
			return (T) entry;
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	private int getFirstIndex(int hashCode) {
		int index = Arrays.binarySearch(this.hashCodes, 0, this.size, hashCode);
		if (index < 0) {
			return -1;
		}
		while (index > 0 && this.hashCodes[index - 1] == hashCode) {
			index--;
		}
		return index;
	}

	void clearCache() {
		this.entriesCache.clear();
	}

	private AsciiBytes applyFilter(AsciiBytes name) {
		return (this.filter != null) ? this.filter.apply(name) : name;
	}

	NestedJarEntryCertification getCertification(NestedJarEntry entry) throws IOException {
		NestedJarEntryCertification[] certifications = this.certifications;
		if (certifications == null) {
			certifications = new NestedJarEntryCertification[this.size];
			// We fall back to use JarInputStream to obtain the certs. This isn't that
			// fast, but hopefully doesn't happen too often.
			try (JarInputStream certifiedJarStream = new JarInputStream(this.jarFile.getData().getInputStream())) {
				java.util.jar.JarEntry certifiedEntry = null;
				while ((certifiedEntry = certifiedJarStream.getNextJarEntry()) != null) {
					// Entry must be closed to trigger a read and set entry certificates
					certifiedJarStream.closeEntry();
					int index = getEntryIndex(certifiedEntry.getName());
					if (index != -1) {
						certifications[index] = NestedJarEntryCertification.from(certifiedEntry);
					}
				}
			}
			this.certifications = certifications;
		}
		NestedJarEntryCertification certification = certifications[entry.getIndex()];
		return (certification != null) ? certification : NestedJarEntryCertification.NONE;
	}

	private int getEntryIndex(CharSequence name) {
		int hashCode = AsciiBytes.hashCode(name);
		int index = getFirstIndex(hashCode);
		while (index >= 0 && index < this.size && this.hashCodes[index] == hashCode) {
			FileHeader candidate = getEntry(index, FileHeader.class, false, null);
			if (candidate.hasName(name, NO_SUFFIX)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	private static void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	private static void swap(long[] array, int i, int j) {
		long temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	/**
	 * Iterator for contained entries.
	 */
	private final class EntryIterator implements Iterator<NestedJarEntry> {

		private final Runnable validator;

		private int index = 0;

		private EntryIterator(Runnable validator) {
			this.validator = validator;
			validator.run();
		}

		@Override
		public boolean hasNext() {
			this.validator.run();
			return this.index < NestedJarFileEntries.this.size;
		}

		@Override
		public NestedJarEntry next() {
			this.validator.run();
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			int entryIndex = NestedJarFileEntries.this.positions[this.index];
			this.index++;
			return getEntry(entryIndex, NestedJarEntry.class, false, null);
		}

	}

	/**
	 * Interface to manage offsets to central directory records. Regular zip files are
	 * backed by an {@code int[]} based implementation, Zip64 files are backed by a
	 * {@code long[]} and will consume more memory.
	 */
	private interface Offsets {

		void set(int index, long value);

		long get(int index);

		void swap(int i, int j);

		static Offsets from(CentralDirectoryEndRecord endRecord) {
			int size = endRecord.getNumberOfRecords();
			return endRecord.isZip64() ? new Zip64Offsets(size) : new ZipOffsets(size);
		}

	}

	/**
	 * {@link Offsets} implementation for regular zip files.
	 */
	private static final class ZipOffsets implements Offsets {

		private final int[] offsets;

		private ZipOffsets(int size) {
			this.offsets = new int[size];
		}

		@Override
		public void swap(int i, int j) {
			NestedJarFileEntries.swap(this.offsets, i, j);
		}

		@Override
		public void set(int index, long value) {
			this.offsets[index] = (int) value;
		}

		@Override
		public long get(int index) {
			return this.offsets[index];
		}

	}

	/**
	 * {@link Offsets} implementation for zip64 files.
	 */
	private static final class Zip64Offsets implements Offsets {

		private final long[] offsets;

		private Zip64Offsets(int size) {
			this.offsets = new long[size];
		}

		@Override
		public void swap(int i, int j) {
			NestedJarFileEntries.swap(this.offsets, i, j);
		}

		@Override
		public void set(int index, long value) {
			this.offsets[index] = value;
		}

		@Override
		public long get(int index) {
			return this.offsets[index];
		}

	}

}
