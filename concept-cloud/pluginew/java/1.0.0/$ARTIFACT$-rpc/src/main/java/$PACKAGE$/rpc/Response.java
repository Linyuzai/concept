package $PACKAGE$.rpc;

import lombok.Data;

/**
 * 远程响应
 *
 * @param <T>
 */
@Data
public class Response<T> {

    private Boolean result;

    private String message;

    private T object;
}
