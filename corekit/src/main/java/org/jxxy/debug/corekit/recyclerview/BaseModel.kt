package org.jxxy.debug.corekit.recyclerview

abstract class BaseModel {
    companion object {
        private const val SINGLE_TYPE = 0
        private const val RATE = 31
    }

    fun viewType(): Int = SINGLE_TYPE

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as? BaseModel
        return viewType() == that?.viewType()
    }

    override fun hashCode(): Int {
        val result: Long = (RATE * viewType()).toLong()
        return result.toInt()
    }
}
