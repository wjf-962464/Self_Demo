package com.wjf.self_demo.util

import com.orhanobut.logger.Logger

object DataCodeUtil {
    val encryptionTable = mutableListOf('$', '-', '_', '+', '!', '*', '\'', '(', ')', ',')
    val decryptionTable by lazy {
        val map = mutableMapOf<Char, Char>()
        encryptionTable.forEachIndexed { index, c -> map[c] = '0' + index }
        map
    }

    @ExperimentalStdlibApi
    fun dataEncode(string: String?): String {
        if (string.isNullOrEmpty()) {
            throw IllegalArgumentException("input string is null or empty")
        }
        val iterator = string.toCharArray().iterator()
        val stringBuilder = StringBuilder()
        while (iterator.hasNext()) {
            var char = iterator.nextChar()
            when (char.code) {
                in 0x30..0x39 -> {
                    char = encryptionTable[(char - 0x30).code]
                }
                in 0x41..0x5A -> {
                    char += 4
                    char.takeIf { it.code > 0x5A }?.let {
                        char -= 26
                    }
                }
                in 0x61..0x7A -> {
                    char -= 5
                    char.takeIf { it.code < 0x61 }?.let {
                        char += 26
                    }
                }
                else -> {
                    Logger.e("DataCodeUtil dataDecode has illegal char")
                }
            }
            stringBuilder.append(char)
        }
        return stringBuilder.toString()
    }

    @ExperimentalStdlibApi
    fun dataDecode(string: String?): String {
        if (string.isNullOrEmpty()) {
            throw IllegalArgumentException("input string is null or empty")
        }
        val iterator = string.toCharArray().iterator()
        val stringBuilder = StringBuilder()
        while (iterator.hasNext()) {
            var char = iterator.nextChar()
            when (char.code) {
                in 0x41..0x5A -> {
                    char -= 4
                    char.takeIf { it.code < 0x41 }?.let {
                        char += 26
                    }
                }
                in 0x61..0x7A -> {
                    char += 5
                    char.takeIf { it.code > 0x7A }?.let {
                        char -= 26
                    }
                }
                else -> {
                    char = decryptionTable[char] ?: char
                }
            }
            stringBuilder.append(char)
        }
        return stringBuilder.toString()
    }
}
