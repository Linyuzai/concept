package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.generate.IdentifyGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
@Setter
@SuppressWarnings("unchecked")
public abstract class AbstractIdentify<T extends Identify<T>> implements Identify<T> {

    private String id;

    private Function<T, String> idProvider = t -> {
        IdentifyGenerator generator = getContext().get(IdentifyGenerator.class);
        return generator.generateId(t);
    };

    private String key;

    private Function<T, String> keyProvider = t -> {
        IdentifyGenerator generator = getContext().get(IdentifyGenerator.class);
        return generator.generateKey(t);
    };

    private ThingContext context;

    @Override
    public String getId() {
        if (id == null) {
            if (idProvider != null) {
                id = idProvider.apply((T) this);
            }
        }
        return id;
    }

    @Override
    public String getKey() {
        if (key == null) {
            if (keyProvider != null) {
                key = keyProvider.apply((T) this);
            }
        }
        return key;
    }
}
