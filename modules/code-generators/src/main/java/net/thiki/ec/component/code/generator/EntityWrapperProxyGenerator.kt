package net.thiki.ec.component.code.generator

/**
 *
 */
class EntityWrapperProxyGenerator {

    fun <T> generateJavaCode(entity: EntityClassMeta<T>): String{
        return "";
    }

    fun generateJavaCode(classGroup: ClassGroup): String{
        return "";
    }
}

/**
 * 一组class
 */
data class ClassGroup(
    val classList: List<EntityClassMeta<*>>,
    val packageName: String
)

/**
 * 某个Entity
 */
data class EntityClassMeta<T>(
    val clazz: Class<T>,
    val fields: Map<String, FieldMeta<*>> = emptyMap()
)

/**
 *
 */
data class FieldMeta<T>(
    val clazz: Class<T>
)

