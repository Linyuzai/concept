package $PACKAGE$.domain.sample.event;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 删除事件
 */
@Getter
@RequiredArgsConstructor
public class SampleCreatedEvent {

    private final Sample sample;

    private final User user;
}
