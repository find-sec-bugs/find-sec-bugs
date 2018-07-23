package com.h3xstream.findsecbugs.jackson

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class JacksonSerialisationFalsePositive : Serializable {

    internal class Bean {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
        var obj: Any? = null
    }

    @Throws(Exception::class)
    fun exampleOne(JSON: String) {
        val mapper = ObjectMapper()
        val obj = mapper.readValue(JSON, JacksonSerialisationFalsePositive::class.java)
    }

    @Throws(Exception::class)
    fun exampleTwo(JSON: String) {
        val mapper = ObjectMapper()
        val obj = mapper.readValue(JSON, Bean::class.java)
    }
}