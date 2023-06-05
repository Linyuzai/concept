package $PACKAGE$.module.sample.infrastructure.sample.mbp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import $PACKAGE$.basic.boot.mbp.MBPBaseRepository;
import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.sample.SampleImpl;
import $PACKAGE$.domain.sample.SampleRepository;
import $PACKAGE$.domain.sample.Samples;
import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.Users;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基于 MBP 的示例存储实现
 */
@Repository
public class MBPSampleRepository extends MBPBaseRepository<Sample, Samples, SamplePO> implements SampleRepository {

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private SampleUserMapper sampleUserMapper;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public SamplePO do2po(Sample sample) {
        SamplePO po = new SamplePO();
        po.setId(sample.getId());
        if (sample.getUser() != null) {
            po.setUserId(sample.getUser().getId());
        }
        return po;
    }

    @Override
    public Sample po2do(SamplePO po) {
        User user = po.getUserId() == null ? null : factory.createObject(User.class, po.getUserId());
        List<SampleUserPO> sampleUsers = sampleUserMapper
                .selectList(Wrappers.<SampleUserPO>lambdaQuery()
                        .eq(SampleUserPO::getId, po.getId()));
        Set<String> userIds = sampleUsers.stream()
                .map(SampleUserPO::getUserId)
                .collect(Collectors.toSet());
        Users users = factory.createCollection(Users.class,
                new LambdaConditions().in(User::getId, userIds));
        return new SampleImpl.Builder()
                .id(po.getId())
                .user(user)
                .users(users)
                .build(validator);
    }

    @Transactional
    @Override
    public void create(Sample sample) {
        super.create(sample);
        sample.getUsers().list().stream().map(it -> {
            SampleUserPO sup = new SampleUserPO();
            sup.setSampleId(sample.getId());
            sup.setUserId(it.getId());
            return sup;
        }).forEach(sampleUserMapper::insert);

    }

    @Transactional
    @Override
    public void delete(Sample sample) {
        super.delete(sample);
        sampleUserMapper.delete(Wrappers.<SampleUserPO>lambdaQuery()
                .eq(SampleUserPO::getSampleId, sample.getId()));
    }

    @Override
    public BaseMapper<SamplePO> getBaseMapper() {
        return sampleMapper;
    }
}
