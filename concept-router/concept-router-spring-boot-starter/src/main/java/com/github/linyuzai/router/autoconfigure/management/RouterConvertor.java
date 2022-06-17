package com.github.linyuzai.router.autoconfigure.management;

import com.github.linyuzai.router.core.concept.Router;

public interface RouterConvertor {

    RouterVO do2vo(Router router);

    Router vo2do(RouterVO vo);
}
