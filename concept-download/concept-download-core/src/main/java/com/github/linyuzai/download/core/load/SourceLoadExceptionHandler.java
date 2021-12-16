package com.github.linyuzai.download.core.load;

import java.util.Collection;

public interface SourceLoadExceptionHandler {

    void onLoading(SourceLoadException e);

    void onLoaded(Collection<SourceLoadException> exceptions);
}
