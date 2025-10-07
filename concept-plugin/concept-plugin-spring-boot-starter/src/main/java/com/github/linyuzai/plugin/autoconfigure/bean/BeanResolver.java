package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractSupplier;
import com.github.linyuzai.plugin.core.listener.PluginListener;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassResolver;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Bean 解析器
 */
@Getter
@Setter
@HandlerDependency(ClassResolver.class)
public class BeanResolver extends AbstractPluginResolver<ClassSupplier, BeanSupplier> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public BeanSupplier doResolve(ClassSupplier classSupplier, PluginContext context) {
        return new BeanSupplierImpl(classSupplier, context.getPlugin(), applicationContext);
    }

    @Override
    public Object getInboundKey() {
        return ClassSupplier.class;
    }

    @Override
    public Object getOutboundKey() {
        return BeanSupplier.class;
    }

    @Getter
    @RequiredArgsConstructor
    public static class BeanSupplierImpl extends AbstractSupplier<Object> implements BeanSupplier {

        private final ClassSupplier classSupplier;

        private final Plugin plugin;

        private final ApplicationContext applicationContext;

        @Override
        public Object create() {
            Object bean = applicationContext.getAutowireCapableBeanFactory()
                    .createBean(classSupplier.get());
            plugin.addListener(new PluginListener() {
                @Override
                public void onUnload(Plugin plugin) {
                    applicationContext.getAutowireCapableBeanFactory().destroyBean(bean);
                }
            });
            return bean;
        }
    }
}
