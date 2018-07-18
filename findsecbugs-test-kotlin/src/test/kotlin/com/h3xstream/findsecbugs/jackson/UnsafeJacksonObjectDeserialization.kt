package com.h3xstream.findsecbugs.jackson

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper

class UnsafeJacksonObjectDeserialization {

    internal class ABean {
        var id: Int = 0
        var obj: Any? = null
    }

    internal class AnotherBean {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
        var obj: Any? = null
    }

    internal class YetAnotherBean {
        @JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
        var obj: Any? = null
    }

    @Throws(Exception::class)
    fun exampleOne(JSON: String) {
        val mapper = ObjectMapper()
        mapper.enableDefaultTyping()
        val obj = mapper.readValue(JSON, ABean::class.java)
    }

    @Throws(Exception::class)
    fun exampleTwo(JSON: String) {
        val mapper = ObjectMapper()
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS)
        val obj = mapper.readValue(JSON, ABean::class.java)
    }

    @Throws(Exception::class)
    fun exampleThree(JSON: String) {
        val mapper = ObjectMapper()
        val obj = mapper.readValue(JSON, AnotherBean::class.java)
    }

    @Throws(Exception::class)
    fun exampleFour(JSON: String) {
        val mapper = ObjectMapper()
        val obj = mapper.readValue(JSON, YetAnotherBean::class.java)
    }

}