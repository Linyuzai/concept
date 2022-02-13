package com.github.linyuzai.download.core.web;

import lombok.*;
import org.springframework.util.StringUtils;

/**
 * 指定资源的范围，对应 'Range' 请求头。
 */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Range {

    /**
     * 开始位置
     */
    long start;

    /**
     * 结束位置
     */
    long end;

    /**
     * 长度
     */
    @Setter
    long length;

    /**
     * 范围是否支持。
     *
     * @return 如果支持则返回 true
     */
    public boolean isSupport() {
        if (hasStart() && hasEnd()) {
            return start >= end;
        } else {
            return true;
        }
    }

    /**
     * 是否有开始位置。
     *
     * @return 如果有开始位置则返回 true
     */
    public boolean hasStart() {
        return start >= 0;
    }

    /**
     * 是否有结束位置。
     *
     * @return 如果有结束位置则返回 true
     */
    public boolean hasEnd() {
        return end >= 0;
    }

    public static Range of(long start, long end) {
        Range range = new Range();
        range.start = start;
        range.end = end;
        return range;
    }

    /**
     * 根据 'Range' 请求头解析 {@link Range}。
     *
     * @param header 'Range' 请求头
     * @return 解析得到的 {@link Range} 或 null
     */
    public static Range header(String header) {
        if (!StringUtils.hasText(header)) {
            return null;
        }
        String[] split = header.split("=");
        if (split.length == 2 && "bytes".equalsIgnoreCase(split[0])) {
            if (split[1].contains(",")) {
                //TODO
            }
            String[] ranges = split[1].split("-", -1);
            if (ranges.length == 2) {
                long start;
                if (ranges[0].isEmpty()) {
                    start = -1;
                } else {
                    start = Long.parseLong(ranges[0]);
                }
                long end;
                if (ranges[1].isEmpty()) {
                    end = -1;
                } else {
                    end = Long.parseLong(ranges[1]);
                }
                return of(start, end);
            }
        }
        return null;
    }
}
