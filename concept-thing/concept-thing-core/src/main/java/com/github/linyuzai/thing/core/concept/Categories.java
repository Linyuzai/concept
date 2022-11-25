package com.github.linyuzai.thing.core.concept;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public interface Categories extends Container<Category> {

    class Impl implements Categories {

        private Map<String, Category> categoryMap;

        @Override
        public Category get(String id) {
            Category category = categoryMap.get(id);
            if (category == null) {
                for (Category value : categoryMap.values()) {
                    Category parent = value.getCategories().get(id);
                    if (parent != null) {
                        return parent;
                    }
                }
            }
            return category;
        }

        @Override
        public Optional<Category> optional(String id) {
            return Optional.empty();
        }

        @Override
        public List<Category> list() {
            return null;
        }

        @Override
        public Stream<Category> stream() {
            return null;
        }
    }
}
