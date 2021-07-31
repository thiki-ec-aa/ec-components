package net.thiki.ec.component.common.utils

import net.thiki.ec.component.exception.unexpectedError

object JDBCMapping{
    fun toSqlDateTime(time: Long): java.sql.Timestamp? {
        return if (time <= 0){
            null
        }else{
            java.sql.Timestamp(time)
        }
    }

    @Deprecated("use sqlDateTimeToLong(row, fieldName) instead.", replaceWith = ReplaceWith("sqlDateTimeToLong(row, fieldName) ") )
    fun sqlDateTimeToLong(dateTime: Any?): Long{
        return (dateTime as java.sql.Timestamp?)?.time ?: -1
    }

    fun sqlDateTimeToLong(row: Map<String, Any?>, fieldName: String): Long{
        if (!row.containsKey(fieldName)){
            unexpectedError("row[$fieldName] not found!")
        }
        return (row[fieldName] as java.sql.Timestamp?)?.time ?: -1
    }

    fun toStr(params: List<Int>): String {
        return params.joinToString(separator = ",")
    }
}