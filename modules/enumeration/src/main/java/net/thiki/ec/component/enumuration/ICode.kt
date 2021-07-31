package net.thiki.ec.component.enumuration

import net.thiki.ec.component.exception.unexpectedError


interface ICode{
    val code: Int
}

fun <T: ICode> enumsToMap(values: Array<T>): Map<Int, T> {
    return values.map { it.code to it }.toMap()
}

inline fun <T: ICode> valueOfCode(all: Map<Int, T>, code: Int): T{
    return all[code] ?: unexpectedError("The code[$code] is not supported.")
}