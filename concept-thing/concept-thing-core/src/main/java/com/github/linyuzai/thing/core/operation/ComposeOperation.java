package com.github.linyuzai.thing.core.operation;

import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class ComposeOperation extends AbstractOperation {

    private final Collection<Operation> operations = new ArrayList<>();

    @Override
    public Operation merge(Operation operation) {
        if (operation instanceof ComposeOperation) {
            operations.addAll(((ComposeOperation) operation).getOperations());
        } else {
            operations.add(operation);
        }
        return this;
    }
}
