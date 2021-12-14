package com.github.linyuzai.download.core.loader;

import java.util.Collection;

public interface LoadExceptionHandler {

    void onLoading(LoadSourceException e);

    void onLoaded(Collection<LoadSourceException> exceptions);
}
