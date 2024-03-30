package com.github.linyuzai.download.autoconfigure.web.mock;

import com.github.linyuzai.download.core.web.DownloadRequest;

public class MockDownloadRequest implements DownloadRequest {

    @Override
    public String getHeader(String name) {
        return null;
    }
}
