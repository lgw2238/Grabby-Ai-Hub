package com.lgw.grabby.notification

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Profile
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "notification.email", name = ["enabled"], havingValue = "true")
@Profile("prod") // 프로덕션 환경에서만 활성화
class EmailNotifier(
    private val mailSender: JavaMailSender,
    @Value("\${notification.email.from}")
    private val from: String,
    @Value("\${notification.email.to}")
    private val to: String
) : Notifier {

    override fun notify(subject: String, message: String) {
        val mail = SimpleMailMessage().apply {
            setFrom(from)
            setTo(to.toString())
            setSubject(subject)
            setText(message)
        }
        mailSender.send(mail)
    }
}