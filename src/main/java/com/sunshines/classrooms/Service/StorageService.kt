package com.sunshines.classrooms.Service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile

import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Timestamp

@Service
class StorageService {
    private val rootLocation = Paths.get("D:/upload")
    fun store(file: MultipartFile): String {
        var fileName = System.currentTimeMillis().toString() + file.originalFilename!!
        fileName = fileName.replace("\\s+".toRegex(), "")
        try {
            Files.copy(file.inputStream, this.rootLocation.resolve(fileName))
        } catch (e: Exception) {
            throw RuntimeException("FAIL!")
        }

        return fileName
    }

    fun loadFile(filename: String): Resource {
        try {
            val file = rootLocation.resolve(filename)
            val resource = UrlResource(file.toUri())
            return if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw RuntimeException("FAIL!")
            }
        } catch (e: MalformedURLException) {
            throw RuntimeException("FAIL!")
        }

    }

    fun init() {
        try {
            Files.createDirectory(rootLocation)
        } catch (e: IOException) {
            throw RuntimeException("Could not initialize storage!")
        }

    }
}

