# FROM nginx:alpine

# # Chỉ copy phần client bundle
# COPY dist/book-client/browser /usr/share/nginx/html

# EXPOSE 80
# CMD ["nginx", "-g", "daemon off;"]

FROM node:18 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Stage 2: Run with Nginx
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Copy Angular browser build (frontend)
COPY --from=build /app/dist/book-client/browser /usr/share/nginx/html


