package org.freedom.backend.associate;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiayx
 */
@SuppressWarnings("unchecked")
public abstract class AssociateUtils {


    /** 主键的属性名 */
    public static final String ID_PROPERTY = "id";

    /** 作为后缀的主键属性名 */
    public static final String SUFFIX_ID_PROPERTY = "Id";

    /**
     * 探测集合中元素类型
     *
     * @param collection 集合，必须不为空且不含null元素
     * @return 集合中的元素类型
     */
    public static <T> Class<? extends T> detectElementType(Collection<T> collection) {
        return (Class<? extends T>) collection.iterator().next().getClass();
    }

    /**
     * 集合转换为映射，将属性名作为键值
     *
     * @param beans    Bean集合，必须不为空且不含null元素
     * @param property 作为键值的属性名，必须是有效的属性名
     */
    public static <I, E> Map<I, E> propertyAsKey(Collection<E> beans, String property) {
        Class<? extends E> beanClass = detectElementType(beans);
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(beanClass, property);
        if (propertyDescriptor == null) throwPropertyNotFoundException(beanClass, property);
        return beans.stream().collect(Collectors.toMap(associate -> (I) ReflectionUtils.invokeMethod(propertyDescriptor.getReadMethod(), associate), Function.identity()));
    }

    /** 抛出属性找不到异常 */
    public static void throwPropertyNotFoundException(Class<?> entityClass, String property) {
        throw new IllegalArgumentException("can't found property['" + property + "'] on class[" + entityClass.getName() + "]");
    }


    /**
     * 设置关联对象，单个主实体对象和单个关联对象
     *
     * @param main              主实体对象，必须不能为null
     * @param associateProperty 关联属性，必须是有效属性
     * @param associate         关联对象
     * @param <M>               主实体类，Main首字母
     * @param <A>               关联实体类，Associate首字母
     */
    public static <M, A> void setAssociate(M main, String associateProperty, @Nullable A associate) {
        PropertyDescriptor associatePropertyDescriptor = BeanUtils.getPropertyDescriptor(main.getClass(), associateProperty);
        if (associatePropertyDescriptor == null) throwPropertyNotFoundException(main.getClass(), associateProperty);
        ReflectionUtils.invokeMethod(associatePropertyDescriptor.getWriteMethod(), main, associate);
    }

    /**
     * 设置关联对象，单个主实体对象和单个关联对象，通过关联对象源间接提供关联对象
     *
     * @param main                主实体对象，必须不能为null
     * @param associateProperty   关联属性，必须是有效属性
     * @param associateSource     关联对象源，必须不能为null
     * @param associateIdProperty 关联主键属性，必须是有效属性
     * @param <M>                 主实体类，Main首字母
     * @param <A>                 关联实体类，Associate首字母
     * @param <I>                 关联实体主键类，Id首字母
     */
    public static <M, A, I> void setAssociate(M main, String associateProperty, AssociateSource<I, A> associateSource, String associateIdProperty) {
        PropertyDescriptor associateIdPropertyDescriptor = BeanUtils.getPropertyDescriptor(main.getClass(), associateIdProperty);
        if (associateIdPropertyDescriptor == null) throwPropertyNotFoundException(main.getClass(), associateIdProperty);
        I id = (I) ReflectionUtils.invokeMethod(associateIdPropertyDescriptor.getReadMethod(), main);
        if (id == null) return;
        A associate = associateSource.getById(id);
        setAssociate(main, associateProperty, associate);
    }


    /**
     * 设置关联对象，单个主实体对象和多个关联对象
     *
     * @param main              主实体对象，必须不能为null
     * @param associateProperty 关联属性，必须是有效属性
     * @param associates        关联对象集合
     * @param <M>               主实体类，Main首字母
     * @param <A>               关联实体类，Associate首字母
     */
    public static <M, A> void setAssociateCollection(M main, String associateProperty, @Nullable Collection<A> associates) {
        PropertyDescriptor associatePropertyDescriptor = BeanUtils.getPropertyDescriptor(main.getClass(), associateProperty);
        if (associatePropertyDescriptor == null) throwPropertyNotFoundException(main.getClass(), associateProperty);
        ReflectionUtils.invokeMethod(associatePropertyDescriptor.getWriteMethod(), main, associates);
    }

    /**
     * 设置关联对象，单个主实体对象和多个关联对象
     *
     * @param main                主实体对象
     * @param associateProperty   关联属性
     * @param associateSource     关联对象源
     * @param associateIdProperty 关联主键属性
     * @param <M>                 主实体类，Main首字母
     * @param <A>                 关联实体类，Associate首字母
     * @param <I>                 关联实体主键类，Id首字母
     */
    public static <M, A, I> void setAssociateCollection(M main, String associateProperty, CollectionAssociateSource<I, A> associateSource, String associateIdProperty) {
        PropertyDescriptor associateIdPropertyDescriptor = BeanUtils.getPropertyDescriptor(main.getClass(), associateIdProperty);
        if (associateIdPropertyDescriptor == null) throwPropertyNotFoundException(main.getClass(), associateIdProperty);
        Collection<I> idCollection = (Collection<I>) ReflectionUtils.invokeMethod(associateIdPropertyDescriptor.getReadMethod(), main);
        if (CollectionUtils.isEmpty(idCollection)) return;
        Collection<A> associate = associateSource.getCollectionById(idCollection);
        setAssociateCollection(main, associateProperty, associate);
    }

    /**
     * 设置关联对象，多个主实体对象和单个关联对象
     *
     * @param mains               主实体对象
     * @param associateProperty   关联属性
     * @param associateMap        关联对象
     * @param associateIdProperty 关联主键属性
     * @param <M>                 主实体类，Main首字母
     * @param <A>                 关联实体类，Associate首字母
     * @param <I>                 关联实体主键类，Id首字母
     */
    public static <M, A, I> void setAssociate(Collection<M> mains, String associateProperty, Map<I, A> associateMap, String associateIdProperty) {
        Class<? extends M> mainClass = detectElementType(mains);
        PropertyDescriptor associateIdDescriptor = BeanUtils.getPropertyDescriptor(mainClass, associateIdProperty);
        if (associateIdDescriptor == null) throwPropertyNotFoundException(mainClass, associateIdProperty);
        PropertyDescriptor associateDescriptor = BeanUtils.getPropertyDescriptor(mainClass, associateProperty);
        if (associateDescriptor == null) throwPropertyNotFoundException(mainClass, associateProperty);
        mains.forEach(main -> {
            I associateId = (I) ReflectionUtils.invokeMethod(associateIdDescriptor.getReadMethod(), main);
            ReflectionUtils.invokeMethod(associateDescriptor.getWriteMethod(), main, associateMap.get(associateId));
        });
    }

    /**
     * 设置关联对象，多个主实体对象和单个关联对象
     *
     * @param mains               主实体对象
     * @param associateProperty   关联属性
     * @param associateSource     关联对象源
     * @param associateIdProperty 关联主键属性
     * @param <M>                 主实体类，Main首字母
     * @param <A>                 关联实体类，Associate首字母
     * @param <I>                 关联实体主键类，Id首字母
     */
    public static <M, A, I> void setAssociate(Collection<M> mains, String associateProperty, CollectionAssociateSource<I, A> associateSource, String associateIdProperty, String idProperty) {
        Class<? extends M> mainClass = detectElementType(mains);
        PropertyDescriptor associateIdDescriptor = BeanUtils.getPropertyDescriptor(mainClass, associateIdProperty);
        if (associateIdDescriptor == null) throwPropertyNotFoundException(mainClass, associateIdProperty);
        Set<I> associateIds = mains.stream().map(main -> (I) ReflectionUtils.invokeMethod(associateIdDescriptor.getReadMethod(), main)).collect(Collectors.toSet());
        if (associateIds.isEmpty()) return;
        Collection<A> associates = associateSource.getCollectionById(associateIds);
        setAssociate(mains, associateProperty, propertyAsKey(associates, idProperty), associateIdProperty);
    }

    /**
     * 设置关联对象，多个主实体对象和单个关联对象
     *
     * @param mains               主实体对象
     * @param associateProperty   关联属性
     * @param associateMap        关联对象
     * @param associateIdProperty 关联主键属性
     * @param <M>                 主实体类，Main首字母
     * @param <A>                 关联实体类，Associate首字母
     * @param <I>                 关联实体主键类，Id首字母
     */
    public static <M, A, I> void setAssociateCollection(Collection<M> mains, String associateProperty, Map<Collection<I>, Collection<A>> associateMap, String associateIdProperty) {
        Class<? extends M> mainClass = detectElementType(mains);
        PropertyDescriptor associateIdDescriptor = BeanUtils.getPropertyDescriptor(mainClass, associateIdProperty);
        if (associateIdDescriptor == null) throwPropertyNotFoundException(mainClass, associateIdProperty);
        PropertyDescriptor associateDescriptor = BeanUtils.getPropertyDescriptor(mainClass, associateProperty);
        if (associateDescriptor == null) throwPropertyNotFoundException(mainClass, associateProperty);
        mains.forEach(main -> {
            Collection<I> idCollection = (Collection<I>) ReflectionUtils.invokeMethod(associateIdDescriptor.getReadMethod(), main);
            ReflectionUtils.invokeMethod(associateDescriptor.getWriteMethod(), main, associateMap.get(idCollection));
        });
    }

    /**
     * 设置关联对象，多个主实体对象和单个关联对象
     *
     * @param mains               主实体对象
     * @param associateProperty   关联属性
     * @param associateSource     关联对象源
     * @param associateIdProperty 关联主键属性
     * @param <M>                 主实体类，Main首字母
     * @param <A>                 关联实体类，Associate首字母
     * @param <I>                 关联实体主键类，Id首字母
     */
    public static <M, A, I> void setAssociateCollection(Collection<M> mains, String associateProperty, CollectionAssociateSource<I, A> associateSource, String associateIdProperty, String idProperty) {
        Class<? extends M> mainClass = detectElementType(mains);
        PropertyDescriptor associateIdDescriptor = BeanUtils.getPropertyDescriptor(mainClass, associateIdProperty);
        if (associateIdDescriptor == null) throwPropertyNotFoundException(mainClass, associateIdProperty);
        Set<Collection<I>> associateCollectionIds = mains.stream().map(main -> ((Collection<I>) ReflectionUtils.invokeMethod(associateIdDescriptor.getReadMethod(), main))).collect(Collectors.toSet());
        if (associateCollectionIds.isEmpty()) return;
        Set<I> associateIds = associateCollectionIds.stream().flatMap(Collection::stream).collect(Collectors.toSet());
        Collection<A> associates = associateSource.getCollectionById(associateIds);
        Map<I, A> associateMap = propertyAsKey(associates, idProperty);
        Map<Collection<I>, Collection<A>> collect = associateCollectionIds.stream().collect(Collectors.toMap(Function.identity(), _associateIds -> _associateIds.stream().map(associateMap::get).collect(Collectors.toList())));
        setAssociateCollection(mains, associateProperty, collect, associateIdProperty);
    }

}
