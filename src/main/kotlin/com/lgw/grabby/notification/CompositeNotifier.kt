package com.lgw.grabby.notification

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class CompositeNotifier(
    private val notifiers: List<Notifier>,
    @Value("\${management.metrics.tags.env:local}")
    private val env: String
) : Notifier {

    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun init() {
        val names = if (notifiers.isEmpty()) "[]" else notifiers.joinToString(", ") { it::class.simpleName ?: "anon" }
        logger.info { "CompositeNotifier initialized. env=$env, delegates=[$names]" }
    }

    override fun notify(subject: String, message: String) {
        if (notifiers.isEmpty()) {
            logger.warn { "No Notifier beans found. Skip notify. env=$env" }
            return
        }
        notifiers.forEach { runCatching { it.notify(subject, message) } }
    }
}