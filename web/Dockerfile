FROM node:20-alpine AS builder
WORKDIR /app
COPY . /app
ENV BACKEND_ADDRESS=typer-backend
RUN npm install
RUN npm run build

FROM nginx:latest
EXPOSE 4200
COPY --from=builder /app/dist/typer /usr/share/nginx/html
