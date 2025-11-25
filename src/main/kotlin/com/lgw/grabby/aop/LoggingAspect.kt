package com.lgw.grabby.aop

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.UUID

@Aspect
@Component
class LoggingAspect {

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val MDC_TRACE_ID = "traceId"
        private const val MDC_THREAD_ID = "threadId"
        private const val HEADER_TRACE_ID = "X-Trace-Id"

        /**
         * UUID v4 기반 traceId 생성
         */
        fun generateTraceIdV4(): String =
            UUID.randomUUID().toString() // RFC 4122 version 4
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    fun logAroundController(joinPoint: ProceedingJoinPoint): Any? {
        val servletRequestAttributes =
            RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes

        val existingTraceId = servletRequestAttributes
            ?.request
            ?.getHeader(HEADER_TRACE_ID)

        // UUID v4 패턴 적용
        val traceId = existingTraceId ?: generateTraceIdV4()
        val threadId = Thread.currentThread().id.toString()

        MDC.put(MDC_TRACE_ID, traceId)
        MDC.put(MDC_THREAD_ID, threadId)

        try {
            val request = servletRequestAttributes?.request
            logger.info {
                "REQUEST start " +
                        "traceId=$traceId, threadId=$threadId, " +
                        "method=${request?.method}, uri=${request?.requestURI}, " +
                        "handler=${joinPoint.signature.declaringType.simpleName}.${joinPoint.signature.name}"
            }

            val start = System.currentTimeMillis()
            val result = joinPoint.proceed()
            val elapsed = System.currentTimeMillis() - start

            servletRequestAttributes?.response?.setHeader(HEADER_TRACE_ID, traceId)

            logger.info {
                "REQUEST end   " +
                        "traceId=$traceId, threadId=$threadId, elapsedMs=$elapsed, " +
                        "handler=${joinPoint.signature.declaringType.simpleName}.${joinPoint.signature.name}"
            }

            return result
        } catch (ex: Throwable) {
            logger.error(ex) {
                "REQUEST error traceId=$traceId, threadId=$threadId, " +
                        "handler=${joinPoint.signature.declaringType.simpleName}.${joinPoint.signature.name}"
            }
            throw ex
        } finally {
            MDC.remove(MDC_TRACE_ID)
            MDC.remove(MDC_THREAD_ID)
        }
    }
}
