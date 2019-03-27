package com.h3xstream.findsecbugs.injection

import javax.servlet.http.HttpServletRequest
import java.util.ResourceBundle
import java.util.function.Supplier
import java.util.logging.*

class KotlinLogging {


    fun javaUtilLogging() {
        val tainted = req!!.getParameter("test")
        val safe = "safe"
        val logger = Logger.getLogger(KotlinLogging::class.java.name)
        logger.level = Level.ALL
        val handler = ConsoleHandler()
        handler.level = Level.ALL
        logger.addHandler(handler)

        logger.config(tainted)
        logger.entering(tainted, safe)
        logger.entering("safe", safe, tainted)
        logger.entering(safe, "safe", arrayOf(tainted))
        logger.exiting(safe, tainted)
        logger.exiting(safe, "safe", tainted)
        logger.fine(tainted)
        logger.finer(tainted.trim { it <= ' ' })
        logger.finest(tainted)
        logger.info(tainted)
        logger.log(Level.INFO, tainted)
        logger.log(Level.INFO, tainted, safe)
        logger.log(Level.INFO, "safe", arrayOf(tainted))
        logger.log(Level.INFO, tainted, Exception())
        logger.logp(Level.INFO, tainted, safe, "safe")
        logger.logp(Level.INFO, safe, "safe", tainted, safe)
        logger.logp(Level.INFO, "safe", safe.toLowerCase(), safe, arrayOf(tainted))
        logger.logp(Level.INFO, tainted, safe, safe, Exception())
        logger.logp(Level.INFO, tainted, "safe", null as Supplier<String>?)
        logger.logp(Level.INFO, "safe", tainted, Exception(), null as Supplier<String>?)
        logger.logrb(Level.INFO, safe, safe, null as ResourceBundle?, "safe", tainted)
        logger.logrb(Level.INFO, tainted, safe, null as ResourceBundle?, safe, Exception())
        logger.logrb(Level.INFO, tainted, safe, "bundle", safe)
        logger.logrb(Level.INFO, safe, tainted, "bundle", safe, safe)
        logger.logrb(Level.INFO, tainted, "safe", "bundle", safe, arrayOf(safe))
        logger.logrb(Level.INFO, safe, safe, "bundle", tainted, Exception())
        logger.severe(tainted + "safe" + safe)
        logger.throwing("safe", tainted.replace('\r', ' '), Exception()) // still insecure (LF not replaced)
        logger.warning(tainted.replace("\n".toRegex(), "")) // still insecure (CR not replaced)

        // these should not be reported
        logger.fine(safe)
        logger.log(Level.INFO, "safe".toUpperCase(), safe + safe)
        logger.logp(Level.INFO, safe, safe, safe, arrayOf(safe))
        logger.logrb(Level.INFO, safe, safe, tainted + "bundle", safe) // bundle name can be tainted
        logger.throwing(safe, safe, Exception())
        logger.info(tainted.replace('\n', ' ').replace('\r', ' '))
        var encoded = tainted.replace("\r", "").toUpperCase()
        encoded = "safe" + encoded.toLowerCase()
        logger.warning(encoded.replace("\n", " (new line)"))
        logger.fine(tainted.replace("[\r\n]+".toRegex(), ""))
    }

    companion object {
        var req: HttpServletRequest? = null
    }

}