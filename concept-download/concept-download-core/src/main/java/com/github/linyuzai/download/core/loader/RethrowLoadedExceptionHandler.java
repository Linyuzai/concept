package com.github.linyuzai.download.core.loader;

import java.util.Collection;

public class RethrowLoadedExceptionHandler implements LoadExceptionHandler {

    @Override
    public void onLoading(LoadSourceException e) {

    }

    @Override
    public void onLoaded(Collection<LoadSourceException> exceptions) {
        throw exceptions.iterator().next();
    }
}
