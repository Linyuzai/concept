package com.github.linyuzai.extension.core.adapter;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.repository.ExtensionRepository;

import java.util.Collection;

public interface ExtensionAdapter {

    Collection<Extension> adapt(Extension.Argument argument, ExtensionRepository repository);
}
