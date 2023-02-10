package _PACKAGE_.domain._DOMAIN_.mbp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import _PACKAGE_.domain._DOMAIN_._UPPER_;
import _PACKAGE_.domain._DOMAIN_._UPPER_Instantiator;
import _PACKAGE_.domain._DOMAIN_._UPPER_Repository;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.mbp.MBPDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class MBP_UPPER_Repository<P extends _UPPER_PO> extends MBPDomainRepository<_UPPER_, P> implements _UPPER_Repository {

    @Autowired
    protected _UPPER_Mapper _LOWER_Mapper;

    /**
     * 领域上下文
     */
    @Autowired
    protected DomainContext context;

    /**
     * 领域校验器
     */
    @Autowired
    protected DomainValidator validator;

    /**
     * 空间实例化器
     */
    @Autowired
    protected _UPPER_Instantiator _LOWER_Instantiator;

    /**
     * 领域模型转数据模型
     */
    @Override
    public P do2po(_UPPER_ _LOWER_) {

    }

    /**
     * 数据模型转领域模型
     */
    @Override
    public _UPPER_ po2do(_UPPER_PO po) {

    }

    @Override
    public Class<P> getFetchClass() {
        return (Class<P>) _UPPER_PO.class;
    }

    @Override
    public BaseMapper<P> getBaseMapper() {
        return (BaseMapper<P>) _LOWER_Mapper;
    }
}
