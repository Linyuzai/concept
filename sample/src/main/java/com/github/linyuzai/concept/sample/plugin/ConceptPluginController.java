package com.github.linyuzai.concept.sample.plugin;

import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginLocation;
import com.github.linyuzai.plugin.core.autoload.WatchServicePluginAutoLoader;
import com.github.linyuzai.plugin.core.concept.DefaultPluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.extract.OnPluginExtract;
import com.github.linyuzai.plugin.core.match.PluginName;
import com.github.linyuzai.plugin.core.match.PluginPath;
import com.github.linyuzai.plugin.core.util.PluginLoadLogger;
import com.github.linyuzai.plugin.jar.autoload.JarNotifier;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import com.github.linyuzai.plugin.jar.extract.JarDynamicExtractor;
import com.github.linyuzai.plugin.jar.filter.ModifierFilter;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import com.github.linyuzai.plugin.jar.match.PluginAnnotation;
import com.github.linyuzai.plugin.jar.match.PluginClass;
import com.github.linyuzai.plugin.jar.match.PluginClassName;
import com.github.linyuzai.plugin.jar.match.PluginPackage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/concept-plugin")
public class ConceptPluginController {

    private CustomPlugin plugin;

    private String append(String s) {
        StringBuilder builder = new StringBuilder(s);
        while (builder.length() < 70) {
            builder.append(" ");
        }
        return builder.toString();
    }

    @SneakyThrows
    private String inputStreamToString(InputStream is) {
        byte[] bytes = new byte[is.available()];
        int read = is.read(bytes);
        return new String(bytes);
    }

    private final PluginConcept concept = new DefaultPluginConcept.Builder()
            //.addFilter(new PathFilter("plugin"))
            //.addFilter(new NameFilter("*.json"))
            .addFilters(new PackageFilter("com.github.linyuzai.concept.sample.plugin"))
            .addFilters(new ModifierFilter(Modifier::isInterface, Modifier::isAbstract).negate())
            //.addFilter(new AnnotationFilter(CustomPluginAnnotation.class))
            /*.addExtractor(new PluginContextExtractor<PluginContext>() {
                @Override
                public void onExtract(PluginContext plugin) {
                    System.out.println(append("PluginContextExtractor<PluginContext>: ") + plugin);
                }
            })
            .addExtractor(new PluginObjectExtractor<Plugin>() {
                @Override
                public void onExtract(Plugin plugin) {
                    System.out.println(append("PluginObjectExtractor<Plugin>: ") + plugin);
                }
            })*/
            //Content
            /*.addExtractor(new ContentExtractor<Collection<byte[]>>() {
                @Override
                public void onExtract(Collection<byte[]> plugin) {
                    System.out.println(append("ContentExtractor<Collection<byte[]>>: ") + plugin);
                    for (byte[] bytes : plugin) {
                        System.out.println(append("ContentExtractor<Collection<byte[]>>: ") + new String(bytes));
                    }
                }
            })
            .addExtractor(new ContentExtractor<List<byte[]>>() {
                @Override
                public void onExtract(List<byte[]> plugin) {
                    System.out.println(append("ContentExtractor<List<byte[]>>: ") + plugin);
                    for (byte[] bytes : plugin) {
                        System.out.println(append("ContentExtractor<List<byte[]>>: ") + new String(bytes));
                    }
                }
            })
            .addExtractor(new ContentExtractor<Set<byte[]>>() {
                @Override
                public void onExtract(Set<byte[]> plugin) {
                    System.out.println(append("ContentExtractor<Set<byte[]>>: ") + plugin);
                    for (byte[] bytes : plugin) {
                        System.out.println(append("ContentExtractor<Set<byte[]>>: ") + new String(bytes));
                    }
                }
            })
            .addExtractor(new ContentExtractor<Map<String, byte[]>>() {
                @Override
                public void onExtract(Map<String, byte[]> plugin) {
                    System.out.println(append("ContentExtractor<Map<String, byte[]>>: ") + plugin);
                    for (byte[] bytes : plugin.values()) {
                        System.out.println(append("ContentExtractor<Map<String, byte[]>>: ") + new String(bytes));
                    }
                }
            })
            .addExtractor(new ContentExtractor<byte[][]>() {
                @Override
                public void onExtract(byte[][] plugin) {
                    System.out.println(append("ContentExtractor<byte[][]>: ") + Arrays.toString(plugin));
                    for (byte[] bytes : plugin) {
                        System.out.println(append("ContentExtractor<byte[][]>: ") + new String(bytes));
                    }
                }
            })*/
            /*.addExtractor(new ContentExtractor<Collection<String>>() {
                @Override
                public void onExtract(Collection<String> plugin) {
                    System.out.println(append("ContentExtractor<Collection<String>>: ") + plugin);
                }
            })
            .addExtractor(new ContentExtractor<List<String>>() {
                @Override
                public void onExtract(List<String> plugin) {
                    System.out.println(append("ContentExtractor<List<String>>: ") + plugin);
                }
            })
            .addExtractor(new ContentExtractor<Set<String>>() {
                @Override
                public void onExtract(Set<String> plugin) {
                    System.out.println(append("ContentExtractor<Set<String>>: ") + plugin);
                }
            })
            .addExtractor(new ContentExtractor<Map<String, String>>() {
                @Override
                public void onExtract(Map<String, String> plugin) {
                    System.out.println(append("ContentExtractor<Map<String, String>>: ") + plugin);
                }
            })
            .addExtractor(new ContentExtractor<String[]>() {
                @Override
                public void onExtract(String[] plugin) {
                    System.out.println(append("ContentExtractor<String[]>: ") + Arrays.toString(plugin));
                }
            })*/
            /*.addExtractor(new ContentExtractor<Collection<? extends InputStream>>() {
                @Override
                public void onExtract(Collection<? extends InputStream> plugin) {
                    System.out.println(append("ContentExtractor<Collection<? extends InputStream>>: ") + plugin);
                    for (InputStream is : plugin) {
                        System.out.println(append("ContentExtractor<Collection<? extends InputStream>>: ") + inputStreamToString(is));
                    }
                }
            })
            .addExtractor(new ContentExtractor<List<? extends InputStream>>() {
                @Override
                public void onExtract(List<? extends InputStream> plugin) {
                    System.out.println(append("ContentExtractor<List<? extends InputStream>>: ") + plugin);
                    for (InputStream is : plugin) {
                        System.out.println(append("ContentExtractor<List<? extends InputStream>>: ") + inputStreamToString(is));
                    }
                }
            })
            .addExtractor(new ContentExtractor<Set<? extends InputStream>>() {
                @Override
                public void onExtract(Set<? extends InputStream> plugin) {
                    System.out.println(append("ContentExtractor<Set<? extends InputStream>>: ") + plugin);
                    for (InputStream is : plugin) {
                        System.out.println(append("ContentExtractor<Set<? extends InputStream>>: ") + inputStreamToString(is));
                    }
                }
            })
            .addExtractor(new ContentExtractor<Map<String, ? extends InputStream>>() {
                @Override
                public void onExtract(Map<String, ? extends InputStream> plugin) {
                    System.out.println(append("ContentExtractor<Map<String, ? extends InputStream>>: ") + plugin);
                    for (InputStream is : plugin.values()) {
                        System.out.println(append("ContentExtractor<Map<String, ? extends InputStream>>: ") + inputStreamToString(is));
                    }
                }
            })
            .addExtractor(new ContentExtractor<InputStream[]>() {
                @Override
                public void onExtract(InputStream[] plugin) {
                    System.out.println(append("ContentExtractor<InputStream[]>: ") + Arrays.toString(plugin));
                    for (InputStream is : plugin) {
                        System.out.println(append("ContentExtractor<InputStream[]>: ") + inputStreamToString(is));
                    }
                }
            })*/
            //Properties
            /*.addExtractor(new PropertiesExtractor<Properties>() {
                @Override
                public void onExtract(Properties plugin) {
                    System.out.println(append("PropertiesExtractor<Properties>: ") + plugin);
                }
            })
            .addExtractor(new PropertiesExtractor<Map<String, String>>() {
                @Override
                public void onExtract(Map<String, String> plugin) {
                    System.out.println(append("PropertiesExtractor<Map<String,String>>: ") + plugin);
                }
            })*/
            /*.addExtractor(new PropertiesExtractor<Collection<Properties>>() {
                @Override
                public void onExtract(Collection<Properties> plugin) {
                    System.out.println(append("PropertiesExtractor<Collection<Properties>>: ") + plugin);
                }
            })
            .addExtractor(new PropertiesExtractor<List<Properties>>() {
                @Override
                public void onExtract(List<Properties> plugin) {
                    System.out.println(append("PropertiesExtractor<List<Properties>>: ") + plugin);
                }
            })
            .addExtractor(new PropertiesExtractor<Set<Properties>>() {
                @Override
                public void onExtract(Set<Properties> plugin) {
                    System.out.println(append("PropertiesExtractor<Set<Properties>>: ") + plugin);
                }
            })
            .addExtractor(new PropertiesExtractor<Map<String, Properties>>() {
                @Override
                public void onExtract(Map<String, Properties> plugin) {
                    System.out.println(append("PropertiesExtractor<Map<String, Properties>>: ") + plugin);
                }
            })
            .addExtractor(new PropertiesExtractor<Properties[]>() {
                @Override
                public void onExtract(Properties[] plugin) {
                    System.out.println(append("PropertiesExtractor<Properties[]>: ") + Arrays.toString(plugin));
                }
            })*/
            /*.addExtractor(new PropertiesExtractor<Collection<Map<String, String>>>() {
                @Override
                public void onExtract(Collection<Map<String, String>> plugin) {
                    System.out.println(append("PropertiesExtractor<Collection<Map<String, String>>>: ") + plugin);
                }
            })
            .addExtractor(new PropertiesExtractor<List<Map<String, String>>>() {
                @Override
                public void onExtract(List<Map<String, String>> plugin) {
                    System.out.println(append("PropertiesExtractor<List<Map<String, String>>>: ") + plugin);
                }
            })
            .addExtractor(new PropertiesExtractor<Set<Map<String, String>>>() {
                @Override
                public void onExtract(Set<Map<String, String>> plugin) {
                    System.out.println(append("PropertiesExtractor<Set<Map<String, String>>>: ") + plugin);
                }
            })
            .addExtractor(new PropertiesExtractor<Map<String, Map<String, String>>>() {
                @Override
                public void onExtract(Map<String, Map<String, String>> plugin) {
                    System.out.println(append("PropertiesExtractor<Map<String, Map<String, String>>>: ") + plugin);
                }
            })
            .addExtractor(new PropertiesExtractor<Map<String, String>[]>() {
                @Override
                public void onExtract(Map<String, String>[] plugin) {
                    System.out.println(append("PropertiesExtractor<Map<String, String>[]>: ") + Arrays.toString(plugin));
                }
            })*/
            //Class
            /*.addExtractor(new ClassExtractor<Collection<Class>>() {
                @Override
                public void onExtract(Collection<Class> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class>>() {
                @Override
                public void onExtract(List<Class> plugin) {
                    System.out.println(append("ClassExtractor<List<Class>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class>>() {
                @Override
                public void onExtract(Set<Class> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class>>() {
                @Override
                public void onExtract(Map<Object, Class> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class[]>() {
                @Override
                public void onExtract(Class[] plugin) {
                    System.out.println(append("ClassExtractor<Class[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<Class<?>>>() {
                @Override
                public void onExtract(Collection<Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class<?>>>() {
                @Override
                public void onExtract(List<Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<List<Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class<?>>>() {
                @Override
                public void onExtract(Set<Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class<?>>>() {
                @Override
                public void onExtract(Map<Object, Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class<?>[]>() {
                @Override
                public void onExtract(Class<?>[] plugin) {
                    System.out.println(append("ClassExtractor<Class<?>[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<? extends Class<?>>>() {
                @Override
                public void onExtract(Collection<? extends Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<? extends Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<? extends Class<?>>>() {
                @Override
                public void onExtract(List<? extends Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<List<? extends Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<? extends Class<?>>>() {
                @Override
                public void onExtract(Set<? extends Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Set<? extends Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, ? extends Class<?>>>() {
                @Override
                public void onExtract(Map<Object, ? extends Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, ? extends Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Collection<Class<Object>>>() {
                @Override
                public void onExtract(Collection<Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class<Object>>>() {
                @Override
                public void onExtract(List<Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<List<Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class<Object>>>() {
                @Override
                public void onExtract(Set<Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class<Object>>>() {
                @Override
                public void onExtract(Map<Object, Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class<Object>[]>() {
                @Override
                public void onExtract(Class<Object>[] plugin) {
                    System.out.println(append("ClassExtractor<Class<Object>[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<? extends Class<Object>>>() {
                @Override
                public void onExtract(Collection<? extends Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<? extends Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<? extends Class<Object>>>() {
                @Override
                public void onExtract(List<? extends Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<List<? extends Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<? extends Class<Object>>>() {
                @Override
                public void onExtract(Set<? extends Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Set<? extends Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, ? extends Class<Object>>>() {
                @Override
                public void onExtract(Map<Object, ? extends Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, ? extends Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Collection<Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Collection<Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class<CustomPlugin>>>() {
                @Override
                public void onExtract(List<Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<List<Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Set<Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Map<Object, Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class<CustomPlugin>[]>() {
                @Override
                public void onExtract(Class<CustomPlugin>[] plugin) {
                    System.out.println(append("ClassExtractor<Class<CustomPlugin>[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<? extends Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Collection<? extends Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<? extends Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<? extends Class<CustomPlugin>>>() {
                @Override
                public void onExtract(List<? extends Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<List<? extends Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<? extends Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Set<? extends Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Set<? extends Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, ? extends Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Map<Object, ? extends Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, ? extends Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Collection<Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Collection<Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(List<Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<List<Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Set<Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Map<Object, Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class<? extends CustomPlugin>[]>() {
                @Override
                public void onExtract(Class<? extends CustomPlugin>[] plugin) {
                    System.out.println(append("ClassExtractor<Class<? extends CustomPlugin>[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<? extends Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Collection<? extends Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<? extends Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<? extends Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(List<? extends Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<List<? extends Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<? extends Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Set<? extends Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Set<? extends Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, ? extends Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Map<Object, ? extends Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, ? extends Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })*/
            //Instance
            /*.addExtractor(new InstanceExtractor<Collection>() {
                @Override
                public void onExtract(Collection plugin) {
                    System.out.println(append("InstanceExtractor<Collection>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List>() {
                @Override
                public void onExtract(List plugin) {
                    System.out.println(append("InstanceExtractor<List>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set>() {
                @Override
                public void onExtract(Set plugin) {
                    System.out.println(append("InstanceExtractor<Set>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map>() {
                @Override
                public void onExtract(Map plugin) {
                    System.out.println(append("InstanceExtractor<Map>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Collection<?>>() {
                @Override
                public void onExtract(Collection<?> plugin) {
                    System.out.println(append("InstanceExtractor<Collection<?>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List<?>>() {
                @Override
                public void onExtract(List<?> plugin) {
                    System.out.println(append("InstanceExtractor<List<?>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set<?>>() {
                @Override
                public void onExtract(Set<?> plugin) {
                    System.out.println(append("InstanceExtractor<Set<?>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map<?, ?>>() {
                @Override
                public void onExtract(Map<?, ?> plugin) {
                    System.out.println(append("InstanceExtractor<Map<?, ?>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Collection<Object>>() {
                @Override
                public void onExtract(Collection<Object> plugin) {
                    System.out.println(append("InstanceExtractor<Collection<Object>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List<Object>>() {
                @Override
                public void onExtract(List<Object> plugin) {
                    System.out.println(append("InstanceExtractor<List<Object>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set<Object>>() {
                @Override
                public void onExtract(Set<Object> plugin) {
                    System.out.println(append("InstanceExtractor<Set<Object>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map<Object, Object>>() {
                @Override
                public void onExtract(Map<Object, Object> plugin) {
                    System.out.println(append("InstanceExtractor<Map<Object, Object>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Object[]>() {
                @Override
                public void onExtract(Object[] plugin) {
                    System.out.println(append("InstanceExtractor<Object[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new InstanceExtractor<Collection<CustomPlugin>>() {
                @Override
                public void onExtract(Collection<CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Collection<CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List<CustomPlugin>>() {
                @Override
                public void onExtract(List<CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<List<CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set<CustomPlugin>>() {
                @Override
                public void onExtract(Set<CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Set<CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map<Object, CustomPlugin>>() {
                @Override
                public void onExtract(Map<Object, CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Map<Object, CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<CustomPlugin[]>() {
                @Override
                public void onExtract(CustomPlugin[] plugin) {
                    System.out.println(append("InstanceExtractor<CustomPlugin[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new InstanceExtractor<Collection<? extends CustomPlugin>>() {
                @Override
                public void onExtract(Collection<? extends CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Collection<? extends CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List<? extends CustomPlugin>>() {
                @Override
                public void onExtract(List<? extends CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<List<? extends CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set<? extends CustomPlugin>>() {
                @Override
                public void onExtract(Set<? extends CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Set<? extends CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map<Object, ? extends CustomPlugin>>() {
                @Override
                public void onExtract(Map<Object, ? extends CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Map<Object, ? extends CustomPlugin>>: ") + plugin);
                }
            })*/
            /*.addExtractor(new InstanceExtractor<CustomPlugin>() {
                @Override
                public void onExtract(CustomPlugin plugin) {
                    ConceptPluginController.this.plugin = plugin;
                }
            })*/
            /*.addExtractor(new ClassExtractor<Class<? extends CustomPlugin>>() {

                @Override
                public void onExtract(Class<? extends CustomPlugin> plugin) {
                }
            })*/
            .addExtractors(new JarDynamicExtractor(this))//自动匹配回调添加了@OnPluginExtract注解的方法参数
            .addEventListeners(new PluginLoadLogger(log::info))
            .build();

    private final PluginAutoLoader loader = new WatchServicePluginAutoLoader.Builder()
            .locations(
                    new PluginLocation.Builder()
                            .path("/Users/tanghanzheng/concept/plugin")
                            .filter(it -> it.endsWith(".jar"))
                            .build(),
                    new PluginLocation.Builder()
                            .path("/Users/tanghanzheng/concept/plugin2")
                            .filter(it -> it.endsWith(".jar"))
                            .build())
            .executor(Executors.newSingleThreadExecutor())
            //.onCreate(concept::load)
            .onNotify(new JarNotifier(concept))
            .onError(e -> log.error("Plugin auto load error", e))
            .build();

    @PostConstruct
    private void start() {
        loader.start();
    }

    @PreDestroy
    private void stop() {
        loader.stop();
    }

    /**
     * 插件匹配回调
     */
    @OnPluginExtract
    private void onPluginExtract(
            ExJarPlugin plugin, PluginContext context,

            //所有的 CustomPlugin 类和 properties 文件
            List<Class<? extends CustomPlugin>> p0, Properties p1,

            //包下所有的 CustomPlugin 实例
            @PluginPackage("com.github.linyuzai.concept.sample.plugin") Collection<? extends CustomPlugin> p2,

            //所有的 com.github.linyuzai.concept.sample.plugin.CustomPlugin 类
            @PluginClassName("com.github.linyuzai.concept.sample.plugin.CustomPlugin") Set<Class<?>> p3,

            //一个 CustomPluginImpl 对象
            @PluginClass(CustomPlugin.class) CustomPlugin p4,

            Class<? extends CustomPlugin> p4_1,

            //所有标注了 CustomPluginAnnotation 注解的类
            @PluginAnnotation(CustomPluginAnnotation.class) Class<?>[] p5,

            //properties 文件通过 Map 接收
            //@PluginProperties("plugin.map.**") Map<String, String> p6,

            //properties 文件中 plugin.a 的属性值
            //@PluginProperties("plugin.a") String p7,

            //properties 文件中 plugin.b 的属性值
            //@PluginProperties("plugin.b") String p8,

            //一个指定目录下的 properties 文件
            @PluginPath("plugin") Properties p9,

            //名称为 config.json 的文件内容
            @PluginName("plugin.json") String p10) {

        System.out.println("onPluginExtract: " + plugin);
        System.out.println("onPluginExtract: " + context);
        System.out.println("List<Class<? extends CustomPlugin>>: " + p0);
        System.out.println("Properties: " + p1);
        System.out.println("@PluginPackage(\"com.github.linyuzai.concept.sample.plugin\") Collection<? extends CustomPlugin>: " + p2);
        System.out.println("@PluginClassName(\"com.github.linyuzai.concept.sample.plugin.CustomPlugin\") Set<Class<?>>: " + p3);
        System.out.println("@PluginClass(CustomPluginImpl.class) CustomPlugin: " + p4);
        System.out.println("Class<? extends CustomPlugin>: " + p4_1);
        System.out.println("@PluginAnnotation(CustomPluginAnnotation.class) Class<?>[]: " + Arrays.toString(p5));
        //System.out.println("@PluginProperties(\"plugin.map.**\") Map<String, String>: " + p6);
        //System.out.println("@PluginProperties(\"plugin.a\") String: " + p7);
        //System.out.println("@PluginProperties(\"plugin.b\") String: " + p8);
        System.out.println("@PluginPath(\"plugin\") Properties: " + p9);
        System.out.println("@PluginName(\"plugin.json\") String: " + p10);
    }

    @GetMapping("/run")
    public void runPlugin() {
        plugin.run();
    }

    @GetMapping("/load")
    public void loadPlugin() {
        concept.load("/Users/concept/plugin/sample-0.0.1-SNAPSHOT-plain.jar");
    }
}
