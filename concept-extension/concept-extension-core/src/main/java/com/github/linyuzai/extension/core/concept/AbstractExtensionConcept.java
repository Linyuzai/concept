package com.github.linyuzai.extension.core.concept;

import com.github.linyuzai.extension.core.adapter.ExtensionAdapter;
import com.github.linyuzai.extension.core.dependence.DependenceProvider;
import com.github.linyuzai.extension.core.exception.ExtensionException;
import com.github.linyuzai.extension.core.factory.ExtensionFactory;
import com.github.linyuzai.extension.core.invoker.ExtensionInvoker;
import com.github.linyuzai.extension.core.invoker.ExtensionInvokerFactory;
import com.github.linyuzai.extension.core.lifecycle.AbstractLifecycle;
import com.github.linyuzai.extension.core.lifecycle.Lifecycle;
import com.github.linyuzai.extension.core.repository.ExtensionRepository;
import com.github.linyuzai.extension.core.strategy.ExtensionStrategy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class AbstractExtensionConcept extends AbstractLifecycle
        implements ExtensionConcept, DependenceProvider {

    private final ExtensionRepository extensionRepository;

    private final ExtensionAdapter extensionAdapter;

    private final ExtensionInvokerFactory extensionInvokerFactory;

    private final ExtensionStrategy extensionStrategy;

    private final Collection<ExtensionFactory> extensionFactories = new CopyOnWriteArrayList<>();

    @Override
    public void register(Extension extension) {
        extension.setConcept(this);
        extension.setDependenceProvider(this);
        initializeExtensionIfConceptInitialized(extension);
        extensionRepository.add(extension);
    }

    @Override
    public void register(ExtensionFactory factory) {
        extensionFactories.add(factory);
    }

    private void initializeExtensionIfConceptInitialized(Extension extension) {
        if (initialized() && !extension.initialized()) {
            extension.initialize();
        }
    }

    @Override
    public Extension getExtension(String extensionId) {
        return extensionRepository.get(extensionId);
    }

    @Override
    public Collection<Extension.ArgumentAndResult> extend(Collection<? extends Extension.Argument> arguments) {
        List<ExtensionInvoker> invokers = new ArrayList<>();
        for (Extension.Argument argument : arguments) {
            ExtensionAdapter adapter = argument.getConfig(ExtensionAdapter.class,
                    extensionAdapter);
            Collection<Extension> extensions = adapter.adapt(argument, extensionRepository);
            if (extensions == null) {
                continue;
            }

            ExtensionInvokerFactory invokerFactory =
                    argument.getConfig(ExtensionInvokerFactory.class,
                            extensionInvokerFactory);
            for (Extension extension : extensions) {
                ExtensionInvoker invoker = invokerFactory.create(extension, argument);
                invokers.add(invoker);
            }
        }

        if (invokers.isEmpty()) {
            return Collections.emptyList();
        }

        return extensionStrategy.extend(invokers);
    }

    @Override
    public Extension getDependenceExtension(String extensionId) {
        Extension extension = extensionRepository.get(extensionId);
        initializeExtensionIfConceptInitialized(extension);
        return extension;
    }

    @Override
    public Extension getDependenceExtension(Class<? extends Extension> clazz) {
        Collection<Extension> extensions = extensionRepository.stream()
                .filter(it -> clazz.isAssignableFrom(it.getClass()))
                .collect(Collectors.toList());
        if (extensions.isEmpty()) {
            return null;
        } else if (extensions.size() == 1) {
            return extensions.iterator().next();
        } else {
            throw new ExtensionException(extensions.size() + " extensions found");
        }
    }

    @Override
    public void onInitialize() {
        extensionRepository.stream().sorted().forEach(this::initializeExtensionIfConceptInitialized);
        extensionFactories.stream().map(ExtensionFactory::create).forEach(this::register);
    }

    @Override
    public void onDestroy() {
        extensionRepository.stream().filter(Lifecycle::initialized).forEach(Lifecycle::destroy);
    }
}
