package com.bytedance.juejin.pin.domain.club;

import com.bytedance.juejin.domain.club.Club;
import com.bytedance.juejin.domain.club.ClubRepository;
import com.bytedance.juejin.pin.domain.club.view.ClubFullVO;
import com.bytedance.juejin.pin.domain.club.view.ClubQuery;
import com.bytedance.juejin.pin.domain.club.view.ClubVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 圈子搜索实现
 */
@Component
public class ClubSearcherImpl implements ClubSearcher {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubFacadeAdapter clubFacadeAdapter;

    @Override
    public ClubFullVO get(String id) {
        Club club = clubRepository.get(id);
        if (club == null) {
            return null;
        }
        return clubFacadeAdapter.do2full(club);
    }

    @Override
    public List<ClubVO> list(ClubQuery query) {
        return clubRepository.select(clubFacadeAdapter.toConditions(query))
                .list()
                .stream()
                .map(clubFacadeAdapter::do2vo)
                .collect(Collectors.toList());
    }
}
