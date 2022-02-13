package com.github.linyuzai.download.core.utils;

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
}
