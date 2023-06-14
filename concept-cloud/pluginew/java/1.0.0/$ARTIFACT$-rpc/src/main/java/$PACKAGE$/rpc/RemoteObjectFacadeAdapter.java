package $PACKAGE$.rpc;

import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.Identifiable;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 远程对象转换适配器
 *
 * @param <T>
 * @param <R>
 */
public interface RemoteObjectFacadeAdapter<T extends DomainObject, R extends Identifiable> {

    /**
     * 领域模型转远程对象
     */
    R do2ro(T object);

    /**
     * 远程对象转领域模型
     */
    T ro2do(R ro);

    default Collection<T> ros2dos(Collection<? extends R> ros) {
        return ros.stream().map(this::ro2do).collect(Collectors.toList());
    }
}
