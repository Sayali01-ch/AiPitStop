FROM node:18-alpine AS build
WORKDIR /app
COPY package.json package-lock.json* ./
# Use npm install with legacy-peer-deps to avoid strict peer resolution issues in this scaffold
RUN npm install --legacy-peer-deps --silent
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /app/nginx.conf
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh
EXPOSE 80
ENTRYPOINT ["/app/docker-entrypoint.sh"]
