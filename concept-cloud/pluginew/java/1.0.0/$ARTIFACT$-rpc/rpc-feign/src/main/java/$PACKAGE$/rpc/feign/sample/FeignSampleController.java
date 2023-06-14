package $PACKAGE$.rpc.feign.sample;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.sample.SampleRepository;
import $PACKAGE$.domain.sample.Samples;
import $PACKAGE$.rpc.sample.RPCSampleFacadeAdapter;
import $PACKAGE$.rpc.sample.SampleRO;
import $PACKAGE$.token.Token;
import com.github.linyuzai.domain.core.condition.Conditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供给 Feign 调用的示例接口
 */
@RestController
@RequestMapping("feign/sample")
public class FeignSampleController {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private RPCSampleFacadeAdapter rpcSampleFacadeAdapter;

    /**
     * 根据用户 id 获得示例信息
     */
    @Token(required = false)
    @GetMapping("{id}")
    public SampleRO get(@PathVariable String id) {
        Sample sample = sampleRepository.get(id);
        if (sample == null) {
            return null;
        }
        return rpcSampleFacadeAdapter.do2ro(sample);
    }

    /**
     * 根据用户条件获得示例信息
     * <p>
     * 避免参数过长用 POST
     */
    @Token(required = false)
    @PostMapping("conditions")
    public SampleRO get(@RequestBody Conditions conditions) {
        Sample sample = sampleRepository.get(conditions);
        if (sample == null) {
            return null;
        }
        return rpcSampleFacadeAdapter.do2ro(sample);
    }

    /**
     * 根据 id 列表获得示例信息列表
     * <p>
     * 避免参数过长用 POST
     */
    @Token(required = false)
    @PostMapping("list/ids")
    public List<SampleRO> list(@RequestBody Collection<String> ids) {
        Samples samples = sampleRepository.select(ids);
        return samples.stream().map(rpcSampleFacadeAdapter::do2ro).collect(Collectors.toList());
    }

    /**
     * 根据条件获得示例信息列表
     * <p>
     * 避免参数过长用 POST
     */
    @Token(required = false)
    @PostMapping("list/conditions")
    public List<SampleRO> conditions(@RequestBody Conditions conditions) {
        Samples samples = sampleRepository.select(conditions);
        return samples.stream().map(rpcSampleFacadeAdapter::do2ro).collect(Collectors.toList());
    }
}