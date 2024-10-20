import {JwtService} from "./JwtService";
import axios from "axios";

let countError = 0

class SendApiService{
    static getRequest = async (url, customHeaders, successCallback, errorCallback) => {
        if(countError === 5){
            countError = 0
            return
        }

        const token = await JwtService.getAccessToken()
        axios.get(url, {
            headers: {
                ...customHeaders,
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            console.log(response)
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                countError++
                await this.getRequest(url, customHeaders, successCallback, errorCallback)
            }
        })
    }

    static postRequest = async (url, data, customHeaders, successCallback, errorCallback) => {
        if(countError === 5){
            countError = 0
            return
        }

        const token = await JwtService.getAccessToken()
        axios.post(url, data, {
            headers: {
                ...customHeaders,
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            countError = 0
            console.log(response)
            successCallback(response)
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if(result){
                countError++
                await this.postRequest(url, data, successCallback, errorCallback)
            }
            errorCallback(error)
        })
    }
}

export {SendApiService}