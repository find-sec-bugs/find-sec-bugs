package com.h3xstream.findsecbugs.deserialisation

import org.apache.commons.collections4.Transformer
import java.io.Serializable
import java.lang.reflect.InvocationTargetException

class InvokerTransformer<I, O> : Transformer<I, O>, Serializable {

    private val iMethodName: String
    private val iParamTypes: Array<Class<*>>
    private val iArgs: Array<Any>

    constructor(methodName: String) {
        this.iMethodName = methodName
        this.iParamTypes = emptyArray()
        this.iArgs = emptyArray()
    }

    constructor(methodName: String, paramTypes: Array<Class<*>>, args: Array<Any>) {
        this.iMethodName = methodName
        this.iParamTypes = paramTypes.clone()
        this.iArgs = args.clone()
    }

    override fun transform(input: I): O {
        try {
            val ex = (input as Any).javaClass
            val method = ex.getMethod(this.iMethodName, *this.iParamTypes)
            return method.invoke(input, *this.iArgs) as O
        } catch (var4: NoSuchMethodException) {
            throw RuntimeException("InvokerTransformer: The method \'" + this.iMethodName + "\' on \'" + (input as Any).javaClass + "\' does not exist")
        } catch (var5: IllegalAccessException) {
            throw RuntimeException("InvokerTransformer: The method \'" + this.iMethodName + "\' on \'" + (input as Any).javaClass + "\' cannot be accessed")
        } catch (var6: InvocationTargetException) {
            throw RuntimeException("InvokerTransformer: The method \'" + this.iMethodName + "\' on \'" + (input as Any).javaClass + "\' threw an exception", var6)
        }

    }

    companion object {
        private const val serialVersionUID = -8653385846894047688L

        fun <I, O> invokerTransformer(methodName: String): Transformer<I, O> {
            return InvokerTransformer(methodName)
        }

        fun <I, O> invokerTransformer(methodName: String, paramTypes: Array<Class<*>>, args: Array<Any>): Transformer<I, O> {
            return if (paramTypes.size != args.size) {
                throw IllegalArgumentException("The parameter types must match the arguments")
            } else {
                if (paramTypes.isNotEmpty()) InvokerTransformer(methodName, paramTypes, args) else InvokerTransformer(methodName)
            }
        }
    }
}
