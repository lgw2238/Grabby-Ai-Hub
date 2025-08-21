package com.lgw.grabby.common

enum class AiProvider(val value: String) {
    OPENAI("OPENAI(gpt)"),
    ANTHROPIC("ANTHROPIC(claude)"),
    OLLAMA("OLLAMA(llama3)");

    companion object {
        private val map = values().associateBy(AiProvider::value)
        fun fromValue(value: String): AiProvider? = map[value]
    }
}