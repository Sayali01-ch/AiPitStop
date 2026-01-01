# AiPitStop

Unified AI Platform scaffold (Spring Boot backend + React frontend)

This repository contains a scaffolded full-stack application demonstrating:
- Spring Boot backend with example endpoints for Groq LLM and Stability AI
- PDF processing using Apache PDFBox
- React SPA frontend (Vite) that calls backend APIs
- Dockerfiles and docker-compose to run services together
- GitHub Actions for CI (build & test)

See the `backend/` and `frontend/` folders for details and instructions.

Quick start (local):

1. Copy `.env.example` to `.env` and set `GROQ_API_KEY` and `STABILITY_API_KEY`.
2. Start services with Docker Compose:

   docker compose up --build

3. Backend will be on http://localhost:8080 and frontend on http://localhost:3000

Example API calls:

- Text generation:
  curl -X POST http://localhost:8080/api/ai/text -H "Content-Type: application/json" -d '{"prompt":"Hello"}'

- PDF text extraction (multipart):
  curl -F "file=@./example.pdf" http://localhost:8080/api/doc/extract

Notes:
- Replace the placeholder endpoints in `AiService` with the exact Groq/Stability API endpoints and adjust payloads per their API docs.
- Keep your API keys secure (use GitHub Secrets for CI / deployment).


