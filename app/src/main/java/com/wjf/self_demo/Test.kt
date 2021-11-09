package com.wjf.self_demo

import android.util.Log

/*
 * 版权所有(C) 2015 永辉云创O2O 。保留所有权利。
 */
interface KeepAttr
open class Animal {
    var uuid: Int? = null

    init {
        uuid = 20
        println("父类构造静态")
    }
}

open class Person(
    var age: Int?,
    var name: String?,
    var pets: MutableList<out Pet>? = mutableListOf()

) : KeepAttr {
    constructor() : this(null, null, null)

    init {
        Log.i("abc", "init")
        println("println>:>init")
    }

    open class Pet() : KeepAttr, Animal() {
        var type: String? = null

        constructor(type: String) : this() {
            this.type = type
        }
    }
}

class Son(val parent: String, val dog: MutableList<Dog>) : KeepAttr, Person()
class Dog(val size: Float? = null) : KeepAttr, Person.Pet() {
    init {
        println("Dog init")
    }
}
// class Dog(val size: Float?) : KeepAttr, Person.Pet()
