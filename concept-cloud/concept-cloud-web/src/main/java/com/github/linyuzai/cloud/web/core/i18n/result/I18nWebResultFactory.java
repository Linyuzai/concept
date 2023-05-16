package com.github.linyuzai.cloud.web.core.i18n.result;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.BooleanWebResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 异常时尝试用 i18n 的配置处理异常信息
 */
@Component
public class I18nWebResultFactory extends BooleanWebResultFactory {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected String getSuccessMessage(WebContext context) {
        String successMessage = super.getSuccessMessage(context);
        return messageSource.getMessage(successMessage, null, getLocale(context));
    }

    @Override
    protected String getFailureMessage(Throwable e, WebContext context) {
        String failureMessage = super.getFailureMessage(e, context);
        return messageSource.getMessage(failureMessage, null, getLocale(context));
    }

    protected Locale getLocale(WebContext context) {
        return context.get(Locale.class, Locale.CHINESE);
    }
}
