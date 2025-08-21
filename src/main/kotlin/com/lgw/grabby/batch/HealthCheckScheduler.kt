package com.lgw.grabby.batch

import com.lgw.grabby.monitor.AiHealthChecker
import com.lgw.grabby.notification.CompositeNotifier
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class HealthCheckScheduler(
    private val checker: AiHealthChecker,
    private val notifier: CompositeNotifier,
    @Value("\${health.check.notify-on-success:false}")
    private val notifyOnSuccess: Boolean
) {
    private val logger = KotlinLogging.logger {}
    // 고정 지연(밀리초) 설정 값을 사용 - 12시간
    @Scheduled(fixedDelayString = "\${health.check.interval-ms:43200000}")
    fun run() {
        logger.info{ "run : healthCheckScheduler" }
        val results = checker.checkAll()
        val hasIssue = results.any { !it.status }

        val summary = buildString {
            appendLine("AI Health Check @ ${Instant.now()}")
            results.forEach {
                val status = if (it.status) "OK" else "FAIL"
                appendLine("- ${it.provider}: $status (${it.latencyMs}ms)${it.error?.let { e -> " - $e" } ?: ""}")
            }
        }.trimEnd()

        /**
         *  Health Check 결과가 문제가 있는 경우에만 알림 전송
         *  해당 분기를 제거하면, 결과에 대한 응답 발송.
         */
        if (hasIssue || notifyOnSuccess) {
            val subject = if (hasIssue) "AI 헬스체크 경고" else "AI 헬스체크 정상"
            notifier.notify(subject, summary)
        }
    }
}