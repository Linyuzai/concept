package com.github.linyuzai.plugin.jar.extension.file;

import java.io.IOException;

/**
 * A ZIP File "End of central directory record" (EOCD).
 *
 * @see <a href="https://en.wikipedia.org/wiki/Zip_%28file_format%29">Zip File Format</a>
 */
public class CentralDirectoryEndRecord {

    private static final int MINIMUM_SIZE = 22;

    private static final int MAXIMUM_COMMENT_LENGTH = 0xFFFF;

    private static final int MAXIMUM_SIZE = MINIMUM_SIZE + MAXIMUM_COMMENT_LENGTH;

    private static final int SIGNATURE = 0x06054b50;

    private static final int COMMENT_LENGTH_OFFSET = 20;

    private static final int READ_BLOCK_SIZE = 256;

    private final Zip64End zip64End;

    private byte[] block;

    private int offset;

    private int size;

    /**
     * Create a new {@link CentralDirectoryEndRecord} instance from the specified
     * {@link RandomAccessData}, searching backwards from the end until a valid block is
     * located.
     *
     * @param data the source data
     * @throws IOException in case of I/O errors
     */
    public CentralDirectoryEndRecord(RandomAccessData data) throws IOException {
        this.block = createBlockFromEndOfData(data, READ_BLOCK_SIZE);
        this.size = MINIMUM_SIZE;
        this.offset = this.block.length - this.size;
        while (!isValid()) {
            this.size++;
            if (this.size > this.block.length) {
                if (this.size >= MAXIMUM_SIZE || this.size > data.getSize()) {
                    throw new IOException(
                            "Unable to find ZIP central directory records after reading " + this.size + " bytes");
                }
                this.block = createBlockFromEndOfData(data, this.size + READ_BLOCK_SIZE);
            }
            this.offset = this.block.length - this.size;
        }
        long startOfCentralDirectoryEndRecord = data.getSize() - this.size;
        Zip64Locator zip64Locator = Zip64Locator.find(data, startOfCentralDirectoryEndRecord);
        this.zip64End = (zip64Locator != null) ? new Zip64End(data, zip64Locator) : null;
    }

    private byte[] createBlockFromEndOfData(RandomAccessData data, int size) throws IOException {
        int length = (int) Math.min(data.getSize(), size);
        return data.read(data.getSize() - length, length);
    }

    private boolean isValid() {
        if (this.block.length < MINIMUM_SIZE || Bytes.littleEndianValue(this.block, this.offset + 0, 4) != SIGNATURE) {
            return false;
        }
        // Total size must be the structure size + comment
        long commentLength = Bytes.littleEndianValue(this.block, this.offset + COMMENT_LENGTH_OFFSET, 2);
        return this.size == MINIMUM_SIZE + commentLength;
    }

    /**
     * Returns the location in the data that the archive actually starts. For most files
     * the archive data will start at 0, however, it is possible to have prefixed bytes
     * (often used for startup scripts) at the beginning of the data.
     *
     * @param data the source data
     * @return the offset within the data where the archive begins
     */
    public long getStartOfArchive(RandomAccessData data) {
        long length = Bytes.littleEndianValue(this.block, this.offset + 12, 4);
        long specifiedOffset = (this.zip64End != null) ? this.zip64End.centralDirectoryOffset
                : Bytes.littleEndianValue(this.block, this.offset + 16, 4);
        long zip64EndSize = (this.zip64End != null) ? this.zip64End.getSize() : 0L;
        int zip64LocSize = (this.zip64End != null) ? Zip64Locator.ZIP64_LOCSIZE : 0;
        long actualOffset = data.getSize() - this.size - length - zip64EndSize - zip64LocSize;
        return actualOffset - specifiedOffset;
    }

    /**
     * Return the bytes of the "Central directory" based on the offset indicated in this
     * record.
     *
     * @param data the source data
     * @return the central directory data
     */
    public RandomAccessData getCentralDirectory(RandomAccessData data) {
        if (this.zip64End != null) {
            return this.zip64End.getCentralDirectory(data);
        }
        long offset = Bytes.littleEndianValue(this.block, this.offset + 16, 4);
        long length = Bytes.littleEndianValue(this.block, this.offset + 12, 4);
        return data.getSubsection(offset, length);
    }

    /**
     * Return the number of ZIP entries in the file.
     *
     * @return the number of records in the zip
     */
    public int getNumberOfRecords() {
        if (this.zip64End != null) {
            return this.zip64End.getNumberOfRecords();
        }
        long numberOfRecords = Bytes.littleEndianValue(this.block, this.offset + 10, 2);
        return (int) numberOfRecords;
    }

    public String getComment() {
        int commentLength = (int) Bytes.littleEndianValue(this.block, this.offset + COMMENT_LENGTH_OFFSET, 2);
        AsciiBytes comment = new AsciiBytes(this.block, this.offset + COMMENT_LENGTH_OFFSET + 2, commentLength);
        return comment.toString();
    }

    public boolean isZip64() {
        return this.zip64End != null;
    }

    /**
     * A Zip64 end of central directory record.
     *
     * @see <a href="https://pkware.cachefly.net/webdocs/casestudies/APPNOTE.TXT">Chapter
     * 4.3.14 of Zip64 specification</a>
     */
    public static final class Zip64End {

        private static final int ZIP64_ENDTOT = 32; // total number of entries

        private static final int ZIP64_ENDSIZ = 40; // central directory size in bytes

        private static final int ZIP64_ENDOFF = 48; // offset of first CEN header

        private final Zip64Locator locator;

        private final long centralDirectoryOffset;

        private final long centralDirectoryLength;

        private final int numberOfRecords;

        public Zip64End(RandomAccessData data, Zip64Locator locator) throws IOException {
            this.locator = locator;
            byte[] block = data.read(locator.getZip64EndOffset(), 56);
            this.centralDirectoryOffset = Bytes.littleEndianValue(block, ZIP64_ENDOFF, 8);
            this.centralDirectoryLength = Bytes.littleEndianValue(block, ZIP64_ENDSIZ, 8);
            this.numberOfRecords = (int) Bytes.littleEndianValue(block, ZIP64_ENDTOT, 8);
        }

        /**
         * Return the size of this zip 64 end of central directory record.
         *
         * @return size of this zip 64 end of central directory record
         */
        private long getSize() {
            return this.locator.getZip64EndSize();
        }

        /**
         * Return the bytes of the "Central directory" based on the offset indicated in
         * this record.
         *
         * @param data the source data
         * @return the central directory data
         */
        private RandomAccessData getCentralDirectory(RandomAccessData data) {
            return data.getSubsection(this.centralDirectoryOffset, this.centralDirectoryLength);
        }

        /**
         * Return the number of entries in the zip64 archive.
         *
         * @return the number of records in the zip
         */
        private int getNumberOfRecords() {
            return this.numberOfRecords;
        }

    }

    /**
     * A Zip64 end of central directory locator.
     *
     * @see <a href="https://pkware.cachefly.net/webdocs/casestudies/APPNOTE.TXT">Chapter
     * 4.3.15 of Zip64 specification</a>
     */
    private static final class Zip64Locator {

        static final int SIGNATURE = 0x07064b50;

        static final int ZIP64_LOCSIZE = 20; // locator size

        static final int ZIP64_LOCOFF = 8; // offset of zip64 end

        private final long zip64EndOffset;

        private final long offset;

        private Zip64Locator(long offset, byte[] block) {
            this.offset = offset;
            this.zip64EndOffset = Bytes.littleEndianValue(block, ZIP64_LOCOFF, 8);
        }

        /**
         * Return the size of the zip 64 end record located by this zip64 end locator.
         *
         * @return size of the zip 64 end record located by this zip64 end locator
         */
        private long getZip64EndSize() {
            return this.offset - this.zip64EndOffset;
        }

        /**
         * Return the offset to locate {@link Zip64End}.
         *
         * @return offset of the Zip64 end of central directory record
         */
        private long getZip64EndOffset() {
            return this.zip64EndOffset;
        }

        private static Zip64Locator find(RandomAccessData data, long centralDirectoryEndOffset) throws IOException {
            long offset = centralDirectoryEndOffset - ZIP64_LOCSIZE;
            if (offset >= 0) {
                byte[] block = data.read(offset, ZIP64_LOCSIZE);
                if (Bytes.littleEndianValue(block, 0, 4) == SIGNATURE) {
                    return new Zip64Locator(offset, block);
                }
            }
            return null;
        }

    }

}
