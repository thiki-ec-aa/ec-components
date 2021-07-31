package net.thiki.ec.component.common.utils

import kotlin.math.min


object Collections {
    /**
     * toList("aStr, with commas", String::toInt),
     */
    fun <T> splitToList(
            str: String,
            block: (String) -> T = { it -> it as T }
    ): List<T> {
        return splitToList(str, ",", block)
    }

    fun <T> splitToList(
            str: String,
            delimiters: String,
            block: (String) -> T = { it -> it as T }
    ): List<T> {
        return if (str.isEmpty()) {
            emptyList()
        } else {
            str.split(delimiters).map {
                block(it)
            }.toList()
        }
    }

    fun splitToIntList(str: String, separator: String): List<Int> {
        return splitToList(str, separator, String::toInt)
    }

    fun splitToLongList(str: String, separator: String): List<Long> {
        return splitToList(str, separator, String::toLong)
    }

    fun splitToIntList(str: String): List<Int> {
        return splitToList(str, String::toInt)
    }

    fun splitToLongList(str: String): List<Long> {
        return splitToList(str, String::toLong)
    }

    fun splitToStringList(str: String): List<String> {
        return splitToList(str, String::toString)
    }

    fun splitToStringList(str: String, delimiters: String): List<String> {
        return splitToList(str, delimiters, String::toString)
    }

    fun <K, V> toMap(obj: Any?, toKey: ((String) -> K), toValue: ((String) -> V)): Map<K, V> {
        val s = obj as String
        if (s.isEmpty()) return emptyMap()
        return s.split(",").map {
            val sp = it.split(":")
            toKey(sp[0]) to toValue(sp[1])
        }.toMap()
    }

    fun <K, V> toMutableMap(s: Any?, toKey: ((String) -> K), toValue: ((String) -> V)): MutableMap<K, V> {
        return toMap(s, toKey, toValue).toMutableMap()
    }

    fun toMap(s: String): Map<Int, Long> {
        return toMap(s, String::toInt, String::toLong)
    }

    fun toIntMap(s: String): Map<Int, Int> {
        return toMap(s, String::toInt, String::toInt)
    }

    fun mapToStr(map: Map<*, *>): String {
        if (map.isEmpty()) return ""
        return map.map { (key, value) ->
            "$key:$value"
        }.joinToString(separator = ",")
    }

    fun mapToString(map: Map<*, *>): String {
        return map.entries.joinToString(",", "mapOf[", "]") {
            "(${recrusiveToString(it.key)}: ${recrusiveToString(it.value)})"
        }

    }

    /**
     * 打乱list顺序
     */
    fun <T> shuffleList(randomGenerator: RandomGenerator, list: List<T>): List<T>{
        val remainingIndices = list.indices.toMutableList()
        val resultList = mutableListOf<T>()
        for (i in list.indices){
            val choice = randomGenerator.whichLucky(remainingIndices.size)
            val chosenIndex = remainingIndices.removeAt(choice)
            resultList.add(list[chosenIndex])
        }
        return resultList
    }

    fun collectionToString(collection: Collection<*>): String {
        return collection.joinToString(",", "listOf[", "]") {
            recrusiveToString(it)
        }
    }

    /**
     * 追根究底的toString，递归处理list和map
     */
    fun recrusiveToString(it: Any?): String {
        return when {
            it is Collection<*> -> {
                collectionToString(it)
            }
            it is Map<*, *> -> {
                mapToString(it)

            }
            else -> {
                it.toString()
            }
        }
    }

    /**
     * 分批次处理列表T，映射为V
     */
    fun <T, V> batchMap(batchSize: Int, list: List<T>, block: ((List<T>) -> List<V>)): MutableList<V> {
        val count = list.size / batchSize + 1
        val result = mutableListOf<V>()
        for (i in 0 until count) {
            val subList = list.subList(i * batchSize, min(list.size, (i + 1) * batchSize))
            result.addAll(block(subList))
        }
        return result
    }
}






