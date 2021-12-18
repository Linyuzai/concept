package com.github.linyuzai.download.aop.advice;

import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * 切面定义 / Definition of advice
 */
public class DownloadConceptAdvisor extends DefaultPointcutAdvisor {

    public DownloadConceptAdvisor(DownloadConceptAdvice advice) {
        this(new DownloadConceptPointcut(), advice);
    }

    public DownloadConceptAdvisor(DownloadConceptPointcut pointcut, DownloadConceptAdvice advice) {
        setPointcut(pointcut);
        setAdvice(advice);
    }
}
