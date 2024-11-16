import os
from flask import Flask, request, jsonify, send_file, render_template
from flask_cors import CORS
from ultralytics import YOLO
import cv2
from PIL import Image
import numpy as np
import io
import base64
from kafka import KafkaProducer
import json
import py_eureka_client.eureka_client as eureka_client
import jwt  
from decorators import (is_authenticated)
from kafka import KafkaConsumer
import json
from uuid import UUID

eruekaServerPort = 5000

# Đăng ký dịch vụ với Eureka
eureka_client.init(
    eureka_server="http://localhost:8761/eureka",  # Địa chỉ của Eureka server
    app_name="AISERVICE",  # Tên dịch vụ
    instance_port=eruekaServerPort,
    instance_ip='127.0.0.1'
)

app = Flask(__name__)
# Load the YOLO model
model = YOLO('best.pt')

# Kết nối với Kafka server
producer = KafkaProducer(
    bootstrap_servers='localhost:9092',  # Địa chỉ Kafka broker
    value_serializer=lambda v: json.dumps(v).encode('utf-8')  # Định dạng JSON
)


# Cấu hình Kafka Consumer
consumer = KafkaConsumer(
    'request_detect_image', 
    bootstrap_servers=['localhost:9092'],  
    group_id='ai_service',
    value_deserializer=lambda m: json.loads(m.decode('utf-8')) 
)


CORS(app, resources={r"/api/v1/ai/upload": {"origins": "http://localhost:8081"}})

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/api/v1/ai/diagnostic', methods=['POST'])
# @is_authenticated
# def upload_image(user_id):
def upload_image():
    if 'image' not in request.files:
        return jsonify({"error": "No image file provided"}), 400

    file = request.files['image']
    if file.filename == '':
        return jsonify({"error": "No image file provided"}), 400

    # Read the image from the uploaded file
    img = Image.open(file.stream)
    img = cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)

    # Resize the image to 640x640
    img_resized = cv2.resize(img, (640, 640))

    # Run inference
    results = model([img_resized], iou=0.5)

    # List to hold detected diseases
    detected_diseases = set()

    # Process results
    for result in results:
        result_img = result.plot()  # Plotting the results

        # Trích xuất nhãn từ kết quả inference
        boxes = result.boxes  # Get boxes attribute
        for cls in boxes.cls:  # cls holds the class index
            disease_name = result.names[int(cls)]  # Lấy tên bệnh từ class index
            detected_diseases.add(disease_name)  # Thêm tên bệnh vào danh sách

    # Convert to PIL Image and save to BytesIO
    result_pil_img = Image.fromarray(cv2.cvtColor(result_img, cv2.COLOR_BGR2RGB))
    img_io = io.BytesIO()
    result_pil_img.save(img_io, 'JPEG', quality=70)
    img_io.seek(0)

    # Encode image to base64
    img_base64 = base64.b64encode(img_io.getvalue()).decode('utf-8')

    # Tạo response với thông tin bệnh phát hiện được
    response = {
        "decodedImg": img_base64,
        "diseases": list(detected_diseases)  # Danh sách các bệnh phát hiện
        # "owner": user_id
    }

    # # Gửi thông điệp tới Kafka topic
    # print('response:', response)
    # headers = [('__TypeId__', b'dev.common.model.ProcessedImageData')]
    # producer.send('processed-image', value=response, headers=headers)  
    # producer.flush() 
    return jsonify(response)

# Xử lý message từ Kafka
for message in consumer:
    try:
        data = message.value
        id = data.get('id')
        image = data.get('image')
        senderId = data.get('senderId')
        receiverId = data.get('receiverId')

        # Hiển thị thông tin nhận được
        print(f"Received message with ID: {id}")
        print(f"Image Data: {image}")
        print(f"Sender ID: {senderId}")
        print(f"Receiver ID: {receiverId}")
        
        # Chuyển đổi ảnh từ base64 thành ảnh PIL
        if image.startswith("data:image"):
            image = image.split(",", 1)[1]  # Remove the prefix before the comma
        image_data = base64.b64decode(image)
        img = Image.open(io.BytesIO(image_data))
        img = cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)

        # Resize the image to 640x640
        img_resized = cv2.resize(img, (640, 640))

        # Run inference (giả sử model đã được load trước đó)
        results = model([img_resized], iou=0.5)

        # List to hold detected diseases
        detected_diseases = set()

        # Process results
        for result in results:
            result_img = result.plot()  # Plotting the results

            # Trích xuất nhãn từ kết quả inference
            boxes = result.boxes  # Get boxes attribute
            for cls in boxes.cls:  # cls holds the class index
                disease_name = result.names[int(cls)]  # Lấy tên bệnh từ class index
                detected_diseases.add(disease_name)  # Thêm tên bệnh vào danh sách

        # Convert to PIL Image and save to BytesIO
        result_pil_img = Image.fromarray(cv2.cvtColor(result_img, cv2.COLOR_BGR2RGB))
        img_io = io.BytesIO()
        result_pil_img.save(img_io, 'JPEG', quality=70)
        img_io.seek(0)

        # Encode image to base64
        processedImage = 'data:image/jpeg;base64,' + base64.b64encode(img_io.getvalue()).decode('utf-8')
        response = {
            "id": id,
            "image": image,
            "processedImage": processedImage,
            "senderId": senderId,
            "receiverId": receiverId
        }  
        headers = [('__TypeId__', b'dev.common.dto.response.chat.DetectedImageChatResponse')]
        producer.send('detected_image', value=response, headers=headers)  
        producer.flush()  

    except (ValueError, TypeError, json.JSONDecodeError) as e:
        print(f"Error processing message: {e}")

if __name__ == '__main__':
    app.run(host='0.0.0.0' , debug=True)