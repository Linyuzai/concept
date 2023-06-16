package $PACKAGE$.rpc.feign.sample;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.sample.SampleRepository;
import $PACKAGE$.domain.sample.Samples;
import $PACKAGE$.rpc.Response;
import $PACKAGE$.rpc.sample.RPCSampleFacadeAdapter;
import $PACKAGE$.rpc.sample.SampleRO;
import com.github.linyuzai.domain.core.QueryDomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 基于 Feign 的示例存储
 */
@Component
public class FeignSampleRepository extends QueryDomainRepository<Sample, Samples, SampleRO> implements SampleRepository {

    @Autowired
    private RPCSampleFacadeAdapter rpcSampleFacadeAdapter;

    @Autowired
    private SampleFeignClient sampleFeignClient;

    @Override
    public SampleRO do2po(Sample sample) {
        return rpcSampleFacadeAdapter.do2ro(sample);
    }

    @Override
    public Sample po2do(SampleRO ro) {
        return rpcSampleFacadeAdapter.ro2do(ro);
    }

    @Override
    public Collection<Sample> pos2dos(Collection<? extends SampleRO> pos) {
        return rpcSampleFacadeAdapter.ros2dos(pos);
    }

    @Override
    protected SampleRO doGet(String id) {
        Response<SampleRO> response = sampleFeignClient.get(id);
        if (response.getResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }

    @Override
    protected Collection<SampleRO> doSelect(Collection<String> ids) {
        Response<List<SampleRO>> response = sampleFeignClient.list(ids);
        if (response.getResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }

    @Override
    protected SampleRO doGet(Conditions conditions) {
        Response<SampleRO> response = sampleFeignClient.get(conditions);
        if (response.getResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }

    @Override
    protected Collection<SampleRO> doSelect(Conditions conditions) {
        Response<List<SampleRO>> response = sampleFeignClient.list(conditions);
        if (response.getResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }
}