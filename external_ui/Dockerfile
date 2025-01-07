# Sử dụng image Node.js chính thức làm base image
FROM node:16 AS build

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép package.json và package-lock.json vào container
COPY package*.json ./

# Cài đặt các dependencies
RUN npm install

# Sao chép mã nguồn React vào container
COPY . .

# Build ứng dụng React
RUN npm run build

# Sử dụng nginx để phục vụ ứng dụng React đã build
FROM nginx:alpine

# Sao chép các file build vào thư mục public của nginx
COPY --from=build /app/build /usr/share/nginx/html

# Mở port 80 để phục vụ ứng dụng
EXPOSE 80

# Khởi động nginx khi container chạy
CMD ["nginx", "-g", "daemon off;"]
