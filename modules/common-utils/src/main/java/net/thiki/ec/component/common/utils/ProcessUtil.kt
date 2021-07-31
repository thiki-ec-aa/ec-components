package net.thiki.ec.component.common.utils

import java.lang.management.ManagementFactory

object ProcessUtil{
    fun getProcessID(): Int {
        val runtimeMXBean = ManagementFactory.getRuntimeMXBean()
        println(runtimeMXBean.name)
        return Integer.valueOf(runtimeMXBean.name.split("@").toTypedArray()[0])
                .toInt()
    }
}