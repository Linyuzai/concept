package com.github.linyuzai.event.core.exchange;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ComposeEventExchange implements EventExchange {

    private Collection<EventExchange> exchanges;

    public ComposeEventExchange(EventExchange... exchanges) {
        this(Arrays.asList(exchanges));
    }

    @Override
    public Collection<EventEndpoint> exchange(EventConcept concept) {
        return exchanges.stream()
                .flatMap(it -> it.exchange(concept).stream())
                .collect(Collectors.toSet());
    }
}
