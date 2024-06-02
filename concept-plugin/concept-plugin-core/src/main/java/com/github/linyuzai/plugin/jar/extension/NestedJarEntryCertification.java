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

import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.jar.JarEntry;

/**
 * {@link Certificate} and {@link CodeSigner} details for a {@link NestedJarEntry} from a signed
 * {@link NestedJarFile}.
 *
 * @author Phillip Webb
 */
class NestedJarEntryCertification {

	static final NestedJarEntryCertification NONE = new NestedJarEntryCertification(null, null);

	private final Certificate[] certificates;

	private final CodeSigner[] codeSigners;

	NestedJarEntryCertification(Certificate[] certificates, CodeSigner[] codeSigners) {
		this.certificates = certificates;
		this.codeSigners = codeSigners;
	}

	Certificate[] getCertificates() {
		return (this.certificates != null) ? this.certificates.clone() : null;
	}

	CodeSigner[] getCodeSigners() {
		return (this.codeSigners != null) ? this.codeSigners.clone() : null;
	}

	static NestedJarEntryCertification from(JarEntry certifiedEntry) {
		Certificate[] certificates = (certifiedEntry != null) ? certifiedEntry.getCertificates() : null;
		CodeSigner[] codeSigners = (certifiedEntry != null) ? certifiedEntry.getCodeSigners() : null;
		if (certificates == null && codeSigners == null) {
			return NONE;
		}
		return new NestedJarEntryCertification(certificates, codeSigners);
	}

}
