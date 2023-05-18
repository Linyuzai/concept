package $PACKAGE$.domain.sample.event;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 更新事件
 */
@Getter
@RequiredArgsConstructor
public class SampleUpdatedEvent {

    private final Sample newSample;

    private final Sample oldSample;

    private final User user;
}
