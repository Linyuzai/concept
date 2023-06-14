package $PACKAGE$.rpc.feign.sample;

import $PACKAGE$.rpc.Response;
import $PACKAGE$.rpc.sample.SampleRO;
import com.github.linyuzai.domain.core.condition.Conditions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

/**
 * 示例 Feign 客户端
 */
@FeignClient(name = "sample")
public interface SampleFeignClient {

    /**
     * 根据 id 获得示例信息
     */
    @GetMapping("feign/sample/{id}")
    Response<SampleRO> get(@PathVariable String id);

    /**
     * 根据条件获得示例信息
     */
    @PostMapping("feign/sample/conditions")
    Response<SampleRO> get(@RequestBody Conditions conditions);

    /**
     * 根据 id 列表获得示例信息列表
     */
    @PostMapping("/feign/sample/list/ids")
    Response<List<SampleRO>> list(@RequestBody Collection<String> ids);

    /**
     * 根据条件获得用户示例列表
     */
    @PostMapping("feign/sample/list/conditions")
    Response<List<SampleRO>> list(@RequestBody Conditions conditions);
}