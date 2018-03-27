package org.freedom.backend.associate;

/**
 * 关联对象源，用于提供关联对象
 *
 * @param <I> 主键类型，Id的首字母
 * @param <E> 实体类型，Entity的首字母
 * @author xiayx
 * @see CollectionAssociateSource
 */
public interface AssociateSource<I, E> {

    /** 根据主键获取实体对象 */
    E getById(I id);

}
