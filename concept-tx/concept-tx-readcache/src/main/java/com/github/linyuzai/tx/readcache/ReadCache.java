package com.github.linyuzai.tx.readcache;

public interface ReadCache {

    enum Mode {
        READ, WRITE
    }

    void setMode(Mode mode);

    Mode getMode();
}
