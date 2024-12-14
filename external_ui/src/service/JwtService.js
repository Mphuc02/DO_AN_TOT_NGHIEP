import axios from "axios";
import {AUTHENTICATION} from '../ApiConstant'
import header from "../layouts/header/admin/Header";

class JwtService{
    static geUserFromToken = () => {
        const token = localStorage.getItem('refresh-token'); // Lấy JWT từ localStorage

        if (token) {
            // Giải mã payload của JWT
            const decodedToken = JSON.parse(atob(token.split('.')[1])); // Decode payload của JWT

            // Kiểm tra token đã hết hạn chưa
            const currentTime = Math.floor(Date.now() / 1000); // Lấy thời gian hiện tại tính bằng giây
            if (decodedToken.exp && decodedToken.exp < currentTime) {
                console.log("Refresh Token đã hết hạn");
                localStorage.removeItem('refresh-token')
                localStorage.removeItem('access-token');
                localStorage.removeItem('receipt')
                localStorage.removeItem('doctor')
                localStorage.removeItem('patient')
                localStorage.removeItem('admin')
                return null;
            } else {
                // Token còn hiệu lực, thực hiện các thao tác cần thiết
                return decodedToken.userId
            }
        }
    }

    static exchangeToken = async () => {
        const token = localStorage.getItem('refresh-token');
        const tokenJson = JSON.stringify({
            token: token
        })
        await axios.post(AUTHENTICATION.exchangeToken(), tokenJson, {
                headers: {
                    'Content-Type': `application/json`,
            }})
            .then(response => {
                localStorage.setItem('access-token', response.data)
            })
            .catch(error => {
                console.error('API Error:', error);
            });
    }

    static checkTokenExpired = async (response) => {
        if(response.status === 401){
            if(response.response.data === 'Expired JWT token' || response.response.data === 'Invalid JWT token'){
                await this.exchangeToken()
                return true
            }
        }
        return false
    }

    static getAccessToken = async () => {
        let token = localStorage.getItem('access-token')
        if(token == null){
            await this.exchangeToken()
            token = localStorage.getItem('access-token')
        }
        return token
    }
}

export {JwtService}