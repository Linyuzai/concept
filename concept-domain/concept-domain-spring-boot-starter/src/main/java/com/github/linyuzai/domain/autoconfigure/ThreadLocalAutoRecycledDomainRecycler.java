package com.github.linyuzai.domain.autoconfigure;

import com.github.linyuzai.domain.core.recycler.DomainRecycler;
import com.github.linyuzai.domain.core.recycler.ThreadLocalDomainRecycler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThreadLocalAutoRecycledDomainRecycler extends ThreadLocalDomainRecycler
        implements WebMvcConfigurer, HandlerInterceptor {

    public ThreadLocalAutoRecycledDomainRecycler(DomainRecycler recycler) {
        super(recycler);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        recycle();
    }
}
