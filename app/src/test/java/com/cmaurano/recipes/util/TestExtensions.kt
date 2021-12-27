package com.cmaurano.recipes.util

import org.junit.Assert
import java.io.FileNotFoundException
import java.net.URL

fun readFile(path: String): String {
    val content: URL? = ClassLoader.getSystemResource(path)

    return content?.readText() ?: throw FileNotFoundException("file path: $path was not found")
}

infix fun Any.shouldBe(expected: Any) = Assert.assertEquals(expected, this)