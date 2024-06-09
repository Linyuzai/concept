/*
 * Copyright 2012-2020 the original author or authors.
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

import com.github.linyuzai.plugin.jar.extension.file.AsciiBytes;
import com.github.linyuzai.plugin.jar.extension.file.CentralDirectoryFileHeader;
import com.github.linyuzai.plugin.jar.extension.file.FileHeader;
import lombok.Getter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.Manifest;

/**
 * Extended variant of {@link JarEntry} returned by {@link ExJarFile}s.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public class ExJarEntry extends JarEntry implements FileHeader {

	@Getter
	private final int index;

	private final AsciiBytes name;

	@Getter
	private final AsciiBytes headerName;

	@Getter
	private final ExJarFile jarFile;

	@Getter
	private final long localHeaderOffset;

	private volatile Certification certification;

	public ExJarEntry(ExJarFile jarFile, int index, CentralDirectoryFileHeader header, AsciiBytes nameAlias) {
		super((nameAlias != null) ? nameAlias.toString() : header.getName().toString());
		this.index = index;
		this.name = (nameAlias != null) ? nameAlias : header.getName();
		this.headerName = header.getName();
		this.jarFile = jarFile;
		this.localHeaderOffset = header.getLocalHeaderOffset();
		setCompressedSize(header.getCompressedSize());
		setMethod(header.getMethod());
		setCrc(header.getCrc());
		setComment(header.getComment().toString());
		setSize(header.getSize());
		setTime(header.getTime());
		if (header.hasExtra()) {
			setExtra(header.getExtra());
		}
	}

	public AsciiBytes getAsciiBytesName() {
		return this.name;
	}

	@Override
	public boolean hasName(CharSequence name, char suffix) {
		return this.headerName.matches(name, suffix);
	}

	/**
	 * Return a {@link URL} for this {@link ExJarEntry}.
	 * @return the URL for the entry
	 * @throws MalformedURLException if the URL is not valid
	 */
	public URL getURL() throws MalformedURLException {
		return new URL(this.jarFile.getURL(), getName(), new ExJarHandler(jarFile));
	}

	@Override
	public Attributes getAttributes() throws IOException {
		Manifest manifest = this.jarFile.getManifest();
		return (manifest != null) ? manifest.getAttributes(getName()) : null;
	}

	@Override
	public Certificate[] getCertificates() {
		return getCertification().getCertificates();
	}

	@Override
	public CodeSigner[] getCodeSigners() {
		return getCertification().getCodeSigners();
	}

	public Certification getCertification() {
		if (!this.jarFile.isSigned()) {
			return Certification.NONE;
		}
		Certification certification = this.certification;
		if (certification == null) {
			certification = this.jarFile.getCertification(this);
			this.certification = certification;
		}
		return certification;
	}

	public ExJarFile asJarFile() throws IOException {
		return jarFile.getNestedJarFile(this);
	}

	/**
	 * Interface that can be used to filter and optionally rename jar entries.
	 *
	 * @author Phillip Webb
	 */
	public interface Filter {

		/**
		 * Apply the jar entry filter.
		 * @param name the current entry name. This may be different that the original entry
		 * name if a previous filter has been applied
		 * @return the new name of the entry or {@code null} if the entry should not be
		 * included.
		 */
		AsciiBytes apply(AsciiBytes name);

	}

	/**
	 * {@link Certificate} and {@link CodeSigner} details for a {@link ExJarEntry} from a signed
	 * {@link ExJarFile}.
	 *
	 * @author Phillip Webb
	 */
	public static class Certification {

		static final Certification NONE = new Certification(null, null);

		private final Certificate[] certificates;

		private final CodeSigner[] codeSigners;

		Certification(Certificate[] certificates, CodeSigner[] codeSigners) {
			this.certificates = certificates;
			this.codeSigners = codeSigners;
		}

		Certificate[] getCertificates() {
			return (this.certificates != null) ? this.certificates.clone() : null;
		}

		CodeSigner[] getCodeSigners() {
			return (this.codeSigners != null) ? this.codeSigners.clone() : null;
		}

		static Certification from(JarEntry certifiedEntry) {
			Certificate[] certificates = (certifiedEntry != null) ? certifiedEntry.getCertificates() : null;
			CodeSigner[] codeSigners = (certifiedEntry != null) ? certifiedEntry.getCodeSigners() : null;
			if (certificates == null && codeSigners == null) {
				return NONE;
			}
			return new Certification(certificates, codeSigners);
		}

	}
}
