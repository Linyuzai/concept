package com.github.linyuzai.plugin.core.type;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@link TypeMetadataFactory} 的默认实现
 */
public class DefaultTypeMetadataFactory implements TypeMetadataFactory {

    /**
     * 根据 {@link Type} 解析泛型信息
     *
     * @param type 泛型 {@link Type}
     * @return {@link TypeMetadata}
     */
    @Override
    public TypeMetadata create(Type type) {
        if (type instanceof Class) {
            Class<?> cClass = (Class<?>) type;
            //元素类型指定Object
            return create(type, cClass, Object.class);
        } else if (type instanceof ParameterizedType) {
            // A<?> A<B> A<B<?>> A<? extends B>
            Type rawType = ((ParameterizedType) type).getRawType();
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (rawType instanceof Class) {
                Class<?> cClass = (Class<?>) rawType;
                TypeMetadata metadata = create(type, cClass, actualTypeArguments[0]);
                //如果是Map，使用第二个泛型参数重新设置元素类型
                if (metadata instanceof MapTypeMetadata) {
                    ((MapTypeMetadata) metadata).setElementType(actualTypeArguments[1]);
                    ((MapTypeMetadata) metadata).setElementClass(getElementClass(actualTypeArguments[1]));
                }
                return metadata;
            }
        } else if (type instanceof WildcardType) {
            // ? extends A
            // ? super A
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            if (upperBounds.length > 0) {
                return create(upperBounds[0]);
            }
            //TODO ? super xxx 好像没有必要
        } else if (type instanceof GenericArrayType) {
            // A<?>[] A<B>[]
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            ArrayTypeMetadata metadata = new ArrayTypeMetadata();
            metadata.setContainerType(type);
            metadata.setContainerClass(ReflectionUtils.toClass(componentType));
            metadata.setElementType(componentType);
            metadata.setElementClass(getElementClass(componentType));
            return metadata;
        }
        return null;
    }

    /**
     * 通过容器类 {@link Class} 生成对应的 {@link TypeMetadata} 并填充元素类 {@link Class}
     *
     * @param cType  容器类 {@link Type}
     * @param cClass 容器类 {@link Class}
     * @param eType  元素类 {@link Type}
     * @return {@link TypeMetadata}
     */
    public TypeMetadata create(Type cType, Class<?> cClass, Type eType) {
        if (Map.class.isAssignableFrom(cClass)) {
            MapTypeMetadata metadata = new MapTypeMetadata();
            metadata.setContainerType(cType);
            metadata.setContainerClass(cClass);
            metadata.setElementType(eType);
            metadata.setElementClass(getElementClass(eType));
            return metadata;
        } else if (List.class.isAssignableFrom(cClass)) {
            ListTypeMetadata metadata = new ListTypeMetadata();
            metadata.setContainerType(cType);
            metadata.setContainerClass(cClass);
            metadata.setElementType(eType);
            metadata.setElementClass(getElementClass(eType));
            return metadata;
        } else if (Set.class.isAssignableFrom(cClass)) {
            SetTypeMetadata metadata = new SetTypeMetadata();
            metadata.setContainerType(cType);
            metadata.setContainerClass(cClass);
            metadata.setElementType(eType);
            metadata.setElementClass(getElementClass(eType));
            return metadata;
        } else if (Collection.class.isAssignableFrom(cClass)) {
            CollectionTypeMetadata metadata = new CollectionTypeMetadata();
            metadata.setContainerType(cType);
            metadata.setContainerClass(cClass);
            metadata.setElementType(eType);
            metadata.setElementClass(getElementClass(eType));
            return metadata;
        } else if (cClass.isArray()) {
            ArrayTypeMetadata metadata = new ArrayTypeMetadata();
            Class<?> componentType = cClass.getComponentType();
            metadata.setContainerType(cType);
            metadata.setContainerClass(componentType);//数组使用元素类型
            metadata.setElementType(componentType);
            metadata.setElementClass(getElementClass(componentType));
            return metadata;
        } else {
            return new ObjectTypeMetadata(cClass);
        }
    }

    public Class<?> getElementClass(Type type) {
        /*if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            return ReflectionUtils.toClass(rawType);
        } else if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length > 0) {
                return ReflectionUtils.toClass(upperBounds[0]);
            }
        }*/
        return ReflectionUtils.toClass(type);
    }
}
