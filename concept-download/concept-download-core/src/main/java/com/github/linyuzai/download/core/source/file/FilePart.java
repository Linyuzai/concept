package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.concept.AbstractPart;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.web.ContentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 支持 {@link File} 的 {@link Part}。
 */
@Getter
@RequiredArgsConstructor
public class FilePart extends AbstractPart {

    /**
     * 文件
     */
    protected final File file;

    /**
     * 名称
     */
    protected final String name;

    /**
     * 路径
     */
    protected final String path;

    /**
     * 如果 {@link File#isFile()} 则返回 {@link FileInputStream}，
     * 否则返回 {@link EmptyInputStream}。
     *
     * @return {@link FileInputStream} 或 {@link EmptyInputStream}
     */
    @Override
    public InputStream openInputStream() throws IOException {
        return file.isFile() ? Files.newInputStream(file.toPath()) : new EmptyInputStream();
    }

    /**
     * 获得名称。
     *
     * @return 名称
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 获得文件路径。
     *
     * @return 文件路径
     */
    @Override
    public String getPath() {
        return file.isFile() ? path : path + File.separator;
    }

    /**
     * 如果 {@link File#isFile()} 则返回 {@link ContentType#file(File)}，否则返回 null。
     *
     * @return {@link ContentType#file(File)} 获得的 Content-Type 或 null
     */
    @Override
    public String getContentType() {
        return file.isFile() ? ContentType.file(file) : null;
    }

    /**
     * 直接返回 null。
     *
     * @return null
     */
    @Override
    public Charset getCharset() {
        return null;
    }

    /**
     * 如果 {@link File#isFile()} 则返回 {@link File#length()}，否则返回 null。
     *
     * @return {@link File#length()} 或 null
     */
    @Override
    public Long getLength() {
        return file.isFile() ? file.length() : null;
    }

    /**
     * 获得子目录。
     * 如果 {@link File#isFile()} 则返回 {@link Collections#emptyList()}。
     *
     * @return 子目录
     */
    @Override
    public Collection<Part> getChildren() {
        if (file.isFile()) {
            return Collections.emptyList();
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return Collections.emptyList();
        } else {
            Collection<Part> parts = new ArrayList<>();
            for (File f : files) {
                String childName = f.getName();
                String childPath = path + File.separator + childName;
                Part childPart = new FilePart(f, childName, childPath);
                parts.add(childPart);
            }
            return parts;
        }
    }

    @Override
    public String toString() {
        return "FilePart(" + file.getAbsolutePath() + ")";
    }
}
