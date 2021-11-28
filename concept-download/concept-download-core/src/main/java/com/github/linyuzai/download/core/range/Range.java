package com.github.linyuzai.download.core.range;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Range {

    long start;

    long end;

    public boolean hasStart() {
        return start > 0;
    }

    public boolean hasEnd() {
        return end > 0;
    }

    public static Range start(long start) {
        if (start < 0) {
            throw new IllegalArgumentException("start < 0");
        }
        return new Range(start, -1);
    }

    public static Range end(long end) {
        if (end < 0) {
            throw new IllegalArgumentException("end < 0");
        }
        return new Range(-1, end);
    }

    public static Range of(long start, long end) {
        if (start < 0 && end < 0) {
            throw new IllegalArgumentException("start < 0 & end < 0");
        }
        if (end >= 0 && start > end){
            throw new IllegalArgumentException("start > end");
        }
        return new Range(start, end);
    }
}
