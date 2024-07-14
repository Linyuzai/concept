package com.github.linyuzai.plugin.jar.extension.file;

/**
 * Callback visitor triggered by {@link CentralDirectoryParser}.
 */
public interface CentralDirectoryVisitor {

    void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData);

    void visitFileHeader(CentralDirectoryFileHeader fileHeader, long dataOffset);

    void visitEnd();

}
