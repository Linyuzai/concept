package $PACKAGE$.basic.rpc.api.user;

/**
 * 用户 api
 */
public interface UserApi {

    /**
     * 通过 id 获得用户信息
     */
    UserRO get(String id);
}
