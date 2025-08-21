package com.lgw.grabby.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import io.github.oshai.kotlinlogging.KotlinLogging

@Component
class HttpLoggingFilter : OncePerRequestFilter() {
    private val logger = KotlinLogging.logger {}
    private val warnThresholdMs = 2000L

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val start = System.currentTimeMillis()
        try {
            filterChain.doFilter(request, response)
        } finally {
            val took = System.currentTimeMillis() - start
            val msg = "HTTP ${request.method} ${request.requestURI} -> ${response.status} (${took}ms)"
            if (took >= warnThresholdMs) {
                logger.warn { "[SLOW] $msg" }
            } else {
                //logger.info { "$msg" }
            }
        }
    }
}