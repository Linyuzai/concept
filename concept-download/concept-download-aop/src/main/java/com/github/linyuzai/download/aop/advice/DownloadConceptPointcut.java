package com.github.linyuzai.download.aop.advice;

import com.github.linyuzai.download.aop.annotation.Download;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

public class DownloadConceptPointcut extends AnnotationMatchingPointcut {

    public DownloadConceptPointcut() {
        super(null, Download.class, true);
    }
}
