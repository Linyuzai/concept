package $PACKAGE$.login;

import $PACKAGE$.domain.user.User;

public interface LoginAuthorizer {

    LoginAuthorization authorize(User user);
}
