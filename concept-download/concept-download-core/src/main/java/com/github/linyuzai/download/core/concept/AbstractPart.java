package com.github.linyuzai.download.core.concept;

import java.io.InputStream;

public abstract class AbstractPart implements Part {

    protected InputStream inputStream;

    @Override
    public InputStream getInputStream() {
        if (inputStream == null) {
            inputStream = openInputStream();
        }
        return inputStream;
    }

    public abstract InputStream openInputStream();

    @Override
    public void release() {
        getChildren().forEach(Part::release);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Throwable ignore) {
            }
            inputStream = null;
        }
    }
}
