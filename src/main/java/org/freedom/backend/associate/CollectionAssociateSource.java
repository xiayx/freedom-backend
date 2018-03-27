package org.freedom.backend.associate;

import java.util.Collection;

/**
 * 关联对象源，提供实体对象集合
 *
 * @param <I> 主键
 * @param <E> 实体类
 * @author xiayx
 * @see AssociateSource
 */
public interface CollectionAssociateSource<I,E> {

    /** 根据主键获取实体对象集合 */
    Collection<E> getCollectionById(Collection<I> id);


}
