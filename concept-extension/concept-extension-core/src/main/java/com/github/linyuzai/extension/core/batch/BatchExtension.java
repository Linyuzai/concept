package com.github.linyuzai.extension.core.batch;

import com.github.linyuzai.extension.core.concept.Extension;

public interface BatchExtension extends Extension {

    boolean batchSupport(Argument argument);
}
