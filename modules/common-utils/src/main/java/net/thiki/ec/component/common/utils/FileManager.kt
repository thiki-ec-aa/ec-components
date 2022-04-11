package net.thiki.ec.component.common.utils

import net.thiki.ec.component.exception.AssertionException
import net.thiki.ec.component.exception.unexpectedError
import net.thiki.ec.component.log.logger
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FilenameFilter
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Load file from file system instead of from classpath resources.
 *
 * Based on the [root] directory.
 */
open class FileManager(root: String) {

    open val fileStorageLocation: Path = Paths.get(root).toAbsolutePath().normalize()

    init {
        logger.info("[FileManager] - Init with root: $root")
    }

    /** files storage root */

    /**
     * 从文件系统目录读取
     */
    private fun loadFileFromFS(fileName: String): BufferedReader? {
        val filePath = fileStorageLocation.resolve(fileName).normalize()
        val file = filePath.toFile()
        logger.debug("try to load file from fs, fileName: ${file.absolutePath}")
        return if (file.exists()) {
            try {
                BufferedReader(InputStreamReader(FileInputStream(file), "UTF-8"))
            } catch (ex: Exception) {
                logger.error("failed, exception occurred.", ex)
                null
            }
        } else {
            logger.debug("failed, the file is not found.")
            null
        }
    }

    /**
     * 从classpath读
     * @param path example: "config/skill1001.xml"
     */
    private fun loadFileFromResource(path: String): BufferedReader? {
        //这里因为windows/max/linux系统差异可能异常类型很多,所以确实需要try-catch
        val resourceAsStream = try {
            FileManager::class.java.classLoader.getResourceAsStream(path)
        } catch (e: Exception) {
            logger.error("Error: ${e.message}", e)
            null
        }
        return resourceAsStream?.let { BufferedReader(InputStreamReader(resourceAsStream, "UTF-8")) }
    }

    /**
     * - type1: classpath resource
     * - type2: file system path relative to the root, see [fileStorageLocation]
     * @param paths mapOf(isClassPathResource to path)
     */
    fun loadFileAsReader(paths: Map<Boolean, String>): BufferedReader? {
        var reader: BufferedReader? = null
        for ((isClassPathResource, path) in paths) {
            reader = if (isClassPathResource) {
                loadFileFromResource(path)
            } else {
                loadFileFromFS(path)
            }
            if (reader != null) {
                //found
                break
            }
        }
        return reader
    }

    fun writeByteArrayToFile(pathString: String, fileName: String, bytes: ByteArray) {
        val parentPath = fileStorageLocation.resolve(pathString).normalize()
        createDirectoryIfAbsent(parentPath)
        val filePath = fileStorageLocation.resolve("$pathString/$fileName").normalize()
        logger.debug("writing the file: ${filePath.fileName}.")
        if (filePath.toFile().exists()) {
            logger.warn("Fail to write a file. The file[${filePath.fileName}] already exists.")
        } else {
            try {
                Files.write(filePath, bytes)
            } catch (ex: Exception) {
                logger.error("Error: ", ex)
                throw AssertionException("Could not create the directory where the uploaded files will be stored.")
            }
        }
    }

    private fun createDirectoryIfAbsent(filePath: Path) {
        if (!filePath.toFile().exists()) {
            try {
                Files.createDirectories(filePath)
            } catch (ex: Exception) {
                logger.error("Error: ", ex)
                throw AssertionException("Could not create the directory where the uploaded files will be stored.")
            }
        }
    }

    /**
     * 读取相对路径[path]下所有指定文件，一次性读取多个文件，仅用于测试环境环境
     * @return fileName to ByteArray
     */
    fun readFilesAsByteArray(path: String, filter: FilenameFilter = FilenameFilter { _, _ -> true }): Map<String, ByteArray> {
        val filePath = fileStorageLocation.resolve(path).normalize().toFile()
        if (!filePath.exists()) {
            return emptyMap()
        }
        val fileNames = filePath.list(filter)!!
        return try {
            fileNames.map { fileName ->
                val file = fileStorageLocation.resolve("$path/$fileName").normalize()
                fileName to Files.readAllBytes(file)
            }.toMap()
        } catch (e: Exception) {
            logger.error("Error: ${e.message}", e)
            unexpectedError(e.message ?: "no ex msg")
        }
    }

    /**
     * 删除指定[path]和指定规则[filter]的所有文件， 危险方法，仅在测试用
     */
    fun clearPath(path: String, filter: FilenameFilter = FilenameFilter { _, _ -> true }) {
        val filePath = fileStorageLocation.resolve(path).normalize().toFile()
        if (!filePath.exists()) {
            return
        }
        val fileNames = filePath.list(filter)!!
        return try {
            fileNames.forEach { fileName ->
                val file = fileStorageLocation.resolve("$path/$fileName").normalize().toFile()
                if (file.exists()) {
                    file.delete()
                    logger.debug("The file[${file.name} is deleted.")
                }
            }
        } catch (e: Exception) {
            logger.error("Error: ${e.message}", e)
            unexpectedError(e.message ?: "no ex msg")
        }
    }

    fun writeTextToFile(pathString: String, fileName: String, text: String) {
        val parentPath = fileStorageLocation.resolve(pathString).normalize()
        createDirectoryIfAbsent(parentPath)
        val filePath = fileStorageLocation.resolve("$pathString/$fileName").normalize()
        logger.debug("writing the file: ${filePath.fileName}.")
        if (filePath.toFile().exists()) {
            logger.warn("Fail to write a file. The file[${filePath.fileName}] already exists.")
        } else {
            try {
                Files.write(filePath, text.toByteArray(charset = Charset.forName("UTF-8")))
            } catch (ex: Exception) {
                logger.error("Error: ", ex)
                throw AssertionException("Could not create the directory where the uploaded files will be stored.")
            }
        }
    }

    fun createOrUpdateTextFile(path: String, fileName: String, content: String) {
        val parentPath = fileStorageLocation.resolve(path).normalize()
        createDirectoryIfAbsent(parentPath)
        val filePath = fileStorageLocation.resolve("$path/$fileName").normalize()
        try {
            if (filePath.toFile().exists()) {
                // 文件存在，比较覆盖。
                val oldContent = String(Files.readAllBytes(filePath), Charset.forName("UTF-8"))
                if (content != oldContent) {
                    Files.write(filePath, content.toByteArray(Charset.forName("UTF-8")), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
                }
            } else {
                // 文件不存在，直接创建。
                Files.write(filePath, content.toByteArray(Charset.forName("UTF-8")), StandardOpenOption.CREATE)
            }
        } catch (ex: Exception) {
            logger.error("Error: ", ex)
            unexpectedError("[FileManager] - createOrUpdateTextFile failed.")
        }
    }

    fun createOrUpdateBytesFile(path: String, fileName: String, bytes: ByteArray) {
        val parentPath = fileStorageLocation.resolve(path).normalize()
        createDirectoryIfAbsent(parentPath)
        val filePath = fileStorageLocation.resolve("$path/$fileName").normalize()
        try {
            Files.write(filePath, bytes)
        } catch (ex: Exception) {
            logger.error("Error: ", ex)
            unexpectedError("[FileManager] - createOrUpdateBytesFile failed.")
        }
    }

    fun deleteFile(path: String, fileName: String) {
//        val parentPath = fileStorageLocation.resolve(path).normalize()
        val filePath = fileStorageLocation.resolve("$path/$fileName").normalize()
        try {
            if (filePath.toFile().exists()) {
                Files.delete(filePath)
            }
        } catch (ex: Exception) {
            logger.error("Error: ", ex)
            unexpectedError("[FileManager] - deleteFile failed.")
        }
    }

    companion object {
        private val logger = logger<FileManager>()
    }
}