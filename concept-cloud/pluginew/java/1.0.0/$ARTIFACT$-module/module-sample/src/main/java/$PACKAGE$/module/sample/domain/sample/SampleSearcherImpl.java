package $PACKAGE$.module.sample.domain.sample;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.sample.SampleRepository;
import $PACKAGE$.module.sample.domain.sample.view.SampleQuery;
import $PACKAGE$.module.sample.domain.sample.view.SampleVO;
import com.github.linyuzai.domain.core.page.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<SampleVO> list(SampleQuery query) {
        return sampleRepository.select(sampleFacadeAdapter.toConditions(query))
                .list()
                .stream()
                .map(sampleFacadeAdapter::do2vo)
                .collect(Collectors.toList());
    }

    @Override
    public Pages<SampleVO> page(SampleQuery query, Pages.Args page) {
        return sampleRepository
                .page(sampleFacadeAdapter.toConditions(query), page)
                .map(sampleFacadeAdapter::do2vo);
    }
}
