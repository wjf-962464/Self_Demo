package org.jxxy.debug.corekit.recyclerview

interface ViewHolderTag<T> {
    /**
     * holder操作
     *
     * @param entity 数据实体
     */
    fun setHolder(entity: T)

    /**
     * holder操作，含payload
     *
     * @param entity 数据实体
     */
    fun setHolder(
        entity: T,
        payload: Any
    )
}
