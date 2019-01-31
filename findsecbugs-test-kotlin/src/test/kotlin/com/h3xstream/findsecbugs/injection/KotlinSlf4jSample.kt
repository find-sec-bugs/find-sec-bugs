package com.h3xstream.findsecbugs.injection

import org.slf4j.Logger

class KotlinSlf4jSample {

    fun slf4j(log: Logger, tainted: DataClass, tainted2: String) {
        val safe = ""
        //Unsafe
        log.info(tainted.input)
        log.info(tainted.input, safe)
        log.info(safe, tainted.input)
        log.info(safe, arrayOf<Any>(tainted.input!!))
        log.info(safe, safe, tainted.input)
        log.info(tainted2)
        log.info(tainted2, safe)
        log.info(safe, tainted2)
        log.info(safe, arrayOf<Any>(tainted2))
        log.info(safe, safe, tainted2)

        // safe

        log.info(safe, arrayOf<Any>(""))
    }

    inner class DataClass {
        var input: String? = null
    }
}