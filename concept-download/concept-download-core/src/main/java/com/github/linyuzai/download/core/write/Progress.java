package com.github.linyuzai.download.core.write;

import lombok.Getter;

@Getter
public class Progress {

    private final Long total;

    private long current;

    public Progress(Long total) {
        this.total = total;
    }

    public boolean hasTotal() {
        return total != null && total > 0;
    }

    public void update(long increase) {
        if (increase <= 0) {
            return;
        }
        current += increase;
    }

    public Progress copy() {
        Progress progress = new Progress(total);
        progress.current = current;
        return progress;
    }
}
