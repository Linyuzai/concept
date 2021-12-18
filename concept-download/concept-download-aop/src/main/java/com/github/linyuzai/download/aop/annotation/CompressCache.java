package com.github.linyuzai.download.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CompressCache {

    /**
     * 是否开启压缩文件缓存 / Enable compressed file cache
     * 开启后，将会先在本地生成一个压缩文件 / When enabled, a compressed file will be generated locally first
     * 否则，讲直接写入输出流 / Otherwise, write the output stream directly
     * 如果文件小可以不开启 / It can not be opened if the file is small
     */
    boolean enabled() default true;

    /**
     * 压缩文件的缓存目录分组 / Cache directory grouping of compressed files
     * 同 {@link SourceCache#group()} / Same as {@link SourceCache#group()}
     */
    String group() default "";

    /**
     * 压缩文件名称 / Compressed file name
     * 默认情况下 / By default
     * 单文件会使用文件名 / A single file will use the file name
     * 多文件会使用切面的类和方法名或是固定的名称 / Multiple files use class and method names from aop or fixed names
     */
    String name() default "";

    /**
     * 是否删除压缩文件 / If delete compressed file
     * 如果压缩文件每次的内容都不一样建议删除 / If the contents of the compressed file are different every time, it is recommended to delete it
     * 防止因名称相同存在缓存而不会再次压缩 / Prevents the cache from being compressed again because it has the same name
     */
    boolean delete() default false;
}
