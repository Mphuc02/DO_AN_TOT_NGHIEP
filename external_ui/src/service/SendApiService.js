import {JwtService} from "./JwtService";
import axios from "axios";

let countError = 0

class SendApiService{
    static getRequest = async (url, customHeaders, successCallback, errorCallback) => {
        if (countError === 5) {
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
            successCallback(response)
        }).catch(async error => {
            console.log(error)
            const result = await JwtService.checkTokenExpired(error)
            if (result) {
                countError++
                await this.getRequest(url, customHeaders, successCallback, errorCallback)
            }
            if (error.status === 400) {
                errorCallback(error)
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
                await this.postRequest(url, data, customHeaders, successCallback, errorCallback)
            }
            errorCallback(error)
        })
    }

    static putRequest = async (url, data, customHeaders, successCallback, errorCallback) => {
        if(countError === 5){
            countError = 0
            return
        }

        const token = await JwtService.getAccessToken()
        axios.put(url, data, {
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
                await this.putRequest(url, data, customHeaders, successCallback, errorCallback)
            }
            errorCallback(error)
        })
    }

    static deleteRequest = async (url, customHeaders, successCallback, errorCallback) => {
        if(countError === 5){
            countError = 0
            return
        }

        const token = await JwtService.getAccessToken()
        console.log(token)
        axios.delete(url, {
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
                await this.deleteRequest(url, customHeaders, successCallback, errorCallback)
            }
            errorCallback(error)
        })
    }
}

export {SendApiService}