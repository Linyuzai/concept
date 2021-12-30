package com.github.linyuzai.download.aop.advice;

import com.github.linyuzai.download.core.concept.DownloadConcept;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

/**
 * 切面定义 / Definition of advice
 */
public class DownloadConceptAdvisor extends DefaultPointcutAdvisor implements BeanPostProcessor {

    public DownloadConceptAdvisor() {
        this(new DownloadConceptPointcut(), new DownloadConceptAdvice());
    }

    public DownloadConceptAdvisor(DownloadConceptAdvice advice) {
        this(new DownloadConceptPointcut(), advice);
    }

    public DownloadConceptAdvisor(DownloadConceptPointcut pointcut, DownloadConceptAdvice advice) {
        setPointcut(pointcut);
        setAdvice(advice);
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof DownloadConcept) {
            DownloadConceptAdvice advice = (DownloadConceptAdvice) getAdvice();
            advice.setDownloadConcept((DownloadConcept) bean);
        }
        return bean;
    }
}
