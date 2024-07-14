package com.github.linyuzai.plugin.jar.extension.file;

import java.io.*;

/**
 * {@link RandomAccessData} implementation backed by a {@link RandomAccessFile}.
 */
public class RandomAccessDataFile implements RandomAccessData {

    private final FileAccess fileAccess;

    private final long offset;

    private final long length;

    /**
     * Create a new {@link RandomAccessDataFile} backed by the specified file.
     *
     * @param file the underlying file
     * @throws IllegalArgumentException if the file is null or does not exist
     */
    public RandomAccessDataFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        this.fileAccess = new FileAccess(file);
        this.offset = 0L;
        this.length = file.length();
    }

    /**
     * Private constructor used to create a {@link #getSubsection(long, long) subsection}.
     *
     * @param fileAccess provides access to the underlying file
     * @param offset     the offset of the section
     * @param length     the length of the section
     */
    private RandomAccessDataFile(FileAccess fileAccess, long offset, long length) {
        this.fileAccess = fileAccess;
        this.offset = offset;
        this.length = length;
    }

    /**
     * Returns the underlying File.
     *
     * @return the underlying file
     */
    public File getFile() {
        return this.fileAccess.file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new DataInputStream();
    }

    @Override
    public RandomAccessData getSubsection(long offset, long length) {
        if (offset < 0 || length < 0 || offset + length > this.length) {
            throw new IndexOutOfBoundsException();
        }
        return new RandomAccessDataFile(this.fileAccess, this.offset + offset, length);
    }

    @Override
    public byte[] read() throws IOException {
        return read(0, this.length);
    }

    @Override
    public byte[] read(long offset, long length) throws IOException {
        if (offset > this.length) {
            throw new IndexOutOfBoundsException();
        }
        if (offset + length > this.length) {
            throw new EOFException();
        }
        byte[] bytes = new byte[(int) length];
        read(bytes, offset, 0, bytes.length);
        return bytes;
    }

    private int readByte(long position) throws IOException {
        if (position >= this.length) {
            return -1;
        }
        return this.fileAccess.readByte(this.offset + position);
    }

    private int read(byte[] bytes, long position, int offset, int length) throws IOException {
        if (position > this.length) {
            return -1;
        }
        return this.fileAccess.read(bytes, this.offset + position, offset, length);
    }

    @Override
    public long getSize() {
        return this.length;
    }

    public void close() throws IOException {
        this.fileAccess.close();
    }

    /**
     * {@link InputStream} implementation for the {@link RandomAccessDataFile}.
     */
    private class DataInputStream extends InputStream {

        private int position;

        @Override
        public int read() throws IOException {
            int read = RandomAccessDataFile.this.readByte(this.position);
            if (read > -1) {
                moveOn(1);
            }
            return read;
        }

        @Override
        public int read(byte[] b) throws IOException {
            return read(b, 0, (b != null) ? b.length : 0);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (b == null) {
                throw new NullPointerException("Bytes must not be null");
            }
            return doRead(b, off, len);
        }

        /**
         * Perform the actual read.
         *
         * @param b   the bytes to read or {@code null} when reading a single byte
         * @param off the offset of the byte array
         * @param len the length of data to read
         * @return the number of bytes read into {@code b} or the actual read byte if
         * {@code b} is {@code null}. Returns -1 when the end of the stream is reached
         * @throws IOException in case of I/O errors
         */
        int doRead(byte[] b, int off, int len) throws IOException {
            if (len == 0) {
                return 0;
            }
            int cappedLen = cap(len);
            if (cappedLen <= 0) {
                return -1;
            }
            return (int) moveOn(RandomAccessDataFile.this.read(b, this.position, off, cappedLen));
        }

        @Override
        public long skip(long n) throws IOException {
            return (n <= 0) ? 0 : moveOn(cap(n));
        }

        @Override
        public int available() throws IOException {
            return (int) RandomAccessDataFile.this.length - this.position;
        }

        /**
         * Cap the specified value such that it cannot exceed the number of bytes
         * remaining.
         *
         * @param n the value to cap
         * @return the capped value
         */
        private int cap(long n) {
            return (int) Math.min(RandomAccessDataFile.this.length - this.position, n);
        }

        /**
         * Move the stream position forwards the specified amount.
         *
         * @param amount the amount to move
         * @return the amount moved
         */
        private long moveOn(int amount) {
            this.position += amount;
            return amount;
        }

    }

    private static final class FileAccess {

        private final Object monitor = new Object();

        private final File file;

        private RandomAccessFile randomAccessFile;

        private FileAccess(File file) {
            this.file = file;
            openIfNecessary();
        }

        private int read(byte[] bytes, long position, int offset, int length) throws IOException {
            synchronized (this.monitor) {
                openIfNecessary();
                this.randomAccessFile.seek(position);
                return this.randomAccessFile.read(bytes, offset, length);
            }
        }

        private void openIfNecessary() {
            if (this.randomAccessFile == null) {
                try {
                    this.randomAccessFile = new RandomAccessFile(this.file, "r");
                } catch (FileNotFoundException ex) {
                    throw new IllegalArgumentException(
                            String.format("File %s must exist", this.file.getAbsolutePath()));
                }
            }
        }

        private void close() throws IOException {
            synchronized (this.monitor) {
                if (this.randomAccessFile != null) {
                    this.randomAccessFile.close();
                    this.randomAccessFile = null;
                }
            }
        }

        private int readByte(long position) throws IOException {
            synchronized (this.monitor) {
                openIfNecessary();
                this.randomAccessFile.seek(position);
                return this.randomAccessFile.read();
            }
        }

    }

}
