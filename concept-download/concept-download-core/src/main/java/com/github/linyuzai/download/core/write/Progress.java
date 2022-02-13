package com.github.linyuzai.download.core.write;

import lombok.Getter;

/**
 * 进度类。
 */
@Getter
public class Progress {

    /**
     * 总大小，可能为 null
     */
    private final Long total;

    /**
     * 当前进度
     */
    private long current;

    public Progress(Long total) {
        this.total = total;
    }

    /**
     * 总大小不为 null 并且大于 0
     *
     * @return 如果总大小存在则返回 true
     */
    public boolean hasTotal() {
        return total != null && total > 0;
    }

    /**
     * 更新进度。
     *
     * @param increase 增长大小
     */
    public void update(long increase) {
        if (increase <= 0) {
            return;
        }
        current += increase;
    }

    /**
     * 冻结当前进度返回一个新的实例。
     *
     * @return 拥有当前进度的新的实例
     */
    public Progress freeze() {
        Progress progress = new Progress(total);
        progress.current = current;
        return progress;
    }
}
