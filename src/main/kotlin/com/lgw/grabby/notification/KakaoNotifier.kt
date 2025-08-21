package com.lgw.grabby.notification

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.MediaType
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Component
@Lazy(false)
@ConditionalOnProperty(matchIfMissing = true, prefix = "notification.kakao", name = ["enabled"], havingValue = "true")
class KakaoNotifier(
    @Value("\${notification.kakao.access-token:}")
    private val accessToken: String
) : Notifier {

    private val logger = KotlinLogging.logger {}

    private val webClientBuilder: WebClient.Builder = WebClient.builder()
        .codecs { configurer ->
            configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024) // 16MB
        }

    private val client = webClientBuilder
        .baseUrl("https://kapi.kakao.com")
        .defaultHeader("Authorization", "Bearer $accessToken")
        .build()

    override fun notify(subject: String, message: String) {
        require(accessToken.isNotBlank()) { "Kakao access token is not configured" }

        val text = "[${subject.take(80)}]\n${message}".take(990)
        val templateObject = """
            {
              "object_type": "text",
              "text": ${json(text)},
              "link": {"web_url":"https://example.com","mobile_web_url":"https://example.com"}
            }
        """.trimIndent()

        logger.info { "Kakao send start. subject='${subject.take(80)}', token=${mask(accessToken)}" }

        val response = client.post()
            .uri("/v2/api/talk/memo/default/send")
            .contentType(MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8"))
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromFormData("template_object", templateObject))
            .exchangeToMono { res ->
                res.bodyToMono(String::class.java).defaultIfEmpty("").map { body ->
                    Triple(res.statusCode().value(), res.headers().asHttpHeaders().toSingleValueMap(), body)
                }
            }
            .block()

        if (response == null) {
            logger.error { "Kakao send failed: no response (block() returned null)" }
            return
        }

        val (status, headers, body) = response
        if (status in 200..299) {
            logger.info { "Kakao send success. status=$status, body=$body" }
        } else {
            logger.error { "Kakao send error. status=$status, headers=$headers, body=$body" }
        }
    }

    private fun json(s: String): String =
        "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\""

    private fun mask(token: String): String =
        if (token.length <= 8) "****" else token.take(4) + "****" + token.takeLast(4)
}