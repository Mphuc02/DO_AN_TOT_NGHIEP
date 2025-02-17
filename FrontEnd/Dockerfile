# Dockerfile
FROM node:16 AS build

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép package.json và package-lock.json vào container
COPY package*.json ./

# Cài đặt các dependencies
RUN npm install

# Sao chép toàn bộ mã nguồn vào container
COPY . .

# Build ứng dụng ReactJS
RUN npm run build

# Chọn image Nginx để phục vụ ứng dụng ReactJS
FROM nginx:alpine

# Sao chép các file đã build vào thư mục mà Nginx sử dụng
COPY --from=build /app/build /usr/share/nginx/html

# Mở cổng 3000 thay vì cổng 80
EXPOSE 3000

# Lệnh khởi động Nginx với cổng 3000
CMD ["nginx", "-g", "daemon off;", "-p", "3000"]
