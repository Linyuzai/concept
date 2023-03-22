package $PACKAGE$.basic.rpc.feign;

/**
 * 如果对响应做了包装需要在外面套一层
 *
 * @param <T> 实际的类型
 */
public class FeignResp<T> {

    private boolean result;

    private String message;

    private T object;
}
