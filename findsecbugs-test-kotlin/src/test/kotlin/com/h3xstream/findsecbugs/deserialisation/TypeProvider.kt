package com.h3xstream.findsecbugs.deserialisation

import java.io.Serializable
import java.lang.reflect.Type

interface TypeProvider : Serializable {
    val type: Type

    val source: Any
}