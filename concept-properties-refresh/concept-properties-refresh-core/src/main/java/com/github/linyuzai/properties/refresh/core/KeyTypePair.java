package com.github.linyuzai.properties.refresh.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyTypePair {

    /**
     * 匹配的key
     */
    private String key;


    /**
     * 属性的类型
     */
    private Type type;
}
