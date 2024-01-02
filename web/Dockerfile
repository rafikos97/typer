FROM node:20-alpine AS builder
WORKDIR /app
COPY . /app
RUN npm install
RUN npm run build

FROM nginx:latest
ENV BACKEND_ADDRESS=typer-backend
RUN mkdir /etc/nginx/templates
COPY default.conf.template /etc/nginx/templates
EXPOSE 4200
COPY --from=builder /app/dist/typer /usr/share/nginx/html
