package com.github.linyuzai.download.core.utils;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.source.Source;

import java.lang.reflect.Array;
import java.util.Collection;

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

    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o.getClass().isArray()) {
            int length = Array.getLength(o);
            if (length == 0) {
                return true;
            }
            for (int i = 0; i < length; i++) {
                Object element = Array.get(o, i);
                if (isEmpty0(element)) {
                    continue;
                }
                return false;
            }
            return true;
        }
        if (o instanceof Collection) {
            if (((Collection<?>) o).isEmpty()) {
                return true;
            }
            for (Object next : (Collection<?>) o) {
                if (isEmpty0(next)) {
                    continue;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    private static boolean isEmpty0(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return ((String) o).isEmpty();
        }
        return false;
    }
}
