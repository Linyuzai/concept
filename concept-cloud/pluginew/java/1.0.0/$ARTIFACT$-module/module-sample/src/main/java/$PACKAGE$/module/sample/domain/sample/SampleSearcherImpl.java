package $PACKAGE$.module.sample.domain.sample;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.sample.SampleRepository;
import $PACKAGE$.module.sample.domain.sample.view.SampleQuery;
import $PACKAGE$.module.sample.domain.sample.view.SampleVO;
import com.github.linyuzai.domain.core.page.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 查询实现
 */
@Component
public class SampleSearcherImpl implements SampleSearcher {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private SampleFacadeAdapter sampleFacadeAdapter;

    @Override
    public SampleVO get(String id) {
        Sample sample = sampleRepository.get(id);
        if (sample == null) {
            return null;
        }
        return sampleFacadeAdapter.do2vo(sample);
    }

    @Override
    public Pages<SampleVO> page(SampleQuery query, Pages.Args page) {
        return sampleRepository
                .page(sampleFacadeAdapter.toConditions(query), page)
                .map(sampleFacadeAdapter::do2vo);
    }
}
