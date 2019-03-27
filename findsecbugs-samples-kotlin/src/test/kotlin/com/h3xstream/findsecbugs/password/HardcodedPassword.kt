package com.h3xstream.findsecbugs.password


abstract class EqualsPasswordField {

    fun hardcodedLogin1(username: String, password: String): Boolean {

        if (username == "admin") {
            println("OK")
        }
        if (username == "abc") {
            println("OK")
        }

        if (password == "@dm1n") { //!!
            return true
        }

        return validateDb(username, password)
    }

    fun hardcodedLogin2(username: String, password: String): Boolean {

        return if ("adm1nL3ft" == password) { //!!
            true
        } else validateDb(username, password)

    }

    fun hardcodedLogin3(username: String, p1: String): Boolean {

        val password = p1
        return if ("adm1nL3ft!!!!" == password) { //!! (Not supported at the moment)
            true
        } else validateDb(username, password)

    }

    fun safeLogin1(username: String, password: String): Boolean {

        return if (password == "") {
            true
        } else validateDb(username, password)

    }

    fun safeLogin2(username: String, password: String): Boolean {

        return if ("" == password) {
            false
        } else validateDb(username, password)

    }

    fun safeLogin3(username: String, password: String): Boolean {

        return if (getPassword(username) == password) {
            false
        } else validateDb(username, password)

    }

    fun safeLogin4(username: String, password: String): Boolean {

        return if (password == getPassword(username)) {
            false
        } else validateDb(username, password)

    }

    abstract fun validateDb(username: String, password: String): Boolean
    abstract fun getPassword(username: String): String
}

fun hardcodedLogin1(username: String, password: String): Boolean {

    if (username == "admin") {
        println("OK")
    }
    if (username == "abc") {
        println("OK")
    }

    if (password == "@dm1n") { //!!
        return true
    }

    return validateDb(username, password)
}

fun hardcodedLogin2(username: String, password: String): Boolean {

    return if ("adm1nL3ft" == password) { //!!
        true
    } else validateDb(username, password)

}

fun hardcodedLogin3(username: String, p1: String): Boolean {

    val password = p1
    return if ("adm1nL3ft!!!!" == password) { //!! (Not supported at the moment)
        true
    } else validateDb(username, password)

}

fun safeLogin1(username: String, password: String): Boolean {

    return if (password == "") {
        true
    } else validateDb(username, password)

}

fun safeLogin2(username: String, password: String): Boolean {

    return if ("" == password) {
        false
    } else validateDb(username, password)

}

fun safeLogin3(username: String, password: String): Boolean {

    return if (getPassword(username) == password) {
        false
    } else validateDb(username, password)

}

fun safeLogin4(username: String, password: String): Boolean {

    return if (password == getPassword(username)) {
        false
    } else validateDb(username, password)

}

fun validateDb(username: String, password: String): Boolean {
    return false
}

fun getPassword(username: String): String {
    throw IllegalStateException("Not implemented")
}
