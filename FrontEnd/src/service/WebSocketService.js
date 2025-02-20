import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {WEBSOCKET} from "../ApiConstant";
import {JwtService} from "./JwtService";

class WebSocketService{
    constructor() {
        const socket = new SockJS(WEBSOCKET.getUrl());
        this.stompClient = Stomp.over(() => socket);
        this.stompClient.activate();
    }

    connectAndSubscribe = (topics, callBack) => {
        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            topics.forEach((topic, index) => {
                this.stompClient.subscribe(topic, (msg) => {
                    if (msg.body) {
                        callBack(msg.body, index)
                    }
                });
            })
        });
    }

    disconnect = () => {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.disconnect(() => {
                console.log('Disconnected from WebSocket');
            });
        } else {
            console.log('STOMP client is not connected');
        }
    }

    sendMessage = (message, topic) => {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.send(topic, {senderId: JwtService.geUserFromToken()}, message);
        } else {
            console.log('STOMP client is not connected');
        }
    }
}

export default WebSocketService