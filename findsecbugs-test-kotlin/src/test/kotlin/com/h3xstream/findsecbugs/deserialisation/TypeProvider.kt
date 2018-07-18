package org.vulnerable.kotlin

import java.io.Serializable
import java.lang.reflect.Type

interface TypeProvider : Serializable {
    val type: Type

    val source: Any
}