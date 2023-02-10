package _PACKAGE_.domain._DOMAIN_.event;

import _PACKAGE_.domain._DOMAIN_._UPPER_;
import _PACKAGE_.domain.user._UPPER_User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class _UPPER_CreatedEvent {

    private final _UPPER_ _LOWER_;

    private final _UPPER_User user;
}
