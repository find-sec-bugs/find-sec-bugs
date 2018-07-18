package com.h3xstream.findsecbugs.deserialisation

import org.apache.commons.io.input.ClassLoaderObjectInputStream

import java.io.*

class ObjectDeserialization {

    @Throws(IOException::class, ClassNotFoundException::class)
    fun deserializeObject(receivedFile: InputStream): UserEntity {
        val `in` = ObjectInputStream(receivedFile)
        try {
            return `in`.readObject() as UserEntity
        } finally {
            `in`.close()
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun deserializeObjectWithInheritance(receivedFile: InputStream): UserEntity {
        val `in` = ClassLoaderObjectInputStream(javaClass.classLoader, receivedFile)
        try {
            return `in`.readObject() as UserEntity
        } finally {
            `in`.close()
        }
    }
}