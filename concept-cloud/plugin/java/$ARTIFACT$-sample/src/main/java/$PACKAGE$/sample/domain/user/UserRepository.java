package $PACKAGE$.sample.domain.user;

/**
 * 方便提供多种实现
 */
public interface UserRepository {

    User get(String id);
}
