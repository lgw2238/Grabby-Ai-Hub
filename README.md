# Grabby-Ai-Hub

HUB SERVER, which provides a RAG-based inference API. made by lgw

## 디렉토리 구조 예시

```
.
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── (핵심 서버 코드)
│   │   └── resources/
│   │       └── (설정 파일 등)
│   └── test/
│       └── (테스트 코드)
├── build.gradle.kts
├── README.md
└── 기타 프로젝트 설정 파일
```

## 프로젝트 스펙 요약

- **언어:** Kotlin 100%
- **주요 기능:**  
  - RAG(Retrieval-Augmented Generation) 기반 인퍼런스 API 제공
  - 다양한 AI 모델 연동 및 추론 지원
  - RESTful API 설계
- **타겟:** RAG 기반 AI 서비스 백엔드 허브
- **개발/빌드:** Gradle 기반
