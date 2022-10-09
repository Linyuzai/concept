package com.github.linyuzai.extension.core.concept;

import com.github.linyuzai.extension.core.adapter.ExtensionAdapter;
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

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Getter
@RequiredArgsConstructor
public abstract class AbstractExtensionConcept extends AbstractLifecycle implements ExtensionConcept {

    private final ExtensionRepository extensionRepository;

    private final ExtensionAdapter extensionAdapter;

    private final ExtensionInvokerFactory extensionInvokerFactory;

    private final ExtensionStrategy extensionStrategy;

    private final List<Extension> extensions =
            Collections.synchronizedList(new LinkedList<>());

    private final List<ExtensionFactory> extensionFactories =
            Collections.synchronizedList(new LinkedList<>());

    @Override
    public void register(Extension extension) {
        if (extension == null) {
            throw new NullPointerException("Extension is null");
        }
        if (initialized()) {
            if (!extension.initialized()) {
                extension.setConcept(this);
                extension.initialize();
            }
            if (!extensionRepository.exist(extension)) {
                extensionRepository.add(extension);
            }
        } else {
            extensions.add(extension);
        }
    }

    @Override
    public void register(ExtensionFactory factory) {
        if (factory == null) {
            throw new NullPointerException("ExtensionFactory is null");
        }
        if (initialized()) {
            register(factory.create());
        } else {
            extensionFactories.add(factory);
        }
    }

    @Override
    public <T extends Extension> T getExtension(Predicate<Extension> predicate) {
        List<Extension> extensions = lookupInRepository(predicate);
        if (extensions.isEmpty()) {
            return null;
        } else if (extensions.size() == 1) {
            return (T) extensions.get(0);
        } else {
            throw new ExtensionException(extensions.size() + " extensions found");
        }
    }

    @Override
    public <T extends Extension> T getExtension(String extensionId) {
        return getExtension(it -> it.getId().equals(extensionId));
    }

    @Override
    public <T extends Extension> T getExtension(Class<T> extensionClass) {
        return getExtension(it -> extensionClass.isAssignableFrom(it.getClass()));
    }

    protected List<Extension> lookupInRepository(Predicate<Extension> predicate) {
        List<Extension> found = extensionRepository.stream()
                .filter(predicate)
                .collect(Collectors.toList());
        if (found.isEmpty()) {
            return lookupFromExtensions(predicate);
        } else {
            return found;
        }
    }

    protected List<Extension> lookupFromExtensions(Predicate<Extension> predicate) {
        List<Extension> found = extensions.stream()
                .filter(predicate)
                .collect(Collectors.toList());
        if (found.isEmpty()) {
            return lookupWithFactories(predicate);
        } else {
            extensions.removeAll(found);
            found.forEach(this::register);
            return found;
        }
    }

    protected List<Extension> lookupWithFactories(Predicate<Extension> predicate) {
        if (extensionFactories.isEmpty()) {
            return Collections.emptyList();
        }
        List<Extension> creates = extensionFactories.stream()
                .map(ExtensionFactory::create)
                .collect(Collectors.toList());
        extensionFactories.clear();
        extensions.addAll(creates);
        return lookupFromExtensions(predicate);
    }

    @Override
    public Collection<Extension.ArgumentAndResult> extend(Collection<? extends Extension.Argument> arguments) {
        List<ExtensionInvoker> invokers = new ArrayList<>();
        for (Extension.Argument argument : arguments) {
            Map<Object, Object> configs = argument.getConfigs();
            ExtensionAdapter adapter = (ExtensionAdapter) configs
                    .getOrDefault(ExtensionAdapter.class, extensionAdapter);
            Collection<Extension> extensions = adapter.adapt(argument, extensionRepository);
            if (extensions == null) {
                continue;
            }
            ExtensionInvokerFactory invokerFactory = (ExtensionInvokerFactory) configs
                    .getOrDefault(ExtensionInvokerFactory.class, extensionInvokerFactory);
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
    public void onInitialized() {
        while (!extensions.isEmpty()) {
            Extension extension = extensions.get(0);
            register(extension);
        }
        while (!extensionFactories.isEmpty()) {
            ExtensionFactory factory = extensionFactories.get(0);
            register(factory);
        }
    }

    @Override
    public void onDestroyed() {
        extensionRepository.stream()
                .filter(Lifecycle::initialized)
                .forEach(Lifecycle::destroy);
    }
}
