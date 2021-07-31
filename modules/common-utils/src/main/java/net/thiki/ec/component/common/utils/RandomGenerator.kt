package net.thiki.ec.component.common.utils

import net.thiki.ec.component.exception.unexpectedError
import net.thiki.ec.component.log.logger
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadLocalRandom

interface RandomGenerator {
    /**
     * 对指定的概率，返回一次随机结果
     * @param probability 0..1, 1表示百分百发生
     * @return true指发生， false指未发生
     */
    fun isLucky(probability: Float): Boolean

    /**
     * 对指定的概率列表
     * @param probs 事件的概率列表，每个概率均在 0..1
     * @return  0表示都不发生，  当n=1..probs.size时，表示第n个概率对应的事件发生了
     */
    fun whichLucky(probs: Array<Float>): Int

    /**
     * 对于指定的权重列表，选取其中一个发生。 必然有且只有一个事件发生
     * @return choice 0..weights.size -1
     */
    fun whichLuckyWithWeight(weights: List<Int>): Int

    /**
     * 对指定的size，返回平均律的随机结果，例如，size=4，则返回0,1,2,3的概率是一样的
     */
    fun whichLucky(size: Int): Int

    /**
     * 标准圆桌算法：
     * 按照传入参数概率顺序优先占位[0,1],超出1的部分会被舍去
     * 如果传入总概率不足1,则认为剩余概率表示前者都未触发
     * @return 触发事件的对应传入概率参数下标,-1表示均未触发
     */
    fun roundTable(probs: List<Float>): Int

    /**
     * 根据[W]的weight，从[map]里挑选[count]个
     *
     * see [whichLuckyWithWeight], [Weightable]
     */
    fun <K, W : Weightable> chooseFromMapWithWeights(map: Map<K, W>, count: Int): List<W>

    /**
     * 根据[W]的weight，从[list]里挑选[count]个
     *
     * see [whichLuckyWithWeight], [Weightable]
     *
     */
    fun <W : Weightable> chooseFromListWithWeights(list: List<W>, count: Int): List<W>

    fun <W : Weightable> chooseFromListWithWeightsAndRepeatable(list: List<W>, count: Int): List<W>

    /**
     * 从[mList]中以相同概率取出n项
     */
    fun chooseNFromList(n: Int, mList: List<Int>): List<Int>
}


/**
 * 可重复的随机数产生器，通过输入相同的seed来得到相同的随机数序列
 */
interface RepeatableRandomGenerator : RandomGenerator {
    val seed: Long
}

interface Weightable {
    val weight: Int
}

/**
 * 用数组来模拟随机数产生
 *
 * 数组的值被循环使用
 */
class RandomList(
        val randomList: List<Double>
) {
    var i = 0
    val getRandom: () -> Double = {
        val r = randomList[i]
        i = (i + 1) % randomList.size
        r
    }
}


typealias SessionIdHolder = () -> Long?

/**
 * Singleton
 *
 * 目的：回放录制的报文时，能确定当时所用的seed
 *
 * 每次command开始前初始化，command结束后销毁
 *
 * 注意其局限性：如果新开了线程就无效了
 *
 * @param isSessionMode true的时候才生效，否则退化为普通的SystemRandomGenerator
 */
class CommandScopeRandomGenerator(
        val isSessionMode: Boolean = false,
        private val sessionIdHolder: SessionIdHolder
) : RandomGenerator {

    private val seedMap = ConcurrentHashMap<Long, Long>()
    private val generatorMap = ConcurrentHashMap<Long, RandomGenerator>()
    private val systemRandomGenerator = SystemRandomGenerator()

    /**
     * 创建一个新的随机数产生器，或覆盖已有的， 在command before切面执行
     */
    fun replaceRandomGenerator(sessionId: Long, restoredSeedId: Long? = null): Long {
        val seed = restoredSeedId ?: ThreadLocalRandom.current().nextLong()
        seedMap[sessionId] = seed
        generatorMap[sessionId] = RepeatableSystemRandomGenerator(seed)
//        loggerFrom(this).debug("Session $sessionId random seed is set: $seed")
        return seed
    }

    /**
     * 清除指定session的随机数产生器，在command after切面执行
     */
    fun removeRandomGenerator(sessionId: Long) {
        seedMap.remove(sessionId)
        generatorMap.remove(sessionId)
//        loggerFrom(this).debug("Session $sessionId random seed is removed.")
    }

    /**
     * 可以记录seed
     */
    fun getSeed(): Long {
        val sessionId = sessionIdHolder()
        return seedMap[sessionId]
                ?: unexpectedError("must in a valid session and command.sessionId=$sessionId. Thread: ${Thread.currentThread().name}")
    }

    ////////////// proxy to real instance
    private val randomGenerator: RandomGenerator
        get() {
            return if (isSessionMode) {
                val sessionId = sessionIdHolder()
                sessionId?.let { generatorMap[it] } ?: systemRandomGenerator
            } else {
                systemRandomGenerator
            }
        }

    override fun isLucky(probability: Float): Boolean {
        return randomGenerator.isLucky(probability)
    }

    override fun whichLucky(probs: Array<Float>): Int {
        return randomGenerator.whichLucky(probs)
    }

    override fun whichLuckyWithWeight(weights: List<Int>): Int {
        return randomGenerator.whichLuckyWithWeight(weights)
    }

    override fun whichLucky(size: Int): Int {
        return randomGenerator.whichLucky(size)
    }

    override fun roundTable(probs: List<Float>): Int {
        return randomGenerator.roundTable(probs)
    }

    override fun <K, W : Weightable> chooseFromMapWithWeights(map: Map<K, W>, count: Int): List<W> {
        return randomGenerator.chooseFromMapWithWeights(map, count)
    }

    override fun <W : Weightable> chooseFromListWithWeights(list: List<W>, count: Int): List<W> {
        return randomGenerator.chooseFromListWithWeights(list, count)
    }

    override fun <W : Weightable> chooseFromListWithWeightsAndRepeatable(list: List<W>, count: Int): List<W> {
        return randomGenerator.chooseFromListWithWeightsAndRepeatable(list, count)
    }

    override fun chooseNFromList(n: Int, mList: List<Int>): List<Int> {
        return randomGenerator.chooseNFromList(n, mList)
    }
}

/**
 * 可重复的随机数产生器，通过输入相同的seed来得到相同的随机数序列
 */
class RepeatableSystemRandomGenerator(
        override val seed: Long,
        random: Random = Random(seed)
) : SystemRandomGenerator({ random.nextDouble() }), RepeatableRandomGenerator


open class SystemRandomGenerator(
        var randomFun: (() -> Double) = { ThreadLocalRandom.current().nextDouble() }
) : RandomGenerator {

    private fun randomValue(): Double {
        return randomFun()
//                .apply {
//            logger.debug("randomValue = $this")
//        }
    }

    override fun roundTable(probs: List<Float>): Int {

        val random = randomValue()
        var nowSign = 0f

        for (i in 0 until probs.size) {

            nowSign += probs[i]

            if (nowSign > random) {
                //logger.debug("roundTable? $i. out of [${recrusiveToString(probs)}]")
                return i
            }

        }

        return -1
    }

    override fun whichLuckyWithWeight(weights: List<Int>): Int {
        val all = weights.sum()
        var accumulated = 0.toDouble()
        var choice = 0

        val random = randomValue() * all
        for (i in 0 until weights.size) {
            accumulated += weights[i]
            choice = i
            if (random < accumulated) {
                break
            }
        }
        return choice
//                .apply {
//            logger.debug("whichLuckyWithWeight? $this. out of [${recrusiveToString(weights)}]")
//        }
    }

    override fun chooseNFromList(n: Int, mList: List<Int>): List<Int> {
        var remaining = mList.size
        val list = mList.toMutableList()
        var count = n
        val result = mutableListOf<Int>()
        while (count > 0) {
            val choice = whichLucky(remaining)
            val value = list[choice]
            list.removeAt(choice)
            result.add(value)
            count--
            remaining--
        }
        return result
    }

    override fun <W : Weightable> chooseFromListWithWeights(list: List<W>, count: Int): List<W> {
        val choiceList = mutableListOf<W>()

        val vList = list.toMutableList()
        val weightList = list.map { it.weight }.toMutableList()

        for (j in 0 until count) {
            val choice = this.whichLuckyWithWeight(weightList)
            weightList.removeAt(choice)
            val weightable = vList.removeAt(choice)
            choiceList.add(weightable)
        }
        if (choiceList.toSet().size != choiceList.size) {
            unexpectedError("ids repeated: ${choiceList.joinToString { "," }}")
        }
        return choiceList
    }

    override fun <W : Weightable> chooseFromListWithWeightsAndRepeatable(list: List<W>, count: Int): List<W> {
        val choiceList = mutableListOf<W>()

        val vList = list.toMutableList()
        val weightList = list.map { it.weight }.toMutableList()

        for (j in 0 until count) {
            val choice = this.whichLuckyWithWeight(weightList)
            val weightable = vList[choice]
            choiceList.add(weightable)
        }
        return choiceList
    }

    override fun <K, W : Weightable> chooseFromMapWithWeights(map: Map<K, W>, count: Int): MutableList<W> {
        val choiceList = mutableListOf<W>()

        val vList = mutableListOf<W>()
        val weightList = mutableListOf<Int>()
        for (value in map.values) {
            val weight = value.weight
            vList.add(value)
            weightList.add(weight)
        }

        for (j in 0 until count) {
            val choice = this.whichLuckyWithWeight(weightList)
            weightList.removeAt(choice)
            val config = vList.removeAt(choice)
            choiceList.add(config)
        }
        return choiceList
    }

    /**
     * @param probability 概率, 0 to 1
     */
    override fun isLucky(probability: Float): Boolean {
        val random = randomValue()

        return (random < probability)
//                .apply {
//                    logger.debug("isLucky? $this. random=$random out of probability[$probability]")
//                }
    }


    override fun whichLucky(size: Int): Int {
        // size = 4,  r= 0.8   0.8/ 0.25 = 3
        val random = randomValue()
        val share = 1f / size
        return Math.floor(random / share).toInt()
//                .apply{
//            logger.debug("whichLucky? $this. out of size[$size]")
//        }
    }

    override fun whichLucky(probs: Array<Float>): Int {
        var otherProb = 1f
        var all = 0f
        probs.forEach {
            otherProb *= 1f - it
            all += it
        }

        return if (isLucky(otherProb)) {
            0
        } else {
            val random = randomValue()

            var choice = 1
            var sum = 0f
            for (i in 0 until probs.size) {
                sum += probs[i]
                choice = i + 1
                if (random < sum / all) {
                    // 0 leave for otherProb already.
                    break
                }
            }
            choice
        }
//                .apply {
//            if (logger.isDebugEnabled) {
//                logger.debug("whichLucky? chose [$this] out of probability[${arrToString(probs)}]")
//            }
//        }
    }

    private fun arrToString(arr: Array<out Any?>): String {
        val sb = StringBuilder()
        sb.append("array[")
        arr.forEach {
            sb.append(it).append(",")
        }
        sb.append("]")
        return sb.toString()
    }

    companion object {
        private val logger = logger<SystemRandomGenerator>()

        /**
         * 随机数中float转为int
         * 约定概率中float支持最多6位
         */
        fun randomFloat2Int(f: Float): Int {
            return (f * 1000000).toInt()
        }
    }
}

