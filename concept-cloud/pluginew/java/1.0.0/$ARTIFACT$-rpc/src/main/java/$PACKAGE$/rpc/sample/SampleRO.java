package $PACKAGE$.rpc.sample;

import com.github.linyuzai.domain.core.Identifiable;
import lombok.Data;

import java.util.List;

/**
 * 示例远程对象 remote object
 */
@Data
public class SampleRO implements Identifiable {

    private String id;

    private String sample;

    private String userId;

    private List<String> userIds;
}
