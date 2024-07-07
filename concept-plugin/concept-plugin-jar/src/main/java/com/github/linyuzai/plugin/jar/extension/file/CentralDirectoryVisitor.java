package com.github.linyuzai.plugin.jar.extension.file;

/**
 * Callback visitor triggered by {@link CentralDirectoryParser}.
 *
 * @author Phillip Webb
 */
public interface CentralDirectoryVisitor {

	void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData);

	void visitFileHeader(CentralDirectoryFileHeader fileHeader, long dataOffset);

	void visitEnd();

}
