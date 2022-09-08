package com.example.eunoia.cache

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class CacheManager {
    private val MAX_SIZE = 5242880L // 5MB

    //private fun CacheManager() {}

    @Throws(IOException::class)
    fun cacheData(context: Context, data: ByteArray, name: String?) {
        val cacheDir: File = context.getCacheDir()
        val size = getDirSize(cacheDir)
        val newSize = data.size + size
        if (newSize > MAX_SIZE) {
            cleanDir(cacheDir, newSize - MAX_SIZE)
        }
        val file = name?.let { File(cacheDir, it) }
        val os = FileOutputStream(file)
        try {
            os.write(data)
        } finally {
            os.flush()
            os.close()
        }
    }

    @Throws(IOException::class)
    fun retrieveData(context: Context, name: String?): ByteArray? {
        val cacheDir: File = context.getCacheDir()
        val file = name?.let { File(cacheDir, it) }
        if (file != null) {
            if (!file.exists()) {
                // Data doesn't exist
                return null
            }
        }
        val data = file?.length()?.let { ByteArray(it.toInt()) }
        val `is` = FileInputStream(file)
        `is`.use { `is` ->
            `is`.read(data)
        }
        return data
    }

    private fun cleanDir(dir: File, bytes: Long) {
        var bytesDeleted: Long = 0
        val files: Array<File> = dir.listFiles()
        for (file in files) {
            bytesDeleted += file.length()
            file.delete()
            if (bytesDeleted >= bytes) {
                break
            }
        }
    }

    private fun getDirSize(dir: File): Long {
        var size: Long = 0
        val files: Array<File> = dir.listFiles()
        for (file in files) {
            if (file.isFile()) {
                size += file.length()
            }
        }
        return size
    }
}