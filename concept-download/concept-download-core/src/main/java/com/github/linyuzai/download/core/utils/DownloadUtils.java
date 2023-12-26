package com.github.linyuzai.download.core.utils;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.source.Source;

/**
 * 工具类。
 */
public class DownloadUtils {

    /**
     * 将大小做格式化处理。
     *
     * @param size 大小
     * @return 格式化后的字符串
     */
    public static String format(long size) {
        if (size >= 1024) {
            double k = size / 1024.0;
            if (k >= 1024) {
                double m = k / 1024;
                return String.format("%.2f", m) + "M";
            } else {
                return String.format("%.2f", k) + "K";
            }
        } else {
            return size + "B";
        }
    }

    /**
     * 处理压缩前后的对比及压缩率。
     *
     * @param source      {@link Source}
     * @param compression {@link Compression}
     * @return 压缩前后的对比及压缩率
     */
    public static String formatCompressedSize(Source source, Compression compression) {
        Long sl = source.getLength();
        Long cl = compression.getLength();
        StringBuilder builder = new StringBuilder();
        if (sl == null) {
            builder.append("?");
        } else {
            builder.append(DownloadUtils.format(source.getLength()));
        }
        builder.append(" => ");
        if (cl == null) {
            builder.append("?");
        } else {
            builder.append(DownloadUtils.format(compression.getLength()));
        }

        if (sl != null && cl != null) {
            double r = cl.doubleValue() / sl.doubleValue() * 100.0;
            builder.append(" (").append(String.format("%.2f", r)).append("%)");
        }
        return builder.toString();
    }
}
