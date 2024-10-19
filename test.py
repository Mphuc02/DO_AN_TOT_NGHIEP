import jwt
import datetime

# SECRET_KEY là khóa bí mật của bạn
SECRET_KEY = "PHUXUYENhanoib20dccn503D20CNPM6d20cqcn11bhospitalsystemDOANTOTNGHIEP2024DINHMINHPHUC24052002"

# Dữ liệu mà bạn muốn đưa vào token
payload = {
    "sub": "user_id_123",  # Subject, có thể là ID người dùng hoặc thông tin khác
    "iat": datetime.datetime.utcnow(),  # Thời gian token được phát hành
    "exp": datetime.datetime.utcnow() + datetime.timedelta(minutes=5)  # Thời gian hết hạn (5 phút sau)
}

# Ký token
token = jwt.encode(payload, SECRET_KEY, algorithm="HS512")

# In ra token
print("Generated JWT Token:", token)
