import jwt
from functools import wraps
from flask import request, jsonify
import base64

# Secret key to decode the JWT
SECRET_KEY = "PHUXUYENhanoib20dccn503D20CNPM6d20cqcn11bhospitalsystemDOANTOTNGHIEP2024DINHMINHPHUC24052002"

def is_authenticated(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = None
        # Check if the request has the token in the headers
        if 'Authorization' in request.headers:
            token = request.headers['Authorization'].split(" ")[1]  # Typically "Bearer <token>"

        if not token:
            return "Token is missing!", 401

        try:
            data = jwt.decode(token, base64.b64decode(SECRET_KEY), algorithms=["HS512"])
            current_user_id = data['userId']  # Extract userId from the token
        except jwt.ExpiredSignatureError:
            return "Expired JWT token", 401
        except jwt.InvalidTokenError:
            return "Invalid JWT token", 401

        return f(current_user_id, *args, **kwargs)

    return decorated
