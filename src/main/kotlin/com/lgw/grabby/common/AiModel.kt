package com.lgw.grabby.common

object AiModel {
    // OpenAI
    const val GPT_3_5_TURBO = "gpt-3.5-turbo"
    const val GPT_4 = "gpt-4"
    const val GPT_4_TURBO = "gpt-4-turbo"
    const val LLM_MATH = "llm-math"
    const val LLM_CODE_INTERPRETER = "llm-code-interpreter"
    const val LLM_DALL_E = "llm-dall-e"

    // Anthropic
    const val CLAUDE_V3_HAIKU = "claude-3-haiku-20240307"
    const val CLAUDE_V3_SONNET = "claude-3-sonnet-20240229"
    const val CLAUDE_V3_OPUS = "claude-3-opus-20240229"

    // Ollama
    const val LLAMA3 = "llama3"
    const val LLAMA3_8B = "llama3:8b-instruct"
    const val LLAMA3_70B = "llama3:70b-instruct"
    const val LLAMA3_1_8B = "llama3.1:8b-instruct"
    const val LLAMA3_1_70B = "llama3.1:70b-instruct"

    // Mistral 계열
    const val MISTRAL_7B = "mistral:7b-instruct"
    const val MIXTRAL_8X7B = "mixtral:8x7b-instruct"

    // 기타 다국어 강한 모델
    const val QWEN2_5_14B = "qwen2.5:14b-instruct"
    const val PHI3_MINI = "phi3:mini"
    const val SOLAR_10_7B = "solar:10.7b-instruct"
    const val YI_34B_CHAT = "yi:34b-chat"

    // 임베딩
    const val NOMiC_EMBED_TEXT = "nomic-embed-text"
    const val ALL_MINILM_22M = "all-minilm:22m"
    const val MXBAI_EMBED_LARGE = "mxbai-embed-large"

    val gptModels = listOf(
        GPT_3_5_TURBO,
        GPT_4,
        GPT_4_TURBO,
        LLM_MATH,
        LLM_CODE_INTERPRETER,
        LLM_DALL_E,
    )

    val anthropicModels = listOf(
        CLAUDE_V3_HAIKU,
        CLAUDE_V3_SONNET,
        CLAUDE_V3_OPUS
    )

    val llamaModels = listOf(
        LLAMA3,
        LLAMA3_70B,
        LLAMA3_8B,
        LLAMA3_1_70B,
        LLAMA3_1_8B
    )

}