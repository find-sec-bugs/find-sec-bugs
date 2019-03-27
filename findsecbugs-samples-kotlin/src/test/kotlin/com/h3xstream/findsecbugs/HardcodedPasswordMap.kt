package com.h3xstream.findsecbugs

import java.util.*

class HardcodedPasswordMap {

    fun `hardcoded password`() {
        val map = mapOf("password" to "secret1234")
    }

    fun `hardcoded password with secret key varient`() {
        val map = mapOf("database.password" to "secret1234")
    }

    fun `hardcoded password with constant key`() {
        val map = mapOf(CAMEL_CASE_KEY_P4SS to "secret1234")
    }
    fun `hardcoded password in map with plus`() {
        val map = HashMap<String, String>()
        map.plus("password" to "secret1234")
    }

    fun `hardcoded password in map with index`() {
        val map = HashMap<String, String>()
        map["password"] = "secret1234"
    }

    fun `hardcoded password in map with set`() {
        val map = HashMap<String, String>()
        map.set("password", "secret1234")
    }

    fun safe(input: String) {
        val map1 = mapOf("password" to input)
        val map2 = mapOf("database.password" to input)
        val map3 = mapOf(CAMEL_CASE_KEY_P4SS to input)
        val map4 = Properties()
        map4.setProperty(CAMEL_CASE_KEY_P4SS, input)
    }

    companion object {
        const val CAMEL_CASE_KEY_P4SS = "KeyStore.PassWord"
    }
}