package _PACKAGE_.domain._DOMAIN_;

import _PACKAGE_.domain._DOMAIN_.event._UPPER_CreatedEvent;
import _PACKAGE_.domain._DOMAIN_.event._UPPER_DeletedEvent;
import _PACKAGE_.domain._DOMAIN_.event._UPPER_UpdatedEvent;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_CreateCommand;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_DeleteCommand;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_UpdateCommand;
import _PACKAGE_.domain.user._UPPER_User;
import com.github.linyuzai.domain.core.DomainEventPublisher;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class _UPPER_Service {

    @Autowired
    protected _UPPER_Repository _LOWER_Repository;

    @Autowired
    protected _UPPER_FacadeAdapter _LOWER_FacadeAdapter;

    @Autowired
    protected DomainEventPublisher eventPublisher;

    public void create(_UPPER_CreateCommand create, _UPPER_User user) {
        _UPPER_ _LOWER_ = _LOWER_FacadeAdapter.from(create);
        beforeCreate(_LOWER_);
        _LOWER_Repository.create(_LOWER_);
        eventPublisher.publish(new _UPPER_CreatedEvent(_LOWER_, user));
    }

    protected void beforeCreate(_UPPER_ _LOWER_) {

    }

    public void update(_UPPER_UpdateCommand update, _UPPER_User user) {
        _UPPER_ _LOWER_ = _LOWER_Repository.get(update.getId());
        if (_LOWER_ == null) {
            throw new DomainNotFoundException(_UPPER_.class, update.getId());
        }
        _UPPER_ new_UPPER_ = _LOWER_FacadeAdapter.from(update, _LOWER_);
        beforeUpdate(new_UPPER_, _LOWER_);
        _LOWER_Repository.update(new_UPPER_);
        eventPublisher.publish(new _UPPER_UpdatedEvent(_LOWER_, new_UPPER_, user));
    }

    protected void beforeUpdate(_UPPER_ new_UPPER_, _UPPER_ old_UPPER_) {

    }

    public void delete(_UPPER_DeleteCommand delete, _UPPER_User user) {
        _UPPER_ _LOWER_ = _LOWER_Repository.get(delete.getId());
        if (_LOWER_ == null) {
            throw new DomainNotFoundException(_UPPER_.class, delete.getId());
        }
        beforeDelete(_LOWER_);
        _LOWER_Repository.delete(_LOWER_);
        eventPublisher.publish(new _UPPER_DeletedEvent(_LOWER_, user));
    }

    protected void beforeDelete(_UPPER_ _LOWER_) {

    }
}
