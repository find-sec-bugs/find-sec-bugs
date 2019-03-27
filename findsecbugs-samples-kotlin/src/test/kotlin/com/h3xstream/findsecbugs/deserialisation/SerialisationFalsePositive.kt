package com.h3xstream.findsecbugs.deserialisation

import java.io.IOException
import java.io.ObjectInputStream
import java.io.Serializable

class SerialisationFalsePositive : Serializable {
    internal var param1: Int = 0
    private var param2: Any? = null

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inputStream: ObjectInputStream) {
        param1 = inputStream.readInt()
        param2 = inputStream.readObject()
    }
}