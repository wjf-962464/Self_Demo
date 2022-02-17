package com.wjf.self_demo

abstract class Parent {
    open val adapter: Animal by lazy {
        setAdapter()
    }
    val parentName: String by lazy {
        setParentName()
    }

    abstract fun setParentName(): String
    abstract fun setAdapter(): Animal

    init {
        println("父类构造$parentName--${adapter?.uuid}")
    }
}

class Son(val sonName: String?) : Parent() {

    override fun setParentName(): String {
        return sonName ?: "爸爸"
    }

    override fun setAdapter(): Animal {
        val pet = Person.Pet()
        pet.uuid = 0
        return pet
    }
}
