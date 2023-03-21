package com.github.linyuzai.cloud.web.core.concept;

import com.github.linyuzai.cloud.web.core.CloudWebProperties;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Web 功能入口实现
 */
@Getter
public class WebConceptImpl implements WebConcept {

    /**
     * 配置文件
     */
    private final CloudWebProperties properties;

    /**
     * 拦截链工厂
     */
    private final WebInterceptorChainFactory chainFactory;

    /**
     * 请求拦截器
     */
    private final List<WebInterceptor> requestInterceptors = new CopyOnWriteArrayList<>();

    /**
     * 响应拦截器
     */
    private final List<WebInterceptor> responseInterceptors = new CopyOnWriteArrayList<>();

    public WebConceptImpl(CloudWebProperties properties,
                          WebInterceptorChainFactory chainFactory,
                          List<WebInterceptor> interceptors) {
        this.properties = properties;
        this.chainFactory = chainFactory;
        interceptors.forEach(this::addInterceptor);
    }

    /**
     * 根据配置判断是否启用了拦截
     * <p>
     * 可以使用配置进行启用或不启用
     * <p>
     * concept.cloud.web.intercept.enabled=true
     */
    @Override
    public boolean isInterceptionEnabled() {
        return properties.getIntercept().isEnabled();
    }

    /**
     * 根据配置判断是否启用了请求拦截
     * <p>
     * 可以使用配置进行启用或不启用
     * <p>
     * concept.cloud.web.intercept.request.enabled=true
     */
    @Override
    public boolean isRequestInterceptionEnabled() {
        return isInterceptionEnabled() && properties.getIntercept().getRequest().isEnabled();
    }

    /**
     * 根据配置判断是否启用了响应拦截
     * <p>
     * 可以使用配置进行启用或不启用
     * <p>
     * concept.cloud.web.intercept.response.enabled=true
     */
    @Override
    public boolean isResponseInterceptionEnabled() {
        return isInterceptionEnabled() && properties.getIntercept().getResponse().isEnabled();
    }

    /**
     * 添加拦截器
     * <p>
     * 根据拦截器的作用域添加到请求拦截器和响应拦截器中
     * <p>
     * 最后进行排序
     *
     * @param interceptor 添加的拦截器
     */
    @Override
    public void addInterceptor(WebInterceptor interceptor) {
        Set<WebInterceptor.Scope> scopes = interceptor.getScopes();
        if (scopes.contains(WebInterceptor.Scope.REQUEST)) {
            requestInterceptors.add(interceptor);
            requestInterceptors.sort(Comparator.comparingInt(WebInterceptor::getOrder));
        }
        if (scopes.contains(WebInterceptor.Scope.RESPONSE)) {
            responseInterceptors.add(interceptor);
            responseInterceptors.sort(Comparator.comparingInt(WebInterceptor::getOrder));
        }
    }

    /**
     * 移除拦截器
     * <p>
     * 从请求拦截器或响应拦截器中分别移除对应的拦截器
     *
     * @param interceptor 移除的拦截器
     */
    @Override
    public void removeInterceptor(WebInterceptor interceptor) {
        requestInterceptors.remove(interceptor);
        responseInterceptors.remove(interceptor);
    }

    @Override
    public Object interceptRequest(WebContext context, ValueReturner returner) {
        return intercept(context, returner, requestInterceptors);
    }

    @Override
    public Object interceptResponse(WebContext context, ValueReturner returner) {
        return intercept(context, returner, responseInterceptors);
    }

    /**
     * 执行拦截
     * <p>
     * 通过 拦截链工厂创建拦截链
     * <p>
     * 然后执行拦截链
     *
     * @param context      上下文
     * @param returner     值返回器
     * @param interceptors 拦截器列表
     * @return 调用值返回器返回的值
     */
    protected Object intercept(WebContext context, ValueReturner returner, List<WebInterceptor> interceptors) {
        return chainFactory.create(0, interceptors).next(context, returner);
    }
}
