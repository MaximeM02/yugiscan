package com.improcorp.myapplication.model

class InitialHashMap {
    companion object {
        var templateHashMap: LinkedHashMap<Int, String>? = null

        override fun toString(): String {
            val buffer = StringBuffer()

            templateHashMap!!.forEach {
                buffer.append("key: ")
                    .append(it.key)
                    .append(", value: ")
                    .append(it.value)
                    .append("\n")
            }

            return buffer.toString()
        }
    }
}