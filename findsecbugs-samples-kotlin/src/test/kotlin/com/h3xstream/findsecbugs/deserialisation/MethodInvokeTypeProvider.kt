package com.h3xstream.findsecbugs.deserialisation

import org.springframework.util.ReflectionUtils
import java.io.IOException
import java.io.ObjectInputStream
import java.lang.reflect.Method
import java.lang.reflect.Type


/**
 * Inspired by the gadget : SerializableTypeWrapper.MethodInvokeTypeProvider.readObject
 * Ref: org.springframework:spring-beans:4.1.4.RELEASE
 */
class MethodInvokeTypeProvider(private val provider: TypeProvider, method: Method, private val index: Int) : TypeProvider {
    private val methodName: String = method.name

    @Transient
    private var result: Any? = null

    override val type: Type
        get() =
            if (this.result !is Type && this.result != null) (this.result as Array<*>)[this.index] as Type else this.result as Type

    override val source: Any
        get() = Any()

    init {
        this.result = ReflectionUtils.invokeMethod(method, provider.type)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inputStream: ObjectInputStream) {
        inputStream.defaultReadObject()
        val method = ReflectionUtils.findMethod(this.provider.type.javaClass, this.methodName)
        this.result = ReflectionUtils.invokeMethod(method, this.provider.type)
    }


}