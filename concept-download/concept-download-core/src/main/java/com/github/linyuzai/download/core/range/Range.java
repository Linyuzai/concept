package com.github.linyuzai.download.core.range;

import lombok.*;
import org.springframework.util.StringUtils;

/**
 * 指定资源的范围 / Specify the range of the resource
 */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Range {

    long start;

    long end;

    @Setter
    long length;

    public boolean isSupport() {
        return true;
    }

    public boolean hasStart() {
        return start >= 0;
    }

    public boolean hasEnd() {
        return end >= 0;
    }

    public static Range of(long start, long end) {
        Range range = new Range();
        range.start = start;
        range.end = end;
        return range;
    }

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
