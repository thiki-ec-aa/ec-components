package net.thiki.ec.component.code.generator

import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import freemarker.template.Template
import net.thiki.ec.component.exception.assertThat
import java.io.StringWriter
import java.lang.Character.isUpperCase
import java.lang.reflect.Field


/**
 *
 */
class EntityWrapperProxyGenerator(
    private val templatePath: String? = null,
    private val templateName: String? = null,
    private val outputRoot: String? = null
) {



    fun <T> generateJavaCode(entity: EntityClassMeta<T>): String{

        // freemarker 配置
        val configuration = Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS)

        configuration.defaultEncoding = "UTF-8"
        val defaultDir = "/net/thiki/ec/component/code/generator"
        configuration.templateLoader = ClassTemplateLoader(
            javaClass,
            templatePath ?: defaultDir
        )

        // 根据模板名称获取路径下的模板
        val template: Template = configuration.getTemplate(templateName ?: "entity-class.ftl")
        // 处理路径问题


        // 定义一个输出流来导出代码文件
//        val outputStreamWriter = OutputStreamWriter(FileOutputStream(entity.calOutFileName(outputRoot ?: "c:$defaultDir/generated")))
        val stringWriter = StringWriter()
        // freemarker 引擎将动态数据绑定的模板并导出为文件

        template.process(entity.toDataModel(), stringWriter)

//        template.process(entity, outputStreamWriter)
//        outputStreamWriter.flush()

        return stringWriter.toString()
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
 * [clazz] : 某个需要代理的类
 * [includedFields] : 如果为空，则所有public的getter setter都包含在内；否则只尝试包含[includedFields]内的字段
 * [excludedFields] : 如果不为空，则排除[excludedFields]内的字段，其他所有public的getter setter都包含在内
 */
data class EntityClassMeta<T>(
    val clazz: Class<T>,
    val includedFields: Set<String> = emptySet(),
    val excludedFields: Set<String> = emptySet(),
    val targetPackage: String? = clazz.`package`.name
){
    fun calOutFileName(root: String): String{
        val clazzName = clazz.simpleName
       return "$root/$clazzName$EXT"
    }

    /**
     * 生成template使用的DataModel
     */
    fun toDataModel(): Map<String, Any>{
        val filteredFields = getAllFields()
        val imports = filteredFields.values.map {
            "import ${it.javaClass.canonicalName}"
        }.sorted()

        val fields = filteredFields.map {(fieldName, fieldType) ->
            fieldName to fieldType
        }.toMap()

        return mapOf(
            "package" to targetPackage!!,
            "className" to clazz.simpleName!!,
            "imports" to imports,
            "fields" to fields
        )
    }

    /**
     * 按javabean规范，从public方法里筛出成对的getter和setter，作为一个field，输出其名字和类型
     */
    private fun getAllFields(): MutableMap<String, Class<*>> {
//        return clazz.fields.filter { hasField(it) }
        val methodMap = clazz.methods.associateBy { it.name }

        val map = mutableMapOf<String, Class<*>>()
        for (method in clazz.methods) {
            val methodName = method.name
            if (methodName.startsWith("get") || methodName.startsWith("is")){
                val fieldName = extractFieldNameFromGetter(methodName)
                if (methodMap.containsKey(buildSetterName(fieldName))){
                    map[fieldName] = method.returnType
                }
            }
        }
        return map
    }

    private fun buildSetterName(fieldName: String): String {
        TODO("Not yet implemented")
    }

    private fun extractFieldNameFromGetter(getterName: String): String {
//        if (getterName)
        assertThat(getterName.isNotEmpty())
        val first = getterName[0]
        if (getterName.length == 1){
            return first.lowercase()
        }
        val second = getterName[1]

        if (isUpperCase(first) && isUpperCase(second)){
            return ""
        }

        return getterName.substring(2, 3).lowercase() + getterName.substring(3, getterName.length)
    }

    private fun isGetterOrSetter(name: String): Boolean {
        return name.startsWith("get")
                || name.startsWith("set")
                || name.startsWith("is")
    }

    private fun hasField(field: Field): Boolean {
        return if (excludedFields.contains(field.name)) {
            false
        } else {
            if (includedFields.isEmpty()) {
                true
            } else {
                includedFields.contains(field.name)
            }
        }
    }

    companion object{
        const val EXT = ".java"
    }
}
