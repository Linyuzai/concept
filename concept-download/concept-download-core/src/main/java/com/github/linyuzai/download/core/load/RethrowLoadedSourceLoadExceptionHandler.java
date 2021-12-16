package com.github.linyuzai.download.core.load;

import java.util.Collection;

public class RethrowLoadedSourceLoadExceptionHandler implements SourceLoadExceptionHandler {

    @Override
    public void onLoading(SourceLoadException e) {

    }

    @Override
    public void onLoaded(Collection<SourceLoadException> exceptions) {
        throw exceptions.iterator().next();
    }
}
