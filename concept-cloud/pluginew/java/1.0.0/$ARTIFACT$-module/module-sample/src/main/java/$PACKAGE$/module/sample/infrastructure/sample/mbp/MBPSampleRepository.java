package $PACKAGE$.module.sample.infrastructure.sample.mbp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.sample.SampleImpl;
import $PACKAGE$.domain.sample.SampleRepository;
import $PACKAGE$.domain.sample.Samples;
import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.Users;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.mbp.MBPDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于 MBP 的示例存储实现
 */
@Repository
public class MBPSampleRepository extends MBPDomainRepository<Sample, Samples, SamplePO> implements SampleRepository {

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
        //根据 userId 生成一个 User，该方法不会立即查询数据库，而会等到调用 User 的方法获取数据时才会触发查询
        User user = po.getUserId() == null ? null : factory.createObject(User.class, po.getUserId());

        //获得和 sampleId 关联的数据对象
        List<SampleUserPO> sampleUsers = sampleUserMapper
                .selectList(Wrappers.<SampleUserPO>lambdaQuery()
                        .eq(SampleUserPO::getSampleId, po.getId()));
        //处理获得 sampleId 关联的 userId 集合
        Set<String> userIds = sampleUsers.stream()
                .map(SampleUserPO::getUserId)
                .collect(Collectors.toSet());
        //根据 userId 集合生成 Users，该方法不会立即查询数据库，而会等到调用 Users 的方法获取数据时才会触发查询
        Users users = factory.createCollection(Users.class, userIds);

        return new SampleImpl.Builder()
                .id(po.getId())
                .sample(po.getSample())
                .user(user)
                .users(users)
                .build(validator);
    }

    /**
     * 查询列表或分页查询时会多次调用 {@link #po2do(SamplePO)} 方法导致多次查询数据库
     * <p>
     * 可重写该方法实现只查询一次数据库
     */
    @Override
    public Collection<Sample> pos2dos(Collection<? extends SamplePO> pos) {
        List<Sample> samples = new ArrayList<>();

        //获得 Sample id 集合
        Set<String> sampleIds = pos.stream()
                .map(SamplePO::getId)
                .collect(Collectors.toSet());

        //获得 sample id 和 User 的 Map，该方法不会立即查询数据库，而会等到调用 Users 的方法获取数据时才会触发查询
        Map<String, User> userMap = factory.createObject(Users.class, sampleIds, ids -> {
            //Sample id 和 userId 的关联 Map
            return pos.stream().collect(Collectors.toMap(SamplePO::getId, SamplePO::getUserId));
        });

        //获得 sample id 和 Users 的 Map，该方法不会立即查询数据库，而会等到调用 Users 的方法获取数据时才会触发查询
        Map<String, Users> usersMap = factory.createCollection(Users.class, sampleIds, ids -> {
            //根据 sampleId 集合获得所有的关联对象
            List<SampleUserPO> sampleUserList = sampleUserMapper
                    .selectList(Wrappers.<SampleUserPO>lambdaQuery()
                            .in(SampleUserPO::getSampleId, sampleIds));
            //处理获得 sampleId 和 userIds 的关联 Map
            return sampleUserList.stream()
                    .collect(Collectors.groupingBy(SampleUserPO::getSampleId,
                            Collectors.mapping(SampleUserPO::getUserId, Collectors.toSet())));
        });

        for (SamplePO po : pos) {
            Sample sample = new SampleImpl.Builder()
                    .id(po.getId())
                    .sample(po.getSample())
                    .user(userMap.get(po.getId()))
                    .users(usersMap.get(po.getId()))
                    .build(validator);
            samples.add(sample);
        }
        return samples;
    }

    /**
     * 如果有关联表
     * <p>
     * 可以重写该方法添加关联关系
     */
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

    /**
     * 如果有关联表
     * <p>
     * 可以重写该方法删除关联关系
     */
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
