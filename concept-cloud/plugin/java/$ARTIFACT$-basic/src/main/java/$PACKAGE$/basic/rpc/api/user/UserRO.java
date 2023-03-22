package $PACKAGE$.basic.rpc.api.user;

/**
 * RO remote object
 * <p>
 * 远程用户对象
 */
public class UserRO {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
