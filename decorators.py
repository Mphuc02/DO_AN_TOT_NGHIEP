import jwt
from functools import wraps
from flask import request, jsonify

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
            return jsonify({"message": "Token is missing!"}), 401

        try:
            print('hehe', token, "123")
            data = jwt.decode(token, SECRET_KEY, algorithms=["HS512"])
            current_user_id = data['userId']  # Extract userId from the token
        except jwt.ExpiredSignatureError:
            return jsonify({"message": "Expired JWT token"}), 401
        except jwt.InvalidTokenError:
            return jsonify({"message": "Invalid JWT token"}), 401

        # If token is valid, proceed with the route
        return f(current_user_id, *args, **kwargs)

    return decorated
