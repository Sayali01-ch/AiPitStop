# AiPitStop Backend

Spring Boot backend providing simple AI endpoints and PDF extraction examples.

Environment variables
- GROQ_API_KEY - Groq LLM API key (used by `/api/ai/text`)
- STABILITY_API_KEY - Stability AI key (used by `/api/ai/image`)
- STABILITY_API_URL - Optional base URL for Stability API

Build & run
- mvn package
- java -jar target/aipitstop-backend-0.1.0.jar

Run tests
- mvn test

Notes
- The `AiService` contains placeholder calls to Groq and Stability APIs; update request URIs and payloads to match provider docs and set `GROQ_API_KEY` / `STABILITY_API_KEY` as env variables.

Redirect behavior
- The backend root `/` now redirects to the frontend SPA by default at `http://localhost:3000/`.
- Override using `FRONTEND_URL` environment variable when running (e.g., `-e FRONTEND_URL=https://app.example.com/`).

Docker
- docker build -t aipitstop-backend .
- docker run -p 8080:8080 -e GROQ_API_KEY=... -e STABILITY_API_KEY=... aipitstop-backend
