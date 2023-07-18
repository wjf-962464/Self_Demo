package com.wjf.self_demo

import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.thread

fun main() {
    /*val v = ReadWriteLockExample()
    repeat(15) {
        thread {
            Thread.sleep((Math.random() * 1000).toLong())
            if (Math.random() > 0.4f) {
                println("thread${Thread.currentThread().id} read :${v.readValue()}")
            } else {
                val n = (Math.random()*10).toInt()
                v.writeValue(n)
                println("thread${Thread.currentThread().id} write :$n")
            }
        }
    }*/

    val example = ConditionExample()

    // 创建多个生产者和消费者线程
    repeat(20) { example.producer() }
    repeat(20) { example.consumer() }
}

class ReadWriteLockExample {
    private val lock = ReentrantReadWriteLock()
    private var value: Int = 0

    fun writeValue(newValue: Int) {
        lock.writeLock().lock()
        try {
            // 写操作，对共享数据进行更新
            value = newValue
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun readValue(): Int {
        lock.readLock().lock()
        try {
            // 读操作，对共享数据进行读取
            return value
        } finally {
            lock.readLock().unlock()
        }
    }
}

class ConditionExample {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    private var sharedResource = 0

    fun producer() {
        thread {
            lock.lock()
            try {
                println("生产1")
                // 生产者线程等待共享资源小于10
                if (sharedResource >= 10) {
                    println("生产竞争锁")
                    condition.await()
                }
                // 生产资源
                sharedResource++
                println("Producing resource: $sharedResource")

                // 唤醒一个等待中的消费者线程
                condition.signal()
            } finally {
                lock.unlock()
            }
        }
    }

    fun consumer() {
        thread {
            lock.lock()
            try {
                println("消费1")
                // 消费者线程等待共享资源大于0
                if (sharedResource <= 0) {
                    println("消费竞争锁")
                    condition.await()
                }

                // 消费资源
                println("Consuming resource: $sharedResource")
                sharedResource--

                // 唤醒一个等待中的生产者线程
                condition.signal()
            } finally {
                lock.unlock()
            }
        }
    }
}

/*
fun quickSort(arr: IntArray, low: Int, high: Int) {
    if (low >= high) {
        return
    }
    val index = low
    val flag = arr[index]
    var l = low
    var r = high
    while (l < r) {
        while (l < r && arr[r] > flag) {
            r--
        }
        while (l < r && arr[l] <= flag) {
            l++
        }
        if (l < r) {
            swap(arr, l, r)
        }
    }
    arr[index] = arr[r]
    arr[r] = flag
    quickSort(arr, low, r - 1)
    quickSort(arr, r + 1, high)
}

fun swap(arr: IntArray, i: Int, j: Int) {
    val temp = arr[i]
    arr[i] = arr[j]
    arr[j] = temp
}

fun main() {
    val arr = intArrayOf(64, 34, 25, 12, 22, 11, 90)
    val n = arr.size
    quickSort(arr, 0, n - 1)
    println("排序后的数组：")
    for (i in 0 until n) {
        print("${arr[i]} ")
    }
}

fun quickSort(arr: IntArray, low: Int, high: Int) {
    if (low < high) {
        val pivotIndex = partition(arr, low, high)
        quickSort(arr, low, pivotIndex - 1)
        quickSort(arr, pivotIndex + 1, high)
    }
}

fun partition(arr: IntArray, low: Int, high: Int): Int {
    val pivot = arr[high]
    var i = low - 1
    for (j in low until high) {
        if (arr[j] < pivot) {
            swap(arr, ++i, j)
        }
    }
    swap(arr, ++i, high)
    return i
}

fun swap(arr: IntArray, i: Int, j: Int) {
    val temp = arr[i]
    arr[i] = arr[j]
    arr[j] = temp
}

fun main() {
    val arr = intArrayOf(64, 34, 25, 12, 22, 11, 90, 22)
    val n = arr.size
    quickSort(arr, 0, n - 1)
    println("排序后的数组：")
    for (i in 0 until n) {
        print("${arr[i]} ")
    }
}
*/
