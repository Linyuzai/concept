package $PACKAGE$.rpc.sample;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.sample.SampleImpl;
import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.Users;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 示例领域模型和示例远程对象转换适配器实现
 */
@Component
public class RPCSampleFacadeAdapterImpl implements RPCSampleFacadeAdapter {

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public SampleRO do2ro(Sample sample) {
        SampleRO ro = new SampleRO();
        ro.setId(sample.getId());
        ro.setSample(sample.getSample());
        ro.setUserId(sample.getUser().getId());
        ro.setUserIds(sample.getUsers()
                .list()
                .stream()
                .map(Identifiable::getId)
                .collect(Collectors.toList()));
        return ro;
    }

    @Override
    public Sample ro2do(SampleRO ro) {
        return new SampleImpl.Builder()
                .id(ro.getId())
                .sample(ro.getSample())
                .user(factory.createObject(User.class, ro.getUserId()))
                .users(factory.createCollection(Users.class, ro.getUserIds()))
                .build(validator);
    }

    @Override
    public Collection<Sample> ros2dos(Collection<? extends SampleRO> ros) {
        List<Sample> samples = new ArrayList<>();

        Set<String> sampleIds = ros.stream()
                .map(SampleRO::getId).collect(Collectors.toSet());

        Map<String, String> userIdMap = ros.stream()
                .collect(Collectors.toMap(SampleRO::getId, SampleRO::getUserId));

        Map<String, List<String>> userIdsMap = ros.stream()
                .collect(Collectors.toMap(SampleRO::getId, SampleRO::getUserIds));

        Map<String, User> userMap = factory.createObject(Users.class, sampleIds, ids -> userIdMap);
        Map<String, Users> usersMap = factory.createCollection(Users.class, sampleIds, ids -> userIdsMap);

        for (SampleRO ro : ros) {
            Sample sample = new SampleImpl.Builder()
                    .id(ro.getId())
                    .sample(ro.getSample())
                    .user(userMap.get(ro.getId()))
                    .users(usersMap.get(ro.getId()))
                    .build(validator);
            samples.add(sample);
        }

        return samples;
    }
}