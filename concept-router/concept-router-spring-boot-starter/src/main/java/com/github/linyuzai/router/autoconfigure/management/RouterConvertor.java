package com.github.linyuzai.router.autoconfigure.management;

import com.github.linyuzai.router.core.concept.Router;

/**
 * 视图转换器
 */
public interface RouterConvertor {

    /**
     * 将路由对象转成视图
     *
     * @param router 路由
     * @return 视图
     */
    RouterVO do2vo(Router router);

    /**
     * 将视图对象转成路由
     *
     * @param vo 视图
     * @return 路由
     */
    Router vo2do(RouterVO vo);
}
