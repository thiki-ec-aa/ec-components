package net.thiki.ec.component.code.generator

import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import freemarker.template.Template
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.StringWriter


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

        template.process(entity, stringWriter)

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
    val includedFields: Map<String, FieldMeta<*>> = emptyMap(),
    val excludedFields: Map<String, FieldMeta<*>> = emptyMap(),
){
    fun calOutFileName(root: String): String{
        val clazzName = clazz.simpleName
       return "$root/$clazzName$EXT"
    }

    companion object{
        const val EXT = ".java"
    }
}

/**
 *
 */
data class FieldMeta<T>(
    val clazz: Class<T>
)

