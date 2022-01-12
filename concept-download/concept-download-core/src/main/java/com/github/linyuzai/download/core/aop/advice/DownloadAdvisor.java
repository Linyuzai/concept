package com.github.linyuzai.download.core.aop.advice;

import com.github.linyuzai.download.core.concept.DownloadConcept;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

/**
 * 切面定义 / Definition of advice
 */
public class DownloadAdvisor extends DefaultPointcutAdvisor implements BeanPostProcessor {

    public DownloadAdvisor() {
        this(new DownloadPointcut(), new DownloadAdvice());
    }

    public DownloadAdvisor(DownloadAdvice advice) {
        this(new DownloadPointcut(), advice);
    }

    public DownloadAdvisor(DownloadPointcut pointcut, DownloadAdvice advice) {
        setPointcut(pointcut);
        setAdvice(advice);
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof DownloadConcept) {
            DownloadAdvice advice = (DownloadAdvice) getAdvice();
            advice.setDownloadConcept((DownloadConcept) bean);
        }
        return bean;
    }
}
